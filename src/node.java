import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

// Java doesn't have a direct equivalent of Python's 'inf', but we can use Double.POSITIVE_INFINITY
final double INF = Double.POSITIVE_INFINITY;

class Node {
    Integer move;
    Node parent;
    int N = 0; // times this position was visited
    double Q = 0; // average reward (wins-losses) from this position
    ArrayList<Node> children = new ArrayList<>();
    String outcome = Gamestate.dicMap.get("None");

    public Node(Integer move, Node parent) {
        this.move = move;
        this.parent = parent;
    }

    void addChildren(ArrayList<Node> children) {
        this.children.addAll(children);
    }

    void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    double value(double explore) {
        if (this.N == 0) {
            return explore == 0 ? 0 : INF;
        } else {
            return this.Q / this.N + explore * Math.sqrt(2 * Math.log(this.parent.N) / this.N);
        }
    }
}

