#include <stdio.h>
#include <stdlib.h>
#include <string.h>
char *fibstr(int n)
{
    int i, a = 1, b = 1, len = 2;
    for(i = 4; i <= n; i++)
    {
        len += b;
        a = b;
        b = len - b;
    }
    len+=1;
    char *s = (char*)malloc(len);
    if (n==0) {strncpy(s, "\0", len); return s;}
    if (n==1) {strncpy(s, "a\0", len); return s;}
    if (n==2) {strncpy(s, "b\0", len); return s;}
    char *sa = (char*)malloc(len), *sb = (char*)malloc(len);
    strncpy(sb, "a", len);
    strncpy(s, "b", len);
    for(int i = 3; i <= n; i++)
    {
        strncpy(sa, sb, len);
        strncpy(sb, s, len);
        strncpy(s, sa, len);
        strncat(s, sb, len);
    }
    strncat(s, "\0", len);
    free(sa);
    free(sb);
    return s;
}
int main()
{
    int n;
    scanf("%d", &n);
    char *str = fibstr(n);
    puts(str);
    free(str);
    return 0;
}