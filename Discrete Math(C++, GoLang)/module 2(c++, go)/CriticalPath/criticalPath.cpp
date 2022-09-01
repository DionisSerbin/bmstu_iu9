#include <iostream>
#include <algorithm>
#include <string>
#include <vector>
#include <iomanip>

using namespace std;

class work {
public:string job;
    int timeOwn;
    int timePrivate;
    int number;
    char colour = 'w';
};


int iter = 0;
int start;

void reader(vector<vector<work>> &g, string &s) {
    int i = 0;

    while(i < s.length()){
        //пока есть первая строка
        bool flagLess = false;
        string dopLess;
        while(i < s.length() && s[i] != ';' ){
            //если это метод
            if(s[i] != '<' && s[i] != ' ' && s[i] != ';'){
                string dop_job;
                bool flag = false;
                // считываем метод
                while(s[i] != ' ' && s[i] != ';' && i < s.length()){
                    //если в первый раз появляется метод
                    if(s[i] == '('){
                        flag = true;
                        i++;
                        break;
                    }

                    dop_job += s[i];
                    i++;
                }

                work dop_g;
                dop_g.job = dop_job;
                if(flag){
                    string sifra;
                    while(s[i] <= '9' && s[i] >= '0'){
                        sifra += s[i];
                        i++;
                    }
                    dop_g.timeOwn = stoi(sifra);
                    dop_g.timePrivate = dop_g.timeOwn;
                    dop_g.number = iter++;
                    g.resize(g.size() + 1);
                    g[g.size() - 1].push_back(dop_g);
                    i++;

                    if(flagLess)
                        for(int j = 0; j < g.size(); j++)
                            if(g[j][0].job == dopLess){
                                g[j].push_back(dop_g);
                                break;
                            }
                }else{
                    if(flagLess){
                        for(int j = 0; j < g.size(); j++)
                            if(g[j][0].job == dop_g.job){
                                dop_g.timeOwn = g[j][0].timeOwn;
                                dop_g.timePrivate = dop_g.timeOwn;
                                dop_g.number = g[j][0].number;
                                break;
                            }

                        for(int j = 0; j < g.size(); j++)
                            if(g[j][0].job == dopLess){
                                g[j].push_back(dop_g);
                                break;
                            }
                    }
                }
                dopLess = dop_g.job;
            }
            bool notNeedIter = false;
            flagLess = false;
            if(s[i] == '<'){
                flagLess = true;
                i++;
                while(s[i] == ' ')
                    i++;
                notNeedIter = true;
            }
            if(s[i] == ';')
                notNeedIter = true;
            if(!notNeedIter)
                i++;
        }

        i++;
    }
}

void test(vector<vector<work>> &g,  vector<char> &used) {
    for(int i = 0; i < g.size(); i++){
        for(int j = 0; j < g[i].size(); j++){
            cout << g[i][j].job << "_" << g[i][j].timeOwn << "_" << g[i][j].colour << " ";
        }
        cout << "\n";
    }
}

void dfsBlue(int i, vector<vector<work>> &g, vector<char> &used2){
    used2[i] = true;
    g[i][0].colour = 'b';
    for(int j = 1; j < g[i].size(); j++){
        if(!used2[g[i][j].number]) {
            g[i][j].colour = 'b';
            dfsBlue(g[i][j].number, g, used2);
        }else{
            g[i][j].colour = 'b';
        }
    }
}

void dfsBlue1(int v, vector<vector<work>> &g){

    for(int i = 0; i < g.size(); i++){
        if (i != v) {
            for (int j = 1; j < g[i].size(); j++) {
                if (g[v][0].job == g[i][j].job)
                    g[i][j].colour = 'b';
            }
        } else if(g[v][0].colour != 'b'){
            g[v][0].colour = 'b';
            for(int j = 1; j < g[v].size(); j ++){
                dfsBlue1(g[v][j].number, g);
            }
        }
    }

}

int max(int place, vector<vector<work>> &g,  vector<char> &used){
    int max = -1;
    for(int i = 1; i < g[place].size(); i++){
        if(g[place][i].timeOwn > max && g[place][i].colour != 'b')
            max = g[place][i].timeOwn;
    }
    return max;
}

int dfs1 (int v, vector<vector<work>> &g,  vector<char> &used) {
    used[v] = true;
    if(g[v].size() == 1) {
        if(g[v][0].colour != 'b')
            g[v][0].colour = 'g';
        return g[v][0].timeOwn;
    }
    for (int i = 1; i < g[v].size(); ++i){
        if (!used[ g[v][i].number ]){
            int before = g[v][i].timeOwn;
            g[v][i].timeOwn = dfs1 (g[v][i].number, g, used);
            if(g[v][i].timeOwn < before && g[ g[v][i].number ][0].colour == 'b')
                g[v][i].colour = 'b';
            if(g[v][i].colour != 'b') {
                g[v][i].colour = 'g';
                g[v][0].colour = 'g';
            }
        }
        else if(g[ g[v][i].number ][0].colour == 'g'){
            g[v][i].timeOwn = g[ g[v][i].number ][0].timeOwn;
            g[v][i].colour = 'g';
        } else if(g[ g[v][i].number ][0].colour == 'b'){
            g[v][i].timeOwn = g[v][i].timeOwn - 1;
        }
        else{
            /*vector<char> used2;
            used2.assign(g.size(), false);
            g[v][i].colour = 'b';
            dfsBlue(g[v][0].number, g, used2);*/
            dfsBlue1(g[v][0].number, g);
        }
    }
    if(g[v][0].colour != 'b')
        g[v][0].colour = 'g';
    g[v][0].timeOwn += max(v, g, used);
    return g[v][0].timeOwn;
}

void makeRed(vector<vector<work>> &g, int v) {
    g[v][0].colour = 'r';
    int need = g[v][0].timeOwn - g[v][0].timePrivate;
    for(int i = 1; i < g[v].size(); i++){
        if(need == g[v][i].timeOwn && g[v][i].colour != 'b'){
            g[v][i].colour = 'r';
            makeRed(g, g[v][i].number);
        }
    }
}

void mySort(vector<vector<work>> &g){
    for(int i = 0; i < g.size() - 1; i++){
        for(int j = i + 1; j < g.size(); j++){
            if(g[i][0].job > g[j][0].job){
                vector<work> swapped = g[i];
                g[i] = g[j];
                g[j] = swapped;
            }
        }
    }

}

void mySort2(vector<vector<work>> &g){
    for(int i = 0; i < g.size(); i++){
        for(int j = 1; j < g[i].size() - 1; j++){
            for(int t = j + 1; t < g[i].size(); t++){
                if(g[i][j].job > g[i][t].job){
                    work swapped = g[i][j];
                    g[i][j] = g[i][t];
                    g[i][t] = swapped;
                }
            }
        }
    }
}

void makeTheSameBlue(vector<vector<work>> &g, int i){
    string dop = g[i][0].job;
    for(int j = 1; j < g[i].size(); j++){
        if(dop == g[i][j].job)
            g[i][j].colour = 'b';
    }
}

void printGraph(vector<vector<work>> &g){
    cout << "digraph {\n";
    for(int i = 0; i < g.size(); i++){
        cout << " " << g[i][0].job << " [label = \"" << g[i][0].job << "(" << g[i][0].timePrivate << ")" << "\"";
        if(g[i][0].colour == 'r'){
            cout << ", color = red]\n";
        }else if (g[i][0].colour == 'b'){
            makeTheSameBlue(g, i);
            cout << ", color = blue]\n";
        }else{
            cout << "]\n";
        }
    }

    for(int i = 0; i < g.size(); i++){
        for(int j = 1; j < g[i].size(); j++){
            cout << " " << g[i][0].job << " -> " << g[i][j].job;
            if(g[i][j].colour == 'r'){
                cout << " [color = red]\n";
            } else if (g[i][j].colour == 'b' && g[i][0].colour == 'b'){
                cout << " [color = blue]\n";
            }else{
                cout << "\n";
            }

        }
    }
    cout << "}";
}

void next() { cin.ignore(); }
char get() { return isspace(cin.peek()) ? (next(), get()) : cin.peek(); }
bool eof() { return get() == EOF || get() == '~'; }

vector<int> checkPeeks(vector<vector<work>> g){
    vector<int> buff;
    int max_i = -1;
    int save_i = -1;
    for(int i = 0; i < g.size(); i++){
        int flag = 0;
        string dop = g[i][0].job;
        for(int j = 0; j < g.size(); j++){
            for(int t = 1; t < g[j].size(); t++){
                if(dop == g[j][t].job){
                    flag = 1;
                    break;
                }
            }
            if(flag == 1)
                break;
        }
        if(flag != 1){
            if(g[i][0].timeOwn > max_i){
                max_i = g[i][0].timeOwn;
                save_i = i;
                buff.clear();
                buff.push_back(save_i);
            }else if(g[i][0].timeOwn == max_i){
                buff.push_back(i);
            }
        }
    }
    return buff;
}

void fixDouble(vector<vector<work>> &g){
    for(int i = 0; i < g.size(); i++){
        for(int j = 1; j < g[i].size() - 1; j++){
            for(int t = j + 1; t < g[i].size(); t++){
                if(g[i][j].job == g[i][t].job){
                    g[i].erase(g[i].begin() + t);
                }
            }
        }
    }
}

int main(int argc, const char * argv[]) {
    vector<vector<work>> g(0);
    string s;
    vector<char> used;
    while(1){
        if(eof())
            break;
        else {
            if(get() == '<'){
                s += " ";
            }
            s += get();
            if (get() == ';'){
                s += " ";
            }
            if(get() == '<'){
                s += " ";
            }
        }
        next();
    }
    reader(g, s);
    used.assign(g.size(), false);
    fixDouble(g);
    for(int i = 0; i < g.size(); i++)
        if(!used[i])
            g[i][0].timeOwn = dfs1 (i, g, used);
    vector<int> buf = checkPeeks(g);
    for(int i : buf) {
        makeRed(g, i);
    }
    //test(g, used);
    mySort(g);
    mySort2(g);


    printGraph(g);
    return 0;
}



