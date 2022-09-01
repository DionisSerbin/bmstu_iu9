import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Eq {
    public  String var;
    public Map<String, List<String>> varMap;
    public List<String> rg;

    public Eq(String var, Map<String, List<String>> varMap, List<String> rg) {
        this.var = var;
        this.varMap = varMap;
        this.rg = rg;
    }

    public void reduce() {
        List<String> coef = varMap.get(var);
        if (coef != null) {
            String s;
            if (coef.size() == 1) {
                s = coef.get(0);
            } else if (coef.size() == 0) {
                s = "";
            } else {
                s = "(" + String.join("|", coef) + ")";
            }
            varMap.remove(var);
            for (List<String> var : varMap.values()) {
                String varCoef;
                if (var.size() == 1) {
                    varCoef = var.get(0);
                } else if (var.size() == 0) {
                    varCoef = "";
                } else {
                    varCoef = "(" + String.join("|", var) + ")";
                }
                if (s.length() == 1 || (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')')) {
                    varCoef = s + "*" + varCoef;
                } else {
                    varCoef = "(" + s + ")*" + varCoef;
                }

                var.clear();
                var.add(varCoef);
            }
            String rgNow;
            if (this.rg.size() == 1) {
                rgNow =this.rg.get(0);
            } else if (this.rg.size() == 0) {
                rgNow = "";
            } else {
                rgNow = "(" + String.join("|", this.rg) + ")";
            }
            String regex = rgNow;
            if (s.length() == 1 || (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')')) {
                regex = s + "*" + regex;
            } else {
                regex = "(" + s + ")*" + regex;
            }
            this.rg = new ArrayList<>();
            this.rg.add(regex);
        }
    }

    public void change(Eq eq) {
        String varName = eq.var;
        List<String> coef = varMap.get(varName);
        if (coef != null) {
            String s;
            if (coef.size() == 1) {
                s = coef.get(0);
            } else if (coef.size() == 0) {
                s = "";
            } else {
                s = "(" + String.join("|", coef) + ")";
            }
            varMap.remove(varName);
            for (String subs: eq.varMap.keySet()) {
                String kef;
                List<String> coeff = eq.varMap.get(subs);
                if (coeff.size() == 1) {
                    kef = coeff.get(0);
                } else if (coeff.size() == 0) {
                    kef = "";
                } else {
                    kef = "(" + String.join("|", coeff) + ")";
                }
                String newS = s + kef;
                List<String> subsVar = varMap.get(subs);
                if (subsVar == null) {
                    ArrayList<String> newCoeff = new ArrayList<>();
                    newCoeff.add(newS);
                    varMap.put(subs, newCoeff);
                } else {
                    subsVar.add(newS);
                }
            }
            String rgNow;
            if (eq.rg.size() == 1) {
                rgNow = eq.rg.get(0);
            } else if (eq.rg.size() == 0) {
                rgNow = "";
            } else {
                rgNow = "(" + String.join("|", eq.rg) + ")";
            }
            this.rg.add( s + rgNow);
        }
    }
}
