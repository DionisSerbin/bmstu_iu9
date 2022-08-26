#include  <stdio.h>

int array[] = {
    110,
    914,
    915
};

const int k = 337;

int compare(unsigned long i)
{
        if (array[i] == k) return 0;
        if (array[i]  < k) return -1;
        return 1;
}

unsigned long binsearch(unsigned long nel, int (*compare)(unsigned long i))
{
    unsigned long i=0,j=nel-1,cmp=0;
    while(i!=j)
    {
        cmp=compare((i+j)/2);
        if (cmp==0)
        {
            i=(i+j)/2;
            break;
        }
        else if (cmp==-1)
            i=(i+j)/2;
        else
            j=(i+j)/2;
        if((j==i+1)&&(compare(i)==-1)&&(compare(j)==1))
            break;
    }
    if (compare(i)==0)
        return i;
    else
        return nel;
}

int main(int argc, char  **argv)
{
        printf("%lu\n", binsearch(3, compare));
        return 0;
}
