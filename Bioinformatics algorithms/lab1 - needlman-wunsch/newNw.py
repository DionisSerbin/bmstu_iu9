import numpy as np


def nw2(seq_1, seq_2, gap_score=-5):
    matrix_score = np.zeros(shape=(len(seq_1) + 1, len(seq_2) + 1))

    for i in range(0, len(seq_1) + 1):
        for j in range(0, len(seq_2) + 1):
            if i == 0:
                matrix_score[0, j] = gap_score * j
            elif j == 0:
                matrix_score[i, 0] = gap_score * i
            else:
                match_score = matrix_score[i - 1, j - 1] + (5 if seq_1[i - 1] == seq_2[j - 1] else -4)
                delete_score = matrix_score[i - 1, j] + gap_score
                insert_score = matrix_score[i, j - 1] + gap_score
                matrix_score[i, j] = max(match_score, delete_score, insert_score)

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

