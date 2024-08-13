package neueKlassen.mvc;

import neueKlassen.Bauer;
import neueKlassen.Dame;
import neueKlassen.Leerfeld;
import neueKlassen.Spielstein;

public class SpielData {
    public static int[][] standardpgn = new int[][] {
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

    public static Spielstein[][] steinpgn = new Spielstein[8][8];
    public static int[][] aktuellepgn = new int[8][8];

    public SpielData() {
        this.aktuellepgn = standardpgn;
        erstelleSteinpgn();
    }

    private void erstelleSteinpgn() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                waehleSpielstein(i, j);

            }
        }
    }

    private void waehleSpielstein(int i, int j) {
        switch (aktuellepgn[i][j]) {
            case 0:
                steinpgn[i][j] = new Leerfeld(i, j);
                break;
            case 1:
                steinpgn[i][j] = new Bauer(i, j, 'w');
                break;
            case -1:
                steinpgn[i][j] = new Bauer(i, j, 's');
                break;
            case 2:
                steinpgn[i][j] = new Dame();
                break;
            case -2:
                steinpgn[i][j] = new Dame();
                break;
            default:
                System.out.println("Unbekannter Spielstein fehler");
        }
    }


}
