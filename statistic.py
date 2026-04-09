import numpy as np
from scipy.stats import wilcoxon

# --- 1. Masukkan Data Akurasi per Fold ---
# UniXcoder (U) adalah model yang diuji
unixcoder_scores = np.array([0.692, 0.872, 0.868, 0.789, 0.789])

# Baseline Models (B)
j48_scores = np.array([0.7838, 0.7297, 0.6757, 0.6111, 0.7500])
nb_scores = np.array([0.6486, 0.6216, 0.7027, 0.6111, 0.6667])
svm_scores = np.array([0.7568, 0.7297, 0.6486, 0.6111, 0.8056])
rf_scores = np.array([0.8378, 0.7297, 0.7568, 0.8611, 0.6944])
lr_scores = np.array([0.7568, 0.6757, 0.7568, 0.7500, 0.7222])

# Daftar perbandingan yang akan diuji
comparisons = [
    ("UniXcoder vs J48 Decision Tree", j48_scores),
    ("UniXcoder vs Naive Bayes", nb_scores),
    ("UniXcoder vs SVM", svm_scores),
    ("UniXcoder vs Random Forest", rf_scores),
    ("UniXcoder vs Logistic Regression", lr_scores),
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