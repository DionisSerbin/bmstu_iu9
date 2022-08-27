#include <stdio.h>
#include <math.h>
#include <stdlib.h>
int len = 4;

struct queue {
  int *data;
  int cap,count,head,tail;
} *q;
struct queue *q;
int empt(){
    if(q->count == 0){
        return 1;
    }
    else
        return 0;
}
void enq(int x,int len){
    q->data[q->tail] = x;
    q->tail++;
    q->count++;
    if(q->tail == q->cap ){
        int *newdata = (int *)malloc(len*2*sizeof(int));
        int itr = q->head;
        for(int i =0;i<q->cap ;i++){
            newdata[i]=q->data[itr];
            itr = (itr + 1) % q->cap;
        }
        free(q->data);
        q->data = newdata;
        q->head = 0;
        q->tail = q->count;
        len *= 2;
        q->cap *=2;
    }
}
int deq(){
    int el = q->data[q->head];
    q->head++;
    if(q->head == q->cap)
        q->head == 0;
    q->count--;
    return el;
}
int main(int argc, const char * argv[]) {
    int n,x;
    scanf("%d", &n);
    q = (struct queue *)malloc(sizeof(struct queue));
    q->data = (int *)malloc(len*sizeof(int));
    q->count = 0;
    q->cap = len;
    q->head = 0;
    q->tail = 0;
    char word[n][10];
    for(int i=0;i<n;i++)
    {
        scanf("%s", word[i]);
        if(word[i][0] == 'E' && word[i][1] == 'N'){
            scanf("%d", &x);
            enq(x,q->cap);
        }
        else if(word[i][0] == 'D')
            printf("%d\n", deq());
        else if (word[i][0] == 'E' && word[i][1] == 'M'){
            if(empt())
                printf("true\n");
            else
                printf("false\n");
        }
    }
    free(q->data);
    free(q);
    return 0;
}
