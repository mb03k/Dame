import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.exit;

public class SpielGUI extends Startbildschirm {

    private String modus;

    protected int[][] spielfigurenOrteINT = new int[][]{
            // Spielaufbau (PGN -> 'Portable Game Notation')
            //a  b  c  d  e  f  g  h
            // 0 = null
            // 1 = bauer schawrz
            // 2 = dame schwarz
            // 3 = bauer weiß
            // 4 = dame weiß
            {0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 2, 0, 0, 0},
            {0, 0, 0, 4, 0, 0, 0, 0},
            {3, 0, 3, 0, 3, 0, 3, 0},
            {0, 3, 0, 3, 0, 3, 0, 3},
            {3, 0, 3, 0, 3, 0, 3, 0}
    };

    protected JPanel[][] spielfigurenOrteJPANEL = new JPanel[][] {
        // Spielaufbau (PGN -> 'Portable Game Notation')
        //a  b  c  d  e  f  g  h
        { null, new SpFigZeichnen("black"), null, new SpFigZeichnen("black"), null, new SpFigZeichnen("black"), null, new SpFigZeichnen("black") },
        { new SpFigZeichnen("black"), null, new SpFigZeichnen("black"), null, new SpFigZeichnen("black"), null, new SpFigZeichnen("black"), null },
        { null, new SpFigZeichnen("black"), null, new SpFigZeichnen("black"), null, new SpFigZeichnen("black"), null, new SpFigZeichnen("black") },
        { null, null, new DameZeichnen("black"), new DameZeichnen("white"), null, null, null, null },
        { null, null, null, null, null, null, null, null },
        { new SpFigZeichnen("white"), null, new SpFigZeichnen("white"), null, new SpFigZeichnen("white"), null, new SpFigZeichnen("white"), null },
        { null, new SpFigZeichnen("white"), null, new SpFigZeichnen("white"), null, new SpFigZeichnen("white"), null, new SpFigZeichnen("white") },
        { new SpFigZeichnen("white"), null, new SpFigZeichnen("white"), null, new SpFigZeichnen("white"), null, new SpFigZeichnen("white"), null },
    };

    private JButton play;
   // private JFrame fenster;
    private JPanel[][] feld;
    private JMenuBar menueLeiste;
    private JMenu dateiMenue;
    private JMenuItem beenden;
    private JMenuItem startseite;
    private JMenuItem speichern;
    private int option;

    private SpielLogik logik;

    public SpielGUI() {

    }

    public SpielGUI(String mod) {
        //this.fenster = Startbildschirm.fenster;
        this.modus = mod;

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

        setSpielfeld();

        logik = new SpielLogik();

        fenster.setVisible(true);
    }

    public void setSpielfeld() {
        fenster.setLayout(new GridLayout(8,8));
        feld = new JPanel[8][8];

        // Spielfeld (und Farben) erstellen
        for ( int i=0; i<8; i++ ) { // vertikal
            for ( int j=0; j<8; j++ ) { // horizontal
                JPanel panel = new JPanel();

                final int iter = i; // sonst Fehler beim Übergeben im ActionListener
                final int jter = j;

                // GEHT SO!!!!
                play = new JButton("");
                play.addActionListener(e -> logik.setAttributes(iter, jter));

                // Button unsichtbar machen
                play.setBorderPainted(false);
                play.setContentAreaFilled(false);
                play.setFocusPainted(false);
                play.setOpaque(false);

                panel.add(play);

                //panel.addMouseListener(new MausKlickListener(i, j)); ersetzt mit JButton

                feld[i][j] = panel;
                feld[i][j].setLayout(new GridLayout());
                fenster.add(feld[i][j]);

                feld[i][j].setOpaque(true);
                feld[i][j].setBackground(Color.GRAY);

                if ((j+i) % 2 == 1) { // jedes zweite Feld färben
                    feld[i][j].setBackground(Color.DARK_GRAY);
                }

                feld[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));

                checkModus(i, j);
            }
        }
        feld[0][1] = null;
        System.out.println("Feld = null");
        checkModus();
    }

    public void checkModus(int i, int j) {
        if (modus.equals("spiel")) {
            setSpielfigurINT(i, j);
        }
    }

    public void checkModus() {
        // wenn Benutzer Debug-Modus angeklickt hat neuen Menüpunkt erstellen
        if (modus.equals("debug")) {
            JMenu debug = new JMenu("Debug");
            JMenuItem wFigur = new JMenuItem("Weiße Spielfigur");
            JMenuItem bFigur = new JMenuItem("Schwarze Spielfigur");
            JMenuItem wDame = new JMenuItem("Weiße Dame");
            JMenuItem bDame = new JMenuItem("Schwarze Dame");
            JMenuItem spielStarten = new JMenuItem("Spiel starten");

            wFigur.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            bFigur.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            wDame.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            bDame.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            debug.add(wDame);
            debug.add(wFigur);
            debug.add(bDame);
            debug.add(bFigur);
            debug.add(spielStarten);
            menueLeiste.add(debug);
        }
    }

    public void setSpielfigurINT(int i, int j) {
        switch (spielfigurenOrteINT[i][j]) {
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

    public void setSpielfigurJPANEL(int i, int j) {
        if (!(spielfigurenOrteJPANEL[i][j] == null)) {
            feld[i][j].add(spielfigurenOrteJPANEL[i][j]);
            fenster.add(feld[i][j]);
        }
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
                setStartbildschirm();
            }
        }
    }

    public JPanel[][] getPGN_JPANEL() {
        return spielfigurenOrteJPANEL;
    }

    public int[][] getPGN_INT() {
        /*System.out.println("1_getPGN:");
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                System.out.print(spielfigurenOrteINT[i][j]+"-");
            }
            System.out.println("");
        }
        System.out.println("---");*/
        return spielfigurenOrteINT;
    }

    public void setSpielfigurenOrteINT(int[][] orte) {
        this.spielfigurenOrteINT = orte;
    }
}
