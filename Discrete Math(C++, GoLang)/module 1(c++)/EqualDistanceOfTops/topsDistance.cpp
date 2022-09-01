#include <vector>
#include <iostream>
#include <string>
using namespace std;

vector<vector<int>> Deixtra( vector<vector<int>> &matrix, long &newTops) {
    vector<vector<int>> ans(newTops);
    for(int v = 0; v < newTops; v++){
        int opora;
        cin >> opora;
        ans[v] = vector<int>(matrix.size(), -1);
        vector<int> now;
        now.push_back(opora);
        ans[v][opora] = 0;
        for(int i = 0; i < now.size(); i++){
            for(int j:matrix[now[i]]){
                if(ans[v][j] == -1 || ans[v][now[i]] + 1 < ans[v][j]){
                    now.push_back(j);
                    ans[v][j] = ans[v][now[i]] + 1;
                    
                }
            }
        }
            
    }
    return ans;
}

 void printTops(vector<vector<int>> &matrix, vector<vector<int>> &ans, long &newTops){
     int minusans = 0;
     for(int i = 0; i < matrix.size(); i++){
         int now = 1;
         for(int j = 1; j < newTops; j++){
             if(ans[0][i] == -1 || ans[j][i] != ans[0][i])
                 now = 0;
             if(now == 0)
                 break;
         }
         if(now == 1){
             cout << i << " ";
             minusans = 1;
         }
     }
     if(minusans == 0)
         cout << "-";
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
       matrix[t].push_back(j);
   }
    long newTops;
    cin >> newTops;
    vector<vector<int>> ans = Deixtra( matrix, newTops);
   
    printTops(matrix, ans, newTops);
    
    return 0;
}
