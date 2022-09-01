import syntax_analyze.rules.RHS;
import syntax_analyze.Parser;
import syntax_analyze.symbols.Nonterm;
import syntax_analyze.symbols.Term;

import java.util.ArrayList;
import java.util.Arrays;

public class GrammarStructure {
    public final static ArrayList<String> terms = staticTermList();
    public final static ArrayList<String> nonterms = staticNontermList();
    public final static Nonterm axiom = new Nonterm("S");
    public final static RHS[][] q = staticDelta();

    public static Parser getParser() {
        return new Parser(terms, nonterms, axiom, q);
    }

    private static ArrayList<String> staticNontermList() {
        return new ArrayList<>(Arrays.asList(
                "S", "DEF", "DN", "NLST", "DT", "TLST", "RLST", "R", "ELST", "E", "SYMLST", "SYM", "AXIOM"
        ));
    }
    private static ArrayList<String> staticTermList() {
        return new ArrayList<>(Arrays.asList(
                "terminal", "non-terminal", ";", ",", "::=", "epsilon", "|", "N", "T", "axiom", "$"
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
        q[0][1] = new RHS(
                new Nonterm("DEF"),
                new Nonterm("RLST"),
                new Nonterm("AXIOM")
                );
        q[1][1] = new RHS(
                new Nonterm("DN"),
                new Nonterm("DT")
                );
        q[2][1] = new RHS(
                new Term("non-terminal"),
                new Term("N"),
                new Nonterm("NLST"),
                new Term(";")
                );
        q[3][2] = RHS.EPSILON;
        q[3][3] = new RHS(
                new Term(","),
                new Term("N"),
                new Nonterm("NLST")
                );
        q[4][0] = new RHS(
                new Term("terminal"),
                new Term("T"),
                new Nonterm("TLST"),
                new Term(";")
                );
        q[5][2] = RHS.EPSILON;
        q[5][3] = new RHS(
                new Term(","),
                new Term("T"),
                new Nonterm("TLST")
                );
        q[6][7] = new RHS(
                new Nonterm("R"),
                new Nonterm("RLST")
                );
        q[6][9] = RHS.EPSILON;
        q[7][7] = new RHS(
                new Term("N"),
                new Term("::="),
                new Nonterm("E"),
                new Nonterm("ELST"),
                new Term(";")
                );
        q[8][2] = RHS.EPSILON;
        q[8][6] = new RHS(
                new Term("|"),
                new Nonterm("E"),
                new Nonterm("ELST")
                );
        q[9][5] = new RHS(
                new Term("epsilon")
                );
        q[9][7] = new RHS(
                new Nonterm("SYM"),
                new Nonterm("SYMLST")
                );
        q[9][8] = new RHS(
                new Nonterm("SYM"),
                new Nonterm("SYMLST")
                );
        q[10][2] = RHS.EPSILON;
        q[10][6] = RHS.EPSILON;
        q[10][7] = new RHS(
                new Nonterm("SYM"),
                new Nonterm("SYMLST")
                );
        q[10][8] = new RHS(
                new Nonterm("SYM"),
                new Nonterm("SYMLST")
                );
        q[11][7] = new RHS(
                new Term("N")
                );
        q[11][8] = new RHS(
                new Term("T")
                );
        q[12][9] = new RHS(
                new Term("axiom"),
                new Term("N"),
                new Term(";")
                );
        return q;
    }
}
