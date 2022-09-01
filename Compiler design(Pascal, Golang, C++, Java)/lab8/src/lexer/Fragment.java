package lexer;

public class Fragment {
    private Position start;
    private Position end;

    public Fragment(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return start.toString() + "-" + end.toString();
    }
}
