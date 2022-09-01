package parser;

import java.util.ArrayList;

public class Rule implements Comparable<Rule> {
    private Nonterminal nonterminal;
    private ArrayList<Terminal> FIRST = null;
    private int oldFirstSize = 0;
    private int newFirstSize = 0;
    private boolean changedFirst = false;
    private boolean hasEps = false;


    public Rule(Nonterminal n) {
        this.nonterminal = n;
        FIRST = new ArrayList<>();
    }

    public boolean isEmptyFirst() {
        return newFirstSize == 0;
    }

    public String getName() {
        return nonterminal.getName();
    }

    public void setNonterminal(Nonterminal nonterminal) {
        this.nonterminal = nonterminal;
    }

    public Nonterminal getNonterminal() {
        return nonterminal;
    }

    public boolean isChangedFirst() {
        return changedFirst;
    }

    private void setNewFirstSize() {
        this.newFirstSize = this.FIRST.size();
        this.changedFirst = this.newFirstSize - this.oldFirstSize > 0;
    }

    public ArrayList<Terminal> getFIRST() {
        return FIRST;
    }

    public ArrayList<Terminal> getFIRSTwoEps() {
        ArrayList <Terminal> cleanFirst = new ArrayList<>();
        for(Terminal t: FIRST) {
            if (!(t instanceof Epsilon)) {
                cleanFirst.add(t);
            }
        }
        return cleanFirst;
    }

    public void removeEps() {
        this.setOldFirstSize();
        if (hasEps) {
            FIRST.removeIf(t -> (t instanceof Epsilon));
        }
        hasEps = false;
        this.setNewFirstSize();
    }

    private void setOldFirstSize() {
        this.oldFirstSize = this.FIRST.size();
    }

    public void addToFirst(Terminal terminal) {
        this.setOldFirstSize();
        if (!this.FIRST.contains(terminal)) {
            this.FIRST.add(terminal);
        }
        if (terminal instanceof Epsilon) {
            hasEps = true;
        }
        this.setNewFirstSize();
    }

    public boolean hasEps() {
        return hasEps;
    }

    public void addToFirst(ArrayList<Terminal> first) {
        this.setOldFirstSize();
        for (Terminal term: first) {
            this.addToFirst(term);
        }
        this.setNewFirstSize();
    }

    public void setChangedFirst(Boolean b){
        changedFirst = b;
    }

    @Override
    public int compareTo(Rule r) {
        return this.nonterminal.getName().compareTo(r.nonterminal.getName());
    }

    public void printFirst() {
        System.out.print(this.nonterminal.getName() + ": ");
        System.out.println(this.FIRST);
    }
}
