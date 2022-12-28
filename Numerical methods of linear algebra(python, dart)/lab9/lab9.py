import numpy
import numpy as np
import matplotlib.pyplot as plt

epsilon = 0.001


def fill_matrix(n, a, b):
    m = np.random.random((n, n)) * (b - a) + a
    for i in range(0, n):
        for j in range(0, i):
            m[i][j] = m[j][i]
        m[i][i] = m[i][i] * 10
    return m


def fill_vector(n, a, b):
    return (np.random.random((n, n)) * (b - a) + a)[0]


def norma_vec(vec):
    max = -float('inf')
    for x in vec:
        if abs(x) > max:
            max = abs(x)

    return max


def calc_evklid_diff(x1, x2):
    x = []
    for i in range(0, len(x1)):
        x.append(abs(x1[i] - x2[i]))

    sum = 0
    for i in range(0, len(x)):
        sum += x[i] ** 2

    return numpy.sqrt(sum)


def own_vals(a):
    return min(numpy.linalg.eigh(a)[0]), max(numpy.linalg.eigh(a)[0])


def draw_charts(ts, iters, min_lamb, max_lamb):
    plt.plot(ts, iters, color='blue')
    plt.plot([2 / (min_lamb + max_lamb) for _ in range(len(iters))], iters, color='red')
    plt.show()


def get_max_mu(own_vals, t):
    mu = []
    for i in range(0, len(own_vals)):
        mu.append((1 - t * own_vals[i]) ** 2)
    return max(mu)


def one_param_method(a, b, n, t):
    x = [1 for i in range(n)]

    p = np.eye(n) - a * t
    g = t * b

    own_vals = numpy.linalg.eigh(a)[0]
    print(f"OWN: {own_vals}")

    max_mu = get_max_mu(own_vals, t)

    iterr = 0
    e_cur = 0
    while True:
        iterr = iterr + 1
        x_temp = p.dot(x) + g

        if norma_vec(x - x_temp) < epsilon:
            return x_temp, iterr

        error_temp = calc_evklid_diff(x_temp, x) ** 2
        print(f"ERROR: {error_temp - max_mu * e_cur}")

        e_cur = error_temp
        x = x_temp


def test_algo():
    n = 5
    left, right = 0.0, 1.0

    b = fill_vector(n, left, right)
    a = fill_matrix(n, left, right)

    min_lamb, max_lamb = own_vals(a)

    t_opt = 2 / (min_lamb + max_lamb)
    t = 0.01
    min_iter = float("inf")
    t_min = 0
    ts = []
    iters = []

    while t < 2 / max_lamb:
        iter_count = one_param_method(a, b, n, t)[1]
        ts.append(t)
        iters.append(iter_count)

        if iter_count < min_iter:
            min_iter = iter_count
            t_min = t

        t += 0.01

    draw_charts(ts, iters, min_lamb, max_lamb)

    print(f"t opt = {t_opt}, calc t = {t_min}")


if __name__ == '__main__':
    test_algo()
