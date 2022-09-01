package parser;

public class ParserTags {
    public static final int FAIL = -1;
    public static final int IGNORE = 0;
    public static final int SUCCESS = 1;
    public enum PARSE_MODE {
        INITIAL,
        CALCULATE,
        SKIP
    }
}
