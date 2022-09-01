%option noyywrap bison-bridge bison-locations

%{

#include <stdio.h>
#include <stdlib.h>

#define TAG_IDENT       1
#define TAG_FUNCNAME    2
#define TAG_KEYWORD_IF  3
#define TAG_KEYWORD_SUB 4
#define TAG_KEYWORD_UNLESS 5
#define TAG_ERROR       6

char *tag_names[] = {
    "END_OF_PROGRAM", "IDENT", "FUNCNAME", "KEYWORD_IF", "KEYWORD_SUB", "KEYWORD_UNLESS", "ERROR"
};

struct Position {
    int line, pos, index;
};

void print_pos(struct Position *p) {
    printf("(%d, %d)", p->line, p->pos);
}

struct Fragment {
    struct Position starting, following;
};

typedef struct Fragment YYLTYPE;

void print_frag(struct Fragment *f) {
    print_pos(&(f->starting));
    printf(" - ");
    print_pos(&(f->following));
}

union Token {
    int funcValue;
    int identValue;
    char *tagKey;
};

typedef union Token YYSTYPE;

int continued;
struct Position cur;

# define YY_USER_ACTION                 \
{                                       \
    int i;                              \
    if (!continued)                     \
        yylloc->starting = cur;         \
    continued = 0;                      \
    for (i = 0; i < yyleng; i++) {      \
        if ('\n' == yytext[i]) {        \
            cur.line++;                 \
            cur.pos = 1;                \
        }                               \
        else                            \
            cur.pos++;                  \
        cur.index++;                    \
    }                                   \
    yylloc->following = cur;            \
}                                       \

void err(char *msg) {
    printf("Error ");
    print_pos(&cur);
    printf(": %s\n", msg);
}

typedef struct {
    char** dict;
    size_t used;
    size_t size;
} Dict;

void initDict(Dict* a, int initial_size) {
    a->dict = malloc(initial_size * sizeof(char*));
    a->used = 0;
    a->size = initial_size;
}

void insertDict(Dict* a, char* element) {
    if (a->used == a->size) {
        a->size *= 2;
        a->dict = realloc(a->dict, a->size * sizeof(char*));
    }
    a->dict[a->used++] = element;
}

int findInDict(Dict* a, char* element) {
    for (int i = 0; i < a->used; i++) {
        if (strcmp(a->dict[i], element) == 0) {
            return i;
        }
    }
    return -1;
}

void freeDict(Dict* a) {
    free(a->dict);
    a->dict = NULL;
    a->used = a->size = 0;
}

Dict ids;
Dict funcs;

void init_scanner(char *program) {
    continued = 0;
    cur.line = 1;
    cur.pos = 1;
    cur.index = 0;
    initDict(&ids, 2);
    yy_scan_string(program);
}

%}

FUNCNAME       [a-zA-Z]([a-zA-Z]|[0-9])*
KEYWORD_SUB    (sub)
KEYWORD_IF     (if)
KEYWORD_UNLESS (unless)
IDENT          ($|@|%)([a-zA-Z]|[0-9])*

%%

[\n\t ]+

{IDENT}     {
                insertDict(&ids, yytext);
                yylval->identValue = findInDict(&ids, yytext);
                return TAG_IDENT;
            }

{KEYWORD_SUB} {
                yylval->tagKey = tag_names[4];
                return TAG_KEYWORD_SUB;
            }

{KEYWORD_IF} {
                yylval->tagKey = tag_names[3];
                return TAG_KEYWORD_IF;
            }

{KEYWORD_UNLESS} {
                yylval->tagKey = tag_names[5];
                return TAG_KEYWORD_UNLESS;
            }

{FUNCNAME}    {
                insertDict(&funcs, yytext);
                yylval->funcValue = findInDict(&funcs, yytext);
                return TAG_FUNCNAME;
            }

<<EOF>>     return 0;

.           {
                err("Unexpected character");
                return TAG_ERROR;
            }

%%

int main()
{

    int tag;
    YYSTYPE value;
    YYLTYPE coords;
    FILE *file = fopen("test.txt", "r");
    char buf[1000];
    char c;
    int i = 0;
    while((c = fgetc(file)) != EOF && i < 1000){
        buf[i++] = c;
    }
    fclose(file);
    init_scanner(buf);

    do {
        tag = yylex(&value, &coords);
        if (0 != tag && TAG_ERROR != tag) {
		    printf("%s ", tag_names[tag]);
		    print_frag(&coords);
		    if (strcmp(tag_names[tag], "IDENT") == 0) {
                //printf("%d ", findInDict(&ids, value.ident));
                printf(": %s\n", ids.dict[value.identValue]);
            } else if (strcmp(tag_names[tag], "FUNCNAME") == 0) {
                printf(": %s\n", funcs.dict[value.funcValue]);
            } else {
                printf(": \n");
            }

        }
    } while (0 != tag);

    freeDict(&ids);
    freeDict(&funcs);
    return 0;
}