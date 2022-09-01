import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tokipoki {
    private static String KEYWORD = "(PRINT|GOTO|GOSUB|print|goto|gosub)";
    private static String INTEGER_CONSTANT = "([0-9]+|\\&\\H[0-9]+)";
    private static String IDENT = "([a-zA-Z]+[0-9]*)";
    private static String pattern = "(" + KEYWORD + ")|(" + INTEGER_CONSTANT + ")|(" + IDENT + ")|(\\S)";

    public static void test_match(String line, int lineNumber){
        Pattern p = Pattern.compile(pattern);
        String[] words = line.split("\\s+");

        for(String word: words){
            Matcher m = p.matcher(word);
            if(m.matches()){
                if(Pattern.compile(KEYWORD).matcher(word).matches()){
                    System.out.format("KEYWORD (%d, %d): %s %n", lineNumber, line.indexOf(word) + 1, word);
                } else if (Pattern.compile(IDENT).matcher(word).matches()) {
                    System.out.format("IDENT (%d, %d): %s %n", lineNumber, line.indexOf(word) + 1, word);
                }else if(Pattern.compile(INTEGER_CONSTANT).matcher(word).matches()) {
                    System.out.format("INTEGER CONSTANT (%d, %d): %s %n", lineNumber, line.indexOf(word) + 1, word);
                }
            } else {
                System.out.format("SYNTAX ERR (%d, %d): %s %n", lineNumber, line.indexOf(word) + 1, word);
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
    }
}
