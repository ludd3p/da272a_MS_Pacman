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


}
