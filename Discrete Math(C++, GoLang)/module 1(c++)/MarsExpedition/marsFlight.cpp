#include <iostream>
#include <vector>
using namespace std;

struct top {
    int val;
    char mark;
};

struct pairvector{
    vector<int> first;
    vector<int> second;
};

void VisitVertex(vector<top> &graph, int v, vector<vector<top>> &matrix, vector<pairvector> &pairs, int place){
    graph[v].mark = 'g';
    
    if(place == 1)
        pairs[pairs.size() - 1].first.push_back(v);
    else
        pairs[pairs.size() - 1].second.push_back(v);
        
    for(int i = 0; i < graph.size(); i++)
        if(graph[i].mark == 'w' && matrix[i][v].val == 1){
            if(place == 1)
                VisitVertex(graph, i, matrix, pairs, 0);
            else
                VisitVertex(graph, i, matrix, pairs, 1);
        }
}

int check(vector<vector<top>> &matrix, vector<pairvector> &pairs){
    for(int i:pairs[pairs.size() - 1].first)
        for(int j:pairs[pairs.size() - 1].first)
            if(matrix[i][j].val == 1){
                cout << "No solution";
                return 0;
            }
         
    for(int i:pairs[pairs.size() - 1].second)
        for(int j:pairs[pairs.size() - 1].second)
            if(matrix[i][j].val == 1){
                cout << "No solution";
                return 0;
            }
    return 1;
}

int DFS(vector<top> &graph, vector<vector<top>> &matrix, vector<pairvector> &pairs) {
    for(int v = 0; v < graph.size(); v++)
        if(graph[v].mark == 'w'){
            pairs.push_back({vector<int>(),vector<int>()});
            VisitVertex(graph, v, matrix, pairs, 0);
            if(check(matrix, pairs) == 0)
                return 0;
        }
    return 1;
}

void vecSwap(vector<int> &a, vector<int> &b){
    vector<int> swap = a;
    a = b;
    b = swap;
}

int comparator(pairvector a, pairvector b){
    if(a.first.size() > b.first.size())
        return false;
    else if(a.first.size() < b.first.size())
        return true;
    else{
        for(int i = 0; i < a.first.size(); i++){
            if(b.first[i] > a.first[i])
                return false;
            if(b.first[i] < a.first[i])
                return true;
        }
    }
    return false;
}

pairvector sortByMask(vector<pairvector> &pairs){
    int edge = 1 << pairs.size();
    pairvector ans;
    for(int i = 0; i < edge; i++){
        pairvector maybeans;
        int ii = i;
        for(int j = 0; j < pairs.size(); j++){
            if(ii & 1){
                for(int t:pairs[j].first)
                    maybeans.first.push_back(t);
                for(int t:pairs[j].second)
                maybeans.second.push_back(t);
            }
            else{
                for(int t:pairs[j].second)
                    maybeans.first.push_back(t);
                for(int t:pairs[j].first)
                maybeans.second.push_back(t);
            }
            ii >>= 1;
        }
        if(maybeans.first.size() > maybeans.second.size())
            swap(maybeans.first, maybeans.second);
        if(i == 0)
            ans = maybeans;
        else if( comparator(ans, maybeans) == 1)
            ans = maybeans;
        }
    return ans;
}

int main(int argc, const char * argv[]) {
    long n;
    cin >> n;
    vector<top> man;

    for(int i = 0; i < n; i++)
        man.push_back({i,'w'});
              
    vector<vector<top>> friends(n, vector<top> (n));
    vector<pairvector> pairs;
    for(int i = 0; i < n; i++)
        for(int t = 0; t < n; t++){
            char sign;
            cin >> sign;
            if(sign == '+'){
                friends[i][t].val = 1;
                friends[t][i].val = 1;
            }
            else {
                friends[i][t].val = 0;
                friends[t][i].val = 0;
            }
            friends[i][t].mark = 'w';
            friends[t][i].mark = 'w';
        }
 
    if(DFS(man, friends, pairs) == 0)
        return 0;
    pairvector ans = sortByMask(pairs);
    for(int i = 0; i < ans.first.size() - 1; i++)
        for(int j = i + 1; j < ans.first.size(); j++)
            if(ans.first[i] > ans.first[j]){
                int swap = ans.first[i];
                ans.first[i] = ans.first[j];
                ans.first[j] = swap;
            }
        
    for(int i:ans.first)
        cout << i + 1 << " ";
    return 0;
}
