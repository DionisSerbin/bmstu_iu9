import math
import random


def exp_func(x):
    return math.exp(x)


def var_func(x):
    return (math.log(x) ** 2) / x


def rectangle(func, a, b, n):
    h = (b - a) / n
    sum = 0
    for i in range(0, n):
        sum += h * func(a + i * h + h / 2)
    return sum


def trapezoid(func, a, b, n):
    h = (b - a) / n
    xs = [a + i * h for i in range(1, n)]
    sum = 0
    for i in range(0, n - 1):
        sum += func(xs[i])

    return h / 2 * (func(a) + func(b) + 2 * sum)


def simpson(func, a, b, n):
    h = (b - a) / n
    xs1 = [a + i * h - h / 2 for i in range(1, n + 1)]
    xs2 = [a + i * h for i in range(1, n)]
    sum = 0
    for i in range(0, n):
        sum += 4 * func(xs1[i])
        if i < n - 1:
            sum += 2 * func(xs2[i])
    return h / 6 * (func(a) + func(b) + sum)


def monte_karlo(func, a, b, n, max, min):
    k = 0
    for i in range(0, n):
        x = a + random.uniform(0, 1)*(b-a)
        y = min + random.uniform(0, 1)*(max - min)
        if y <= func(x):
            k += 1
    integ = (max-min) *(b-a)* (k / n)
    return integ


def monte_karlo_solve(func, a, b, integ, epsilon, max, min):
    k = 2
    iter = 0
    mk = monte_karlo(func, a, b, k, max, min)
    while abs(mk - integ) > epsilon:
        k *= 2
        iter += 1
        mk = monte_karlo(func, a, b, k, max, min)
    print(" Iteration: " + str(iter))
    print(" Result: " + str(mk))
    print(" DIFF: " + str(abs(mk - integ)))


def approx_richardson(i1, i2, k):
    return (i1 - i2) / (2 ** k - 1)


def calc_int(a, b, epsilon, method, k, func):
    print("\n Epsilon: " + str(epsilon))
    n = 1
    richardson = float('inf')
    iter = i_n = 0
    while abs(richardson) >= epsilon:
        n *= 2
        i_2 = i_n
        i_n = method(func, a, b, n=n)
        richardson = approx_richardson(i_n, i_2, k)
        iter += 1
    print(" Iteration: " + str(iter))
    print(" Result: " + str(i_n))
    print(" Result with Richardson " + str(i_n + richardson))
    print(" DIFF: " + str(abs(richardson)))


if __name__ == '__main__':
    epsilon = 0.001
    print("\nExp:" + str(exp_func(1) - exp_func(0)))
    print("\nTrapezoid:")
    calc_int(0, 1, epsilon, trapezoid, 2, exp_func)
    print("\nRectangle:")
    calc_int(0, 1, epsilon, rectangle, 2, exp_func)
    print("\nSimpson:")
    calc_int(0, 1, epsilon, simpson, 4, exp_func)
    print("\nMonte-Karlo:")
    monte_karlo_solve(exp_func, 0, 1, exp_func(1) - exp_func(0), epsilon, exp_func(1), 0)
    l = 1 / exp_func(1)
    r = exp_func(1)

    print("\nTrapezoid:")
    calc_int(l, r, epsilon, trapezoid, 2, var_func)
    print("\nRectangle:")
    calc_int(l, r, epsilon, rectangle, 2, var_func)
    print("\nSimpson:")
    calc_int(l, r, epsilon, simpson, 4, var_func)
    print("\nMonte-Karlo:")
    monte_karlo_solve(var_func, l, r, 2/3, epsilon, exp_func(1), 0)

