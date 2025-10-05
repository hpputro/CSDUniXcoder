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

def plot_trainval(df):
    plt.figure(figsize=(8,5))
    plt.plot(df['epoch'], df['train_accuracy'], marker='^', label='Train Accuracy', color='#B490D1')
    plt.plot(df['epoch'], df['eval_accuracy'], marker='v', label='Validation Accuracy', color='#D04F4F')
    plt.plot(df['epoch'], df['loss'], marker='<', label='Train Loss', color='#D2B87E')
    plt.plot(df['epoch'], df['eval_loss'], marker='>', label='Validation Loss', color='#92BF9E')
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
    markers = ['^', 's', 'o', 'd', 'v']
    colors = [ '#D88080', '#D9C48F', '#95C9A6', '#7EB9CA','#C493D2']
    #colors = [ '#D88080', '#D9C48F', '#95C9A6']
    label_lr = ['1e-6', '5e-6', '8e-6', '1e-5', '2e-5']
    label_wr = ['0', '30', '50', '80', '100']
    label_bs = ['8', '16', '32']
    label_ls = ['0', '0.1', '0.2']
    label_st = ['linear', 'polynomial', 'cosine']
    label_fold = ['Fold 1', 'Fold 2', 'Fold 3', 'Fold 4', 'Fold 5']
    
    for i, df in enumerate(dfs):
        plt.plot(df['epoch'], df[metric], marker=markers[i], label=label_fold[i], color=colors[i])
    plt.legend(title='K-Fold Cross-Validation')

    plt.xlabel('Epoch')
    plt.ylabel(metric)
    plt.grid(True, linestyle='--', alpha=0.5)
    if max_epochs != 0 and len(dfs) > 0 and not dfs[0].empty:
        plt.xticks([x for x in range(int(min(dfs[0]['epoch'])), int(max(dfs[0]['epoch']))+1) if x <= max_epochs])
    else:
        plt.xticks([x for x in range(int(min(dfs[0]['epoch'])), int(max(dfs[0]['epoch']))+1)])
    plt.tight_layout()
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

df_lr = [
    "remote/250822a_76.txt",
    "remote/250730b_77.txt",
    "remote/250731a_87.json",
    "remote/250723a_84.json",
    "remote/251002a_87.txt",
    #"remote/250730b_87.txt",
]
df_bs = [
    "remote/251003a_89.txt",
    "remote/250723a_84.json",
    "remote/251003b_82.txt",
]
df_wr = [
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
plot_over(dfs, 'train_accuracy')
plot_over(dfs, 'eval_accuracy')
plot_over(dfs, 'loss')
plot_over(dfs, 'eval_loss')

'''
df = extract_from_text("remote/251004a_85.txt")
plot_trainval(df)
'''