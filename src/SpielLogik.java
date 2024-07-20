import javax.swing.*;
import java.awt.*;
import java.sql.SQLOutput;

public class SpielLogik {

    private int i_arr;
    private int j_arr;
    private JPanel[][] PGN_JPANEL;
    private int[][] pgn;

    public SpielLogik() {

    }

    public void setAttributes(int i, int j, int[][] pgn) {
        System.out.println("Set Attributes!");
        this.i_arr = i;
        this.j_arr = j;
        this.pgn = pgn;

        getFigur();
    }

    public int getFigur() {
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


    public void diagonaleLinks() {
        int zaehler = j_arr+1;
        // oberere rechte Diagonale - Weiß
        for (int i=i_arr-1; i>=0; i--) {
            if (pgn[i][zaehler] == 0) {
                System.out.println("Diagonal Rechts: "+i+" - "+zaehler+" ist frei!!!");
            } else {
                System.out.println("Diagonal Rechts: "+i+" - "+zaehler+" ist BESETZT!!!");
            }
            zaehler++;
        }
    }

    public void pruefeSpielzuegeWHITE() {
        try {
            int zaehler = j_arr+1;
            // oberere rechte Diagonale - Weiß
            for (int i=i_arr-1; i>=0; i--) {
                if (pgn[i][zaehler] == 0) {
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
                if (pgn[i][zaehler] == 0) {
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
                if (pgn[i][zaehler] == 0) {
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
            int zaehler = j_arr-1;
            for (int i=i_arr+1; i<=8; i++) {
                if (pgn[i][zaehler] == 0) {
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

    public void pruefeSpielzuegeDameWHITE() {
        try {
            int zaehler = j_arr+1;
            // untere rechte Diagonale - Schwarz
            for (int i = i_arr + 1; i <= 8; i++) {
                if (pgn[i][zaehler] == 0) {
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
            int zaehler = j_arr-1;
            for (int i=i_arr+1; i<=8; i++) {
                if (pgn[i][zaehler] == 0) {
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

    public void pruefeSpielzuegeDameBLACK() {
        try {
            int zaehler = j_arr+1;
            // untere rechte Diagonale - Schwarz
            for (int i = i_arr + 1; i <= 8; i++) {
                if (pgn[i][zaehler] == 0) {
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
            int zaehler = j_arr-1;
            for (int i=i_arr+1; i<=8; i++) {
                if (pgn[i][zaehler] == 0) {
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
}