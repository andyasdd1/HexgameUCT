import java.util.ArrayList;
import java.util.List;

public class HexCoordinate {
    private int q; // axial column
    private int r; // axial row

    // Constructor for axial coordinates
    public HexCoordinate(int q, int r) {
        this.q = q;
        this.r = r;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public static HexCoordinate fromOffsetToAxialFlat(int x, int y) {
        int q = x - (y / 2); // Adjust q based on how far down y you've gone
        int r = y;
        return new HexCoordinate(q, r);
    }

    // Overridden equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HexCoordinate that = (HexCoordinate) o;
        return q == that.q && r == that.r;
    }

    // Overridden hashCode method
    @Override
    public int hashCode() {
        return 31 * q + r; // A simple prime number hash code
    }
    
    // toString method for easy representation, useful in debugging and logging
    @Override
    public String toString() {
        return "HexCoordinate{" +
                "q=" + q +
                ", r=" + r +
                '}';
    }

    public List<Bridge> getBridges(HexCoordinate originalLocation) {
        List<Bridge> bridges = new ArrayList<>();
    
        // The six directions in which a bridge can potentially be formed from a given hex in axial coordinates
        int[][] bridgeDirections = {
            {2, -2},  // Northeast
            {2, 0},   // East
            {2, 2},   // Southeast
            {-2, 2},  // Southwest
            {-2, 0},  // West
            {-2, -2}, // Northwest
        };
    
        // For each direction, calculate the potential bridge end points and add them to the list in 'l2'
        for (int i = 0; i < bridgeDirections.length; i++) {
            int[] dir = bridgeDirections[i];
            
            // The first potential bridge end point, skipping one hex
            HexCoordinate bridgeEnd1 = new HexCoordinate(originalLocation.getQ() + (2 * dir[0]), originalLocation.getR() + (2 * dir[1]));
            if (isValidBridgeEnd(bridgeEnd1)) {
                // Add potential bridge end points to a new Bridge object
                Bridge bridge = new Bridge(originalLocation, i);
                bridge.addPotentialBridgeEnd(bridgeEnd1);
                
                // Additional potential bridge end points can be added here if necessary
                bridges.add(bridge);
            }
            
        }
    
        return bridges;
    }
    

    private boolean isValidBridgeEnd(HexCoordinate end) {
        // Implement logic to check if the potential end is within the board bounds
        // and is unoccupied. This is pseudo-code and needs actual board data.
        HexBoard board = new HexBoard();
        boolean empty = board.isEmpty(end);
        boolean inbound = HexBoard.isValidHex(end);
        return inbound && empty;
    }

}
//read this file can you tell me how did he choose when to control the ai to explore and when to Exploration vs. Exploitation Balance