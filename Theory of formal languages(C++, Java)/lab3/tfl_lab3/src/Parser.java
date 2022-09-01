import com.sun.javaws.exceptions.ErrorCodeResponseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class ExtraStructure {
    public final List<String[]> majorityRewritting;

    public Set<String> firstDepends;
    public Set<String> depends;

    public ExtraStructure(String[] rewritingVariant) {
        this.majorityRewritting = new ArrayList<>(Collections.singleton(rewritingVariant));
        this.depends = null;
    }

}

public class Parser {

    public static Map<String, ExtraStructure> rules;
    public static Set<String> used;
    public static Map<String, Set<String>> majority;
    public static Set<String> noRgNoterminals;
    public String[] inputData;

    protected Parser(String path) throws IOException {
        readFromFile(path);
        used = new HashSet<>();
        rules = new HashMap<>();
        majority = new HashMap<>();
        noRgNoterminals = new HashSet<>();
        parse(inputData);
        Set<String> stack = new HashSet<>();
        Queue<String> newNoRgNoterminals = new PriorityQueue<>(rules.size());
        for (String noterminal : rules.keySet()) {
            Set<String> firstDepends = new HashSet<>();
            for (String[] rewritingRule : rules.get(noterminal).majorityRewritting) {
                if (rewritingRule.length == 1) {
                    firstDepends.add(rewritingRule[0]);
                } else {
                    for (String exprRule : rewritingRule) {
                        if (exprRule.matches("^[A-Z][0-9]*$")) {
                            firstDepends.add(exprRule);
                        }
                    }
                }
            }
            rules.get(noterminal).firstDepends = firstDepends;
        }
        for (String noterminal : rules.keySet()) {
            if (rules.get(noterminal).depends == null) {
                dfs1(noterminal, stack, newNoRgNoterminals);
            }
        }
        while (!newNoRgNoterminals.isEmpty()) {
            String noRgNoterminal = newNoRgNoterminals.poll();
            for (String noterminal : rules.keySet()) {
                if (!noRgNoterminals.contains(noterminal) && !noterminal.equals(noRgNoterminal)
                        && rules.get(noterminal).depends.contains(noRgNoterminal)) {
                    newNoRgNoterminals.add(noterminal);
                }
            }
            noRgNoterminals.add(noRgNoterminal);
        }
        for (String noterminal : rules.keySet()) {
            if (!noRgNoterminals.contains(noterminal)) {
                majority.put(noterminal,  new HashSet<>(rules.get(noterminal).depends));
            }
        }
        Set<String> rgNoterminals = new HashSet<>(majority.keySet());
        for (String noterminal : rgNoterminals) {
            majority.get(noterminal).add(noterminal);
        }
        for (String noterminal1 : rgNoterminals) {
            if (majority.containsKey(noterminal1)) {
                for (String noterminal2 : rgNoterminals) {
                    if (!noterminal1.equals(noterminal2) && majority.containsKey(noterminal2)
                            && majority.get(noterminal1).contains(noterminal2)) {
                        majority.remove(noterminal2);
                    }
                }
            }
        }
    }

    private void readFromFile(String path) throws IOException {
        Scanner inputData = new Scanner(new FileReader(path));
        List<String> dataLines = new ArrayList<>();
        while (inputData.hasNext()) {
            dataLines.add(inputData.nextLine());
        }
        String[] newData = dataLines.stream().filter(s -> !s.isEmpty()).toArray(String[]::new);
        this.inputData = new String[newData.length];
        System.arraycopy(newData, 0, this.inputData, 0, this.inputData.length);
    }

    private static void parse(String[] input) {
        for (String s : input) {
            if(!s.matches("^[A-Z][0-9]*\\s*->\\s*[a-z](\\s*([A-Z][0-9]*)|[a-z])*$")){
                throw new Error("грамматика не удовлетворяет требуемому условию\n" +
                        "Правило " + s + " не квази-GNF");
            }
            s = s.replaceAll("\\s", "");
            int indRight = s.indexOf("->");
            String left = s.substring(0, indRight);
            String right = s.substring(indRight + 2);
            List<String> rightAll = new ArrayList<>();
            StringBuilder rightSB = new StringBuilder(right);
            while (rightSB.length() != 0) {
                if (String.valueOf(rightSB.charAt(0)).matches("[A-Z]")) {
                    int endInd = 1;
                    while (endInd < rightSB.length()
                            && String.valueOf(rightSB.charAt(endInd)).matches("[0-9]")) {
                        endInd++;
                    }
                    String noterminal = rightSB.substring(0, endInd);
                    rightSB.delete(0, endInd);
                    rightAll.add(noterminal);
                    used.add(noterminal);
                } else {
                    int endIndexOfUnit = 1;
                    while (endIndexOfUnit < rightSB.length()
                            && String.valueOf(rightSB.charAt(endIndexOfUnit)).matches("[a-z]")) {
                        endIndexOfUnit++;
                    }
                    String term = rightSB.substring(0, endIndexOfUnit);
                    rightSB.delete(0, endIndexOfUnit);
                    rightAll.add(term);
                }
            }
            String[] newRight = rightAll.toArray(new String[0]);
            if (newRight.length > 2) {
                noRgNoterminals.add(left);
            }
            ExtraStructure rightES = rules.get(left);
            if (rightES != null) {
                rightES.majorityRewritting.add(newRight);
            } else {
                rules.put(left, new ExtraStructure(newRight));
            }
        }
        if (used.contains("S")) {
            throw new Error("Начальный S есть в правой части");
        } else if (!rules.containsKey("S")) {
            throw new Error("Нет S");
        } else if (used.size() != rules.size() - 1) {
            throw new Error("Неправильное количество правил");
        }
    }

    private static void dfs1(String noterminal, Set<String> stack, Queue<String> newNoRgRules) {
        ExtraStructure es = rules.get(noterminal);
        stack.add(noterminal);
        Set<String> firsDepends = es.firstDepends;
        Set<String> fullDepends = new HashSet<>(firsDepends);
        for (String expr : firsDepends) {
            if (expr.matches("^[A-Z][0-9]*$") && !stack.contains(expr)) {
                if (rules.get(expr).depends == null) {
                    dfs1(expr, stack, newNoRgRules);
                }
                fullDepends.addAll(new HashSet<>(rules.get(expr).depends));
                if (noRgNoterminals.contains(expr)) {
                    newNoRgRules.add(noterminal);
                }
            }
        }
        es.depends = fullDepends;
        stack.remove(noterminal);
    }

}
