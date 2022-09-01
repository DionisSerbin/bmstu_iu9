package lexer;

public class Message {
    private String text;
    private boolean isError;
    private Position position;

    public Message(String text, boolean isError, Position position) {
        this.text = text;
        this.isError = isError;
        this.position = position;
    }

    @Override
    public String toString() {
        if (isError) return "ERROR " + text + " " + position.toString();
        else return "WARN " + text + " " + position.toString();
    }
}
