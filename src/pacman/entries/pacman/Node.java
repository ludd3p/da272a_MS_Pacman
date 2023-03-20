package pacman.entries.pacman;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public void printTree(int depth) {
        if (isLeaf()) {
            for (int i = 0; i < depth; i++) {
                System.out.print(" " + (i+1) + " ");
            }
            System.out.println("= " + getLabel());
        }
        ArrayList<Map.Entry<String, Node>> nodeList = new ArrayList<>(children.entrySet());
        for (int i = 0; i < nodeList.size(); i++) {
            for (int j = 0; j < depth; j++) {
                System.out.print(" " + (j+1) + " ");
            }
            if (i == nodeList.size() - 1) {
                System.out.println(label + " = " + nodeList.get(i).getKey());
                nodeList.get(i).getValue().printTree(depth + 1);
            } else {
                System.out.println(label + " = " + nodeList.get(i).getKey());
                nodeList.get(i).getValue().printTree(depth + 1);
            }
        }
    }
}