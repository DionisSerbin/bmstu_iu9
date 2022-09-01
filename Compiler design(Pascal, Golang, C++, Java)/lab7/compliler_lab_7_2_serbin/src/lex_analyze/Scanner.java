package lex_analyze;

import syntax_analyze.symbols.Term;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Scanner {
    protected final static String NEWLINE = "newline";
    protected final static String BLANK   = "blank";
    final private static String newline_epxr = "\\R";
    final private static String blank_expr = "[ \\t]+";


    protected HashMap<String, String> regexp = new HashMap<>();

    protected String text = "";
    private Pattern p ;
    protected Matcher m;
    protected Coords coord;
    protected String image = "";
    protected StringBuilder log = new StringBuilder();

    public static String makeGroup(String name, String expr) {
        return "(?<" + name + ">(" + expr + "))";
    }

    public String setPattern() {
        String res =
                makeGroup(BLANK, blank_expr) + "|" +
                makeGroup(NEWLINE, newline_epxr);
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
        coord = Coords.start();
    }

    public Scanner(HashMap<String, String> termsexpr, String filepath){
        File file = new File(filepath);
        try {
            text = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            System.err.printf("file %s cannot be read\n", file.toPath());
        }
        regexp = termsexpr;
        String pattern = setPattern();
        p = Pattern.compile(pattern, Pattern.DOTALL);
//        text = text.replaceAll("\\+", "'\\+'").replaceAll("\\(", "'\\('")
//                .replaceAll("\\*", "'\\*'").replaceAll("\\)", "'\\)'");
        m = p.matcher(text);
        coord = Coords.start();
    }

    protected boolean isType(String type) {
        return (image = m.group(type)) != null;
    }

    public String getText() {
        return text;
    }

    protected Token returnToken (String type) {
        Coords last = coord;
        coord = coord.shift(image.length());
        log.append(type).append(' ').append(last.toString()).append('-').append(coord.toString())
                .append(": <").append(image).append(">\n");
        return new Token(type, image, last, coord);
    }

    public Token nextToken() {
        if (coord.getPos() >= text.length()) {
            return new Token(Term.EOF, coord);
        }
        String image;
        if (m.find()) {
            if (m.start() != coord.getPos()) {
                log.append(String.format("SYNTAX ERROR: %d",
                       coord.getPos())).append(coord.toString()).append('\n');
                System.out.println(String.format("SYNTAX ERROR: %d",
                        coord.getPos()) + coord.toString());
                System.exit(-1);
            }
            if ((image = m.group(BLANK)) != null) {
                coord = coord.shift(image.length());
                return nextToken();
            }
            if ((image = m.group(NEWLINE)) != null) {
                coord = coord.newline();
                coord = coord.shift(image.length() - 1);
                return nextToken();
            }
            for (String s: regexp.keySet()) {
                if (isType(s)) {
                    return returnToken(s);
                }
            }

            System.out.println("ERROR " + coord.toString() + " " + text.substring(coord.getPos()));
            return nextToken();

        } else {
            log.append("SYNTAX ERROR: ").append(coord.toString()).append('\n');
            log.append("SYNTAX ERROR: ").append(coord.toString()).append('\n');
            System.out.println("SYNTAX ERROR: " + coord.toString());
            return new Token(Term.EOF, coord);
        }
    }
}