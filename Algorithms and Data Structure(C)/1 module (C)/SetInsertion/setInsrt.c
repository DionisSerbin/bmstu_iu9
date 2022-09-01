#include <stdio.h>

int main()
{
    unsigned int a = 0, b = 0, c, i, k, n, m;
    scanf("%u", &n);
    for (i = 0; i < n; i++)
    {
        scanf("%u", &k);
        a = a | (1 << k);
    }
    scanf("%u", &m);
    for (i = 0; i < m; i++)
    {
        scanf("%u", &k);
        b = b | (1 << k);
    }
    c = a & b;
    for (i = 0; i < 32; i++)
    {
        if (1 & (c >> i))
            printf("%u ", i);
    }
    return 0;
}