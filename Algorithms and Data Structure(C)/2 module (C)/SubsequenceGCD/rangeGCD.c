#include <stdio.h>
#include <stdlib.h>
#include <math.h>
int NOD(int n1, int n2)
{
    if(!n1)
        return n2;
    else
    {
        if (n1 == n2)
            return n1;int div;
        int d = n1 - n2;
        if (d < 0)
        {
            d = -d;
            div = NOD(n1, d);
        }
        else
            div = NOD(n2, d);
        return div;
    }
}
int *computelog(int n)
{
    int *lg=malloc((1<<n)*sizeof(int));
    int i=1, j=0;
    while(i<=n)
    {
        while(j<(1<<i))
        {
            lg[j]=i-1;
            j++;
        }
        i++;
    }
    return lg;
}
int querry(int **arr, int l, int r, int *lg)
{
    int j=lg[r-l+1];
    return NOD(arr[l][j],arr[r+1-(1<<j)][j]);
}
int **built(int *u,int n, int *lg)
{
    int m=lg[n]+1;
    int **st=malloc(n*sizeof(int*));
    int i=0;
    while(i<n)
    {
        st[i] = malloc(m * sizeof(int));
        st[i][0]=abs(u[i]);
        i++;
    }
    int j=1;
    while(j<m)
    {
        i=0;
        while(i<=n-(1<<j))
        {
            st[i][j]=NOD(st[i][j-1],st[i+(1<<(j-1))][j-1]);
            i++;
        }
        j++;
    }
    return st;
}
int main(int argc, const char * argv[])
{
    int i,n,n1;
    scanf("%d", &n);
    int *a = malloc(n * sizeof(int));
    for (i = 0; i < n; i++)
        scanf("%d", &a[i]);
    int *lg=computelog(log2f(n) + 1);
    int **st=built(a,n,lg);
    scanf("%d", &n1);
    int l,r;
    for(i=0;i<n1;i++)
    {
        scanf("%d%d", &l,&r);
        printf("%d\n", querry(st,l,r,lg));
    }
    for (i = 0; i < n; i++)
        free(st[i]);
    free(st);
    free(a);
    free(lg);
    return 0;
}
