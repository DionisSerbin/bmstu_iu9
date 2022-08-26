
#include <stdio.h>

unsigned char array[] = {
    153,
    1,
    15,
    191,
    232,
    251,
    27,
    174,
    26,
    3,
    68,
    48
};
int maxarray(void  *base, unsigned long nel, unsigned long width,
             int (*compare)(void *a, void *b))
{
    int i,j,z;
    z=0;

    for(i=0;i<nel;i++)
        for(j=0;j<width;j++)
            if ((*compare)((void *)((char *)base+z*width+j),(void *)((char *)base +i*width+j)) > 0)
                break;
            else if ((*compare)((void *)((char *)base+z*width+j),(void *)((char *)base +i*width+j)) < 0)
            {
                z=i;
                break;
            }
    return z;
}
int compare(void  *a, void  *b)
{
        int va =  *(int*)a;
        int vb =  *(int*)b;
        if (va == vb) return 0;
        return va  < vb ? -1 : 1;
}



int main(int argc, char  **argv)
{
        printf("%d\n", maxarray(array, 5, sizeof(int), compare));
        return 0;
}
