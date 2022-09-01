package grammar_parser;

import lex_analyze.Token;
import semantic_analize.Interpreter;
import syntax_analyze.ParseNode;
import syntax_analyze.ParseTree;
import syntax_analyze.rules.RHS;
import syntax_analyze.rules.Rules;
import syntax_analyze.symbols.Nonterm;
import syntax_analyze.symbols.Symbol;
import syntax_analyze.symbols.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GrammarInterpreter extends Interpreter {
    private ArrayList<String> terms = new ArrayList<>();
    private ArrayList<String> nonterms = new ArrayList<>();
    private Nonterm axiom = new Nonterm("S");
    private RHS[][] q = null;
    private ParseTree tree = null;
    private HashMap<String, Rules> grammar_list = new HashMap<>();

    public GrammarInterpreter (ParseTree parse_tree) {
        super(parse_tree);
        tree = parse_tree;
        interpretTree();
        checkForUndefinedNonterms();

        System.out.println("TERMS: " + terms);
        System.out.println("NONTERMS: " + nonterms);
        System.out.println("AXIOM: " + axiom);
        System.out.println("GRAMMAR: " + grammar_list + "\n");
    }

    public CompilerGenerator getCompilerGenerator() {
        return new CompilerGenerator(terms, nonterms, axiom, grammar_list);
    }

    private void addNonterm(Token token) {
        if (nonterms.contains(token.getImage())) {
            System.out.println("*** Nonterminal <" + token.getImage() + "> " +
                    "defined twice at " + token.coordsToString() + " ***");
            System.exit(1);
        }
        nonterms.add(token.getImage());
        grammar_list.put(token.getImage(), new Rules());
    }

    private void addTerm(Token token) {
        token.setImage(token.getImage().replaceAll("[\\s']+", ""));
        if (terms.contains(token.getImage())) {
            System.out.println("*** Terminal <" + token.getImage() + "> " +
                    "defined twice at " + token.coordsToString() + " ***");
            System.exit(1);
        }
        terms.add(token.getImage());
//        grammar_list.put(token.getImage(), new Rules());
    }

    private void checkForUndefinedNonterms() {
        boolean error = false;
        for (Map.Entry<String, Rules> entry: grammar_list.entrySet()) {
            Rules rule = entry.getValue();
            if (rule.isEmpty()) {
                System.out.println("*** No rules found for nonterminal <" + entry.getKey() + "> ***");
                error = true;
            }
            for (RHS chunk: rule) {
                for (Symbol symbol: chunk) {
                    if (symbol instanceof Nonterm && !nonterms.contains(symbol.getType())) {
                        System.out.println("*** Undefined nonterminal <" + symbol.getType() + "> " +
                                "at " + symbol.coordsToString() + " ***");
                        error = true;
                    }
                }
            }
        }
        if (error) {
            System.exit(2);
        }
    }

//  DEF ::= D_N D_T
    private void scanDef(ParseNode def){
        scanDN((ParseNode)def.getChildAt(0));
        scanDT((ParseNode)def.getChildAt(1));
    }

//  D_N ::= "non-terminal" N N_LST ';'
    private void scanDN(ParseNode dn){
        Symbol symbol = ((ParseNode)dn.getChildAt(1)).getSymbol();
        addNonterm((Token)symbol);
        scanNList((ParseNode)dn.getChildAt(2));
    }

//  N_LST ::= ',' N N_LST | eps
    private void scanNList(ParseNode nonterm){
        while(!nonterm.isLeaf()){
            Symbol symbol = ((ParseNode)nonterm.getChildAt(1)).getSymbol();
            addNonterm((Token)symbol);
            nonterm = (ParseNode)nonterm.getChildAt(2);
        }
    }

//  D_T ::= "terminal" T T_LST ';'
    private void scanDT(ParseNode dt){
        Symbol symbol = ((ParseNode)dt.getChildAt(1)).getSymbol();
        addTerm((Token)symbol);
        scanTList((ParseNode)dt.getChildAt(2));
    }

//  T_LST ::= ',' T T_LST | eps
    private void scanTList(ParseNode term){
        while (!term.isLeaf()){
            Symbol symbol = ((ParseNode)term.getChildAt(1)).getSymbol();
            addTerm((Token)symbol);
            term = (ParseNode)term.getChildAt(2);
        }
    }

//  R_LST ::= R R_LST | eps
    private void scanRList(ParseNode rule){
        while(!rule.isLeaf()){
            scanR((ParseNode)rule.getChildAt(0));
            rule = (ParseNode)rule.getChildAt(1);
        }
    }

//  R ::= N "::=" E E_LST ';'
    private void scanR(ParseNode rule){
        String N = ((Token)(rule.getSymbolAt(0))).getImage();
        if (grammar_list.containsKey(N)) {
            RHS E = scanE((ParseNode)rule.getChildAt(2));
            Rules Elst = scanEList((ParseNode)rule.getChildAt(3));
            Rules rules = new Rules(E);
            rules.addAll(Elst);
            Rules union_rules_list = grammar_list.get(N);
            union_rules_list.addAll(rules);
            grammar_list.put(N, union_rules_list);
        }else {
            Token tok = (Token)(rule.getSymbolAt(0));
            System.out.println("*** A rule for undefined nonterminal <" + tok.getImage() + "> "+
                    "at " + tok.coordsToString() + " ***");
        }
    }

//  E_LST ::= '|' E E_LST | eps
    private Rules scanEList(ParseNode expr){
        Rules rules = new Rules();
        while (!expr.isLeaf()) {
            rules.add(scanE((ParseNode) expr.getChildAt(1)));
            expr = (ParseNode) expr.getChildAt(2);
        }
        return rules;
    }

//  E ::= SYM SYM_LST | "epsilon"
    private RHS scanE(ParseNode rule){
        if (rule.getChildCount() == 1) {
            RHS res = new RHS(RHS.EPSILON);
            res.setCoords(rule.getSymbolAt(0).getStart());
            return res;
        } else {
            Symbol symbol = scanSym((ParseNode)rule.getChildAt(0));
            RHS res = new RHS(symbol);
            res.setCoords(symbol.getStart());
            res.addAll(scanSymList((ParseNode)rule.getChildAt(1)));
            return res;
        }
    }

//  SYM_LST ::= SYM SYM_LST | eps
    private RHS scanSymList(ParseNode node){
        RHS res = new RHS();
        while (!node.isLeaf()) {
            Symbol sym = scanSym((ParseNode)node.getChildAt(0));
            res.add(sym);
            node = (ParseNode)node.getChildAt(1);
        }
        return res;
    }

//  SYM ::= N | T
    private Symbol scanSym(ParseNode node){
        Token sym = (Token)node.getSymbolAt(0);
        String image = sym.getImage();
//        System.out.println(sym.toString());
        if (sym.getType().equals(GrammarScanner.TERMINAL)) {
            image = image.replaceAll("[\\s']+", "");
            if (image.isEmpty()) image = " "; //Видимо, терминалом был пробельный символ
            return new Term(image, sym.getStart(), sym.getFollow());
        } else {
            return new Nonterm(image, sym.getStart(), sym.getFollow());
        }
    }

//  AXIOM ::= "axiom" N ';'
    private void scanAxiom(ParseNode axiom){
        ParseNode axiom_name = (ParseNode)axiom.getChildAt(1);
        Token symbol = (Token)axiom_name.getSymbol();
        this.axiom = new Nonterm(symbol.getImage());
    }

    // S ::= DEF R_LST AXIOM
    private void interpretS(ParseNode root) {
        scanDef((ParseNode)root.getChildAt(0));
        scanRList((ParseNode)root.getChildAt(1));
        scanAxiom((ParseNode)root.getChildAt(2));
    }

    private void interpretTree() {
        interpretS((ParseNode)tree.getRoot());
    }
}
