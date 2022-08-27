#include <stdio.h>
#include <stdlib.h>
#include <math.h>
struct stack {
    int top;
    int data[100000];
};
void init(struct stack *s) {
  s->top = 0;
}
void cons(struct stack *s, int x)
{
    s->data[s->top]=x;
    s->top++;
}
int pop(struct stack *s)
{
    int el;
    s->top--;
    el = s->data[s->top];
    return el;
}
void add(struct stack *s){
    int a = pop(s);
    int b = pop(s);
    cons(s, a + b);
}
void sub(struct stack *s){
    int a = pop(s);
    int b = pop(s);
    cons(s, a - b);
}
void mul(struct stack *s){
    int a = pop(s);
    int b = pop(s);
    cons(s, a * b);
}
void divv(struct stack *s){
    int a = pop(s);
    int b = pop(s);
    cons(s, a / b);
}
int MAX(int  a, int b){
    if(a>b)
        return a;
    else
        return b;
}
void max(struct stack *s){
    int a = pop(s);
    int b = pop(s);
    cons(s, MAX(a, b));
}
int MIN(int  a, int b){
    if(a<b)
        return a;
    else
        return b;
}
void min(struct stack *s){
    int a = pop(s);
    int b = pop(s);
    cons(s, MIN(a, b));
}
void neg(struct stack *s){
    s->data[s->top - 1] = - s->data[s->top - 1];
}
void duplic(struct stack *s){
    s->data[s->top] = s->data[s->top - 1];
    s->top++;
}
void swap(struct stack *s){
    int sswap = s->data[s->top - 1];
    s->data[s->top - 1] = s->data[s->top - 2];
    s->data[s->top - 2] = sswap;
}
int main(int argc, const char * argv[]) {
    struct stack *s;
    int n,x;
    scanf("%d", &n);
    s = (struct stack*)malloc(sizeof(struct stack));
    init(s);
    char word[n][10];
    for(int i=0;i<n;i++)
    {
        scanf("%s", word[i]);
        if(word[i][0] == 'C')
        {
            scanf("%d", &x);
            cons(s,x);
        }
        else if(word[i][0] == 'A')
            add(s);
        else if (word[i][0] == 'S' && word[i][1] == 'U')
            sub(s);
        else if(word[i][0] == 'M' && word[i][1] == 'U')
            mul(s);
        else if(word[i][0] == 'D' && word[i][1] == 'I')
            divv(s);
        else if(word[i][0] == 'M' && word[i][1] == 'A')
            max(s);
        else if(word[i][0] == 'M' && word[i][1] == 'I')
            min(s);
        else if(word[i][0] == 'N')
            neg(s);
        else if(word[i][0] == 'D' && word[i][1] == 'U')
            duplic(s);
        else if(word[i][0] == 'S' && word[i][1] == 'W')
            swap(s);
    }
    printf("%d", s->data[s->top - 1]);
    free(s);
    return 0;
}
