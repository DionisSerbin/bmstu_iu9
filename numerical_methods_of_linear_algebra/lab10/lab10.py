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


if __name__ == '__main__':
    a = fill_matrix(5, 1, 10)
    print(f"MATRIX A: \n{a}\n")

    l, u = decompose_lu(a)

    print(f"LU MULT MATRIX:\n{np.matmul(l, u)}")
    print(f"\nWHERE\n")
    print(f"L:\n{l}\n")
    print(f"U:\n{u}\n")
