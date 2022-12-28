import random
from copy import deepcopy

import numpy as np
from matplotlib import pyplot as plt

import lab1
import lab2
import lab3
from my_python_math.my_matrix import MyMatrix


def step_random_matrix(n,a,b, is_diag):
    if is_diag:
        matrix_n = lab2.increase_diag(lab2.fill_matrix(n, a, b), 4)
    else:
        matrix_n = lab2.fill_matrix(n, a, b)
    xx = [random.uniform(0.9, 1.1) for i in range(n)]
    a = MyMatrix(deepcopy(matrix_n))
    calc_b = a.mult_on_vector(deepcopy(xx))
    a_prepared = lab2.prepare_to_gauss(deepcopy(a.matrix), deepcopy(calc_b))

    calc_x_gauss = lab2.gauss_method(deepcopy(a_prepared))
    calc_x_gauss_rows = lab3.gauss_with_row_permut(deepcopy(a_prepared))
    calc_x_gauss_columns = lab3.gauss_with_column_permut(deepcopy(a_prepared))
    calc_x_gauss_all = lab3.gauss_with_all_permut(deepcopy(a_prepared))

    evkl_diff_gauss = lab2.calc_evklid_diff(deepcopy(xx), calc_x_gauss)
    evkl_diff_gauss_rows = lab2.calc_evklid_diff(deepcopy(xx), calc_x_gauss_rows)
    evkl_diff_gauss_columns = lab2.calc_evklid_diff(deepcopy(xx), calc_x_gauss_columns)
    evkl_diff_gauss_all = lab2.calc_evklid_diff(deepcopy(xx), calc_x_gauss_all)
    return evkl_diff_gauss, evkl_diff_gauss_rows, evkl_diff_gauss_columns, evkl_diff_gauss_all


def draw_many_graphics(x_p, y1, y2, y3, y4):
    print(y1, y2, y3, y4, sep='\n')
    plt.plot(x_p, np.log(y1), color="blue")
    plt.plot(x_p, np.log(y2), color="yellow")
    plt.plot(x_p, np.log(y3), color="red")
    plt.plot(x_p, np.log(y4), color="green")
    plt.legend(["обычный", "строки", "колонны", "все"])
    plt.show()


def compute(flag):
    x = []
    gauss_vec = []
    gauss_rows_vec = []
    gauss_columns_vec = []
    gauss_all_vec = []
    for n in range(3, 100, 5):
        gauss, gauss_rows, gauss_columns, gauss_all = step_random_matrix(n, 0, 10, flag)
        x.append(n)
        gauss_vec.append(gauss)
        gauss_rows_vec.append(gauss_rows)
        gauss_columns_vec.append(gauss_columns)
        gauss_all_vec.append(gauss_all)

    draw_many_graphics(x, gauss_vec, gauss_rows_vec, gauss_columns_vec, gauss_all_vec)


if __name__ == '__main__':
    compute(False)
    compute(True)
    # lab1.draw_chart(x, gauss_vec)
    # lab1.draw_chart(x, gauss_rows_vec)
    # lab1.draw_chart(x, gauss_columns_vec)
    # lab1.draw_chart(x, gauss_all_vec)
