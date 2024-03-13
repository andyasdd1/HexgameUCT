import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

public class Gamestate {
    private HexBoard board;
    private int currentPlayer; // 1 for white, 2 for black
    public HashMap<Integer, String> dicMap;
    private HexBoardUnionFind whiteGroups;
    private HexBoardUnionFind blackGroups;
    private Panel gamePanel;
    private boolean gameEnded = false;

    // Constants for player representation for easy access
    public static final int NONE = 0;
    public static final int WHITE = 1;
    public static final int BLACK = 2;

    public Gamestate() {
        this.board = new HexBoard();
        this.whiteGroups = new HexBoardUnionFind(board);
        this.blackGroups = new HexBoardUnionFind(board);
        this.gamePanel = new Panel();
        this.currentPlayer = WHITE; // White starts
        this.gameEnded = false;
        this.dicMap = new HashMap<>();
        dicMap.put(NONE, "None");
        dicMap.put(WHITE, "White");
        dicMap.put(BLACK, "Black");
        // Initialize your HexBoardUnionFind instances and game panel here
    }

    public void resetGame() {
        this.board.reset(); // Assuming reset method in HexBoard class clears the board
        this.whiteGroups = new HexBoardUnionFind(board);
        this.blackGroups = new HexBoardUnionFind(board);
        this.currentPlayer = WHITE;
        this.gameEnded = false;
        this.gamePanel.repaint(); // Update UI
    }

    public void checkWinAndStop() {
        boolean whiteWins = whiteGroups.checkConnected(1, board); // Assuming 1 represents white
        boolean blackWins = blackGroups.checkConnected(2, board); // Assuming 2 represents black

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

    public void placeWhite(HexCoordinate move) {
        if (!gameEnded && currentPlayer == 1 && board.isValidHex(move)) {
            board.placeStone(move, currentPlayer);
    
            whiteGroups.connectToEdge(move, 1); 
            
            // Update connections beyond just edges
            whiteGroups.buildChains(board); // Update connections based on the new move
            checkWinAndStop();
        } else {
            System.out.println("It's not White's turn or the move is invalid.");
        }
    }
    
    public void placeBlack(HexCoordinate move) {
        if (!gameEnded && currentPlayer == 2 && board.isValidHex(move)) {
            board.placeStone(move, currentPlayer);
           
            blackGroups.connectToEdge(move, 2); 
        
            // Update connections beyond just edges
            blackGroups.buildChains(board); // Update connections based on the new move
            checkWinAndStop();
        } else {
            System.out.println("It's not Black's turn or the move is invalid.");
        }
    }
    
    
    public void play(HexCoordinate move) {
        if (gameEnded) {
            System.out.println("Game has already ended.");
            return;
        }
        if (!board.isValidHex(move)) {
            System.out.println("Invalid move.");
            return;
        }
    
        if (currentPlayer == 1) {
            placeWhite(move);
        } else {
            placeBlack(move);
        }
        togglePlayer();
    }
    

    public void draw() {
        gamePanel.repaint(); // Assuming gamePanel handles drawing logic
    }

    private void togglePlayer() {
        currentPlayer = currentPlayer == WHITE ? BLACK : WHITE;
    }

    public void setTurn(int player) {
        if (player == 1 || player == 2) {
            this.currentPlayer = player;
        } else {
            throw new IllegalArgumentException("Invalid turn: " + player);
        }
    }
    
    public boolean isGameOver() {
        return gameEnded;
    }
    
    public List<HexCoordinate> moves() {
        List<HexCoordinate> validMoves = new ArrayList<>();
        // Assuming board.getSize() returns the diameter of the board
        for (int q = -board.getSize(); q <= board.getSize(); q++) {
            for (int r = -board.getSize(); r <= board.getSize(); r++) {
                HexCoordinate coord = new HexCoordinate(q, r);
                if (board.isValidHex(coord)) {
                    validMoves.add(coord);
                }
            }
        }
        return validMoves;
    }
    
    // Add any other game logic methods here
}
