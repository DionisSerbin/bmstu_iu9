
#include <stdio.h>

int main()
{
    long long s1 = 0, s2 = 0, p1 = 1, p2 = 1, k1 = 0, k2 = 0, c, i;
    for (i = 0; i < 8; i++)
    {
        scanf("%lli", &c);
        if (c != 0)
        {
            p1 *= c;
            s1 += c;
        }
        else k1 += 1;
    }
    for (i = 0; i < 8; i++)
    {
        scanf("%lli", &c);
        if (c != 0)
        {
            p2 *= c;
            s2 += c;
        }
        else k2 += 1;
    }
    if ((s1 == s2) & (p1 == p2) & (k1 == k2))
        printf("yes");
    else printf("no");
    return 0;
}

