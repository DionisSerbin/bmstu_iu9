import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {

    private HashMap<String, String> regexp = new HashMap<>();

    private String text = "";
    private Pattern p ;
    private Matcher m;
    private Position coord;
    private String image = "";
    private ArrayList<Fragment> comments  = new ArrayList<>();

    public static String makeGroup(String name, String expr) {
        return "(?<" + name + ">(" + expr + "))";
    }

    public String setPattern() {
        String res =
                makeGroup("tab", "[ \\t]+") + "|" +
                makeGroup("newline", "\\R") + "|" +
                makeGroup("comments", "#[^#\\n]*\\n");
        for (Map.Entry<String, String> e: regexp.entrySet()) {
            res += "|" + makeGroup(e.getKey(), e.getValue());
        }
        return res;
    }

    public Scanner(String filepath, HashMap<String, String> termsexpr) {
        File file = new File(filepath);
        try {
            text = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            System.err.printf("file %s cannot be read\n", file.toPath());
        }
        regexp = termsexpr;
        String pattern = setPattern();
        p = Pattern.compile(pattern, Pattern.DOTALL);
        m = p.matcher(text);
        coord = Position.start();
    }

    private boolean isType(String type) {
        return (image = m.group(type)) != null;
    }

    private Token returnToken (String type) {
        Position last = coord;
        coord = coord.shift(image.length());
        return new Token(type, image, last, coord);
    }

    public Token nextToken() {
        if (coord.getPos() >= text.length()) {
            return new Token(Term.EOF, coord);
        }
        String image;
        if (m.find()) {
            if (m.start() != coord.getPos()) {
                System.out.println(String.format("ERROR: SYNTAX %d - %d",
                        m.start(), coord.getPos()) + coord.toString());
                coord = coord.shift(m.start() - coord.getPos());
            }
            for (String s: regexp.keySet()) {
                if (isType(s)) {
                    return returnToken(s);
                }
            }
            if ((image = m.group("tab")) != null) {
                coord = coord.shift(image.length());
                return nextToken();
            }
            if ((image = m.group("newline")) != null) {
                coord = coord.newline();
                coord = coord.shift(image.length() - 1);
                return nextToken();
            }
            if ((image = m.group("comments")) != null) {
                Position last = coord;
                coord = coord.shift(image.length() - 1);
                comments.add(new Fragment(image, last, coord));
                coord = coord.newline();
                return nextToken();
            } else {
                System.out.println("ERROR: " + coord.toString() + " " + text.substring(coord.getPos()));
                return nextToken();
            }
        } else {
            System.out.println("ERROR: SYNTAX" + coord.toString());
            return new Token(Term.EOF, coord);
        }
    }

    public ArrayList<Fragment> getComments() {
        return this.comments;
    }
}