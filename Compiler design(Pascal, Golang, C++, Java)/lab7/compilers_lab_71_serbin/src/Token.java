public class Token extends Term {
    private String image;
    private Position start, follow;

    public Token(String type, String image, Position start, Position follow) {
        super(type);
        this.image = image;
        this.start = start;
        this.follow = follow;
    }

    public Token(String type, Position start) {
        super(type);
        this.image = "";
        this.start = start;
        this.follow = start;
    }

    public Token(String type) {
        super(type);
        this.image  = "";
        this.start  = Position.undefined();
        this.follow = Position.undefined();
    }

    @Override
    public String toString(){
        return String.format("Token %s %s-%s <%s>",
                super.toString(), start.toString(), follow.toString(), image);
    }

    @Override
    public String toDot() {
        return String.format("[label=\"%s\"][color=red]\n", toString().replaceAll("\"", "\\\\\""));
    }
}