import java.util.*;

public class Tree {

    public final String treeExp;
    public final boolean isNoterm;
    private List<Boolean> achieveEdge;
    public List<List<Tree>> leftSubTree;
    public List<List<String>> rightSubTree;
    public Set<StringBuilder> words;
    public final boolean isEnd;
    public boolean isRoot;
    public int indLeft;
    public List<String> suffRoot;
    public StringBuilder prefRoot;
    public StringBuilder graphviz;

    public Tree(String noterm, Parser rules, String root, boolean isRoot, Set<String> stack) {
        isNoterm = true;
        achieveEdge = new ArrayList<>();
        treeExp = noterm;
        leftSubTree = new ArrayList<>();
        rightSubTree = new ArrayList<>();
        words = new HashSet<>();
        isEnd = false;
        this.isRoot = isRoot;
        prefRoot = new StringBuilder();
        graphviz = new StringBuilder();
        if (!isRoot) {
            List<String[]> majorityRewrittin = rules.rules.get(noterm).majorityRewritting;
            for (String[] mr : majorityRewrittin) {
                leftSubTree.add(new ArrayList<>());
                rightSubTree.add(new ArrayList<>(Arrays.asList(mr)));
                achieveEdge.add(false);
            }
            boolean isEdgeMaaintain = false;
            for (boolean b : achieveEdge) {
                if (!b) {
                    isEdgeMaaintain = true;
                }
            }
            while (isEdgeMaaintain) {
                isEdgeMaaintain = false;
                for (boolean b : achieveEdge) {
                    if (!b) {
                        isEdgeMaaintain = true;
                    }
                }
                iterLeft(rules, root, stack);
            }
        }
    }

    public Tree(String terms, boolean isEnd) {
        isNoterm = isEnd;
        treeExp = terms;
        this.isEnd = isEnd;
        prefRoot = new StringBuilder();
        graphviz = new StringBuilder();
    }

    private void iterLeft(Parser rules, String root, Set<String> stack) {
        List<Integer> circled = new ArrayList<>();
        for (int i = 0; i < rightSubTree.size(); i++) {
            if (!achieveEdge.get(i) && (!isRoot || i != indLeft)) {
                List<String> right = rightSubTree.get(i);
                if (right.isEmpty()) {
                    Set<StringBuilder> outTerms = new HashSet<>();
                    for (Tree tre : leftSubTree.get(i)) {
                        if (tre.isNoterm) {
                            Set<StringBuilder> subStr = new HashSet<>();
                            Set<StringBuilder> wordsTree = tre.words;
                            if (wordsTree == null) {
                                outTerms = null;
                            }
                            for (StringBuilder wordPrefix : outTerms) {
                                for (StringBuilder wordSuffix : wordsTree) {
                                    subStr.add(new StringBuilder(wordPrefix.toString().concat(wordSuffix.toString())));
                                }
                            }
                            outTerms = subStr;
                        } else {
                            String derivationEnd = tre.treeExp;
                            if (outTerms.isEmpty()) {
                                outTerms.add(new StringBuilder(derivationEnd));
                            } else {
                                for (StringBuilder word : outTerms) {
                                    word.append(derivationEnd);
                                }
                            }
                        }
                    }
                    if (outTerms != null) {
                        words.addAll(outTerms);
                    }
                    achieveEdge.set(i,true);
                } else {
                    String expr = right.get(0);
                    if (expr.matches("^[A-Z][0-9]*$")) {
                        if (stack.contains(expr)) {
                            circled.add(0, i);
                        } else if (!isRoot && expr.equals(root)) {
                            isRoot = true;
                            indLeft = i;
                            achieveEdge.set(i, true);
                            leftSubTree.get(i).add(new Tree(root, true));
                        } else {
                            Set<String> newStack = new HashSet<>(stack);
                            newStack.add(expr);
                            Tree newTree = new Tree(expr, rules, root, false, newStack);
                            if (newTree.leftSubTree.isEmpty()) {
                                circled.add(0, i);
                            } else {
                                if (newTree.isRoot) {
                                    isRoot = true;
                                    indLeft = i;
                                    achieveEdge.set(i, true);
                                }
                                leftSubTree.get(i).add(newTree);
                            }
                        }
                    } else {
                        leftSubTree.get(i).add(new Tree(expr, false));
                    }
                    right.remove(0);
                }
            }
        }
        for (int noRootInd : circled) {
            if (isRoot && noRootInd < indLeft) {
                indLeft--;
            }
            leftSubTree.remove(noRootInd);
            rightSubTree.remove(noRootInd);
            achieveEdge.remove(noRootInd);
        }
    }

    protected int dfsGraphviz(int i) {
        int j = i + 1;
        if (isNoterm) {
            if (isEnd) {
                return j;
            }
            StringBuilder prefix = new StringBuilder();
            if (isRoot) {
                for (Tree prefixTree : leftSubTree.get(indLeft)) {
                    int k = j;
                    j = prefixTree.dfsGraphviz(j);
                    prefix.append(prefixTree.prefRoot);
                    graphviz.append(i);
                    graphviz.append("[label = \"");
                    graphviz.append(treeExp);
                    graphviz.append("\"]\n");
                    graphviz.append(k);
                    graphviz.append("[label = \"");
                    graphviz.append(prefixTree.treeExp);
                    graphviz.append("\"];\n");
                    graphviz.append(i).append(" -> ").append(k).append("\n");
                    graphviz.append(prefixTree.graphviz.toString());
                }
                List<Tree> out = leftSubTree.get(indLeft);

                List<String> suffix = rightSubTree.get(indLeft);
                if (out.get(out.size() - 1).isEnd) {
                    suffRoot = new ArrayList<>(suffix);
                } else {
                    suffRoot = new ArrayList<>(out.get(out.size() - 1).suffRoot);
                    suffRoot.addAll(suffix);
                }
                if (!suffix.isEmpty()) {
                    graphviz.append(i);
                    graphviz.append("[label = \"");
                    graphviz.append(treeExp);
                    graphviz.append("\"]\n");
                    graphviz.append(j);
                    graphviz.append("[label = \"");
                    graphviz.append(String.join("", suffix));
                    graphviz.append("\"];\n");
                    graphviz.append(i).append(" -> ").append(j++).append("\n");
                }
            } else {
                for (Tree prefixTree2 : leftSubTree.get(0)) {
                    int k = j;
                    j = prefixTree2.dfsGraphviz(j);
                    prefix.append(prefixTree2.prefRoot);

                    graphviz.append(i);
                    graphviz.append("[label = \"");
                    graphviz.append(treeExp);
                    graphviz.append("\"]\n");
                    graphviz.append(k);
                    graphviz.append("[label = \"");
                    graphviz.append(prefixTree2.treeExp);
                    graphviz.append("\"];\n");
                    graphviz.append(i).append(" -> ").append(k).append("\n");
                    graphviz.append(prefixTree2.graphviz.toString());
                }
            }
            prefRoot = prefix;
            return j;
        }
        prefRoot = new StringBuilder(treeExp);
        return j;
    }
}
