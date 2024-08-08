public class SpielLogikEnde {

    // Prüfe Spielende:
    // -> !! 'aktuelleFigur' wird überschrieben !!
    // -> genau so wie i_arr, j_arr, newI und newJ

    public SpielLogikEnde() {
        System.out.println("SpielLogikEnde()");
    }

    final private SpielLogikAlt logik = new SpielLogikAlt();
    private int[][] pgn;

    private int i_arr;
    private int j_arr;
    private int aktuelleFigur;

    private int richtungVertikal;
    private int richtungHorizontal;

    private int newI;
    private int newJ;

    private int zaehlerSchwarz;
    private int zaehlerWeiss;

    public void istSpielZuende() {
        if (!setzeSchleife()) {
            System.out.println("SPIEL IST ZU ENDE!");
        }
    }

    // wenn sich Bauer bewegen oder schlagen kann wird der Zähler erhöht
    public void pruefeBauerBewegen() {
        if (logik.bauerKannSichBewegen()) {
            if (logik.istSchwarzeFigur()) {
                System.out.println("schwarz++");
                zaehlerSchwarz++;
            }

            else {
                System.out.println("weiß++");
                zaehlerWeiss++;
            }
        }
    }

    public void pruefeBauerSchlagen() {
        if (logik.bauerKannSchlagen()) {
            if (logik.istSchwarzeFigur()) {
                System.out.println("schwarz++");
                zaehlerSchwarz++;
            }

            else {
                System.out.println("weiß++");
                zaehlerWeiss++;
            }
        }
    }

    public boolean setzeSchleife() {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (zaehlerSchwarz > 0 && pgn[i][j] < 0) { // schwarz kann sich bewegen aber man schaut nochmal schwarz an
                    continue;
                }

                if (zaehlerWeiss > 0 && pgn[i][j] > 0) {
                    continue;
                }

                if (zaehlerSchwarz > 0 && zaehlerWeiss > 0) {
                    return true;
                }
                pruefePGN(i, j);
            }
        }

        return false;
    }

    public void pruefePGN(int i, int j) {
        this.i_arr = i;
        this.j_arr = j;

        switch (pgn[i][j]) {
            case 0:
                break;

            case -1:
                System.out.println("case schwarzer Bauer");

                //aktuelleFigur = -1;
                logik.setAktuelleFigur(-1);
                logik.setRichtungVertikal(1);
                //this.richtungVertikal = 1;

                // nach rechts bewegen
                logik.setNewI(i_arr+1);
                logik.setNewJ(j_arr+1);
                logik.setRichtungHorizontal(1);
                //this.newI = i_arr + 1;
                //this.newJ = j_arr + 1;
                //this.richtungHorizontal = 1;
                pruefeBauerBewegen();

                // nach links bewegen
                //this.newJ = j_arr - 1;
                //this.richtungHorizontal = -1;
                logik.setNewJ(j_arr-1);
                logik.setRichtungHorizontal(-1);
                pruefeBauerBewegen();


                // nach rechts schlagen
                //this.newI = i + 2;
                //this.newJ = j_arr + 2;
                //this.richtungHorizontal = 1; // rechts
                logik.setNewI(i_arr+1);
                logik.setNewJ(j_arr+1);
                logik.setRichtungHorizontal(1);
                pruefeBauerSchlagen();

                // nach links schlagen
                this.newJ = j_arr - 2;
                this.richtungHorizontal = -1;
                pruefeBauerSchlagen();
                break;

            case 1:
                System.out.println("case weißer Bauer");

                aktuelleFigur = 1;
                this.richtungVertikal = -1;

                // nach rechts bewegen
                this.newI = i_arr - 1;
                this.newJ = j_arr + 1;
                this.richtungHorizontal = 1;
                pruefeBauerBewegen();

                // nach links bewegen
                this.newI = i_arr - 1;
                this.newJ = j_arr - 1;
                this.richtungHorizontal = -1;
                pruefeBauerBewegen();


                // nach rechts schlagen
                this.newI = i - 2;
                this.newJ = j_arr + 2;
                this.richtungHorizontal = 1; // rechts
                pruefeBauerSchlagen();

                // nach links schlagen
                this.newJ = j_arr - 2;
                this.richtungHorizontal = -1; // rechts
                pruefeBauerSchlagen();
                break;
        }
    }


    /*
    case 2:
                case -2:

                    aktuelleFigur = pgn[i][j];

                    this.richtungVertikal = -1;
                    // nach rechts oben bewegen
                    this.newI = i_arr - 1;
                    this.newJ = j_arr + 1;
                    this.richtungHorizontal = 1;
                    pruefeBauerBewegen();

                    // nach links oben bewegen
                    this.newJ = j_arr - 1;
                    this.richtungHorizontal = -1;
                    pruefeBauerBewegen();

                    this.richtungVertikal = 1;
                    // nach rechts unten bewegen
                    this.newI = i_arr + 1;
                    this.newJ = j_arr + 1;
                    this.richtungHorizontal = 1;
                    pruefeBauerBewegen();

                    // nach links unten bewegen
                    this.newI = i_arr + 1;
                    this.newJ = j_arr - 1;
                    this.richtungHorizontal = -1;
                    pruefeBauerBewegen();




                    this.newI = i - 2;
                    // nach rechts oben schlagen
                    this.newJ = j_arr + 2;
                    this.richtungHorizontal = 1; // rechts
                    pruefeBauerSchlagen();

                    // nach links oben schlagen
                    this.newJ = j_arr - 2;
                    this.richtungHorizontal = -1;
                    pruefeBauerSchlagen();

                    aktuelleFigur = 1;
                    this.richtungVertikal = -1;

                    this.newI = i + 2;
                    // nach rechts unten schlagen
                    this.newJ = j_arr + 2;
                    this.richtungHorizontal = 1; // rechts
                    pruefeBauerSchlagen();

                    // nach links unten schlagen
                    this.newJ = j_arr - 2;
                    this.richtungHorizontal = -1; // rechts
                    pruefeBauerSchlagen();

     */

    public void setPGN(int[][] pgn) {
        this.pgn = pgn;
    }
}
