package parser;

import token.Token;
import token.TokenDomainTags;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private ArrayList<Token> chain = null;
    private ArrayList<Rule> rules = null;
    private ArrayList<Nonterminal> nonterminals = null;
    private HashMap<Nonterminal, Integer> rulesMap = null;

    public Parser() {
        this.rules = new ArrayList<>();
    }

    public void setChain(ArrayList<Token> chain) {
        this.chain = chain;
    }

    // S ::= DN DT RLST?
    private void parseS(ParserTags.PARSE_MODE mode) throws Exception{
        int index = this.parseDN(mode, 0);
        index = this.parseDT(mode, index);
        this.parseRLST(mode, index);
    }

    // DN ::= "non-terminal" net NLST? ';'
    private int parseDN(ParserTags.PARSE_MODE mode, int index) throws Exception {
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.NONTERMINALSIGN) {
            throw new Exception(String.format("expected '::=' token at position:%s", this.chain.get(index).toString()));
        }
        index++;
        Rule rule = null;
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.NONTERMINAL) {
            throw new Exception(String.format("expected nonterminal token at position:%s", this.chain.get(index).toString()));
        }
        if (mode == ParserTags.PARSE_MODE.INITIAL) {
            Nonterminal newN = new Nonterminal(this.chain.get(index).getAttribute());
            this.addNonterminal(newN);
        }
        if (mode == ParserTags.PARSE_MODE.INITIAL) {
            Nonterminal newRuleN = new Nonterminal(this.chain.get(index).getAttribute());
            this.addRule(newRuleN);
        }
        if (mode == ParserTags.PARSE_MODE.CALCULATE) {
            Nonterminal newRuleN = new Nonterminal(this.chain.get(index).getAttribute());
            rule = rules.get(rulesMap.get(newRuleN));
        }
        index++;
//        index = this.parseNLST(mode, index, rule);

        if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.COMMA) {
//            index++;
            index = this.parseNLST(mode, index, rule);
        }

        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.DOT) {
            throw new Exception(String.format("expected ';' token at position:%s", this.chain.get(index).toString()));
        }
        index++;
        return index;
    }

    // NLST ::= ',' net NLST?
    private int parseNLST(ParserTags.PARSE_MODE mode, int index, Rule rule) throws Exception{
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.COMMA) {
            throw new Exception(String.format("expected = token at position:%s", this.chain.get(index).toString()));
        }
        index++;
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.NONTERMINAL) {
            throw new Exception(String.format("expected nonterminal token at position:%s", this.chain.get(index).toString()));
        }
        if (mode == ParserTags.PARSE_MODE.INITIAL) {
            Nonterminal newRuleN = new Nonterminal(this.chain.get(index).getAttribute());
            this.addRule(newRuleN);
        }
        if (mode == ParserTags.PARSE_MODE.CALCULATE) {
            Nonterminal newRuleN = new Nonterminal(this.chain.get(index).getAttribute());
            rule = rules.get(rulesMap.get(newRuleN));
        }
        index++;
        if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.COMMA) {
            return this.parseNLST(mode, index, rule);
        }
        return index;
    }

    // DT ::= "terminal" tern TLST? ';'
    private int parseDT(ParserTags.PARSE_MODE mode, int index) throws Exception {
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.TERMINALSIGN) {
            throw new Exception(String.format("expected '::=' token at position:%s", this.chain.get(index).toString()));
        }
        index++;
        Rule rule = null;
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.TERMINAL) {
            throw new Exception(String.format("expected nonterminal token at position:%s", this.chain.get(index).toString()));
        }
        index++;
        if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.COMMA) {
//            index++;
            index = this.parseTLST(mode, index, rule);
        }
//        index = this.parseTLST(mode, index, rule);
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.DOT) {
            throw new Exception(String.format("expected ';' token at position:%s", this.chain.get(index).toString()));
        }
        index++;
        return index;
    }

    // TLST ::= ',' term TLST?
    private int parseTLST(ParserTags.PARSE_MODE mode, int index, Rule rule) throws  Exception{
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.COMMA) {
            throw new Exception(String.format("expected '::=' token at position:%s", this.chain.get(index).toString()));
        }
        index++;
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.TERMINAL) {
            throw new Exception(String.format("expected nonterminal token at position:%s", this.chain.get(index).toString()));
        }
        index++;

        if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.COMMA) {
            return this.parseTLST(mode, index, rule);
        }
        return index;
    }

    // RLST ::= net "::=" R* ';' RLST?
    private int parseRLST(ParserTags.PARSE_MODE mode, int index) throws Exception {
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.NONTERMINAL) {
            throw new Exception(String.format("expected nonterminal token at position:%s", this.chain.get(index).toString()));
        }
        Rule rule = null;
        if (mode == ParserTags.PARSE_MODE.CALCULATE) {
            Nonterminal newRuleN = new Nonterminal(this.chain.get(index).getAttribute());
            rule = rules.get(rulesMap.get(newRuleN));
        }
        index++;
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.EQ) {
            throw new Exception(String.format("expected '::=' token at position:%s", this.chain.get(index).toString()));
        }
        index++;
        index = this.parseR(mode, index, rule);
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.DOT) {
            throw new Exception(String.format("expected ';' token at position:%s", this.chain.get(index).toString()));
        }
        index++;
        if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.END) {
            return index;
        }
        index = parseRLST(mode, index);
        return index;
    }

    // R ::= RE ('|' R)*
    private int parseR(ParserTags.PARSE_MODE mode, int index, Rule rule) throws Exception{
        index = this.parseRE(mode, index, rule);
        if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.DELIMITER) {
            index++;
            return this.parseR(mode, index, rule);
        }
        return index;
    }

    // RE ::= (net|term|P) RE1
    private int parseRE(ParserTags.PARSE_MODE mode, int index, Rule rule) throws Exception {
        ParserTags.PARSE_MODE modeTimes = mode;
        ParserTags.PARSE_MODE modeSelf = mode;
        if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.NONTERMINAL) {
            if (mode == ParserTags.PARSE_MODE.CALCULATE) {
                Nonterminal newN = new Nonterminal(this.chain.get(index).getAttribute());
                Rule add = this.rules.get(this.rulesMap.get(newN));
                if (add.hasEps()) {
                    rule.addToFirst(add.getFIRSTwoEps());
                    modeSelf = ParserTags.PARSE_MODE.CALCULATE;
                } else {
                    rule.addToFirst(add.getFIRST());
                    modeSelf = ParserTags.PARSE_MODE.SKIP;
                }
            }
            index++;
            index = this.parseRE1(modeTimes, index, rule);
            if (mode == ParserTags.PARSE_MODE.CALCULATE) {
                if (rule.hasEps()) {
                    rule.removeEps();
                    modeSelf = ParserTags.PARSE_MODE.CALCULATE;
                }
            }
            index = this.parseRE(modeSelf, index, rule);
            return index;
        } else if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.TERMINAL) {
            if (mode == ParserTags.PARSE_MODE.CALCULATE) {
                if (!rule.getFIRST().contains(new Terminal(this.chain.get(index).getAttribute()))) {
                    rule.addToFirst(new Terminal(this.chain.get(index).getAttribute()));
                    rule.setChangedFirst(true);
                } else{
                    rule.setChangedFirst(false);
                }
                modeTimes = ParserTags.PARSE_MODE.CALCULATE;

            }
            index++;
            index = this.parseRE1(modeTimes, index, rule);
            if (mode == ParserTags.PARSE_MODE.CALCULATE) {
                if (rule.hasEps()) {
                    if(rule.isChangedFirst()) {
                        rule.removeEps();
                    }
                    modeSelf = ParserTags.PARSE_MODE.CALCULATE;

                } else {
                    modeSelf = ParserTags.PARSE_MODE.SKIP;
                }
            }
            index = this.parseRE(modeSelf, index, rule);
            return index;
        } else if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.BR_OPEN) {
            index++;
            index = this.parseP(mode, index, rule);
            index = this.parseRE1(mode, index, rule);
            if (mode == ParserTags.PARSE_MODE.CALCULATE) {
                if (rule.hasEps()) {
                    rule.removeEps();
                    modeSelf = ParserTags.PARSE_MODE.CALCULATE;

                } else {
                    modeSelf = ParserTags.PARSE_MODE.SKIP;
                }
            }
            index = this.parseRE(modeSelf, index, rule);
            return index;
        } else {
            if (mode == ParserTags.PARSE_MODE.CALCULATE) {
                if (!rule.getFIRST().contains(new Epsilon())) {
                    rule.addToFirst(new Epsilon());
                    rule.setChangedFirst(true);
                }else {
                    rule.setChangedFirst(false);
                }
            }
            return index;
        }
    }

    // RE1 ::= ('+'|'?'|'*')? RE
    private int parseRE1(ParserTags.PARSE_MODE mode, int index, Rule rule) throws Exception{
        if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.PLUS) {
            index++;
        }
        if (this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.QUESTION || this.chain.get(index).getTag() == TokenDomainTags.TOKEN_TAG.STAR) {
            if (mode == ParserTags.PARSE_MODE.CALCULATE) {
                if(!rule.getFIRST().contains(new Epsilon())) {
                    rule.addToFirst(new Epsilon());
                    rule.setChangedFirst(true);
                }else{
                    rule.setChangedFirst(false);
                }
            }
            index++;
        }
        return index;
    }

    // P ::= '(' R ')'
    private int parseP(ParserTags.PARSE_MODE mode, int index, Rule rule) throws Exception {
        index = this.parseR(mode, index, rule);
        if (this.chain.get(index).getTag() != TokenDomainTags.TOKEN_TAG.BR_CLOSE) {
            throw new Exception(String.format("expected ')' token at position:%s", this.chain.get(index).toString()));
        }
        index++;
        return index;
    }

    private void parse(ParserTags.PARSE_MODE mode) throws Exception {
        if (chain == null) {
            throw new Exception("no tokens");
        }
        try {
            this.parseS(mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateRules() {
        for (Nonterminal n: nonterminals) {
            if(rulesMap.getOrDefault(n, ParserTags.FAIL) == ParserTags.FAIL) {
                return false;
            }
        }
        return true;
    }

    private boolean checkFirstChanged() {
        boolean changed = false;
        for (Rule r: rules) {
            changed = changed || r.isChangedFirst();
        }
        return changed;
    }

    private void printFirsts() {
        for (Rule rule:rules) {
            rule.printFirst();
        }
    }

    private void addRule(Nonterminal nonterminal) {
        int result = rulesMap.getOrDefault(nonterminal, ParserTags.FAIL);
        if (result == ParserTags.FAIL) {
            this.rules.add(new Rule(nonterminal));
            rulesMap.put(nonterminal, rulesMap.size());
        }
    }

    private void addNonterminal(Nonterminal nonterminal) {
        for (Nonterminal n: nonterminals) {
            if (nonterminal == n) return;
        }
        this.nonterminals.add(nonterminal);
    }

    public void calculateFIRST() {
        try {
            rules = new ArrayList<>();
            rulesMap = new HashMap<>();
            nonterminals = new ArrayList<>();
            this.parse(ParserTags.PARSE_MODE.INITIAL);
            if (validateRules()) {
                do {
                    this.parse(ParserTags.PARSE_MODE.CALCULATE);
                } while (checkFirstChanged());
                this.printFirsts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
