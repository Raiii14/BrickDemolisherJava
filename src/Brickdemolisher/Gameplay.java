package Brickdemolisher;
// I imported that is needed for graphics and the JPanel which is the frame of the app
import java.awt.Color;
import java.awt.Graphics;
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
    private final int totalBrick = 21;
    
    private Timer timer;
    private int speed = 8;
    
    private int playerX = 310;
    // The position and the direction of the ball
    private int ballposX = 350;
    private int ballposY = 350;
    private int ballXdir = 3;
    private int ballYdir = -5;
    
    Image img = Toolkit.getDefaultToolkit().createImage("/Users/Acer/Downloads/Background.jpg");
    
    public Gameplay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(speed, this);
        timer.start();
    }
    
    @Override
    public void paint(Graphics g) {
        // insert the background
        g.setColor(Color.black);
        g.fillRect(1,1, 692, 592);
        g.drawImage(img,40,40,null);
        
        // insert the border
        g.setColor(Color.gray);
        g.fillRect(0, 0, 15, 592);
        g.fillRect(0, 0, 692, 15);
        g.fillRect(691, 0, 15, 592);
        
        // the paddle
        g.setColor(Color.white);
        g.fillRect(playerX, 545, 115, 10);
        
        // insert the ball
        g.setColor(Color.white);
        g.fillOval(ballposX,ballposY,20,20);
        
        g.dispose();
    }

        @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play) {
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX,550, 100, 8))) {
                ballYdir = -ballYdir;
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
    }
    public void moveRight() {
        play = true;
        playerX += 20; // the speed of the paddle going right
    }
    public void moveLeft() {
        play = true;
        playerX -= 20; // the speed of the paddle going left
    }
}
