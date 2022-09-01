package token;

import lexer.Position;

public class OperatorToken extends Token {

    public OperatorToken(String attribute, Position start, Position end) {
        super(TokenDomainTags.TOKEN_TAG.END, start, end);
        //'+', '-', '*', '/', '(', ')', n, 'non-terminal', 'terminal', ';', ',', '|', '::=', '?'
        switch (attribute) {
            case ";":
                this.tag = TokenDomainTags.TOKEN_TAG.DOT;
                break;
            case "|":
                this.tag = TokenDomainTags.TOKEN_TAG.DELIMITER;
                break;
            case "::=":
                this.tag = TokenDomainTags.TOKEN_TAG.EQ;
                break;
            case "?":
                this.tag = TokenDomainTags.TOKEN_TAG.QUESTION;
                break;
            case "*":
                this.tag = TokenDomainTags.TOKEN_TAG.STAR;
                break;
            case "+":
                this.tag = TokenDomainTags.TOKEN_TAG.PLUS;
                break;
            case "(":
                this.tag = TokenDomainTags.TOKEN_TAG.BR_OPEN;
                break;
            case ")":
                this.tag = TokenDomainTags.TOKEN_TAG.BR_CLOSE;
                break;
            case "non-terminal":
                this.tag = TokenDomainTags.TOKEN_TAG.NONTERMINALSIGN;
                break;
            case "terminal":
                this.tag = TokenDomainTags.TOKEN_TAG.TERMINALSIGN;
                break;
            case ",":
                this.tag = TokenDomainTags.TOKEN_TAG.COMMA;
        }
        this.setAttribute(attribute);
    }

    @Override
    protected void setAttribute(String token) {
        super.setAttribute(token);
        this.attribute = token;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + attribute;
    }

}