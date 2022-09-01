package lex_analyze;


public class Coords {
    private final int row;
    private int col;
    private int pos;

    public int getPos() {
        return pos;
    }

    public void setPos() {
        ++pos;
        ++col;
    }
    private Coords(int row, int col, int pos) {
        this.row = row;
        this.col = col;
        this.pos = pos;
    }

    public static Coords undefined() {
        return new Coords(-1, -1, -1);
    }

    public static Coords start() {
        return new Coords(1, 1, 0);
    }

    public Coords shift(int positions) {
        return new Coords(row, col + positions, pos + positions);
    }

    public Coords newline() {
        return new Coords(row + 1, 1, pos + 1);
    }

    @Override
    public String toString() {
        return pos > -1 ? String.format("(%d, %d)", row, col) : "?";
    }
}
