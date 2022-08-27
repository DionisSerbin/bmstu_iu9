#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
char a[1000000];
int min(int  a, unsigned long b)
{
    if(a<b)
        return a;
    else
        return b;
}
int querry(int *T,int i)
{
    int v=0;
    while(i>=0)
    {
        v^=T[i];
        i=(i&(i+1))-1;
    }
    return v;
}
int Query(int *T,int l,int r)
{
    int v = querry(T,r)^querry(T,l-1);
    int numofdrom = 0;
    while(v >= 1){
        numofdrom += (v % 2);
        v /= 2;
    }
    return numofdrom;
}
int build(char *a, int l, int r, unsigned long n,int *T)
{
    int sum=0,m;
    int bound=min(r,n);
    while(l<bound)
    {
        m=(l+r)/2;
        sum^=build(a,l,m,n,T);
        l=m+1;
    }
    if(r<n)
    {
        sum ^= 1 << (a[r] - 97);
        T[r]=sum;
    }
    return sum;
}
int Build(char *a,unsigned long n,int *T,int d)
{
    for(int i=0;i<d;i++)
        T[i]=0;
    return build(a,0,d-1,n,T);
}
void upd(int was,char will,char a, int *T,unsigned long n)
{
    while(was<n)
    {
        T[was]^= (1 << (will - 97)) ^ (1 << (a - 97));
        was|=was+1;
    }
}
int main(int argc, const char * argv[])
{
    int i,first,second,l;
    scanf("%s", a);
    unsigned long n=strlen(a);
    int d=1;
    while(d < n)
        d*=2;
    int *T=malloc(d*sizeof(int));
    Build(a,n,T,d);
    int m;
    char str[n + 1];
    char word[20];
    scanf("%d", &m);
    for(i=0;i<m;i++)
    {
        scanf("%s",word);
        if(word[0]=='H')
        {
            scanf("%d%d",&first,&second);
            if(Query(T, first, second) == (second - first + 1) % 2)
                printf("YES\n");
            else
                printf("NO\n");
        }
        else
        {
            scanf("%d %s", &l, str);
            int n2=strlen(str);
            for(int j = 0;  j < n2; j++)
            {
                upd(l + j, str[j], a[l + j],T, n);
                a[l+j] = str[j];
            }
        }
    }
    free(T);
    return 0;
}

