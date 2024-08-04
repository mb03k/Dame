
public class SpielLogik {

    private int i_arr;
    private int j_arr;
    private int[][] pgn;

    private int aktuelleFigur;

    private int schlagenMitte_i;
    private int schlagenMitte_j;

    private int richtungHorizontal;
    private int richtungVertikal;

    private int newI;
    private int newJ;

    int j_temp;
    int besetzteFelder;
    int i_schlagenMitte;
    int j_schlagenMitte;

    private String werIstDran = "w"; // w -> weiß ; s -> schwarz

    public SpielLogik() {
    }

    public void setAttributes(int i, int j, int[][] pgn) {
        this.pgn = pgn;
        this.i_arr = i;
        this.j_arr = j;
        this.aktuelleFigur = pgn[i][j];
    }

    public boolean spielerIstDranUndRichtigeFigur(int i, int j) {
        if (werIstDran.equals("w") && pgn[i][j] >= 1) {
            return true;
        }
        else return werIstDran.equals("s") && pgn[i][j] <= -1;
    }

    // sollte lieber "WaehltSchlagenOderBewegen" heißen
    public int[][] schlageOderBewege(int newI, int newJ) { // wenn leeres Feld angeklickt wird

        this.newI = newI;
        this.newJ = newJ;

        if (spielerIstDranUndRichtigeFigur(i_arr, j_arr)) {
            return setzeFiguren();
        }

        return null;
    }

    public int[][] setzeFiguren() {
        switch (aktuelleFigur) {
            case 1:
            case -1: // Bauern
                setzeBauer();
                break;

            case 2:
            case -2: // Damen
                bewegeDame();
                break;

            default:
        }
        return pgn;
    }

    public String getWerIstDran() {
        if (this.werIstDran.equals("w")) {
            return "Weiß";
        } else {
            return "Schwarz";
        }
    }

    // ---------------------------------------------------------------------------------------------------
    // Damen:

    public void bewegeDame() {
        if (!setzeDame()) { // Figur wurde von Dame NICHT geschlagen
            // Dame bewegen (zeichnen)
            if (figurLiegtAufDerSelbenDiagonale() && neuesFeldIstLeer()) {
                pgn[newI][newJ] = pgn[i_arr][j_arr]; // Dame auf leeres Feld zeichnen
                pgn[i_arr][j_arr] = 0; // alten Ort leeren
                werIstDran = aendereAktuellenSpieler();
            }
        }
    }

    // in welche Richtung geschlagen werden soll
    public boolean setzeDame() {
        if (i_arr > newI) { // nach oben (weiß)
            if (j_arr < newJ) { // nach rechts
                this.richtungHorizontal = 1;
            } else {
                this.richtungHorizontal = -1;
            }
            return dameSchlagenOben();
        } else { // nach unten (schwarz)
            if (j_arr < newJ) { // nach rechts
                this.richtungHorizontal = 1;
            } else { // nach links
                this.richtungHorizontal = -1;
            }
            return dameSchlagenUnten();
        }
    }

    public boolean figurLiegtAufDerSelbenDiagonale() {
        return Math.abs(i_arr - newI) == Math.abs(j_arr - newJ);
    }

    public boolean pruefeSelbeFarbe(int i) {
        if (pgn[i][j_temp] < 0) { // ein Feld auf der Diagonalen (schwarze Figur)
            return aktuelleFigur < 0; // schwarz schlägt schwarz
        } else if (pgn[i][j_temp] > 0) { // ein Feld auf der Diagonalen (weiße Figur)
            return aktuelleFigur > 0; // weiß schlägt weiß
        }
        return false;
    }

    // schlägt den Stein
    public boolean dameSchlagenOben() { // richtung: -1 = oben links; 1 = oben rechts
        j_temp = (j_arr + richtungHorizontal);
        besetzteFelder = 0;
        i_schlagenMitte = -1;
        j_schlagenMitte = -1;

        // läuft die Diagonale nach oben ab und schaut welche Felder besetzt sind
        for (int i = (i_arr - 1); i > newI; i--) {

            if (pgn[i][j_temp] != 0) { // wenn Feld besetzt ist
                if (pruefeSelbeFarbe(i)) {
                    return true;
                }
                besetzteFelder++;
            }

            // wenn man schlagen kann indizes vom geschlagenen Stein setzen
            if ((pgn[i][j_temp] != 0) && (besetzteFelder == 1)) {
                i_schlagenMitte = i;
                j_schlagenMitte = j_temp;
            }

            j_temp += richtungHorizontal;
        }

        // man kann nicht schlagen
        if (besetzteFelder == 1) { // schlagen
            schlagenDame(i_schlagenMitte, j_schlagenMitte, richtungHorizontal, -1);
            return true;
        } else return besetzteFelder > 1; // true wenn man nicht zeichnen soll
    }

    public boolean dameSchlagenUnten() { // richtung: -1 = unten links; 1 = unten rechts
        j_temp = (j_arr + richtungHorizontal);
        besetzteFelder = 0;
        i_schlagenMitte = -1;
        j_schlagenMitte = -1;

        // läuft die Diagonale nach unten ab und schaut welche Felder besetzt sind
        // prüft NICHT ob auf dem neuen Feld ein Stein liegt
        for (int i = (i_arr + 1); i < newI; i++) {
            if (pgn[i][j_temp] != 0) { // wenn Feld besetzt ist
                besetzteFelder++;
                if (pruefeSelbeFarbe(i)) {
                    return true;
                }
            }

            // wenn man schlagen kann indizes vom geschlagenen Stein setzen
            if ((pgn[i][j_temp] != 0) && (besetzteFelder == 1)) {
                i_schlagenMitte = i;
                j_schlagenMitte = j_temp;
            }

            j_temp += richtungHorizontal;
        }

        if (besetzteFelder == 1) { // schlagen
            schlagenDame(i_schlagenMitte, j_schlagenMitte, richtungHorizontal, 1);
            return true;
        } else return besetzteFelder > 1; // true wenn man nicht zeichnen soll
    }

    // wenn Dame eine Figur schlägt alles zeichnen
    public void schlagenDame(int i_schlagenMitte, int j_schlagenMitte, int horizontaleRichtung, int vertikaleRichtung) {
        pgn[i_schlagenMitte + vertikaleRichtung][j_schlagenMitte + horizontaleRichtung] = pgn[i_arr][j_arr]; // Dame auf leeres Feld zeichnen
        pgn[i_arr][j_arr] = 0; // alten Ort leeren
        pgn[i_schlagenMitte][j_schlagenMitte] = 0; // geschlagene Figur entfernen
        werIstDran = aendereAktuellenSpieler();
        //checkSpielende();
    }

    public boolean neuesFeldIstLeer() {
        return pgn[newI][newJ] == 0;
    }


    // ---------------------------------------------------------------------------------------------------
    // Bauern:


    public void setzeBauer() {
        if (i_arr > newI) { // nach oben (weiß)
            if (j_arr < newJ) { // nach rechts
                this.richtungHorizontal = 1;
            } else {
                this.richtungHorizontal = -1;
            }
            this.richtungVertikal = -1;
            bauerSchlagenOderBewegen();
        } else { // nach unten (schwarz)
            if (j_arr < newJ) { // nach rechts
                this.richtungHorizontal = 1;
            } else { // nach links
                this.richtungHorizontal = -1;
            }
            this.richtungVertikal = 1;
            bauerSchlagenOderBewegen();
        }
    }

    public void bauerSchlagenOderBewegen() { // für weiß (schwarz schlagen)
        try {
            if (bauerZiehtAufRichtigerDiagonale(2) && bauerSchlaegtAndereFarbe() && bauerSchlagenZweitesFeldFrei()) {
                schlagenMitte_i = (i_arr + 1);
                schlagenMitte_j = (j_arr + richtungHorizontal);
                bauerSchlagenZeichnen();
            } else {
                checkBauerBewegen();
            }
        } catch (Exception ignored) {}
    }

    // Schwarz schlägt weiß und umgekehrt && weiß nach oben, schwarz nach unten
    public boolean bauerSchlaegtAndereFarbe() {
        if ((aktuelleFigur < 0) && (pgn[i_arr+richtungVertikal][j_arr+richtungHorizontal] > 0) // schwarz schlägt weiß
            && (newI>i_arr)) {
            return true;
        }
        else return (aktuelleFigur > 0) && (pgn[i_arr + richtungVertikal][j_arr + richtungHorizontal] < 0
            && (newI < i_arr));
    }

    public boolean bauerSchlagenZweitesFeldFrei() {
        return (pgn[i_arr+(richtungVertikal*2)][j_arr+(richtungHorizontal*2)] == 0);
    }

    public boolean bauerZiehtAufRichtigerDiagonale(int diagonalenWeite) {
        return (Math.abs(newI-i_arr) == diagonalenWeite && Math.abs(newJ-j_arr) == diagonalenWeite);
    }

    public void bauerSchlagenZeichnen() {
        pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf leeres Feld zeichnen
        pgn[i_arr + richtungVertikal][j_arr + richtungHorizontal] = 0; // geschlagene Figur entfernen
        pgn[i_arr][j_arr] = 0; // alten Ort leeren
        werIstDran = aendereAktuellenSpieler();
        checkBauerZuDame();
        //checkSpielende();
    }

    public void checkBauerBewegen() {
        // Feld frei; richtige Diagonale;
        /*if ((pgn[i_arr + richtungVertikal][j_arr + richtungHorizontal] == 0) && (Math.abs(newI - i_arr) == 1)
                && ((j_arr + richtungHorizontal) == newJ) && pruefeRichtigeBauerRichtung()) {*/
        if ((pgn[i_arr + richtungVertikal][j_arr + richtungHorizontal] == 0) && bauerZiehtAufRichtigerDiagonale(1)
                && pruefeRichtigeBauerRichtung()) {
            bauerBewegen();
        }
    }

    public void bauerBewegen() {
        pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf neues Feld setzen
        pgn[i_arr][j_arr] = 0; // altes Feld der Figur leeren
        werIstDran = aendereAktuellenSpieler();
        checkBauerZuDame();
        //checkSpielende();
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
}