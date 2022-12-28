import math

import numpy as np
import matplotlib.pyplot as plt

from my_python_math.my_matrix import MyMatrix
from my_python_math.my_scalar import scalar_multiply


def test(sc_vector1, sc_vector2, my_matrix, vector_for_matrix, matrix_for_my_matrix):

    nump_sc_vector1 = np.array(sc_vector1)
    nump_sc_vector2 = np.array(sc_vector2)

    print("numpy version: {}".format(np.dot(nump_sc_vector1, nump_sc_vector2)))
    print("my version:    {}".format(scalar_multiply(sc_vector1, sc_vector2)))

    nump_my_matrix = np.array(my_matrix)
    nump_vector_for_matrix = np.array(vector_for_matrix)
    my_matrix_class = MyMatrix(my_matrix)

    print("numpy version multiply on vector: {}".format(np.matmul(nump_my_matrix, nump_vector_for_matrix)))
    print("my version multiply on vector:    {}".format(my_matrix_class.mult_on_vector(vector_for_matrix)))

    matrix_for_my_matrix_class = MyMatrix(matrix_for_my_matrix)
    nump_matrix_for_my_matrix = np.array(matrix_for_my_matrix)

    print("numpy version multiply on matrix: {}".format(np.dot(nump_my_matrix, nump_matrix_for_my_matrix)))
    print("my version multiply on matrix:    {}".format(my_matrix_class.mult_on_matrix(matrix_for_my_matrix_class)))


def error_calculation(x):

    real_result = 0.005051

    formula_power_of_six = (x - 1)**6
    formula_power_of_three = (3 - 2*x)**3
    formula_power_of_one = 99 - 70*x

    print("Погрешность первой формулы при √2 = {}:  {}".format(x, abs(formula_power_of_six - real_result)/real_result))
    print("Погрешность второй формулы при √2 = {}:  {}".format(x, abs(formula_power_of_three - real_result)/real_result))
    print("Погрешность третьей формулы при √2 = {}: {}".format(x, abs(formula_power_of_one - real_result)/real_result))

    y_labels = ["(√2 - 1)^6", "(3 - 2√2)^3", "(99 - 70√2)", "Реальный результат"]

    x_labels = [abs(formula_power_of_six - real_result)/real_result,
        abs(formula_power_of_three - real_result)/real_result,
        abs(formula_power_of_one - real_result)/real_result,
        real_result]

    plt.rcdefaults()

    plt.barh(y_labels, x_labels)
    plt.ylabel("Формула")
    plt.xlabel('Относительная погрешность %')
    plt.title('Оценка погрешности при √2 = {}'.format(x))

    plt.show()


def draw_chart(x, y):
    x = np.array(x)
    y = np.array(y)
    plt.plot(x, y)
    plt.show()


def calc_error_rate(x):
    delta_x = abs(math.sqrt(2) - x)

    first_formula = 6/(math.sqrt(2) - 1)
    second_formula = 6/(3 - 2*math.sqrt(2))
    third_formula = 70/(99 - 70*math.sqrt(2))
    print("Погрешности:   ")
    print(int(first_formula*delta_x*100))
    print(int(second_formula*delta_x*100))
    print(int(third_formula*delta_x*100))

    y_labels = ["(√2 - 1)^6", "(3 - 2√2)^3", "(99 - 70√2)"]
    x_labels = [abs(int(first_formula*delta_x*100)),
                abs(int(second_formula*delta_x*100)),
                abs(int(third_formula*delta_x*100))]

    plt.rcdefaults()

    plt.barh(y_labels, x_labels)
    plt.ylabel("Формула")
    plt.xlabel('Относительная погрешность %')
    plt.title('Оценка погрешности при √2 = {}'.format(x))

    plt.show()


if __name__ == '__main__':

    # Реализовать вспомогательную библиотеку для вычисления скалярного произведения,
    # умножение матрицы на вектор, матрицы наматрицу.
    # Проверить корректность работы с помощью сторонних библиотек.
    vector11 = [1, 2, 3, 4, 5]
    vector12 = [5, 4, 3, 2, 1]
    m11 = [[1, 2, 3],[3, 2, 1],[4, 5, 6]]
    vector13 = [7, 8, 9]
    m12 = [[1, 2, 3],[3, 2, 1],[4, 5, 6]]
    test(vector11, vector12, m11, vector13, m12)

    vector21 = [1, 2, 3, 4, 5, 9, 1, 142]
    vector22 = [5, 4, 3, 2, 1, 7, 78, 35]
    m21 = [[1, 2, 3, 4], [3, 2, 1, 1], [4, 5, 6, 7], [7, 66, 4, 6]]
    vector23 = [7, 8, 9, 123]
    m22 = [[1, 2, 3, 1], [3, 2, 1, 1], [4, 5, 6, 1], [505, 1, 6, 1]]
    test(vector21, vector22, m21, vector23, m22)

    # Реализовать оценку погрешности вычисления шара при различных значениях корней из 2.
    error_calculation(7/5)
    error_calculation(17/12)

    # Реализовать вывод графиков произвольных двумерных функций.
    x = [4, 5, 6, 7, 8]
    y = [1, 2, -6, 0, 4]
    draw_chart(x, y)

    calc_error_rate(7/5)
    calc_error_rate(17/12)



