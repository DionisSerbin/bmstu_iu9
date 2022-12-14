public abstract class Token {
    private DomainTag tag;
    private Fragment coords;
    private String image;

    Token(DomainTag tag, String image, Position starting, Position following) {
        this.tag = tag;
        this.coords = new Fragment(starting, following);
        this.image = image;
    }

    public DomainTag getTag(){
        return this.tag;
    }

    @Override
    public String toString() {
        return tag.toString() + " " + coords.toString() + ": " + image;
    }
}
