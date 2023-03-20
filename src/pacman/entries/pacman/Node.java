package pacman.entries.pacman;
import java.util.HashMap;

public class Node {
    private String label;
    private HashMap<String, Node> children;

    public Node () {
        children = new HashMap<>();
    }
    public Node(String label) {
        this.label = label;
        children = new HashMap<>();
    }

    public void addChild(String attribute, Node child) {
        children.put(attribute, child);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public HashMap<String, Node> getChildren() {
        return children;
    }

    public Node getChild(String attribute) {
        return children.get(attribute);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void printTree(int depth, Node node) {
        if (isLeaf()) {
            return;
        }

        // Print the label of the current node
        for (int i = 0; i < depth; i++) {
            System.out.print(" " + i + " ");
        }
        System.out.println(node.getLabel());

        // Recursively print each child of the current node
        HashMap<String, Node> childrenNodes = node.getChildren();
        for (String attribute : childrenNodes.keySet()) {
            Node child = childrenNodes.get(attribute);
            printTree(depth + 1, child);
        }
    }

}