import sys

import numpy
from Bio import SeqIO


# задаем значения
class Constants:
    def __init__(self, GAP_PENALTY, SEQUENCES, TEXT):
        self.GAP_PENALTY = gap
        self.SEQUENCES = SEQUENCES
        self.INF = float("inf")
        self.TEXT = TEXT

gap = 2

def score_fun(a: str,
              b: str,
              match_score: int = 0,
              mismatch_score: int = 1) -> int:
    if a == '-' or b == '-':
        return gap
    else:
        return match_score if a == b else mismatch_score


# вычисляем табличку расстояний, которая заполняется с помощью значений score,
# и список текущих кластеров
def initialize_distance(sequences, constants):
    current_clusters = []
    distances = {}

    for i in range(len(sequences)):
        name = sequences[i].name
        current_clusters.append(sequences[i].name)
        distances[name] = {}

    for i in range(len(current_clusters)):
        for j in range(i + 1, len(current_clusters)):
            cluster_one = list(sequences)[i]
            cluster_two = list(sequences)[j]

            # вычисляем score для cluster_one и cluster_two
            cluster_distance = calculate_score(cluster_one.seq, cluster_two.seq, constants)

            # заполняем этим значением таблицу расстояний
            distances[cluster_one.name][cluster_two.name] = cluster_distance
            distances[cluster_two.name][cluster_one.name] = cluster_distance

    return current_clusters, distances


# для вычисления score используется алгоритм Needleman-Wunsch
def calculate_score(seq_1, seq_2, constants, return_matrix=False):
    LEN_SEQ_1 = len(seq_1) + 1
    LEN_SEQ_2 = len(seq_2) + 1
    # создаем табличку, которая заполнена нулями
    score_grid = numpy.zeros(shape=(LEN_SEQ_1, LEN_SEQ_2))

    # заполняем табличку согласно алгоритму
    for i in range(0, LEN_SEQ_1):
        for j in range(0, LEN_SEQ_2):
            if i == 0:
                score_grid[0, j] = constants.GAP_PENALTY * j
            elif j == 0:
                score_grid[i, 0] = constants.GAP_PENALTY * i
            else:
                match_score = score_grid[i - 1, j - 1] + score_fun(seq_1[i - 1], seq_2[j - 1])
                delete_score = score_grid[i - 1, j] + constants.GAP_PENALTY
                insert_score = score_grid[i, j - 1] + constants.GAP_PENALTY
                score_grid[i, j] = min(match_score, delete_score, insert_score)

    # начинаем идти с нижней правой ячейки
    i, j = len(seq_1), len(seq_2)

    if return_matrix:
        return score_grid
    else:
        return score_grid[i][j]


# делаем выравнивание для последовательностей, которые находятся в кластерах
def align_1(closest_cluster_one, closest_cluster_two, constants):
    merged_seq = []

    for i in range(len(closest_cluster_one)):
        seq_aligned_1 = None

        for j in range(len(closest_cluster_two)):
            if i == 0:
                seq_aligned_1, closest_cluster_two[j] = align_2(closest_cluster_one[i], closest_cluster_two[j],
                                                                constants)
            else:
                seq_aligned_1, closest_cluster_two[j] = align_2(seq_aligned_1, closest_cluster_two[j], constants)

        merged_seq.append(seq_aligned_1)

    merged_seq += closest_cluster_two

    return merged_seq


def align_2(seq_1, seq_2, constants):
    align1, align2 = '', ''

    score_grid = calculate_score(seq_1, seq_2, constants, True)

    i, j = len(seq_1), len(seq_2)

    while i > 0 and j > 0:
        current_cell = score_grid[i][j]
        diagonal_cell = score_grid[i - 1][j - 1]
        up_cell = score_grid[i][j - 1]
        left_cell = score_grid[i - 1][j]

        if current_cell == diagonal_cell + score_fun(seq_1[i - 1], seq_2[j - 1]):
            align1 += seq_1[i - 1]
            align2 += seq_2[j - 1]
            i -= 1
            j -= 1
        elif current_cell == left_cell + constants.GAP_PENALTY:
            align1 += seq_1[i - 1]
            align2 += '-'
            i -= 1
        elif current_cell == up_cell + constants.GAP_PENALTY:
            align1 += '-'
            align2 += seq_2[j - 1]
            j -= 1

    while i > 0:
        align1 = seq_1[i - 1] + align1
        align2 = "-" + align2
        i -= 1
    while j > 0:
        align1 = "-" + align1
        align2 = seq_1[j - 1] + align2
        j -= 1

    return align1[::-1], align2[::-1]


def calculate(sequences):
    const_input = 10

    text = {}
    for sequence in sequences:
        text[sequence.name] = [sequence.seq]
    constants = Constants(int(const_input), sequences, text)

    # список кластеров и таблица расстояний
    current_clusters, distances = initialize_distance(sequences, constants)
    for i in range(len(current_clusters)):
        print(f"cluster: {current_clusters[i]}")
        print(f"distances: {distances[current_clusters[i]]}")
        print()

    # начинаем объединять кластеры до тех пор, пока не останется один
    while len(current_clusters) != 1:
        # определим два кластера, которые необходимо будет объединить
        closest_cluster_one = None
        closest_cluster_two = None
        closest_distance = constants.INF

        # ищем кластеры с наименьшим расстоянием
        for i in range(len(current_clusters)):
            for j in range(i + 1, len(current_clusters)):
                if distances[current_clusters[i]][current_clusters[j]] < closest_distance:
                    closest_cluster_one = current_clusters[i]
                    closest_cluster_two = current_clusters[j]
                    # closest_distance = distances[current_clusters[i]][current_clusters[j]]

        # определяем новый кластер, полученный объединением двух других
        new_cluster_name = "(" + closest_cluster_one + "," + closest_cluster_two + ")" + ":"
        print(new_cluster_name +str(distances[closest_cluster_one][closest_cluster_two] / 2))

        new_cluster_seq = align_1(text[closest_cluster_one], text[closest_cluster_two], constants)
        print(text)
        print(closest_cluster_one)
        print(closest_cluster_two)

        text[new_cluster_name] = new_cluster_seq
        distances[new_cluster_name] = {}

        # пересчитываем дистанции для таблицы расстояний
        for cluster in current_clusters:
            if cluster != closest_cluster_one and cluster != closest_cluster_two:
                dist_one = len(text[closest_cluster_one])
                print(f"text: {text}")
                dist_two = len(text[closest_cluster_two])
                print(f"closest_cluster_one:                     {closest_cluster_one}")
                print(f"dist_one:                                {dist_one}")
                print(f"distances[closest_cluster_one][cluster]: {distances[closest_cluster_one][cluster]}")
                print(f"closest_cluster_two:                     {closest_cluster_two}")
                print(f"dist_two:                                {dist_two}")
                print(f"distances[closest_cluster_two][cluster]: {distances[closest_cluster_two][cluster]}")
                new_dist = float(
                    dist_one * distances[closest_cluster_one][cluster] + dist_two * distances[closest_cluster_two][
                        cluster]) / (dist_one + dist_two)

                distances[new_cluster_name][cluster] = new_dist
                distances[cluster][new_cluster_name] = new_dist

        current_clusters.remove(closest_cluster_one)
        current_clusters.remove(closest_cluster_two)
        current_clusters.append(new_cluster_name)

    for cluster in new_cluster_seq:
        print(cluster)


# print(new_cluster_seq)

def input_seq(fasta_file):
    sequences = []
    for sequence in SeqIO.parse(fasta_file, "fasta"):
        sequences.append(sequence)
    print(sequences)
    return sequences


if __name__ == '__main__':
    fasta_file = ''.join(sys.argv[1])
    sequences = input_seq('input.fasta')
    calculate(sequences)
    #
    # SP--E--TVIHS--GWVIWRELFSH-WPDQCKL-  LFGDWFAWIHWTYLVYYSAGPPCQGQSDIVVMMQKKLRTNFCQCYKYWYQ
    # SPSDQFFTVIHSCLYWVIWRDLMSHLFMNGAAIDIHWTWDSIAIGPPLV - YPIEEVFAGPSTIVVMMQKMLRTNFCQCYKPWYQ

# SP----ETVIHS--GWVIWRELFSHWPDQCKLLFGDWFA-WIHWTYLVYYS-A-G-P---P----CQGQSDIVVMMQKKLRTNFCQCYKYWYQ
# SPSDQFFTVIHSCLYWVIWRDL-MSH-------LFMNGAAIDIHWT---WDSIAIGPPLVYPIEEVFAGPSTIVVMMQKMLRTNFCQCYKPWYQ
# SPSDQFFTVIHSCLYYVIWRDLMSH-PL----------------------VYPI---EEV--FAG-P----STIVVMMQKMLRTNFCQCYKPWY-
# SP----ETVIHS--GWVIWREL-------CKLLFGDWFA-WIHWTYLVYYS-A-G-P---P----CQGQSDIVVMMQKKLRTNF--C-KYWYZ
