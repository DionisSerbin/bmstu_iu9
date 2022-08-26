#include <stdio.h>
#include <math.h>

int main(int argc, const char * argv[])
{
    signed long n, k, x0, i, a, mult,sum;
    scanf("%ld%ld%ld", &n, &k, &x0);
    sum = 0;
    
    while (n - k >= 0)
    {
        mult=1;
        i=0;
        while (i<k)
        {
            mult = mult*(n-i);
            ++i;
        }
        scanf("%ld", &a);
        sum = sum*x0 + mult*a;
        --n;
    }
    printf("%ld", sum);
    return 0;
}
