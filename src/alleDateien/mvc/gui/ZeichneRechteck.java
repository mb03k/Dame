package alleDateien.mvc.gui;

import javax.swing.*;
import java.awt.*;

public class ZeichneRechteck extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int diameter = Math.min(getWidth(), getHeight());
        diameter *= 0.95;

        // Durchmesser äußerer Kreis
        int xOuter = (getWidth() - diameter) / 2;
        int yOuter = (getHeight() - diameter) / 2;

        g2d.setColor(Color.CYAN);
        g2d.drawRect(xOuter, yOuter, diameter, diameter);

    }

}
