import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//
// /Users/denisserbin/lab2/tfl_lab2_time_of_regex/test1_tfl_time

public class Main {
    static String academic_regex = "(0|1(01*0)*1)+";
    static String negative_regex = "([^1]|[^0]([^1][^0]*[^1])*[^0])+";
    static String klini_regex = "(0|1(01*?0)*?1)+";
    static ArrayList<Long> academic_times = new ArrayList<>();
    static ArrayList<Long> negative_times = new ArrayList<>();
    static ArrayList<Long> klini_times = new ArrayList<>();

    public static void check_regex(ArrayList<String> exprs, String regex){

        if(regex.equals(academic_regex)){
            System.out.println("academic regex:");
        } else if(regex.equals(negative_regex)){
            System.out.println("negative regex:");
        } else if(regex.equals(klini_regex)){
            System.out.println("klini regex:");
        }

        long startTime = System.nanoTime();
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(exprs.get(0));
        while (matcher.find()) {
            matcher.group(0);
        }
        long endTime = System.nanoTime();
        System.out.println("Total execution time: " + (endTime - startTime) + "ns");
        if(regex.equals(academic_regex)){
            academic_times.add(endTime - startTime);
        } else if(regex.equals(negative_regex)){
            negative_times.add(endTime - startTime);
        } else if(regex.equals(klini_regex)){
            klini_times.add(endTime - startTime);
        }
    }

    public static long average(ArrayList<Long> arrayList){
        long aver = 0;
        for (int i = 0; i < arrayList.size(); i++){
            aver += arrayList.get(i);
        }
        return aver / arrayList.size();
    }

    public static void main(String[] args) throws FileNotFoundException {
        for (int i = 0; i < 10; i++) {
            Scanner in = new Scanner(System.in);
            System.out.print("Enter path to test: ");
            String pathToTest = in.nextLine();
            Scanner inputData = new Scanner(new FileReader(pathToTest));
            ArrayList<String> exprs = new ArrayList<>(0);
            while (inputData.hasNext()) {
                exprs.add(inputData.nextLine());
            }
            check_regex(exprs, academic_regex);
            check_regex(exprs, negative_regex);
            check_regex(exprs, klini_regex);
        }
        System.out.println("academic times: " + average(academic_times));
        System.out.println("negative times: " + average(negative_times));
        System.out.println("klini times: " + average(klini_times));
    }
}
