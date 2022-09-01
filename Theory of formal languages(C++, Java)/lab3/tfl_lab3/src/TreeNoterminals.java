import java.util.*;

public class TreeNoterminals {
    public final Tree tree;

    public TreeNoterminals(String noterminal, Parser rules) {
        tree = new Tree(noterminal, rules, noterminal, false, new HashSet<>());
    }

    protected void distrib(Map<String, TreeNoterminals> left, Map<String, Set<String>> subMajor,
                           Set<String> rgNoterminals, Set<String> possiblyRgNoterminals, Set<String> warningNoterminals) {
        if (!tree.isRoot) {
            if (!tree.words.isEmpty()) {
                List<String> list = new ArrayList<>(Collections.singleton(tree.treeExp));
                String subKey1 = null;
                for (String key : subMajor.keySet()) {
                    Set<String> intersection = new HashSet<>(subMajor.get(key));
                    intersection.retainAll(list);
                    if (intersection.size() == list.size()) {
                        subKey1 = key;
                    }
                }
                if (subKey1 != null) {
                    String subKey = null;
                    List<String> subSuff = new ArrayList<>(Collections.singleton(tree.treeExp));
                    for (String key : subMajor.keySet()) {
                        Set<String> mySet = new HashSet<>(subMajor.get(key));
                        mySet.retainAll(subSuff);
                        if (mySet.size() == subSuff.size()) {
                            subKey = key;
                        }
                    }
                    if (subKey != null) {
                        rgNoterminals.add(tree.treeExp);
                    }
                } else {
                    possiblyRgNoterminals.add(tree.treeExp);
                }
                return;
            }
        }
        tree.dfsGraphviz(0);
        String prefix = tree.prefRoot.toString();
        List<String> suffix = tree.suffRoot;
        if (suffix.isEmpty()) {
            if(!tree.words.isEmpty()) {
                rgNoterminals.add(tree.treeExp);
            } else {
                possiblyRgNoterminals.add(tree.treeExp);
            }
            return;
        }
        String regularSubsetKey = null;
        for (String key : subMajor.keySet()) {
            Set<String> mySet = new HashSet<>(subMajor.get(key));
            mySet.retainAll(suffix);
            if (mySet.size() == suffix.size()) {
                regularSubsetKey = key;
                break;
            }
        }
        StringBuilder wordPrefix = new StringBuilder(prefix);
        boolean result;
        boolean flagCheckWord = true;
        while(wordPrefix.length() != 0) {
            result = dfs(left, wordPrefix, suffix, 0);
            if (!result) {
                flagCheckWord = false;
                break;
            }
        }
        if (flagCheckWord) {
            Set<StringBuilder> words = tree.words;
            if (words.isEmpty()) {
                warningNoterminals.add(tree.treeExp);
                return;
            } else {
                for (StringBuilder word : words) {
                    StringBuilder wordWord = new StringBuilder(word.toString());
                    boolean resultWord;
                    boolean flagCheckWordWord = true;
                    while(wordWord.length() != 0) {
                        resultWord = dfs(left, wordWord, suffix, 0);
                        if (!resultWord) {
                            flagCheckWordWord = false;
                            break;
                        }
                    }
                    if (!flagCheckWordWord) {
                        warningNoterminals.add(tree.treeExp);
                        return;
                    }
                }
            }
            if (regularSubsetKey != null && subMajor.get(regularSubsetKey).contains(tree.treeExp)) {
                rgNoterminals.add(tree.treeExp);
            } else {
                possiblyRgNoterminals.add(tree.treeExp);
            }
            return;
        }
        warningNoterminals.add(tree.treeExp);
    }

    private boolean dfs(Map<String, TreeNoterminals> left, StringBuilder s, List<String> suf, int i) {
        String str = suf.get(i);
        if (str.matches("^[A-Z][0-9]*$")) {
            if (s.length() == 0) {
                return false;
            }
            for (StringBuilder prefix : left.get(str).tree.words) {
                StringBuilder sb = new StringBuilder(s.toString());
                boolean flag;
                if (!sb.toString().startsWith(prefix.toString())) {
                    flag = false;
                } else {
                    sb.delete(0, prefix.toString().length());
                    flag = true;
                }
                if (flag && (i + 1 == suf.size() || dfs(left, sb, suf, i + 1))) {
                    s.replace(0, s.length(), sb.toString());
                    return true;
                }
            }
            return false;
        }
        boolean flag;
        if (!s.toString().startsWith(str)) {
            flag = false;
        } else {
            s.delete(0, str.length());
            flag = true;
        }
        boolean returned = flag && (i + 1 == suf.size() || dfs(left, s, suf, i + 1));
        return returned;
    }

}

