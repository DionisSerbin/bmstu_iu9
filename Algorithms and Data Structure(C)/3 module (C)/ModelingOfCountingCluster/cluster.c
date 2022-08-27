#include <stdio.h>
#include <stdlib.h>
#include <math.h>
struct priorqueue{
     int *heap;
     int cap,count;
}*q;
struct priorqueue *q;
void insertqueue(struct priorqueue *q, int x){
    int i = q->count;
    q->count = i + 1;
    q->heap[q->count - 1] = x;
    while(i > 0 && q->heap[i] < q->heap[(i - 1) / 2]){
        int swap = q->heap[(i - 1) / 2];
        q->heap[(i - 1) / 2] = q->heap[i];
        q->heap[i] = swap;
        i = i - 1;
        i /= 2;
    }
}
void heapify(int i, int n, struct priorqueue *q){
    for( ; ; ){
        int l = 2 * i + 1;
        int r = l + 1;
        int j = i;
        if ((l < n) && (q->heap[i] > q->heap[l]))
            i = l;
        if ((r < n) && (q->heap[i] > q->heap[r]))
            i = r;
        if (i == j)
            break;
        int swap = q->heap[i];
        q->heap[i] = q->heap[j];
        q->heap[j] = swap;
    }
}
int extractmin(struct priorqueue *q){
    int x = q->heap[0];
    q->count--;
    if(q->count > 0){
        q->heap[0] = q->heap[q->count];
        heapify(0, q->count, q);
    }
    return x;
}
int main(int argc, const char * argv[]) {
    int cl,n;
    scanf("%d%d", &cl, &n);
    q = malloc(sizeof(struct priorqueue));
    q->heap = malloc(cl * sizeof(int));
    q->count = 0;
    q->cap = cl;
    int i;
    for(i = 0; i < q->cap; i++){
        int start, time;
        scanf("%d%d", &start, &time);
        insertqueue(q, start + time);
    }
    for(i = q->cap; i < n; i++){
        int start, time;
        scanf("%d%d", &start, &time);
        int nextstart = extractmin(q);
        if(start > nextstart){
            insertqueue(q, start + time);
        }
        else{
            insertqueue(q, nextstart + time);
        }
    }
    int timez;
    while(q->count > 0){
        timez = extractmin(q);
    }
    printf("%d", timez);
    free(q->heap);
    free(q);
    return 0;
}
