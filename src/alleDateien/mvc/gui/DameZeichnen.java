package alleDateien.mvc.gui;

import javax.swing.*;
import java.awt.*;

public class DameZeichnen extends JPanel {

    String color;

    public DameZeichnen(String farbe) {
        this.color = farbe;
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g); // FEHLER: Markierungen werden nicht richtig angezeigt
        Graphics2D g2d = (Graphics2D) g;

        int diameter = Math.min(getWidth(), getHeight());
        diameter *= 0.8;

        // Durchmesser äußerer Kreis
        int xOuter = (getWidth() - diameter) / 2;
        int yOuter = (getHeight() - diameter) / 2;

        // Äußerste Kreis
        g2d.setColor(getColor());
        g2d.fillOval(xOuter, yOuter, diameter, diameter);

        // Durchmesser des mittleren Kreises
        int middleDiameter = (int) (diameter * 0.6);
        int xMiddle = xOuter + (diameter - middleDiameter) / 2;
        int yMiddle = yOuter + (diameter - middleDiameter) / 2;

        // Mittlerer Kreis
        g2d.setColor(Color.GRAY);
        g2d.fillOval(xMiddle, yMiddle, middleDiameter, middleDiameter);

        // Durchmesser des inneren Kreises (schwarz)
        int innerDiameter = (int) (diameter * 0.3);
        int xInner = xOuter + (diameter - innerDiameter) / 2;
        int yInner = yOuter + (diameter - innerDiameter) / 2;

        // Innerster Kreis
        g2d.setColor(getColor());
        g2d.fillOval(xInner, yInner, innerDiameter, innerDiameter);
    }

    public Color getColor() {
        if (color.equals("black")) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }
}
