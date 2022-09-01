import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

class Automata {
    private SortedMap<Position, String> messages;
    public String program;
    private Position pos;
    private int state;
    private int[][] table;

    public Automata(String program, int[][] table) {
        this.program = program;
        this.pos = new Position(program);
        this.state = 0;
        this.messages = new TreeMap<>();
        this.table = table;
    }

    private int getCode(char c) {
        switch (c) {
            case 'c':
                return 0;
            case 'a':
                return 1;
            case 's':
                return 2;
            case 'e':
                return 3;
            case 'b':
                return 4;
            case 'r':
                return 5;
            case 'k':
                return 6;
        }
        if (c >= '0' && c <= '9')
            return 7;
        if (c == '*')
            return 8;
        if (c == ')')
            return 9;
        if(c == '(')
            return 10;
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
            return 11;
        if (' ' == c || '\n' == c || '\r' == c || '\t' == c)
            return 12;
        return 13;
    }

    private DomainTag getStateName(int state) {
        switch (state) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return DomainTag.IDENT;
            case 8:
                return DomainTag.KEYWORD;
            case 9:
                return DomainTag.IDENT;
            case 10:
                return DomainTag.NUMBER;
            case 11:
            case 12:
            case 13:
                return DomainTag.OPERATION;
            case 14:
                return DomainTag.PRECOMMENT2;
            case 15:
                return DomainTag.PRECOMMENT3;
            case 16:
                return DomainTag.COMMENT;
            case 17:
                return DomainTag.WHITESPACE;
            default:
                return DomainTag.ERROR;
        }
    }

    boolean err = false;

    public Token nextToken() {

        if(pos.getCp() == -1){
            Position posCopy = pos.copy();
            return new Token(DomainTag.EOP, "", "", posCopy, posCopy);
        }

        while (-1 != pos.getCp()) {
            StringBuilder word = new StringBuilder();
            StringBuilder parse = new StringBuilder();
            state = 0;
            boolean final_state = false;
            Position start = pos.copy();

            while (-1 != pos.getCp()) {

                char curr_char = program.charAt(pos.getIndex());
                int jump_code = getCode(curr_char);

                if (13 == jump_code && table[state][jump_code] == -1) {
                    if (!err) {
                        messages.put(pos.copy(), "Unexpected characters");
                        err = true;
                    }
                    break;
                }
                err = false;

                parse.append("(").append(state).append(")->");
                if (curr_char == '\n'){
                    parse.append("[" + "\\n" + "]->");
                }
                else parse.append("[").append(curr_char).append("]->");

                int next_state = table[state][jump_code];

                if (-1 == next_state) {
                    final_state = true;
                    parse.append("(-1)\n");
                    break;
                }

                word.append(curr_char);
                state = next_state;
                pos.next();
            }
            if (final_state) {

                return new Token(getStateName(state), word.toString().replaceAll("\n"," "), parse.toString(), start, pos);
            }
            if (pos.getCp() == -1){

                parse.append("(-1)\n");

                if(getStateName(state) == DomainTag.PRECOMMENT2 || getStateName(state) == DomainTag.PRECOMMENT3){
                    messages.put(pos.copy(), "end of program found, '*)' expected");
                    return new Token(DomainTag.COMMENT, word.toString().replaceAll("\n"," "), parse.toString(), start, pos);

                }
                return new Token(getStateName(state), word.toString().replaceAll("\n"," "), parse.toString(), start, pos);

            }
            pos.next();
        }
        return null;
    }

    public void output_messages() {
        System.out.println("\nMessages:");
        for (Map.Entry<Position, String> entry : messages.entrySet()) {
            System.out.print("ERROR ");
            System.out.print("(" + entry.getKey().getLine() + ", " +
                    entry.getKey().getPos() + "): ");
            System.out.println(entry.getValue());
        }
    }

}