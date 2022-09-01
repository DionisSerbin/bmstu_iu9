#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAX 100

int wcount(char *s)
{
    int sum = 0, n = strnlen(s, MAX);
    if (n == 0) return 0;
    for(int i = 0; i <= n-2; i++)
        if (s[i] != ' ' && s[i+1] == ' ') sum++;
    if (s[n-1] != ' ') sum++;
    return sum;
}
int main()
{
    char *str = (char*)malloc(MAX);
    if (str == NULL) return -1;
    gets(str);
    printf("%i", wcount(str));
    free(str);
    return 0;
}
