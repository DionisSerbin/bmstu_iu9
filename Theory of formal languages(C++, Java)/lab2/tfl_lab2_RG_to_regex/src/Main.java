import javafx.util.Pair;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
// /Users/denisserbin/lab2/tfl_lab2_RG_to_regex/test4_tfl_lab2_RG_to_regex
// /Users/denisserbin/tfl_lab2_regex/test2_tfl_lab2_RG_to_regex
public class Main {
    public static ArrayList<ArrayList<Pair<String, Integer>>> system = new ArrayList<>();
    public static ArrayList<String> terms = new ArrayList<>(0);
    public static ArrayList<ArrayList<String>> systemRG = new ArrayList<>();

    public static boolean parseExpr(String expr) {
        String[] var = expr.split("[\\s=+]");
        ArrayList<Pair<String, Integer>> terms_exprs = new ArrayList<>();
        for(int i = 1; i < var.length; i++){
            if(var[i].matches("(?=.*[A-Z]).+$")){
                String[] x = var[i].split("(?=\\p{Upper})");
                if(x.length < 2){
                    return false;
                }
                if(terms.indexOf(x[1]) != -1) {
                    terms_exprs.add(new Pair<>(x[0], terms.indexOf(x[1])));
                } else {
                    return false;
//                    terms.add(x[1]);
//                    terms_exprs.add(new Pair<>(x[0], terms.indexOf(x[1])));
                }
            }
            else {
                terms_exprs.add(new Pair<>(var[i], -1));
            }
        }
        system.add(terms_exprs);
        return true;
    }

    public static ArrayList<Pair<String, Integer>> parseEquation(ArrayList<Pair<String, Integer>> equation,
                                                                 int var, boolean flag) {
        StringBuilder v = new StringBuilder();
        ArrayList<Pair<String, Integer>> newEquation = new ArrayList<>();
        if(flag) {
            v.append("(");
        }
        int iter = 0;
        for (int i = 0; i < equation.size(); i++){
            if(equation.get(i).getValue() == var) {
                iter++;
                if(v.length() > 1) {
                    v.append("+").append(equation.get(i).getKey());
                } else {
                    v.append(equation.get(i).getKey());
                }
            } else {
                newEquation.add(equation.get(i));
            }
        }
        if(flag) {
            v.append(")");
        }
        if(!v.toString().equals("()")) {
            if (iter == 1 && flag) {
                v.deleteCharAt(0);
                v.deleteCharAt(v.length() - 1);
            }
            if (v.length() > 0) {
                newEquation.add(new Pair<>(v.toString(), var));
            }
        }
        return newEquation;
    }

    public static boolean parse(ArrayList<String> exprs) {
        for (int i = 0; i < exprs.size(); i++){
            exprs.set(i, exprs.get(i).replaceAll("\\s+", ""));
        }
        for(int i = 0; i < exprs.size(); i++){
            String[] var = exprs.get(i).split("[\\s=+]");
            terms.add(var[0]);
        }
        for (String expr : exprs) {
            if(!parseExpr(expr)){
                return false;
            }
        }
        for(int i = 0; i < system.size(); i++){
            system.set(i, parseEquation(system.get(i), i, false));
        }
        return true;
    }

    public static ArrayList<Pair<String, Integer>> makeDiffExpr(String expr,
                                                                ArrayList<Pair<String, Integer>> equation, int length){
        ArrayList<Pair<String, Integer>> diffExpr = new ArrayList<>();
        for(int i = 0; i < length; i++){
            StringBuilder s = new StringBuilder();
            s.append(expr);
            s.append(equation.get(i).getKey());
            diffExpr.add(new Pair<>(s.toString(), equation.get(i).getValue()));
        }
        return diffExpr;
    }

    public static void addDiffExpr(ArrayList<Pair<String, Integer>> eq, int place,
                                   ArrayList<Pair<String, Integer>> diffExpr) {
        eq.remove(place);
        for(int i = 0; i < diffExpr.size(); i++){
            eq.add(place, diffExpr.get(i));
        }
    }

    public static void simplifyNextExpr (int place) {
        for(int eq = place + 1; eq < system.size(); eq++){
            for(int eqVar = 0; eqVar < system.get(eq).size(); eqVar++){
                if( system.get(eq).get(eqVar).getValue() == place) {
                    StringBuilder v = new StringBuilder();
                    int first = system.get(place).get( system.get(place).size() - 1).getValue();
                    int second = place;
                    if(first == second) {
                        v.append(system.get(eq).get(eqVar).getKey());
                        v.append("(");
                        v.append(system.get(place).get(system.get(place).size() - 1).getKey());
                        v.append(")");
                        v.append("*");
                        system.get(eq).set(eqVar, new Pair<>(v.toString(), place));
                    } else {
                        v.append(system.get(eq).get(eqVar).getKey());
                        if(system.get(place).size() == 1) {
                            v.append(system.get(place).get(system.get(place).size() - 1).getKey());
                        }
                        system.get(eq).set(eqVar, new Pair<>(v.toString(), system.get(place).get(system.get(place).size() - 1).getValue()));
                    }
                    ArrayList<Pair<String, Integer>> diffExpr = new ArrayList<>();
                    if(system.get(place).size() > 1 && (first != second)) {
                        diffExpr = makeDiffExpr(system.get(eq).get(eqVar).getKey(),
                                system.get(place), system.get(place).size());
                    } else {
                        diffExpr = makeDiffExpr(system.get(eq).get(eqVar).getKey(),
                                system.get(place), system.get(place).size() - 1);
                    }
                    if(diffExpr.size() > 0) {
                        addDiffExpr(system.get(eq), eqVar, diffExpr);
                    }
                    if (diffExpr.size() == 0 && (first == second || system.get(place).get(0).getKey() == "")){
                        system.get(place).set(0, new Pair<>("", -1));
                        if(system.get(eq).size() > 1){
                            system.get(eq).remove(eqVar);
                        }else {
                            system.get(eq).set(eqVar, new Pair<>("", -1));
                        }
                    }
                    ArrayList<Pair<String, Integer>> parseEq = parseEquation(system.get(eq), eq, true);
                    if(parseEq != system.get(eq)){
                        eqVar = 0;
                    }
                    system.set(eq, parseEq);
                }
            }
        }
    }

    public static void simplifyPreviousExpr (int place) {
        StringBuilder s = new StringBuilder();
        s.append(system.get(place).get( system.get(place).size() - 1 ).getKey());
        if(s.length() > 1 && (s.charAt(0) != '(' && s.charAt(s.length() - 1) != ')')){
            s.insert(0 , "(").append(")");
        }
        if(system.get(place).get(system.get(place).size() - 1).getValue() == place) {
            StringBuilder v = new StringBuilder();
            s.append("*");
            system.get(place).remove(system.get(place).size() - 1);
            v.append("(");
            for (int i = 0; i < system.get(place).size(); i++) {
                if (system.get(place).get(i).getKey() != "") {
                    if (v.length() > 1) {
                        v.append("+").append(system.get(place).get(i).getKey());
                    } else {
                        v.append(system.get(place).get(i).getKey());
                    }
                }
            }
            v.append(")");
            if (v.toString().equals("()")) {
                s.deleteCharAt(s.length() - 1);
            } else {
                s.append(v);
            }
            ArrayList<Pair<String, Integer>> newExpr = new ArrayList<>();
            newExpr.add(new Pair<>(s.toString(), -1));
            system.set(place, newExpr);
        } else {
            StringBuilder v = new StringBuilder();
            v.append("(");
            for (int i = 0; i < system.get(place).size(); i++) {
                if (system.get(place).get(i).getKey() != "") {
                    if (v.length() > 1) {
                        v.append("+").append(system.get(place).get(i).getKey());
                    } else {
                        v.append(system.get(place).get(i).getKey());
                    }
                }
            }
            v.append(")");
            ArrayList<Pair<String, Integer>> newExpr = new ArrayList<>();
            newExpr.add(new Pair<>(v.toString(), -1));
            system.set(place, newExpr);
        }
        for(int eq = place - 1; eq > -1; eq--){
            for(int eqVar = 0; eqVar < system.get(eq).size(); eqVar++){
                if(system.get(eq).get(eqVar).getValue() == place) {
                    StringBuilder ss = new StringBuilder();
                    ss.append("(");
                    ss.append(system.get(eq).get(eqVar).getKey());
                    ss.append(system.get(place).get(0).getKey());
                    ss.append(")");
                    if(system.get(place).get(0).getKey().equals("")){
                        system.get(eq).set(eqVar, new Pair<>("", -1));
                    } else {
                        system.get(eq).set(eqVar, new Pair<>(ss.toString(), -1));
                    }
                }
            }
        }
    }

    public static void makeDecision(){
        for(int i = 0; i < system.size(); i++){
            simplifyNextExpr(i);
        }
        for(int i = system.size() - 1; i > -1; i--){
            simplifyPreviousExpr(i);
        }
    }

    public static ArrayList<String> parseRG(ArrayList<String> exprs){
        ArrayList<String> newVar = new ArrayList<>();
        for(int i = 0; i < exprs.size(); i++) {
            String[] var = exprs.get(i).split("[\\s→]");
            for (int t = 0; t < var.length; t++) {
                if (!var[t].equals("")) {
                    newVar.add(var[t]);
                }
            }
        }

        for(int j = 0; j < newVar.size(); j += 2){
            String left = newVar.get(j);
            String right = newVar.get(j + 1);
            ArrayList<String> buf = new ArrayList<>();
            buf.add(left);
            buf.add(right);
            systemRG.add(buf);
        }

        for(int i = 0; i < systemRG.size() - 1; i++){
            for(int j = i + 1; j < systemRG.size(); j++){
                if(systemRG.get(i).get(0).equals(systemRG.get(j).get(0))){
                    int length = systemRG.get(i).size();
                    systemRG.get(j).addAll(systemRG.get(i));
                    systemRG.get(j).remove(systemRG.get(j).size() - length);
                    systemRG.remove(i);
                    j = systemRG.size();
                    i--;
                }
            }
        }
        ArrayList<String> returned = new ArrayList<>();
        for(int i = 0; i < systemRG.size(); i++){
            StringBuilder buf = new StringBuilder();
            for (int j = 0; j < systemRG.get(i).size(); j++){
                if(j == 0){
                    buf.append(systemRG.get(i).get(j)).append(" = ");
                } else if (j != systemRG.get(i).size() - 1){
                    buf.append(systemRG.get(i).get(j)).append(" + ");
                } else {
                    buf.append(systemRG.get(i).get(j));
                }
            }
            returned.add(buf.toString());
        }
        return returned;
    }

    public static void print(){
        int place = terms.indexOf("S");
        for (int i = 0; i < system.get(place).get(0).getKey().length(); i++){
            if(system.get(place).get(0).getKey().charAt(i) == '+'){
                System.out.print("|");
            }else {
                System.out.print(system.get(place).get(0).getKey().charAt(i));
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter path to test: ");
        String pathToTest = in.nextLine();
        Scanner inputData = new Scanner(new FileReader(pathToTest));
        ArrayList<String> exprs = new ArrayList<>(0);
        while(inputData.hasNext()) {
            exprs.add(inputData.nextLine());
        }

        ArrayList<String> exprsRG = parseRG(exprs);

        if(!parse(exprsRG)){
            System.out.println("Система не корректна");
        } else {
            makeDecision();
            print();
        }
    }

}

