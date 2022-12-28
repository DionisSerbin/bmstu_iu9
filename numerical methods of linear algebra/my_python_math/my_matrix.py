class MyMatrix(object):

    def __init__(self, matrix):
        self.matrix = matrix

    def __getitem__(self, item):
        return self.matrix

    def mult_on_vector(self, vector):

        multed_matrix = []

        for i in range(len(self.matrix)):

            vector_elem = 0

            for j in range(len(vector)):
                # print("a[{},{}]*b[{}]: {}*{} +  ".format(j+1,i+1,j+1,self.matrix[i][j],vector[j]))
                vector_elem += self.matrix[i][j] * vector[j]

            multed_matrix.append(vector_elem)

        return multed_matrix

    def mult_on_matrix(self, matrix, m):

        multed_matrix = []

        if len(m.matrix) != len(self.matrix[0]):

            print("Ошибка в перемножении матриц")

        else:

            n_row_matrix1 = len(self.matrix)
            n_column_matrix1 = len(self.matrix[0])
            n_row_matrix2 = n_column_matrix1
            n_column_matrix2 = len(m.matrix[0])

            for i in range(0, n_row_matrix1):

                temp_matrix = []

                for j in range(0, n_column_matrix2):

                    sum = 0

                    for k in range(0, n_column_matrix1):
                        sum += self.matrix[i][k] * m.matrix[k][j]

                    temp_matrix.append(sum)

                multed_matrix.append(temp_matrix)

        return multed_matrix
