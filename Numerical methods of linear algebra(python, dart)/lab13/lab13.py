import copy
import time
from multiprocessing.pool import ThreadPool
import matplotlib.pyplot as plt
import numpy as np



def fill_matrix(n, a, b):
    m = np.random.random((n, n)) * (b - a) + a
    return m


def split(matrix):
    row, col = matrix.shape
    row2, col2 = row // 2, col // 2
    return matrix[:row2, :col2], matrix[:row2, col2:], matrix[row2:, :col2], matrix[row2:, col2:]


def strass(a, b, n_min):
    if len(a) <= n_min:
        return multiply(a, b)

    a11, a12, a21, a22 = split(a)
    b11, b12, b21, b22 = split(b)

    p1 = strass(a11 + a22, b11 + b22, n_min)
    p2 = strass(a21 + a22, b11, n_min)
    p3 = strass(a11, b12 - b22, n_min)
    p4 = strass(a22, b21 - b11, n_min)
    p5 = strass(a11 + a12, b22, n_min)
    p6 = strass(a11 - a21, b11 + b12, n_min)
    p7 = strass(a12 - a22, b21 + b22, n_min)

    c11 = p1 + p4 - p5 + p7
    c12 = p3 + p5
    c21 = p2 + p4
    c22 = p3 + p1 - p2 - p6

    a21 = np.vstack((np.hstack((c11, c12)), np.hstack((c21, c22))))

    return a21


def strass_thread(a, b, n_min):
    if len(a) <= n_min:
        return multiply(a, b)

    a11, a12, a21, a22 = split(a)
    b11, b12, b21, b22 = split(b)

    pool = ThreadPool(processes=7)

    p1 = pool.apply_async(strass, (a11 + a22, b11 + b22, n_min)).get()
    p2 = pool.apply_async(strass, (a21 + a22, b11, n_min)).get()
    p3 = pool.apply_async(strass, (a11, b12 - b22, n_min)).get()
    p4 = pool.apply_async(strass, (a22, b21 - b11, n_min)).get()
    p5 = pool.apply_async(strass, (a11 + a12, b22, n_min)).get()
    p6 = pool.apply_async(strass, (a11 - a21, b11 + b12, n_min)).get()
    p7 = pool.apply_async(strass, (a12 - a22, b21 + b22, n_min)).get()

    c11 = p1 + p4 - p5 + p7
    c12 = p3 + p5
    c21 = p2 + p4
    c22 = p3 + p1 - p2 - p6

    a21 = np.vstack((np.hstack((c11, c12)), np.hstack((c21, c22))))

    return a21


def multiply(a, b):
    c = np.matrix(np.zeros([a.shape[0], a.shape[1]]))
    for i in range(c.shape[0]):
        for j in range(c.shape[0]):
            for k in range(c.shape[0]):
                c[i, j] += a[i, k] * b[k, j]
    return c


def compare():
    i = 6
    power = []
    time_strass = []
    time_mul = []
    time_th = []
    while np.power(2, i) < np.power(2, 10):
        power.append(i)

        a = fill_matrix(np.power(2, i), 1, 10)
        b = fill_matrix(np.power(2, i), 1, 10)

        strass_time_now = time.time()
        strass_matrix = strass(copy.deepcopy(a), copy.deepcopy(b), 8)
        strass_time_after = time.time()

        print(f"strass time for {np.power(2, i)}: {strass_time_after - strass_time_now}")

        mult_time_now = time.time()
        mult_matrix = multiply(copy.deepcopy(a), copy.deepcopy(b))
        mult_time_after = time.time()

        print(f"mult time for {np.power(2, i)}:   {mult_time_after - mult_time_now}")

        th_time_now = time.time()
        th_matrix = strass_thread(a, b, 8)
        th_time_after = time.time()

        print(f"threading time for {np.power(2, i)}:   {th_time_after - th_time_now}")

        time_strass.append(strass_time_after - strass_time_now)
        time_mul.append(mult_time_after - mult_time_now)
        time_th.append(th_time_after - th_time_now)

        print()
        print(f"Соотношение практическое: {time_strass[-1] / time_mul[-1]}")
        print(f"Соотношение формульное:   {7 / 8}")
        print()
        i += 1

    plt.plot(power, time_strass, 'red')
    plt.plot(power, time_mul, 'blue')
    plt.plot(power, time_th, 'green')
    plt.show()

if __name__ == '__main__':
    compare()
