#include <vector>
#include <iostream>
#include <string>
using namespace std;
struct top {
    int val;
    char mark;
    int place;
    int there;
};

struct big {
    int top;
    int rib;
};

void graphPrint( vector<top> &graph, vector<vector<top>> &matrix) {
    cout << "graph {\n";
    for(int i = 0; i < graph.size(); i++) {
        cout << "    ";
        cout << i;
        if(graph[i].mark == 'r')
            cout << " [color = red]";
        cout<<"\n";
    }
    
    for(int i = 0; i < graph.size(); i++){
        for(int t = 0; t < matrix[i].size(); t++){
            if(matrix[i][t].there == 1){
                    cout << "    " << i << " -- " << matrix[i][t].val;
                    if(matrix[i][t].mark == 'r')
                        cout << " [color = red]";
                    cout<< "\n";
            }
        }
    }
    cout << "}";
}

void VisitVertex(vector<top> &graph, int v, vector<vector<top>> &matrix,int &nTops, int &nRibs,int place){
    graph[v].mark = 'g';
    
    if(graph[v].place != place){
        nTops++;
        graph[v].place = place;
    }
    
    for(int i = 0; i < matrix[v].size(); i++){
        if(matrix[v][i].mark == 'w'){
            matrix[v][i].mark = 'g';
            matrix[v][i].place = place;
            if(matrix[v][i].there == 1)
                nRibs ++;
            VisitVertex(graph, matrix[v][i].val, matrix,nTops, nRibs,place);
        }
    }
}
void DFS(vector<top> &graph, vector<vector<top>> &matrix, vector<big> &reds) {
    int place = 0;
    for(int v = 0; v < graph.size(); v++)
        if(graph[v].mark == 'w'){
            place++;
            int nTops = 0;
            int nRibs = 0;
            VisitVertex(graph, v, matrix,nTops,nRibs,place);
            reds.push_back({nTops,nRibs});
        }
}

int max(vector<big> reds){
    int max = -1;
    int place = -1;
    int maxRibs = -1;
    for(int i = 0; i < reds.size(); i++)
        if(reds[i].top > max || (reds[i].top == max && reds[i].rib > maxRibs)) {
            max = reds[i].top;
            maxRibs = reds[i].rib;
            place = i;
        }
    return place;
}

void VisitVertexReds(vector<top> &graph, int v, vector<vector<top>> &matrix,int &place){
    graph[v].mark = 'r';
    for(int i = 0; i < matrix[v].size(); i++)
        if(matrix[v][i].mark != 'r'){
            matrix[v][i].mark = 'r';
            VisitVertexReds(graph, matrix[v][i].val, matrix, place);
        }
}

void DFSreds(vector<top> &graph, vector<vector<top>> &matrix, int place) {
    for(int v = 0; v < graph.size(); v++){
        if(graph[v].place == place){
            VisitVertexReds(graph, v, matrix, place);
            v = graph.size() + 1;
        }
    }
}

void test(vector<top> &graph, vector<vector<top>> &matrix, vector<big> reds) {
    for(int i = 0; i < graph.size(); i++){
        cout << "place" << i << ":";
        for(int t = 0; t < matrix[i].size(); t++){
            std::cout << matrix[i][t].place << " ";
        }
        std::cout << "\n";
    }
    for(int i = 0; i < graph.size(); i++){
        cout << "mark" << i << ":";
        for(int t = 0; t < matrix[i].size(); t++){
            std::cout << matrix[i][t].mark << " ";
        }
        std::cout << "\n";
    }
    cout << "place" << ":\n";
    for(int i = 0; i < graph.size(); i++)
        cout << graph[i].place;
    cout << "\nmark" << ":\n";
    for(int i = 0; i < graph.size(); i++)
        cout << graph[i].mark;
    cout << "\n";
    for(int i = 0; i < graph.size(); i++){
        cout << "val" << i << ":";
        for(int t = 0; t < matrix[i].size(); t++){
            std::cout << matrix[i][t].val << " ";
        }
        std::cout << "\n";
    }
    for(int i = 0; i < graph.size(); i++){
        cout << "there" << i << ":";
        for(int t = 0; t < matrix[i].size(); t++){
            std::cout << matrix[i][t].there << " ";
        }
        std::cout << "\n";
    }
    cout << "max:" << max(reds) + 1;
}
int main(int argc, const char * argv[]) {
    long tops,ribs;
    cin >> tops;
    cin >> ribs;
    vector<top> graph;

    for(int i = 0; i < tops; i++)
        graph.push_back({i,'w'});
    
   vector<vector<top>> matrix(tops);
   
   for(int i = 0; i < ribs; i++){
       int j,t;
       cin >> j >> t;
       matrix[j].push_back({t,'w',0, 1});
       if (j != t)
           matrix[t].push_back({j, 'w',0});
   }
    
    vector<big> reds;
    DFS(graph, matrix, reds);
    DFSreds(graph, matrix, max(reds) + 1);
    /*test(graph, matrix,reds);*/
    graphPrint(graph, matrix);
    return 0;
}
