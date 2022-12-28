import numpy as np


def fill_matrix(n, a, b):
    m = np.random.random((n, n)) * (b - a) + a
    return m


def decompose_lu(a):
    lu = np.matrix(np.zeros([a.shape[0], a.shape[1]]))

    for k in range(a.shape[0]):
        for j in range(k, a.shape[0]):
            lu[k, j] = a[k, j] - lu[k, :k] * lu[:k, j]
        for i in range(k + 1, a.shape[0]):
            lu[i, k] = (a[i, k] - lu[i, : k] * lu[: k, k]) / lu[k, k]

    l = lu.copy()
    for i in range(l.shape[0]):
        l[i, i] = 1
        l[i, i + 1:] = 0

    u = lu.copy()
    for i in range(1, u.shape[0]):
        u[i, :i] = 0

    return np.matrix(l), np.matrix(u)


def find_inv(a, l, u):
    n = len(a)
    a_inv = np.matrix(np.zeros([a.shape[0], a.shape[1]]))
    # a_inv = []
    # for i in range(0, n):
    #     row = []
    #     for j in range(0, n):
    #         row.append(0)
    #     a_inv.append(row)

    for i in range(n - 1, -1, -1):
        for j in range(n - 1, -1, -1):
            if i < j:
                sum = 0
                for k in range(i + 1, n):
                    sum += (u[i, k] * a_inv[k, j])
                a_inv[i, j] = (-(1 / u[i, i])) * sum
            elif i == j:
                sum = 0
                for k in range(j + 1, n):
                    sum += (u[j, k] * a_inv[k, j])
                a_inv[j, i] = (1 / u[j, j]) * (1 - sum)
            elif i > j:
                sum = 0
                for k in range(j + 1, n):
                    sum += (l[k, j] * a_inv[i, k])
                a_inv[i, j] = -sum

    print(a_inv)
    print()
    print(np.linalg.inv(a))
    e = np.dot(a, a_inv)
    m = len(e)
    for i in range(0, m):
        x = []
        for j in range(0, m):
            x.append(f"{e[i, j]:.0f}")
        print(x)


if __name__ == '__main__':
    a = fill_matrix(5, 1, 10)

    l, u = decompose_lu(a)

    l_inv = np.linalg.inv(l)
    u_inv = np.linalg.inv(u)
    A_inv = np.dot(u_inv, l_inv)
    find_inv(a, l, u)
