public class SpielLogik {

    private int[][] pgn;
    private int y_arr;
    private int x_arr;

    private int aktuelleFigur;

    private int richtungHorizontal;
    private int richtungVertikal;

    private int newY;
    private int newX;

    private int geschlagenerSteinY;
    private int geschlagenerSteinX;

    private int bauerSchlagenMittleresFeld;

    private int x_temp;
    private int besetzteFelder;

    private String werIstDran = "w"; // w -> weiß ; s -> schwarz

    SpielLogikEnde ende = new SpielLogikEnde();

    public SpielLogik() {
    }

    public void setAttributes(int y, int x, int[][] pgn) {
        this.pgn = pgn;
        this.y_arr = y;
        this.x_arr = x;
        this.aktuelleFigur = pgn[y][x];
    }

    public int[][] schlageOderBewege(int newY, int newX) { // wenn leeres Feld angeklickt wird

        this.newY = newY;
        this.newX = newX;

        if (pgn != null && neuesFeldLiegtAufDiagonale() && spielerIstDranUndRichtigeFigur()) {
            setzeFiguren();
            return pgn;
        }

        return null;
    }

    public boolean spielerIstDranUndRichtigeFigur() {
        if (werIstDran.equals("w") && pgn[y_arr][x_arr] > 0) {
            return true;
        }
        else return werIstDran.equals("s") && pgn[y_arr][x_arr] < 0;
    }

    public boolean neuesFeldLiegtAufDiagonale() {
        return (Math.abs(y_arr-newY)==Math.abs(x_arr-newX));
    }

    public void setzeFiguren() {
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
    }

    // zentrale Auswahl
    public void waehleBauerAktion() {
        setFigurSchlagenIndizes();

        if (moechteBauerBewegen() && bewegtInRichtigeRichtung()) {
            figurBewegenZeichnen();
        }
        else if (moechteBauerSchlagen() && bauerSchlagenMitteBesetzt()) {
            geschlagenerSteinY = y_arr+richtungVertikal;
            geschlagenerSteinX = x_arr+richtungHorizontal;
            figurSchlagenZeichnen();
        }
    }

    public boolean bewegtInRichtigeRichtung() {
        if ((istSchwarzeFigur()) && (neuesFeldUnterUrsprung())) { // schwarze Figur will nach oben
            return true;
        }
        return (!istSchwarzeFigur()) && (!neuesFeldUnterUrsprung());
    }

    public boolean moechteBauerBewegen() {
        return ((Math.abs(newY-y_arr)==1) && (Math.abs(newX-x_arr)==1));
    }

    public boolean moechteBauerSchlagen() {
        return ((Math.abs(newY-y_arr)==2) && (Math.abs(newX-x_arr)==2));
    }

    public boolean neuesFeldUnterUrsprung() {
        return y_arr < newY;
    }

    public boolean neuesFeldRechtsNebenUrsprung() {
        return x_arr < newX;
    }

    public boolean bauerSchlagenMitteBesetzt() {
        if (istSchwarzeFigur() && (bauerSchlagenMittleresFeld > 0) && (neuesFeldUnterUrsprung())) {
            return true;
        }
        else {
            return !istSchwarzeFigur() && (bauerSchlagenMittleresFeld < 0) && !neuesFeldUnterUrsprung();
        }
    }

    public boolean istSchwarzeFigur() {
        return aktuelleFigur < 0;
    }

    public void setFigurSchlagenIndizes() {
        if (neuesFeldUnterUrsprung()) { // nach unten
            this.richtungVertikal = 1;
        } else { // nach oben
            this.richtungVertikal = -1;
        }

        if (neuesFeldRechtsNebenUrsprung()) { // nach rechts
            this.richtungHorizontal = 1;
        } else {
            this.richtungHorizontal = -1;
        }
        bauerSchlagenMittleresFeld = pgn[y_arr+richtungVertikal][x_arr+richtungHorizontal];
    }

    public void checkBauerZuDame() {
        if (newY == 0 || newY == 7) {
            if (istSchwarzeFigur()) {
                pgn[newY][newX] = -2;
            } else {
                pgn[newY][newX] = 2;
            }
        }
    }

    public void figurBewegenZeichnen() {
        pgn[newY][newX] = pgn[y_arr][x_arr]; // Figur auf neues Feld zeichnen
        pgn[y_arr][x_arr] = 0; // alten Ort leeren

        y_arr = newY;
        x_arr = newX;

        werIstDran = aendereAktuellenSpieler(); // fliegt noch raus
        checkBauerZuDame();
    }

    public void figurSchlagenZeichnen() {
        pgn[newY][newX] = pgn[y_arr][x_arr]; // Figur auf neues Feld zeichnen
        pgn[geschlagenerSteinY][geschlagenerSteinX] = 0; // geschlagene Figur entfernen
        pgn[y_arr][x_arr] = 0; // alten Ort leeren

        y_arr = newY;
        x_arr = newX;

        werIstDran = aendereAktuellenSpieler(); // fliegt noch raus
        checkBauerZuDame();
        //checkZugzwang();
    }

    // -----------------------------------------------------------------

    public void waehleDameAktion() {
        if (dameRichtungSchlagenPruefen()) {
            figurSchlagenZeichnen();
        } else if (pruefeDameBewegen()) {
            figurBewegenZeichnen();
        }
    }

    public boolean dameRichtungSchlagenPruefen() {
        if (neuesFeldUnterUrsprung()) { // nach unten
            if (neuesFeldRechtsNebenUrsprung()) {
                this.richtungHorizontal = 1;
            } else {
                this.richtungHorizontal = -1;
            }

            return pruefeDameSchlagenUnten();
        }

        else { // nach oben
            if (neuesFeldRechtsNebenUrsprung()) {
                this.richtungHorizontal = 1;
            } else {
                this.richtungHorizontal = -1;
            }

            return pruefeDameSchlagenOben();
        }
    }

    public boolean pruefeDameSchlagenOben() {
        x_temp = (x_arr + richtungHorizontal);
        besetzteFelder = 0;
        geschlagenerSteinY = -1;
        geschlagenerSteinX = -1;

        // läuft die Diagonale nach oben ab und schaut welche Felder besetzt sind
        for (int i = (y_arr - 1); i > newY; i--) {
            if (dameSchlagenForSchleife(i)) {
                return false;
            }
        }

        if (besetzteFelder == 1) {
            this.newX = (geschlagenerSteinX + richtungHorizontal);
            return true;
        }

        return false;
    }

    public boolean pruefeDameSchlagenUnten() { // richtung: -1 = unten links; 1 = unten rechts
        x_temp = (x_arr + richtungHorizontal);
        besetzteFelder = 0;
        geschlagenerSteinY = -1;
        geschlagenerSteinX = -1;

        // läuft die Diagonale nach unten ab und schaut welche Felder besetzt sind
        // prüft NICHT ob auf dem neuen Feld ein Stein liegt
        for (int i = (y_arr + 1); i < newY; i++) {
            if (dameSchlagenForSchleife(i)) {
                return false;
            }
        }

        if (besetzteFelder == 1) {
            this.newX = (geschlagenerSteinX + richtungHorizontal);
            return true;
        }

        return false;
    }

    public boolean dameSchlagenForSchleife(int i) {
        if (pgn[i][x_temp] != 0) { // wenn Feld besetzt ist
            besetzteFelder++;
            if (figurenSindSelbeFarbe(i)) {
                return true;
            }
        }

        // wenn man schlagen kann indizes vom geschlagenen Stein setzen
        if ((pgn[i][x_temp] != 0) && (besetzteFelder == 1)) {
            geschlagenerSteinY = i;
            geschlagenerSteinX = x_temp;
        }

        x_temp += richtungHorizontal;

        return false;
    }



    public boolean figurenSindSelbeFarbe(int i) {
        if (pgn[i][x_temp] < 0) {
            return aktuelleFigur < 0; // beide figuren sind schwarz -> return true
        } else if (pgn[i][x_temp] > 0) {
            return aktuelleFigur > 0; // beide figuren sind weiß -> return true
        }
        return false;
    }

    public boolean pruefeDameBewegen() {
        return besetzteFelder<1;
    }

    public String getWerIstDran() {
        if (this.werIstDran.equals("w")) {
            return "Weiß";
        } else {
            return "Schwarz";
        }
    }

    public String aendereAktuellenSpieler() {
        if (werIstDran.equals("w")) { // weiß hat gespielt, jetzt schwarz
            return "s";
        }
        return "w";
    }



    // ---------------------------------------------------------
    // Zugzwang


    // GIBT DIE RICHTIGEN ERGEBNISSE!!!!!
    public void checkZugzwang() {
        if (aktuelleFigur == -1 || aktuelleFigur == 1) {
            newY = (y_arr + (richtungVertikal*2));
            newX = (x_arr + 2);
            setFigurSchlagenIndizes();
            checkZugzwangBauer();

            newX = (x_arr - 2);
            setFigurSchlagenIndizes();
            checkZugzwangBauer();
        } else {
            checkZugzwangDame();
        }
    }

    public boolean checkZugzwangBauer() {
        if (bauerSchlagenMitteBesetzt() && pgn[newY][newX]==0) {
            System.out.println("KANNST NOCHMAL SCHLAGEN");
            return true;
        }
        System.out.println("");
        return false;
    }

    public boolean checkZugzwangDame() {
        return false;
    }



}