#include <stdlib.h>
#include <stdio.h>
#include <math.h>
struct Date {
    int Year, Month, Day;
}**data;
void **distribdaysort(int start, int end, int (*keyDay)(int i), const void* (*mymass)(int i),int n)
{
    int count[end-start+1];
    for(int i=0;i<end-start+1;i++)
        count[i]=0;
    int i=0;
    for (i = 0; i < n; i++)
        count[(*keyDay)(i)-start]++;
    for (i = 1; i < end-start+1; i++)
        count[i] += count[i - 1];
    void** submass = malloc(n * sizeof(void*));
    for (int i = n - 1; i >= 0; i--)
        submass[--count[(*keyDay)(i) - start]] = (*mymass)(i);
    return submass;
}
void **distribmonthsort(int start, int end, int (*keyMonth)(int i), const void* (*mymass)(int i),int n)
{
    int count[end-start+1];
    for(int i=0;i<end-start+1;i++)
        count[i]=0;
    int i=0;
    for (i = 0; i < n; i++)
        count[(*keyMonth)(i)-start]++;
    for (i = 1; i < end-start+1; i++)
        count[i] += count[i - 1];
    void** submass = malloc(n * sizeof(void*));
    for (int i = n - 1; i >= 0; i--)
        submass[--count[(*keyMonth)(i) - start]] = (*mymass)(i);
    return submass;
}
void **distribyearsort(int start, int end, int (*keyYear)(int i), const void* (*mymass)(int i),int n)
{
    int count[end-start+1];
    for(int i=0;i<end-start+1;i++)
        count[i]=0;
    int i=0;
    for (i = 0; i < n; i++)
        count[(*keyYear)(i)-start]++;
    for (i = 1; i < end-start+1; i++)
        count[i] += count[i - 1];
    void** submass = malloc(n * sizeof(void*));
    for (int i = n - 1; i >= 0; i--)
        submass[--count[(*keyYear)(i) - start]] = (*mymass)(i);
    return submass;
}
int keyDay(int i)
{
    return data[i]->Day;
}
int keyMonth(int i)
{
    return data[i]->Month;
}
int keyYear(int i)
{
    return data[i]->Year;
}
void* mymass(int i)
{
    return data[i];
}
void datesort(const void* (*mymass)(int i),int n)
{
    struct Date** buf =distribdaysort(1,31,*keyDay,*mymass,n);
    free(data);
    data=buf;
    buf =distribmonthsort(1,12,*keyMonth,*mymass,n);
    free(data);
    data=buf;
    buf =distribyearsort(1970,2030,*keyYear,*mymass,n);
    free(data);
    data=buf;
}
int main()
{
    int n,i;
    scanf("%d", &n);
    data=malloc(n*sizeof(struct Date*));
    for(i=0;i<n;i++)
    {
        data[i] = malloc(sizeof(struct Date));
        scanf("%d%d%d", &data[i]->Year, &data[i]->Month, &data[i]->Day);
    }
    datesort(*mymass, n);
    for(i=0;i<n;i++)
    {
        printf("%d %d %d\n", data[i]->Year, data[i]->Month, data[i]->Day);
        free(data[i]);
    }
    free(data);
    return 0;
}
