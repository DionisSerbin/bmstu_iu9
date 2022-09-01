import java.util.ArrayList;

class Symbol {

    protected String name;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals (Object o) {
        return ((o instanceof Symbol) && name.equals(((Symbol)o).name))
                || ((o instanceof String) && name.equals(o));
    }

    public String toDot() {
        return "";
    }
}

class Term extends Symbol {
    final static String EOF = "$";

    public Term(String type) {
        super.name = type;
    }

    public String toDot() {
        return String.format("[label=\"%s\"][color=black]\n", name);
    }

//   public static ArrayList<Term> asTermList(String ... names) {
//        ArrayList<Term> list = new ArrayList<>();
//        for(String name: names) {
//            list.add(new Term(name));
//        }
//        return list;
//    }

}