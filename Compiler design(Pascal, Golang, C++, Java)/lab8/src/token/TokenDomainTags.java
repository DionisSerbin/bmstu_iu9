package token;

public class TokenDomainTags {
    public static final String END_TOKEN = "END";
    public enum TOKEN_TAG {
        NONTERMINAL,
        TERMINAL,
        EQ,
        DOT,
        COMMA,
        DELIMITER,
        STAR,
        QUESTION,
        PLUS,
        BR_OPEN,
        BR_CLOSE,
        END,
        NONTERMINALSIGN,
        TERMINALSIGN
    }
}
