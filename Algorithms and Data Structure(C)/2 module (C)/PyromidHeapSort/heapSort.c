#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
int compare(char* a, char* b)
{
    int first=0,second=0;
    for(int i = 0; i<=strlen(a); i++)
        if(a[i] == 'a')
            first++;
    for(int i = 0; i<=strlen(b); i++)
        if(b[i] == 'a')
            second++;
    return first-second;
}
void heapify(char **a,int i, int n, int *p)
{
    int l,r,j;
    for(;;)
    {
        l = 2 * i + 1;
        r = l + 1;
        j = i;
        if (l < n && compare(*(char**)((char*)a + sizeof(char*) * p[i]),
            *(char**)((char*)a + sizeof(char*) * p[l])) < 0)
            i = l;
        if (r < n && compare(*(char**)((char*)a + sizeof(char*) * p[i]),
            *(char**)((char*)a + sizeof(char*) * p[r])) < 0)
            i = r;
        if(i == j)
            break;
        int del=p[i];
        p[i]=p[j];
        p[j]=del;
    }
}
void buildheap(char **a, int n, int *p)
{
    int i = n/2 - 1;
    while(i >= 0)
    {
        heapify(a,i,n, p);
        i--;
    }
}
void Heapsort(char **a, int n)
{
    int *p=malloc(n*sizeof(int));
    for(int i=0;i<n;i++)
    {
        p[i] = i;
    }
    buildheap(a,n, p);
    int i = n - 1;
    while(i>0)
    {
        int del = p[0];
        p[0] = p[i];
        p[i]=del;
        heapify(a, 0, i, p);
        i--;
    }
    char** b = malloc(n * sizeof(char*));
    for (i = 0; i < n; i++)
        b[i] = a[ p[i] ];
    for (i = 0; i < n; i++)
        a[i] = b[i];
    free(p);
    free(b);
}
int main(int argc, const char * argv[])
{
    int n,i;
    scanf("%d", &n);
    char **a=malloc(n*sizeof(char*));
    for(i = 0;i < n;i++)
    {
        a[i]=malloc(20*sizeof(char));
        scanf("%s", a[i]);
    }
    Heapsort(a, n);
    for(i = 0;i < n;i++)
        printf("%s\n", a[i]);
    for(i=0;i<n;i++)
        free(a[i]);
    free(a);
    return 0;
}
