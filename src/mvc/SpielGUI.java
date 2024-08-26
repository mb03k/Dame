package neueKlassen.mvc;

import neueKlassen.*;
import neueKlassen.mvc.data.SpielSpeichern;
import neueKlassen.mvc.gui.DameZeichnen;
import neueKlassen.mvc.gui.DebugModus;
import neueKlassen.mvc.gui.SpFigZeichnen;
import neueKlassen.mvc.gui.ZeichneRechteck;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static java.lang.System.exit;

public class SpielGUI extends Main {

    private String modus;
    private int[][] pgn=new int[8][8]; //das in Data?
    private Spielstein[][] steinpgn = new Spielstein[8][8];

    private JButton spielfeldButtonListener;
    private JFrame fenster;

    private static JPanel[][] feld;
    private int option;
    protected int[][] afterDebugPGN;
    private JPanel checkerboard;
    private String zug_grid = "Weiß";
    private JMenuBar menueLeiste;

    private SpielLogik logik;
    private DebugModus debug;
    private SpielSpeichern speichern;

    public SpielGUI() {
        feld = new JPanel[8][8];
    }

    public void setModus(String modus) {
        this.modus = modus;
    }

    public void starteGUI() {
        this.fenster = Startbildschirm.fenster;

        logik = new SpielLogik();
        debug = new DebugModus();
        speichern = new SpielSpeichern();

        this.afterDebugPGN = new int[8][8];
        fenster.setVisible(true);
    }

    public void setSpielfeld() {
        setMenueBar();
        setGridLayout();

        // Spielfeld (und Farben) erstellen
        for ( int i=0; i<8; i++ ) { // vertikal
            for ( int j=0; j<8; j++ ) { // horizontal
                setSpielfeldInhalte(i, j);
            }
        }

        checkModus();
    }

    public void setSpielfeldInhalte(int i, int j) {
        JPanel panel = new JPanel();

        spielfeldButtonListener = new JButton(""); // 'zentraler' Button für ActionListener
        setSpielfeldButton(i,j);

        panel.add(spielfeldButtonListener);

        feld[i][j] = panel;
        feld[i][j].setLayout(new GridLayout());

        feld[i][j].setOpaque(true);
        faerbeHintergrund(i, j);

        checkerboard.add(feld[i][j]); // Spielfeld in die Mitte des Bildschirms setzen


        // Button unsichtbar machen
        spielfeldButtonListener.setBorderPainted(false);
        spielfeldButtonListener.setContentAreaFilled(false);
        spielfeldButtonListener.setFocusPainted(false);
        spielfeldButtonListener.setOpaque(false);

        setSpielfigur(i, j);
    }

    private static void faerbeHintergrund(int i, int j) {
        feld[i][j].setBackground(Color.GRAY);
        if ((j+i) % 2 == 1) { // jedes zweite Feld färben
            feld[i][j].setBackground(Color.DARK_GRAY);
        }
    }

    public void setGridLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 10, 0);


        JPanel zugPanel = new JPanel();
        JLabel zugLabel = new JLabel("Zug von: " + zug_grid);
        zugLabel.setForeground(Color.WHITE);
        zugLabel.setFont(new Font("Arial", Font.BOLD, 20));
        zugPanel.add(zugLabel);
        zugPanel.setBackground(Color.DARK_GRAY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // komplette Breite
        gbc.weightx = 1.0;
        gbc.weighty = 0.01;
        fenster.add(zugPanel, gbc);

        // Panel fuer labels (A-H)
        JPanel letterPanel = new JPanel(new GridLayout(1, 8));
        letterPanel.setBackground(Color.DARK_GRAY);
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 0.05;
        fenster.add(letterPanel, gbc);

        for (int col = 0; col < 8; col++) {
            JLabel letterLabel = new JLabel(String.valueOf((char) ('A' + col)), SwingConstants.CENTER);
            letterLabel.setForeground(Color.WHITE);
            letterLabel.setFont(new Font("Arial", Font.BOLD, 20));
            letterPanel.add(letterLabel);
        }

        // Panel fuer labels (1-8)
        JPanel nummernPanel = new JPanel(new GridLayout(8, 1));
        nummernPanel.setBackground(Color.DARK_GRAY);
        gbc.gridx = 0; //
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.8;
        fenster.add(nummernPanel, gbc);

        for (int row = 1; row <= 8; row++) {
            JLabel nummernLabel = new JLabel(String.valueOf(row), SwingConstants.CENTER);
            nummernLabel.setForeground(Color.WHITE);
            nummernLabel.setFont(new Font("Arial", Font.BOLD, 20));
            nummernLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Abstand Rand
            nummernPanel.add(nummernLabel);
        }

        // Spielfeld
        checkerboard = new JPanel();
        checkerboard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        checkerboard.setLayout(new GridLayout(8, 8));
        checkerboard.setBackground(Color.DARK_GRAY);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        fenster.add(checkerboard, gbc);

        // Raender erstellen
        JPanel linksRand = new JPanel();
        linksRand.setBackground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.8;
        fenster.add(linksRand, gbc);

        JPanel rechtsRand = new JPanel();
        rechtsRand.setBackground(Color.DARK_GRAY);
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.8;
        fenster.add(rechtsRand, gbc);
    }

    public void setSpielfeldButton(int i, int j) {
        if (modus.equals("spiel")) {
            setSollGeschlagenWerden(i,j);
        } else {
            spielfeldButtonListener.addActionListener(e -> aktualisierePGN_debug(i, j));
        }
    }

    public void setSpielfigur(int i, int j) {
        switch (pgn[i][j]) {
            case -1: // Bauer schwarz
                SpFigZeichnen figur1 = new SpFigZeichnen("black");
                spielfeldButtonListener.add(figur1);
                checkerboard.add(feld[i][j]);
                break;

            case -2: // Dame schwarz
                DameZeichnen figur3 = new DameZeichnen("black");
                spielfeldButtonListener.add(figur3);
                checkerboard.add(feld[i][j]);
                break;

            case 1: // Bauer weiß
                SpFigZeichnen figur2 = new SpFigZeichnen("white");
                spielfeldButtonListener.add(figur2);
                checkerboard.add(feld[i][j]);
                break;

            case 2: // Dame weiß
                DameZeichnen figur4 = new DameZeichnen("white");
                spielfeldButtonListener.add(figur4);
                checkerboard.add(feld[i][j]);
                break;

            case 6: // Rechteck
                ZeichneRechteck figur5 = new ZeichneRechteck();
                spielfeldButtonListener.add(figur5);
                fenster.add(feld[i][j]);
                break;

            default:
        }
    }

    public void checkModus() {
        // wenn Benutzer Debug-Modus angeklickt hat, neuen Menüpunkt erstellen
        if (modus.equals("debug")) {
            JMenu debug = new JMenu("Debug");
            JMenuItem wBauer = new JMenuItem("Weiße Spielfigur");
            JMenuItem bBauer = new JMenuItem("Schwarze Spielfigur");
            JMenuItem wDame = new JMenuItem("Weiße Dame");
            JMenuItem bDame = new JMenuItem("Schwarze Dame");
            JMenuItem loeschen = new JMenuItem("Figur löschen");
            JMenuItem spielStarten = new JMenuItem("Spiel starten");

            wDame.addActionListener(e -> debugSetzeSpielfigur(2));
            wBauer.addActionListener(e -> debugSetzeSpielfigur(1));
            bDame.addActionListener(e -> debugSetzeSpielfigur(-2));
            bBauer.addActionListener(e -> debugSetzeSpielfigur(-1));
            loeschen.addActionListener(e -> debugSetzeSpielfigur(0));
            spielStarten.addActionListener(e -> debugStarten());

            wBauer.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            bBauer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            wDame.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            bDame.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            debug.add(wDame);
            debug.add(wBauer);
            debug.add(bDame);
            debug.add(bBauer);
            debug.add(loeschen);
            debug.add(spielStarten);
            menueLeiste.add(debug);
        }
    }

    public void setMenueBar() {
        // Menüleiste
        menueLeiste = new JMenuBar();
        JMenu dateiMenue = new JMenu("Datei");
        JMenuItem speichern_menuItem = new JMenuItem("Speichern\tstrg-s");
        speichern_menuItem.addActionListener(e -> spielSpeichern());
        JMenuItem startseite_menuItem = new JMenuItem("Startseite");
        startseite_menuItem.addActionListener(e -> setStartseite());
        JMenuItem beenden_menuItem = new JMenuItem("Beenden");
        beenden_menuItem.addActionListener(e -> spielBeenden());
        dateiMenue.add(speichern_menuItem);
        dateiMenue.add(startseite_menuItem);
        dateiMenue.add(beenden_menuItem);
        menueLeiste.add(dateiMenue);
        fenster.setJMenuBar(menueLeiste);
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

    public void setSollGeschlagenWerden(int i, int j) {


        if (pgn[i][j]==0) {
            spielfeldButtonListener.addActionListener(e -> schlageFigur(i, j));
        } else {
            spielfeldButtonListener.addActionListener(e -> logik.aktiviereFeld(i, j, steinpgn, pgn));
//            spielfeldButtonListener.addActionListener(e -> logik.setAttributes(i, j, pgn));
        }
    }

    public void schlageFigur(int i, int j) {
        int[][] setzePGN = logik.schlageOderBewege(i, j);
        if (setzePGN != null) {
            this.zug_grid = logik.getWerIstDran();
            pgn = setzePGN;
            aktualisiereSteinPGN(pgn);
            fenster.getContentPane().removeAll();
            fenster.setJMenuBar(null);
            setMenueBar();
            setSpielfeld();
            fenster.repaint();
        }
    }

    private void aktualisiereSteinPGN(int[][] pgn) {
        SpielData.erstelleSteinpgn(pgn); //hier könnte man verbessern, indem man nicht jedes Mal alle Objekte neu erstellt, sondern nur relevante abändert
        this.steinpgn = SpielData.getSteinpgn();
    }

    // Standardaufstellung zum spielen
    public void setStandardPGN() {
        this.pgn = SpielData.standardpgn;
        this.steinpgn = SpielData.steinpgn;


//        this.pgn = new int[][] {
//                // a, b, c, d, e, f, g, h
//                // -1 = Bauer schwarz
//                // -2 = Dame schwarz
//                // 1 = Bauer weiß
//                // 2 = Dame weiß
//                // 0 = leer
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {-1, 0, -1, 0, -1, 0, -1, 0},
//                {0, -1, 0, -1, 0, -1, 0, -1},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {1, 0, 1, 0, 1, 0, 1, 0},
//                {0, 1, 0, 1, 0, 1, 0, 1},
//                {1, 0, 1, 0, 1, 0, 1, 0}};
    }

    // Debug-Modus: neue Figuren anzeigen
    public void aktualisierePGN_debug(int i, int j) {
        // live anzeigen der neuen Steine
        afterDebugPGN[i][j] = debug.getDebugFigur();
        pgn[i][j] = debug.getDebugFigur();
        fenster.getContentPane().removeAll();
        fenster.setJMenuBar(null);
        setMenueBar();
        setSpielfeld();
        fenster.repaint();
    }

    public void setGespeichertePGN() {
        this.pgn = speichern.getGeladenePGN();
        SpielData.setAktuellepgn(this.pgn);
        SpielData.erstelleSteinpgn(this.pgn);
        this.steinpgn = SpielData.steinpgn;
    }

    public void infoBox() {
        Object[] optionen = {"Fortfahren", "Abbrechen"};

        option = JOptionPane.showOptionDialog(null, "Das Spiel wird nicht gespeichert. Fortfahren?", "Spielspeicherung",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionen, optionen[0]);
    }

    public void setStartseite() {
        // von Spielfeld zu Startseite -> Warnung
        if (speichernAbfrage()) { // wenn ungleich
            infoBox();
            if (option == JOptionPane.YES_OPTION) { // abbrechen
                clearSpielGUI();
            }
        } else {
            clearSpielGUI();
        }
    }

    public void clearSpielGUI() {
        fenster.getContentPane().removeAll();
        fenster.setJMenuBar(null);
        fenster.repaint();
        Startbildschirm sb = new Startbildschirm();
        sb.setStartbildschirm();
    }

    public boolean speichernAbfrage() {
        return !Arrays.deepEquals( // wenn gleich: true; else: false
                speichern.getGeladenePGN(),
                this.pgn
        );
    }

    public void spielBeenden() {
        if (speichernAbfrage()) { // wenn ungleich
            infoBox(); // Warnung wenn spiel nicht gespeichert wurde
            if (option == JOptionPane.YES_OPTION) { // abbrechen
                exit(0);
            }
        } else {
            exit(0);
        }
    }

    public void spielSpeichern() {
        speichern.setPGN(pgn);
        speichern.speichereSpiel();
    }

    public void debugSetzeSpielfigur(int figur) {
        debug.setFigur(figur);
    }

    public void setPGN(int[][] pgn) {
        this.pgn = pgn;
        SpielData.setAktuellepgn(this.pgn);
        SpielData.erstelleSteinpgn(this.pgn);
        this.steinpgn = SpielData.steinpgn;
    }

    public static void markiereZieleFarbig(java.util.List<int[]> bewegungsziele) {
        for ( int i=0; i<8; i++ ) {
            for ( int j=0; j<8; j++ ) {
                faerbeHintergrund(i, j);
            }
        }
        for (int[] bewegungsziel: bewegungsziele) {
            feld[bewegungsziel[0]][bewegungsziel[1]].setBackground(Color.ORANGE);
        }

    }
}
