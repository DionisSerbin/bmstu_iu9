#include <iostream>
#include <vector>
#include <math.h>
#include <iomanip>
using namespace std;

int dist(int x0, int x1, int y0, int y1){
    return (x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1);
}

void Merge_sort(vector<pair<int, pair<int, int>>> &g, int min, int max){
    vector<int> c(max - min + 1);
    for (int i = 0; i < g.size(); i++)
        c[g[i].first - min]++;

    for (int i = 1; i < max - min + 1; i++)
        c[i] += c[i - 1];

    vector<pair<int, pair<int, int>>> dopvec(g.size());
    for (long i = g.size() - 1; i >= 0; i--)
        dopvec[--c[g[i].first - min]] = g[i];
    g = dopvec;
}

long double krusakal(vector<pair<int,int>> &atract, vector<pair<int,pair<int,int>>> &g){
    long double cost = 0;
    vector < pair<int,int> > res;
    int max = -2147483647 - 1;
    int min = 2147483647;
    for (int i = 0, k = 0; i < atract.size(); i++){
        for (int j = i + 1; j < atract.size(); j++){
            g[k].first = dist(atract[i].first, atract[j].first, atract[i].second, atract[j].second);
            if (g[k].first > max)
                max = g[k].first;
            if (g[k].first < min)
                min = g[k].first;
            g[k].second.first = i;
            g[k++].second.second = j;
        }
    }
    
    Merge_sort(g, min, max);
    
    vector<int> tree_id (atract.size());
    for (int i = 0; i < atract.size(); ++i)
        tree_id[i] = i;
    
    for (int i = 0; i < g.size(); ++i){
        int a = g[i].second.first,  b = g[i].second.second;
        long double l = sqrtl(g[i].first);
        if (tree_id[a] != tree_id[b]){
            cost += l;
            res.push_back (make_pair (a, b));
            int old_id = tree_id[b],  new_id = tree_id[a];
            for (int j = 0; j < atract.size(); ++j)
                if (tree_id[j] == old_id)
                    tree_id[j] = new_id;
        }
    }
    return cost;
}

int main(int argc, const char * argv[]) {
    int tops;
    cin >> tops;
    vector<pair<int,int>> atract(tops);
    for(int i = 0; i < tops; i++)
        cin >> atract[i].first >> atract[i].second;
    int m = tops * (tops - 1) / 2;
    vector<pair<int,pair<int,int>>> g (m);
    cout << fixed << setprecision(2) << krusakal(atract, g);
    return 0;
}
