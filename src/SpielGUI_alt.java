/*import javax.swing.*;
import java.awt.*;

import static java.lang.System.exit;

public class SpielGUI_alt extends Startbildschirm {

    private String modus;
    private JFrame fenster;
    private JPanel[][] feld;
    private JMenuBar menueLeiste;
    private JMenu dateiMenue;
    private JMenuItem beenden;
    private JMenuItem startseite;
    private JMenuItem speichern;
    private int option;

    public SpielGUI_alt() {}

    public SpielGUI_alt(String mod) {
        this.fenster = Startbildschirm.fenster;
        this.modus = mod;

        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                System.out.print(spielfigurenOrte[i][j]+" - ");
            }
        }

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

        fenster.setVisible(true);
    }

    public void setSpielfeld() {
        fenster.setLayout(new GridLayout(8,8));
        feld = new JPanel[8][8];

        // Spielfeld (und Farben) erstellen
        for ( int i=0; i<8; i++ ) { // vertikal
            for ( int j=0; j<8; j++ ) { // horizontal
                JPanel panel = new JPanel();
                panel.addMouseListener(new MausKlickListener(i, j));
                feld[i][j] = panel;
                feld[i][j].setLayout(new GridLayout());
                fenster.add(feld[i][j]);

                feld[i][j].setOpaque(true);
                feld[i][j].setBackground(Color.DARK_GRAY);

                if ((j+i) % 2 == 1) { // jedes zweite Feld färben
                    feld[i][j].setBackground(Color.GRAY);
                }

                feld[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));

                checkModus(i, j);
            }
        }
    }

    public void checkModus(int i, int j) {
        // wird 64x aufgerufen
        // wenn es nicht das standard-spiel ist und die letzte Iteration erfolgt ist, wird der Debug-Modus erstellt
        if (modus.equals("spiel")) {
            setSpielfigur(i, j);
        } else {
            if (i == 7 && j == 7) {
                setDebug();
            }
        }
    }

    public void setSpielfigur(int i, int j) {
        switch (spielfigurenOrte[i][j]) {
            case 1:
                SpFigZeichnen figur1 = new SpFigZeichnen("black");
                feld[i][j].add(figur1);
                fenster.add(feld[i][j]);
                break;

            case 2:
                SpFigZeichnen figur2 = new SpFigZeichnen("white");
                feld[i][j].add(figur2);
                fenster.add(feld[i][j]);
                break;

            case 3:
                DameZeichnen figur3 = new DameZeichnen("black");
                feld[i][j].add(figur3);
                fenster.add(feld[i][j]);
                break;

            case 4:
                DameZeichnen figur4 = new DameZeichnen("white");
                feld[i][j].add(figur4);
                fenster.add(feld[i][j]);
                break;

            default:
        }
    }

    public void setDebug() {
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

    public void setSpielfigurenOrte(int[][] aktSporte) {
        this.spielfigurenOrte = aktSporte;
    }

    public int[][] getSpielfigurenOrte() {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                System.out.print(spielfigurenOrte[i][j]+" - ");
            }
        }
        return this.spielfigurenOrte;
    }

    public void infoBox() {
        Object[] options = {"Fortfahren", "Abbrechen"};

        option = JOptionPane.showOptionDialog(null, "Das Spiel wird nicht gespeichert. Fortfahren?", "Spielspeicherung",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    public void setStartseite() {
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
}*/
