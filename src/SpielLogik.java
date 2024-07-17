import javax.swing.*;
import java.awt.*;

public class SpielLogik extends SpielGUI {

    private int i_arr;
    private int j_arr;
    private JPanel[][] PGN;

    public SpielLogik(int i, int j) {
        this.i_arr = i;
        this.j_arr = j;
        this.PGN = getPGN();

        getFigur();
    }

    public void checkMoves() {

    }

    public int getFigur() {

        // hässliche Abfrage ob Bauer oder Dame (und jeweilige Farbe)
        // angeklickt wurden
        // -> DEFINITIV Verbesserungspotential (evtl. zweiten 2D Array implementieren
        // der nur Zahlen (0-4) hat?)
        if (PGN[i_arr][j_arr] instanceof SpFigZeichnen) {
            SpFigZeichnen fig = (SpFigZeichnen) PGN[i_arr][j_arr];
            if (fig.getColor().equals(Color.BLACK)) {
                System.out.println("BAUER SCHWARZ - i:"+i_arr+" - j:"+j_arr);
            } else if (fig.getColor().equals(Color.WHITE)) {
                System.out.println("BAUER WEIß - i:"+i_arr+" - j:"+j_arr);
            }
        }
        if (PGN[i_arr][j_arr] instanceof DameZeichnen) {
            DameZeichnen fig = (DameZeichnen) PGN[i_arr][j_arr];
            if (fig.getColor().equals(Color.BLACK)) {
                System.out.println("DAME SCHWARZ - i:"+i_arr+" - j:"+j_arr);
            } else if (fig.getColor().equals(Color.WHITE)) {
                System.out.println("DAME WEIß - i:"+i_arr+" - j:"+j_arr);
            }
        }
        return 1;
    }
}
