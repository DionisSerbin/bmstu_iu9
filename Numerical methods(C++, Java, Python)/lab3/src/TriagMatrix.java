import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class TriagMatrix {

    private static int size = 0;

    public static ArrayList<ArrayList<Double>> matr = new ArrayList<>();
    public static ArrayList<Double> a = new ArrayList<>();
    public static ArrayList<Double> b = new ArrayList<>();
    public static ArrayList<Double> c = new ArrayList<>();

    public static ArrayList<Double> d = new ArrayList<>();

    public static ArrayList<Double> alpha = new ArrayList<>();
    public static ArrayList<Double> beta = new ArrayList<>();

    public static ArrayList<Double> x = new ArrayList<>();

    public void initMatr(Scanner in) {
        System.out.println("Input a numbers of matrix");
        for (int i = 0; i < size; i++) {
            ArrayList<Double> str = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                double numberOfEq = in.nextDouble();
                str.add(numberOfEq);
            }
            matr.add(str);
        }

        for(int i = 0; i < size; i++){
            b.add(i, matr.get(i).get(i));
        }

        for (int i = 0; i < size - 1; i++){
            a.add(i, matr.get(i + 1).get(i));
            c.add(i, matr.get(i).get(i + 1));
        }
    }

    public void initD(Scanner in){
        System.out.println("Input a numbers of vector d");
        ArrayList<Double> str = new ArrayList<>();
        for (int i = 0; i < size; i++){
            double numberOfEq = in.nextDouble();
            d.add(numberOfEq);
        }
    }

    public void initialize(){
        Scanner in = new Scanner(System.in);
        System.out.println("M * x = d");
        System.out.println("Input a size of matrix m");
        size = in.nextInt();

        initMatr(in);
        initD(in);

        for (int i = 0; i < size; i++) x.add((double) 0);
    }

    public void straightRun(ArrayList<Double> d){
        alpha.add(- c.get(0) / b.get(0));
        beta.add(d.get(0) / b.get(0));
        for(int i = 1; i < size; i ++){
            double divider = (a.get(i - 1) * alpha.get(i - 1)) + b.get(i);
            double betaDivisible = (d.get(i) - (a.get(i - 1) * beta.get(i - 1))) / divider;
            if(i != size - 1) {
                double alphaDivisible = -c.get(i) / divider;
                alpha.add(alphaDivisible);
            }
            beta.add(betaDivisible);
        }
    }

    public void reverseRun(ArrayList<Double> x){
        x.set(x.size() - 1, beta.get( beta.size() - 1 ));
        for (int i = size - 2; i > -1; i--){
            double xIter = alpha.get(i) *  x.get(i + 1) + beta.get(i);
            x.set(i, xIter);
        }
    }

    public void decideEq(ArrayList<Double> d, ArrayList<Double> x){
        straightRun(d);
        reverseRun(x);
    }

    public void printX(){
        System.out.println("x: ");
        for (int i = 0; i < x.size(); i++){
            DecimalFormat dF = new DecimalFormat("00.000000000000000000000000");
            //String str = String.format("  %f", x.get(i));
            System.out.println(dF.format(x.get(i)));
        }
    }

    public void findE(){
        ArrayList<Double> dPoint = new ArrayList<>();
        for(int i = 0; i < size; i++){
            double sub = 0;
            for(int j = 0; j < x.size(); j++){
                sub += x.get(i) * matr.get(i).get(j);
            }
            dPoint.add(sub);
        }
        ArrayList<Double> dE = new ArrayList<>();
        for (int i = 0; i < d.size(); i++){
            dE.add(d.get(i) - dPoint.get(i));
        }
        ArrayList<Double> xE = new ArrayList<>();
        for (int i = 0; i < size; i++) xE.add((double) 0);
        alpha.clear();
        beta.clear();
        decideEq(dE, xE);
        System.out.println("E: ");
        for (int i = 0; i < xE.size(); i++){
            DecimalFormat dF = new DecimalFormat("00.0000000000000000000000000000000000");
            System.out.println(dF.format(xE.get(i)));
        }
        for (int i = 0; i < d.size(); i++){
            xE.set(i, x.get(i) - xE.get(i));
        }
        System.out.println("x*: ");
        for (int i = 0; i < xE.size(); i++){
            DecimalFormat dF = new DecimalFormat("00.0000000000000000000000000000000000");
            System.out.println(dF.format(xE.get(i)));
        }

    }

    public void main() {
        initialize();
        decideEq(d, x);
        printX();
        //findE();
    }

    public int size() {
        return size;
    }

    public double getX(int i) {
        return x.get(i);
    }
}

/*
4
4 1 0 0
1 4 1 0
0 1 4 1
0 0 1 4
5 6 6 5
 */