import lexer.Lexer;
import parser.Parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) throws IOException {
        String fileName = args[0];
        Scanner scanner = new Scanner(Paths.get(fileName), StandardCharsets.UTF_8.name());
        String contents = scanner.useDelimiter("\\A").next();
        scanner.close();
        Lexer lexer = new Lexer();
        lexer.analyze(contents);
        lexer.printTokens();
        lexer.printMessages();

        Parser parser = new Parser();
        parser.setChain(lexer.getTokens());
        parser.calculateFIRST();
    }
}
