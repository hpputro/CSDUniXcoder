print("import 1")
import pandas as pd
import numpy as np

import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.utils.data import Dataset

print("import 2")
from transformers import Trainer, TrainingArguments, AutoTokenizer, AutoModelForSequenceClassification
from sklearn.utils.class_weight import compute_class_weight
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report, accuracy_score, precision_recall_fscore_support, confusion_matrix

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

    def on_epoch_end(self, args, state, control, **kwargs):
        trainer = kwargs.get("trainer")
        if trainer is not None:
            train_metrics = trainer.evaluate(trainer.train_dataset, metric_key_prefix="train")
            trainer.log({"train_accuracy": train_metrics.get("train_accuracy", None)})

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
    preds = logits.argmax(axis=-1)  # Ambil kelas dengan probabilitas tertinggi
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

dataset = pd.read_csv(FILEDS)
dataset = dataset[dataset['length'] < MAX_LENGTH]
dataset = dataset[['id', 'filename', 'label']]
print(dataset['label'].value_counts())

print("split")
split_index = int(0.8 * len(dataset))
train_data = dataset.iloc[:split_index].reset_index(drop=True)
val_data = dataset.iloc[split_index:].reset_index(drop=True)
print(train_data['label'].value_counts())
print(val_data['label'].value_counts())

device = "cuda" if torch.cuda.is_available() else "cpu"
model_name = "microsoft/unixcoder-base"
tokenizer = AutoTokenizer.from_pretrained(model_name)
train_dataset = CodeDataset(train_data, tokenizer, device)
val_dataset = CodeDataset(val_data, tokenizer, device)
all_dataset = CodeDataset(dataset, tokenizer, device)
print("Device: "+device+"\n")

model = AutoModelForSequenceClassification.from_pretrained(model_name, 
    num_labels=2, problem_type="single_label_classification")
model.to(device)
training_args = TrainingArguments(
    output_dir="./results",
    overwrite_output_dir=True,
    eval_strategy="epoch",
    logging_strategy="epoch",
    save_strategy="epoch",
    per_device_train_batch_size=16,
    per_device_eval_batch_size=16,  
    num_train_epochs=20,
    learning_rate=1e-6,
    warmup_steps=50,
    lr_scheduler_type="cosine",
    save_total_limit=2, 
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

print("Training")
trainer.train()

final_predictions = trainer.predict(val_dataset)
preds = np.argmax(final_predictions.predictions, axis=-1)
true_labels = val_data['label'].values

target_names=["0", "1"]
report = classification_report(true_labels, preds, target_names=target_names, zero_division=0)
print(f"Code Smell using uniXCoder Classification Report\n {report}")
print("Validation Confusion Matrix:")
print(confusion_matrix(true_labels, preds))

print("Training Confusion Matrix:")
train_preds = trainer.predict(train_dataset)
train_preds_labels = np.argmax(train_preds.predictions, axis=-1)
train_true_labels = train_data['label'].values
print(confusion_matrix(train_true_labels, train_preds_labels))

with open("output_log.txt", "w", encoding="utf-8") as f:
    f.write(f"Code Smell using uniXCoder Classification Report\n {report}\n")
    f.write("Validation Confusion Matrix:\n")
    f.write(str(confusion_matrix(true_labels, preds)) + "\n")
    f.write("Training Confusion Matrix:\n")
    f.write(str(confusion_matrix(train_true_labels, train_preds_labels)) + "\n\n")
    f.write(str(training_args) + "\n")