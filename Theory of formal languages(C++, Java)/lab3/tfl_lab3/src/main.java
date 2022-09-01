import java.util.*;


// /Users/denisserbin/tfl_lab3/test1_lab3
public class main {

    private static void closure(Set<String> possiblyRgNoterminals, Set<String> rgNoterminals,
                                Queue<String> newRgNoterminals, Parser rules) {
        for (String noterminal : possiblyRgNoterminals) {
            boolean check = true;
            Set<String> notermFirstDepends = rules.rules.get(noterminal).firstDepends;
            for (String depend : notermFirstDepends) {
                if (!depend.equals(noterminal) && !rgNoterminals.contains(depend)) {
                    check = false;
                    break;
                }
            }
            if (check) {
                newRgNoterminals.add(noterminal);
                possiblyRgNoterminals.remove(noterminal);
            }
        }
    }

    public static void makeDecision() throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter path to test: ");
        String pathToTest = in.nextLine();
        Parser rules = new Parser(pathToTest);
        Map<String, TreeNoterminals> leftOutStart = new HashMap<>();
        Set<String> connected = new HashSet<>();
        for (String s : new HashSet<>(rules.rules.get("S").depends)) {
            if (s.matches("^[A-Z][0-9]*$")) {
                connected.add(s);
            }
        }
        for (String s : connected) {
            leftOutStart.put(s, new TreeNoterminals(s, rules));
        }
        Set<String> rgNoterminals = new HashSet<>();
        Set<String> possiblyRgNoterminals = new HashSet<>();
        Set<String> warningNoterminals = new HashSet<>();
        for (TreeNoterminals treeNoterminals : leftOutStart.values()) {
            treeNoterminals.distrib(leftOutStart, rules.majority,
                    rgNoterminals, possiblyRgNoterminals, warningNoterminals);
            if (treeNoterminals.tree.isRoot && rules.noRgNoterminals.contains(treeNoterminals.tree.treeExp)) {
                String s = "digraph {\n"
                        + treeNoterminals.tree.graphviz.toString()
                        + "}";
                System.out.println(s);
            }
        }
        if (warningNoterminals.isEmpty()) {
            Queue<String> newRegularNoterminals = new PriorityQueue<>();
            closure(possiblyRgNoterminals, rgNoterminals, newRegularNoterminals, rules);
            while (!newRegularNoterminals.isEmpty()) {
                rgNoterminals.add(newRegularNoterminals.poll());
                closure(possiblyRgNoterminals, rgNoterminals, newRegularNoterminals, rules);
            }
            if(!rgNoterminals.isEmpty()) {
                System.out.print("Регулярные: ");
                System.out.println(rgNoterminals);
            }
            if(!possiblyRgNoterminals.isEmpty()) {
                System.out.print("Возможно регулярные: ");
                System.out.println(possiblyRgNoterminals);
            }
            if(!warningNoterminals.isEmpty()) {
                System.out.print("Подозрительные: ");
                System.out.println(warningNoterminals);
            }
            if (rgNoterminals.isEmpty()) {
                System.out.println("Нельзя проверить регулярность");
            } else if (possiblyRgNoterminals.isEmpty()) {
                System.out.println("регулярный");
            } else {
                System.out.println("возможно регулярный");
            }
        } else {
            if(!rgNoterminals.isEmpty()) {
                System.out.print("Регулярные: ");
                System.out.println(rgNoterminals);
            }
            if(!possiblyRgNoterminals.isEmpty()) {
                System.out.print("Возможно регулярные: ");
                System.out.println(possiblyRgNoterminals);
            }
            if(!warningNoterminals.isEmpty()) {
                System.out.print("Подозрительные: ");
                System.out.println(warningNoterminals);
            }
            System.out.println("не регулярный");
        }
    }

    public static void main(String[] args) throws Exception {
        makeDecision();
    }
}
