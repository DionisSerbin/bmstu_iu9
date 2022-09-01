package parser;

import java.util.Objects;

public class Epsilon extends Terminal {

    public Epsilon() {
        super("eps");
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Epsilon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
