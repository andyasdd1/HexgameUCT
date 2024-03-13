import java.util.HashMap;
import java.util.Map;

public class HexBoard {
    private int[][] offsetBoard; // The board representation used for displaying (offset coordinates)
    private Map<HexCoordinate, Integer> axialBoard; // The board representation used for calculations (axial coordinates)
    private int size;
    public HexBoard() {
        this.axialBoard = new HashMap<>();
        this.size = 7;
    }

    public int getSize (){
        return this.size;
    }
     public void reset (){
        axialBoard.clear();
     }
    public static HexCoordinate[] getNeighborCoordinates(HexCoordinate hex) {
        HexCoordinate[] neighbors = new HexCoordinate[6];
        int[][] directions = {{0, -1}, {1, -1}, {1, 0}, {0, 1}, {-1, 1}, {-1, 0}};
    
        for (int i = 0; i < directions.length; i++) {
            int[] dir = directions[i];
            int newQ = hex.getQ() + dir[0];
            int newR = hex.getR() + dir[1];
            HexCoordinate checker = new HexCoordinate(newQ, newR);
            // Check if the new coordinates are within the boundaries
            if (isValidHex(checker)) {
                neighbors[i] = new HexCoordinate(newQ, newR);
            } else {
                neighbors[i] = null; // Or some other way of marking invalid neighbors
            }
        }
    
        return neighbors;
    }

    public void placeStone(HexCoordinate coordinate, int stone){
        this.axialBoard.put(coordinate, stone);
    }

    public static boolean isValidHex(HexCoordinate check) {
        // Define the boundaries for q and r based on your board's shape and size
        // For a 7x7 rhombus, this will depend on the specific layout of the hexes
        // The following is an example and might need to be adjusted
        int absQ = Math.abs(check.getQ());
        int absR = Math.abs(check.getR());
        return absQ <= 3 && absR <= 3 && absQ + absR <= 3;
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

    public static int getDistance(HexCoordinate hex1, HexCoordinate hex2) {
        return (Math.abs(hex1.getQ() - hex2.getQ()) 
                + Math.abs(hex1.getQ() + hex1.getR() - hex2.getQ() - hex2.getR()) 
                + Math.abs(hex1.getR() - hex2.getR())) / 2;
    }

    public HexBoard(int[][] offsetBoard) {
        this.offsetBoard = offsetBoard;
        this.axialBoard = new HashMap<>();
        convertOffsetToAxial();
    }

    public Map<HexCoordinate, Integer> getAxialBoard (){
        return axialBoard;
    }

    public static HexCoordinate fromOffsetToAxialFlat(int x, int y) {
        int q = x - (y / 2); // Adjust q based on how far down y you've gone
        int r = y;
        return new HexCoordinate(q, r);
    }
    
    public Location convertAxialToOffset(HexCoordinate axialCoord) {
        int x = axialCoord.getQ() + (axialCoord.getR() / 2); // Compensate for the shift in q for every row down you go
        int y = axialCoord.getR();
        return new Location(x, y);
    }

    private void convertOffsetToAxial() {
        for (int y = 0; y < offsetBoard.length; y++) {
            for (int x = 0; x < offsetBoard[y].length; x++) {
                HexCoordinate axialCoord = HexCoordinate.fromOffsetToAxialFlat(x, y);
                axialBoard.put(axialCoord, offsetBoard[y][x]);
            }
        }
    }
    
    public void printBoardContents() {
        System.out.println("Axial Board Contents:");
        for (Map.Entry<HexCoordinate, Integer> entry : axialBoard.entrySet()) {
            HexCoordinate key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("Hex at (" + key.getQ() + ", " + key.getR() + ") = " + value);
        }
    }
    
    public void printOffsetBoardContents() {
        System.out.println("Offset Board Contents:");
        for (int y = 0; y < offsetBoard.length; y++) {
            for (int x = 0; x < offsetBoard[y].length; x++) {
                System.out.print(offsetBoard[y][x] + " ");
            }
            System.out.println();
        }
    }
    
    // Checks if a hex at a given axial coordinate is empty (not occupied)
    public boolean isEmpty(HexCoordinate hex) {
        boolean a = axialBoard.getOrDefault(hex, -1) == 0; 
        return a;
    }

}
