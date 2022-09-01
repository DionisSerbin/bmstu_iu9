#include <iostream>
#include <vector>

using namespace std;

vector<int> heap;
vector<int> val;
vector<int> pos;
vector<int> g;
vector<int> treeValue;
vector<bool> used;

int size() {
    return heap.size();
}

int getHeap(int i){
    return heap[i];
}

void makeHeap(int i, int val){
    heap[i] = val;
}

int getPos(int i){
    return pos[i];
}

void makePos(int i, int val){
    pos[i] = val;
}

int getVal(int i){
    return val[i];
}

void makeVal(int i, int value){
    val[i] = value;
}

int compare(int a, int b) {
    return getVal(getHeap(a)) - getVal(getHeap(b));
}

void swap(int a, int b) {
    int dop = getHeap(a);
    makeHeap(a, getHeap(b));
    makeHeap(b, dop);
    dop = getPos(getHeap(a));
    makePos(getHeap(a), getPos(getHeap(b)));
    makePos(getHeap(b), dop);
}

void makeBalanceUp(int v) {
    while (v >= 1 && compare(v, (v - 1) / 2) < 0) {
        swap(v, (v - 1) / 2);
        v = (v - 1) / 2;
    }
}

void makeSet(int v, int x) {
    if (getVal(v) == -1) {
        makeVal(v, x);
        makePos(v, heap.size());
        heap.push_back(v);
        makeBalanceUp(getPos(v));
    }
    else if (getVal(v) > x) {
        makeVal(v, x);
        makeBalanceUp(getPos(v));
    }
}

void makeBalanceDown(int v) {
    while (2 * v + 2 <= size()) {
        if (2 * v + 2 == size()) {
            if (compare(2 * v + 1, v) < 0) {
                swap(2 * v + 1, v);
                v = 2 * v + 1;
            }
            else return;
        }
        else if (compare(2 * v + 1, 2 * v + 2) < 0) {
            if (compare(2 * v + 1, v) < 0) {
                swap(2 * v + 1, v);
                v = 2 * v + 1;
            }
            else return;
        }
        else if (compare(2 * v + 2, v) < 0) {
            swap(2 * v + 2, v);
            v = 2 * v + 2;
        }
        else return;
    }
}

class Pair {
public:
    int car;
    int cdr;
    Pair(int first, int second){
        car = first;
        cdr = second;
    }
};

Pair getMin() {
    Pair ans(getHeap(0), getVal(getHeap(0)));
    makeVal(getHeap(0), -1);
    makePos(getHeap(0), -1);
    swap(0, heap.size() - 1);
    heap.resize(heap.size() - 1);
    makeBalanceDown(0);
    return ans;
}

int main() {
    int n;
    cin >> n;
    
    g.resize(n * n);
    treeValue.resize(n * n);
    treeValue.assign(n * n, 2147483647);
    used.resize(n * n);
    used.assign(n * n, false);
    
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            cin >> g[i * n + j];
    
    val = vector<int>(n * n, -1);
    pos = vector<int>(n * n, -1);
    makeSet(0, g[0]);
    
    while (size() && !used[n * n - 1]) {
        Pair minimalHeap = getMin();
        treeValue[minimalHeap.car] = minimalHeap.cdr;
        used[minimalHeap.car] = true;
        if (minimalHeap.car % n != 0 && !used[minimalHeap.car - 1])
            makeSet(minimalHeap.car - 1, treeValue[minimalHeap.car] + g[minimalHeap.car - 1]);
        if (minimalHeap.car % n != n - 1 && !used[minimalHeap.car + 1])
            makeSet(minimalHeap.car + 1, treeValue[minimalHeap.car] + g[minimalHeap.car + 1]);
        if (minimalHeap.car / n != 0 && !used[minimalHeap.car - n])
            makeSet(minimalHeap.car - n, treeValue[minimalHeap.car] + g[minimalHeap.car - n]);
        if (minimalHeap.car / n != n - 1 && !used[minimalHeap.car + n])
            makeSet(minimalHeap.car + n, treeValue[minimalHeap.car] + g[minimalHeap.car + n]);
    }
    cout << treeValue[n * n - 1];
}
