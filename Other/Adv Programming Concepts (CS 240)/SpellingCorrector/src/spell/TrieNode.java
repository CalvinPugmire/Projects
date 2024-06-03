package spell;

public class TrieNode implements INode {
    private int count;
    private TrieNode[] children;

    public TrieNode() {
        count = 0;
        children  = new TrieNode[26];
    }

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        count++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }
}
