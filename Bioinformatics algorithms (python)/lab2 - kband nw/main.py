def in_band(left, right, middle):
    return left <= middle <= right


def in_band2(left, right, middle):
    return left <= middle < right


def n_w_with_k_band(seq_1, seq_2, k, is_print):
    gap_score = -5
    h_seq1, w_seq2 = len(seq_1) + 1, len(seq_2) + 1

    matrix_score = [[gap_score * max(w_seq2, h_seq1)] * w_seq2 for _ in range(h_seq1)]

    for i in range(k + 2):
        matrix_score[i][0] = i * gap_score
    for j in range(k + 2):
        matrix_score[0][j] = j * gap_score

    for i in range(1, h_seq1):
        for h in range(-k, k + 1):
            j = i + h
            if in_band2(1, w_seq2, j):
                match = matrix_score[i - 1][j - 1] + (5 if seq_1[i - 1] == seq_2[j - 1] else -4)
                delete = matrix_score[i - 1][j] + gap_score
                insert = matrix_score[i][j - 1] + gap_score
                if in_band(-k, k, (i - 1) - j):
                    matrix_score[i][j] = max(match, delete)
                    match = matrix_score[i][j]
                if in_band(-k, k, i - (j - 1)):
                    matrix_score[i][j] = max(match, insert)
    if is_print:
        print(f"matrix score")
        for row in matrix_score:
            print(row)

    score = matrix_score[-1][-1]
    align1, align2 = '', ''
    i, j = len(seq_1), len(seq_2)
    while i > 0 or j > 0:
        curr = matrix_score[i][j]
        diag = matrix_score[i - 1][j - 1]
        up = matrix_score[i][j - 1]
        left = matrix_score[i - 1][j]

        if i > 0 and j > 0 and curr == diag + (5 if seq_1[i - 1] == seq_2[j - 1] else -4):
            align1 += seq_1[i - 1]
            align2 += seq_2[j - 1]
            i -= 1
            j -= 1
        elif i > 0 and curr == left + gap_score:
            align1 += seq_1[i - 1]
            align2 += '-'
            i -= 1
        elif j > 0 and curr == up + gap_score:
            align1 += '-'
            align2 += seq_2[j - 1]
            j -= 1

    return align1[::-1], align2[::-1], score


if __name__ == "__main__":
    fasta_path = "diagonal_test.fasta"
    k = 3
    seq1 = "TTGACACCCTCCCAATT"
    seq2 = "ACCCCAGGCTTTACACAT"
    align_1, align_2, score = n_w_with_k_band(seq1, seq2, k, True)
    _, _, score_k_minus_1 = n_w_with_k_band(seq1, seq2, k - 1, False)
    if score_k_minus_1 < score:
        print("wrong k")
    else:
        print(f"score:  {score}")
        print(f"align1: {align_1}")
        print(f"align2: {align_2}")
