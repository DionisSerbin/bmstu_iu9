from copy import deepcopy

import numpy as np

from lab2 import prepare_to_gauss, gauss_method, calc_evklid_diff, increase_diag
from my_python_math.my_matrix import MyMatrix


def swap_rows(m, column):
    max_el = m[column][column]
    max_row = column
    for i in range(column + 1, len(m)):
        if abs(m[i][column]) > abs(max_el):
            max_el = m[i][column]
            max_row = i
    if max_row != column:
        m[column], m[max_row] = m[max_row], m[column]


def gauss_with_column_permut(m):
    n = len(m)
    x = [0 for i in range(n)]

    for k in range(n - 1):
        swap_rows(m, k)
        for i in range(k + 1, n):
            div = m[i][k] / m[k][k]
            m[i][-1] -= div * m[k][-1]
            for j in range(k, n):
                m[i][j] -= div * m[k][j]

    for k in range(n - 1, -1, -1):
        x[k] = (m[k][-1] - sum([m[k][j] * x[j] for j in range(k + 1, n)])) / m[k][k]

    return x


def swap_colums(m, row, swap_row):
    max_el = m[row][row]
    max_column = row
    for i in range(row + 1, len(m)):
        if abs(m[row][i]) > abs(max_el):
            max_el = m[row][i]
            max_column = i
    if max_column != row:
        for i in range(0, len(m)):
            m[i][max_column], m[i][row] = m[i][row], m[i][max_column]

    return max_column, row


def get_key(mass, val):
    for i in range(0, len(mass)):
        if val == mass[i]:
            return i

    return "key doesn't exist"


def make_swap(swapped_rows, vect):
    new_vect = []
    for i in range(0, len(swapped_rows)):
        ind = get_key(swapped_rows, i)
        new_vect.append(vect[ind])

    return new_vect


def gauss_with_row_permut(m):
    swaped_rows = []
    for i in range(0, len(m)):
        swaped_rows.append(i)
    n = len(m)
    x = [0 for i in range(n)]

    for k in range(n - 1):
        max_column, row = swap_colums(m, k, swaped_rows)
        swaped_rows[max_column], swaped_rows[row] = swaped_rows[row], swaped_rows[max_column]
        for i in range(k + 1, n):
            div = m[i][k] / m[k][k]
            m[i][-1] -= div * m[k][-1]
            for j in range(k, n):
                m[i][j] -= div * m[k][j]

    for k in range(n - 1, -1, -1):
        x[k] = (m[k][-1] - sum([m[k][j] * x[j] for j in range(k + 1, n)])) / m[k][k]

    x = make_swap(swaped_rows, x)

    return x


def find_swap_max(m, column, row):
    max_el_row = m[column][column]
    max_row = column
    for i in range(column + 1, len(m)):
        if abs(m[i][column]) > abs(max_el_row):
            max_el_row = m[i][column]
            max_row = i

    max_el_column = m[row][row]
    max_column = row
    for i in range(row + 1, len(m)):
        if abs(m[row][i]) > abs(max_el_column):
            max_el_column = m[row][i]
            max_column = i
    if abs(m[max_column][column]) > abs(m[column][max_row]):
        return max_column, column
    else:
        return column, max_row


def gauss_with_all_permut(matr):
    swapped_rows = []
    n = len(matr)
    for k in range(len(matr)):
        swapped_rows.append(k)
    res = [0 for i in range(n)]

    for k in range(len(matr)):
        pos_max, is_row = find_swap_max(matr, k, k)
        if is_row:
            if pos_max != k:
                swapped_rows[pos_max], swapped_rows[k] = swapped_rows[k], swapped_rows[pos_max]

                for row in matr:
                    row[pos_max], row[k] = row[k], row[pos_max]
        else:
            if pos_max != k:
                matr[pos_max], matr[k] = matr[k], matr[pos_max]
        for i in range(k + 1, n):
            div = matr[i][k] / matr[k][k]
            matr[i][-1] -= div * matr[k][-1]
            for j in range(k, n):
                matr[i][j] -= div * matr[k][j]

    for k in range(n - 1, -1, -1):
        res[k] = (matr[k][-1] - sum([matr[k][j] * res[j] for j in range(k + 1, n)])) / matr[k][k]

    return make_swap(swapped_rows, res)


def testing_algo(m, x, func):
    a = MyMatrix(
        deepcopy(m)
    )
    calc_b = a.mult_on_vector(deepcopy(x))

    a_prepared = prepare_to_gauss(deepcopy(a.matrix), calc_b)
    calc_x = func(a_prepared)

    a_numpy = np.array(deepcopy(m))
    x_numpy = np.array(deepcopy(x))
    b_numpy = np.matmul(a_numpy, x_numpy)
    x_numpy_calc = np.linalg.solve(a_numpy, b_numpy)

    evkl_diff = calc_evklid_diff(deepcopy(x), calc_x)
    print(func)
    print("Evklid difference between calculated x and given x: %.30f" % evkl_diff)
    print()
    evkl_diff_numpy = calc_evklid_diff(calc_x, x_numpy_calc)
    print("Evklid difference between calculated x and numpy x: %.30f" % evkl_diff_numpy)
    print("-----")


def test_algo():
    m = [
        [1, 1, 1, 4, 1],
        [2, -3, 4, 5, 5],
        [3, 4, 5, 10, 10],
        [3, 6, 7, 8, 8],
        [3, 3, 3, 3, 6]
    ]

    x = [1.0, 3.0, 5.0, 7.0, 9.0]
    testing_algo(deepcopy(m), deepcopy(x), gauss_method)

    testing_algo(deepcopy(m), deepcopy(x), gauss_with_column_permut)
    testing_algo(deepcopy(m), deepcopy(x), gauss_with_row_permut)
    testing_algo(deepcopy(m), deepcopy(x), gauss_with_all_permut)


if __name__ == '__main__':
    # a = MyMatrix([
    #     [1, 1, 1, 4, 1],
    #     [2, -3, 4, 5, 5],
    #     [3, 4, 5, 10, 10],
    #     [3, 6, 7, 8, 8],
    #     [3, 3, 3, 3, 6]
    # ])
    test_algo()
    # calc_b = a.mult_on_vector(x)
    #
    # a_prepared = prepare_to_gauss(deepcopy(a.matrix), calc_b)
    # calc_x = gauss_method(a_prepared)
    # print(calc_x)
    # b_prepared = prepare_to_gauss(deepcopy(a.matrix), calc_b)
    # calc_xx = gauss_with_column_permut(b_prepared)
    # print(calc_xx)
    # c_prepared = prepare_to_gauss(deepcopy(a.matrix), calc_b)
    # calc_xxx = gauss_with_row_permut(c_prepared)
    # print(calc_xxx)
    # d_prepared = prepare_to_gauss(deepcopy(a.matrix), calc_b)
    # calc_xxxx = gauss_with_all_permut(d_prepared)
    # print(calc_xxxx)
