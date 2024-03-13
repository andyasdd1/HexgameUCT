import java.util.Arrays;
import java.util.HashMap;

public class GameState {
    private static final HashMap<String, Integer> PLAYERS = new HashMap<String, Integer>() {{
        put("none", 0);
        put("white", 1);
        put("black", 2);
    }};
    private static final int GAMEOVER = -1;
    private static final int EDGE1 = 1;
    private static final int EDGE2 = 2;
    private static final int[][] neighborPatterns = {{-1, 0}, {0, -1}, {-1, 1}, {0, 1}, {1, 0}, {1, -1}};

    private int size;
    private int toplay;
    private int[][] board;
    private UnionFind whiteGroups;
    private UnionFind blackGroups;

    public GameState(int size) {
        this.size = size;
        this.toplay = PLAYERS.get("white");
        this.board = new int[size][size]; // Java initializes int arrays to 0 by default
        this.whiteGroups = new UnionFind(size * size + 2);
        this.blackGroups = new UnionFind(size * size + 2);
    }

    public void play(int x, int y) throws Exception {
        int[] cell = new int[]{x, y};
        if (toplay == PLAYERS.get("white")) {
            placeWhite(cell);
            toplay = PLAYERS.get("black");
        } else if (toplay == PLAYERS.get("black")) {
            placeBlack(cell);
            toplay = PLAYERS.get("white");
        }
    }

    private void placeStone(int[] cell, String color) throws Exception {
        UnionFind groups = color.equals("white") ? whiteGroups : blackGroups;
        int edge1 = color.equals("white") ? EDGE1 : EDGE2;
        int edge2 = color.equals("white") ? EDGE2 : EDGE1;

        if (board[cell[0]][cell[1]] == PLAYERS.get("none")) {
            board[cell[0]][cell[1]] = PLAYERS.get(color);
        } else {
            throw new Exception("Cell occupied");
        }

        int cellId = cell[0] * size + cell[1] + 3; // +3 to avoid conflict with special edge IDs
        if ((color.equals("white") && cell[0] == 0) || (color.equals("black") && cell[1] == 0)) {
            groups.union(edge1, cellId);
        }
        if ((color.equals("white") && cell[0] == size - 1) || (color.equals("black") && cell[1] == size - 1)) {
            groups.union(edge2, cellId);
        }

        for (int[] n : neighbors(cell)) {
            if (board[n[0]][n[1]] == PLAYERS.get(color)) {
                int neighborId = n[0] * size + n[1] + 3;
                groups.union(cellId, neighborId);
            }
        }
    }

    private void placeWhite(int[] cell) throws Exception {
        placeStone(cell, "white");
    }

    private void placeBlack(int[] cell) throws Exception {
        placeStone(cell, "black");
    }

    public int turn() {
        return toplay;
    }

    public void setTurn(String player) throws Exception {
        if (PLAYERS.containsKey(player) && !player.equals("none")) {
            toplay = PLAYERS.get(player);
        } else {
            throw new Exception("Invalid turn: " + player);
        }
    }

    public String winner() {
        if (whiteGroups.connected(EDGE1, EDGE2)) {
            return "white";
        } else if (blackGroups.connected(EDGE1, EDGE2)) {
            return "black";
        } else {
            return "none";
        }
    }

    public List<int[]> neighbors(int[] cell) {
        List<int[]> neighbors = new ArrayList<>();
        for (int[] pattern : neighborPatterns) {
            int newX = cell[0] + pattern[0];
            int newY = cell[1] + pattern[1];
            if (newX >= 0 && newX < size && newY >= 0 && newY < size) {
                neighbors.add(new int[]{newX, newY});
            }
        }
        return neighbors;
    }

    public List<int[]> moves() {
        List<int[]> moves = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board[x][y] == PLAYERS.get("none")) {
                    moves.add(new int[]{x, y});
                }
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("\n");
        char white = 'O', black = '@', empty = '.';
        ret.append(" ".repeat(Math.max(0, 1)));
        for (int x = 0; x < size; x++) {
            ret.append((char) ('A' + x)).append(" ".repeat(2));
        }
        ret.append("\n");
        for (int y = 0; y < size; y++) {
            ret.append(y + 1).append(" ".repeat(2 * (1 - Integer.toString(y + 1).length())));
            for (int x = 0; x < size; x++) {
                ret.append(board[x][y] == PLAYERS.get("white") ? white : board[x][y] == PLAYERS.get("black") ? black : empty);
                ret.append(" ".repeat(2));
            }
            ret.append(white).append("\n").append(" ".repeat(y + 1));
        }
        ret.append(" ".repeat(2)).append((black + " ".repeat(2)).repeat(size));
        return ret.toString();
    }

    public static void main(String[] args) {
        GameState game = new GameState(11);
        System.out.println(game.toString());
        // Example of playing a game
    }
}

// Note: The `UnionFind` class must be implemented to use in this code.
