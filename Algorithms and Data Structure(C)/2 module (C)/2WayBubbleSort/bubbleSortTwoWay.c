#include <stdlib.h>
#include <stdio.h>
#include <math.h>
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
 
void bubblesort(unsigned long nel,
        int (*compare)(unsigned long i, unsigned long j),
        void (*swap)(unsigned long i, unsigned long j))
{
    int i=0,j,t;
    int left=1;
    int right=nel-1;
    
    while (left<=right)
    {
        t=0;
        for(j=left;j<=right;j++)
            if (compare(j-1,j)==1)
            {
                swap(j-1,j);
                t=j;
            }
        if(t==0)
        {
            break;
        }
        else
            right =t-1;
        t=nel;
        for(j=right;j>=left;j--)
            if (compare(j-1,j)==1)
            {
                swap(j-1,j);
                t=j-1;
            }
        if(t==nel)
        {
            break;
        }
        else
            left=t+2;
        i++;
    }
}
 
int main(int argc, char **argv)
{
        int i, n;
        scanf("%d", &n);
 
        array = (int*)malloc(n * sizeof(int));
        for (i = 0; i < n; i++) scanf("%d", array+i);
 
        bubblesort(n, compare, swap);
        for (i = 0; i < n; i++) printf("%d␣", array[i]);
        printf("\n");
 
        free(array);
        return 0;
}
