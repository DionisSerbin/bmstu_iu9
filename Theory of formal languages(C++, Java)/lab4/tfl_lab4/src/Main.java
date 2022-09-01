import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private enum type {
        RIGHT_LINEAR,
        LEFT_LINEAR
    }
    public static Map<String, String> ans;
    public static Set<String> tokens;
    public static Map<String, List<List<String>>> rules;
    public static Set<String> used;
    public static Set<String> right;
    public static Set<String> left;
    public static Set<String> closure;
    public static String[] inputData;

    // /Users/denisserbin/tfl_lab4/test1.txt
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter path to test: ");
        String pathToTest = in.nextLine();
        makeDesision(pathToTest);
        System.out.println("Ответ: ");
        for (String tokens : ans.keySet()) {
            System.out.println(tokens + " " + ans.get(tokens));
        }
    }

    public static void readFromFile(String path) throws IOException {
        Scanner input = new Scanner(new FileReader(path));
        List<String> dataLines = new ArrayList<>();
        while (input.hasNext()) {
            dataLines.add(input.nextLine());
        }
        String[] newData = dataLines.stream().filter(s -> !s.isEmpty()).toArray(String[]::new);
        inputData = new String[newData.length];
        System.arraycopy(newData, 0, inputData, 0, newData.length);
    }

    public static void parsing(String path) throws IOException {
        readFromFile(path);

        rules = new HashMap<>();
        used = new HashSet<>();

        for (String l: inputData) {
            if (!l.matches("\\[[A-Za-z]+]\\s*::=\\s*(([a-z0-9_*+=()$;:]|(\\[[A-Za-z]+]))\\s*)+")) {
                throw new Error("Не правильное обьявление правила " + l);
            }
            l = l.replaceAll("\\s", "");
            int ind = l.indexOf("::=");
            String noterm = l.substring(0, ind);
            List<String> parsed = new ArrayList<>();
            StringBuilder rwr = new StringBuilder(l.substring(ind + 3));
            while (rwr.length() != 0) {
                if (rwr.charAt(0) == '[') {
                    int closeInd = rwr.indexOf("]") + 1;
                    String noterminal = rwr.substring(0, closeInd);
                    parsed.add(noterminal);
                    used.add(noterminal);
                    rwr.delete(0, closeInd);
                } else {
                    parsed.add(rwr.substring(0, 1));
                    rwr.deleteCharAt(0);
                }
            }
            if (rules.containsKey(noterm)) {
                rules.get(noterm).add(parsed);
            } else {
                rules.put(noterm, new ArrayList<>(Collections.singleton(parsed)));
            }
        }

        for (String noterm: used) {
            if (!rules.containsKey(noterm)) {
                throw new Error("Обявления правила для нетерминала" + noterm + "не найдено");
            }
        }

        Map<String, Set<String>> dpnd = new HashMap<>();
        for (String noterm: rules.keySet()) {
            if (!dpnd.containsKey(noterm)) {
                Set<String> stackTrace = new HashSet<>(Collections.singleton(noterm));
                dfsDepence(noterm, dpnd, stackTrace);
            }
        }
        left = getRegularNonterms(type.LEFT_LINEAR, dpnd);
        right = getRegularNonterms(type.RIGHT_LINEAR, dpnd);
        closure = new HashSet<>();

        while (true) {
            boolean boolChanged = false;
            for (String noterm : rules.keySet()) {
                boolean boolNoterm = left.contains(noterm)
                        || right.contains(noterm)
                        || closure.contains(noterm);
                if (!boolNoterm) {
                    boolean isReg = true;
                    for (String rewritingNonterm : dpnd.get(noterm)) {
                        boolean boolRewr = left.contains(rewritingNonterm)
                                || right.contains(rewritingNonterm)
                                || closure.contains(rewritingNonterm);
                        if (!boolRewr) {
                            isReg = false;
                        }
                    }
                    if (isReg) {
                        closure.add(noterm);
                        boolChanged = true;
                    }
                }
            }
            if (!boolChanged) {
                break;
            }
        }

        Map<String, String> protectd = new HashMap<>();
        for (String noterm : rules.keySet()) {
            if (!left.contains(noterm) && !right.contains(noterm)) {
                for (List<String> rwr : rules.get(noterm)) {
                    for (int i = 0; i < rwr.size(); i++) {
                        String term = rwr.get(i);
                        if (term.matches("[a-z0-9_*+=()$;:]")) {
                            Optional<String> protecting = Optional.ofNullable(protectd.get(term));
                            if (protecting.isPresent()) {
                                rwr.set(i,protecting.get());
                            } else {
                                String newProtecting = "[$Protected$ " + term + "]";
                                protectd.put(term, newProtecting);
                                rwr.set(i, newProtecting);
                            }
                        }
                    }
                }
            }
        }
        for (String termProtect : protectd.keySet()) {
            String newProtecting = protectd.get(termProtect);
            List<List<String>> newRewriting = new ArrayList<>();
            newRewriting.add(new ArrayList<>(Collections.singleton(termProtect)));
            rules.put(newProtecting, newRewriting);
            right.add(newProtecting);
            left.add(newProtecting);
        }

        System.out.println("Правила:");
        for (String nonterm : rules.keySet()) {
            System.out.println(nonterm + ": " + rules.get(nonterm));
        }
        if (right.size() > 0) {
            System.out.print("Правые регулярные: ");
            System.out.println(right);
        }
        if(left.size() > 0) {
            System.out.print("Левые регулярные: ");
            System.out.println(left);
        }
        if (closure.size() > 0) {
            System.out.print("Замыкание: ");
            System.out.println(closure);
        }
    }

    private static Set<String> getRegularNonterms(final type type, final Map<String, Set<String>> dpnd) {
        Set<String> regular = new HashSet<>();
        Set<String> noRegular = new HashSet<>();
        Queue<String> newNoRegular = new PriorityQueue<>(rules.size());
        for (String noterm : rules.keySet()) {
            for (List<String> rwr : rules.get(noterm)) {
                boolean chain = rwr.size() != 1 || !(rwr.get(0).matches("\\[[A-Za-z]+]") || rwr.get(0).startsWith("[$Protected$"));

                if (chain) {
                    boolean boolReg;
                    if (rwr.size() == 1) {
                        boolReg = !rwr.get(0).matches("[a-z0-9_*+=()$;:]");
                    } else if (rwr.size() == 2){
                        boolReg = (type.equals(type.RIGHT_LINEAR) &&
                                !(rwr.get(0).matches("[a-z0-9_*+=()$;:]") &&
                                        (rwr.get(1).matches("\\[[A-Za-z]+]") || rwr.get(1).startsWith("[$Protected$")))) ||
                                (type.equals(type.LEFT_LINEAR) &&
                                        !(rwr.get(1).matches("[a-z0-9_*+=()$;:]") &&
                                                (rwr.get(0).matches("\\[[A-Za-z]+]") || rwr.get(0).startsWith("[$Protected$"))));
                    } else boolReg = true;
                    if(boolReg) {
                        newNoRegular.add(noterm);
                    }
                }
            }
        }

        while (!newNoRegular.isEmpty()) {
            String noRegNoTerm = newNoRegular.poll();
            for (String nonterm: rules.keySet()) {
                if (!noRegular.contains(nonterm) && !newNoRegular.contains(nonterm) && dpnd.get(nonterm).contains(noRegNoTerm)) {
                    newNoRegular.add(nonterm);
                }
            }
            noRegular.add(noRegNoTerm);
        }
        for (String noterm : rules.keySet()) {
            if (!noRegular.contains(noterm)) {
                regular.add(noterm);
            }
        }
        return  regular;
    }

    private static void dfsDepence(String nonterm, Map<String, Set<String>> dpnd, Set<String> stack) {
        Set<String> notermDpnd = new HashSet<>();
        for (List<String> rwr : rules.get(nonterm)) {
            for (String term : rwr) {
                if ((term.matches("\\[[A-Za-z]+]") || term.startsWith("[$Protected$"))) {
                    notermDpnd.add(term);
                    if (!stack.contains(term)) {
                        if (!dpnd.containsKey(term)) {
                            stack.add(term);
                            dfsDepence(term, dpnd, stack);
                            stack.remove(term);
                        }
                        notermDpnd.addAll(dpnd.get(term));
                    }
                }
            }
        }
        dpnd.put(nonterm, notermDpnd);
    }

    public static void makeToken() {
        tokens = new HashSet<>();
        searching();
        System.out.println("Токены: " + tokens);
    }

    private static void searching() {
        Map<String, Set<String>> follow = followPrecede(true);
        Map<String, Set<String>> precede = followPrecede(false);
        System.out.println("FOLLOW:");
        for (String noterm : follow.keySet()) {
            System.out.println(noterm + " " + follow.get(noterm));
        }
        System.out.println("PRECEDE:");
        for (String noterm : precede.keySet()) {
            System.out.println(noterm + " " + precede.get(noterm));
        }

        Set<String> rg = new HashSet<>(left);
        rg.addAll(right);
        rg.addAll(closure);

        for (String noterm : rg) {
            Set<String> terms = new HashSet<>(follow.getOrDefault(noterm, new HashSet<>()));
            terms.addAll(precede.getOrDefault(noterm, new HashSet<>()));
            Set<String> lang = dfsLang(noterm, new HashSet<>());
            lang.retainAll(terms);
            if (lang.size() == 0) {
                tokens.add(noterm);
            } else {
                System.out.println("Найден конфликт с нетерминалом: " + noterm);
            }
        }
    }

    private static Set<String> dfsLang(String noterm, Set<String> stack) {
        Set<String> lang = new HashSet<>();
        for (List<String> rwr : rules.get(noterm)) {
            if (rwr.size() == 1) {
                String term = rwr.get(0);
                if (term.matches("[a-z0-9_*+=()$;:]")) {
                    lang.add(term);
                } else if (!stack.contains(term)) {
                    stack.add(noterm);
                    lang.addAll(dfsLang(term, stack));
                }
            }
        }
        return lang;
    }

    private static Map<String, Set<String>> followPrecede(boolean follow) {
        Map<String, List<List<String>>> ruless = rules;

        Map<String, List<List<String>>> rulesNow = rules;
        Map<String, Set<String>> oneOrTwo = new HashMap<>();

        for (String noterm : rulesNow.keySet()) {
            for (List<String> rwr : rulesNow.get(noterm)) {
                String generator = follow ? rwr.get(0) : rwr.get(rwr.size() - 1);
                if (generator.matches("[a-z0-9_*+=()$;:]")) {
                    if (oneOrTwo.containsKey(noterm)) {
                        oneOrTwo.get(noterm).add(generator);
                    } else {
                        oneOrTwo.put(noterm, new HashSet<>(Collections.singleton(generator)));
                    }
                }
            }
        }

        while (true) {
            boolean upd = false;
            for (String noterm : rulesNow.keySet()) {
                for (List<String> rwr : rulesNow.get(noterm)) {
                    String generator = follow ? rwr.get(0) : rwr.get(rwr.size() - 1);
                    if ((generator.matches("\\[[A-Za-z]+]") || generator.startsWith("[$Protected$"))) {
                        Set<String> newSet = new HashSet<>(oneOrTwo.getOrDefault(noterm, new HashSet<>()));
                        newSet.addAll(oneOrTwo.getOrDefault(generator, new HashSet<>()));
                        if (newSet.size() > oneOrTwo.getOrDefault(noterm, new HashSet<>()).size()) {
                            upd = true;
                            oneOrTwo.put(noterm, newSet);
                        }
                    }
                }
            }
            if (!upd) {
                break;
            }
        }
        System.out.println(follow ? "FIRST:" : "LAST:");
        for (String nonterm : oneOrTwo.keySet()) {
            System.out.println(nonterm + " " + oneOrTwo.get(nonterm));
        }
        Map<String, Set<String>> resultSet = new HashMap<>();
        resultSet.put("[S]", new HashSet<>(Collections.singleton(follow ? "$" : "^")));

        while (true) {
            boolean upd = false;
            for (String noterm : ruless.keySet()) {
                for (List<String> rwr : ruless.get(noterm)) {
                    for (int i = 0; i < rwr.size(); i++) {
                        String current = rwr.get(i);
                        if ((current.matches("\\[[A-Za-z]+]") || current.startsWith("[$Protected$"))) {
                            Set<String> newSet = new HashSet<>(resultSet.getOrDefault(current, new HashSet<>()));
                            Set<String> joined = new HashSet<>();
                            if ((i == rwr.size() - 1 && follow)
                                    || (i == 0 && !follow)) {
                                joined.addAll(resultSet.getOrDefault(noterm, new HashSet<>()));
                            } else {
                                String makeFollowOrPrecede = rwr.get(
                                        follow ? i + 1 : i - 1
                                );
                                if (makeFollowOrPrecede.matches("[a-z0-9_*+=()$;:]")) {
                                    joined.add(makeFollowOrPrecede);
                                } else {
                                    joined.addAll(oneOrTwo.getOrDefault(makeFollowOrPrecede, new HashSet<>()));
                                }
                            }

                            if (newSet.size() < joined.size()) {
                                upd = true;
                            }
                            newSet.addAll(joined);
                            resultSet.put(current, newSet);
                        }
                    }
                }
            }
            if (!upd) {
                return resultSet;
            }
        }
    }

    public static void makeDesision(String pathToTest) throws IOException {
        ans = new HashMap<>();
        Set<String> limited = new HashSet<>();
        parsing(pathToTest);
        makeToken();

        while (true) {
            boolean flag = false;
            for (String noterm : rules.keySet()) {
                for (List<String> rwrRule : rules.get(noterm)) {
                    boolean ind = false;
                    for (String term : rwrRule) {
                        if (!term.matches("[a-z0-9_*+=()$;:]") && !limited.contains(term)) {
                            ind = true;
                            break;
                        }
                    }
                    if (!ind && !limited.contains(noterm)) {
                        flag = true;
                        limited.add(noterm);
                    }
                }
            }
            if (!flag) {
                break;
            }
        }
        Set<String> rightt = new HashSet<>(right);
        rightt.retainAll(limited);
        ArrayList<Eq> system = new ArrayList<>();
        for (String t : rules.keySet()) {
            if (rightt.contains(t)) {
                ArrayList<String> rg = new ArrayList<>();
                HashMap<String, List<String>> variables = new HashMap<>();
                for (List<String> rwr : rules.get(t)) {
                    if (rwr.size() == 1) {
                        String term = rwr.get(0);
                        if (term.matches("[a-z0-9_*+=()$;:]")) {
                            rg.add(term);
                        } else if (rightt.contains(term)) {
                            if (variables.containsKey(term)) {
                                variables.get(term).add("");
                            } else {
                                variables.put(term, new ArrayList<>(Collections.singleton("")));
                            }
                        }
                    } else {
                        String rwrNt = rwr.get(1);
                        String rwrT = rwr.get(0);
                        if (rightt.contains(rwrNt)) {
                            if (variables.containsKey(rwrNt)) {
                                variables.get(rwrNt).add(rwrT);
                            } else {
                                variables.put(rwrNt, new ArrayList<>(Collections.singleton(rwrT)));
                            }
                        }
                    }
                }
                system.add(new Eq(t, variables, rg));
            }
        }
        Eq[] eqs = system.toArray(new Eq[0]);
        int n = eqs.length;
        if (n != 1) {
            for (int i = 0; i < n - 1; i++) {
                eqs[i].reduce();
                for (int j = i + 1; j < n; j++) {
                    eqs[j].change(eqs[i]);
                }
            }
            for (int i = n - 1; i > 0; i--) {
                eqs[i].reduce();
                for (int j = i - 1; j > -1; j--) {
                    eqs[j].change(eqs[i]);
                }
            }
        } else {
            eqs[0].reduce();
        }
        Map<String, String> res = new HashMap<>();
        for (Eq eq : eqs) {
            res.put(eq.var, String.join("|", eq.rg));
        }

        for (String s : res.keySet()) {
            if (limited.contains(s)) {
                ans.put(s, res.get(s));
            }
        }
        Set<String> noComputedLeft = new HashSet<>(left);
        noComputedLeft.removeAll(rightt);
        for (String s : noComputedLeft) {
            Map<String, List<List<String>>> grammarNow = new HashMap<>();
            dfsMakeRight(s, s, new HashSet<>(), rules, grammarNow);
            Set<String> limited1 = new HashSet<>();

            while (true) {
                boolean flag = false;
                for (String s1 : grammarNow.keySet()) {
                    for (List<String> rwr : grammarNow.get(s1)) {
                        boolean ind = false;
                        for (String term : rwr) {
                            if (!term.matches("[a-z0-9_*+=()$;:]") && !limited1.contains(term)) {
                                ind = true;
                                break;
                            }
                        }
                        if (!ind && !limited1.contains(s1)) {
                            flag = true;
                            limited1.add(s1);
                        }
                    }
                }
                if (!flag) {
                    break;
                }
            }
            Set<String> rgLeft = new HashSet<>(grammarNow.keySet());
            rgLeft.retainAll(limited1);

            ArrayList<Eq> system1 = new ArrayList<>();
            for (String nt1 : grammarNow.keySet()) {
                if (rgLeft.contains(nt1)) {
                    ArrayList<String> rg = new ArrayList<>();
                    HashMap<String, List<String>> variables = new HashMap<>();
                    for (List<String> rwr : grammarNow.get(nt1)) {
                        if (rwr.size() == 1) {
                            String term = rwr.get(0);
                            if (term.matches("[a-z0-9_*+=()$;:]")) {
                                rg.add(term);
                            } else if (rgLeft.contains(term)) {
                                if (variables.containsKey(term)) {
                                    variables.get(term).add("");
                                } else {
                                    variables.put(term, new ArrayList<>(Collections.singleton("")));
                                }
                            }
                        } else {
                            String rwrNt = rwr.get(1);
                            String rwrT = rwr.get(0);
                            if (rgLeft.contains(rwrNt)) {
                                if (variables.containsKey(rwrNt)) {
                                    variables.get(rwrNt).add(rwrT);
                                } else {
                                    variables.put(rwrNt, new ArrayList<>(Collections.singleton(rwrT)));
                                }
                            }
                        }
                    }
                    system1.add(new Eq(nt1, variables, rg));
                }
            }

            Eq[] eqs1 = system1.toArray(new Eq[0]);
            int n1 = eqs1.length;
            if (n1 != 1) {
                for (int i = 0; i < n1 - 1; i++) {
                    eqs1[i].reduce();
                    for (int j = i + 1; j < n1; j++) {
                        eqs1[j].change(eqs1[i]);
                    }
                }
                for (int i = n1 - 1; i > 0; i--) {
                    eqs1[i].reduce();
                    for (int j = i - 1; j > -1; j--) {
                        eqs1[j].change(eqs1[i]);
                    }
                }
            } else {
                eqs1[0].reduce();
            }
            Map<String, String> generatedRegex = new HashMap<>();
            for (Eq eq : eqs1) {
                generatedRegex.put(eq.var, String.join("|", eq.rg));
            }
            ans.put(s, generatedRegex.get("[$Q$]"));
        }
        for (String nonterm : closure) {
            if (limited.contains(nonterm)) {
                if (!ans.containsKey(nonterm)) {
                    dfxClosur(nonterm, rules, limited);
                }
            }
        }
        Set<String> notTokens = new HashSet<>(ans.keySet());
        notTokens.removeAll(tokens);
        for (String notToken : notTokens) {
            ans.remove(notToken);
        }
    }

    private static void dfsMakeRight(String nonterm, String root, Set<String> stackTraceVisited, Map<String, List<List<String>>> baseRules, Map<String, List<List<String>>> newGrammar) {
        if (stackTraceVisited.contains(nonterm)) {
            return;
        }
        for (List<String> rewritingRule : baseRules.get(nonterm)) {
            if (rewritingRule.size() != 1) {
                String term = rewritingRule.get(0);
                stackTraceVisited.add(nonterm);
                dfsMakeRight(term, root, stackTraceVisited, baseRules, newGrammar);
                stackTraceVisited.remove(nonterm);
                if (!newGrammar.containsKey(term)) {
                    newGrammar.put(term, new ArrayList<>());
                }
                newGrammar.get(term).add(Arrays.asList(rewritingRule.get(1), nonterm));
                if (nonterm.equals(root)) {
                    newGrammar.get(term).add(Collections.singletonList(rewritingRule.get(1)));
                }
            } else {
                String term = rewritingRule.get(0);
                if (term.matches("[a-z0-9_*+=()$;:]")) {
                    if (!newGrammar.containsKey("[$Q$]")) {
                        newGrammar.put("[$Q$]", new ArrayList<>());
                    }
                    newGrammar.get("[$Q$]").add(Arrays.asList(term, nonterm));
                    if (nonterm.equals(root)) {
                        newGrammar.get("[$Q$]").add(Collections.singletonList(term));
                    }
                } else {
                    stackTraceVisited.add(nonterm);
                    dfsMakeRight(term, root, stackTraceVisited, baseRules, newGrammar);
                    stackTraceVisited.remove(nonterm);
                    if (!newGrammar.containsKey(term)) {
                        newGrammar.put(term, new ArrayList<>());
                    }
                    newGrammar.get(term).add(Collections.singletonList(nonterm));
                }
            }
        }
    }

    private static void dfxClosur(String nonterm, Map<String, List<List<String>>> rules, Set<String> finiteNonterms) {
        String regex = "";
        for (List<String> rewritingRule : rules.get(nonterm)) {
            StringBuilder ruleRegex = new StringBuilder();
            for (String term : rewritingRule) {
                if (term.matches("[a-z0-9_*+=()$;:]")) {
                    ruleRegex.append(term);
                } else if (finiteNonterms.contains(term)) {
                    if (!ans.containsKey(term)) {
                        dfxClosur(term, rules, finiteNonterms);
                    }
                    ruleRegex.append("(").append(ans.get(term)).append(")");
                } else {
                    ruleRegex = null;
                    break;
                }
            }
            if (ruleRegex != null) {
                regex = regex.equals("") ? ruleRegex.toString() : "(" + regex + ")|" + "(" + ruleRegex + ")";
            }
        }
        ans.put(nonterm, regex);
    }
}
