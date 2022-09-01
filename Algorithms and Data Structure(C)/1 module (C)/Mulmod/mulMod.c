#include <stdio.h>

int main()
{
    long long i, a, b, c, s = 0;
    scanf("%lli%lli%lli", &a, &b, &c);
    for (i = 63; i>=0; i--)
    {
        s = (s * 2) % c + (((b >> i) & 1) * a) % c;
    }
    if (s >= c) s -= c;
    printf("%lli", s);
    return 0;
} 
