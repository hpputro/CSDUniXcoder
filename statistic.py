import numpy as np
import pandas as pd
from scipy.stats import wilcoxon

df = pd.read_csv('all_fold_method.csv')
df.columns = df.columns.str.strip()

# Extract data per metode
def get_scores_by_method(df, method_name):
    """Extract akurasi scores untuk metode tertentu, diurutkan per fold"""
    method_data = df[df['method'] == method_name].sort_values('fold')
    return np.array(method_data['accuracy'].values)

unixcoder_scores = get_scores_by_method(df, 'UniXcoder')
j48_scores = get_scores_by_method(df, 'J48 Decision Tree')
nb_scores = get_scores_by_method(df, 'Naive Bayes')
svm_scores = get_scores_by_method(df, 'SVM')
rf_scores = get_scores_by_method(df, 'Random Forest')
lr_scores = get_scores_by_method(df, 'Logistic Regression')
bow_scores = get_scores_by_method(df, 'BoW')
tfidf_scores = get_scores_by_method(df, 'TFIDF')
cb_scores = get_scores_by_method(df, 'CodeBERT')
gcb_scores = get_scores_by_method(df, 'GraphCodeBERT')
ct5_scores = get_scores_by_method(df, 'CodeT5')

# Daftar perbandingan yang akan diuji
comparisons = [
    ("VS Naive Bayes", nb_scores),
    ("VS J48 Decision Tree", j48_scores),
    ("VS Logistic Regression", lr_scores),
    ("VS SVM", svm_scores),
    ("VS Random Forest", rf_scores),
    ("VS BoW", bow_scores),
    ("VS TFIDF", tfidf_scores),
    ("VS CodeBERT", cb_scores),
    ("VS GraphCodeBERT", gcb_scores),
    ("VS CodeT5", ct5_scores),
]

print("--- Hasil Uji Wilcoxon Signed-Rank Test (Akurasi) ---")
alpha = 0.05
results = []

for name, baseline_scores in comparisons:
    mean_unixcoder = np.mean(unixcoder_scores)
    mean_baseline = np.mean(baseline_scores)
    improvement = ((mean_unixcoder - mean_baseline) / mean_baseline) * 100

    # --- 2. Lakukan Uji Wilcoxon ---
    # alternative='greater' menguji apakah UniXcoder > Baseline
    try:
        # Pengecekan untuk Wilcoxon di mana skor U dan B sama di semua fold
        if np.array_equal(unixcoder_scores, baseline_scores):
            p_value = 1.0
            statistic = np.nan
        else:
            # Stats Wilcoxon tidak bisa dijalankan jika semua perbedaan adalah 0
            statistic, p_value = wilcoxon(unixcoder_scores, baseline_scores, alternative='greater', zero_method='pratt')
    except ValueError:
        # Jika nilai p tidak dapat dihitung
        p_value = 1.0 
        statistic = np.nan

    if p_value < alpha:
        decision = "Signifikan"
    else:
        decision = "Tidak Signifikan"

    results.append({
        "Perbandingan": name,
        "Rata-Rata UniXcoder": mean_unixcoder,
        "Rata-Rata Baseline": mean_baseline,
        "Improvement (%)": improvement,
        "Wilcoxon Statistic (W)": statistic,
        "P-Value (p)": p_value,
        "Keputusan": decision,
    })

results_df = pd.DataFrame(results)
print(
    results_df.to_string(
        index=False,
        formatters={
            "Rata-Rata UniXcoder": "{:.2f}".format,
            "Rata-Rata Baseline": "{:.2f}".format,
            "Improvement (%)": "{:.1f}".format,
            "Wilcoxon Statistic (W)": lambda x: f"{x:.1f}" if pd.notna(x) else "NaN",
            "P-Value (p)": "{:.3f}".format,
        },
    )
)
