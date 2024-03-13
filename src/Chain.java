import java.util.ArrayList;
import java.util.List;

public class Chain {
    private int length;
    private List<HexCoordinate> locations;
    private HexCoordinate start; // The hex with the smallest r (and then smallest q in case of a tie)
    private HexCoordinate end; // The hex with the largest r (and then largest q in case of a tie)
    private HexCoordinate right; // The hex with the largest q
    private HexCoordinate left; // The hex with the smallest q
    private List<Bridge> bridgesets;

    public Chain() {
        this.length = 0;
        this.locations = new ArrayList<>();
        this.bridgesets = new ArrayList<>();
        this.start = null;
        this.end = null;
        this.right = null;
        this.left = null;
    }

    // Method to add a location to the chain
    public void addLocation(HexCoordinate location) {
        locations.add(location);
        updateChainAttributes(location);
        length = locations.size();
    }

    // Method to add a bridge to the chain
    public void addBridge(Bridge bridge) {
        bridgesets.add(bridge);
    }

    // Update the start, end, right, and left attributes based on the new location
    private void updateChainAttributes(HexCoordinate location) {
        if (start == null || location.getR() < start.getR() || (location.getR() == start.getR() && location.getQ() < start.getQ())) {
            start = location;
        }
        if (end == null || location.getR() > end.getR() || (location.getR() == end.getR() && location.getQ() > end.getQ())) {
            end = location;
        }
        if (right == null || location.getQ() > right.getQ()) {
            right = location;
        }
        if (left == null || location.getQ() < left.getQ()) {
            left = location;
        }
    }

    public int getLength() {
        return length;
    }

    // Getter for locations
    public List<HexCoordinate> getLocations() {
        return new ArrayList<>(locations); // Return a copy to preserve encapsulation
    }

    // Getter for start
    public HexCoordinate getStart() {
        return start;
    }

    // Getter for end
    public HexCoordinate getEnd() {
        return end;
    }

    // Getter for right
    public HexCoordinate getRight() {
        return right;
    }

    // Getter for left
    public HexCoordinate getLeft() {
        return left;
    }

    // Getter for bridgesets
    public List<Bridge> getBridgesets() {
        return new ArrayList<>(bridgesets); // Return a copy to preserve encapsulation
    }
    // ...
}
