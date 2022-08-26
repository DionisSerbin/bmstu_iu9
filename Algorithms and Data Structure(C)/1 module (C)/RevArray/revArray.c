#include <stdio.h>
int array[] = {
        10000000,
        20000000,
        30000000,
        40000000,
        50000000
};

void revarray(void *base, unsigned long nel, unsigned long width)
{
    int i,j;
    for(i=0;i<nel/2;i++)
        for(j=0;j<width;j++)
        {
            int dop=*((char*)base + i*width + j);
            *((char*)base + i*width +j) = *((char*)base + (nel-i-1)*width + j);
            *((char*)base + (nel-i-1)*width + j)=dop;
        }
}

int main(int argc, const char * argv[]) {
    
    revarray(array, 5, sizeof(int));

    int i;
    for (i = 0; i < 5; i++) {
            printf("%d\n", array[i]);
    }

    return 0;
}
