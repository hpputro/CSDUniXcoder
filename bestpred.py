#crossval in a checkpoint and reporting each fold
print("import 1")
import os
import pandas as pd
import numpy as np
import torch
from torch.utils.data import Dataset
import time
from datetime import datetime

print("import 2")
from transformers import Trainer, TrainingArguments, AutoTokenizer, AutoModelForSequenceClassification
from sklearn.metrics import classification_report, accuracy_score, precision_recall_fscore_support, confusion_matrix
from sklearn.model_selection import StratifiedKFold

MAX_LENGTH: int = 1024
FILEDS: str = 'switch_statements_1024.csv'

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

def compute_metrics(eval_pred):
    logits, labels = eval_pred
    preds = logits.argmax(axis=-1)
    accuracy = accuracy_score(labels, preds)
    precision, recall, f1, _ = precision_recall_fscore_support(labels, preds, average="weighted")
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
model_name = "microsoft/unixcoder-base"
tokenizer = AutoTokenizer.from_pretrained(model_name)
print("Device: " + device + "\n")

all_true = []
all_pred = []
all_fold = []
fold_metrics = []
target_names = ["0", "1"]

best_epoch = 180
log_path = f"output_log_ckpt{best_epoch}.txt"
run_timestamp = datetime.now().strftime("%d/%m/%Y %H:%M")
with open(log_path, "w", encoding="utf-8") as f:
    f.write(f"uniXCoder Checkpoint-{best_epoch} Inference over 5-Fold\n")
    f.write(f"Run Timestamp: {run_timestamp}\n")
    
skf = StratifiedKFold(n_splits=5, shuffle=True, random_state=0)
for fold_idx, (train_idx, val_idx) in enumerate(skf.split(dataset, dataset['label']), start=1):
    print(f"\n===== Fold {fold_idx} / 5 Checkpoint {best_epoch} =====")
    val_data = dataset.iloc[val_idx].reset_index(drop=True)
    val_dataset = CodeDataset(val_data, tokenizer, device)

    # Locate fixed checkpoint-X for this fold
    fold_dir = os.path.join("results", f"fold_{fold_idx}")
    ckpt_path = os.path.join(fold_dir, f"checkpoint-{best_epoch}")
    if not os.path.isdir(ckpt_path):
        raise FileNotFoundError(f"Expected checkpoint not found: {ckpt_path}.")
    print(f"Using checkpoint: {ckpt_path}")

    # Load model from the checkpoint
    model = AutoModelForSequenceClassification.from_pretrained(ckpt_path)
    model.to(device)
    try:
        tokenizer = AutoTokenizer.from_pretrained(ckpt_path)
    except Exception:
        tokenizer = AutoTokenizer.from_pretrained(model_name)

    # Minimal training args for prediction only
    pred_output_dir = os.path.join(fold_dir, f"pred_ckpt{best_epoch}")
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

    preds = np.argmax(final_predictions.predictions, axis=-1)
    true_labels = val_data['label'].values
    all_true.append(true_labels)
    all_pred.append(preds)
    all_fold.append(np.full(len(true_labels), fold_idx))

    # Per-fold report
    val_report = classification_report(true_labels, preds, target_names=target_names, zero_division=0)
    cm_val = confusion_matrix(true_labels, preds)
    print(f"Validation Report (Fold {fold_idx})\n{val_report}")
    print("Validation Confusion Matrix (Fold {}):".format(fold_idx))
    print(cm_val)

    pr, rc, f1, _ = precision_recall_fscore_support(true_labels, preds, average="weighted", zero_division=0)
    acc = accuracy_score(true_labels, preds)
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
all_fold = np.concatenate(all_fold)
all_report = classification_report(all_true, all_pred, target_names=target_names, zero_division=0)
all_cm = confusion_matrix(all_true, all_pred)

all_acc = np.mean([m["accuracy"] for m in fold_metrics])
all_pr = np.mean([m["precision"] for m in fold_metrics])
all_rc = np.mean([m["recall"] for m in fold_metrics])
all_f1 = np.mean([m["f1"] for m in fold_metrics])

all_ns = np.sum([m["nsamples"] for m in fold_metrics])
all_time = np.sum([m["time"] for m in fold_metrics])
avg_latency = all_time / all_ns
throughput = all_ns / all_time

print(f"\n===== Checkpoint-{best_epoch} Cross-Validation Summary =====")
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
    f.write(f"\n===== Checkpoint-{best_epoch} Cross-Validation Summary =====\n")
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

pred_csv_path = f"all_true_pred_ckpt{best_epoch}.csv"
pd.DataFrame({
    "all_fold": all_fold,
    "all_true": all_true,
    "all_pred": all_pred
}).to_csv(pred_csv_path, index=False, encoding="utf-8")
