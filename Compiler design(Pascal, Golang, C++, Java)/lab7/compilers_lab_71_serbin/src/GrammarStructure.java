import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

class GrammarParser extends Parser {
    GrammarParser() {
        super(GrammarStructure.termList, GrammarStructure.nontermList, GrammarStructure.axiom, GrammarStructure.q);
    }
}

class GrammarStructure {

    final static ArrayList<String> termList = getTermTags();
    final static ArrayList<String> nontermList = getNonTermTags();
    final static Nonterm axiom = new Nonterm("S");
    final static HashMap<String, String> tagsRegex = getTagRegex();
    final static HashMap<String, Rules> grammarList = getGrammar();
    final static RHS[][] q = analiseTable();

    private static ArrayList<String> getTermTags() {
        return new ArrayList<>(Arrays.asList(
                "N", "T", "NONTERMINALSIGN", "TERMINALSIGN", "AXIOMSIGN", "EPSILON",
                "COMMA", "ORSIGN", "COLON", "EQSIGN", Term.EOF
        ));

    }

    private static ArrayList<String> getNonTermTags() {
        return new ArrayList<>(Arrays.asList(
                "S", "DEF", "D_N", "N_LST", "D_T", "T_LST",
                "R_LST", "R", "E_LST", "E", "SYM_LST", "SYM", "AXIOM"
        ));
    }

    private static HashMap<String, String> getTagRegex() {
        LinkedHashMap<String, String> exprs = new LinkedHashMap<>();
        exprs.put("COMMA", ",");
        exprs.put("ORSIGN", "\\|");
        exprs.put("COLON", ";");
        exprs.put("EQSIGN", "::=");
        exprs.put("NONTERMINALSIGN", "non-terminal");
        exprs.put("TERMINALSIGN", "terminal");
        exprs.put("AXIOMSIGN", "axiom");
        exprs.put("EPSILON", "epsilon");
        exprs.put("N", "[A-Z]+1?");
        exprs.put("T", "(\'[^\\s\"]\'|[a-z])");
        return exprs;
    }

    private static HashMap<String, Rules> getGrammar() {
        HashMap<String, Rules> rules = new HashMap<>();
        rules.put("S", new Rules(new RHS(
                new Nonterm("DEF"),
                new Nonterm("R_LST"),
                new Nonterm("AXIOM")
        )));
        rules.put("DEF",
                new Rules(new RHS(
                        new Nonterm("D_N"),
                        new Nonterm("D_T")
                )));
        rules.put("D_N", new Rules(new RHS(
                new Term("NONTERMINALSIGN"),
                new Term("N"),
                new Nonterm("N_LST"),
                new Term("COLON")
        )));
        rules.put("N_LST", new Rules(
                new RHS(
                        new Term("COMMA"),
                        new Term("N"),
                        new Nonterm("N_LST")
                ),
                new Epsilon()
        ));
        rules.put("D_T", new Rules(new RHS(
                new Term("TERMINALSIGN"),
                new Term("T"),
                new Nonterm("T_LST"),
                new Term("COLON")
        )));
        rules.put("T_LST", new Rules(
                new RHS(
                        new Term("COMMA"),
                        new Term("T"),
                        new Nonterm("T_LST")
                ),
                new Epsilon()
        ));
        rules.put("R_LST",new Rules(
                new RHS(
                        new Nonterm("R"),
                        new Nonterm("R_LST")
                ),
                new Epsilon()
        ));
        rules.put("R", new Rules(new RHS(
                new Term("N"),
                new Term("EQSIGN"),
                new Nonterm("E"),
                new Nonterm("E_LST"),
                new Term("COLON")
        )));
        rules.put("E_LST", new Rules(
                new RHS(
                        new Term("ORSIGN"),
                        new Nonterm("E"),
                        new Nonterm("E_LST")
                ),
                new Epsilon()
        ));
        rules.put("E", new Rules(
                new RHS(
                        new Nonterm("SYM"),
                        new Nonterm("SYM_LST")
                ),
                new RHS(
                        new Term("EPSILON")
                )
        ));
        rules.put("SYM_LST", new Rules(
                new RHS(
                        new Nonterm("SYM"),
                        new Nonterm("SYM_LST")
                ),
                new Epsilon()
        ));
        rules.put("SYM", new Rules(
                new RHS(
                        new Term("N")
                ),
                new RHS(
                        new Term("T")
                )
        ));
        rules.put("AXIOM", new Rules(
                new RHS(
                        new Term("AXIOMSIGN"),
                        new Term("N"),
                        new Term("COLON")
                )
        ));

//        System.out.println(rules.toString());
        return rules;
    }

    private static RHS[][] analiseTable() {
        ArrayList<String> T = termList;
        ArrayList<String> N = nontermList;
        HashMap<String, Rules> rules = grammarList;
        int m = N.size();
        int n = T.size();
        RHS[][] q = new RHS[m][n];
        for (RHS[] line: q) {
            Arrays.fill(line, new Error());
        }

        q[N.indexOf("S")][T.indexOf("NONTERMINALSIGN")] = rules.get("S").get(0);

        q[N.indexOf("DEF")][T.indexOf("NONTERMINALSIGN")] = rules.get("DEF").get(0);

        q[N.indexOf("D_N")][T.indexOf("NONTERMINALSIGN")] = rules.get("D_N").get(0);

        q[N.indexOf("N_LST")][T.indexOf("COMMA")] = rules.get("N_LST").get(0);
        q[N.indexOf("N_LST")][T.indexOf("COLON")] = rules.get("N_LST").get(1);

        q[N.indexOf("D_T")][T.indexOf("TERMINALSIGN")] = rules.get("D_T").get(0);

        q[N.indexOf("T_LST")][T.indexOf("COMMA")] = rules.get("T_LST").get(0);
        q[N.indexOf("T_LST")][T.indexOf("COLON")] = rules.get("T_LST").get(1);

        q[N.indexOf("R_LST")][T.indexOf("AXIOMSIGN")] = rules.get("R_LST").get(1);
        q[N.indexOf("R_LST")][T.indexOf("N")] = rules.get("R_LST").get(0);
        q[N.indexOf("R_LST")][T.indexOf(Term.EOF)] = rules.get("R_LST").get(1);

        q[N.indexOf("R")][T.indexOf("N")] = rules.get("R").get(0);

        q[N.indexOf("E_LST")][T.indexOf("ORSIGN")] = rules.get("E_LST").get(0);
        q[N.indexOf("E_LST")][T.indexOf("COLON")] = rules.get("E_LST").get(1);

        q[N.indexOf("E")][T.indexOf("EPSILON")] = rules.get("E").get(1);
        q[N.indexOf("E")][T.indexOf("N")] = rules.get("E").get(0);
        q[N.indexOf("E")][T.indexOf("T")] = rules.get("E").get(0);

        q[N.indexOf("SYM_LST")][T.indexOf("ORSIGN")] = rules.get("SYM_LST").get(1);
        q[N.indexOf("SYM_LST")][T.indexOf("COLON")] = rules.get("SYM_LST").get(1);
        q[N.indexOf("SYM_LST")][T.indexOf("N")] = rules.get("SYM_LST").get(0);
        q[N.indexOf("SYM_LST")][T.indexOf("T")] = rules.get("SYM_LST").get(0);

        q[N.indexOf("SYM")][T.indexOf("N")] = rules.get("SYM").get(0);
        q[N.indexOf("SYM")][T.indexOf("T")] = rules.get("SYM").get(1);

        q[N.indexOf("AXIOM")][T.indexOf("AXIOMSIGN")] = rules.get("AXIOM").get(0);

        return q;
    }
}

