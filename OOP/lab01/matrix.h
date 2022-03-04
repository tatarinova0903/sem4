#ifndef MATRIX_H
#define MATRIX_H

#include <stdio.h>
#include <stdlib.h>
#include"rc.h"

typedef int ** matrix_t;

void free_matrix(matrix_t mat, size_t n);
ReturnCode allocate_matrix(matrix_t &matrix, size_t n);


#endif // MATRIX_H
