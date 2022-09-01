import java.io.File;
import java.util.Scanner;


/*
digraph G {

    node [shape = doublecircle];
    spaces;
    number;
    signStar
    signCloseBrace;
    signOpenBrace;
    comment;
    ident;
    preKey1;
    preKey2;
    preKey3;
    preKey4;
    preKey5;
    preKey6;
    preKey7;
    keyword;

    node [shape = circle];

    start -> spaces[label = "\s"]
    spaces -> spaces[label = "\s"]
    start -> number[label = "\d"]
    number -> number[label = "\d"]
    start -> signStar[label = "\*"]
    start -> signCloseBrace[label = "\)"]
    start -> signOpenBrace[label = "\("]
    signOpenBrace -> preComment2[label = "\*"]
    preComment2 -> preComment2[label = "[^]"]
    preComment2 -> preComment3[label = "\*"]
    preComment3 -> preComment3[label = "\*"]
    preComment3 -> preComment2[label = "[^]"]
    preComment3 -> comment[label = "\)"]
    start -> preKey1[label = "c"]
    start -> ident[label = "[a-zA-Z]"]
    start -> preKey4[label = "b"]
    preKey1 -> preKey2 [label = "a"]
    preKey1 -> ident[label = "[a-zA-Z0-9]"]
    preKey2 -> preKey3[label = "s"]
    preKey2 -> ident[label = "[a-zA-Z0-9]"]
    preKey3 -> keyword[label = "e"]
    preKey3 -> ident[label = "[a-zA-Z0-9]"]
    keyword -> ident[label = "[a-zA-Z0-9]"]
    preKey4 -> preKey5[label = "r"]
    preKey4 -> ident[label = "[a-zA-Z0-9]"]
    preKey5 -> preKey6[label = "e"]
    preKey5 -> ident[label = "[a-zA-Z0-9]"]
    preKey6 -> preKey7[label = "a"]
    preKey6 -> ident[label = "[a-zA-Z0-9]"]
    preKey7 -> keyword[label = "k"]
    preKey7 -> ident[label = "[a-zA-Z0-9]"]

}
 */

public class complab5 {

    final static int[][] table = {
                              /*  0   1   2   3   4   5   6   7    8    9  10     11     12  13*/
                              /*  c   a   s   e   b   r   k  num   *    )   (   [a-zA-Z] ws  [^]*/
    /*     START             */ { 1,  9,  9,  9,  4,  9,  9, 10,  11,  12, 13,     9,   17,  -1},
    /* 1   preKey1(c)        */ { 9,  2,  9,  9,  9,  9,  9,  9,  -1,  -1, -1,     9,   -1,  -1},
    /* 2   preKey2(a)        */ { 9,  9,  3,  9,  9,  9,  9,  9,  -1,  -1, -1,     9,   -1,  -1},
    /* 3   preKey3(s)        */ { 9,  9,  9,  8,  9,  9,  9,  9,  -1,  -1, -1,     9,   -1,  -1},
    /* 4   preKey4(b)        */ { 9,  9,  9,  9,  9,  5,  9,  9,  -1,  -1, -1,     9,   -1,  -1},
    /* 5   preKey5(r)        */ { 9,  9,  9,  6,  9,  9,  9,  9,  -1,  -1, -1,     9,   -1,  -1},
    /* 6   preKey6(e)        */ { 9,  7,  9,  9,  9,  9,  9,  9,  -1,  -1, -1,     9,   -1,  -1},
    /* 7   preKey7(a)        */ { 9,  9,  9,  9,  9,  9,  8,  9,  -1,  -1, -1,     9,   -1,  -1},
    /* 8   keyword           */ { 9,  9,  9,  9,  9,  9,  9,  9,  -1,  -1, -1,     9,   -1,  -1},
    /* 9   ident             */ { 9,  9,  9,  9,  9,  9,  9,  9,  -1,  -1, -1,     9,   -1,  -1},
    /* 10  number            */ {-1, -1, -1, -1, -1, -1, -1, 10,  -1,  -1, -1,    -1,   -1,  -1},
    /* 11  signStar          */ {-1, -1, -1, -1, -1, -1, -1, -1,  -1,  -1, -1,    -1,   -1,  -1},
    /* 12  signCloseBrace    */ {-1, -1, -1, -1, -1, -1, -1, -1,  -1,  -1, -1,    -1,   -1,  -1},
    /* 13  signOpenBrace     */ {-1, -1, -1, -1, -1, -1, -1, -1,  14,  -1, -1,    -1,   -1,  -1},
    /* 14  preComment2       */ {14, 14, 14, 14, 14, 14, 14, 14,  15,  14, 14,    14,   14,  14},
    /* 15  preComment3       */ {14, 14, 14, 14, 14, 14, 14, 14,  15,  16, 14,    14,   14,  14},
    /* 16  comment           */ {-1, -1, -1, -1, -1, -1, -1, -1,  -1,  -1, -1,    -1,   -1,  -1},
    /* 17  spaces            */ {-1, -1, -1, -1, -1, -1, -1, -1,  -1,  -1, -1,    -1,   17,  -1},
    };



    public static void main(String []args) {

        StringBuilder text = new StringBuilder();
        Scanner scanner;

        try {
            scanner = new Scanner(new File("in.txt"));
        } catch (java.io.FileNotFoundException e) {
            System.out.println(e.toString());
            return;
        }

        int i = 1;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(i + " [" + line + "]");
            text.append(line).append("\n");
            i++;
        }
        text.deleteCharAt(text.length() - 1);

        Automata auto = new Automata(text.toString(), table);

        System.out.println("\nTokens:");

        Token t = auto.nextToken();
        while (null != t) {
            if (t.getTag() == DomainTag.EOP) {
                System.out.println(t.toString());
                break;
            }
            if(t.getTag() != DomainTag.WHITESPACE)
                System.out.println(t.toString());
            t = auto.nextToken();

        }
        auto.output_messages();
    }
}