
#include <stdio.h>

int main()
{
    long n, i, k, sum = 0, max = 0;
    scanf("%li", &n);
    long a[n];
    for (i = 0; i < n; i++) scanf("%li", &a[i]);
    scanf("%li", &k);
    for (i = 0; i < k; i++) sum += a[i];
    max = sum;
    for (i = k; i < n; i++)
    {
        sum = sum - a[i-k] + a[i];
        if (sum > max) max = sum;
    }
    printf("%li", max);
    return 0;
}
