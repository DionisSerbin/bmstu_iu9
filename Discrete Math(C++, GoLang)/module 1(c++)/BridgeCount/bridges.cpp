#include <vector>
#include <iostream>
using namespace std;

int min(int a, int b){
    if(a > b)
        return b;
    else return a;
}

void DFS(int v, vector<bool> &used,vector<int> &tin,vector<int> &fup, vector<vector<int>> &matrix, int &timer, int &ans, int p = -1) {
    used[v] = true;
    tin[v] = timer++;
    fup[v] = timer;
    for (int i = 0; i < matrix[v].size(); ++i) {
        int to = matrix[v][i];
        if (to == p)  continue;
        if (used[to])
            fup[v] = min (fup[v], tin[to]);
        else {
            DFS(to, used, tin, fup, matrix, timer, ans, v);
            fup[v] = min (fup[v], fup[to]);
            if (fup[to] > tin[v])
                ans++;
        }
    }
}
 
void find_bridges(int &timer,vector<bool> used,vector<int> tin,vector<int> fup,vector<vector<int>> &matrix,int &ans) {
    for (int i = 0; i < matrix.size(); ++i)
        if (!used[i])
            DFS(i, used, tin, fup, matrix,timer,ans);
}

int main(int argc, const char * argv[]) {
    long tops,ribs;
    cin >> tops;
    cin >> ribs;
    vector<vector<int>> matrix(tops);
   
    for(int i = 0; i < ribs; i++){
        int j,t;
        cin >> j >> t;
        matrix[j].push_back(t);
        if (j != t)
            matrix[t].push_back(j);
    }
    vector<bool> used(tops);
    int timer;
    timer = 0;
    for (int i = 0; i < tops; ++i)
        used[i] = false;
    vector<int> tin(tops);
    vector<int> fup(tops);
    int ans = 0;
    find_bridges(timer, used, tin, fup, matrix,ans);
    cout << ans;
    return 0;
}
