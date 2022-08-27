#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void insert_sort(int *a, int left, int right) {
    int swap;
    
    for(int j = left; j < right; j++) {
        swap = a[j];
        int i = j - 1;
        while(i >= left && abs(a[i]) > abs(swap)) {
            a[i + 1] = a[i];
            i--;
        }
        a[i + 1] = swap;
    }
}
int* merge_sort(int *mass, int *premass,  int left,  int right)
{
    if (left == right)
    {
        premass[left] = mass[left];
        return premass;
    }
    if(right-(left + right) / 2+1<5)
        insert_sort(mass, (left + right) / 2+1, right);
    if((left + right) / 2-left<5)
        insert_sort(mass, left, (left + right) / 2);
    int *lmass = merge_sort(mass, premass, left, (left + right) / 2);
    int *rmass = merge_sort(mass, premass, (left + right) / 2 + 1, right);
    int *ans= lmass == mass ? premass : mass;
    int lpart = left, rpart = (left + right) / 2 + 1,i;
    for  ( i = left; i <= right; i++)
    {
        if (lpart <= (left + right) / 2 && rpart <= right)
        {
            if (abs(lmass[lpart]) <= abs(rmass[rpart]))
            {
                ans[i] = lmass[lpart];
                lpart++;
            }
            else
            {
                ans[i] = rmass[rpart];
                rpart++;
            }
        }
        else if (lpart <= (left + right) / 2)
        {
            ans[i] = lmass[lpart];
            lpart++;
        }
        else
        {
            ans[i] = rmass[rpart];
            rpart++;
        }
    }
    return ans;
}
int main(int argc, char **argv)
{
    int n;
    scanf("%d", &n);
    int a[n],b[n];
    for(int i = 0; i < n; i++)
        scanf("%d", &a[i]);
    int *aprint=merge_sort(a, b, 0, n-1);
    for (int i = 0; i < n; i++)
        printf("%d ", aprint[i]);
    return 0;
}
