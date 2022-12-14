package syntax_analyze.symbols;

import grammar_parser.Compilable;
import lex_analyze.Coords;

public class Symbol implements Compilable{

    protected String type;

    protected Coords start, follow;

    public String getType() {
        return type;
    }

    protected Symbol(String type)
    {
        this.type = type;
        this.start  = Coords.undefined();
        this.follow = Coords.undefined();
    }

    protected Symbol (String type, Coords start, Coords follow)
    {
        this.type = type;
        this.start  = start;
        this.follow = follow;
    }

    @Override
    public String toString() {
        return type;
    }

    @Override
    public boolean equals (Object o) {
        return ((o instanceof Symbol) && type.equals(((Symbol)o).type))
                || ((o instanceof String) && type.equals(o));
    }

    public String toDot() {
        return "";
    }

    public String coordsToString() {
        return start.toString() + "-" + follow.toString();
    }

    public Coords getStart() {
        return start;
    }

    public Coords getFollow() {
        return follow;
    }

    public String printConstructor() {
        return "*** Error in Symbol.printConstructor(): no public constructor";
    }

}
