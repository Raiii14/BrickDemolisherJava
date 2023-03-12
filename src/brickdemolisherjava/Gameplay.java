package brickdemolisherjava;
// These imports are needed for graphics and the JPanel for the Gameplay class to use
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.JPanel;  
import javax.swing.JButton;
// These 4 imports are for the controls
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    // this is added so when the game is ran, it will not play automatically

    private int score = 0;
    private int totalBricks = 63;
    
    private Timer timer;
    private double speed = 1;
    
    private int playerX = 310;
    // The position and the direction of the ball
    private int ballposX = 350;
    private int ballposY = 350;
    private int ballXdir = -2;
    private int ballYdir = -4;
    
    String diff = "Normal";
    
    int i;
    int j;
    
    private MapGenerator map;
    
    int highscore;
    String currentact = GameLogInForm.currentact;
    Connection con= ConnectionForms.connectdb();
    PreparedStatement ps=null;
    ResultSet rs=null;
    
    Image img = Toolkit.getDefaultToolkit().createImage("Bdpics/bg1.png");
    
    public Gameplay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer((int) speed, this);
        timer.start();
        
        ConnectionForms.connectdb();
    }
    
    @Override
    public void paint(Graphics g) {
        // the background
        g.drawImage(img, 0, 0, 1033, 660, 0, 0, 1500, 1200, null);
        
        // the paddle
        g.setColor(new Color(50, 200, 255));
        g.fillRect(playerX, 545, 115, 10);
        
        // the ball
        g.setColor(new Color(50, 200, 255));
        g.fillOval(ballposX,ballposY,20,20);
        
        // difficulty
        g.setFont(new Font("courier", Font.BOLD, 20));
        g.setColor(Color.white);
        g.drawString(diff, 9, 27);
        
        g.setFont(new Font("courier", Font.BOLD, 16));
        g.setColor(Color.white);
        g.drawString("1 - Normal      2 - Hard      3 - Difficult", 230, 27);
        
        // the score
        g.setColor(Color.white);
        g.setFont(new Font("sansserif", Font.BOLD, 23));
        g.drawString(" " + score, 650, 29);
        if(ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
            g.drawString("Game over! Your Score is " + score, 170, 300);

            g.setFont(new Font("sansserif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 250, 350);
        }
        
        if(totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.white);
            g.setFont(new Font("sansserif", Font.BOLD, 30));
            g.drawString("You Won! Your Score is " + score, 167, 300);

            g.setFont(new Font("sansserif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 250, 350);
        }
        map.draw((Graphics2D)g);
        g.dispose();
    }

        @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play) {
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX,550, 100, 8))) {
                ballYdir = -ballYdir; // ^this can make the ball hit the paddle and bounce back
            }
            A: for(i = 0; i< map.map.length; i++) {
                for(j = 0; j< map.map[0].length; j++) {
                    if(map.map[i][j] > 0) {
                        int brickX = j* map.brickWidth + 80;
                        int brickY = i* map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;
                        
                        if(ballRect.intersects(brickRect)) { // this can make the ball hit the brick
                            map.setBrickValue(0, i, j); // when hit, removes a brick that are hit by the ball
                            totalBricks--;
                            if(diff == "Difficult") {
                                score += 30;
                            }
                            else if(diff == "Hard") {
                                score += 20;
                            }
                            else {
                                score += 10;
                            }
                            if(ballposX + 10 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
            // these are to stop the ball from going outside the frame
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
        if(ballposY > 570 || totalBricks <= 0) {
            String addhighscore = "UPDATE brickplayerinfo SET highscore = ? WHERE username = '"+currentact+"'";
            String addscore = "UPDATE brickplayerinfo SET currentscore = ? WHERE username = '"+currentact+"'";
            String checkhscore = "SELECT highscore FROM brickplayerinfo WHERE username = '"+currentact+"'";
            try {
                ps = con.prepareStatement(checkhscore);
                rs = ps.executeQuery();
                
                if(rs.next()) {
                    highscore = rs.getInt("highscore");
                    
                    if(highscore <= score) { // adding the score if the recent score is higher than their previous high score
                        ps = con.prepareStatement(addhighscore);
                        ps.setInt(1, score);
                        ps.executeUpdate();
                        
                    } else { // if the recent score is lower than the high score
                        ps = con.prepareStatement(addscore);
                        ps.setInt(1, score);
                        ps.executeUpdate();
                        
                    }
                } else {
                    ps = con.prepareStatement(addscore);
                    ps.setInt(1, score);
                    ps.executeUpdate();
                }
            } 
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
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
            if(playerX >= 575) {
                playerX = 575;
            } else {
                moveRight();
            }
        } // these two stops the paddle going outside the frame
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX < 16) {
                playerX = 16;
            } else {
                moveLeft();
            }
        }
        // Difficulties
        if(e.getKeyCode() == KeyEvent.VK_1) { // Restart, and Default/Normal difficulty
            if(!play) {
                play = false;
                ballXdir = -2;
                ballYdir = -4;
                diff = "Normal";
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_2) {
            if(!play) {
                play = false;
                ballXdir = 5;
                ballYdir = -7;
                diff = "Hard";
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_3) {
            if(!play) {
                play = false;
                ballXdir = 7;
                ballYdir = -9;
                diff = "Difficult";
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                play = false;
                map = new MapGenerator(3, 7);
                ballposX = 350;
                ballposY = 350;
                playerX = 310;
                score = 0;
                totalBricks = 63;
                if(diff == "Normal") {
                    ballXdir = -2;
                    ballYdir = -4;
                }
                else if(diff == "Hard") {
                    ballXdir = 5;
                    ballYdir = -7;
                }
                else {
                    ballXdir = 7;
                    ballYdir = -9;
                }
                repaint();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_L) {
            if(!play) {
                new Leaderboard().setVisible(true);
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setVisible(false);
            new GameMenu().setVisible(true);
        }
        
    }
    public void moveRight() {
        play = true;
        playerX += 35; // the speed of the paddle going right
    }
    public void moveLeft() {
        play = true;
        playerX -= 35; // the speed of the paddle going left
    }
}
