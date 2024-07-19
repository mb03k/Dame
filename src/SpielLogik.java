import javax.swing.*;
import java.awt.*;
import java.sql.SQLOutput;

public class SpielLogik extends SpielGUI {

    private int i_arr;
    private int j_arr;
    private JPanel[][] PGN_JPANEL;
    private int[][] PGN_INT;

    public SpielLogik() {

    }

    public void setAttributes(int i, int j) {
        this.i_arr = i;
        this.j_arr = j;
        this.PGN_JPANEL = getPGN_JPANEL();
        this.PGN_INT = getPGN_INT();

        getFigur();
        setSpiiel();
    }

    public int getFigur() {
        // hässliche Abfrage ob Bauer oder Dame (und jeweilige Farbe)
        // angeklickt wurden
        // -> DEFINITIV Verbesserungspotential (evtl. zweiten 2D Array implementieren
        // der nur Zahlen (0-4) hat?)
        /*if (PGN_JPANEL[i_arr][j_arr] instanceof SpFigZeichnen) {
            SpFigZeichnen fig = (SpFigZeichnen) PGN_JPANEL[i_arr][j_arr];
            if (fig.getColor().equals(Color.BLACK)) {
                System.out.println("BAUER SCHWARZ - i:"+i_arr+" - j:"+j_arr);
            } else if (fig.getColor().equals(Color.WHITE)) {
                System.out.println("BAUER WEIß - i:"+i_arr+" - j:"+j_arr);
            }
            pruefWeite = 2;
        }
        if (PGN_JPANEL[i_arr][j_arr] instanceof DameZeichnen) {
            DameZeichnen fig = (DameZeichnen) PGN_JPANEL[i_arr][j_arr];
            if (fig.getColor().equals(Color.BLACK)) {
                System.out.println("DAME SCHWARZ - i:"+i_arr+" - j:"+j_arr);
            } else if (fig.getColor().equals(Color.WHITE)) {
                System.out.println("DAME WEIß - i:"+i_arr+" - j:"+j_arr);
            }
            pruefWeite = 8;
        }*/

        switch (PGN_INT[i_arr][j_arr]) {
            case 1:
                System.out.println("BAUER SCHWARZ - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeBLACK();
                break;

            case 2:
                System.out.println("DAME SCHWARZ - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeBLACK();
                break;

            case 3:
                System.out.println("BAUER WEIß - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeWHITE();
                break;

            case 4:
                System.out.println("DAME WEIß - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeWHITE();
                break;

            default:

        }
        return 1;
    }

    public void pruefeSpielzuegeWHITE() {
        try {
            int zaehler = j_arr+1;
            // oberere rechte Diagonale - Weiß
            for (int i=i_arr-1; i>=0; i--) {
                if (PGN_INT[i][zaehler] == 0) {
                    System.out.println("Diagonal Rechts: "+i+" - "+zaehler+" ist frei!!!");
                } else {
                    System.out.println("Diagonal Rechts: "+i+" - "+zaehler+" ist BESETZT!!!");
                }
                zaehler++;
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("OutOfBounds! ");
        }

        try {
            // obere linke Diagonale
            int zaehler = j_arr-1;
            for (int i=i_arr-1; i>=0; i--) {
                if (PGN_INT[i][zaehler] == 0) {
                    System.out.println("Diagonal links: "+i+" - "+zaehler+" ist frei!!!");
                } else {
                    System.out.println("Diagonal links: "+i+" - "+zaehler+" ist BESETZT!!!");
                }
                zaehler--;
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Bountsi");
        }
    }

    public void pruefeSpielzuegeBLACK() {
        try {
            int zaehler = j_arr+1;
            // untere rechte Diagonale - Schwarz
            for (int i = i_arr + 1; i <= 8; i++) {
                if (PGN_INT[i][zaehler] == 0) {
                    System.out.println("Untere rechte Diagonale: " + i + " - " + zaehler + " ist frei!!!");
                } else {
                    System.out.println("Untere rechte Diagonale: " + i + " - " + zaehler + " ist BESETZT!!!");
                }
                zaehler++;
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("OutOfBounds!");
        }

        try {
            // untere linke Diagonale
            int zaehler = j_arr+1;
            for (int i=i_arr+1; i<=8; i++) {
                if (PGN_INT[i][zaehler] == 0) {
                    System.out.println("Untere linke Diagonale : "+i+" - "+zaehler+" ist frei!!!");
                } else {
                    System.out.println("Untere linke Diagonale : "+i+" - "+zaehler+" ist BESETZT!!!");
                }
                zaehler--;
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("BountsiBounts");
        }
    }

    public void setSpiiel() {

        // wenn aufgerufen wird, werden die Attribute aus Startbildschirm überschrieben und es kommen Fehler
        //setSpielfeld();
    }

}