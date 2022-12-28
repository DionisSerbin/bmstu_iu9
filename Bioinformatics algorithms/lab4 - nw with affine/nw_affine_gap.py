from typing import Callable, Tuple

DEBUG = False


def score_fun(a: str,
              b: str,
              match_score: int = 5,
              mismatch_score: int = -4) -> int:
    return match_score if a == b else mismatch_score


def construct_ms(seq1, seq2, gap_open, gap_extend):
    inf = float('-inf')

    n, m = len(seq1) + 1, len(seq2) + 1
    m_m = [[0] * m for _ in range(n)]
    i_m = [[0] * m for _ in range(n)]
    d_m = [[0] * m for _ in range(n)]
    result = [[0] * m for _ in range(n)]

    for i in range(n):
        m_m[i][0] = inf
        i_m[i][0] = gap_open + (i - 1) * gap_extend
        d_m[i][0] = inf
        result[i][0] = i_m[i][0]

    for j in range(m):
        m_m[0][j] = inf
        i_m[0][j] = inf
        d_m[0][j] = gap_open + (j - 1) * gap_extend
        result[0][j] = d_m[0][j]

    i_m[0][0] = inf
    d_m[0][0] = inf
    m_m[0][0] = 0
    result[0][0] = 0

    for i in range(1, n):
        for j in range(1, m):
            score = score_fun(seq1[i - 1], seq2[j - 1])
            m_m[i][j] = max(
                m_m[i - 1][j - 1],
                i_m[i - 1][j - 1],
                d_m[i - 1][j - 1]) + score
            i_m[i][j] = max(
                i_m[i][j - 1] + gap_extend,
                m_m[i][j - 1] + gap_open
            )
            d_m[i][j] = max(
                d_m[i - 1][j] + gap_extend,
                m_m[i - 1][j] + gap_open
            )
            result[i][j] = max(
                m_m[i][j],
                i_m[i][j],
                d_m[i][j]
            )

    return m_m, i_m, d_m, result


def needleman_wunsch_affine(seq1: str,
                            seq2: str,
                            score_fun: Callable = score_fun,
                            gap_open: int = -10,
                            gap_extend: int = -1) -> Tuple[str, str, int]:
    m_m, i_m, d_m, result = construct_ms(seq1, seq2, gap_open, gap_extend)

    i, j = len(seq1), len(seq2)
    n, m = len(seq1), len(seq2)
    res1 = ""
    res2 = ""
    is_extended = False
    while i > 0 or j > 0:
        a, b = '-', '-'
        if not is_extended and i > 0 and j > 0 \
                and result[i][j] == result[i - 1][j - 1] + score_fun(seq1[i - 1], seq2[j - 1]):
            a = seq1[i - 1]
            b = seq2[j - 1]
            i -= 1
            j -= 1
        elif i > 0 and result[i][j] == result[i - 1][j] + gap_open:
            is_extended = False
            a = seq1[i - 1]
            i -= 1
        elif j > 0 and result[i][j] == result[i][j - 1] + gap_open:
            is_extended = False
            b = seq2[j - 1]
            j -= 1
        elif i > 0 and result[i][j] == result[i - 1][j] + gap_extend:
            is_extended = True
            a = seq1[i - 1]
            i -= 1
        elif j > 0 and result[i][j] == result[i][j - 1] + gap_extend:
            is_extended = True
            b = seq2[j - 1]
            j -= 1
        else:
            break

        res1 += a
        res2 += b

    res1 = res1[::-1]
    res2 = res2[::-1]

    return res1, \
           res2, \
           max(m_m[n][m],
               i_m[n][m],
               d_m[n][m]
               )


def print_array(matrix: list):
    for row in matrix:
        for element in row:
            print(f"{element:6}", end="")
        print()


def main():
    aln1, aln2, score = needleman_wunsch_affine("ACG", "ACGT", gap_open=-10, gap_extend=-1)
    print(f'str 1: {aln1}')
    print(f'str 2: {aln2}')
    print(f'score: {score}')


if __name__ == "__main__":
    main()
