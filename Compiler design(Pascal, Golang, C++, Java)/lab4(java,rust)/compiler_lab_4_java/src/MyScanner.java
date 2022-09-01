import java.util.ArrayList;
import java.util.List;

public class MyScanner {
    public final String program;

    private Compiler compiler;
    private Position cur;
    private List<Fragment> comments;

    public MyScanner(String program, Compiler compiler) {
        this.compiler = compiler;
        this.cur = new Position(program);
        this.program = program;
        this.comments = new ArrayList<>();
    }

    public void outputComments() {
        System.out.println();
        System.out.println("Comments:");

        for (Fragment f : comments) {
            int commStart = f.starting.getIndex();
            int commEnd = f.following.getIndex();

            System.out.println(f.toString() + ": " + program.substring(commStart, commEnd).replaceAll("\n", " "));
        }
    }

    public Token nextToken() {

        if(cur.getCp() == -1){
            Position curCopy = cur.copy();
            return new SpecToken("", curCopy, curCopy);
        }

        while (cur.getCp() != -1){

            while (cur.isWhiteSpace() || cur.isNewLine())
                cur.next();

            Position start = cur.copy();

            if (cur.getCp() == '/'){
                cur.next();
                if(cur.getCp() == '/'){
                    do{
                        cur.next();
                    } while (cur.getCp() != '\n' && cur.getCp() != -1);

                    Position curCopy = cur.copy();
                    comments.add(new Fragment(start, curCopy));
                }
                else {
                    String currWord = "/";
                    boolean flagIdentEOP = false;
                    while(cur.getCp() != '/' && cur.getCp() != -1){
                        currWord += (char) (cur.getCp());
                        cur.next();
                    }

                    if (-1 == cur.getCp())
                        compiler.addMessage(true, cur,
                                "end of program found, '/' expected");

                    Position curCopy = cur.copy();

                    if(cur.getCp() == '/') {
                        currWord += (char) (cur.getCp());
                        cur.next();
                    }

                    if(currWord.equals("/while/")){
                        return new KeyToken(DomainTag.KEYWORD_WHILE, currWord.replaceAll("/", ""), start, curCopy);
                    }

                    if(currWord.equals("/do/")){
                        return new KeyToken(DomainTag.KEYWORD_DO, currWord.replaceAll("/", ""), start, curCopy);
                    }

                    if(currWord.equals("/end/")){
                        return new KeyToken(DomainTag.KEYWORD_END, currWord.replaceAll("/", ""), start, curCopy);
                    }
//                    if (currWord.equals("/while/") || currWord.equals("/do/") || currWord.equals("/end/"))
//                        return new KeyToken(currWord.replaceAll("/", ""), start, curCopy);
                    return new IdentToken(compiler.addName(currWord.replaceAll("/", "").replaceAll("\n", "")), currWord, start, curCopy);
                }
            } else {
                compiler.addMessage(true, start, "unexpected character in ident");
                while (cur.getCp() != '\n' && cur.getCp() != ' ' && cur.getCp() != -1){
                    cur.next();
                }
            }
            cur.next();
        }
        return null;
    }
}
