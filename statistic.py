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
gcb_scores = get_scores_by_method(df, 'GraphCodeBERT')

# Daftar perbandingan yang akan diuji
comparisons = [
    ("VS J48 Decision Tree", j48_scores),
    ("VS Naive Bayes", nb_scores),
    ("VS SVM", svm_scores),
    ("VS Random Forest", rf_scores),
    ("VS Logistic Regression", lr_scores),
    ("VS GraphCodeBERT", gcb_scores),
]

print("--- Hasil Uji Wilcoxon Signed-Rank Test (Akurasi) ---")
alpha = 0.05

for name, baseline_scores in comparisons:
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

    print(f"\n{name}:")
    print(f"  Rata-Rata UniXcoder: {np.mean(unixcoder_scores):.4f}")
    print(f"  Rata-Rata Baseline: {np.mean(baseline_scores):.4f}")
    print(f"  Wilcoxon Statistic (W): {statistic:.4f}")
    print(f"  P-Value (p): {p_value:.4f}")

    # --- 3. Interpretasi Hasil ---
    if p_value < alpha:
        print("  Keputusan: Peningkatan kinerja signifikan secara statistik (p < 0.05).")
    else:
        print("  Keputusan: Peningkatan kinerja TIDAK signifikan secara statistik (p >= 0.05).")