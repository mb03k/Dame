import javax.swing.*;

import java.awt.*;
import java.util.Arrays;

import static java.lang.System.exit;

public class SpielGUI extends Main {

    private String modus;
    protected int[][] pgn=new int[8][8];
    private JButton spielfeldButtonListener;
    private JFrame fenster;
    private JPanel[][] feld;
    private int option;
    protected int[][] afterDebugPGN;
    private JPanel secondPanel;
    private JPanel innerFirstRow;
    private JPanel innerMiddleRow;
    private JPanel innerThirdRow;
    private String zug_grid = "Weiß";
    private JMenuBar menueLeiste;


    private SpielLogik logik;
    private DebugModus debug;
    private SpielSpeichern speichern;

    protected boolean werIstDran = true; // true -> weiß; false -> schwarz

    public SpielGUI() {
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

        feld = new JPanel[8][8];

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
        feld[i][j].setBackground(Color.GRAY);

        innerMiddleRow.add(feld[i][j]); // Spielfeld in die Mitte des Bildschirms setzen

        if ((j+i) % 2 == 1) { // jedes zweite Feld färben
            feld[i][j].setBackground(Color.DARK_GRAY);
        }

        // Button unsichtbar machen
        spielfeldButtonListener.setBorderPainted(false);
        spielfeldButtonListener.setContentAreaFilled(false);
        spielfeldButtonListener.setFocusPainted(false);
        spielfeldButtonListener.setOpaque(false);

        setSpielfigur(i, j);
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
                innerMiddleRow.add(feld[i][j]);
                break;

            case -2: // Dame schwarz
                DameZeichnen figur3 = new DameZeichnen("black");
                spielfeldButtonListener.add(figur3);
                innerMiddleRow.add(feld[i][j]);
                break;

            case 1: // Bauer weiß
                SpFigZeichnen figur2 = new SpFigZeichnen("white");
                spielfeldButtonListener.add(figur2);
                innerMiddleRow.add(feld[i][j]);
                break;

            case 2: // Dame weiß
                DameZeichnen figur4 = new DameZeichnen("white");
                spielfeldButtonListener.add(figur4);
                innerMiddleRow.add(feld[i][j]);
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
            spielfeldButtonListener.addActionListener(e -> logik.setAttributes(i, j, pgn));
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

    // Standardaufstellung zum spielen
    public void setStandardPGN() {
        this.pgn = new int[][] {
          // a, b, c, d, e, f, g, h
            // -1 = Bauer schwarz
            // -2 = Dame schwarz
            // 1 = Bauer weiß
            // 2 = Dame weiß
            // 0 = leer
        {0, -1, 0, -1, 0, -1, 0, -1},
        {-1, 0, -1, 0, -1, 0, -1, 0},
        {0, -1, 0, -1, 0, -1, 0, -1},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {1, 0, 1, 0, 1, 0, 1, 0},
        {0, 1, 0, 1, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 0}};
    }

    // Debug-Modus: neue Figuren anzeigen
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

    public void setGeladenePGN() {
        this.pgn = speichern.getGeladenePGN();
    }

    public void infoBox() {
        Object[] options = {"Fortfahren", "Abbrechen"};

        option = JOptionPane.showOptionDialog(null, "Das Spiel wird nicht gespeichert. Fortfahren?", "Spielspeicherung",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
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
    }
}
