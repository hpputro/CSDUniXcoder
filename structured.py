import warnings
warnings.filterwarnings("ignore")

import numpy as np
import pandas as pd

from pathlib import Path
from sklearn.base import clone
from sklearn.model_selection import StratifiedKFold
from sklearn.metrics import classification_report, confusion_matrix, accuracy_score, precision_recall_fscore_support
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression

from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn.svm import SVC

CSV_PATH = "switch_statements_structured.csv"
assert Path(CSV_PATH).exists(), f"CSV not found: {CSV_PATH}"

df = pd.read_csv(CSV_PATH)
drop_cols = [c for c in ['id', 'idp', 'filename'] if c in df.columns]
if drop_cols:
    df = df.drop(columns=drop_cols)
assert 'label' in df.columns, "Label not found."

if df['label'].dtype == 'bool':
    df['label'] = df['label'].map({True: 1, False: 0})
else:
    df['label'] = df['label'].replace({True: 1, False: 0, 'True': 1, 'False': 0})

df = df.dropna().reset_index(drop=True)
X = df.drop(columns=['label'])
y = df['label'].astype(int).values
print("Dataset shape:", X.shape)
print("Label distribution:", np.bincount(y))

models = {
    "J48 Decision Tree": DecisionTreeClassifier(criterion='entropy', random_state=0),
    "Naive Bayes": GaussianNB(),
    "SVM": Pipeline([("scaler", StandardScaler()), ("svc", SVC(kernel='rbf', probability=True, random_state=0))]),
    "Random Forest": RandomForestClassifier(n_estimators=300, max_depth=None, random_state=0, n_jobs=-1),
    "Logistic Regression": LogisticRegression(solver='lbfgs', max_iter=1000, random_state=0),
}

kf = StratifiedKFold(n_splits=5, shuffle=True, random_state=0)
labels = np.unique(y)

results = []
for name, base_clf in models.items():
    print("=" * 80)
    print(f"Evaluating {name} with 5-fold Stratified CV")
    acc_scores = []
    prec_scores = []
    rec_scores = []
    f1_scores = []
    cm_sum = np.zeros((labels.size, labels.size), dtype=int)

    for fold_idx, (train_idx, test_idx) in enumerate(kf.split(X, y), 1):
        clf = clone(base_clf)
        X_train, X_test = X.iloc[train_idx], X.iloc[test_idx]
        y_train, y_test = y[train_idx], y[test_idx]

        clf.fit(X_train, y_train)
        y_pred = clf.predict(X_test)

        cm = confusion_matrix(y_test, y_pred, labels=labels)
        cm_sum += cm
        acc = accuracy_score(y_test, y_pred)
        prec, rec, f1, _ = precision_recall_fscore_support(y_test, y_pred, average='macro', zero_division=0)

        acc_scores.append(acc)
        prec_scores.append(prec)
        rec_scores.append(rec)
        f1_scores.append(f1)

        print(f"Fold {fold_idx}: accuracy={acc:.4f}, precision={prec:.4f}, recall={rec:.4f}, f1={f1:.4f}")
        print("Classification Report:")
        print(classification_report(y_test, y_pred, digits=4, zero_division=0))

    res = {
        "model": name,
        "accuracy": float(np.mean(acc_scores)),
        "accuracy_std": float(np.std(acc_scores, ddof=1)),
        "precision": float(np.mean(prec_scores)),
        "recall": float(np.mean(rec_scores)),
        "f1_score": float(np.mean(f1_scores)),
        "confusion_matrix_sum": cm_sum.tolist(),
    }
    results.append(res)

summary_df = pd.DataFrame(results).sort_values(by="accuracy", ascending=True).reset_index(drop=True)
print("\n" + "#" * 80)
print("RINGKASAN HASIL (5-fold CV, urut akurasi rata-rata menurun):")
metrics_cols = [
    "accuracy",
    "accuracy_std",
    "precision",
    "recall",
    "f1_score",
]
summary_pretty = summary_df.copy()
for col in metrics_cols:
    summary_pretty[col] = summary_pretty[col].map(lambda x: f"{x * 100:.1f}%")
print(summary_pretty[["model"] + metrics_cols])

