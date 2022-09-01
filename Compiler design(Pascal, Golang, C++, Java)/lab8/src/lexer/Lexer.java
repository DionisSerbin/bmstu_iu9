package lexer;

import token.*;

import java.util.ArrayList;

public class Lexer {

    private Position position;
    private ArrayList<Message> messages;
    private ArrayList<Token> tokens;
    private String programm;

    public Lexer() {
        this.tokens = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public void analyze(String programm) {
        this.programm = programm;
        this.position = new Position(programm);
        this.setTokens();
    }

    private void setTokens(){
        while (this.position.getCurrentPosition() != -1) {
            while (this.position.isWhiteSpace()) {
                this.position.next();
            }
            Position start = new Position(position);
            if (this.position.isCapLetterOrDigit()){
                if (this.position.isSmallLetter() || this.position.isSymbol()) {
                    this.messages.add(new Message("neterminal token name must look like: T1", false, new Position(this.position)));
                    if (this.position.isLetterOrDigit() || this.position.isSymbol()) {
                        while (this.position.isLetterOrDigit() || this.position.isSymbol()) this.position.next();
                    }
                    while (this.position.isWhiteSpace()) {
                        this.position.next();
                    }

                    this.tokens.add(new NonterminalToken(programm.substring(start.getIndex(), this.position.getIndex()), start, new Position(this.position)));
                    continue;
                }

                if (this.position.isCapLetter()) {
                    while (this.position.isCapLetter()) this.position.next();
                    if (this.position.isDigit()) {
                        while (this.position.isDigit()) this.position.next();
                    }
                    if (this.position.isSmallLetter()) {
                        this.messages.add(new Message("neterminal token name must look like: T1", false, new Position(this.position)));
                        while (this.position.isLetterOrDigit() || this.position.isSymbol()) this.position.next();
                    }
                    while (this.position.isWhiteSpace()) {
                        this.position.next();
                    }
//                    this.position.next();
                    this.tokens.add(new NonterminalToken(programm.substring(start.getIndex(), this.position.getIndex()), start, new Position(this.position)));
                    continue;
                } else {
                    this.messages.add(new Message("wrong neterminal or axiom token", true, new Position(this.position)));
                    this.tokens.add(new NonterminalToken(programm.substring(start.getIndex(), this.position.getIndex()), start, new Position(this.position)));
                    continue;
                }
            }
            if (this.position.isKeySymbol()){
                this.tokens.add(new OperatorToken(programm.substring(start.getIndex(), this.position.getIndex()+1), start, new Position(this.position)));
                this.position.next();
                continue;
            }
            if (this.position.isEscapeChar()) {
                do {
                    this.position.next();
                }while (!this.position.isEscapeChar());
                this.position.next();
                if (this.position.isKeySymbol()) {
                    this.tokens.add(new TerminalToken(programm.substring(start.getIndex(), this.position.getIndex()), start, new Position(this.position)));
                } else {
                    this.tokens.add(new TerminalToken(programm.substring(start.getIndex(), this.position.getIndex()), start, new Position(this.position)));
                }
                continue;
            }
            if (this.position.isLetter()){
                while (!(this.position.isWhiteSpace() || this.position.isComma())){
                    this.position.next();
                }
                if (this.position.isTerminalSign(start.getIndex(), this.position.getIndex())){
                    this.tokens.add(new OperatorToken(programm.substring(start.getIndex(), this.position.getIndex()), start, new Position(this.position)));
                    this.position.next();
                    continue;
                }
                else if (this.position.isNonTerminalSign(start.getIndex(), this.position.getIndex())){
                    this.tokens.add(new OperatorToken(programm.substring(start.getIndex(), this.position.getIndex()), start, new Position(this.position)));
                    this.position.next();
                    continue;
                }
                else if (this.position.isTerminalDigit(start.getIndex(), this.position.getIndex())){
                    this.tokens.add(new TerminalToken(programm.substring(start.getIndex(), this.position.getIndex()), start, new Position(this.position)));
                    continue;
                }
            }
            if (this.position.isColon()){
                while (!this.position.isWhiteSpace()){
                    this.position.next();
                }
                if (this.position.isEquals(start.getIndex(), this.position.getIndex())){
                    this.tokens.add(new OperatorToken(programm.substring(start.getIndex(), this.position.getIndex()), start, new Position(this.position)));
                    this.position.next();
                    continue;
                } else {
                    this.messages.add(new Message("WRONG USE COLON", false, new Position(this.position)));
                }
            }
            if (this.position.getCurrentPosition() != -1) {
                while ((this.position.isSymbol() || this.position.isLetterOrDigit()) && !this.position.isWhiteSpace() && this.position.getCurrentPosition() != -1) {
                    this.position.next();
                }
                this.tokens.add(new TerminalToken(programm.substring(start.getIndex(), this.position.getIndex()), start, new Position(this.position)));
            }
        }
        this.tokens.add(new EndToken(new Position(this.position), new Position(this.position)));
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    public ArrayList<Token> getTokens() {
        return this.tokens;
    }

    public void printTokens() {
        tokens.forEach(System.out::println);
    }

    public void printMessages() {
        messages.forEach(System.out::println);
    }

}
