package syntax_analyze.rules;

import grammar_parser.Compilable;

import java.util.ArrayList;
import java.util.Arrays;

public class Rules extends ArrayList<RHS> implements Compilable{

    public Rules(RHS... rules) {
        super(Arrays.asList(rules));
    }

    public String printConstructor() {
        StringBuilder res = new StringBuilder("new Rules(");
        if (!isEmpty()) {
            res.append("\n        ").append(get(0).printConstructor());
        }
        for (int i = 1; i < size(); ++i) {
            res.append(",\n        ").append(get(i).printConstructor());
        }
        res.append("\n)\n");
        return  res.toString();
    }
}
