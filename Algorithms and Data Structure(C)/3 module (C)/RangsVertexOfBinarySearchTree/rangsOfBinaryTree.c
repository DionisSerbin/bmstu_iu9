#include <stdio.h>
#include <stdlib.h>
#include <math.h>
struct mass{
    struct mass* left;
    struct mass* right;
    struct mass* parent;
    char* v;
    int k;
    int space;
}*t = NULL;
struct mass *t;
struct mass* minimum(struct mass* t){
    if (!t)
        return NULL;
    else{
        struct mass* x = t;
        while(x->left)
            x = x->left;
        return x;
    }
}
struct mass* succ(struct mass* x){
    struct mass* y;
    if(x->right)
        return y = minimum(x->right);
    else{
        y = x->parent;
        while(y && x == y->right){
            x = y;
            y = y->parent;
        }
        return y;
    }
}
struct mass* descend(struct mass* t, int k){
    struct mass* x = t;
    while(x && x->k != k){
        if(k < x->k)
            x = x->left;
        else
            x = x->right;
    }
    return x;
}
struct mass* lookup(struct mass* t, int k){
    struct mass *x = descend(t, k);
    return x;
}
struct mass* insert(struct mass* t, int k, char *v){
    struct mass* y = malloc(sizeof(struct mass));
    y->v = v;
    y->k = k;
    y->parent = NULL;
    y->left = NULL;
    y->right = NULL;
    if(!t){
        y->space = 0;
        return y;
    }
    else{
        struct mass* x = t;
        while (x){
            if (y->k > x->k)
                if (x->right)
                    x = x->right;
            else{
                y->parent = x;
                x->right = y;
                while(y){
                    int fs,ss;
                    if(!y->left)
                        fs = -1;
                    else
                        fs = y->left->space;
                    if(!y->right)
                        ss = 0;
                    else
                        ss = y->right->space + 1;
                    y->space = fs + ss + 1;
                    y = y->parent;
                }
                free(y);
                return t;
            }
            else if (y->k < x->k)
                if (x->left)
                    x = x->left;
            else{
                y->parent = x;
                x->left = y;
                while(y){
                    int fs,ss;
                    if(!y->left)
                        fs = -1;
                    else
                        fs = y->left->space;
                    if(!y->right)
                        ss = 0;
                    else
                        ss = y->right->space + 1;
                    y->space = fs + ss + 1;
                    y = y->parent;
                }
                free(y);
                return t;
            }
        }
    }
    free(y);
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
    while(x){
        int fs,ss;
        if(!x->left)
            fs = -1;
        else
            fs = x->left->space;
        if(!x->right)
            ss = 0;
        else
            ss = x->right->space + 1;
        x->space = fs + ss + 1;
        x = x->parent;
    }
    while(y){
        int fs,ss;
        if(!y->left)
            fs = -1;
        else
            fs = y->left->space;
        if(!y->right)
            ss = 0;
        else
            ss = y->right->space + 1;
        y->space = fs + ss + 1;
        y = y->parent;
    }
    return t;
}
struct mass* delete(struct mass* t, int k){
    struct mass* x = descend(t, k);
    if(!x)
        return NULL;
    if (!x->left && !x->right)
        t = replacenode(t, x, NULL);
    else if (!x->left)
        t = replacenode(t, x, x->right);
    else if (!x->right)
        t = replacenode(t, x, x->left);
    else{
        struct mass* y = succ(x);
        t = replacenode(t, y, y->right);
        x->left->parent = y;
        y->left = x->left;
        if(x->right)
            x->right->parent = y;
        y->right = x->right;
        t = replacenode(t, x, y);
    }
    free(x->v);
    free(x);
    return t;
}
struct mass* searchbyrank(struct mass* t, int numb){
    while(1){
        if(t->left && numb <= t->left->space){
            t = t->left;
        }
        else{
            int fs;
            if(!t->left)
                fs = 0;
            else
                fs = t->left->space + 1;
            numb -= (fs + 1);
            if(numb < 0)
                return t;
            t = t->right;
        }
    }
}
int main(int argc, const char * argv[]) {
    int n;
    scanf("%d", &n);
    char word[n][10];
    for(int i = 0; i < n; i++){
        scanf("%s", word[i]);
        if(word[i][0] == 'I'){
            int k;
            char *v = malloc(10 * sizeof(char));
            scanf("%d%s", &k, v);
            t = insert(t, k, v);
        }
        else if(word[i][0] == 'D'){
            int k;
            scanf("%d", &k);
            t = delete(t, k);
        }
        else if(word[i][0] == 'L'){
            int k;
            scanf("%d", &k);
            char *v = lookup(t, k)->v;
            printf("%s\n", v);
        }
        else if(word[i][0] == 'S'){
            int numb;
            scanf("%d", &numb);
            char *v = searchbyrank(t, numb)->v;
            printf("%s\n", v);
        }
    }
    return 0;
}
