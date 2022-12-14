package syntax_analyze;

import lex_analyze.Scanner;
import lex_analyze.Token;
import syntax_analyze.rules.RHS;
import syntax_analyze.symbols.Nonterm;
import syntax_analyze.symbols.Symbol;
import syntax_analyze.symbols.Term;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Stack;

public class Parser {

    protected ArrayList<String> terms;//    = grammar_parser.GrammarStructure.terms;
    protected ArrayList<String> nonterms;// = grammar_parser.GrammarStructure.nonterms;
    protected Nonterm axiom;//    = grammar_parser.GrammarStructure.axiom;
    protected RHS[][] q;// = grammar_parser.GrammarStructure.q;
    private ParseTree parse_tree = null;// = new syntax_analyze.ParseTree(axiom);
    protected StringBuilder log = new StringBuilder();

    public Parser() {
    }

    public Parser(Parser p) {
        this.terms    = p.terms;
        this.nonterms = p.nonterms;
        this.axiom    = p.axiom;
        this.q = p.q;

    }

    public Parser(ArrayList<String> terms, ArrayList<String> nonterms, Nonterm axiom, RHS[][] q) {
        this.terms    = terms;
        this.nonterms = nonterms;
        this.axiom    = axiom;
        this.q = q;
    }

    public String getLog() {
        return log.toString();
    }

    private RHS delta(Nonterm N, Token T) {
        int i = nonterms.indexOf(N.getType());
        int j = terms.indexOf(T.getType());
        if (i == -1) {
            System.out.println("[Parser.java/delta]: no nontem " + N.toString() + " is found in " + nonterms);
        }
        if (j == -1) {
            System.out.println("[Parser.java/delta]: no term " + T.getType() + " is found in " + terms);
        }
        return q[i][j];
    }

    private void printError(Token tok, Symbol expected) {
        System.out.println("***ERROR: " + expected.toString() + " expected, got: " + tok.toString());
    }

    public ParseTree topDownParse(Scanner scanner) {
        log.setLength(0);
        Stack<Symbol> stack = new Stack<>();
        stack.push(new Term(Term.EOF));
        stack.push(axiom);
        parse_tree = new ParseTree(axiom);
        Token tok = scanner.nextToken();
        do {
            log.append(stack.toString()).append("-----------").append(tok.toString()).append('\n');
            Symbol X = stack.pop();
            if (X instanceof Term) {
                if (X.equals(tok)) {
                    parse_tree.setToken(tok);
                    tok = scanner.nextToken();
                } else {
                    printError(tok, X);
                    return parse_tree;
                }
            } else {
                RHS nextRule = delta((Nonterm)X, tok);
                if (RHS.isError(nextRule)) {
                    printError(tok, X);
                    return parse_tree;
                } else {
                    stack.addAll(nextRule.reverse());
                    parse_tree.add(nextRule);
                }
            }
        } while (!stack.empty());
        return parse_tree;
    }

    public ParseTree getParseTree() {
        return parse_tree;
    }

    public void addFile(String path) {
        File dotfile = new File(path);
        try {
            Files.write(dotfile.toPath(), parse_tree.toDot().getBytes());
        } catch (IOException e) {
            System.err.printf("file %s cannot be read\n", dotfile.toPath());
        }
    }

}