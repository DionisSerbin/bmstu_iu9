package parser;

import java.util.Objects;

public class Nonterminal implements Comparable<Nonterminal>{
    private String name;

    public Nonterminal(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Nonterminal n) {
        return this.name.compareTo(n.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nonterminal)) return false;
        Nonterminal n = (Nonterminal) o;
        return getName().equals(n.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
