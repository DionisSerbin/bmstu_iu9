import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static ArrayList<String> token = new ArrayList<>();
    private static String KEYWORD = "(print|goto|gosub)";
    private static String INTEGER_CONSTANT = "([0-9]+|&\\H[0-9a-f]+)";
    private static String IDENT = "\\p{L}(\\p{L}|[0-9])*";
    private static String pattern = "(" + KEYWORD + ")|(" + INTEGER_CONSTANT + ")|(" + IDENT + ")|(\\S)";

    public static void test_match(String line, int lineNumber){
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE);
        String[] words = line.split("\\s+");

        for(String word: words){
            Matcher m = p.matcher(word);
            if(m.matches()){
                if(Pattern.compile(KEYWORD).matcher(word).matches()){
                  //  System.out.format("KEYWORD (%d, %d): %s %n", lineNumber, line.indexOf(word) + 1, word);
                    token.add("KEYWORD (" + lineNumber + ", " + (line.indexOf(word) + 1) + "): " + word);
                } else if (Pattern.compile(IDENT).matcher(word).matches()) {
                   // System.out.format("IDENT (%d, %d): %s %n", lineNumber, line.indexOf(word) + 1, word);
                    token.add("IDENT (" + lineNumber + ", " + (line.indexOf(word) + 1) + "): " + word);
                }else if(Pattern.compile(INTEGER_CONSTANT).matcher(word).matches()) {
                  //  System.out.format("INTEGER CONSTANT (%d, %d): %s %n", lineNumber, line.indexOf(word) + 1, word);
                    token.add("INTEGER CONSTANT (" + lineNumber + ", " + (line.indexOf(word) + 1) + "):" + word);
                }
            } else {
               // System.out.format("SYNTAX ERR (%d, %d): %s %n", lineNumber, line.indexOf(word) + 1, word);
                token.add("SYNTAX ERR (" + lineNumber + ", " + (line.indexOf(word) + 1) + ")");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner;
        try {
            scanner = new Scanner(new File("src/test.txt"));
        }catch (java.io.FileNotFoundException e) {
            System.out.println(e.toString());
            return;
        }

        int lineNumber = 0;
        while (scanner.hasNextLine()){
            lineNumber++;
            String line = scanner.nextLine();
            test_match(line, lineNumber);
        }

        Iterator<String> iter = token.iterator();

        while (iter.hasNext()){
            System.out.println(iter.next());
        }
    }
}
/*
dasd19213
421674215 &H461418
gfsa321415 12435 1351asfaf
&Hwriteln writeln&H
&H6241756198 dassaf
PRINT        6198531568
goto print &H5326523 safasf61784
gosub GOSUB GOTO  432PRINT 21414352 fasgufas52418712 &H411
 */