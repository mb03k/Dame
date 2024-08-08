
public class SpielLogikAlt {

    private int i_arr;
    private int j_arr;
    private int[][] pgn;

    private int aktuelleFigur;

    private int richtungHorizontal;
    private int richtungVertikal;

    private int newI;
    private int newJ;

    private int j_temp;
    private int besetzteFelder;
    private int i_schlagenMitte;
    private int j_schlagenMitte;

    private SpielLogikEnde endepruefen;

    private String werIstDran = "w"; // w -> weiß ; s -> schwarz

    public SpielLogikAlt() {
    }

    public void setSpielLogikEnde() {
         endepruefen = new SpielLogikEnde();
    }

    public void setAttributes(int i, int j, int[][] pgn) {
        this.pgn = pgn;
        this.i_arr = i;
        this.j_arr = j;
        this.aktuelleFigur = pgn[i][j];
    }

    public int[][] schlageOderBewege(int newI, int newJ) { // wenn leeres Feld angeklickt wird

        this.newI = newI;
        this.newJ = newJ;

        if (pgn != null && spielerIstDranUndRichtigeFigur(i_arr, j_arr)) {
            return setzeFiguren();
        }

        return null;
    }

    public boolean spielerIstDranUndRichtigeFigur(int i, int j) {
        if (werIstDran.equals("w") && pgn[i][j] >= 1) {
            return true;
        }
        else return werIstDran.equals("s") && pgn[i][j] <= -1;
    }

    public int[][] setzeFiguren() {
        switch (aktuelleFigur) {
            case 1:
            case -1: // Bauern
                waehleBauerAktion();
                break;

            case 2:
            case -2: // Damen
                waehleDameAktion();
                break;

            default:
        }
        return pgn;
    }

    // Bauern:

    public void waehleBauerAktion() {
        waehleBauerRichtung();
        if (bauerKannSchlagen()) {
            figurSchlagenZeichnen();
            checkBauerZuDame();
        } else {
            if (bauerKannSichBewegen()) {
                figurBewegenZeichnen();
                checkBauerZuDame();
            }
        }
    }

    public void waehleBauerRichtung() {
        if (neuesFeldUnterUrsprung()) {
            if (neuesFeldRechtsNebenUrsprung()) {
                this.richtungHorizontal = 1;
            } else {
                this.richtungHorizontal = -1;
            }
            this.richtungVertikal = 1;
        } else {
            if (neuesFeldRechtsNebenUrsprung()) {
                this.richtungHorizontal = 1;
            } else {
                this.richtungHorizontal = -1;
            }
            this.richtungVertikal = -1;
        }
    }

    public boolean bauerKannSchlagen() {
        if (bauerZiehtAufRichtigerDiagonale(2) && bauerSchlaegtAndereFarbe() && bauerSchlagenZweitesFeldFrei()) {
            i_schlagenMitte = (i_arr+richtungVertikal);
            j_schlagenMitte = (j_arr+richtungHorizontal);
            return true;
        }

        return false;
    }

    // Schwarz schlägt weiß und umgekehrt && weiß nach oben, schwarz nach unten
    public boolean bauerSchlaegtAndereFarbe() {
        try {
            if ((aktuelleFigur < 0) && (pgn[i_arr + richtungVertikal][j_arr + richtungHorizontal] > 0) // schwarz schlägt weiß
                    && (newI > i_arr)) {
                return true;
            } else {
                return (aktuelleFigur > 0) && (pgn[i_arr + richtungVertikal][j_arr + richtungHorizontal] < 0
                        && (newI < i_arr));
            }
        } catch (Exception ignored) {}

        return false;
    }

    public boolean bauerSchlagenZweitesFeldFrei() {
        try {
            return (pgn[i_arr + (richtungVertikal * 2)][j_arr + (richtungHorizontal * 2)] == 0);
        } catch (Exception ignored) {}

        return false;
    }

    public boolean bauerZiehtAufRichtigerDiagonale(int diagonalenWeite) {
        return ((Math.abs(newI-i_arr)==diagonalenWeite) && (Math.abs(newJ-j_arr)==diagonalenWeite));
    }

    public boolean bauerKannSichBewegen() {
        System.out.print("bauerKannSichBewegen(): ");
        try {
            System.out.print("return...\n\n");
            return ((pgn[i_arr + richtungVertikal][j_arr + richtungHorizontal] == 0)
                    && bauerZiehtAufRichtigerDiagonale(1)
                    && pruefeRichtigeBauerRichtung());
        } catch (Exception ignored) {}

        return false;
    }

    public boolean pruefeRichtigeBauerRichtung() {
        if ((istSchwarzeFigur()) && (neuesFeldUnterUrsprung())) { // schwarze Figur will nach oben
            return true;
        }
        return (!istSchwarzeFigur()) && (!neuesFeldUnterUrsprung());
    }

    public boolean istSchwarzeFigur() {
        return aktuelleFigur < 0;
    }

    public boolean neuesFeldUnterUrsprung() {
        return newI > i_arr;
    }

    public boolean neuesFeldRechtsNebenUrsprung() {
        return newJ > j_arr;
    }

    // wird nur aufgerufen wenn man mit einem Bauer auch schlagen / bewegen kann
    public void checkBauerZuDame() {
        if (newI == 0 || newI == 7) {
            if (aktuelleFigur == -1) { // aktuelle Figur: schwarzer Bauer
                pgn[newI][newJ] = -2;
            } else {
                pgn[newI][newJ] = 2;
            }
        }
    }

    public String aendereAktuellenSpieler() {
        if (werIstDran.equals("w")) { // weiß hat gespielt, jetzt schwarz
            return "s";
        }
        return "w";
    }

    // ---------------------------------------------------------------------------------------------------
    // Damen:

    public void waehleDameAktion() {
        if (dameRichtungSchlagenPruefen()) {
            figurSchlagenZeichnen();
        } else if (pruefeDameBewegen()) {
            figurBewegenZeichnen();
        }
    }

    // in welche Richtung geschlagen werden soll
    public boolean dameRichtungSchlagenPruefen() {
        if (i_arr > newI) { // nach oben (weiß)
            if (j_arr < newJ) { // nach rechts
                this.richtungHorizontal = 1;
            } else {
                this.richtungHorizontal = -1;
            }
            this.richtungVertikal = -1;
            return pruefeDameSchlagenOben();
        } else { // nach unten (schwarz)
            if (j_arr < newJ) { // nach rechts
                this.richtungHorizontal = 1;
            } else { // nach links
                this.richtungHorizontal = -1;
            }
            this.richtungVertikal = 1;
            return pruefeDameSchlagenUnten();
        }
    }

    public boolean figurenSindSelbeFarbe(int i) {
        if (pgn[i][j_temp] < 0) {
            return aktuelleFigur < 0; // beide figuren sind schwarz -> return true
        } else if (pgn[i][j_temp] > 0) {
            return aktuelleFigur > 0; // beide figuren sind weiß -> return true
        }
        return false;
    }

    public boolean pruefeDameBewegen() {
        return (figurLiegtAufDerSelbenDiagonale() && neuesFeldIstLeer() && besetzteFelder<1);
    }

    public boolean figurLiegtAufDerSelbenDiagonale() {
        return Math.abs(i_arr - newI) == Math.abs(j_arr - newJ);
    }

    public boolean neuesFeldIstLeer() {
        return pgn[newI][newJ] == 0;
    }

    public boolean pruefeDameSchlagenOben() {
        j_temp = (j_arr + richtungHorizontal);
        besetzteFelder = 0;
        i_schlagenMitte = -1;
        j_schlagenMitte = -1;

        // läuft die Diagonale nach oben ab und schaut welche Felder besetzt sind
        for (int i = (i_arr - 1); i > newI; i--) {
            dameSchlagenForSchleife(i);
        }

        if (besetzteFelder == 1) {
            this.newI = (i_schlagenMitte + richtungVertikal);
            this.newJ = (j_schlagenMitte + richtungHorizontal);
            return true;
        }

        return false;
    }

    public boolean pruefeDameSchlagenUnten() { // richtung: -1 = unten links; 1 = unten rechts
        j_temp = (j_arr + richtungHorizontal);
        besetzteFelder = 0;
        i_schlagenMitte = -1;
        j_schlagenMitte = -1;

        // läuft die Diagonale nach unten ab und schaut welche Felder besetzt sind
        // prüft NICHT ob auf dem neuen Feld ein Stein liegt
        for (int i = (i_arr + 1); i < newI; i++) {
            if (dameSchlagenForSchleife(i)) {
                return false;
            }
        }

        if (besetzteFelder == 1) {
            this.newI = (i_schlagenMitte + richtungVertikal);
            this.newJ = (j_schlagenMitte + richtungHorizontal);
            return true;
        }

        return false;
    }

    public boolean dameSchlagenForSchleife(int i) {
        if (pgn[i][j_temp] != 0) { // wenn Feld besetzt ist
            besetzteFelder++;
            if (figurenSindSelbeFarbe(i)) {
                return true;
            }
        }

        // wenn man schlagen kann indizes vom geschlagenen Stein setzen
        if ((pgn[i][j_temp] != 0) && (besetzteFelder == 1)) {
            i_schlagenMitte = i;
            j_schlagenMitte = j_temp;
        }

        j_temp += richtungHorizontal;

        return false;
    }

    public void figurBewegenZeichnen() {
        pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf leeres Feld zeichnen
        pgn[i_arr][j_arr] = 0; // alten Ort leeren

        werIstDran = aendereAktuellenSpieler();

        setSpielLogikEnde();
        endepruefen.setPGN(pgn);
        endepruefen.istSpielZuende();
    }

    public void figurSchlagenZeichnen() {
        pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf leeres Feld zeichnen
        pgn[i_schlagenMitte][j_schlagenMitte] = 0; // geschlagene Figur entfernen
        pgn[i_arr][j_arr] = 0; // alten Ort leeren

        werIstDran = aendereAktuellenSpieler();

        setSpielLogikEnde();
        endepruefen.setPGN(pgn);
        endepruefen.istSpielZuende();
    }

    public String getWerIstDran() {
        if (this.werIstDran.equals("w")) {
            return "Weiß";
        } else {
            return "Schwarz";
        }
    }

    // aktuelleFigur, newI, newJ, i_arr, j_arr, richtungVertikal, richtungHorizontal

    public void setAktuelleFigur(int aktuelleFigur) {
        this.aktuelleFigur = aktuelleFigur;
    }

    public void setNewI(int newI) {
        this.newI = newI;
    }

    public void setNewJ(int newJ) {
        this.newJ = newJ;
    }

    public void setI_arr(int i_arr) {
        this.i_arr = i_arr;
    }

    public void setJ_arr(int j_arr) {
        this.j_arr = j_arr;
    }

    public void setRichtungHorizontal(int richtungHorizontal) {
        this.richtungHorizontal = richtungHorizontal;
    }

    public void setRichtungVertikal(int richtungVertikal) {
        this.richtungVertikal = richtungVertikal;
    }

}