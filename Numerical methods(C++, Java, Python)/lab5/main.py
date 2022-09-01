import math


def exp_func(x):
    return math.exp(x)


def exp_q_func(x):
    return -1


def exp_p_func(x):
    return 1


x_exp_A = 0
x_exp_B = 1

y_exp_A = exp_func(x_exp_A)
y_exp_B = exp_func(x_exp_B)

n = 100

h_exp = (x_exp_B - x_exp_A) / n

f_exp = [0] * n
p_exp = [0] * n
q_exp = [0] * n


def my_func(x):
    return 2 * x


def my_func_d(x):
    return 2


def my_func_dd(x):
    return 0


def new_func(x):
    return x + math.exp(x) * (math.sin(x) - math.cos(x)) + 1


def my_p_func(x):
    return -2


def my_q_func(x):
    return 2


x_my_func_A = 0
x_my_func_B = 1

# y_my_func_A = my_func(x_my_func_A)
# y_my_func_B = my_func(x_my_func_B)

y_my_func_A = 0
y_my_func_B = 2 + math.exp(1) * (math.sin(1) - math.cos(1))

h_my_func = (x_my_func_B - x_my_func_A) / n

f_my_func = [0] * n
p_my_func = [0] * n
q_my_func = [0] * n


def solve_matrix(a, b, c, d):
    size = len(d)
    alpha = [- c[0] / b[0]]
    beta = [d[0] / b[0]]

    for i in range(1, size):
        if i != size - 1:
            alpha.append(- c[i] / (a[i - 1] * alpha[i - 1] + b[i]))
        beta.append((d[i] - a[i - 1] * beta[i - 1]) / (a[i - 1] * alpha[i - 1] + b[i]))

    res = [0.0 for i in range(size)]
    res[-1] = beta[-1]

    i = size - 2
    while i > -1:
        res[i] = alpha[i] * res[i + 1] + beta[i]
        i -= 1

    return res


def count(n, x_A, h, func, p_func, q_func, f, p, q):
    for i in range(0, n):
        x = x_A + i * h
        # f[i] = func_dd(x) + p_func(x) * func_d(x) + q_func(x) * func(x)
        f[i] = func(x)
        p[i] = p_func(x)
        q[i] = q_func(x)


def solve(n, x_A, y_A, y_B, h, f, p, q, func):
    a = []
    b = [q[1] * h * h - 2]
    c = [1 + p[1] * (h / 2)]
    d = [f[1] * h * h - y_A * (1 - p[1] * (h / 2))]

    for i in range(2, n - 1):
        a.append(1 - p[i] * (h / 2))
        b.append(q[i] * h * h - 2)
        c.append(1 + p[i] * (h / 2))
        d.append(f[i] * h * h)

    a.append(1 - p[n - 1] * (h / 2))
    b.append(q[n - 1] * h * h - 2)
    d.append(f[n - 1] * h * h - y_B * (1 + p[n - 1] * (h / 2)))

    res = [y_A]
    for x in solve_matrix(a, b, c, d):
        res.append(x)

    res.append(y_B)

    text = ""

    for i in range(0, n):
        text += "{}: x: {}\n   y: {}\n   solve_result: {}\n   ".format(i, x_A + i * h, func(x_A + i * h), res[i])
        text += "diff: {0:0.20f}\n\n".format(abs(func(x_A + i * h) - res[i]))

    print(text)


if __name__ == '__main__':
    # count(n, x_exp_A, h_exp, exp_func, exp_func, exp_func,
    #       exp_p_func, exp_q_func, f_exp, p_exp, q_exp)
    # solve(n, x_exp_A, y_exp_A, y_exp_B, h_exp,
    #       f_exp, p_exp, q_exp, exp_func)

    count(n, x_my_func_A, h_my_func, new_func,
          my_p_func, my_q_func, f_my_func, p_my_func, q_my_func)
    solve(n, x_my_func_A, y_my_func_A, y_my_func_B, h_my_func,
          f_my_func, p_my_func, q_my_func, new_func)

