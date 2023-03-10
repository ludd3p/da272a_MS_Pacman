package pacman.entries.pacman;

import java.util.ArrayList;

public class Node {
    private String attribute;
    private String attributeValue;
    private String label;
    private ArrayList<Node> children;

    public Node () {

    }
    public Node(String label) {
        this.label = label;
        children = new ArrayList<>();
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
