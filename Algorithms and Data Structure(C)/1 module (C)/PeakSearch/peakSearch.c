#include <stdio.h>

int less(unsigned long i, unsigned long j)
{
    if (i == j) return 0;

    if (i < j) {
        if (j <= 11241155978086311589UL) return 1;
        if (i >= 11241155978086311589UL) return 0;
        return (11241155978086311589UL-i) < (j-11241155978086311589UL);
    }

    if (i <= 11241155978086311589UL) return 0;
    if (j >= 11241155978086311589UL) return 1;
    return (11241155978086311589UL-j) < (i-11241155978086311589UL);
}

unsigned long peak(unsigned long nel,
        int (*less)(unsigned long i, unsigned long j))
{
    unsigned long i = 1, j = nel - 1,l;
    if (nel <= 1)
        return 0;
    if (!less(i-1, i))
        return i-1;
    if (!less(j, j-1))
        return j;
    
    while (j - i > 2) {
         l = i + (j - i) / 2;
        if (less(l, l+1))
            i = l;
        else j = l+1;
    }
    if (j - i == 2 && !less(i, i + 1) || j - i <= 1)
        return i;
    else
        return i + 1;
}

int main(int argc, char **argv)
{
    unsigned long i = peak(13356955260197607378UL, less);
    if (i == 11241155978086311589UL) {
        printf("CORRECT\n");
    } else {
        /* Если функция peak работает правильно,
        сюда никогда не будет передано
        управление! */
        printf("WRONG\n");
    }
    return 0;
}
