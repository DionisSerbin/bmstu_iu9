import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Stack;

public class Parser {

    private ArrayList<String> terms;
    private ArrayList<String> nonterms;
    private Nonterm axiom;
    private RHS[][] table;
    private ParseTree parseTree = null;

    public Parser(ArrayList<String> terms, ArrayList<String> nonterms, Nonterm axiom, RHS[][] table) {
        this.terms    = terms;
        this.nonterms = nonterms;
        this.axiom    = axiom;
        this.table = table;
    }

    private RHS delta(Nonterm N, Term T) {
        return table[nonterms.indexOf(N.name)][terms.indexOf(T.name)];
    }

    private void printError(Token tok, Symbol expected) {
        System.out.println("ERROR: " + expected.toString() + " expected, got: " + tok.toString());
    }

    public ParseTree topDownParser(Scanner scanner) {
        Stack<Symbol> stack = new Stack<>();
        stack.push(new Term(Term.EOF));
        stack.push(axiom);
        parseTree = new ParseTree(axiom);
        Token tok = scanner.nextToken();
        do {
//            System.out.print(stack);
//            System.out.println("=====>" + tok);
            Symbol X = stack.pop();
            if (X instanceof Term) {
                if (X.equals(tok)) {
                    parseTree.setToken(tok);
                    tok = scanner.nextToken();
                } else {
                    printError(tok, X);
                    return parseTree;
                }
            } else {
                RHS nextRule = delta((Nonterm)X, tok);
                if (nextRule instanceof Error) {
                    printError(tok, X);
                    return parseTree;
                } else {
                    stack.addAll(nextRule.reverse());
                    parseTree.add(nextRule);
                }
            }
        } while (!stack.empty());
        return parseTree;
    }

    public ParseTree getParseTree() {
        return parseTree;
    }

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(args[0], GrammarStructure.tagsRegex);
        Parser parser = new GrammarParser();
        parser.topDownParser(sc);
        File dotfile = new File("graph.dot");
        Files.write(dotfile.toPath(), parser.getParseTree().toDot().getBytes());

        System.out.println(parser.getParseTree().toDot());
        for (Token tok = sc.nextToken(); !tok.name.equals(Term.EOF); tok = sc.nextToken()) {
            System.out.println(tok.toString());
        }

        System.out.println("COMMENTS");
        for(Fragment comm : sc.getComments()){
            System.out.print(comm.toString());
        }

    }
}