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

void addOrChange(int _key, int _val, int m)
{
	int hkey = abs(_key) % m;
	if (hash->arr[hkey] == NULL)
	{
		hash->arr[hkey] = malloc(sizeof(struct LIST));
		hash->arr[hkey]->key = _key;
		hash->arr[hkey]->val = _val;
		hash->arr[hkey]->next = NULL;
		return;
	}

	if (hash->arr[hkey]->key == _key)
	{
		hash->arr[hkey]->val = _val;
		return;
	}

	if (hash->arr[hkey]->key > _key)
	{
		struct LIST* new = malloc(sizeof(struct LIST));
		new->key = _key;
		new->val = _val;
		new->next = hash->arr[hkey];
		hash->arr[hkey] = new;
		return;
	}

	struct LIST* now = hash->arr[hkey];

	while (now->next != NULL && now->next->key < _key)
		now = now->next;

	if (now->next != NULL && now->next->key == _key)
		now->next->val = _val;
	else
	{
		struct LIST* new = malloc(sizeof(struct LIST));
		new->key = _key;
		new->val = _val;
		new->next = now->next;
		now->next = new;
	}
}

int ifGet;
int ansGet;

void getOrNormal(int _key, int _normal, int m)
{
	int hkey = abs(_key) % m;

	struct LIST* now = hash->arr[hkey];

	while (now != NULL && now->key < _key)
		now = now->next;

	if (now == NULL || now->key != _key)
	{
		ifGet = 0;
		ansGet = _normal;
	}
	else
	{
		ifGet = 1;
		ansGet = now->val;
	}
}

int main()
{
	int n, m = 1e5 + 3;
	scanf("%d", &n);

	hash = malloc(sizeof(struct HASH));
	hash->arr = malloc(m * sizeof(struct LIST*));
	for (int i = 0; i < m; i++)
		hash->arr[i] = NULL;

	int* arr = malloc((n + 1) * sizeof(int));
	for (int i = 0; i <= n; i++)
		arr[i] = 0;
	int xor = 0;
	addOrChange(0, 0, m);

	for (int i = 1; i <= n; i++)
	{
		int k;
		scanf("%d", &k);

		xor ^= k;

		getOrNormal(xor, -1, m);

		if (ifGet)
			arr[i] = ansGet + 1;

		addOrChange(xor, arr[i], m);
	}

	int ans = 0;
	for (int i = 0; i <= n; i++)
		ans += arr[i];
	printf("%d", ans);

	free(arr);

	return 0;
}