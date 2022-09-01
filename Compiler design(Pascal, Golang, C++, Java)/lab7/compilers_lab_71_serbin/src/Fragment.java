public class Fragment {
    private String image;
    private Position start, follow;

    public Fragment(String image, Position start, Position follow) {
        this.image = image;
        this.start = start;
        this.follow = follow;
    }

    public Fragment(Position start) {
        this.image = "";
        this.start = start;
        this.follow = start;
    }

    @Override
    public String toString(){
        return String.format("COMMENT %s-%s %s",
                start.toString(), follow.toString(), image);
    }
}
