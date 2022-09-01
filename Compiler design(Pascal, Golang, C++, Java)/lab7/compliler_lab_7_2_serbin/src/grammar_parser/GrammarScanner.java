package grammar_parser;

import lex_analyze.Coords;
import lex_analyze.Scanner;
import lex_analyze.Token;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class GrammarScanner extends Scanner {

    public final static HashMap<String, String> reg_expressions = staticRegExpressions();
    public final static String NONTERMINAL = "N";
    public final static String TERMINAL = "T";

    private static HashMap<String, String> staticRegExpressions() {
        LinkedHashMap<String, String> exprs = new LinkedHashMap<>();
        exprs.put("COMMA", ",");
        exprs.put("ORSIGN", "\\|");
        exprs.put("COLON", ";");
        exprs.put("EQSIGN", "::=");
        exprs.put("NONTERMINALSIGN", "non-terminal");
        exprs.put("TERMINALSIGN", "terminal");
        exprs.put("AXIOMSIGN", "axiom");
        exprs.put("EPSILON", "epsilon");
        exprs.put(NONTERMINAL, "[A-Z]+1?");
        exprs.put(TERMINAL, "(\'[^\\s\']*\'|[a-z]|[0-9]*)");
        return exprs;
    }
    public GrammarScanner(String filepath) {
        super(filepath, reg_expressions);
    }
    @Override
    protected Token returnToken (String type) {
        Coords last = coord;
        coord = coord.shift(image.length());
        log.append(type).append(' ').append(last.toString()).append('-').append(coord.toString())
                .append(": <").append(image).append(">\n");
        if (type.equals(TERMINAL) || type.equals(NONTERMINAL)) {
            return new Token(type, image, last, coord);
        }
        return new Token(image, image, last, coord);
    }
}
