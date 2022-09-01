package calculator;

import lex_analyze.Coords;
import lex_analyze.Scanner;
import lex_analyze.Token;
import syntax_analyze.symbols.Term;

public class ArithmeticScanner extends Scanner {
    public ArithmeticScanner(String filepath) {
        super(ArithmeticStructure.staticRegExpressions(), filepath);
    }
    @Override
    protected Token returnToken (String type) {
        Coords last = coord;
        coord = coord.shift(image.length());
        log.append(type).append(' ').append(last.toString()).append('-').append(coord.toString())
                .append(": <").append(image).append(">\n");
        if (image.matches("[0-9]+")){
            return new Token(type, image, last, coord);
        }
        return new Token(image, image, last, coord);
    }

    @Override
    public Token nextToken() {
        if (coord.getPos() >= text.length()) {
            return new Token(Term.EOF, coord);
        }
        String image;
        if (m.find()) {
            if (m.start() != coord.getPos()) {
                log.append(String.format("SYNTAX ERROR: %d",
                        coord.getPos())).append(coord.toString()).append('\n');
                System.out.println(String.format("SYNTAX ERROR: %d",
                        coord.getPos()) + coord.toString());
                System.exit(-1);
            }
            if ((image = m.group(BLANK)) != null) {
                coord = coord.shift(image.length());
                return nextToken();
            }
            if ((image = m.group(NEWLINE)) != null) {
                coord = coord.newline();
                coord = coord.shift(image.length() - 1);
                return nextToken();
            }
            for (String s: regexp.keySet()) {
                if (isType(s)) {
                    return returnToken(s);
                }
            }

            System.out.println("ERROR " + coord.toString() + " " + text.substring(coord.getPos()));
            return nextToken();

        } else {
            log.append("SYNTAX ERROR: ").append(coord.toString()).append('\n');
            log.append("SYNTAX ERROR: ").append(coord.toString()).append('\n');
            System.out.println("SYNTAX ERROR: " + coord.toString());
            return new Token(Term.EOF, coord);
        }
    }
}
