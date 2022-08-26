#include  <stdio.h>

int strdiff(char *a, char *b)
{
    int i=0,j;
    while(*a && *b && *a == *b)
    {
        a++;
        b++;
        i++;
    }
    
    for (j=0;j<8&&(((*a>>j)&1)==((*b>>j)&1));j++);
    if (!*a && !*b)
        return -1;
    else return i*8+j;
}


int main(int argc, char **argv)
{
        char s1[1000], s2[1000];
        gets(s1);
        gets(s2);
        printf("%d\n", strdiff(s1, s2));

        return 0;
}
