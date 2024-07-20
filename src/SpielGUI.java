import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.exit;

public class SpielGUI {

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

    private SpielLogik logik;
    private DebugModus debug;

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

        //setSpielfeld();

        fenster.setVisible(true);
    }

    public void setSpielfeld() {
        fenster.setLayout(new GridLayout(8,8));
        feld = new JPanel[8][8];

        // Spielfeld (und Farben) erstellen
        for ( int i=0; i<8; i++ ) { // vertikal
            for ( int j=0; j<8; j++ ) { // horizontal

                JPanel panel = new JPanel();

                // GEHT SO!!!!
                play = new JButton("");
                setSpielfeldButton(i,j);

                // Button unsichtbar machen
                play.setBorderPainted(false);
                play.setContentAreaFilled(false);
                play.setFocusPainted(false);
                play.setOpaque(false);

                panel.add(play);

                feld[i][j] = panel;
                feld[i][j].setLayout(new GridLayout());
                fenster.add(feld[i][j]);

                feld[i][j].setOpaque(true);
                feld[i][j].setBackground(Color.GRAY);

                if ((j+i) % 2 == 1) { // jedes zweite Feld färben
                    feld[i][j].setBackground(Color.DARK_GRAY);
                }

                feld[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));

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

            wBauer.addActionListener(e -> debugSetzeSpielfigur(3));
            bBauer.addActionListener(e -> debugSetzeSpielfigur(1));
            wDame.addActionListener(e -> debugSetzeSpielfigur(4));
            bDame.addActionListener(e -> debugSetzeSpielfigur(2));
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

    public void debugStarten() {
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
            play.addActionListener(e -> logik.setAttributes(i, j, pgn));
        } else {
            play.addActionListener(e -> aktualisierePNG_debug(i, j));
        }
    }

    public void setStandardPGN() {
        this.pgn = new int[][] {
            {0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {3, 0, 3, 0, 3, 0, 3, 0},
            {0, 3, 0, 3, 0, 3, 0, 3},
            {3, 0, 3, 0, 3, 0, 3, 0}};
    }

    public void setSpielfigur(int i, int j) {
        switch (pgn[i][j]) {
            case 1: // Bauer schwarz
                SpFigZeichnen figur1 = new SpFigZeichnen("black");
                play.add(figur1);
                fenster.add(feld[i][j]);
                break;

            case 2: // Dame schwarz
                DameZeichnen figur3 = new DameZeichnen("black");
                play.add(figur3);
                fenster.add(feld[i][j]);
                break;

            case 3: // Bauer weiß
                SpFigZeichnen figur2 = new SpFigZeichnen("white");
                play.add(figur2);
                fenster.add(feld[i][j]);
                break;

            case 4: // Dame weiß
                DameZeichnen figur4 = new DameZeichnen("white");
                play.add(figur4);
                fenster.add(feld[i][j]);
                break;

            default:
        }
    }

    public void aktualisierePNG_debug(int i, int j) {
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

    public int[][] getPGN() {
        return pgn;
    }

    public void setSpielfiguren(int[][] orte) {
        this.pgn = orte;
    }

    public void setPGN(int[][] pgn) {
        this.pgn = pgn;
    }
}
