import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    @FXML
    private Button implRank;
    @FXML
    private Button implSize;
    @FXML
    private Button implCompr;
    @FXML
    private Button implHalf;
    @FXML
    private Button implSplit;
    @FXML
    private TextField createN;
    @FXML
    private TextField union1;
    @FXML
    private TextField union2;
    @FXML
    private Pane playground;
    @FXML
    private Label log;
    @FXML
    private Button exitBtn;

    /**
     * The following setImpl- functions ale used to set the UF implementation according to the user's choice,
     * click on the button (implRank, implSize, implCompr, implHalf, implSlit)
     * calls the corresponding function (setImplRank, setImplSize, setImplCompr, setImplHalf, setImplSplit).
     * So the chosen implementation is set and the button style changes to show the current implementation.
     * The current implementation is shown to the user on the log panel as well.
     */
    @FXML
    void setImplRank() {
        UFV.implU = UFV.implsU.byRank;
        setImplBtnUStyle(implRank);
        implLog();
    }

    @FXML
    void setImplSize() {
        UFV.implU = UFV.implsU.bySize;
        setImplBtnUStyle(implSize);
        implLog();
    }

    @FXML
    void setImplCompr() {
        UFV.implF = UFV.implsF.pathCompr;
        setImplBtnFStyle(implCompr);
        implLog();
    }

    @FXML
    private void setImplHalf() {
        UFV.implF = UFV.implsF.pathHalf;
        setImplBtnFStyle(implHalf);
        implLog();
    }
    @FXML
    void setImplSplit() {
        UFV.implF = UFV.implsF.pathSplit;
        setImplBtnFStyle(implSplit);
        implLog();
    }


    /**
     * The createBtn Button calls the clickOnCreate() function, which
     * checks if the input from the createN TextField (number N of nodes) is valid,
     * creates N nodes, each having unique id and color,
     * calls function addNodes() to draw the nodes,
     * and finally shows the user message on the log panel telling that N nodes were created.
     */
    @FXML
    void clickOnCreate() {
        Integer number;
        try {
            number = Integer.parseInt(createN.getText());
        } catch (NumberFormatException e) {
            log.setText("Number value should be numeric.");
            return;
        }

        List<Color> colors = generateNColors(number);
        UFV.nodes = new ArrayList<>();
        UFV.N = number;
        for (int i = 0; i < number; i++) {
            UFV.nodes.add(new Node(i, colors.get(i)));
        }

        addNodes();

        log.setText("Added " + number + " nodes");
    }


    /**
     * The unionBtn Button calls the clickOnUnion() function, which
     * checks if there are nodes to union,
     * checks if the input from the union1 TextField and union2 TextField (nodes ids) is valid,
     * checks if the nodes with chosen ids exist,
     * does UF operation,
     * redraws the nodes,
     * and finally shows the user message on the log panel telling that UF operation on the chosen nodes was completed.
     */
    @FXML
    void clickOnUnion() {
        if (UFV.N == 0) {
            log.setText("Add the nodes first.");
            return;
        }
        Integer node1id;
        Integer node2id;
        try {
            node1id = Integer.parseInt(union1.getText());
            node2id = Integer.parseInt(union2.getText());
        } catch (NumberFormatException e) {
            log.setText("Number value should be numeric.");
            return;
        }

        Integer finalNode1id = node1id;
        Integer finalNode2id = node2id;
        Node node1 = UFV.nodes.stream().filter(e -> e.getId().equals(finalNode1id)).findFirst().orElse(null);
        Node node2 = UFV.nodes.stream().filter(e -> e.getId().equals(finalNode2id)).findFirst().orElse(null);
        if (node1 == null || node2 == null) {
            log.setText("Choose the existing nodes.");
            return;
        }

        union(node1, node2);

        updNodes();

        log.setText("Union: node " + node1.getId() + " and node " + node2.getId());
    }


    /**
     * The exitBtn Button calls exit() function.
     * Closes the apllication.
     */
    @FXML
    void exit() {
        Platform.exit();
    }


    /**
     * Union operation using one of the Union by Rank and Union by Size algorithms
     * according to the current implementation.
     * Finds the roots of node1 and node2,
     * checks if node1 and node2 have the same root, so if they are in the same set already,
     * if not, merges them according to the algorithm,
     * updates the size and rank of the node1 root,
     * updated the color and treeId of the nodes from the resulting set.
     * @param node1 the first node
     * @param node2 the second node
     */
    void union(Node node1, Node node2) {
        Node root1 = find(node1);
        Node root2 = find(node2);
        if (root1.getId().equals(root2.getId())) return;

        if (UFV.implU.equals(UFV.implsU.byRank)) {
            if (root1.getRank().compareTo(root2.getRank()) < 0) {
                Node temp = root1;
                root1 = root2;
                root2 = temp;
            }
        }
        else if (UFV.implU.equals(UFV.implsU.bySize)) {
            if (root1.getSize().compareTo(root2.getSize()) < 0) {
                Node temp = root1;
                root1 = root2;
                root2 = temp;
            }
        }

        root2.setParent(root1);
        updTreeColor(root1, root2);
        updTreeId(root2.getTreeId(), root1.getTreeId());
        root1.setSize(root1.getSize() + root2.getSize());
        if (root1.getRank().equals(root2.getRank())) {
            root1.setRank(root1.getRank() + 1);
        }
    }

    /**
     * Find operation using one of the Find by Path Compression, Find by Path Halving, FInd by Path Splitting
     * according to the current implementation.
     * @param node the node which root to be found
     * @return the root of the node
     */
    Node find(Node node) {
        if (UFV.implF.equals(UFV.implsF.pathHalf)) {
             while (!node.getParent().getId().equals(node.getId())) {
                 node.setParent(node.getParent().getParent());
                 node = node.getParent();
             }
        }
        if (UFV.implF.equals(UFV.implsF.pathSplit)) {
            while (!node.getParent().getId().equals(node.getId())) {
                Node next = node.getParent();
                node.setParent(next.getParent());
                node = next;
            }
        }
        // UFV.implF = pathCompr (default)
        if (!node.getParent().getId().equals(node.getId())) {
            node.setParent(find(node.getParent()));
        }
        return node.getParent();
    }

    /**
     * Draws the just created nodes on the playground Pane.
     */
    void addNodes() {
        playground.getChildren().removeAll(playground.getChildren());
        double cellW = playground.getWidth() / UFV.nodes.size();
        int radius = getRadius();
        for (Node node : UFV.nodes) {
            Circle c = new Circle( cellW * node.getId() + radius, radius, radius, node.getColor());
            c.setId(node.getId().toString());
            Label t = createLabel(node, radius, c, cellW * node.getId(), 0);
            playground.getChildren().addAll(c, t);
        }
    }

    /**
     * Draws the nodes after UF operation on the playground Pane.
     */
    void updNodes() {
        playground.getChildren().removeAll(playground.getChildren());

        double x = playground.getWidth() / UFV.nodes.size();
        int order = 0;
        int radius = getRadius();
        Map<Integer, List<Node>> grByTree = UFV.nodes.stream().collect(Collectors.groupingBy(Node::getTreeId));
        for (Integer treeId : grByTree.keySet()) {
            Map<Integer, List<Node>> grTreeByRank = grByTree.get(treeId).stream().collect(Collectors.groupingBy(Node::getRank));
            int maxTreeWidth = 0;
            int maxTreeHeiht = grTreeByRank.keySet().stream().max((Comparator.naturalOrder())).orElse(0);
            for (Integer rank : grTreeByRank.keySet().stream().sorted((Comparator.reverseOrder())).collect(Collectors.toSet())) {
                List<Node> nodes = grTreeByRank.get(rank);
                if (nodes.size() > maxTreeWidth) maxTreeWidth = nodes.size();
                int suborder = order;
                for (Node node : nodes) {
                    Line l = new Line();
                    if (!node.getParent().getId().equals(node.getId())) {
                        l = new Line(x * suborder + radius, radius + 3 * radius * (maxTreeHeiht - rank),
                                x * order + radius, radius + 3 * radius * (maxTreeHeiht - rank - 1));
                        l.setStroke(node.getColor());
                        l.setStrokeWidth(3);
                    }
                    Circle c = new Circle(x * suborder + radius, radius + 3 * radius * (maxTreeHeiht - rank), radius, node.getColor());
                    c.setId(node.getId().toString());
                    Label t = createLabel(node, radius, c, x * suborder, 3 * radius * (maxTreeHeiht - rank));
                    playground.getChildren().addAll(l, c, t);
                    suborder++;
                }
            }
            order += maxTreeWidth;
        }
    }

    /* Utility functions */

    private void setImplBtnUStyle(Button btn) {
        implRank.getStyleClass().remove("menu-button-active");
        implSize.getStyleClass().remove("menu-button-active");
        btn.getStyleClass().add("menu-button-active");
    }

    private void setImplBtnFStyle(Button btn) {
        implCompr.getStyleClass().remove("menu-button-active");
        implHalf.getStyleClass().remove("menu-button-active");
        implSplit.getStyleClass().remove("menu-button-active");
        btn.getStyleClass().add("menu-button-active");
    }

    private List<Color> generateNColors(int colorsNum) {
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < 360; i += 360 / colorsNum) {
            colors.add(Color.hsb(i+1, 0.6, 0.9));
        }
        return colors;
    }

    private void updTreeId(Integer childTreeId, Integer parentTreeId) {
        for (Node node : UFV.nodes) {
            if (node.getTreeId().equals(childTreeId)) {
                node.setTreeId(parentTreeId);
            }
        }
    }

    private void updTreeColor(Node child, Node parent) {
        for (Node node : UFV.nodes) {
            if (node.getTreeId().equals(child.getTreeId())) {
                node.setColor(parent.getColor());
            }
        }
    }

    private Label createLabel(Node node, int radius, Circle c, double x, double y) {
        Label t = new Label(node.getId().toString());
        t.setLabelFor(c);
        t.setMinSize(radius*2, radius*2);
        t.setLayoutX(x);
        t.setLayoutY(y);
        t.setAlignment(Pos.CENTER);
        t.setTextAlignment(TextAlignment.CENTER);
        t.setFont(Font.font("Consolas", radius));
        t.setTextFill(Color.WHITE);
        return t;
    }

    private int getRadius() {
        if (UFV.N > 20) return 10;
        if (UFV.N > 15) return 15;
        if (UFV.N > 10) return 20;
        return 30;
    }

    private void implLog() {
        StringBuilder text = new StringBuilder("Union-Find implementation: ");
        switch (UFV.implU) {
            case byRank:
                text.append("Union by rank");
                break;
            case bySize:
                text.append("Union by size");
                break;
        }
        switch (UFV.implF) {
            case pathCompr:
                text.append(", Find by Path Compression");
                break;
            case pathHalf:
                text.append(", Find by Path Halving");
                break;
            case pathSplit:
                text.append(", Find by Path Slplitting");
                break;
        }
        log.setText(text.toString());
    }
}
