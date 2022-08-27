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
    int ans;
    while(k<Tsize)
    {
        while(q>0 && s[q]!=t[k])
            q=p[q-1];
        if (s[q]==t[k])
            q++;
        if (q==Ssize)
        {
            ans=k-Ssize+1;
            printf("%d ", ans);
        }
        k++;
    }
    free(p);
}
int main(int argc, char *argv[])
{
    char* S = argv[1];
    char* T = argv[2];
    KMPSubst(S, T);
    return 0;
}
