#include <stdio.h>

long CheckStep2 (long x)
{
    long count = 0;
    if ((x && ((x & (x - 1)) == 0)) == 1)
    count++;
    return count;
}

long Sum(long *arr,long nel){
	long answer = 0;
	long range = (1 << nel) - 1;
	for (long i = 0; i <= range; i++){
		long x = 0, y = i, sum = 0;
		while(y > 0){
			if ((y & 1) == 1)
				sum += arr[x];
			x++;
			y = y >> 1;
		}
		if (CheckStep2(sum) == 1)
			answer++;
	}
	return answer;
}

int main(int argc, const char * argv[]) {
	long n, power2 = 0;
	scanf("%ld", &n);
	long arr[n];
	for (int i = 0 ; i < n; i++){
		scanf("%ld", &arr[i]);
	}
	power2 = Sum(arr, n);
	printf("%ld\n", power2);
	return 0;
}