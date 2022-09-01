public class Token {
    private DomainTag tag;
    private Fragment coords;
    private String image;
    private String parse;

    Token(DomainTag tag, String image, String parse, Position starting, Position following) {
        this.tag = tag;
        this.coords = new Fragment(starting, following);
        this.image = image;
        this.parse = parse;
    }

    public DomainTag getTag(){
        return this.tag;
    }

    @Override
    public String toString() {
        return tag.toString() + " " + coords.toString() + ": " + image + "\n" + parse;
    }
}