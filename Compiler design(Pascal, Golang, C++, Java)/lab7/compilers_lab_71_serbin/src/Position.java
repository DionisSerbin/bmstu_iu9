public class Position {
    private final int row, column, pos;

    public int getPos() {
        return pos;
    }

    private Position(int row, int column, int pos) {
        this.row = row;
        this.column = column;
        this.pos = pos;
    }

    public static Position undefined() {
        return new Position(-1, -1, -1);
    }

    public static Position start() {
        return new Position(1, 1, 0);
    }

    public Position shift(int positions) {
        return new Position(row, column + positions, pos + positions);
    }

    public Position newline() {
        return new Position(row + 1, 1, pos + 1);
    }

    @Override
    public String toString() {
        return pos > -1 ? String.format("(%d, %d)", row, column) : "?";
    }
}
