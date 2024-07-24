import javax.swing.*;
import java.awt.*;

public class Fenster extends JFrame {
    public Fenster() {
        super("Dame");
        this.getContentPane().setBackground(Color.DARK_GRAY);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setSize(650, 720);
        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(500, 450));
        this.setVisible(true);
    }
}
