#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#define MAX 100

char *concat(char **s, int n)
{
    int k = 1;
    for(int i = 0; i <= n; i++)
        k += strlen(s[i]);
    char *str = (char *)malloc(k);
    strncpy(str, "", k);
    for(int i = 0; i <= n; i++)
        strncat(str, s[i], MAX);
    strncat(str, "\0", k);
    return str;
}

int main()
{
    int i, n;
    scanf("%i", &n);
    char *s[n];
    for(i = 0; i <= n; i++)
    {
        s[i] = (char *)malloc(MAX);
        gets(s[i]);
    }
    char *sum = concat(s, n);
    printf("%s", sum);
    for(i = 0; i <= n; i++)
        free(s[i]);
    free(sum);
    return 0;
}
