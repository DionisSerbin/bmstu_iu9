#include <iostream>
#include <string>
#include <vector>
#include <fstream>
using namespace std;

void constructors_parse(int &i, string s, vector<pair<string, int>> &constructors){
    string x;
    while ( i < s.length() && s[i] != '('){
        x += s[i];
        i++;
    }
    while(x[0] == ',' || x[0] == ' ')
        x.erase(0, 1);
    string string_to_int;
    while(i < s.length() && s[i] != ')'){
        string_to_int += s[i];
        i++;
    }
    while(string_to_int[0] == ',' || string_to_int[0] == ' ' || string_to_int[0] == '(')
        string_to_int.erase(0, 1);
    int y = stoi(string_to_int);
    constructors.emplace_back(x, y);
}

void TRS_constructors(string s, vector<pair<string, int>> &constructors){
    int i = 0;
    while (s[i] != '=') {
        s.erase(0, 1);
    }
    s.erase(0, 1);
    s.erase(0, 1);
    for(int j = 0; j < s.length(); j++){
        constructors_parse(j, s, constructors);
    }
}

void variables_parse(int &i, string &s, vector<string> &variables){
    string x;
    while(i < s.length() && s[i] != ',') {
        x += s[i];
        s.erase(i, 1);
    }
    while(s[i] == ',' || s[i] == ' ')
        s.erase(i, 1);
    variables.push_back(x);
}

void TRS_variables(string s, vector<string> &variables) {
    int i = 0;
    while (s[i] != '=') {
        s.erase(0, 1);
    }
    s.erase(0, 1);
    s.erase(0, 1);
    for(int j = 0; j < s.length(); ){
        variables_parse(j, s, variables);
    }
}

void first_parse(int &i, string &s, vector <string> &first) {
    string x;
    if(first.empty()) {
        while (i < s.length() && s[i] != '(') {
            x += s[i];
            i++;
        }

        first.push_back(x);

        while (i < s.length() && (s[i] == '(' || s[i] == ' ')) {
            s.erase(i, 1);
        }
        s.erase(s.length() - 1, 1);

        x = "";
    } else {

        int count = 0;
        while (i < s.length()) {
            if (s[i] == '(')
                count++;
            if (s[i] == ')')
                count--;
            if (s[i] == ',' && count == 0)
                break;
            x += s[i];
            i++;
        }
        first.push_back(x);
        while (i < s.length() && (s[i] == ',' || s[i] == ' ')) {
            s.erase(i, 1);
        }
    }
}

void TRS_first(string s, vector <string> &first) {
    int i = 0;
    while (s[i] != '=') {
        s.erase(0, 1);
    }
    s.erase(0, 1);
    s.erase(0, 1);
    for(int i = 0; i < s.length(); ){
        first_parse(i, s, first);
    }
}

void parse_cout(vector<pair<string, int>> first){
    cout << "\n";
    for(auto & i : first){
        cout << i.first << "(" << i.second << ") ";
    }
    cout << "\n";
}

void parse_cout(vector<string> first){
    cout << "\n";
    for(const auto & i : first){
        cout << i << " ";
    }
    cout << "\n";
}

void delete_spaces(vector<string> &s) {
    for (int j = 0; j < s.size(); ++j) {
        for (int i = 0; i < s[j].length(); ++i) {
            if (s[j][i] == ' ') {
                s[j].erase(i, 1);
                i--;
            }
        }
    }
}

void parse_TRS(string str, vector<pair<string, int>> &constructors, vector<string> &variables,
        vector<string> &first, vector<string> &second){
    string sub_str;
    int line = 0;
    for(char i : str){
        sub_str += i;
        if (i == '\n') {
            line++;
            if(line == 1) {
                sub_str.pop_back();
                TRS_constructors(sub_str, constructors);
                cout << "\n" << sub_str;
                sub_str = "";
            } else if(line == 2) {
                sub_str.pop_back();
                TRS_variables(sub_str, variables);
                cout << sub_str;
                sub_str = "";
            } else if(line == 3) {
                sub_str.pop_back();
                TRS_first(sub_str, first);
                cout << sub_str;
                sub_str = "";
            } else if(line == 4) {
                sub_str.pop_back();
                TRS_first(sub_str, second);
                cout << sub_str;
                sub_str = "";
            }
        }
    }
    delete_spaces(first);
    delete_spaces(second);
    parse_cout(constructors);
    parse_cout(variables);
    parse_cout(first);
    parse_cout(second);
}

bool branch(vector<string> first, string x, string symbol, vector<pair<string, int>> constr, vector<string> var);

bool consist_variables(string symbol, vector<string> s) {
    for(auto & i : s){
        if(symbol == i) {
            return true;
        }
    }
    return false;
}

bool consist_constr(string symbol, vector<pair<string, int>> s) {
    for(auto & i : s){
        if(symbol == i.first) {
            return true;
        }
    }
    return false;
}

int find_constr(string symbol, vector<pair<string, int>> z) {
    vector<pair<string, int>> s = z;
    for (int j = 0; j < s.size(); ++j) {
        if(symbol == s[j].first){
            return s[j].second;
        }
    }
    return -5;
}

int divide_on_two(string &s){
    int count_open = 0;
    int count_close = 0;
    for(int i = 0; i < s.length(); i++) {
        if (s[i] == '(')
            count_open++;
        if (s[i] == ')')
            count_close++;
        if(s[i] == ',' && count_open == count_close)
            return i;
    }
    return -1;
}

bool check_rules_of_constr(string s, string x, vector<pair<string, int>> constr){
    string ss = s;
    string xx= x;
    int count_open = 0;
    int count_close = 0;
    int summary_count = 0;
    for(int i = 0; i < s.length(); i++) {
        if (s[i] == '(')
            count_open++;
        if (s[i] == ')')
            count_close++;
        if(s[i] == ',' && count_open - 1 == count_close)
            summary_count++;
    }
    int amount_of = find_constr(x, constr) - 1;
    if(amount_of == -1 && summary_count == 0)
        return true;
    if(summary_count == amount_of) {
        return true;
    } else return false;
}

bool consist(vector<string> first, string sub_str, vector<pair<string, int>> constr, vector<string> var) {
    string x;
    string y;
    string z;
    int i = 0;

    int sub_divide = divide_on_two(sub_str);
    if(sub_divide != -1){
        z = sub_str.substr(0, sub_divide);
        y = sub_str.substr(sub_divide + 1);
        if (!consist(first, z, constr, var)){
            return false;
        }
        if (!consist(first, y, constr, var)) {
            return false;
        }
        return true;
    } else {
        while(i < sub_str.size()) {
            if(sub_str[i] == '('){
                if( !branch(first,x, sub_str, constr, var)){
                    return false;
                }
                return true;
            } else {
                x += sub_str[i];
            }
            i++;
        }
    }
    if(consist_variables(x, var)) {
        return true;
    } else if (consist_constr(x, constr) && check_rules_of_constr(x, x, constr)){
        return true;
    } else return false;
}

string find_subterm(string constr, string str){
    string x = str;
    x.erase(0, constr.length() + 1);
    if(x[x.length() - 1] == ')')
        x.pop_back();
    return x;
}


bool branch(vector<string> first, string x, string symbol, vector<pair<string, int>> constr, vector<string> var) {
    if(consist_variables(x, var)){
        return false;
    }  else if (consist(first, find_subterm(x, symbol), constr, var)) {
        if((consist_constr(x, constr)) && check_rules_of_constr(symbol, x, constr)) {
            return true;
        } else return false;
    }
}

pair<int, int> parse_bracket(string first, string second){
    int i = 0,j = 0;
    while(i < first.length()) {
        if(first[i] == '(')
            break;
        i++;
    }
    while(j < first.length()) {
        if(first[j] == '(')
            break;
        j++;
    }
    return {i, j};
}

//bool there_is(string s, char symbol){
//    for(int i = 0; i < s.length(); i ++){
//        if (s[i] == symbol)
//            return true;
//    }
//    return false;
//}

int check_consist_two_constr(string first, string second, vector<pair<string, int>> constr, vector<string> var) {

    string x_first, y_first, x_second, y_second;
    pair<int, int> bracket;
    bracket = parse_bracket(first, second);
    x_first = first.substr(0, bracket.first);
    if (bracket.first < first.length() ) {
        y_first = first.substr(bracket.first + 1);
        y_first.erase(y_first.length() - 1);
    }
    x_second = second.substr(0, bracket.second);
    if (bracket.second < second.length() ) {
        y_second = second.substr(bracket.second + 1);
        y_second.erase(y_second.length() - 1);
    }
//    if(!(check_rules_of_constr(y_first, x_first, constr) && check_rules_of_constr(y_second, y_first, constr))){
//        return 0;
    if(consist_variables(x_first, var) && consist_constr(x_second, constr)){
        return 2;
    } else if(consist_variables(x_second, var) && consist_constr(x_first, constr)){
        return 1;
    } else if(consist_variables(x_second, var) && consist_variables(x_first, var)){
        return 0;
    }
//    else if(there_is(y_first, ',') && there_is(y_second, ',')) {
//        int one = divide_on_two(y_first);
//        string one_first = y_first.substr(0, one);
//        string one_second = y_first.substr(one + 1);
//        int two = divide_on_two(y_second);
//        string two_first = y_second.substr(0, two);
//        string two_second = y_second.substr(two + 1);
//
//
     else if(x_first == x_second){
            return check_consist_two_constr(y_first, y_second, constr, var);
     }
}

bool unification_rules(int i, vector<pair<string, int>> constructors,vector<string> variables,
        vector<string> first,vector<string> second, string &answer, vector<string> &unificator) {
    if(first.size() != second.size() || first[0] != second[0]) { // проверка свопадают ли функции
        return false;
    } else if(consist_variables(first[i], variables)){ // является ли 1 аргумент переменной
            if(second[i].find(first[i]) != string::npos){ // если 2 арг тоже переменной то нельзя унифицировать
                return true;
            }
            if(consist(first, second[i], constructors, variables)){//если 2 арг функция то все хорошо
                answer += ' ' + first[i] + ":=" + second[i] + ',';
                unificator.push_back(second[i]);
                return true;
            }
    } else if(consist_variables(second[i], variables)){ // если 2 аргумент переменная
        if(first[i].find(second[i]) != string::npos){// если 1 арг тоже переменной то нельзя унифицировать
            return true;
        }
        if(consist(first, first[i], constructors, variables)){ // //если 1 арг функция то все хорошо
            answer += ' ' + second[i] +  ":=" + first[i] + ',';
            unificator.push_back(first[i]);
            return true;
        }
    } else if(consist(first, first[i], constructors, variables) && consist(first, second[i], constructors, variables)) {
        int ans = check_consist_two_constr(first[i], second[i], constructors, variables);
        if (ans == 2) {
            answer += ' ' + first[i] + ":=" + second[i] + ',';
            unificator.push_back(second[i]);
            return true;
        } else if (ans == 1) {
            answer += ' ' + second[i] + ":=" + first[i] + ',';
            unificator.push_back(first[i]);
            return true;
        } else if (ans == 0) {
            return false;
        }
    }
    return false;
}

void unification(string s){

    vector<pair<string, int>> constructors;
    vector<string> variables;
    vector<string> first;
    vector<string> second;
    vector<string> unificator;

    parse_TRS(s, constructors, variables, first, second);
    unificator.push_back(first[0]);

    string answer;

    for(int i = 1; i < first.size(); i++) {
        if (!unification_rules(i, constructors, variables, first, second, answer, unificator)) {
            cout << "термы НЕ унифицируются посредством подстановок";
            return;
        }
    }

    answer[answer.length() - 1] = '.';
    cout << "термы унифицируются посредством подстановок" << answer << " Унификатор: " << unificator[0] << "(";
    for(int j = 1; j < unificator.size(); j++) {
        cout << unificator[j];
        if(j != unificator.size() - 1) {
            cout << ",";
        }
    }

    cout << ")";

}

int main() {

    vector<string> s(11);
    string line;
    for(int i = 0; i < s.size(); i++) {
        ifstream in("/Users/denisserbin/Documents/tfl_lab1/tfl_lab1_test" + to_string(i + 1) + ".txt");
        if (in.is_open()) {
            while (getline(in, line)) {
                s[i] += line + "\n ";
            }
        }
        in.close();
    }

    for (int i = 0; i < s.size(); ++i) {
        unification(s[i]);
        cout << "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n";
    }

    return 0;
}