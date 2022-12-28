import time

from db_create import dec_dig, dec_seq
from extras import smith_waterman, score_fun

DEBUG = False
PATH_DB = "fasta.db"
gap_score = -10
k = 2
MIN_SCORE = 50
MIN_LENGTH = 10


def my_filter(mass, list, filter):
    for el in list:
        if mass[el][0] < filter:
            del mass[el]
    return mass


def already_in_diag(diags, diff, pos_a):
    already = False
    for i in range(len(diags[diff][1])):
        pair = diags[diff][1][i]
        if pair[0] == pos_a + 1:
            already = True
            pair[0] -= 1
        elif pair[1] == pos_a - 1:
            already = True
            pair[1] += 1
    return already


def make_diags(a_poss, b_poss):
    diags = {}
    for key_a in a_poss.keys():
        if key_a in b_poss.keys():
            for pos_a in a_poss[key_a]:
                for pos_b in b_poss[key_a]:
                    diff = pos_b - pos_a
                    if diff not in diags:
                        diags[diff] = [1, [[pos_a, pos_a]]]
                    else:
                        diags[diff][0] += 1
                        if not already_in_diag(diags, diff, pos_a):
                            diags[diff][1].append([pos_a, pos_a])
    return my_filter(diags, list(diags.keys()), MIN_LENGTH)


def filter_score(a, b, diags):
    new_diags = {}
    for key in diags:
        for sub_diag in diags[key][1]:
            n = sub_diag[1] - sub_diag[0] + 1
            new_diags[(sub_diag[0], key + sub_diag[0], n)] = [
                sum([score_fun(
                    a[sub_diag[0] + i],
                    b[key + sub_diag[0] + i]
                ) for i in range(sub_diag[1] - sub_diag[0] + 1)]), n
            ]
    return my_filter(new_diags, list(new_diags.keys()), MIN_SCORE)

#подсмотрел как сделал жук
def merge_seq(seq1, seq2, diags):
    watched_diags = {}
    diags = filter_score(seq1, seq2, diags)
    while len(list(diags.keys())) > 0:
        for sub_diag in list(diags.keys()):
            new_created = False
            for sub_diag1 in list(diags.keys()):
                if sub_diag1 == sub_diag:
                    continue
                dist_x = sub_diag1[0] - (sub_diag[0] + diags[sub_diag][1] - 1)
                dist_y = sub_diag1[1] - (sub_diag[1] + diags[sub_diag][1] - 1)
                if dist_y < 0 or dist_x < 0:
                    continue
                final_score = gap_score * (dist_x + dist_y) + diags[sub_diag][0] + diags[sub_diag1][0]
                if final_score > diags[sub_diag][0]:
                    new_created = True
                    new_key = (sub_diag[0], sub_diag[1], sub_diag[2] + dist_x + sub_diag1[2])
                    diags[new_key] = [final_score, new_key[2]]
            if not new_created:
                watched_diags[sub_diag] = diags[sub_diag]
            del diags[sub_diag]
    return watched_diags


def make_score(seq1, seq2, sequences):
    result = []
    for s in sequences:
        result.append(smith_waterman(seq1[s[0]:(s[0] + s[2])], seq2[s[1]:(s[1] + s[2])]))
    if len([s[2] for s in result]) == 0:
        return -1
    max_score = -float('inf')
    for r in result:
        if r[2] > max_score:
            max_score = r[2]

    return max_score


def make_map(a):
    a_poss = {}
    for i in range(len(a) - 1):
        if a[i:i + k] in a_poss.keys():
            a_poss[a[i:i + k]].append(i)
        else:
            a_poss[a[i:i + k]] = [i]
    return a_poss


def find(a):
    aligns = []
    a_poss = make_map(a)
    with open(PATH_DB, 'rb') as f:
        new_b = dec_dig(f.read(3))
        time_bef = time.time()
        while new_b:
            print(f"time now: {time.time() - time_bef}")
            b = dec_seq(f.read(new_b))
            name_b, db_b = next(b)
            b_poss = make_map(db_b)
            align = merge_seq(a, db_b, make_diags(a_poss, b_poss))
            max_score = make_score(a, db_b, align)
            if max_score > 0:
                aligns.append((name_b, max_score))
            new_b = dec_dig(f.read(3))
    return aligns

#потом запусти меня
if __name__ == '__main__':
    a = "QVQLVQSGAEVKKPGSSVKVSCKASGGTFSNYAISWVRQAPGQGLEWMGRIIPILGIANYAQKFQGRVTITADKSTSTAYMELSSLRSEDTAVYYCARGYYEARHYYYYYAMDVWGQGTAVTVSSAS"
    aligns = find(a)
    print(f"3 тест при мин кол-ве матчей на диагонали: {MIN_LENGTH}, и мин скоре: {MIN_SCORE}")
    for align in aligns:
        print(f"бд имя:      {align[0]}")
        print(f"score:       {align[1]}")
        print()

# 3 тест при мин кол-ве матчей на диагонали: 10, и мин скоре: 50
# бд имя:      SP|P23083|HV102_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 1-2 OS=HOMO SAPIENS OX=9606 GN=IGHV1-2 PE=1 SV=2
# score:       53.0
#
# бд имя:      SP|A0A0C4DH29|HV103_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 1-3 OS=HOMO SAPIENS OX=9606 GN=IGHV1-3 PE=3 SV=1
# score:       53.0
#
# бд имя:      SP|P0DP01|HV108_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 1-8 OS=HOMO SAPIENS OX=9606 GN=IGHV1-8 PE=3 SV=1
# score:       53.0
#
# бд имя:      SP|A0A0C4DH31|HV118_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 1-18 OS=HOMO SAPIENS OX=9606 GN=IGHV1-18 PE=3 SV=1
# score:       53.0
#
# бд имя:      SP|A0A0C4DH33|HV124_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 1-24 OS=HOMO SAPIENS OX=9606 GN=IGHV1-24 PE=3 SV=1
# score:       53.0
#
# бд имя:      SP|P01743|HV146_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 1-46 OS=HOMO SAPIENS OX=9606 GN=IGHV1-46 PE=1 SV=2
# score:       53.0
#
# бд имя:      SP|P01742|HV169_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 1-69 OS=HOMO SAPIENS OX=9606 GN=IGHV1-69 PE=1 SV=2
# score:       67.0
#
# бд имя:      SP|A0A0C4DH38|HV551_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 5-51 OS=HOMO SAPIENS OX=9606 GN=IGHV5-51 PE=3 SV=1
# score:       53.0
#
# бд имя:      SP|A0A0J9YXX1|HV5X1_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 5-10-1 OS=HOMO SAPIENS OX=9606 GN=IGHV5-10-1 PE=3 SV=1
# score:       53.0
#
# бд имя:      SP|A0A0G2JMI3|HV692_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 1-69-2 OS=HOMO SAPIENS OX=9606 GN=IGHV1-69-2 PE=3 SV=2
# score:       53.0
#
# бд имя:      SP|A0A0B4J2H0|HV69D_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 1-69D OS=HOMO SAPIENS OX=9606 GN=IGHV1-69D PE=1 SV=1
# score:       67.0
#
# бд имя:      SP|A0A0J9YVY3|HV741_HUMAN IMMUNOGLOBULIN HEAVY VARIABLE 7-4-1 OS=HOMO SAPIENS OX=9606 GN=IGHV7-4-1 PE=3 SV=1
# score:       53.0