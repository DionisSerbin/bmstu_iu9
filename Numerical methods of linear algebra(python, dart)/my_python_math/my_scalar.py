def scalar_multiply(vector1, vector2):

    if len(vector1) > len(vector2):
        length = len(vector1)
    else:
        length = len(vector2)

    scalar_digit = 0

    for i in range(0, length):
        scalar_digit += vector1[i]*vector2[i]

    return scalar_digit
