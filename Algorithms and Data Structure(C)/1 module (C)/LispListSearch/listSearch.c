#include <stdio.h> 
#include <stdlib.h> 
#include "elem.h" 
struct Elem *searchlist(struct Elem *list, int k)
{
    if (list != NULL)
    {
        if (list->tag == INTEGER && list->value.i == k) return list;
        if (list->tag == LIST && searchlist(list->value.list, k) != NULL)
            return searchlist(list->value.list, k);
        searchlist(list->tail, k);
    }
    else return NULL;
}