import javax.swing.*;

public class SpielLogik {

    private int i_arr;
    private int j_arr;
    private JPanel[][] PGN_JPANEL;
    private int[][] pgn;
    private int[] schlagen;
    private int[] jetztAberSchlagen;

    public SpielLogik() {

    }

    // wenn man schlagen kann -> noch implementieren
    public void getSchlagmoeglichkeit() {
        for(int i=0; i<8; i++) {
            if (schlagen[i] > 0) {
                //jetztAberSchlagen[i] =
            }
        }
    }

    public void setAttributes(int i, int j, int[][] pgn) {
        System.out.println("Set Attributes!");
        this.i_arr = i;
        this.j_arr = j;
        this.pgn = pgn;

        getSpielzuege();
    }

    public int getSpielzuege() {
        //schlagen = new int[];
        //jetztAberSchlagen = new int[];

        switch (pgn[i_arr][j_arr]) {
            case 1:
                System.out.println("BAUER SCHWARZ - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeBLACK();
                break;

            case 2:
                System.out.println("DAME SCHWARZ - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeDameBLACK();
                break;

            case 3:
                System.out.println("BAUER WEIß - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeWHITE();
                break;

            case 4:
                System.out.println("DAME WEIß - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeDameWHITE();
                break;

            default:
                System.out.println("NICHTS");

        }
        return 1;
    }

    public void diagonaleLROben(int wert) {
        int zaehler = (j_arr + wert);
        try {
            // nach oben links oder rechts -> weiß
            for (int i=i_arr-1; i>=0; i--) {
                if (pgn[i][zaehler] == 0) {
                    if (wert<0) {
                        System.out.println("o_Diagonal Rechts: "+i+" - "+zaehler);
                    } else {
                        System.out.println("o_Diagonal Links: "+i+" - "+zaehler);
                    }
                } else {
                    if (wert<0) {
                        System.out.println("o_Diagonal Rechts: "+i+" - "+zaehler+" BESETZT!!!");
                    } else {
                        System.out.println("o_Diagonal Links: "+i+" - "+zaehler+" BESETZT!!!");
                    }
                }
                zaehler += wert;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
    }

    public void diagonaleLRUnten(int wert) {
        int zaehler = (j_arr - wert);
        try {
            // nach unten links oder rechts -> schwarz
            for (int i = i_arr + 1; i < 8; i++) {
                if (pgn[i][zaehler] == 0) {
                    if (wert < 0) {
                        System.out.println("u_Diagonal Links: " + i + " - " + zaehler);
                    } else {
                        System.out.println("u_Diagonal Rechts: " + i + " - " + zaehler);
                    }
                } else {
                    if (wert < 0) {
                        System.out.println("u_Diagonal Links: " + i + " - " + zaehler + " BESETZT!!!");
                    } else {
                        System.out.println("u_Diagonal Rechts: " + i + " - " + zaehler + " BESETZT!!!");
                    }
                }
                zaehler -= wert;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
    }

    public void pruefeSpielzuegeWHITE() {
        diagonaleLROben(1);
        diagonaleLROben(-1);
    }

    public void pruefeSpielzuegeBLACK() {
        diagonaleLRUnten(1);
        diagonaleLRUnten(-1);
    }

    public void pruefeSpielzuegeDameWHITE() {
        diagonaleLROben(1);
        diagonaleLROben(-1);
        diagonaleLRUnten(1);
        diagonaleLRUnten(-1);
    }

    public void pruefeSpielzuegeDameBLACK() {
        diagonaleLROben(1);
        diagonaleLROben(-1);
        diagonaleLRUnten(1);
        diagonaleLRUnten(-1);
    }
}