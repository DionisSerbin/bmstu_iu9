#include <stdlib.h>
#include <stdio.h>
void selectionsort(int *arr, int left, int right)
{
    int min, temp;
    for (int i = left; i < right; i++)
    {
      min = i;
      for (int j = i + 1; j < right+1; j++)
      {
        if (arr[j] < arr[min])
          min = j;
      }
      temp = arr[i];
      arr[i] = arr[min];
      arr[min] = temp;
    }
}

void qs(int *arr, int first, int last,int scanlim)
{
    if (first < last)
    {
        if(last-first+1<scanlim)
        {
            selectionsort(arr,first,last);
            return;
        }
        int left = first, right = last, middle = arr[(left + right) / 2],mid=(left+right)/2;
        while (left <= right)
        {
            while (arr[left] < middle)
                left++;
            while (arr[right] > middle)
                right--;
            if (left <= right)
            {
                int tmp = arr[left];
                arr[left] = arr[right];
                arr[right] = tmp;
                left++;
                right--;
            }
        }
        if(2*mid>right+left)
               {
                   selectionsort(arr, right+1, last);
                   qs(arr, first, right,scanlim);
               }
               else
               {
                   selectionsort(arr, first, left-1);
                   qs(arr, left, last,scanlim);
               }
    }
}

int main(int argc, const char * argv[])
{
    int n,m,i;
    scanf("%d%d", &n, &m);
    int *a=malloc(n*sizeof(int));
    for(i=0;i<n;i++)
        scanf("%d", &a[i]);
    qs(a,0,n-1,m);
    for(i=0;i<n;i++)
        printf("%d ", a[i]);
    free(a);
    return 0;
}
