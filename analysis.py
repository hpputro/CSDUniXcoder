print("start importing libraries")
import pandas as pd
import numpy as np
import torch
print("importing libraries") 
import matplotlib.pyplot as plt

MAX_LENGTH: int = 1024
FILEDS: str = 'switch_statements.csv'

from torch.utils.data import Dataset
class CodeDataset(Dataset):
    def __init__(self, dataframe, tokenizer, device):
        self.dataframe = dataframe.reset_index(drop=True)
        self.tokenizer = tokenizer
        self.device = device

    def __len__(self):
        return len(self.dataframe)

    def __getitem__(self, idx):
        file_path = "newsrc/"+self.dataframe.iloc[idx]['filename']
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

import random
def set_seed(seed):
    random.seed(seed)
    np.random.seed(seed)
    torch.manual_seed(seed)
    torch.cuda.manual_seed_all(seed)
set_seed(0)

import os
print("Current working directory:", os.getcwd())
device = "cuda" if torch.cuda.is_available() else "cpu"
dataset = pd.read_csv(FILEDS)
print("Dataset columns:", dataset.columns) 
dataset = dataset[['id', 'filename', 'length', 'label']]
print(dataset['label'].value_counts())
split_index = int(0.8 * len(dataset))
train_data = dataset.iloc[:split_index].reset_index(drop=True)
val_data = dataset.iloc[split_index:].reset_index(drop=True)

# Barchart persebaran jumlah file per 100 token, label x tiap 200 token
bin_size = 100
max_token = int(dataset['length'].max())
bins = np.arange(0, max_token + bin_size, bin_size)
counts, edges = np.histogram(dataset['length'], bins=bins)
bin_centers = [(edges[i] + edges[i+1]) / 2 for i in range(len(edges)-1)]

# Plot bar chart with vertical line at x=1024
plt.figure(figsize=(10,6))
plt.bar(bin_centers, counts, width=bin_size * 0.9, color='#81957F')
plt.xlabel("Token Count")
plt.ylabel("Number of Files")
plt.title("File Count per 100 Token Range")
xtick_vals = np.arange(200, max(bin_centers)+1, 200)
xtick_labels = [str(int(v)) for v in xtick_vals]
plt.xticks(xtick_vals, xtick_labels, rotation=45)
plt.axvline(x=1024, color='gray', linestyle='dashed', linewidth=1)
plt.tight_layout()
plt.show()


# Stacked bar chart for label distribution in train and validation sets
label_names = {0: "Clean Code", 1: "Smell Code"}
train_counts = train_data['label'].value_counts().sort_index()
val_counts = val_data['label'].value_counts().sort_index()
labels = [0, 1]
bar_labels = [label_names.get(i, str(i)) for i in labels]
train_bar = [train_counts.get(i, 0) for i in labels]
val_bar = [val_counts.get(i, 0) for i in labels]
plt.figure(figsize=(6,4))
plt.bar(bar_labels, train_bar, color=['#2A3B36', '#2A3B36'], edgecolor='black', label='Train')
plt.bar(bar_labels, val_bar, bottom=train_bar, color=['#D7EEC8', '#D7EEC8'], edgecolor='black', label='Validation')
plt.xlabel("Label")
plt.ylabel("Number of Samples")
plt.title("Dataset Distribution: Clean Code vs Smell Code (Stacked Train/Val)")
plt.legend()
plt.tight_layout()
plt.show()


# Stacked histogram for token count (length) by label
plt.figure(figsize=(7,4))
bins = range(0, int(dataset['length'].max()) + 100, 100)
labels = [0, 1]
colors = ['#A9C6D9', '#E9CBAA']
label_names = {0: "Clean Code", 1: "Smell Code"}
data_by_label = [dataset[dataset['label'] == l]['length'] for l in labels]
plt.axvline(x=1024, color='gray', linestyle='dashed', linewidth=1)

plt.hist(
    data_by_label,
    bins=bins,
    stacked=True,
    color=colors,
    label=[label_names[l] for l in labels],
    edgecolor='black'
)
plt.xlabel("Token Count (length)")
plt.ylabel("Number of Samples")
plt.title("Stacked Token Count Distribution per File by Label")
plt.legend()
plt.tight_layout()
plt.show()