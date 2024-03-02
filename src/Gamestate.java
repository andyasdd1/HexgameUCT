import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

public class Gamestate {
    private HexBoard board = new HexBoard();
    private int turn = 1;
    private AI ai;
    private HexBoardUnionFind whiteGroups;
    private HexBoardUnionFind blackGroups;
    private Panel gamePanel;
    private boolean isWhiteTurn = true;
    private boolean gameEnded = false;

    public Gamestate() {
        whiteGroups = new HexBoardUnionFind();
        blackGroups = new HexBoardUnionFind();
        gamePanel = new Panel();
        // Initialize your HexBoardUnionFind instances and game panel here
    }

    public void checkWinAndStop() {
        boolean whiteWins = whiteGroups.checkConnected(1); // Assuming 1 represents white
        boolean blackWins = blackGroups.checkConnected(2); // Assuming 2 represents black

        if (whiteWins) {
            System.out.println("White wins!");
            JOptionPane.showMessageDialog(null, "White wins!");
            gameEnded = true;
        } else if (blackWins) {
            System.out.println("Black wins!");
            JOptionPane.showMessageDialog(null, "Black wins!");
            gameEnded = true;
        }
    }

    public void placeWhite(int x, int y) {
        // Place a white piece and update connections
    }

    public void placeBlack(int x, int y) {
        // Place a black piece and update connections
    }

    public void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    public void play(int x, int y) {
        if (isWhiteTurn) {
            placeWhite(x, y);
        } else {
            placeBlack(x, y);
        }
        checkWinAndStop();
        if (!gameEnded) {
            switchTurn();
        }
    }

    public void draw() {
        gamePanel.repaint(); // Assuming gamePanel handles drawing logic
    }

    // Add any other game logic methods here
}
