package brickdemolisherjava;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame obj = new JFrame(); // creating an object "obj" and using it for method calling
        Gameplay gamePlay = new Gameplay(); // creating an object
        obj.setBounds(350, 120, 722, 600);
        obj.setTitle("Brick Demolisher");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
    }
    
}
