public enum DomainTag {
    IDENT(0),
    KEYWORD_WHILE(1),
    KEYWORD_END(2),
    KEYWORD_DO(3),
    EOP(4);

    private final int val;

    public int getVal() {
        return this.val;
    }

    DomainTag(int val) {
        this.val = val;
    }



}

