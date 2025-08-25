import re
import json
import pandas as pd
import matplotlib.pyplot as plt

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
    plt.plot(df['epoch'], df['train_accuracy'], marker='o', label='Train Accuracy', color='#5D7080')
    plt.plot(df['epoch'], df['loss'], marker='s', label='Train Loss', color='#B58B6A')
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

def plot_loss(df):
    plt.figure(figsize=(8,5))
    plt.plot(df['epoch'], df['loss'], marker='o', label='Train Loss', color='#1565C0')
    plt.plot(df['epoch'], df['eval_loss'], marker='s', label='Validation Loss', color='#FFB300')
    plt.xlabel('Epoch')
    plt.ylabel('Loss')
    plt.title('Train vs Validation Loss per Epoch')
    plt.legend()
    plt.grid(True, linestyle='--', alpha=0.5)
    plt.xticks([x for x in range(int(min(df['epoch'])), int(max(df['epoch']))+1) if x % 2 == 0])
    plt.tight_layout()
    plt.show()

def plot_metrics(df):
    plt.figure(figsize=(8,5))
    plt.plot(df['epoch'], df['eval_precision'], marker='^', label='Precision', color='#5D7080')
    plt.plot(df['epoch'], df['eval_recall'], marker='v', label='Recall', color='#2A3B36')
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
    markers = ['^', 'v', 's', 'd', 'x']
    #colors = ['#C5776D', '#7FAEC5', '#92BF9E', '#D2B87E', '#A783A7']
    colors = ['#7FAEC5', '#92BF9E', '#C5776D', '#D2B87E', '#92BF9E']
    labels = ['linear', 'polynomial', 'cosine', 'constant', 'constant with warmup'] 

    for i, df in enumerate(dfs):
        plt.plot(df['epoch'], df[metric], marker=markers[i], label=labels[i], color=colors[i])

    plt.xlabel('Epoch')
    plt.ylabel(metric)
    plt.legend(title='Scheduler Type')
    plt.grid(True, linestyle='--', alpha=0.5)
    if max_epochs != 0 and len(dfs) > 0 and not dfs[0].empty:
        plt.xticks([x for x in range(int(min(dfs[0]['epoch'])), int(max(dfs[0]['epoch']))+1) if x <= max_epochs])
    else:
        plt.xticks([x for x in range(int(min(dfs[0]['epoch'])), int(max(dfs[0]['epoch']))+1)])
    plt.tight_layout()
    plt.show()

'''
df7 = extract_from_json("remote/250724a_79.json")
df8 = extract_from_json("remote/250723b_79.json")
df5 = extract_from_json("remote/250723a_84.json")
df10 = extract_from_json("remote/250722c_79.json")
plot_over(df5, df7, df8, df10, 'eval_accuracy')

#learning rate
df1 = extract_from_text("remote/250730b_77.txt")
df2 = extract_from_json("remote/250731a_87.json")
df3 = extract_from_json("remote/250723a_84.json")
df4 = extract_from_json("remote/250730b_87.json")
plot_over(df1, df2, df3, df4, 'eval_accuracy')
plot_over(df1, df2, df3, df4, 'eval_loss')

#warmup steps
df1 = extract_from_json("remote/250805b_82.json")
df2 = extract_from_json("remote/250805a_85.json")
df3 = extract_from_json("remote/250723a_84.json")
df4 = extract_from_text("remote/250730c_85.txt")
plot_over(df1, df2, df3, df4, 'eval_accuracy')
plot_over(df1, df2, df3, df4, 'eval_loss')
'''

df_files = [
    "remote/250822c_85.json",
    "remote/250723a_84.json",
    "remote/250823_85.txt"
]

dfs = []
for file in df_files:
    if file.endswith(".txt"):
        dfs.append(extract_from_text(file))
    elif file.endswith(".json"):
        dfs.append(extract_from_json(file))

plot_over(dfs, 'eval_accuracy')
plot_over(dfs, 'eval_loss')

'''
df = extract_from_json("remote/250723a_84.json")
plot_eval(df)
plot_metrics(df)
'''

