import javax.swing.*;
import java.awt.*;

import static java.lang.System.exit;

public class Startbildschirm {

    static protected JFrame fenster = new Fenster();

    public Startbildschirm() {

    }

    public void setStartbildschirm() {
        // Design für Hauptbildschirm
        fenster.setLayout(new GridBagLayout());

        JPanel elmementPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;

        JLabel dame = new JLabel("Dame", SwingConstants.CENTER);
        dame.setFont(new Font("Arial", Font.BOLD, 160));
        dame.setForeground(Color.LIGHT_GRAY);

        JButton debug = new JButton("Debug-Modus");
        JButton spiel = new JButton("Spielen");
        JButton schliessen = new JButton("Beenden");

        gbc.gridx = 0;
        gbc.gridy = 0;
        elmementPanel.add(dame, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        elmementPanel.add(debug, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        elmementPanel.add(spiel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        elmementPanel.add(schliessen, gbc);

        spiel.addActionListener(e -> starteSpiel());
        debug.addActionListener(e -> starteDebug());
        schliessen.addActionListener(e -> exit(0));

        elmementPanel.setBackground(Color.BLACK);
        fenster.add(elmementPanel);
        fenster.setVisible(true);
    }

    public void starteDebug() {
        // Startbildschirm entfernen
        fenster.getContentPane().removeAll();
        SpielGUI gui = new SpielGUI();
        gui.setModus("debug");
        gui.setSpielfeld();
        fenster.repaint();
    }

    public void starteSpiel() {
        // Startbildschirm entfernen
        fenster.getContentPane().removeAll();
        SpielGUI gui = new SpielGUI();
        gui.setModus("spiel");
        gui.setStandardPGN();
        gui.setSpielfeld();
        fenster.repaint();
    }
}
