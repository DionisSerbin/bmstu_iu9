
#include <stdio.h>

int main()
{
    long long n, i, j, m = 0, d;
    scanf("%lli", &n);
    n = abs(n);
    d = sqrt(n);
    char a[d];
    for (i = 0; i <= d; i++) a[i] = 1;
    a[1] = 0;
    for (i = 2; i <= d; i++)
        if (a[i])
        {
            for (j = i*i; j <= d; j += i)
                a[j] = 0;
            if (n % i == 0)
            {
                while (n % i == 0) n /= i;
                if (i > m) m = i;
            }
        }
    if (n > m) m = n;
    printf("%lli", m);
    return 0;
}