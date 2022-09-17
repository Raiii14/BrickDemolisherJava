package Brickdemolisher;
// I imported that is needed for graphics and the JPanel which is the frame of the app
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
// These 4 imports are for the controls
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
public class Gameplay extends JPanel implements KeyListener, ActionListener{
    // this is added so when the program is running, it will not play automatically
    private boolean play = false;

    private int score = 0;
    private final int totalBrick = 21;
    
    private Timer timer;
    private final int delay = 8;
    
    private int playerX = 310;
    // The position and the direction of the ball
    private final int ballposX = 120;
    private final int ballposY = 350;
    private final int ballXdir = -1;
    private final int ballYdir = -2;
    
    public Gameplay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }
    
    @Override
    public void paint(Graphics g) {
        // insert the background
        
        
        // insert the border
        
        
        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);
        
        // insert the ball
        
        g.dispose();
    }

        @Override
    public void actionPerformed(ActionEvent e) {
        // calling the paint method again
        repaint();
        
        timer.start();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 600) {
                playerX = 600;
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
        playerX += 20;
    }
    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}
