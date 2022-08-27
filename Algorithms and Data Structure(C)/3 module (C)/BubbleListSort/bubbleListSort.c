#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <string.h>
struct Elem {
    struct Elem* next;
    char* word;
};

struct Elem *bubblesort(struct Elem *l){
    struct Elem *end = NULL;
    struct Elem *nextt;
    while(l->next != end){
        if(strlen(l->word) > strlen(l->next->word)){
            struct Elem* el = l;
            l = el->next;
            el->next = l->next;
            l->next = el;
        }
        nextt = l;
        while(nextt->next->next != end){
            if(strlen(nextt->next->word) > strlen(nextt->next->next->word)){
                struct Elem* el = nextt->next;
                nextt->next = el->next;
                el->next = nextt->next->next;
                nextt->next->next = el;
            }
            nextt = nextt->next;
        }
        end = nextt->next;
    }
    return l;
}
int main()
{
    char str[10000];
    gets(str);
    struct Elem* l = NULL;
    int i = strlen(str) - 1;
    while(i >= 0){
        if (str[i] != ' '){
            struct Elem *new = malloc(sizeof(struct Elem));
            new->word = malloc((strlen(str) + 1) * sizeof(char));
            int j = 0;
            int startlen;
            for(startlen = 1; i - startlen >= 0 &&
                str[i - startlen] != ' '; startlen++);
            while(j < startlen){
                new->word[j] = str[i + j - 1 - startlen + 2];
                j++;
            }
            i -= startlen;
            new->word[startlen] = 0;
            new->next = l;
            l = new;
        }
        i--;
    }
    l = bubblesort(l);
    while (l){
        printf("%s ", l->word);
        struct Elem *next= l->next;
        free(l->word);
        free(l);
        l = next;
    }
    return 0;
}
