import grammar_parser.CompilerGenerator;
import grammar_parser.GrammarInterpreter;
import grammar_parser.GrammarScanner;
import grammar_parser.GrammarStructure;
import lex_analyze.Scanner;
import syntax_analyze.Parser;

import java.io.File;

public class SelfMain {
    public static void main(String[] args) {
        String grammar_src = args[0];
        Scanner scanner = new GrammarScanner(grammar_src);
        Parser parser = GrammarStructure.getParser();
        parser.topDownParse(scanner);
        parser.addFile("parse_output" + File.separator + "parse_graph.dot");

        GrammarInterpreter gr = new GrammarInterpreter(parser.getParseTree());
        CompilerGenerator cg = gr.getCompilerGenerator();
        cg.calculateJava("parse_output/GrammarStructure.java");
    }
}
