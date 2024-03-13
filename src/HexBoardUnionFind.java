import java.util.HashMap;
import java.util.Map;

public class HexBoardUnionFind {
    private Map<HexCoordinate, HexCoordinate> parent; // Maps each hex to its parent
    private Map<HexCoordinate, Integer> rank; // Tracks the depth of each tree
    //private Map<HexCoordinate, Integer> axialBoard; // Board representation

    private final HexCoordinate virtualStartWhite = new HexCoordinate(Integer.MIN_VALUE, Integer.MIN_VALUE);
    private final HexCoordinate virtualEndWhite = new HexCoordinate(Integer.MAX_VALUE, Integer.MAX_VALUE);
    private final HexCoordinate virtualStartBlack = new HexCoordinate(Integer.MIN_VALUE, Integer.MAX_VALUE);
    private final HexCoordinate virtualEndBlack = new HexCoordinate(Integer.MAX_VALUE, Integer.MIN_VALUE);
    // Constructor
    public HexBoardUnionFind(HexBoard board) {
        //this.axialBoard = check.getAxialBoard();
        this.parent = new HashMap<>();
        this.rank = new HashMap<>();
        initialize(board);
    }

    private void initialize(HexBoard board) {
        // Initialize virtual nodes
        parent.put(virtualStartWhite, virtualStartWhite);
        rank.put(virtualStartWhite, 0);
        parent.put(virtualEndWhite, virtualEndWhite);
        rank.put(virtualEndWhite, 0);
        parent.put(virtualStartBlack, virtualStartBlack);
        rank.put(virtualStartBlack, 0);
        parent.put(virtualEndBlack, virtualEndBlack);
        rank.put(virtualEndBlack, 0);
        
        Map<HexCoordinate, Integer> axialBoard = board.getAxialBoard();
        for (HexCoordinate hex : axialBoard.keySet()) {
            parent.put(hex, hex); // Initial parent is the hex itself
            rank.put(hex, 0); // Initial rank is 0
        }
    }

    public void connectToEdge(HexCoordinate hex, int player) {
        boolean start = isEdgeHex(hex, player, true);
        boolean end = isEdgeHex(hex, player, false);

        if (player == 1) { // White player
            if (start) {
                union(hex, virtualStartWhite);
            }
            if (end) {
                union(hex, virtualEndWhite);
            }
        } else if (player == 2) { // Black player
            if (start) {
                union(hex, virtualStartBlack);
            }
            if (end) {
                union(hex, virtualEndBlack);
            }
        }
    }


    private HexCoordinate find(HexCoordinate hex) {
        if (!parent.get(hex).equals(hex)) {
            parent.put(hex, find(parent.get(hex))); // Path compression
        }
        return parent.get(hex);
    }

    private void union(HexCoordinate hex1, HexCoordinate hex2) {
        HexCoordinate root1 = find(hex1);
        HexCoordinate root2 = find(hex2);

        if (!root1.equals(root2)) {
            if (rank.get(root1) < rank.get(root2)) {
                parent.put(root1, root2);
            } else if (rank.get(root1) > rank.get(root2)) {
                parent.put(root2, root1);
            } else {
                parent.put(root2, root1);
                rank.put(root1, rank.get(root1) + 1);
            }
        }
    }

    public void buildChains(HexBoard board) {
        Map<HexCoordinate, Integer> axialBoard = board.getAxialBoard();
        for (Map.Entry<HexCoordinate, Integer> entry : axialBoard.entrySet()) {
            HexCoordinate hex = entry.getKey();
            Integer player = entry.getValue();
            // Assuming a method to get neighbors given a HexCoordinate
            for (HexCoordinate neighbor : getNeighbors(hex)) {
                if (axialBoard.containsKey(neighbor) && axialBoard.get(neighbor).equals(player)) {
                    union(hex, neighbor);
                }
            }
        }
    }

    // Assuming there exists a method to get neighboring HexCoordinates
    private HexCoordinate[] getNeighbors(HexCoordinate hex) {

        return HexBoard.getNeighborCoordinates(hex); 
    }

    public boolean checkConnected(int player, HexBoard board) {
        Map<HexCoordinate, Integer> axialBoard = board.getAxialBoard();
        // Assume player 1 is white and player 2 is black
        HexCoordinate startEdge = null, endEdge = null;
        for (HexCoordinate hex : axialBoard.keySet()) {
            if (axialBoard.get(hex) == player) {
                if (isEdgeHex(hex, player, true)) { // true for start edge
                    startEdge = find(hex); // Find root of start edge hex
                } else if (isEdgeHex(hex, player, false)) { // false for end edge
                    endEdge = find(hex); // Find root of end edge hex
                }
            }
        }
        
        // Check if start and end edges are connected
        return startEdge != null && endEdge != null && startEdge.equals(endEdge);
    }
    
    private boolean isEdgeHex(HexCoordinate hex, int player, boolean start) {
        // player 1 (white) aims for top and bottom edges
        // player 2 (black) aims for left and right edges
        int q = hex.getQ();
        int r = hex.getR();
    
        if (player == 1) { // White player
            if (start) {
                return r == -3; // Top edge for starting edge
            } else {
                return r == 3; // Bottom edge for ending edge
            }
        } else if (player == 2) { // Black player
            if (start) {
                return q == -3; // Left edge for starting edge
            } else {
                return q == 3; // Right edge for ending edge
            }
        }
        
        return false; // In case of incorrect player number
    }
    
    // Additional methods for game logic and chain identification could follow
}
