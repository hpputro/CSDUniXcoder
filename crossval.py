print("import 1")
import pandas as pd
import numpy as np
import torch
from torch.utils.data import Dataset
import os

print("import 2")
from transformers import Trainer, TrainingArguments, AutoTokenizer, AutoModelForSequenceClassification
from sklearn.metrics import classification_report, accuracy_score, precision_recall_fscore_support, confusion_matrix
from sklearn.model_selection import StratifiedKFold

MAX_LENGTH: int = 1024
FILEDS: str = 'switch_statements_1024.csv'

from transformers import TrainerCallback
class TrainAccuracyCallback(TrainerCallback):
    def on_log(self, args, state, control, logs=None, model=None, **kwargs):
        train_dataloader = kwargs.get("train_dataloader", None)
        if train_dataloader is not None:
            model.eval()
            batch = next(iter(train_dataloader))
            inputs = {k: v.to(model.device) for k, v in batch.items() if k != "labels"}
            labels = batch["labels"].to(model.device)

            with torch.no_grad():
                outputs = model(**inputs)
            logits = outputs.logits
            preds = torch.argmax(logits, dim=-1)
            acc = accuracy_score(labels.cpu().numpy(), preds.cpu().numpy())
            logs["train_accuracy"] = acc

class CodeDataset(Dataset):
    def __init__(self, dataframe, tokenizer, device):
        self.dataframe = dataframe.reset_index(drop=True)
        self.tokenizer = tokenizer
        self.device = device

    def __len__(self):
        return len(self.dataframe)

    def __getitem__(self, idx):
        file_path = "finalsrc/"+self.dataframe.iloc[idx]['filename']
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

# Load dataset
dataset = pd.read_csv(FILEDS)
dataset = dataset[dataset['length'] < MAX_LENGTH]
dataset = dataset[['id', 'filename', 'label']].reset_index(drop=True)
print(dataset['label'].value_counts())

device = "cuda" if torch.cuda.is_available() else "cpu"
model_name = "microsoft/unixcoder-base"
tokenizer = AutoTokenizer.from_pretrained(model_name)
print("Device: "+device+"\n")

all_true = []
all_pred = []
fold_metrics = []
target_names=["0", "1"]

log_path = "output_log_crossval.txt"
with open(log_path, "w", encoding="utf-8") as f:
    f.write("uniXCoder 5-Fold Cross-Validation Results\n")

skf = StratifiedKFold(n_splits=5, shuffle=True, random_state=0)
for fold_idx, (train_idx, val_idx) in enumerate(skf.split(dataset, dataset['label']), start=1):
    print(f"\n===== Fold {fold_idx} / 5 =====")
    train_data = dataset.iloc[train_idx].reset_index(drop=True)
    val_data = dataset.iloc[val_idx].reset_index(drop=True)

    # Save per-fold train/val splits to CSV
    fold_dir = os.path.join("results", f"fold_{fold_idx}")
    os.makedirs(fold_dir, exist_ok=True)
    train_csv_path = os.path.join(fold_dir, "train.csv")
    val_csv_path = os.path.join(fold_dir, "val.csv")
    train_data.to_csv(train_csv_path, index=False)
    val_data.to_csv(val_csv_path, index=False)
    print("Train label distribution:\n", train_data['label'].value_counts())
    print("Val label distribution:\n", val_data['label'].value_counts())

    train_dataset = CodeDataset(train_data, tokenizer, device)
    val_dataset = CodeDataset(val_data, tokenizer, device)
    model = AutoModelForSequenceClassification.from_pretrained(
        model_name, num_labels=2, problem_type="single_label_classification"
    )
    model.to(device)

    training_args = TrainingArguments(
        output_dir=f"./results/fold_{fold_idx}",
        overwrite_output_dir=True,
        eval_strategy="epoch",
        logging_strategy="epoch",
        save_strategy="epoch",
        per_device_train_batch_size=16,
        per_device_eval_batch_size=16,
        num_train_epochs=20,
        learning_rate=1e-5,
        warmup_steps=50,
        lr_scheduler_type="cosine",
        save_total_limit=10,
        fp16=torch.cuda.is_available(),
        report_to="none",
        seed=0
    )

    trainer = Trainer(
        model=model,
        args=training_args,
        train_dataset=train_dataset,
        eval_dataset=val_dataset,
        compute_metrics=compute_metrics,
        callbacks=[TrainAccuracyCallback()]
    )

    print("Training Fold", fold_idx)
    trainer.train()

    # Validation predictions for this fold
    final_predictions = trainer.predict(val_dataset)
    preds = np.argmax(final_predictions.predictions, axis=-1)
    true_labels = val_data['label'].values
    all_true.append(true_labels)
    all_pred.append(preds)

    # Per-fold report
    val_report = classification_report(true_labels, preds, target_names=target_names, zero_division=0)
    cm_val = confusion_matrix(true_labels, preds)
    print(f"Validation Report (Fold {fold_idx})\n{val_report}")
    print("Validation Confusion Matrix (Fold {}):".format(fold_idx))
    print(cm_val)

    # Optional: also evaluate on train set per fold (to mirror main.py)
    train_preds = trainer.predict(train_dataset)
    train_preds_labels = np.argmax(train_preds.predictions, axis=-1)
    train_true_labels = train_data['label'].values
    train_report = classification_report(train_true_labels, train_preds_labels, target_names=target_names, zero_division=0)
    cm_train = confusion_matrix(train_true_labels, train_preds_labels)
    print(f"Training Report (Fold {fold_idx})\n{train_report}")
    print("Training Confusion Matrix (Fold {}):".format(fold_idx))
    print(cm_train)

    # Store summary metrics for averaging
    pr, rc, f1, _ = precision_recall_fscore_support(true_labels, preds, average="weighted", zero_division=0)
    acc = accuracy_score(true_labels, preds)
    fold_metrics.append({
        "fold": fold_idx,
        "accuracy": acc,
        "precision": pr,
        "recall": rc,
        "f1": f1
    })

# Aggregate across all folds
all_true = np.concatenate(all_true)
all_pred = np.concatenate(all_pred)
overall_report = classification_report(all_true, all_pred, target_names=target_names, zero_division=0)
overall_cm = confusion_matrix(all_true, all_pred)

avg_acc = np.mean([m["accuracy"] for m in fold_metrics])
avg_pr = np.mean([m["precision"] for m in fold_metrics])
avg_rc = np.mean([m["recall"] for m in fold_metrics])
avg_f1 = np.mean([m["f1"] for m in fold_metrics])

print("\n===== Cross-Validation Summary =====")
print(f"Avg Accuracy: {avg_acc:.4f}")
print(f"Avg Precision: {avg_pr:.4f}")
print(f"Avg Recall: {avg_rc:.4f}")
print(f"Avg F1: {avg_f1:.4f}")
print("Overall Report (concatenated predictions):\n" + overall_report)
print("Overall Confusion Matrix:")
print(overall_cm)

with open(log_path, "a", encoding="utf-8") as f:
    f.write("\n===== Cross-Validation Summary =====\n")
    f.write(f"Avg Accuracy: {avg_acc:.4f}\n")
    f.write(f"Avg Precision: {avg_pr:.4f}\n")
    f.write(f"Avg Recall: {avg_rc:.4f}\n")
    f.write(f"Avg F1: {avg_f1:.4f}\n")
    f.write("Overall Report (concatenated predictions):\n")
    f.write(overall_report + "\n")
    f.write("Overall Confusion Matrix:\n")
    f.write(str(overall_cm) + "\n")
