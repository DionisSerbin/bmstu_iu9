#include <stdio.h>
#include <string.h>
#include <stdlib.h>
void *prefix(char *str)
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
    return p;
}
void KMPSubst(char *s,char *t)
{
    int *p=prefix(s);
    int q=0,k=0;
    int Tsize=strlen(t);
    int Ssize=strlen(s);
    p=realloc(p,(Ssize+Tsize)*sizeof(char*));
    while(k<Tsize)
       {
           while(q>0 && s[q]!=t[k])
               q=p[q-1];
           if (s[q]==t[k])
               q++;
           p[k+Ssize]=q;
           if (q==Ssize)
           {
               q=0;
           }
           k++;
       }
    int f = 1;
    for (int i = Ssize; i < (Tsize+Ssize) && f; i++)
        if (!p[i])
            f = !f;
    if(f)
        printf("yes");
    else
        printf("no");
    free(p);
}
int main(int argc, char *argv[])
{
    char* S = malloc(100*sizeof(char));
    gets(S);
    S=realloc(S, strlen(S)*sizeof(char));
    char* T = malloc(100*sizeof(char));
    gets(T);
    T=realloc(T, strlen(T)*sizeof(char));
    KMPSubst(S, T);
    return 0;
}
