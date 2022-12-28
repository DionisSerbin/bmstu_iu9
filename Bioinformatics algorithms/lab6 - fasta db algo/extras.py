import blosum as bl

matrix = bl.BLOSUM(62)


def score_fun(a: str,
              b: str,
              match_score: int = 5,
              mismatch_score: int = 1) -> int:
    return matrix[a + b]


def smith_waterman(seq1: str, seq2: str, score_fun=score_fun, gap_score: int = -10):
    m, n = len(seq1) + 1, len(seq2) + 1

    matrix = [[0] * n for _ in range(m)]

    for i in range(m):
        matrix[i][0] = i * gap_score
    for j in range(n):
        matrix[0][j] = j * gap_score

    for i in range(1, m):
        for j in range(1, n):
            matrix[i][j] = max(matrix[i - 1][j - 1] + score_fun(seq1[i - 1], seq2[j - 1]),
                               matrix[i - 1][j] + gap_score,
                               matrix[i][j - 1] + gap_score)

    score = max(n, m) * gap_score
    max_i, max_j = -1, -1
    for i in range(1, m):
        for j in range(1, n):
            if matrix[i][j] > score:
                max_i = i
                max_j = j
                score = matrix[i][j]

    i = max_i
    j = max_j
    aln1 = ""
    aln2 = ""
    while matrix[i][j] > 0:
        a, b = '-', '-'
        # (A, B)
        if i > 0 and j > 0 and matrix[i][j] == matrix[i - 1][j - 1] + score_fun(seq1[i - 1], seq2[j - 1]):
            a = seq1[i - 1]
            b = seq2[j - 1]
            i -= 1
            j -= 1

        # (A, -)
        elif i > 0 and matrix[i][j] == matrix[i - 1][j] + gap_score:
            a = seq1[i - 1]
            i -= 1

        # (-, A)
        elif j > 0 and matrix[i][j] == matrix[i][j - 1] + gap_score:
            b = seq2[j - 1]
            j -= 1

        aln1 += a
        aln2 += b
    return aln1[::-1], aln2[::-1], score
