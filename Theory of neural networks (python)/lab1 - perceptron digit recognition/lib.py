import math
import random
from PIL import Image


class Sample:
    # инициализация примера с входными данными и ожидаемым ответом
    def __init__(self, input, expected):
        self.input = input
        self.expected = expected

# класс функций матрицы
class Matrix:
    def __init__(self, values):
        self.values = values

    def add_matrix(self, other):
        res = get_empty_matrix(self.rows, self.columns)
        for i in range(self.rows):
            for j in range(self.columns):
                res.values[i][j] = self.values[i][j] + other.values[i][j]
        return res

    def multiply_matrix(self, other):
        res = get_empty_matrix(self.rows, other.columns)
        for i in range(self.rows):
            for j in range(other.columns):
                for k in range(self.columns):
                    res.values[i][j] += self.values[i][k] * other.values[k][j]
        return res

    def multiply_vector(self, other):
        res = get_empty_vector(self.rows)
        for i in range(self.rows):
            for j in range(self.columns):
                res.values[i] += self.values[i][j] * other.values[j]
        return res

    def multiply_scalar(self, value):
        res = get_empty_matrix(self.rows, self.columns)
        for i in range(self.rows):
            for j in range(self.columns):
                res.values[i][j] = value * self.values[i][j]
        return res

    def transpose(self):
        res = get_empty_matrix(self.columns, self.rows)
        for i in range(self.rows):
            for j in range(self.columns):
                res.values[j][i] = self.values[i][j]
        return res

    @property
    def rows(self):
        return len(self.values)

    @property
    def columns(self):
        if self.rows == 0:
            return 0
        return len(self.values[0])


# класс функции вектора
class Vector:
    def __init__(self, values):
        self.values = values

    def get_max_index(self):
        ans = 0
        for i in range(self.elements):
            if self.values[i] > self.values[ans]:
                ans = i
        return ans

    @property
    def length(self):
        res = 0
        for i in self.values:
            res += i**2
        return math.sqrt(res)

    def add_vector(self, other):
        res = get_empty_vector(self.elements)
        for i in range(self.elements):
            res.values[i] = self.values[i] + other.values[i]
        return res

    # покомпонентное произведение векторов
    def hadamar_product(self, other):
        res = get_empty_vector(self.elements)
        for i in range(self.elements):
            res.values[i] = self.values[i] * other.values[i]
        return res

    def to_matrix(self):
        return Matrix([[x] for x in self.values])

    def multiply_scalar(self, value):
        return self.map(lambda x: x * value)

    def map(self, func):
        return Vector(list(map(func, self.values)))

    @property
    def elements(self):
        return len(self.values)


# создает рандомный вектор
def get_random_vector(n, a, b):
    res = get_empty_vector(n)
    for i in range(n):
        res.values[i] = random.uniform(a, b)
    return res


# создавет рандомную матрицу
def get_random_matrix(n, m, a, b):
    res = get_empty_matrix(n, m)
    for i in range(n):
        for j in range(m):
            res.values[i][j] = random.uniform(a, b)
    return res


# создает пустую матрицу
def get_empty_matrix(n, m):
    return Matrix([[0] * m for _ in range(n)])


# создает рандомный вектор
def get_empty_vector(n):
    return Vector([0] * n)


# функция активации
def sigmoid(x):
    return 1 / (1 + math.exp(-x))

# производная функции активации для обратного распространения ошибки функции стоимости
def sigmoid_derivative(x):
    return sigmoid(x) * (1 - sigmoid(x))


# получение примера
def get_samples():
    samples = []
    for i in range(10):
        res = get_empty_vector(10)
        res.values[i] = 1
        for j in range(10):
            name = f"digits/{i}_{j}.bmp"
            image = Image.open(name)
            vec = Vector([])
            for k in range(image.width):
                for l in range(image.height):
                    vec.values.append(image.getpixel((k, l)))
            samples.append(Sample(vec.multiply_scalar(1 / vec.length), res))
    return samples
