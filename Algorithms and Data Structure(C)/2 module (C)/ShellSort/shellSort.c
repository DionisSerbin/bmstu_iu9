#include <stdlib.h>
#include <stdio.h>
 
int *array;
 
int compare(unsigned long i, unsigned long j)
{
        if (i <= j) {
                printf("COMPARE␣%d␣%d\n", i, j);
        } else {
                printf("COMPARE␣%d␣%d\n", j, i);
        }
 
        if (array[i] == array[j]) return 0;
        return array[i] < array[j] ? -1 : 1;
}
 
void swap(unsigned long i, unsigned long j)
{
        if (i <= j) {
                printf("SWAP␣%d␣%d\n", i, j);
        } else {
                printf("SWAP␣%d␣%d\n", j, i);
        }
 
        int t = array[i];
        array[i] = array[j];
        array[j] = t;
}
 
void shellsort(unsigned long nel,
        int (*compare)(unsigned long i, unsigned long j),
        void (*swap)(unsigned long i, unsigned long j))
{
    int a[nel+2];
    a[0]=0;
    a[1]=1;
    a[2]=1;
    int f=2;
    while (a[f]<=nel)
    {
        a[f+1]=a[f]+a[f-1];
        f++;
    }
    f--;
    int d, i,j;
    d=a[f];
        while(f!=1)
        {
            for (i = d; i < nel; i++)
            
                for (j = i; j >= d; j -= d)
                {
                    if (compare(j-d,j)==1)
                        swap(j-d,j);
                    else
                        break;
                }
            
            f--;
            d=a[f];
        }

}
 
int main(int argc, char **argv)
{
        int i, n;
        scanf("%d", &n);
 
        array = (int*)malloc(n * sizeof(int));
        for (i = 0; i < n; i++) scanf("%d", array+i);
 
        shellsort(n, compare, swap);
        for (i = 0; i < n; i++) printf("%d␣", array[i]);
        printf("\n");
 
        free(array);
        return 0;
}
