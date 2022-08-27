#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <string.h>

#define true 1
#define false 0
#define bool unsigned char

struct Node
{
	unsigned short numArgs;
	bool ifWord;
	struct Node* parent;
	struct Node** args;
} *trie;

struct Node* MakeNode(struct Node** x)
{
	(*x) = malloc(sizeof(struct Node));
	(*x)->numArgs = 0;
	(*x)->ifWord = false;
	(*x)->parent = NULL;
	(*x)->args = malloc(('z' - 'a' + 1) * sizeof(struct Node));
	for (int i = 'a'; i <= 'z'; ++i)
		(*x)->args[i - 'a'] = NULL;
	return *x;
}

struct Node* Find(char* s)
{
	struct Node* x = trie;
	while (*s != 0 && x != NULL)
		x = x->args[*(s++) - 'a'];
	return x;
}

void Insert(char* s)
{
	struct Node* f = Find(s);
	if (f != NULL && f->ifWord)
		return;

	struct Node* x = trie;
	while (*s != 0)
	{
		++x->numArgs;
		if (x->args[*s - 'a'] == NULL)
			MakeNode(&x->args[*s - 'a'])->parent = x;
		x = x->args[*s - 'a'];
		++s;
	}
	x->ifWord = true;
}

void Delete(char* s)
{
	struct Node* x = Find(s);

	while (*s != 0) ++s;
	--s;

	x->ifWord = false;

	while (x != NULL)
	{
		struct Node* y = x->parent;
		if (x->numArgs == 0 && !x->ifWord)
		{
			free(x->args);
			free(x);
			if (y != NULL)
				y->args[*s - 'a'] = NULL;
		}
		--s;
		if (y != NULL)
			--y->numArgs;

		x = y;
	}
}

int Prefix(char* s)
{
	struct Node* x = Find(s);
	return (x == NULL ? 0 : x->numArgs + x->ifWord);
}

int main(int num, char** args)
{
	int n;
	scanf("%d", &n);

	MakeNode(&trie);
	trie->ifWord = true;

	char s1[7], s2[100000];
	for (int i = 0; i < n; ++i)
	{
		scanf("%s%s", s1, s2);

		if (s1[0] == 'I')
			Insert(s2);
		else if (s1[0] == 'D')
			Delete(s2);
		else if (s1[0] == 'P')
			printf("%d\n", Prefix(s2));
		else
		{
			printf("ERROR!");
			return -1;
		}
	}

	return 0;
}