package brickdemolisherjava;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionForms {
    public static Connection connectdb() {
        Connection con=null;
        try {
            con=DriverManager.getConnection("jdbc:derby://localhost:1527/BrickDemolisherDB","ict13","brickgame");
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionForms.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
