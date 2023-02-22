package brickdemolisherjava;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    public static final int brickdurab = 3;
    
    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for(int i = 0; i < map.length; i++) {
            for(int j=0; j< map[0].length; j++) {
                map[i][j] = brickdurab;
            }
        }
        brickWidth = 540/col;
        brickHeight = 150/row;
    }
    public void draw(Graphics2D g) {
    for(int i = 0; i < map.length; i++) {
        for(int j=0; j< map[0].length; j++) {
            if(map[i][j] > 0) {
                // use different colors for different levels of durability
                if (map[i][j] == brickdurab) {
                    g.setColor(Color.black);
                } else if (map[i][j] == brickdurab - 1) {
                    g.setColor(Color.gray);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }
                g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                g.setStroke(new BasicStroke(4));
                g.setColor(Color.white);
                g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
            }
        }
    }
}

public void setBrickValue(int value, int row, int col) {
    // reduce the durability of a brick when it's hit by the ball
    map[row][col] -= 1;
}
}

