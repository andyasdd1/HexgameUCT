/* Copyright 2012 David Pearson.
 * BSD License.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of a bridge on a Hex board.
 *
 * @author David Pearson
 */
public class Bridge {
    public HexCoordinate l1;
    public List<HexCoordinate> l2;
    public int dir;

    public Bridge(HexCoordinate l1, int dir) {
        this.l1 = l1;
        this.l2 = new ArrayList<>();
        this.dir = dir;
    }

    public void addPotentialBridgeEnd(HexCoordinate end) {
        l2.add(end);
    }

	public List<HexCoordinate> getl2s (){
		return l2;
	}

	
}