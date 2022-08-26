#include <stdio.h>
#include <math.h>

int main(int argc, const char * argv[])
{
    long n,i,x,x1,x0;
    scanf("%ld", &n);
    scanf("%ld", &x);
    x1=2;
    x0=2;
    i=0;
    if ((n>1)&&(x==1))
    {
        printf("%d ", 0);
        for(i=0;i<(n-1);i+=2){
            scanf("%ld%ld", &x1, &x0);
            if ((x1==0)&&(x0==1))
                printf("%d %d ", 0, 0);
            if ((x1==0)&&(x0==0)){
                printf("%d %d ", 1, 0);
                i+=n;
            }
        }
    }
    else if ((n>1)&&(x==0)){
        scanf("%ld", &x1);
        if(x1==0){
            printf("%d %d ", 1,0);
            i+=n;
        }
        else{
            printf("%d %d ", 0, 0);
            for(i=1;i<(n-1);i+=2){
                scanf("%ld%ld", &x1, &x0);
                if ((x1==0)&&(x0==1))
                    printf("%d %d ", 0, 0);
                if ((x1==0)&&(x0==0)){
                    printf("%d %d ", 1, 0);
                    i+=n;
                }
            }
        }
    }
    if ((x1==0)&&(x0==0)&&(x==1))
        for(i=i-n+1;i<n;i++){
            scanf("%ld", &x);
            printf("%ld ", x);
        }
    if ((x1==0)&&(x==0)&&(x0==0))
        for(i=i-n+1;i<n;i++){
            scanf("%ld", &x);
            printf("%ld ", x);
        }
    if(n==1){
        if(x==0)
            printf("%d ", 1);
        else printf("%d %d ", 0, 1);
    }
    if ((i==n-1)&&(n>1)&&(((x1==0)&&(x0==1))||((x==0)&&(x1==1))))
        printf("%d ", 1);
    if((i==n)&&(x==0)&&(x1==0))
        printf("%d ", 1);
}
