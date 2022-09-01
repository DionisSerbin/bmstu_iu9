import calculator.*;
import grammar_parser.CompilerGenerator;
import grammar_parser.GrammarInterpreter;
import grammar_parser.GrammarScanner;
import grammar_parser.GrammarStructure;
import lex_analyze.Scanner;
import syntax_analyze.Parser;

import java.io.File;

public class CalcMain {
    public static void main(String[] args) {
        String arithm_grammar_src = args[0];
        String expr_src = args[1];

//        Scanner scanner = new GrammarScanner(arithm_grammar_src);
//        Parser parser  = GrammarStructure.getParser();
//        parser.topDownParse(scanner);
//        parser.addFile("parse_output2" + File.separator + "arithm_parse_graph.dot");
//        GrammarInterpreter gr = new GrammarInterpreter(parser.getParseTree());
//        CompilerGenerator cg = gr.getCompilerGenerator();
//        cg.toJava("parse_output2/GrammarStructure.java");



        Parser parser = ArithmeticStructure.getParser();
        Scanner scanner = new ArithmeticScanner(expr_src);
        parser.topDownParse(scanner);

        parser.addFile("parse_output2" + File.separator + "expr_parse_graph.dot");
        ArithmeticInterpreter evaluator = new ArithmeticInterpreter(parser.getParseTree());
        String expression = scanner.getText();
        System.out.println(expression.replaceAll("\n", "") + " = " + evaluator.getResult());
    }
}
