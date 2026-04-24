import re
import json
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.metrics import auc, precision_recall_curve, roc_curve

def extract_from_json(json_path):
    with open(json_path, 'r') as f:
        data = json.load(f)

    metrics_dict = {}
    for entry in data.get('log_history', []):
        epoch = entry.get('epoch')
        if epoch is not None:
            if epoch not in metrics_dict:
                metrics_dict[epoch] = {'epoch': epoch}
                
            if 'loss' in entry:
                metrics_dict[epoch].update({
                    'loss': entry['loss'],
                    'learning_rate': entry.get('learning_rate'),
                    'train_accuracy': entry.get('accuracy')
                })

            if 'eval_loss' in entry:
                metrics_dict[epoch].update({
                    'eval_loss': entry['eval_loss'],
                    'eval_accuracy': entry.get('eval_accuracy'),
                    'eval_precision': entry.get('eval_precision'),
                    'eval_recall': entry.get('eval_recall'),
                    'eval_f1': entry.get('eval_f1')
                })

    df = pd.DataFrame(list(metrics_dict.values()))
    df.sort_values(by="epoch", inplace=True)
    return df

def extract_from_text(text_file):
    with open(text_file, "r") as f:
        lines = f.readlines()

    metrics_dict = {}
    for line in lines:
        train_match = re.search(
            r"\{'loss': ([\d\.e\-]+).*'learning_rate': ([\d\.e\-]+).*'epoch': ([\d\.]+).*'train_accuracy': ([\d\.]+)\}", line)
        if train_match:
            epoch = float(train_match.group(3))
            if epoch not in metrics_dict:
                metrics_dict[epoch] = {}
            metrics_dict[epoch].update({
                'epoch': epoch,
                'loss': float(train_match.group(1)),
                'learning_rate': float(train_match.group(2)),
                'train_accuracy': float(train_match.group(4)),
            })

        eval_match = re.search(
            r"\{'eval_loss': ([\d\.e\-]+), 'eval_accuracy': ([\d\.e\-]+), 'eval_precision': ([\d\.e\-]+), 'eval_recall': ([\d\.e\-]+), 'eval_f1': ([\d\.e\-]+).*'epoch': ([\d\.]+),.*\}",
            line)
        if eval_match:
            epoch = float(eval_match.group(6))
            if epoch not in metrics_dict:
                metrics_dict[epoch] = {}
            metrics_dict[epoch].update({
                'epoch': epoch,
                'eval_loss': float(eval_match.group(1)),
                'eval_accuracy': float(eval_match.group(2)),
                'eval_precision': float(eval_match.group(3)),
                'eval_recall': float(eval_match.group(4)),
                'eval_f1': float(eval_match.group(5)),
            })

    # Ubah ke DataFrame
    df = pd.DataFrame(list(metrics_dict.values()))
    df.sort_values(by="epoch", inplace=True)
    return df

def plot_accuracy(df):
    plt.figure(figsize=(8,5))
    plt.plot(df['epoch'], df['train_accuracy'], marker='o', label='Train Accuracy', color='#1565C0')
    plt.plot(df['epoch'], df['eval_accuracy'], marker='s', label='Validation Accuracy', color='#FFB300')
    plt.xlabel('Epoch')
    plt.ylabel('Accuracy')
    plt.title('Train vs Validation Accuracy per Epoch')
    plt.legend()
    plt.grid(True, linestyle='--', alpha=0.5)
    plt.xticks([x for x in range(int(min(df['epoch'])), int(max(df['epoch']))+1) if x % 2 == 0])
    plt.tight_layout()
    plt.show()

def plot_train(df):
    plt.figure(figsize=(8,5))
    plt.plot(df['epoch'], df['train_accuracy'], marker='d', label='Train Accuracy', color='#3F6E8F')
    plt.plot(df['epoch'], df['loss'], marker='s', label='Train Loss', color='#F4CFA4')
    plt.xlabel('Epoch')
    plt.ylabel('Accuracy / Loss')
    plt.title('Training Performance per Epoch')
    plt.legend()
    plt.grid(True, linestyle='--', alpha=0.5)
    plt.xticks([x for x in range(int(min(df['epoch'])), int(max(df['epoch']))+1) if x % 2 == 0])
    plt.tight_layout()
    plt.show()

def plot_eval(df):
    plt.figure(figsize=(8,5))
    plt.plot(df['epoch'], df['eval_accuracy'], marker='o', label='Validation Accuracy', color='#5D7080')
    plt.plot(df['epoch'], df['eval_loss'], marker='s', label='Validation Loss', color='#B58B6A')
    plt.xlabel('Epoch')
    plt.ylabel('Accuracy / Loss')
    plt.title('Validation Performance per Epoch')
    plt.legend()
    plt.grid(True, linestyle='--', alpha=0.5)
    plt.xticks([x for x in range(int(min(df['epoch'])), int(max(df['epoch']))+1) if x % 2 == 0])
    plt.tight_layout()
    plt.show()

def plot_trainval(df):
    plt.figure(figsize=(8,5))
    plt.plot(df['epoch'], df['train_accuracy'], marker='p', label='Train Accuracy', color='#d48989')
    plt.plot(df['epoch'], df['eval_accuracy'], marker='x', label='Validation Accuracy', color='#c7ab68')
    plt.plot(df['epoch'], df['loss'], marker='s', label='Train Loss', color='#397C9E')
    plt.plot(df['epoch'], df['eval_loss'], marker='d', label='Validation Loss', color='#5fae7c')
    plt.xlabel('Epoch')
    plt.ylabel('Accuracy/Loss')
    plt.title('Training Performance per Epoch')
    plt.legend()
    plt.grid(True, linestyle='--', alpha=0.5)
    plt.xticks([x for x in range(int(min(df['epoch'])), int(max(df['epoch']))+1) if x % 2 == 0])
    plt.tight_layout()
    plt.show()

def plot_metrics(df):
    plt.figure(figsize=(8,5))
    plt.plot(df['epoch'], df['eval_precision'], marker='p', label='Precision', color='#5D7080')
    plt.plot(df['epoch'], df['eval_recall'], marker='x', label='Recall', color='#2A3B36')
    plt.plot(df['epoch'], df['eval_f1'], marker='s', label='F1 Score', color='#B58B6A')
    plt.plot(df['epoch'], df['eval_accuracy'], marker='d', label='Accuracy', color='#8A5C5C')
    plt.xlabel('Epoch')
    plt.ylabel('Evaluation Metrics')
    plt.title('Accuracy vs Precision vs Recall vs F1 Score per Epoch')
    plt.legend()
    plt.grid(True, linestyle='--', alpha=0.5)
    plt.xticks([x for x in range(int(min(df['epoch'])), int(max(df['epoch']))+1) if x % 2 == 0])
    plt.tight_layout()
    plt.show()

def plot_over(dfs, metric, max_epochs=0):
    if max_epochs != 0:
        dfs = [df[df['epoch'] <= max_epochs] for df in dfs]

    plt.figure(figsize=(8,5))
    marker_bs = ['d', 'x', 's']
    marker_st = ['d', 's', 'x']
    marker_ls = ['x', 'd', '.']
    marker_ws = ['^', 's', 'x', 'd', 'v']
    marker_lr = ['^', 's', 'd', 'x', 'v']
    colors = [ '#d48989', '#c7ab68', '#5fae7c', '#397C9E','#883A9D']
    label_lr = ['1e-6', '5e-6', '8e-6', '1e-5', '2e-5']
    label_ws = ['0', '30', '50', '80', '100']
    label_bs = ['8', '16', '32']
    label_ls = ['0', '0.1', '0.2']
    label_st = ['linear', 'polynomial', 'cosine']
    label_fold = ['Fold 1', 'Fold 2', 'Fold 3', 'Fold 4', 'Fold 5']
    
    for i, df in enumerate(dfs):
        plt.plot(df['epoch'], df[metric], marker=marker_ws[i], label=label_fold[i], color=colors[i])
    plt.legend(title='K-Fold Cross Validation')

    plt.xlabel('Epoch')
    if metric == 'train_accuracy':
        metric = 'Training Accuracy'
    if metric == 'eval_accuracy':
        metric = 'Validation Accuracy'
    if metric == 'loss':
        metric = 'Training Loss'
    if metric == 'eval_loss':
        metric = 'Validation Loss'
    plt.ylabel(metric)

    plt.grid(True, linestyle='--', alpha=0.5)
    if max_epochs != 0 and len(dfs) > 0 and not dfs[0].empty:
        plt.xticks([x for x in range(int(min(dfs[0]['epoch'])), int(max(dfs[0]['epoch']))+1) if x <= max_epochs])
    else:
        plt.xticks([x for x in range(int(min(dfs[0]['epoch'])), int(max(dfs[0]['epoch']))+1)])
    plt.tight_layout()
    plt.show()

def plot_confusion_matrix(values, class_labels=None, normalize=False, title='Confusion Matrix'):
    matrix = np.array(values).reshape(2, 2)
    if normalize:
        row_sums = matrix.sum(axis=1, keepdims=True)
        row_sums[row_sums == 0] = 1
        matrix = matrix / row_sums

    fig, ax = plt.subplots(figsize=(6, 5))
    cmap = plt.cm.Blues
    im = ax.imshow(matrix, interpolation='nearest', cmap=cmap)
    fig.colorbar(im, ax=ax, fraction=0.046, pad=0.04)

    ax.set(title=title, xlabel='Predicted label', ylabel='True label')
    tick_marks = np.arange(matrix.shape[0])
    ax.set_xticks(tick_marks)
    ax.set_yticks(tick_marks)

    if class_labels:
        ax.set_xticklabels(class_labels, rotation=45, ha='right')
        ax.set_yticklabels(class_labels)
    else:
        default_labels = [f'Class {i}' for i in range(matrix.shape[0])]
        ax.set_xticklabels(default_labels, rotation=45, ha='right')
        ax.set_yticklabels(default_labels)

    fmt = '.2f' if normalize else 'd'
    thresh = matrix.max() / 2.0
    for i in range(matrix.shape[0]):
        for j in range(matrix.shape[1]):
            value = matrix[i, j]
            ax.text(
                j,
                i,
                format(value, fmt),
                ha='center',
                va='center',
                color='white' if value > thresh else 'black'
            )

    fig.tight_layout()
    plt.show()

def get_file(files):
    dfs = []
    for file in files:
        if file.endswith(".txt"):
            print(file)
            dfs.append(extract_from_text(file))
        elif file.endswith(".json"):
            dfs.append(extract_from_json(file))
    return dfs

def ROCCurve(data_dict):
    """Plot ROC Curves for multiple models in one chart"""
    fig, ax = plt.subplots(figsize=(8, 8))    
    colors = [ '#8C3A3A', '#e8bcbc', '#e3d6b0', '#bfe0cc', '#a9c9da','#d2b3db']
    
    for idx, (model_name, (y_true, y_pred)) in enumerate(data_dict.items()):
        fpr, tpr, _ = roc_curve(y_true, y_pred)
        roc_auc = auc(fpr, tpr)
        ax.plot(fpr, tpr, label=f'{model_name} (AUC = {roc_auc:.3f})', linewidth=2, color=colors[idx % len(colors)])
    
    ax.plot([0, 1], [0, 1], 'k--', label='Random Guess (AUC = 0.5)')
    ax.set_xlabel('False Positive Rate')
    ax.set_ylabel('True Positive Rate')
    ax.set_title('ROC Curve Comparison')
    ax.legend()
    ax.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.show()

def PrecisionRecallCurve(data_dict):
    """Plot Precision-Recall Curves for multiple models in one chart"""
    fig, ax = plt.subplots(figsize=(8, 8))
    colors = [ '#8C3A3A', '#e8bcbc', '#e3d6b0', '#bfe0cc', '#a9c9da','#d2b3db']
    
    for idx, (model_name, (y_true, y_pred)) in enumerate(data_dict.items()):
        precision, recall, _ = precision_recall_curve(y_true, y_pred)
        pr_auc = auc(recall, precision)
        ax.plot(recall, precision, label=f'{model_name} (AUC = {pr_auc:.3f})', linewidth=2, color=colors[idx % len(colors)])
    
    ax.axhline(y=0.5, color='k', linestyle='--', label='Baseline (Precision = 0.5)')
    ax.set_xlabel('Recall')
    ax.set_ylabel('Precision')
    ax.set_title('Precision-Recall Curve Comparison')
    ax.legend()
    ax.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.show()

df_truepred = [
    "truepred_UniXcoder.csv",
    "truepred_BoW.csv",
    "truepred_TF-IDF.csv",
    "truepred_CodeBERT.csv",
    "truepred_GraphCodeBERT.csv",
    "truepred_CodeT5.csv",
]

# Load all truepred data and prepare for comparison
data_dict = {}
for file in df_truepred:
    df = pd.read_csv(file)
    model_name = file.replace("truepred_", "").replace(".csv", "")
    data_dict[model_name] = (df['all_true'].values, df['all_pred'].values)

# Plot both models in one chart
ROCCurve(data_dict)
PrecisionRecallCurve(data_dict)

df_lr = [
    "remote/250822a_76.txt",
    "remote/250730b_77.txt",
    "remote/250731a_87.json",
    "remote/250723a_84.json",
    "remote/251002a_87.txt",
]
df_bs = [
    "remote/251003a_89.txt",
    "remote/250723a_84.json",
    "remote/251003b_82.txt",
]
df_ws = [
    "remote/250805b_82.json",
    "remote/250805a_85.json",
    "remote/250723a_84.json",
    "remote/251004b_85.txt",
    "remote/250822b_82.txt",
]
df_ls = [
    "remote/250723a_84.json",
    "remote/250730a_85.json",
    "remote/250728_85.txt",
]
df_st = [
    "remote/250822c_85.txt",
    "remote/250823_85.txt",
    "remote/250723a_84.json",
]
df_fold = [
    "remote/251005f1_69.txt",
    "remote/251005f2_87.txt",
    "remote/251005f3_87.txt",
    "remote/251005f4_74.txt",
    "remote/251005f5_79.txt",
]

dfs = get_file(df_fold)
#plot_over(dfs, 'train_accuracy')
#plot_over(dfs, 'eval_accuracy')
#plot_over(dfs, 'loss')
#plot_over(dfs, 'eval_loss')

#df = extract_from_json("trainer_state.json")
#plot_trainval(df)

confusion_values = [
    [84, 12],
    [26, 70],
]
#plot_confusion_matrix(confusion_values,class_labels=['Clean code', 'Smell code'])

