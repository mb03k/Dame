import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// AKTUELLER PLAN:

// Schwarz weiß schwarz weiß,...

/*
* Mögliche Züge zeichnen:
* eventuell neue PGN für die Farben erstellen und da die farbe einfach färben?
* */

// JPanels mit Objekten füllen? -> für die

import static java.lang.System.exit;

public class SpielGUI extends Main {

    private String modus;
    protected int[][] pgn=new int[8][8];
    private JButton play;
    private JFrame fenster;
    private JPanel[][] feld;
    private JMenuBar menueLeiste;
    private JMenu dateiMenue;
    private JMenuItem beenden;
    private JMenuItem startseite;
    private JMenuItem speichern;
    private int option;
    protected int[][] afterDebugPGN;
    private JPanel secondPanel;
    private JPanel innerFirstRow;
    private JPanel innerMiddleRow;
    private JPanel innerThirdRow;
    private String zug_grid = "Weiß";

    private SpielLogik logik;
    private DebugModus debug;

    protected boolean werIstDran = true; // true -> weiß; false -> schwarz

    public SpielGUI() {
    }

    public void setModus(String modus) {
        this.modus = modus;
        starteGUI();
    }

    public void starteGUI() {
        this.fenster = Startbildschirm.fenster;
        logik = new SpielLogik();
        debug = new DebugModus();
        this.afterDebugPGN = new int[8][8];

        setMenueBar();

        fenster.setVisible(true);
    }

    public void setSpielfeld() {
        setGridLayout();

        feld = new JPanel[8][8];


        // Spielfeld (und Farben) erstellen
        for ( int i=0; i<8; i++ ) { // vertikal
            for ( int j=0; j<8; j++ ) { // horizontal

                JPanel panel = new JPanel();

                // GEHT SO!!!!
                play = new JButton("");
                setSpielfeldButton(i,j);

                panel.add(play);

                feld[i][j] = panel;
                feld[i][j].setLayout(new GridLayout());
                innerMiddleRow.add(feld[i][j]);

                feld[i][j].setOpaque(true);
                feld[i][j].setBackground(Color.GRAY);

                if ((j+i) % 2 == 1) { // jedes zweite Feld färben
                    feld[i][j].setBackground(Color.DARK_GRAY);
                }

                // Button unsichtbar machen
                play.setBorderPainted(false);
                play.setContentAreaFilled(false);
                play.setFocusPainted(false);
                play.setOpaque(false);

                setSpielfigur(i, j);
            }
        }
        checkModus();
    }

    public void setMenueBar() {
        // Menüleiste
        menueLeiste = new JMenuBar();
        dateiMenue = new JMenu("Datei");
        speichern = new JMenuItem("Speichern\tstrg-s");
        startseite = new JMenuItem("Startseite");
        startseite.addActionListener(e -> setStartseite());
        beenden = new JMenuItem("Beenden");
        beenden.addActionListener(e -> exit(0));
        dateiMenue.add(speichern);
        dateiMenue.add(startseite);
        dateiMenue.add(beenden);
        menueLeiste.add(dateiMenue);
        fenster.setJMenuBar(menueLeiste);
    }

    public void checkModus() {
        // wenn Benutzer Debug-Modus angeklickt hat neuen Menüpunkt erstellen
        if (modus.equals("debug")) {
            JMenu debug = new JMenu("Debug");
            JMenuItem wBauer = new JMenuItem("Weiße Spielfigur");
            JMenuItem bBauer = new JMenuItem("Schwarze Spielfigur");
            JMenuItem wDame = new JMenuItem("Weiße Dame");
            JMenuItem bDame = new JMenuItem("Schwarze Dame");
            JMenuItem spielStarten = new JMenuItem("Spiel starten");

            wDame.addActionListener(e -> debugSetzeSpielfigur(2));
            wBauer.addActionListener(e -> debugSetzeSpielfigur(1));
            bDame.addActionListener(e -> debugSetzeSpielfigur(-2));
            bBauer.addActionListener(e -> debugSetzeSpielfigur(-1));
            spielStarten.addActionListener(e -> debugStarten());

            wBauer.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            bBauer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            wDame.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            bDame.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            debug.add(wDame);
            debug.add(wBauer);
            debug.add(bDame);
            debug.add(bBauer);
            debug.add(spielStarten);
            menueLeiste.add(debug);
        }
    }

    public void setGridLayout() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0,0,0,0);

        // Panel for the first line
        JPanel firstPanel = new JPanel();
        JLabel label = new JLabel("Zug von: "+zug_grid);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 20));

        firstPanel.add(label);
        firstPanel.setBackground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        fenster.add(firstPanel, gbc);

        // Second panel with three rows
        secondPanel = new JPanel(new GridBagLayout());
        gbc.weighty = 0.8; // 80% of the height
        gbc.gridx = 0;
        gbc.gridy = 1;
        fenster.add(secondPanel, gbc);

        // Constraints for the inner rows of the second panel
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.fill = GridBagConstraints.BOTH;
        innerGbc.weightx = 1.0;
        innerGbc.weighty = 1.0;

        // First inner row
        innerFirstRow = new JPanel();
        innerFirstRow.setBackground(Color.DARK_GRAY);
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;  // This should remain 0
        secondPanel.add(innerFirstRow, innerGbc);

        // Middle inner row
        innerMiddleRow = new JPanel();
        innerMiddleRow.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        innerMiddleRow.setLayout(new GridLayout(8,8));
        innerMiddleRow.setBackground(Color.DARK_GRAY);
        innerGbc.gridx = 1;  // This should be 0
        innerGbc.gridy = 0;  // Change to 1 to place in the second row
        secondPanel.add(innerMiddleRow, innerGbc);

        // Third inner row
        innerThirdRow = new JPanel();
        innerThirdRow.setBackground(Color.DARK_GRAY);
        innerGbc.gridx = 2;  // This should be 0
        innerGbc.gridy = 0;  // Change to 2 to place in the third row
        secondPanel.add(innerThirdRow, innerGbc);

    }

    public void debugStarten() {
        // alles neu zeichnen und spiellogik starten
        this.modus = "spiel";
        fenster.setJMenuBar(null);
        setMenueBar();
        fenster.getContentPane().removeAll();
        setPGN(afterDebugPGN);
        setSpielfeld();
        fenster.repaint();
    }

    public void setSpielfeldButton(int i, int j) {
        if (modus.equals("spiel")) {
            setSollGeschlagenWerden(i,j);
        } else {
            play.addActionListener(e -> aktualisierePGN_debug(i, j));
        }
    }

    public void setSollGeschlagenWerden(int i, int j) {
        if (pgn[i][j]==0) {
            play.addActionListener(e -> schlageFigur(i, j));
        } else {
            play.addActionListener(e -> logik.setAttributes(i, j, pgn));
        }
    }

    public void schlageFigur(int i, int j) {
        int[][] temp = logik.schlage(i, j);
        if (temp != null) {
            pgn = temp;
            fenster.getContentPane().removeAll();
            fenster.setJMenuBar(null);
            setMenueBar();
            setSpielfeld();
            fenster.repaint();
        }
    }

    public void setStandardPGN() {
        this.pgn = new int[][] {
          // a, b, c, d, e, f, g, h

                // NEU
                // -1 = Bauer schwarz
                // -2 = Dame schwarz
                // 1 = Bauer weiß
                // 2 = Dame weiß
                // 0 = leer

        // 1 = Bauer schwarz
        // 2 = Dame schwarz
        // 3 = Bauer weiß
        // 4 = Bauer schwarz
        {0, -1, 0, -1, 0, -1, 0, -1},
        {-1, 0, -1, 0, -1, 0, -1, 0},
        {0, -1, 0, -1, 0, -1, 0, -1},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {1, 0, 1, 0, 1, 0, 1, 0},
        {0, 1, 0, 1, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 0}};
    }

    public void setSpielfigur(int i, int j) {
        switch (pgn[i][j]) {
            case -1: // Bauer schwarz
                SpFigZeichnen figur1 = new SpFigZeichnen("black");
                play.add(figur1);
                innerMiddleRow.add(feld[i][j]);
                break;

            case -2: // Dame schwarz
                DameZeichnen figur3 = new DameZeichnen("black");
                play.add(figur3);
                innerMiddleRow.add(feld[i][j]);
                break;

            case 1: // Bauer weiß
                SpFigZeichnen figur2 = new SpFigZeichnen("white");
                play.add(figur2);
                innerMiddleRow.add(feld[i][j]);
                break;

            case 2: // Dame weiß
                DameZeichnen figur4 = new DameZeichnen("white");
                play.add(figur4);
                innerMiddleRow.add(feld[i][j]);
                break;

            case 6: // Rechteck
                ZeichneRechteck figur5 = new ZeichneRechteck();
                play.add(figur5);
                fenster.add(feld[i][j]);
                break;

            default:
        }
    }

    public void aktualisierePGN_debug(int i, int j) {
        // live anzeigen der neuen Steine
        afterDebugPGN[i][j] = debug.getDebugFigur();
        fenster.getContentPane().removeAll();
        fenster.setJMenuBar(null);
        setMenueBar();
        pgn[i][j] = debug.getDebugFigur();
        setSpielfeld();
        fenster.repaint();
    }

    public void infoBox() {
        Object[] options = {"Fortfahren", "Abbrechen"};

        option = JOptionPane.showOptionDialog(null, "Das Spiel wird nicht gespeichert. Fortfahren?", "Spielspeicherung",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    public void setStartseite() {
        // von Spielfeld zu Startseite -> Warnung
        if (0 < 1) { // wenn nicht gespeichert
            infoBox();
            if (option == JOptionPane.NO_OPTION) { // abbrechen
                System.out.println("Zurück");
            } else {
                System.out.println("Nicht speichern");
                fenster.getContentPane().removeAll();
                fenster.setJMenuBar(null);
                fenster.repaint();
                Startbildschirm sb = new Startbildschirm();
                sb.setStartbildschirm();
            }
        }
    }

    public void debugSetzeSpielfigur(int figur) {
        debug.setFigur(figur);
    }

    public void setPGN(int[][] pgn) {
        this.pgn = pgn;
    }
}
