package grammar_parser;

import syntax_analyze.Parser;
import syntax_analyze.rules.RHS;
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
                "S", "DEF", "D_N", "N_LST", "D_T", "T_LST",
                "R_LST", "R", "E_LST", "E", "SYM_LST", "SYM", "AXIOM"
//                S-0, DEF-1, D_N-2, N_LST-3, D_T-4, T_LST-5,
//                R_LST-6, R-7, E_LST-8, E-9, SYM_LST-10, SYM-11, AXIOM-12
        ));
    }

    private static ArrayList<String> staticTermList() {
        return new ArrayList<>(Arrays.asList(
                "non-terminal", "terminal", "axiom", "epsilon",
                ",", "|", ";", "::=", "N", "T", "$"
//               non-terminal-0, terminal-1, axiom-2, epsilon-3
//                , - 4, | - 5, ; - 6, ::= - 7, N - 8, T - 9, $ -10
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

        q[0][0] = new RHS(
                new Nonterm("DEF"),
                new Nonterm("R_LST"),
                new Nonterm("AXIOM")
        );

        q[1][0] = new RHS(
                new Nonterm("D_N"),
                new Nonterm("D_T")
        );

        q[2][0] = new RHS(
                new Term("non-terminal"),
                new Term("N"),
                new Nonterm("N_LST"),
                new Term(";")
        );

        q[3][4] = new RHS(
                new Term(","),
                new Term("N"),
                new Nonterm("N_LST")
        );
        q[3][6] = RHS.EPSILON;

        q[4][1] = new RHS(
                new Term("terminal"),
                new Term("T"),
                new Nonterm("T_LST"),
                new Term(";")
        );

        q[5][4] = new RHS(
                new Term(","),
                new Term("T"),
                new Nonterm("T_LST")
        );
        q[5][6] = RHS.EPSILON;

        q[6][2] = RHS.EPSILON;
        q[6][8] = new RHS(
                new Nonterm("R"),
                new Nonterm("R_LST")
        );
        q[6][10] = RHS.EPSILON;

        q[7][8] = new RHS(
                new Term("N"),
                new Term("::="),
                new Nonterm("E"),
                new Nonterm("E_LST"),
                new Term(";")
        );

        q[8][5] = new RHS(
                new Term("|"),
                new Nonterm("E"),
                new Nonterm("E_LST")
        );
        q[8][6] = RHS.EPSILON;

        q[9][3] = new RHS(
                new Term("epsilon")
        );
        q[9][8] = new RHS(
                new Nonterm("SYM"),
                new Nonterm("SYM_LST")
        );
        q[9][9] = new RHS(
                new Nonterm("SYM"),
                new Nonterm("SYM_LST")
        );

        q[10][5] = RHS.EPSILON;
        q[10][6] = RHS.EPSILON;
        q[10][8] = new RHS(
                new Nonterm("SYM"),
                new Nonterm("SYM_LST")
        );
        q[10][9] = new RHS(
                new Nonterm("SYM"),
                new Nonterm("SYM_LST")
        );

        q[11][8] = new RHS(
                new Term("N")
        );
        q[11][9] = new RHS(
                new Term("T")
        );

        q[12][2] = new RHS(
                new Term("axiom"),
                new Term("N"),
                new Term(";")
        );
        return q;
    }
}
