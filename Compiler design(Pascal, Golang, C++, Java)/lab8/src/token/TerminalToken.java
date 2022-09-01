package token;

import lexer.Position;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TerminalToken extends Token {

    private String regex = "(\'[^\\s\']*\'|[a-z]*)";

    public TerminalToken(String number, Position start, Position end) {
        super(TokenDomainTags.TOKEN_TAG.TERMINAL, start, end);
        this.setAttribute(number);
    }

    @Override
    protected void setAttribute(String token) {
        super.setAttribute(token);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(token);
        if (matcher.find()) {
            this.attribute = token;
        } else {
            System.out.println("TERM ERROR");
        }
    }

    @Override
    public String toString() {
        return super.toString() + ": " + attribute;
    }

}