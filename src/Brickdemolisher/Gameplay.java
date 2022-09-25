package Brickdemolisher;
// I imported that is needed for graphics and the JPanel which is the frame of the app
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.JPanel;    
// These 4 imports are for the controls
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
public class Gameplay extends JPanel implements KeyListener, ActionListener{
    private boolean play = false;
    // this is added so when the program is running, it will not play automatically

    private int score = 0;
    private int totalBricks = 21;
    
    private Timer timer;
    private int speed = 8;
    
    private int playerX = 310;
    // The position and the direction of the ball
    private int ballposX = 350;
    private int ballposY = 350;
    private int ballXdir = 2;
    private int ballYdir = -5;
    
    private MapGenerator map;
    Image img = Toolkit.getDefaultToolkit().createImage("/Users/Acer/Downloads/Background.jpg");
    
    public Gameplay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(speed, this);
        timer.start();
    }
    
    @Override
    public void paint(Graphics g) {
        // the background
        g.setColor(Color.black);
        g.fillRect(1,1, 692, 592);
        g.drawImage(img,40,40,null);
        
        // the bricks
        map.draw((Graphics2D)g);
        
        // the border
        g.setColor(Color.gray);
        g.fillRect(0, 0, 15, 592);
        g.fillRect(0, 0, 692, 15);
        g.fillRect(691, 0, 15, 592);
        
        // the paddle
        g.setColor(Color.white);
        g.fillRect(playerX, 545, 115, 10);
        
        // the ball
        g.setColor(Color.white);
        g.fillOval(ballposX,ballposY,20,20);
       
        // the score
        g.setColor(Color.white);
        g.setFont(new Font("SansSerif", Font.BOLD, 25));
        g.drawString(" " + score, 590, 40);
        
        if(ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
            g.drawString("Game Over! Your Score is " + score, 170, 300);

            g.setFont(new Font("sansserif", Font.BOLD, 20));
            g.drawString("Press Space To Restart", 250, 350);
        }
        
        if(totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.white);
            g.setFont(new Font("sansserif", Font.BOLD, 30));
            g.drawString("You Won! Your Score is ", 190, 300);

            g.setFont(new Font("sansserif", Font.BOLD, 20));
            g.drawString("Press Space To Restart", 250, 350);
        }
        g.dispose();
    }

        @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play) {
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX,550, 100, 8))) {
                ballYdir = -ballYdir;
            }
            A: for(int i = 0; i< map.map.length; i++) {
                for(int j = 0; j< map.map[0].length; j++) {
                    if(map.map[i][j] > 0) {
                        int brickX = j* map.brickWidth + 80;
                        int brickY = i* map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;
                        
                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;
                            
                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
            
            ballposX  += ballXdir;
            ballposY += ballYdir;
            if(ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if(ballposY < 0) {
                ballYdir =  -ballYdir;
            }
            if(ballposX > 670) {
                ballXdir = -ballXdir;
            }
        }
        
        repaint(); // calling the paint method again
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 595) {
                playerX = 595;
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(!play) {
                play = false;
                ballposX = 350;
                ballposY = 350;
                ballXdir = 2;
                ballYdir = -5;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                
                repaint();
            }
        }
    }
    public void moveRight() {
        play = true;
        playerX += 30; // the speed of the paddle going right
    }
    public void moveLeft() {
        play = true;
        playerX -= 30; // the speed of the paddle going left
    }
}
