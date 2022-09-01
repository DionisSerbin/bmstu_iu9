package lexer;

public class Position implements Cloneable {
    private String file;
    private int fileLength;
    private int index = 0;
    private int line = 1;
    private int position = 1;

    public Position(String file) {
        this.file = file;
        this.fileLength = file.length();
    }

    public Position(Position pos) {
        this.file = pos.getFile();
        this.fileLength = this.file.length();
        this.index = pos.getIndex();
        this.line = pos.getLine();
        this.position = pos.getPosition();

    }
    public String getFile() {
        return this.file;
    }
    public int getIndex() {
        return this.index;
    }
    public int getLine() {
        return this.line;
    }
    public int getPosition() {
        return this.position;
    }

    public boolean isNewLine() {
        if (this.index == this.fileLength) {
            return true;
        }
        if (this.file.charAt(this.index) == '\r' && this.index+1 < this.fileLength) {
            return (this.file.charAt(this.index+1) == '\n');
        }
        return (this.file.charAt(this.index) == '\n');
    }

    public int getCurrentPosition() {
        if (this.index == this.fileLength) {
            return -1;
        }
        return this.file.charAt(this.index);
    }

    public boolean isWhiteSpace() {
        return (this.index != this.fileLength && Character.isWhitespace(this.file.charAt(this.index)));
    }

    public boolean isDigit() {
        return (this.index != this.fileLength && Character.isDigit(this.file.charAt(this.index)));
    }

    public boolean isSymbol() {
        return (this.index != this.fileLength && LexerDomainTags.symbols.contains(this.file.charAt(this.index)));
    }

    public boolean isKeySymbol() {
        if (this.index != this.fileLength) {
            char value = this.file.charAt(this.index);
            return value == LexerDomainTags.delimiter || value == LexerDomainTags.dot
                    || value == LexerDomainTags.comma || value == LexerDomainTags.closeBracketSquare
                    || value == LexerDomainTags.plus || value == LexerDomainTags.openBracketSquare
                    || value == LexerDomainTags.closeBracket || value == LexerDomainTags.openBracket
                    || value == LexerDomainTags.question || value == LexerDomainTags.star;
        }
        return false;
    }

    public boolean isDot() {
        return (this.index != this.fileLength && this.file.charAt(this.index) == LexerDomainTags.dot);
    }


    public boolean isEquals(int start, int end){
        return (this.index != this.fileLength && this.file.substring(start, end).equals(LexerDomainTags.equals));
    }

    public boolean isTerminalSign(int start, int end){
        return (this.index != this.fileLength && this.file.substring(start, end).equals(LexerDomainTags.terminal));
    }

    public boolean isNonTerminalSign(int start, int end){
        return (this.index != this.fileLength && this.file.substring(start, end).equals(LexerDomainTags.nonTerminal));
    }

    public boolean isTerminalDigit(int start, int end) {
        return (this.index != this.fileLength && this.file.substring(start, end).matches(LexerDomainTags.terminalDigit));
    }

    public boolean isColon(){
        return (this.index != this.fileLength && this.file.charAt(this.index) == ':');
    }

    public boolean isDelimiter() {
        return (this.index != this.fileLength && this.file.charAt(this.index) == LexerDomainTags.delimiter);
    }

    public boolean isCloseBracket() {
        return (this.index != this.fileLength && this.file.charAt(this.index) == LexerDomainTags.closeBracket);
    }

    public boolean isOpenBracket() {
        return (this.index != this.fileLength && this.file.charAt(this.index) == LexerDomainTags.openBracket);
    }

    public boolean isPlus() {
        return (this.index != this.fileLength && this.file.charAt(this.index) == LexerDomainTags.plus);
    }

    public boolean isQuestion() {
        return (this.index != this.fileLength && this.file.charAt(this.index) == LexerDomainTags.question);
    }

    public boolean isStar() {
        return (this.index != this.fileLength && this.file.charAt(this.index) == LexerDomainTags.star);
    }

    public boolean isEscapeChar() {
        return (this.index != this.fileLength && this.file.charAt(this.index) == LexerDomainTags.escape);
    }

    public boolean isLetter() {
        return (this.index != this.fileLength && Character.isLetter(this.file.charAt(this.index)));
    }

    public boolean isComma(){
        return (this.index != this.fileLength && this.file.charAt(this.index) == LexerDomainTags.comma);
    }

    public boolean isLetterOrDigit() {
        return this.isDigit() || this.isLetter();
    }

    public boolean isCapLetterOrDigit(){
        return this.isDigit() || this.isCapLetter();
    }


    public boolean isCapLetter() {
        if (this.index != this.fileLength) {
            char value = this.file.charAt(this.index);
            return  ((value >= 'A' && value <= 'Z'));
        }
        return false;
    }

    public boolean isSmallLetter() {
        if (this.index != this.fileLength) {
            char value = this.file.charAt(this.index);
            return  ((value >= 'a' && value <= 'z'));
        }
        return false;
    }


    public void next() {
        if (this.index < this.fileLength) {
            if (this.isNewLine()) {
                if (this.getCurrentPosition() == '\r') this.index++;
                this.line++;
                this.position = 1;
            } else {
                this.position++;
            }
            this.index++;
        }

    }

    @Override
    public String toString() {
        return "(" + line + "," + position + ")" ;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
