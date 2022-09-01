#include <iostream>
#include <vector>
#include <math.h>
int between(int big, int little, std::vector<long> a) {
    for (int bet = big + 1; bet < little; bet++)
        if (a[big] % a[bet] == 0 && a[bet] % a[little] == 0)
            return 0;
    return 1;
}

void graph(unsigned long j, std::vector<long> a,bool **mas) {
    std::cout << "graph {\n";
    for(int i = 0; i < j; i++) {
        std::cout << "    ";
        std::cout << a[i] <<"\n";
    }
    for(int i = 0; i < j; i++){
        for(int t = i; t < j; t++){
            if(mas[i][t] == 1)
                std::cout << "    " << a[i] << " -- " << a[t] << "\n";
        }
    }
    std::cout << "}";
}

int main(int argc, const char * argv[]) {
    long n;
    std::cin >> n;
    std::vector<long> a;

    for(long i = floor(sqrt(n)); i > 1; i--)
           if(n % i == 0){
               a.push_back(i);
               if(n / i != i)
                   a.insert(a.begin(), n / i);
           }
    if(n == 1){
        a.push_back(n);
    }
    else{
        a.insert(a.begin(), n);
        a.push_back(1);
    }
    unsigned long j = a.size();
    bool** mas = new bool *[j];
    for (int i = 0; i < j; i++)
        mas[i] = new bool [j];
    
    for(int i = 0; i < j; i++){
        for(int t = i; t < j; t++){
            if(i != t && a[i] % a[t] == 0 && between(i, t, a) == 1){
                mas[i][t] = 1;
                mas[t][i] = 1;
            }
            else {
                mas[i][t] = 0;
                mas[t][i] = 0;
            }
        }
    }
    
   /*for(int i = 0; i < j; i++){
        for(int t = 0; t < j; t++){
            std::cout << mas[i][t] << " ";
        }
        std::cout << "\n";
    }*/
    graph(j, a, mas);
    for(int i = 0; i < j; i++)
        delete [] mas[i];
    delete [] mas;
    
    return 0;
}
