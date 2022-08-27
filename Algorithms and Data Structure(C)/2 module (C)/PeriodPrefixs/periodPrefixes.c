#include <stdio.h>
#include <string.h>
#include <stdlib.h>
void prefix(char *str)
{
    unsigned long lenSTR=strlen(str);
    int *p=malloc(lenSTR*sizeof(char*));
    int t=0;
    p[0]=t;
    int i=1;
    while(i<lenSTR)
    {
        while(t>0 && str[t]!=str[i])
            t=p[t-1];
        if (str[t]==str[i])
            t++;
        p[i]=t;
        i++;
    }
    for (int i = 1; i < strlen(str); i++)
    if (!((i + 1) % (i + 1 - p[i])) && ((i + 1) / (i + 1 - p[i]) > 1))
        printf("%d %d\n", i + 1, (i + 1) / (i + 1 - p[i]));
    free(p);
}
int main(int argc, char *argv[])
{
    char* s = argv[1];
    prefix(s);
    return 0;
}
