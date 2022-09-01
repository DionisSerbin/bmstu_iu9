
#include <stdio.h>
#include <limits.h>

int main()
{
    int i, j, n, m;
    scanf("%i%i", &n, &m);
    int a[10][10] = {{0}};
    for (i = 0; i < n; i++)
        for (j = 0; j < m; j++)
            scanf("%i", &a[i][j]);
    int max[10] = {0}, min[10] = {0};
    for (j = 0; j < m; j++)
        min[j] = INT_MAX;
    for (i = 0; i < n; i++)
        for (j = 0; j < m; j++)
            {
                if (a[i][j] > max[i]) max[i] = a[i][j];
                if (a[i][j] < min[j]) min[j] = a[i][j];
            }
    for (i = 0; i < n; i++)
        for (j = 0; j < m; j++)
            if (max[i] == min[j])
            {
                printf("%i %i", i, j);
                return 0;
            }
    printf("none");
    return 0;
}