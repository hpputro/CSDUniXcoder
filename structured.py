# file: compare_classifiers_structured.py
import warnings
warnings.filterwarnings("ignore")

import numpy as np
import pandas as pd

from pathlib import Path
from sklearn.model_selection import train_test_split, StratifiedKFold, cross_val_score
from sklearn.metrics import classification_report, confusion_matrix, accuracy_score, precision_recall_fscore_support
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression

# ====== Classifiers ======
from sklearn.tree import DecisionTreeClassifier      # proxy untuk J48 (C4.5-like) -> gunakan 'entropy'
from sklearn.ensemble import RandomForestClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn.svm import SVC

# JRip (RIPPER) via wittgenstein (opsional)
# -------------------------------------------------------
# 1) Load & prepare data
# -------------------------------------------------------
HAS_WITTGENSTEIN = False
CSV_PATH = "switch_statements_structured.csv"  # ganti jika berbeda
assert Path(CSV_PATH).exists(), f"CSV tidak ditemukan: {CSV_PATH}"

df = pd.read_csv(CSV_PATH)

# Buang kolom non-fitur jika ada
drop_cols = [c for c in ['id', 'idp', 'filename'] if c in df.columns]
if drop_cols:
    df = df.drop(columns=drop_cols)

# Pastikan kolom label ada
assert 'label' in df.columns, "Kolom 'label' tidak ditemukan di CSV."

# Normalisasi nilai label ke {0,1}
if df['label'].dtype == 'bool':
    df['label'] = df['label'].map({True: 1, False: 0})
else:
    # jika string 'True'/'False'
    df['label'] = df['label'].replace({True:1, False:0, 'True':1, 'False':0})

# Drop NA
df = df.dropna().reset_index(drop=True)

# Pisahkan fitur dan label
X = df.drop(columns=['label'])
y = df['label'].astype(int).values

# Train-test split stratified 80:20
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42, stratify=y
)
print("Train shape:", X_train.shape, " Test shape:", X_test.shape)
print("Label distrib (train):", np.bincount(y_train), " (test):", np.bincount(y_test))

# -------------------------------------------------------
# 2) Definisikan model
# -------------------------------------------------------
models = {
    # J48 (approx) -> DecisionTree 'entropy' mendekati C4.5
    "J48_like_DecisionTree(entropy)": DecisionTreeClassifier(
        criterion='entropy', random_state=42
    ),

    # Naive Bayes
    "NaiveBayes(GaussianNB)": GaussianNB(),

    # SVM RBF + scaling
    "SVM_RBF": Pipeline([
        ("scaler", StandardScaler(with_mean=False) if hasattr(X_train, "sparse") else StandardScaler()),
        ("svc", SVC(kernel='rbf', probability=True, random_state=42))
    ]),

    # Random Forest
    "RandomForest": RandomForestClassifier(
        n_estimators=300, max_depth=None, random_state=42, n_jobs=-1
    ),

    # Logistic Regression
    "LogisticRegression": LogisticRegression(
        solver='lbfgs', max_iter=1000, random_state=42
    ),
}

# JRip (RIPPER) opsional
if HAS_WITTGENSTEIN:
    # Wittgenstein menerima DataFrame dengan label di kolom terakhir
    models["JRip(RIPPER)_wittgenstein"] = "WITTGENSTEIN_PLACEHOLDER"

# -------------------------------------------------------
# 3) Fungsi evaluasi
# -------------------------------------------------------
def evaluate_and_report(name, clf, X_tr, y_tr, X_te, y_te):
    """Latih, prediksi, dan kembalikan ringkasan metrik + cetak laporan."""
    clf.fit(X_tr, y_tr)
    y_pred = clf.predict(X_te)

    cm = confusion_matrix(y_te, y_pred)
    report_dict = classification_report(y_te, y_pred, output_dict=True, zero_division=0)
    acc = accuracy_score(y_te, y_pred)
    prec, rec, f1, _ = precision_recall_fscore_support(y_te, y_pred, average='macro', zero_division=0)

    print("="*80)
    print(f"[{name}] Accuracy: {acc:.4f}")
    print("Confusion Matrix:\n", cm)
    print("Classification Report:")
    print(classification_report(y_te, y_pred, digits=4, zero_division=0))

    return {
        "model": name,
        "test_accuracy": acc,
        "test_precision_macro": prec,
        "test_recall_macro": rec,
        "test_f1_macro": f1,
        "confusion_matrix": cm.tolist(),
    }

results = []

# -------------------------------------------------------
# 4) Jalankan evaluasi untuk semua model
# -------------------------------------------------------
for name, clf in models.items():
    if name.startswith("JRip") and clf == "WITTGENSTEIN_PLACEHOLDER":
        # Latih & evaluasi JRip via wittgenstein
        # JRip memerlukan df gabungan (fit(X, y) tersedia di package terbaru, namun aman gunakan cara klasik)
        try:
            # Gabungkan X_train dan y_train menjadi satu DataFrame
            df_train = X_train.copy()
            df_train['label'] = y_train
            df_test = X_test.copy()
            df_test['label'] = y_test

            ripper = lw.RIPPER(random_state=42)
            ripper.fit(df_train, class_feat='label')

            y_pred = ripper.predict(X_test)

            cm = confusion_matrix(y_test, y_pred)
            report_dict = classification_report(y_test, y_pred, output_dict=True, zero_division=0)
            acc = accuracy_score(y_test, y_pred)
            prec, rec, f1, _ = precision_recall_fscore_support(y_test, y_pred, average='weighted', zero_division=0)

            print("="*80)
            print(f"[{name}] Accuracy: {acc:.4f}")
            print("Confusion Matrix:\n", cm)
            print("Classification Report:")
            print(classification_report(y_test, y_pred, digits=4, zero_division=0))

            results.append({
                "model": name,
                "test_accuracy": acc,
                "test_precision_weighted": prec,
                "test_recall_weighted": rec,
                "test_f1_weighted": f1,
                "cv5_acc_mean_on_train": np.nan,
                "cv5_acc_std_on_train": np.nan,
                "confusion_matrix": cm.tolist(),
            })
        except Exception as e:
            print(f"[{name}] Gagal dijalankan (abaikan): {e}")
        continue

    # Model scikit-learn biasa
    res = evaluate_and_report(name, clf, X_train, y_train, X_test, y_test)
    results.append(res)

# -------------------------------------------------------
# 5) Ringkasan hasil & simpan
# -------------------------------------------------------
summary_df = pd.DataFrame(results).sort_values(by="test_accuracy", ascending=False).reset_index(drop=True)
print("\n" + "#"*80)
print("RINGKASAN HASIL (urut akurasi test menurun):")
print(summary_df[["model", "test_accuracy", "test_precision_macro", "test_recall_macro", "test_f1_macro"]])