/* Copyright 2012 David Pearson.
 * BSD License
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Arrays;

/**
 * The actual gameplay view, and associated AI methods.
 *
 * @author David Pearson
 */
public class Panel extends JPanel {
    private Graphics graph;
    private BufferedImage image;
    private Gamestate gamestate;
    private int[][] board;
    private Polygon[][] squares;

    private final Color[] colors={Color.GRAY, Color.WHITE, Color.BLACK,Color.BLUE};
    private AI ai;
    private Location currentHover;

    /**
     * Default constructor for the view.
     */
    public Panel() {
        board=new int[7][7];
        this.gameLogic = gameLogic;
        squares=new Polygon[7][7];

        String[] opts={"Black", "White"};
        colour=JOptionPane.showOptionDialog(null, "Which colour would you like to play as?", "Choose Colour", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);

        int opp=1;
        if (colour!=JOptionPane.CLOSED_OPTION) {
            opp=colour+1;
        } else {
            colour=2;
        }

        ai=new MCAI(opp);

        image=new BufferedImage(400, 500, BufferedImage.TYPE_INT_RGB);
        graph=image.getGraphics();
        graph.setColor(new Color(210, 180, 140));
        graph.fillRect(0, 0, 400, 500);

        drawBoard();

        if (turn==ai.getPlayerCode()) {
            Location loc=ai.getPlayLocation(board, new Location(-1, -1));
            playAt(loc.x, loc.y);
        }

        addMouseListener(new Mouse());
        addMouseMotionListener(new Mouse());
    }


    /**
     * Redraws the board as represented internally and forces a repaint
     * 	to occur immediately.
     */
    private void drawBoard() {
        graph.setColor(new Color(210, 180, 140));
        graph.fillRect(0, 0, 400, 500);

        for (int y=0; y<7; y++) {
            for (int x=0; x<7; x++) {
                graph.setColor(getColor(x, y));
                squares[y][x]=drawHex(25.0, x*42+70, y*47+178-(23*x));
                graph.fillPolygon(squares[y][x]);
            }
        }

        paintImmediately(getBounds());
    }

    /**
     * Paints the view.
     * @param  g  the Graphics instance to paint to.
     */
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }

    /**
     * Creates a regular hexagon for drawing.
     *
     * @param rad The side length of the hexagon
     * @param centerX The x coordinate of the center point
     * @param centerY The y coordinate of the center point
     *
     * @return A hexagon-shaped Polygon
     */
    private Polygon drawHex(double rad, double centerX, double centerY) {
        Polygon p=new Polygon();

        double arc=(Math.PI*2)/6;

        for (int i=0; i<=6; i++) {
            p.addPoint((int)Math.round(centerX+rad*Math.cos(arc*i)), (int)Math.round(centerY+rad*Math.sin(arc*i)));
        }

        return p;
    }

    /**
     * Gets the color of the hexagon at a given point.
     *
     * @param x The x coordinate of the hexagon
     * @param y The y coordinate of the hexagon
     *
     * @return The color of the hexagon at (x, y)
     */
    private Color getColor(int x, int y) {
        return colors[board[y][x]];
    }

    private void resetColorAt(int x, int y) {
        // 重置方块颜色为初始颜色
        // 这里假设是根据 board 数组来设置颜色，你可以根据实际情况调整
        if (board[y][x]==3){
            board[y][x] = 0; // 设置为原始颜色对应的值
        }

        drawBoard();
    }


    /**
     * Checks if a player can legally play at the given location.
     *
     * @param x The x coordinate of the play location
     * @param y The y coordinate of the play location
     *
     * @return true if the location is empty
     */
    private boolean isLegalPlay(int x, int y) {
        return board[y][x]==0||board[y][x]==3;
    }

    private Location pointerStayAt(int x, int y) {

        System.out.println("tzy it becomes blue");
        if (board[y][x]==0){
            board[y][x]=3;
        }

        drawBoard();
        return null;


    }
    /**
     * Handles mouse events in the panel.
     */
    private class Mouse extends MouseAdapter {
        /**
         * Handles a left click event and makes a play if necessary.
         *
         * @param e A MouseEvent to process.
         */
        public void mousePressed(MouseEvent e) {
            int eX=e.getX();
            int eY=e.getY();

            if (turn==ai.getPlayerCode() && turn>0) {
                return;
            }
            Location l = null;
            for (int y=0; y<squares.length; y++) {
                for (int x=0; x<squares[y].length; x++) {
                    if (squares[y][x].contains(eX, eY)) {
                        l = playAt(x, y);
                    }
                }
            }

            if (turn==ai.getPlayerCode() && l != null) {
                Location loc=ai.getPlayLocation(board, l);
                playAt(loc.x, loc.y);
            }
        }

        public void mouseMoved(MouseEvent e) {
            int eX = e.getX();
            int eY = e.getY();

            boolean found = false;
            for (int y = 0; y < squares.length; y++) {
                for (int x = 0; x < squares[y].length; x++) {
                    if (squares[y][x].contains(eX, eY)) {
                        // 如果当前方块不是之前悬停的方块，更新颜色
                        if (currentHover == null || currentHover.x != x || currentHover.y != y) {
                            if (currentHover != null) {
                                resetColorAt(currentHover.x, currentHover.y); // 重置之前方块的颜色
                            }
                            pointerStayAt(x, y); // 更新当前方块的颜色
                            currentHover = new Location(x, y);
                        }
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }

            // 如果鼠标不在任何方块上，重置当前悬停的方块颜色
            if (!found && currentHover != null) {
                resetColorAt(currentHover.x, currentHover.y);
                currentHover = null;
            }
        }

    }
}