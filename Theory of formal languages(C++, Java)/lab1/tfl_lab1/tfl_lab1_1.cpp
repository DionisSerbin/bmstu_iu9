#include <iostream>
#include <vector>
#include <string>
#include <fstream>


using namespace std;



vector<string> delete_right_part(vector<string> &s) {
    for(int i = 0; i < s.size(); i++)
        for(int j = 0; j < s[i].length(); j++)
            if(s[i][j] == ' ')
                s[i] = s[i].substr(0, j);
    return s;
}

bool check_term(string s){
    string ss = s;
    for(int i = 1; i <= s.length() / 2; i++) {
        string prefix, suffix;
        prefix = s.substr(0, i);
        suffix = s.substr(s.length() - i);
        if(prefix == suffix)
            return false;
    }
    return true;
}

bool sufficiency_confluency(string x, string y) {
    string xx = x;
    string yy = y;
    int length;
    if(x.length() < y.length()) {
        length = x.length();
    } else length = y.length();

    for(int i = 1; i < length; i++) {
        string prefix, suffix;
        prefix = x.substr(0, i);
        suffix = y.substr(y.length() - i);
        if(suffix == prefix)
            return false;
    }
    return true;
}

pair<int,int> check_confluency(vector<string> s) {
    vector<string> ss = s;
    for(int i = 0; i < s.size() - 1; i++) {
        if (!check_term(s[i])){
            return {i, -1};
        }
        for(int j = i + 1; j < s.size(); j++) {
            if(j == s.size() - 1){
                if(!check_term(s[j])){
                    return {j, -1};
                }
            }
            if(!sufficiency_confluency(s[i], s[j]) || !sufficiency_confluency(s[j], s[i])) {
                return {i, j};
            }
        }
    }
    return {-1, -1};
}

void confluency(vector<string> s){
    delete_right_part(s);
    pair<int, int> ans;
    ans = check_confluency(s);
    if(ans.first != -1 && ans.second == -1) {
        cout << "система, возможно, не конфлюэнтна (есть перекрытие внутри терма " + s[ans.first] + " ).\n";
    } else if(ans.first != -1 && ans.second != -1){
        cout << "система, возможно, не конфлюэнтна (префикс терма " + s[ans.first] + " совпадает с суффиксом терма " + s[ans.second] + " ).\n";
    } else if( ans.first == -1 && ans.second == -1) {
        cout << "система конфлюэнтна.\n";
    }
}

int main() {

    vector<vector<string>> s(10);
    string line;
    for (int i = 0; i < s.size(); i++) {
        ifstream in("/Users/denisserbin/Documents/tfl_lab1/tfl_lab1_1_test" + to_string(i + 1) + ".txt");
        if (in.is_open()) {
            while (getline(in, line)) {
                s[i].push_back(line + "\n");
            }
        }
        in.close();
    }
    for(int i = 0; i < s.size(); i++)
        confluency(s[i]);

}