
#include <iostream>
#include <vector>

using namespace std;
class condens {
public:vector<int> componentaSvyaz;
};

vector < vector<int> > g, gr;
vector<vector<vector<int>>> topsComponent;
vector<condens> componentCondens;
vector<char> used;
vector<int> order, component;
vector<int> base;
vector<int> dopVector;

void dfs1 (int v) {
    used[v] = true;
    for (int i = 0; i < g[v].size(); ++i)
        if (!used[ g[v][i] ])
            dfs1 (g[v][i]);
    order.push_back (v);
}

void dfs2 (int v) {
    used[v] = true;
    component.push_back (v);
    for (int i = 0; i < gr[v].size(); ++i)
        if (!used[ gr[v][i] ])
            dfs2 (gr[v][i]);
}


bool dFS (int v, int j) {

    for(int t : componentCondens[j].componentaSvyaz){
        for(int i : g[v]){
            if(t == i)
                return true;
        }
    }
    return false;
}

void printComponent(vector<int> x) {
    for(int i : x)
        cout << i << "_";
    cout << " ";
}

void test(){
    cout << "\n\n";
    /*5
    7
    0 1
    1 3
    2 1
    3 2
    4 0
    4 2
    4 3*/
    for(int i = 0; i < g.size(); i++){

        for(int j = 0; j < g[i].size(); j++){
            cout << g[i][j] << " ";
        }
        cout << "\n";
    }
    cout << "\n\n";

    for(int i = 0; i < order.size(); i++)
        cout << order[i] << " " ;
    cout << "\n\n";

    for(int i = 0; i < componentCondens.size(); i++){
        for(int j = 0; j < componentCondens[i].componentaSvyaz.size(); j++)
            cout << componentCondens[i].componentaSvyaz[j] << " " ;
        cout << "\n";
    }
    cout << "\n\n";

    for(int i = 0; i < topsComponent.size(); i++){
        printComponent(topsComponent[i][0]);
        for(int j = 1; j < topsComponent[i].size(); j++)
            printComponent(topsComponent[i][j]);
        cout << "\n";
    }
}

void makeComponentAndBasse(int n){
    used.assign(n, false);
    topsComponent.resize(componentCondens.size());
    bool flag = false;
    for(int i =0; i < componentCondens.size(); i++){
        topsComponent[i].push_back(componentCondens[i].componentaSvyaz);
    }

    for(int i = 0; i < componentCondens.size() - 1; i++){
        for(int j = i + 1; j < componentCondens.size(); j++){

            for(int x : componentCondens[i].componentaSvyaz){
                if(dFS(x, j)){
                    flag = true;
                    break;
                }
            }
            if(flag)
                topsComponent[i].push_back(componentCondens[j].componentaSvyaz);
            flag = false;

            for(int x : componentCondens[j].componentaSvyaz){
                if(dFS(x, i)){
                    flag = true;
                    break;
                }
            }
            if(flag)
                topsComponent[j].push_back(componentCondens[i].componentaSvyaz);
            flag = false;
        }
    }
}

int min(vector<int> mass){
    int min = 2147483647;
    for(int x : mass){
        if(x < min)
            min = x;
    }
    return min;
}
void recursDelete(int i){
    for(int t = 1; t < topsComponent[i].size(); t++){
        int j_size = topsComponent.size();
        for(int j = i + 1; j < j_size; j++) {
            if (topsComponent[i][t] == topsComponent[j][0]) {
                recursDelete(j);
                //cout << topsComponent[j].size() << "? ";
                int size = topsComponent[j].size();
                for(int z = 1; z < size; z++) {
                    topsComponent[j].pop_back();
                }
                topsComponent[j][0] = dopVector;
            }
        }
    }
}

void makeBase(){
    for(int i = 0; i < topsComponent.size(); i++){
        recursDelete(i);
    }

    /*for(int i = 0; i < topsComponent.size(); i++){
        printComponent(topsComponent[i][0]);
        for(int j = 1; j < topsComponent[i].size(); j++)
            printComponent(topsComponent[i][j]);
        cout << "\n";
    }*/

    if(topsComponent.size() == 1){
        base.push_back(min(topsComponent[0][0]));
    } else {
        for (int i = 0; i < topsComponent.size(); i++) {
            if (topsComponent[i].size() > 1 || (topsComponent[i].size() == 1 && topsComponent[i][0] != dopVector))
                base.push_back(min(topsComponent[i][0]));
        }
    }
}

void quickSort(vector<int> &numbers, int left, int right)
{
    int pivot; // разрешающий элемент
    int l_hold = left; //левая граница
    int r_hold = right; // правая граница
    pivot = numbers[left];
    while (left < right) // пока границы не сомкнутся
    {
        while ((numbers[right] >= pivot) && (left < right))
            right--; // сдвигаем правую границу пока элемент [right] больше [pivot]
        if (left != right) // если границы не сомкнулись
        {
            numbers[left] = numbers[right]; // перемещаем элемент [right] на место разрешающего
            left++; // сдвигаем левую границу вправо
        }
        while ((numbers[left] <= pivot) && (left < right))
            left++; // сдвигаем левую границу пока элемент [left] меньше [pivot]
        if (left != right) // если границы не сомкнулись
        {
            numbers[right] = numbers[left]; // перемещаем элемент [left] на место [right]
            right--; // сдвигаем правую границу вправо
        }
    }
    numbers[left] = pivot; // ставим разрешающий элемент на место
    pivot = left;
    left = l_hold;
    right = r_hold;
    if (left < pivot) // Рекурсивно вызываем сортировку для левой и правой части массива
        quickSort(numbers, left, pivot - 1);
    if (right > pivot)
        quickSort(numbers, pivot + 1, right);
}

int main() {
    int n, m;
    cin >> n >> m;
    g.resize(n);
    gr.resize(n);
    dopVector.push_back(-1);

    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        g[a].push_back (b);
        gr[b].push_back (a);
    }

    used.assign(n, false);

    for (int i = 0; i < n; ++i)
        if (!used[i])
            dfs1 (i);

    used.assign(n, false);

    for (int i = 0; i < n; ++i) {
        int v = order[n - 1 - i];
        if (!used[v]) {
            dfs2 (v);
            condens now;
            now.componentaSvyaz = component;
            componentCondens.push_back(now);
            component.clear();
        }
    }

    makeComponentAndBasse(n);
    //test();
    makeBase();
    quickSort(base, 0, base.size() - 1);
    for(int x : base)
        cout << x << " ";

}
