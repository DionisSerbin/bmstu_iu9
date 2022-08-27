#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>

struct LIST
{
	int key;
	int val;
	struct LIST* next;
};

struct HASH
{
	struct LIST** arr;
} *hash;

int main()
{
	int n, m;
	scanf("%d%d", &n, &m);

	hash = malloc(sizeof(struct HASH));
	hash->arr = malloc(m * sizeof(struct LIST*));
	for (int i = 0; i < m; i++)
		hash->arr[i] = NULL;

	char s[7];
	for (int i = 0; i < n; i++)
	{
		scanf("%s", s);
		if (s[1] == 'S')
		{
			int key, val;
			scanf("%d%d", &key, &val);

			if (hash->arr[key % m] == NULL)
			{
				hash->arr[key % m] = malloc(sizeof(struct LIST));
				hash->arr[key % m]->key = key;
				hash->arr[key % m]->val = val;
				hash->arr[key % m]->next = NULL;
				continue;
			}

			if (hash->arr[key % m]->key == key)
			{
				hash->arr[key % m]->val = val;
				continue;
			}

			if (hash->arr[key % m]->key > key)
			{
				struct LIST* new = malloc(sizeof(struct LIST));
				new->key = key;
				new->val = val;
				new->next = hash->arr[key % m];
				hash->arr[key % m] = new;
				continue;
			}

			struct LIST* now = hash->arr[key % m];

			while (now->next != NULL && now->next->key < key)
				now = now->next;

			if (now->next != NULL && now->next->key == key)
				now->next->val = val;
			else
			{
				struct LIST* new = malloc(sizeof(struct LIST));
				new->key = key;
				new->val = val;
				new->next = now->next;
				now->next = new;
			}
		}
		else
		{
			int key;
			scanf("%d", &key);

			struct LIST* now = hash->arr[key % m];

			while (now != NULL && now->key < key)
				now = now->next;

			if (now == NULL || now->key != key)
				printf("0\n");
			else
				printf("%d\n", now->val);
		}
	}

	return 0;
}