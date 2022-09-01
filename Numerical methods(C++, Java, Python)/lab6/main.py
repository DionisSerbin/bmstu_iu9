import math

x_0 = [3, 1.0]


def df1_x(x):
    return -math.sin(x-1)


def df1_y(y):
    return 1


def df2_x(x):
    return 1


def df2_y(y):
    return math.sin(y)


def func1(x, y):
    return math.cos(x-1)+y-0.8


def func2(x,y):
    return x-math.cos(y)-2


def get_matrix(x,y):
    return [
        [df1_x(x), df1_y(y)],
        [df2_x(x), df2_y(y)]
    ]


def gauss(d, m):
    for k in range(0, len(d)):
        max = k
        for i in range(k+1, len(d)):
            if abs(m[i][k]) > abs(m[max][k]):
                max = i

        temp = m[k]
        m[k] = m[max]
        m[max] = temp

        t = d[k]
        d[k] = d[max]
        d[max] = t

        for i in range(k+1, len(d)):
            fact = m[i][k]/m[k][k]
            d[i] -= fact*d[k]
            for j in range(k, len(d)):
                m[i][j] -= fact*m[k][j]

    out = []
    for i in range(0,len(d)):
        out.append(0.0)

    for i in range(len(d)-1, -1, -1):
        sum = 0.0
        for j in range(i+1, len(d)):
            sum += m[i][j]*out[j]
        out[i] = (d[i] - sum) / m[i][i]

    return out


def my_max(x, y):
    return x if x > y else y


if __name__ == '__main__':
    x_matr = get_matrix(x_0[0], x_0[1])
    f_vector = [-func1(x_0[0], x_0[1]), -func2(x_0[0], x_0[1])]
    y = gauss(f_vector, x_matr)
    x_k = [
        x_0[0] + y[0],
        x_0[1] + y[1]
    ]
    print(x_k[0], x_k[1])
    diff = my_max(abs(x_k[0] - x_0[0]), abs(x_k[1] - x_0[1]))
    while diff > 0.001:
        x_k_1 = x_k
        x_matr = get_matrix(x_k[0], x_k[1])
        f_vector = [-func1(x_k[0], x_k[1]), -func2(x_k[0], x_k[1])]
        y = gauss(f_vector, x_matr)
        x_k = [
            x_k[0] + y[0],
            x_k[1] + y[1]
        ]
        print(x_k[0], x_k[1])
        diff = my_max(abs(x_k[0] - x_k_1[0]), abs(x_k[1] - x_k_1[1]))





