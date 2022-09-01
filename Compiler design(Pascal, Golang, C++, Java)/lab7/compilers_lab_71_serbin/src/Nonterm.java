class Nonterm extends Symbol {

    public Nonterm(String type) {
        super.name = type;
    }

    @Override
    public String toString() {
        return "<" + super.toString() + ">";
    }

    public String toDot() {
        return String.format("[label=\"%s\"][color=black]\n", name);
    }

}
