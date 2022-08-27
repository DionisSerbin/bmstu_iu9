#include <stdlib.h>
#include <stdio.h>
union Int32 {
    int x;
    unsigned char bytes[4];
}**mass;
void **distrib8(int start, int end, int (*key8)(int i), const void* (*mymass)(int i),int n)
{
    int count[end-start+1];
    for(int i=0;i<end-start+1;i++)
        count[i]=0;
    int i=0;
    for (i = 0; i < n; i++)
        count[(*key8)(i)-start]++;
    for (i = 1; i < end-start+1; i++)
        count[i] += count[i - 1];
    void** submass = malloc(n * sizeof(void*));
    for (int i = n - 1; i >= 0; i--)
        submass[--count[(*key8)(i) - start]] = (*mymass)(i);
    return submass;
}
void **distrib16(int start, int end, int (*key16)(int i), const void* (*mymass)(int i),int n)
{
    int count[end-start+1];
    for(int i=0;i<end-start+1;i++)
        count[i]=0;
    int i=0;
    for (i = 0; i < n; i++)
        count[(*key16)(i)-start]++;
    for (i = 1; i < end-start+1; i++)
        count[i] += count[i - 1];
    void** submass = malloc(n * sizeof(void*));
    for (int i = n - 1; i >= 0; i--)
        submass[--count[(*key16)(i) - start]] = (*mymass)(i);
    return submass;
}
void **distrib24(int start, int end, int (*key24)(int i), const void* (*mymass)(int i),int n)
{
    int count[end-start+1];
    for(int i=0;i<end-start+1;i++)
        count[i]=0;
    int i=0;
    for (i = 0; i < n; i++)
        count[(*key24)(i)-start]++;
    for (i = 1; i < end-start+1; i++)
        count[i] += count[i - 1];
    void** submass = malloc(n * sizeof(void*));
    for (int i = n - 1; i >= 0; i--)
        submass[--count[(*key24)(i) - start]] = (*mymass)(i);
    return submass;
}
void **distrib32(int start, int end, int (*key32)(int i), const void* (*mymass)(int i),int n)
{
    int count[end-start+1];
    for(int i=0;i<end-start+1;i++)
        count[i]=0;
    int i=0;
    for (i = 0; i < n; i++)
        count[(*key32)(i)-start]++;
    for (i = 1; i < end-start+1; i++)
        count[i] += count[i - 1];
    void** submass = malloc(n * sizeof(void*));
    for (int i = n - 1; i >= 0; i--)
        submass[--count[(*key32)(i) - start]] = (*mymass)(i);
    return submass;
}
int key8(int i)
{
    return mass[i]->bytes[0];
}
int key16(int i)
{
    return mass[i]->bytes[1];
}
int key24(int i)
{
    return mass[i]->bytes[2];
}
int key32(int i)
{
    return ((mass[i]->bytes[3])^(1<<7));
}
void* mymass(int i)
{
    return mass[i];
}
void radix_sort(const void* (*mymass)(int i),int n)
{
    union Int32** buf =distrib8(0,255,*key8,*mymass,n);
    free(mass);
    mass=buf;
    buf =distrib16(0,255,*key16,*mymass,n);
    free(mass);
    mass=buf;
    buf =distrib24(0,255,*key24,*mymass,n);
    free(mass);
    mass=buf;
     buf =distrib32(0,255,*key32,*mymass,n);
    free(mass);
    mass=buf;
}
int main()
{
    int n,i;
    scanf("%d", &n);
    mass=malloc(n*sizeof(union Int32*));
    for(i=0;i<n;i++)
    {
        mass[i] = malloc(sizeof(union Int32));
        scanf("%d", &mass[i]->x);
    }
    radix_sort(*mymass, n);
    for(i=0;i<n;i++)
    {
        printf("%d ", mass[i]->x);
        free(mass[i]);
    }
    free(mass);
    return 0;
}
