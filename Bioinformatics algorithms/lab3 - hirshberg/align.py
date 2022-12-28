from typing import Callable, Tuple
DEBUG = False


def score_fun(a: str,
              b: str,
              match_score: int = 5,
              mismatch_score: int = -4) -> int:
    return match_score if a == b else mismatch_score


def reverse(variable):
    res = ''
    for i in range(len(variable) - 1, -1, -1):
        res += variable[i]
    return res


def calc_score(seq1, seq2, gap_score):
    start = [i * gap_score for i in range(len(seq1) + 1)]
    res = start[:]
    for i in range(1, len(seq2) + 1):
        res[0] = i * gap_score
        for j in range(1, len(seq1) + 1):
            score = 5 if seq2[i - 1] == seq1[j - 1] else -4
            diag = start[j - 1] + score
            left = start[j] + gap_score
            up = res[j - 1] + gap_score
            res[j] = max(diag, left, up)
        start = res[:]
    return res


def hirschberg(seq1: str,
               seq2: str,
               score_fun: Callable = score_fun,
               gap_score: int = -5) -> Tuple[str, str, int]:
    """
        Inputs:
        seq1 - first sequence
        seq2 - second sequence
        score_fun - function that returns score for two symbols
        gap_score - score for gap in final alignment

        Outputs:
        aln1 - first sequence in alignment
        aln2 - second sequence in alignment
        score - score of alignment
        """
    n1 = len(seq1)
    n2 = len(seq2)

    if n1 < 2 or n2 < 2:
        return needleman_wunsch(seq1, seq2, score_fun, gap_score)

    mid = n2 // 2
    left = calc_score(seq1, seq2[0:mid], gap_score)
    right = calc_score(reverse(seq1), reverse(seq2[mid:]), gap_score)[::-1]
    full = [a + b for a, b in zip(left, right)]
    max_index = full.index(max(full))
    left_res = hirschberg(seq1[:max_index], seq2[:mid], gap_score=gap_score)
    right_res = hirschberg(seq1[max_index:], seq2[mid:], gap_score=gap_score)

    r0 = left_res[0] + right_res[0]
    r1 = left_res[1] + right_res[1]
    score = 0
    for i in range(len(r0)):
        first = r0[i]
        second = r1[i]
        if first == '-' or second == '-':
            # print(f"f: {f}, s: {s}")
            score += gap_score
        else:
            score += 5 if first == second else -4

    return r0, r1, score


def needleman_wunsch(seq1: str, seq2: str, score_fun: Callable = score_fun, gap_score: int = -5):
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
    if DEBUG:
        print_array(matrix)

    score = matrix[-1][-1]
    i, j = m - 1, n - 1
    aln1 = ""
    aln2 = ""
    while i > 0 or j > 0:
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


def print_array(matrix: list):
    for row in matrix:
        for element in row:
            print(f"{element:6}", end="")
        print()


if __name__ == "__main__":
    aln1, aln2, score = hirschberg("ATCT", "ACT", gap_score=-5)

    assert len(aln1) == len(aln2)
    print(aln1)
    print(aln2)
    print(score)



