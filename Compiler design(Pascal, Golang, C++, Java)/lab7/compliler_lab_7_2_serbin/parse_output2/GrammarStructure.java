import syntax_analyze.rules.RHS;
import syntax_analyze.Parser;
import syntax_analyze.symbols.Nonterm;
import syntax_analyze.symbols.Term;

import java.util.ArrayList;
import java.util.Arrays;

public class GrammarStructure {
    public final static ArrayList<String> terms = staticTermList();
    public final static ArrayList<String> nonterms = staticNontermList();
    public final static Nonterm axiom = new Nonterm("E");
    public final static RHS[][] q = staticDelta();

    public static Parser getParser() {
        return new Parser(terms, nonterms, axiom, q);
    }

    private static ArrayList<String> staticNontermList() {
        return new ArrayList<>(Arrays.asList(
                "E", "E1", "T", "T1", "F"
        ));
    }
    private static ArrayList<String> staticTermList() {
        return new ArrayList<>(Arrays.asList(
                "+", "*", "(", ")", "n", "$"
        ));
    }
    private static RHS[][] staticDelta() {
        ArrayList<String> T = terms;
        ArrayList<String> N = nonterms;
        int m = N.size();
        int n = T.size();
        RHS[][] q = new RHS[m][n];
        for (RHS[] line: q) {
            Arrays.fill(line, RHS.ERROR);
        }
        q[0][2] = new RHS(
                new Nonterm("T"),
                new Nonterm("E1")
                );
        q[0][4] = new RHS(
                new Nonterm("T"),
                new Nonterm("E1")
                );
        q[1][0] = new RHS(
                new Term("+"),
                new Nonterm("T"),
                new Nonterm("E1")
                );
        q[1][3] = RHS.EPSILON;
        q[1][5] = RHS.EPSILON;
        q[2][2] = new RHS(
                new Nonterm("F"),
                new Nonterm("T1")
                );
        q[2][4] = new RHS(
                new Nonterm("F"),
                new Nonterm("T1")
                );
        q[3][0] = RHS.EPSILON;
        q[3][1] = new RHS(
                new Term("*"),
                new Nonterm("F"),
                new Nonterm("T1")
                );
        q[3][3] = RHS.EPSILON;
        q[3][5] = RHS.EPSILON;
        q[4][2] = new RHS(
                new Term("("),
                new Nonterm("E"),
                new Term(")")
                );
        q[4][4] = new RHS(
                new Term("n")
                );
        return q;
    }
}
