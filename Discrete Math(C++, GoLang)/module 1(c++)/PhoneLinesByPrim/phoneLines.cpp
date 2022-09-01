#include <iostream>
#include <vector>
#include <math.h>
#include <set>
using namespace std;

int PrimeAlgo(vector<vector<pair<int,int>>> &g){
    vector<int> min_e (g.size(), 2147483647);
    min_e[0] = 0;
    set<pair<int,int>> q;
    q.insert (make_pair (0, 0));
    int length = 0;

    for (int i = 0; i < g.size(); ++i) {
        
        int v = q.begin()->second;
        int n_cost = q.begin()->first;
        q.erase (q.begin());
        min_e[v] = 0;
        length += n_cost;
        
        for (int j = 0; j < g[v].size(); ++j) {
            int to = g[v][j].first,
                cost = g[v][j].second;
            if (to < min_e[cost]) {
                q.erase (make_pair (min_e[cost], cost));
                min_e[cost] = to;
                q.insert (make_pair (min_e[cost], cost));
            }
        }
    }
    return length;
}

int main(int argc, const char * argv[]) {
    int tops;
    cin >> tops;
    vector<vector<pair<int,int>>> g(tops);
    int ribs;
    cin >> ribs;
    for(int i = 0; i < ribs; i++){
        int u, v , len;
        cin >> u >> v >> len;
        g[u].push_back(make_pair(len, v));
        g[v].push_back(make_pair(len, u));
    }
    
    cout << PrimeAlgo(g);
    return 0;
}

