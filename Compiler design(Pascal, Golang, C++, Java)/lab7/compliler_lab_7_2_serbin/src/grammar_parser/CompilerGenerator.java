package grammar_parser;

import syntax_analyze.Parser;
import syntax_analyze.rules.RHS;
import syntax_analyze.rules.Rules;
import syntax_analyze.symbols.Nonterm;
import syntax_analyze.symbols.Symbol;
import syntax_analyze.symbols.Term;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class CompilerGenerator extends Parser {
    protected HashMap<String, Rules> gLst;

    private HashMap<String, HashSet<String>> FIRST = new HashMap<>();
    private HashMap<String, HashSet<String>> FOLLOW = new HashMap<>();
    public CompilerGenerator(ArrayList<String> terms, ArrayList<String> nonterms, Nonterm axiom,
                             HashMap<String, Rules> gLst) {
        this.terms    = terms;
        this.nonterms = nonterms;
        this.gLst = gLst;
        this.axiom = axiom;
        for (String t: nonterms) {
            FIRST.put (t, new HashSet<>());
            FOLLOW.put(t, new HashSet<>());
        }
        buildFIRST();
        buildFOLLOW();
        isLL1();
        calculateDelta();
        log.append("Terms: ").append(terms.toString()).append('\n')
                .append("Nonterms: ").append(nonterms.toString()).append('\n')
                .append("Axiom: ").append(axiom.toString()).append('\n')
                .append("FIRST: ").append(FIRST.toString()).append('\n')
                .append("FOLLOW: ").append(FOLLOW.toString()).append('\n');
        for (int i = 0; i < nonterms.size(); ++i) {
            for (int j = 0; j < terms.size(); ++j) {
                log.append(String.format("q[%s][%s] = %s\n", nonterms.get(i), terms.get(j),
                        q[i][j] != null ? q[i][j].toString() : "ERROR"));
            }
        }
    }

    private HashSet<String> calculateFIRST(RHS chunk) {
        HashSet<String> res = new HashSet<>();
        if (chunk.equals(RHS.EPSILON)) {
            res.add(Term.EPSILON);
            return res;
        }
        for (Symbol symbol: chunk) {
            if (symbol instanceof Term) {
                res.add(symbol.getType());
                return res;
            }
            HashSet<String> symbol_first = FIRST.get(symbol.getType());
            if (!symbol_first.contains(Term.EPSILON)) {
                res.addAll(symbol_first);
                return res;
            } else {
                HashSet<String> copy = new HashSet<>(symbol_first);
                copy.remove(Term.EPSILON);
                res.addAll(copy);
            }
        }
        res.add(Term.EPSILON);
        return res;
    }

    private void buildFIRST() {
        for (Map.Entry<String, Rules> pair: gLst.entrySet()) {
            for (RHS chunk: pair.getValue()) {
                if (chunk.isEmpty()) continue;
                Symbol symbol = chunk.get(0);
                if (symbol instanceof Term) {
                    FIRST.get(pair.getKey()).add(symbol.getType());
                }
            }
        }
        boolean changed;
        do {
            changed = false;
            for (Map.Entry<String, Rules> pair: gLst.entrySet()) {
                for (RHS chunk: pair.getValue()) {
                    changed |= (FIRST.get(pair.getKey())).addAll(calculateFIRST(chunk));
                }
            }
        } while (changed);
    }



    private void buildFOLLOW() {
        FOLLOW.get(axiom.getType()).add(Term.EOF);
        for (Rules rule: gLst.values()) {
            for (RHS chunk: rule) {
                if (chunk.isEmpty()) continue;
                for (int i = 0; i < chunk.size() - 1; ++i) {
                    Symbol symbol = chunk.get(i);
                    if (symbol instanceof Nonterm) {
                        HashSet<String> sublist_first = calculateFIRST(
                                new RHS(
                                        chunk.subList(i+1,
                                                chunk.size()
                                        )
                                )
                        );
                        sublist_first.remove(Term.EPSILON);
                        FOLLOW.get(symbol.getType()).addAll(sublist_first);
                    }
                }
            }
        }

        boolean changed;
        do {
            changed = false;
            for (Map.Entry<String, Rules> pair: gLst.entrySet()) {
                String X = pair.getKey();
                for (RHS chunk: pair.getValue()) {
                    if (chunk.isEmpty()) continue;
                    int last_elem = chunk.size() - 1;
                    Symbol Y = chunk.get(last_elem);
                    if (Y instanceof Nonterm) {
                        changed |= FOLLOW.get(Y.getType()).addAll(FOLLOW.get(X));
                    } else {
                        continue;
                    }
                    for (int i = last_elem-1; i >= 0; --i) {
                        Y = chunk.get(i);
                        if (Y instanceof Term) break;
                        HashSet<String> sublist_first = calculateFIRST(
                                new RHS(
                                        chunk.subList(i+1,
                                                chunk.size()
                                        )
                                )
                        );
                        if (sublist_first.contains(Term.EPSILON)) {
                            changed |= FOLLOW.get(Y.getType()).addAll(FOLLOW.get(X));
                        } else {
                            break;
                        }
                    }
                }
            }

        } while (changed);
    }

private boolean isFIRSTAndFOLLOW(RHS u, HashSet<String> firstU,
                                 RHS v, HashSet<String> firstV,
                                 String A, HashSet<String> followA) {

    HashSet<String> intersection_uA = new HashSet<>(firstU);
    intersection_uA.retainAll(followA);
    if (firstV.contains(Term.EPSILON) && !intersection_uA.isEmpty()) {
        StringBuilder log = new StringBuilder();
        log.append("** Grammar not LL(1): at " ).append(v.getCoords().toString()).append(' ')
                .append(v.toString()).append(" =>* epsilon")
                .append("and FIRST (").append(u.toString()).append(" at ").append(u.getCoords().toString()).append(' ')
                .append(") and FOLLOW (").append(A).append(") != empty").append('\n');
        log.append("FIRST ").append(u.toString()).append(" = ").append(firstU.toString());
        log.append("FOLLOW ").append(A).append(" = ").append(followA.toString()).append('\n');
        System.out.print(log);
        this.log.append(log);
        return true;
    }
    return false;
}

    private void isLL1() {
        boolean error = false;
        for (Map.Entry<String, Rules> entry: gLst.entrySet()) {
            String A = entry.getKey();
            HashSet<String> follow_A = FOLLOW.get(A);
            Rules rules = entry.getValue();
            for (int i = 0; i < rules.size() - 2; ++i) {
                RHS u = rules.get(i);
                HashSet<String> first_u = calculateFIRST(u);
                for (int j = i + 1; j < rules.size() - 1; ++j) {
                    RHS v = rules.get(j);
                    HashSet<String> first_v = calculateFIRST(v);
                    HashSet<String> intersection = new HashSet<>(first_u);
                    intersection.retainAll(first_v);
                    if (!intersection.isEmpty()) {
                        error = true;
                        System.out.println("Grammar not LL(1): FIRST (u) and FIRST (v) != empty for " +
                        u + " at " + u.getCoords().toString() +  " and " + v + " at " + v.getCoords().toString());
                        System.out.println("FIRST "+ u +" = " + calculateFIRST(u));
                        System.out.println("FIRST "+ v +" = " + calculateFIRST(v));
                    }
                    error |= isFIRSTAndFOLLOW(u, first_u, v, first_v, A, follow_A);
                    error |= isFIRSTAndFOLLOW(v, first_v, u, first_u, A, follow_A);
                }
            }
        }
        if (error) System.exit(3);
    }

    private void calculateDelta() {
        if (!terms.contains(Term.EOF)) {
            terms.add(Term.EOF);
        }
        int m = nonterms.size();
        int n = terms.size();
        q = new RHS[m][n];
        for (RHS[] line : q) {
            Arrays.fill(line, RHS.ERROR);
        }
        for (Map.Entry<String, Rules> pair: gLst.entrySet()) {
            String X = pair.getKey();
            for (RHS rule: pair.getValue()) {
                HashSet<String> chunk_first = calculateFIRST(rule);
                for (String a: chunk_first) {
                    if (!a.equals(Term.EPSILON)) {
                        q[nonterms.indexOf(X)][terms.indexOf(a)] = rule;
                    } else {
                        for (String b: FOLLOW.get(X)) {
                            q[nonterms.indexOf(X)][terms.indexOf(b)] = RHS.EPSILON;
                        }
                    }
                }
            }
        }
    }

    private StringBuilder makeFile(ArrayList<String> list)
    {
        StringBuilder res = new StringBuilder(
                "        return new ArrayList<>(Arrays.asList(\n                "
        );
        if (!list.isEmpty()) {
            res.append('"').append(list.get(0)).append('"');
        }
        for (int i = 1; i < list.size(); ++i) {
            res.append(", ").append('"').append(list.get(i)).append('"');
        }
        res.append("\n        ));\n");
        return res;
    }

    public String printCompiler(String classname) {
        StringBuilder res = new StringBuilder(
                "import syntax_analyze.rules.RHS;\n" +
                        "import syntax_analyze.Parser;\n" +
                        "import syntax_analyze.symbols.Nonterm;\n" +
                        "import syntax_analyze.symbols.Term;\n\n" +
                        "import java.util.ArrayList;\n" +
                        "import java.util.Arrays;\n\n" +
                        "public class " + classname + " {\n" +
                        "    public final static ArrayList<String> terms = staticTermList();\n" +
                        "    public final static ArrayList<String> nonterms = staticNontermList();\n" +
                        "    public final static Nonterm axiom = " + axiom.printConstructor() + ";\n" +
                        "    public final static RHS[][] q = staticDelta();\n\n" +
                        "    public static Parser getParser() {\n" +
                        "        return new Parser(terms, nonterms, axiom, q);\n" +
                        "    }\n\n"
        );
        res.append("    private static ArrayList<String> staticNontermList() {\n")
                .append(makeFile(nonterms))
                .append("    }\n");

        res.append("    private static ArrayList<String> staticTermList() {\n")
                .append(makeFile(terms))
                .append("    }\n");;


        res.append("    private static RHS[][] staticDelta() {\n" +
                "        ArrayList<String> T = terms;\n" +
                "        ArrayList<String> N = nonterms;\n" +
                "        int m = N.size();\n" +
                "        int n = T.size();\n" +
                "        RHS[][] q = new RHS[m][n];\n" +
                "        for (RHS[] line: q) {\n" +
                "            Arrays.fill(line, RHS.ERROR);\n" +
                "        }\n");
        for (int i = 0; i < q.length; ++ i) {
            for (int j = 0; j < q[0].length; ++j) {
                if (!RHS.isError(q[i][j])) {
                    res.append(String.format("        q[%d][%d] = ", i, j))
                            .append(q[i][j].printConstructor()).append(";\n");
                }
            }
        }
        res.append("        return q;\n    }\n");
        res.append("}\n");
        return res.toString();
    }

    public void calculateJava(String path) {
        File javafile = new File(path);
        try {
            Files.write(javafile.toPath(), printCompiler(javafile.getName().replace(".java", "")).getBytes());
        } catch (IOException e) {
            System.err.printf("file %s cannot be read\n", javafile.toPath());
        }
    }

}
