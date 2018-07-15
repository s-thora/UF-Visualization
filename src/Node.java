import javafx.scene.paint.Color;

public class Node {

    private final Integer id;
    private Color color;
    private Integer height;
    private Integer treeId;
    private Node parent;
    private Integer rank;
    private Integer size;

    Node(int id, Color color) {
        this.id = id;
        this.treeId = id;
        this.color = color;
        this.parent = this;
        this.rank = 0;
        this.size = 1;
    }

    Integer getId() {
        return this.id;
    }

    Integer getTreeId() {
        return this.treeId;
    }
    void setTreeId(Integer treeId) { this.treeId = treeId; }

    Color getColor() {
        return this.color;
    }
    void setColor(Color color) {
        this.color = color;
    }

    Node getParent() {
        return this.parent;
    }
    void setParent(Node parent) {
        this.parent = parent;
    }

    Integer getRank() {
        return this.rank;
    }
    void setRank(Integer rank) {
        this.rank = rank;
    }

    Integer getSize() {
        return this.size;
    }
    void setSize (Integer size) {
        this.size = size;
    }

}
