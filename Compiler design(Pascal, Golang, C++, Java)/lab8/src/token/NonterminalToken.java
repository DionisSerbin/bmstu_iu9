package token;

import lexer.Position;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NonterminalToken extends Token {

    private String regex = "[A-Z]+1?";

    public NonterminalToken(String number, Position start, Position end) {
        super(TokenDomainTags.TOKEN_TAG.NONTERMINAL, start, end);
        this.setAttribute(number);
    }

    @Override
    protected void setAttribute(String token) {
        super.setAttribute(token);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(token);
        if (matcher.find()) {
            token = matcher.group();
            this.attribute = token;
        } else {
            System.out.println("NONTERM ERROR");
        }
    }

    @Override
    public String toString() {
        return super.toString() + ": " + attribute;
    }

}