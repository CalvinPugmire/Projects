package spell;

public class Dictionary implements ITrie {
    private TrieNode root;
    private int wordCount;
    private int nodeCount;

    public Dictionary() {
        root = new TrieNode();
        wordCount = 0;
        nodeCount = 1;
    }

    @Override
    public void add(String word) {
        int iter = 0;
        int index = word.charAt(iter)-'a';
        if (root.getChildren()[index] != null) {
            if (iter < word.length()-1) {
                addHelper(word, root.getChildren()[index], iter);
            } else {
                if (root.getChildren()[index].getValue() == 0) {
                    wordCount++;
                }
                root.getChildren()[index].incrementValue();
            }
        } else {
            root.getChildren()[index] = new TrieNode();
            nodeCount++;
            if (iter < word.length()-1) {
                addHelper(word, root.getChildren()[index], iter);
            } else {
                root.getChildren()[index].incrementValue();
                wordCount++;
            }
        }
    }

    public void addHelper(String word, INode node, int iter) {
        iter++;
        int index = word.charAt(iter)-'a';
        if (node.getChildren()[index] != null) {
            if (iter < word.length()-1) {
                addHelper(word, node.getChildren()[index], iter);
            } else {
                if (node.getChildren()[index].getValue() == 0) {
                    wordCount++;
                }
                node.getChildren()[index].incrementValue();
            }
        } else {
            node.getChildren()[index] = new TrieNode();
            nodeCount++;
            if (iter < word.length()-1) {
                addHelper(word, node.getChildren()[index], iter);
            } else {
                node.getChildren()[index].incrementValue();
                wordCount++;
            }
        }
    }

    @Override
    public INode find(String word) {
        int iter = 0;
        int index = word.charAt(iter)-'a';
        if (root.getChildren()[index] != null) {
            if (iter < word.length()-1) {
                return findHelper(word, root.getChildren()[index], iter);
            } else {
                if (root.getChildren()[index].getValue() > 0) {
                    return root.getChildren()[index];
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public INode findHelper(String word, INode node, int iter) {
        iter++;
        int index = word.charAt(iter)-'a';
        if (node.getChildren()[index] != null) {
            if (iter < word.length()-1) {
                return findHelper(word, node.getChildren()[index], iter);
            } else {
                if (node.getChildren()[index].getValue() > 0) {
                    return node.getChildren()[index];
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder currentWord = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toStringHelper(root, currentWord, output);
        return output.toString();
    }

    private void toStringHelper(INode n, StringBuilder currentWord, StringBuilder output) {
        if (n.getValue() > 0) {
            output.append(currentWord.toString());
            output.append("\n");
        }
        for (int i = 0; i < n.getChildren().length; ++i) {
            INode child = n.getChildren()[i];
            if (child != null) {
                char childLetter = (char)('a'+i);
                currentWord.append(childLetter);
                toStringHelper(child, currentWord, output);
                currentWord.deleteCharAt(currentWord.length()-1);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Dictionary d = (Dictionary)obj;
        if (d.wordCount != this.wordCount || d.nodeCount != this.nodeCount) {
            return false;
        }
        return equalsHelper(this.root, d.root);
    }

    private boolean equalsHelper(INode n1, INode n2) {
        if (n1 == null || n2 == null) {
            return n1 == null && n2 == null;
        }
        if (n1.getValue() != n2.getValue()) {
            return false;
        }
        for (int i = 0; i < n1.getChildren().length; i++) {
            if (!equalsHelper(n1.getChildren()[i], n2.getChildren()[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int nodeSum = 0;
        for (int i = 0; i < root.getChildren().length; i++) {
            if (root.getChildren()[i] != null) {
                nodeSum = nodeSum+i;
            }
        }
        return (nodeSum*wordCount*nodeCount*31);
    }
}
