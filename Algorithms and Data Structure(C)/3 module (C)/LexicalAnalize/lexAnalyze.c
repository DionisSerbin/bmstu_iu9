#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
struct mass{
    struct mass* left;
    struct mass* right;
    struct mass* parent;
    int v;
    char* k;
    int balance;
}*t = NULL;
struct mass *t;
struct mass* insert(struct mass* t, struct mass* y){
    if(!t){
        return y;
    }
    else{
        struct mass* x = t;
        while (x){
            if (strcmp(y->k, x->k) > 0)
                if (x->right)
                    x = x->right;
            else{
                y->parent = x;
                x->right = y;
                return t;
            }
            else if (strcmp(y->k, x->k) < 0)
                if (x->left)
                    x = x->left;
            else{
                y->parent = x;
                x->left = y;
                return t;
            }
            else return  NULL;//
        }
    }
    return 0;
}
struct mass* replacenode(struct mass* t, struct mass* x, struct mass* y){
    if(x == t){
        t = y;
        if(y)
            y->parent = NULL;
    }
    else{
        struct mass* p = x->parent;
        if (y)
            y->parent = p;
        if (p->left == x)
            p->left = y;
        else
            p->right = y;
    }
    return t;
}
struct mass* rotateleft(struct mass* t, struct mass* x){
    struct mass* y = x->right;
    if(!y)//
        return NULL;
    t = replacenode(t, x, y);
    struct mass* b = y->left;
    if(b)
        b->parent = x;
    x->right = b;
    x->parent = y;
    y->left = x;
    x->balance--;
    if(y->balance > 0)
        x->balance -= y->balance;
    y->balance--;
    if(x->balance < 0)
        y->balance += x->balance;
    return t;
}
struct mass* rotateright(struct mass* t, struct mass* x){
    struct mass* y = x->left;
    if(!y)//
        return NULL;
    t = replacenode(t, x, y);
    struct mass* b = y->right;
    if(b)
        b->parent = x;
    x->left = b;
    x->parent = y;
    y->right = x;
    y->balance++;
    if(x->balance < 0)
        y->balance -= x->balance;
    x->balance++;
    if(y->balance > 0)
        x->balance += y->balance;
    return t;
}
struct mass* insertAVL(struct mass* t, struct mass *a,char *k, int v){
    a->v = v;
    a->k = k;
    a->parent = NULL;
    a->left = NULL;
    a->right = NULL;
    t = insert(t, a);
    a->balance = 0;
    for( ; ; ){
        struct mass* x = a->parent;
        if (!x)
            return t;
        if (a == x->left){
            x->balance--;
            if (x->balance == 0)
                return t;
            if (x->balance == -2){
                if(a->balance == 1)
                    return rotateright(rotateleft(t, a), x);
                else
                    return rotateright(t, x);
            }
        }
        else{
            x->balance++;
            if (x->balance == 0)
                return t;
            if (x->balance == 2){
                if(a->balance == -1)
                    return rotateleft(rotateright(t, a), x);
                else
                    return rotateleft(t, x);
            }
        }
        a = x;
    }
}
struct mass* descend(struct mass* t, char* k){
    struct mass* x = t;
    while(x && strcmp(k, x->k)){
        if(strcmp(k, x->k) < 0)
            x = x->left;
        else
            x = x->right;
    }
    return x;
}
struct mass* lookup(struct mass* t, char* k){
    struct mass *x = descend(t, k);
    return x;
}

int main(int argc, char** args){
    int n;
    scanf("%d", &n);
    char* str = malloc((n + 1) * sizeof(char));
    scanf("%c", str);
    for (int i = 0; i < n; ++i)
        scanf("%c", str + i);
    str[n] = 0;
    int i,count = 0;
    for(i = 0; i < n; i++){
        if(str[i] == '+')
            printf("SPEC %d\n", 0);
        else if(str[i] == '-')
            printf("SPEC %d\n", 1);
        else if(str[i] == '*')
            printf("SPEC %d\n", 2);
        else if(str[i] == '/')
            printf("SPEC %d\n", 3);
        else if(str[i] == '(')
            printf("SPEC %d\n", 4);
        else if(str[i] == ')')
            printf("SPEC %d\n", 5);
        else if(str[i] >= '0' && str[i] <= '9'){
            printf("CONST %c", str[i]);
            while(str[i + 1] >= '0' && str[i + 1] <= '9'){
                i++;
                printf("%c", str[i]);
            }
            printf("\n");
        }
        else if((str[i] >= 'a' && str[i] <= 'z')
                || (str[i] >= 'A' && str[i] <= 'Z')){
            int j = i;
            while((str[i + 1] >= 'a' && str[i + 1] <= 'z')
                || (str[i + 1] >= 'A' && str[i + 1] <= 'Z')
                    || (str[i + 1] >= '0' && str[i + 1] <= '9'))
                i++;
            char* newstr = malloc((i - j + 2) * sizeof(char));
            for (int t = 0; t <= i - j; ++t)
                newstr[t] = str[j + t];
            newstr[i - j + 1] = 0;
            struct mass *subtree = lookup(t, newstr);
            if(!subtree){
                subtree = malloc(sizeof(struct mass));
                t = insertAVL(t, subtree, newstr, count);
                count++;
            }
            else
                free(newstr);
            printf("IDENT %d\n", subtree->v);
        }
    }
    free(str);
    return 0;
}
