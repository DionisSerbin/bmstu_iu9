package calculator;

import grammar_parser.CompilerGenerator;
import lex_analyze.Token;
import semantic_analize.Interpreter;
import syntax_analyze.ParseNode;
import syntax_analyze.ParseTree;

public class ArithmeticInterpreter extends Interpreter {
    private int result;
    private ParseTree tree = null;

    public ArithmeticInterpreter(ParseTree parseTree)
    {
        super(parseTree);
        this.tree = parseTree;
        interpretTree();
    }

    public int getResult() {
        return result;
    }

    private void interpretTree() {
        result = scanE((ParseNode)tree.getRoot());
    }

    // E ::= T E1;
    private int scanE(ParseNode root) {
        return
        scanT ((ParseNode)root.getChildAt(0))
        +
        scanE1((ParseNode)root.getChildAt(1));
    }

    //E1 ::= '+' T E1 | epsilon;
    private int scanE1(ParseNode node) {
        int res = 0;
        while (node.getChildCount() == 3) {
            res += scanT((ParseNode)node.getChildAt(1));
            node = (ParseNode)node.getChildAt(2);
        }
        return res;
    }

    //T ::= F T1;
    private int scanT(ParseNode node) {
        return scanF ((ParseNode)node.getChildAt(0))
                * scanT1((ParseNode)node.getChildAt(1));
    }

    //T1 ::= '*' F T1 | epsilon;
    private int scanT1(ParseNode node) {
        int res = 1;
        while (node.getChildCount() == 3) {
            res *= scanF((ParseNode)node.getChildAt(1));
            node = (ParseNode)node.getChildAt(2);
        }
        return res;
    }

    //F ::= n | '(' E ')';
    private int scanF(ParseNode node) {
        if (node.getChildCount() == 3) {
            return scanE((ParseNode)node.getChildAt(1));
        } else {
            Token tok = (Token)node.getSymbolAt(0);
            return Integer.parseInt(tok.getImage());
        }
    }

}
