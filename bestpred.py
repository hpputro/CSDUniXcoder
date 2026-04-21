#crossval in a checkpoint and reporting each fold
print("import 1")
import os
import pandas as pd
import numpy as np
import sklearn.metrics as met
import torch
import time
from torch.utils.data import Dataset
from datetime import datetime

print("import 2")
from transformers import Trainer, TrainingArguments, AutoTokenizer, AutoModelForSequenceClassification
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.model_selection import StratifiedKFold

MAX_LENGTH: int = 1024
MODEL_NAME: str = "Salesforce/codet5-base"
FILEDS: str = 'switch_statements_1024.csv'
SPLIT: int = 5
BEST_EPOCH = 140

class CodeDataset(Dataset):
    def __init__(self, dataframe, tokenizer, device):
        self.dataframe = dataframe.reset_index(drop=True)
        self.tokenizer = tokenizer
        self.device = device

    def __len__(self):
        return len(self.dataframe)

    def __getitem__(self, idx):
        file_path = "finalsrc/" + self.dataframe.iloc[idx]['filename']
        label = self.dataframe.iloc[idx]['label']
        with open(file_path, 'r', encoding='utf-8') as f:
            code = f.read()

        encoding = self.tokenizer.encode_plus(
            code,
            add_special_tokens=True,
            max_length=MAX_LENGTH,
            padding='max_length',
            truncation=True,
            return_tensors='pt',
        )

        return {
            'input_ids': encoding['input_ids'].flatten().to(self.device),
            'attention_mask': encoding['attention_mask'].flatten().to(self.device),
            'labels': torch.tensor(label, dtype=torch.long).to(self.device)
        }

def extract_logits(predictions):
    if isinstance(predictions, tuple):
        predictions = predictions[0]
    return predictions

def compute_metrics(eval_pred):
    logits, labels = eval_pred
    logits = extract_logits(logits)
    preds = logits.argmax(axis=-1)

    accuracy = met.accuracy_score(labels, preds)
    precision = met.precision_score(labels, preds, zero_division=0)
    recall =  met.recall_score(labels, preds, zero_division=0)
    f1 = met.f1_score(labels, preds, zero_division=0)
    return {
        "accuracy": accuracy,
        "precision": precision,
        "recall": recall,
        "f1": f1
    }

import random
def set_seed(seed):
    random.seed(seed)
    np.random.seed(seed)
    torch.manual_seed(seed)
    torch.cuda.manual_seed_all(seed)
set_seed(0)

# Load dataset and build folds identical to training
dataset = pd.read_csv(FILEDS)
dataset = dataset[dataset['length'] < MAX_LENGTH]
dataset = dataset[['id', 'filename', 'label']].reset_index(drop=True)
print(dataset['label'].value_counts())

device = "cuda" if torch.cuda.is_available() else "cpu"
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
print("Device: " + device + "\n")

all_true = []
all_pred = []
all_prob = []
all_fold = []
fold_metrics = []
target_names = ["0", "1"]

model_name_clean = MODEL_NAME.split('/')[-1]
log_path = f"log_{model_name_clean}_ckpt{BEST_EPOCH}.txt"
run_timestamp = datetime.now().strftime("%d/%m/%Y %H:%M")
with open(log_path, "w", encoding="utf-8") as f:
    f.write(f"{MODEL_NAME} Checkpoint-{BEST_EPOCH} Inference over {SPLIT}-Fold\n")
    f.write(f"Run Timestamp: {run_timestamp}\n")

skf = StratifiedKFold(n_splits=SPLIT, shuffle=True, random_state=0)
for fold_idx, (train_idx, val_idx) in enumerate(skf.split(dataset, dataset['label']), start=1):
    print(f"\n===== Fold {fold_idx} / {SPLIT} {MODEL_NAME} Checkpoint {BEST_EPOCH} =====")
    val_data = dataset.iloc[val_idx].reset_index(drop=True)
    val_dataset = CodeDataset(val_data, tokenizer, device)

    # Locate fixed checkpoint-X for this fold
    fold_dir = os.path.join("results", f"fold_{fold_idx}")
    ckpt_path = os.path.join(fold_dir, f"checkpoint-{BEST_EPOCH}")
    if not os.path.isdir(ckpt_path):
        raise FileNotFoundError(f"Expected checkpoint not found: {ckpt_path}.")
    print(f"Using checkpoint: {ckpt_path}")

    # Load model from the checkpoint
    model = AutoModelForSequenceClassification.from_pretrained(ckpt_path)
    model.to(device)
    try:
        tokenizer = AutoTokenizer.from_pretrained(ckpt_path)
    except Exception:
        tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)

    # Minimal training args for prediction only
    pred_output_dir = os.path.join(fold_dir, f"pred_ckpt{BEST_EPOCH}")
    os.makedirs(pred_output_dir, exist_ok=True)
    training_args = TrainingArguments(
        output_dir=pred_output_dir,
        per_device_eval_batch_size=16,
        dataloader_drop_last=False,
        fp16=torch.cuda.is_available(),
        report_to="none",
        seed=0,
    )

    trainer = Trainer(
        model=model,
        args=training_args,
        eval_dataset=val_dataset,
        compute_metrics=compute_metrics,
    )

    pred_start_time = time.time()
    final_predictions = trainer.predict(val_dataset)
    pred_end_time = time.time()
    total_pred_time = pred_end_time - pred_start_time
    ns = len(val_dataset)
    avg_latency = total_pred_time / ns
    throughput = ns / total_pred_time
    print(f"Fold {fold_idx}: {total_pred_time:.4f} s, {avg_latency:.6f} s/sample, {throughput:.2f} samples/s")

    logits_tensor = torch.tensor(extract_logits(final_predictions.predictions))
    probs = torch.nn.functional.softmax(logits_tensor, dim=-1).numpy()
    y_scores = probs[:, 1]
    
    preds = np.argmax(extract_logits(final_predictions.predictions), axis=-1)
    true_labels = val_data['label'].values
    all_true.append(true_labels)
    all_pred.append(preds)
    all_prob.append(y_scores)
    all_fold.append(np.full(len(true_labels), fold_idx))

    # Per-fold report
    val_report = classification_report(true_labels, preds, target_names=target_names, zero_division=0)
    cm_val = confusion_matrix(true_labels, preds)
    print(f"Validation Report (Fold {fold_idx})\n{val_report}")
    print("Validation Confusion Matrix (Fold {}):".format(fold_idx))
    print(cm_val)

    acc = met.accuracy_score(true_labels, preds)
    pr = met.precision_score(true_labels, preds, zero_division=0)
    rc =  met.recall_score(true_labels, preds, zero_division=0)
    f1 = met.f1_score(true_labels, preds, zero_division=0)
    fold_metrics.append({
        "fold": fold_idx,
        "accuracy": acc,
        "precision": pr,
        "recall": rc,
        "f1": f1,
        "nsamples": ns,
        "time": total_pred_time
    })

# Aggregate across all folds
all_true = np.concatenate(all_true)
all_pred = np.concatenate(all_pred)
all_prob = np.concatenate(all_prob)
all_fold = np.concatenate(all_fold)
all_report = classification_report(all_true, all_pred, target_names=target_names, zero_division=0)
all_cm = confusion_matrix(all_true, all_pred)

all_acc = met.accuracy_score(all_true, all_pred)
all_pr = met.precision_score(all_true, all_pred, zero_division=0)
all_rc =  met.recall_score(all_true, all_pred, zero_division=0)
all_f1 = met.f1_score(all_true, all_pred, zero_division=0)

all_ns = np.sum([m["nsamples"] for m in fold_metrics])
all_time = np.sum([m["time"] for m in fold_metrics])
avg_latency = all_time / all_ns
throughput = all_ns / all_time

print(f"\n===== Checkpoint-{BEST_EPOCH} Cross-Validation Summary =====")
print(f"Avg Accuracy: {all_acc:.4f}")
print(f"Avg Precision: {all_pr:.4f}")
print(f"Avg Recall: {all_rc:.4f}")
print(f"Avg F1: {all_f1:.4f}")
print(f"Total prediction time: {all_time:.4f} seconds")
print(f"Average latency per sample: {avg_latency:.4f} seconds/sample")
print(f"Throughput: {throughput:.2f} samples/second")
print("Overall Report (concatenated predictions):\n" + all_report)
print("Overall Confusion Matrix:")
print(all_cm)

with open(log_path, "a", encoding="utf-8") as f:
    f.write(f"\n===== Checkpoint-{BEST_EPOCH} Cross-Validation Summary =====\n")
    f.write(f"Avg Accuracy: {all_acc:.4f}\n")
    f.write(f"Avg Precision: {all_pr:.4f}\n")
    f.write(f"Avg Recall: {all_rc:.4f}\n")
    f.write(f"Avg F1: {all_f1:.4f}\n")
    print(f"Total prediction time: {all_time:.4f} seconds")
    print(f"Average latency per sample: {avg_latency:.4f} seconds/sample")
    print(f"Throughput: {throughput:.2f} samples/second")
    f.write("Overall Report (concatenated predictions):\n")
    f.write(all_report + "\n")
    f.write("Overall Confusion Matrix:\n")
    f.write(str(all_cm) + "\n")

pred_csv_path = f"truepred_{model_name_clean}_ckpt{BEST_EPOCH}.csv"
pd.DataFrame({
    "all_fold": all_fold,
    "all_true": all_true,
    "all_pred": all_prob
}).to_csv(pred_csv_path, index=False, encoding="utf-8")
