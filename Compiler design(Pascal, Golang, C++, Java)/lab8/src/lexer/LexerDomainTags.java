package lexer;

import java.util.ArrayList;
import java.util.Arrays;

public class LexerDomainTags {
    public static final ArrayList<Character> symbols= new ArrayList<>(
            Arrays.asList('!', '\"', '#', '$', '%', '&', '-',
                    '/', ':', ';', '<', '>', '@', '^',
                    '_', '`', '{', '}', '~'));
    public static final char openBracket = '(';
    public static final char closeBracket = ')';
    public static final char openBracketSquare = '[';
    public static final char closeBracketSquare = ']';
    public static final String terminalDigit = "[a-z]";
    public static final char delimiter = '|';
    public static final char dot = ';';
    public static final char comma = ',';
    public static final char escape = '\'';
    public static final String equals = "::=";
    public static final String nonTerminal = "non-terminal";
    public static final String terminal = "terminal";
    public static final char plus = '+';
    public static final char question = '?';
    public static final char star = '*';
}
