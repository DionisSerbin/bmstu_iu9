import math

import lib

ETA = 10


class Prediction:
    # инициализация  с входными данными и ожидаемым ответом
    def __init__(self, sample, prediction):
        self.sample = sample
        self.prediction = prediction


# вывод результата
class ScoreResult:
    # инициализация точности, потери и ожидаемого ответа
    def __init__(self, accuracy, loss, predictions):
        self.accuracy = accuracy
        self.loss = loss
        self.predictions = predictions

    def __str__(self):
        return f"accuracy: {self.accuracy * 100}%, loss: {self.loss}"


# резултаты на слое
class RunResult:
    # активированные нейроны и входные веса
    def __init__(self, activations, weighted_inputs):
        self.activations = activations
        self.weighted_inputs = weighted_inputs


# нейронная сеть
class Network:
    # инициализация слоев, весов, сдвигов
    def __init__(self, layers):
        self.layers = layers
        self.weights = [lib.get_empty_matrix(0, 0)]
        self.biases = [lib.get_empty_vector(0)]
        # рандомное заполнение весов и сдвигов отностильно слоев
        for i in range(1, len(layers)):
            self.weights.append(lib.get_random_matrix(layers[i], layers[i - 1], -1, 1))
            self.biases.append(lib.get_random_vector(layers[i], -1, 1))

    # тренировка
    def train(self, samples, epochs):
        # для каждой эпохи запускаем процесс тренировки
        for epoch in range(epochs):
            print(f"эпоха: {epoch + 1}/{epochs}")
            errors = []
            activations = []
            # для примера
            for sample in samples:
                # запсукаем прямой ход
                result = self.run(sample)
                # записываем выходные результаты
                activations.append(result.activations)
                # записывем текущую ошибку для обратного распространения ошибки
                cur_errors = self.backprop(sample, result)
                # добавляем ошибки в список ошибок
                errors.append(cur_errors)
            self.descend(samples, activations, errors)

    # тест
    def test(self, samples, epochs):
        # для каждой слоя запускаем процесс тренировки
        for epoch in range(epochs):
            print(f"эпоха: {epoch + 1}/{epochs}")
            activations = []
            # для примера
            for sample in samples:
                # запсукаем прямой ход
                result = self.run(sample)
                # записываем выходные результаты
                activations.append(result.activations)

    # запуск прямого хода
    def run(self, sample):
        #  создаем выходные результаты и инициализируем пустым вектором
        activations = [lib.get_empty_vector(layer) for layer in self.layers]
        # первая выходной результат это входной пример
        activations[0] = sample.input
        # создаем веса и инициализируем пустым вектором
        weighted_inputs = [lib.get_empty_vector(layer) for layer in self.layers]
        # для кажого скрытого слоя
        for j in range(1, len(self.layers)):
            # вычисляем веса и добваляем сдвиги
            weighted_inputs[j] = (
                self.weights[j]
                .multiply_vector(activations[j - 1])
                .add_vector(self.biases[j])
            )
            # высчитываем выходные с помощью сигмоиды
            activations[j] = weighted_inputs[j].map(self.calc_activation)
        return RunResult(activations, weighted_inputs)

    # обратное распространение ошибки
    def backprop(self, sample, result):
        #  создаем ошибки и инициализируем пустым вектором
        errors = [lib.get_empty_vector(layer) for layer in self.layers]
        # создаеи функцию стомости
        nabla_cost = self.calc_nabla_cost(sample.expected, result.activations[-1])
        errors[-1] = nabla_cost.hadamar_product(
            result.weighted_inputs[-1].map(self.calc_activation_derivative)
        )
        # идем обратно и высчитываем наши ошибки
        for j in reversed(range(1, len(self.layers) - 1)):
            errors[j] = (
                self.weights[j + 1]
                .transpose()
                .multiply_vector(errors[j + 1])
                .hadamar_product(result.weighted_inputs[j].map(self.calc_activation_derivative))
            )
        return errors

    # градиентный спуск
    def descend(self, samples, activations, errors):
        for i in range(1, len(self.layers)):
            # создаем высчитанные вектора весов и сдвигов
            acc_weights = lib.get_empty_matrix(self.layers[i], self.layers[i - 1])
            acc_biases = lib.get_empty_vector(self.layers[i])
            # заполняем высчитанные веса
            for j in range(len(samples)):
                acc_weights = acc_weights.add_matrix(
                    errors[j][i]
                    .to_matrix()
                    .multiply_matrix(activations[j][i - 1].to_matrix().transpose())
                )
                acc_biases = acc_biases.add_vector(errors[j][i])
            factor = -ETA / len(samples)
            # обучаем и изменяем веса и сдвиги для обучения
            self.weights[i] = self.weights[i].add_matrix(
                acc_weights.multiply_scalar(factor)
            )
            self.biases[i] = self.biases[i].add_vector(
                acc_biases.multiply_scalar(factor)
            )

    #  считаем результат
    def score(self, samples):
        cost = 0
        accurate = 0
        predictions = []
        for sample in samples:
            res = self.run(sample)
            out = res.activations[-1]
            pred = Prediction(sample, out)
            predictions.append(pred)
            cost += self.calc_cost(sample.expected, out)
            if out.get_max_index() == sample.expected.get_max_index():
                accurate += 1
        return ScoreResult(accurate / len(samples), cost / len(samples), predictions)

    # считаем стоимость ошибки
    def calc_cost(self, expected, out):
        res = 0
        for y, a in zip(expected.values, out.values):
            res += - (y * math.log(a) + (1 - y) * math.log(1 - a))
        return res

    # вектор частных производных C по a_i, где C - функция потерь, a_i - вывод(активация) выходного нейрона с индексом i
    def calc_nabla_cost(self, expected, activations):
        res = lib.get_empty_vector(activations.elements)
        for i in range(activations.elements):
            res.values[i] = (1 - expected.values[i]) / (1 - activations.values[i]) - expected.values[i] / \
                            activations.values[i]
        return res

    def calc_activation(self, x):
        return lib.sigmoid(x)

    def calc_activation_derivative(self, x):
        return lib.sigmoid_derivative(x)


def find_max_index(vec):
    m = max(vec)
    for i in range(0, len(vec)):
        if vec[i] == m:
            return i


# создаем сеть  с 24 входными нейронами и 10 выходными
network = Network([24, 10])
# получаем примеры
samples = lib.get_samples()
# тренируем на 100 слоях
network.train(samples, 100)
# считаем результат
# score = network.score(samples)
# for pred in score.predictions:
#     print("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
#     print("Значения выходных нейронов:")
#     print(pred.prediction.values)
#     print("Ожидаемый результат")
#     print(pred.sample.expected.values)
#     for i in range(0, len(pred.sample.expected.values)):
#         if pred.sample.expected.values[i] == 1:
#             print("Ожидваемый результат: {}".format(i))
#             print("полученное значение:  {}".format(pred.prediction.values[i]))
# print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
# print(score)
network.test(samples, 100)
score = network.score(samples)
for pred in score.predictions:
    print(
        "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
    print("Значения выходных нейронов:")
    print(pred.prediction.values)
    print("Ожидаемый результат")
    print(pred.sample.expected.values)
    for i in range(0, len(pred.sample.expected.values)):
        if pred.sample.expected.values[i] == 1:
            print("Ожидваемый результат: {}".format(i))
            print("полученное значение:  {} с вероятностью - {}".format(find_max_index(pred.prediction.values),
                                                                        max(pred.prediction.values)))
print(
    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
print(score)
