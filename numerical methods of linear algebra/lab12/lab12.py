import numpy as np


def prepare_to_gauss(m, v):
    a = m
    for i in range(0, len(v)):
        a[i].append(v[i])
    return a


def gauss_method(a):
    n = len(a)
    x = [0 for i in range(n)]

    for i in range(n):
        if a[i][i] == 0.0:
            print('Divide by zero detected!')

        for j in range(i + 1, n):
            rat = a[j][i] / a[i][i]

            for k in range(n + 1):
                a[j][k] = a[j][k] - rat * a[i][k]

    x[n - 1] = a[n - 1][n] / a[n - 1][n - 1]
    for i in range(n - 2, -1, -1):
        x[i] = a[i][n]

        for j in range(i + 1, n):
            x[i] = x[i] - a[i][j] * x[j]

        x[i] = x[i] / a[i][i]

    return x


def solve_phi(r0):
    #  PHIa + PHIb - 4PHIc = 0
    # -2PHIa + PHIb + 6PHIc = 0
    #  PHIa                = 5

    a = [
        [1, 1, -4],
        [-2, 1, 6],
        [1, 0, 0]
    ]
    b = [0, 0, 5]
    x = gauss_method(prepare_to_gauss(a, b))
    phi = {"a": x[0], 'b': x[1], 'c': x[2]}
    print(phi)
    i6 = phi['b'] / r0
    i5 = phi['c'] / r0
    i4 = i5
    i3 = (phi['c'] - phi['b']) / r0
    i1 = (phi['a'] - phi['c']) / r0
    i0 = i1 * 2
    return [i0, i1, i3, i4, i5, i6]


def solve_shtudgard(r1, r2, r3, r4, r5, r6, r7):
    a = [
        [(1 / r3 + 1 / r6), -(1 / r3), 0, 0],
        [-(1 / r3), (1 / r5 + 1 / r3 + 1 / r2), -(1 / r4), -(1 / r2)],
        [0, -(1 / r4), (1 / r7 + 1 / r4 + 1 / r1), -(1 / r1)],
        [0, -(1 / r2), -(1 / r1), (1 / r2 + 1 / r1)]
    ]
    b = np.array([-0.01, 0, 0, 0.01])
    x = gauss_method(prepare_to_gauss(a, b))
    v = {"1": x[0], '2': x[1], '3': x[2], '4': x[3]}
    print(v)
    i1 = (v['4'] - v['3']) / r1
    i2 = (v['4'] - v['2']) / r2
    i3 = (v['1'] - v['2']) / r3
    i4 = (v['2'] - v['3']) / r4
    i5 = (v['2'] - 0) / r5
    i6 = (v['1'] - 0) / r6
    i7 = (v['3'] - 0) / r7
    return i1, i2, i3, i4, i5, i6, i7


if __name__ == '__main__':
    r0 = 2
    pr = solve_phi(r0)
    for i in range(len(pr)):
        if i > 1:
            print(f"i{i+1}: {pr[i]}")
        else:
            print(f"i{i}: {pr[i]}")

    print()

    st = solve_shtudgard(r1=100000, r2=0.5, r3=0.5, r4 = 100000, r5=100000, r6=100000, r7=100000)
    for i in range(len(st)):
        print(f"i{i}: {st[i]}")

# {'a': 5.0, 'b': 1.0, 'c': 1.5}
# i0: 1.4
# i1: 0.7
# i2: 0.1
# i3: 0.3
# i4: 0.3
# i5: 0.2
#
# {'1': -0.35017018379850195, '2': 1.3494894486044942, '3': 0.9916950306330846, '4': 2.5074200136147056}
# i0: 0.004210347174948947
# i1: 0.005789652825051057
# i2: -0.007081915135012484
# i3: 0.0019877467665078306
# i4: 0.004217154526889044
# i5: -0.002918084864987516
# i6: 0.006198093941456779