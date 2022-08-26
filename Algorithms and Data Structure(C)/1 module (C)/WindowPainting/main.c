#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main(int argc, char **argv)
{
    
    if (argc!=4)
    {
        printf("Usage: frame <height> <width> <text>");
        return 0;
    }
    int i,j;
    int h=atoi(argv[1]);
    int w=atoi(argv[2]);
    long t=strlen(argv[3]);
    char *s = argv[3];
    
    if(w-t<2||h<3)
    {
        printf("Error");
        return 0;
    }
    for(i=0;i<h;i++)
    {
        for (j=0;j<w;j++)
        {
            if ((i==0)||(i==h-1))
                printf("*");
            else if ((j==0)||(j==w-1))
                printf("*");
            else if ((j>=(w-t)/2)&&(j<(w+t)/2)&&(i==(h-1)/2))
                printf("%c", s[j-(w-t)/2] );
            else 
                printf(" ");
        }
        printf("\n");
    }
    return 0;
}
