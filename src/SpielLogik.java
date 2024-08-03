
public class SpielLogik {

    private int i_arr;
    private int j_arr;
    private int[][] pgn;

    private int aktuelleFigur;

    private int schlagenMitte_i;
    private int schlagenMitte_j;

    private int richtung;

    private int newI;
    private int newJ;

    int j_temp;
    int besetzteFelder;
    int i_schlagenMitte;
    int j_schlagenMitte;

    public SpielLogik() {
    }

    public void setAttributes(int i, int j, int[][] pgn) {
        this.i_arr = i;
        this.j_arr = j;
        this.aktuelleFigur = pgn[i][j];
        this.pgn = pgn;
    }

    public int[][] schlageOderBewege(int newI, int newJ) { // wenn leeres Feld angeklickt wird

        this.newI = newI;
        this.newJ = newJ;

        switch(aktuelleFigur) {
            case 1:
            case -1: // Bauern
                checkBauerSchlagen();
                break;

            case 2:
            case -2: // Damen
                bewegeDame();
                break;

            default:
        }

        return pgn;
    }

    // ---------------------------------------------------------------------------------------------------
    // Damen:

    public void bewegeDame() {
        if (!setzeDame()) { // Figur wurde von Dame NICHT geschlagen
            // Dame bewegen (zeichnen)
            if (pgn[newI][newJ] == 0 && dameLiegtAufDerSelbenDiagonale()) { // wenn neues Feld leer ist
                pgn[newI][newJ] = pgn[i_arr][j_arr]; // Dame auf leeres Feld zeichnen
                pgn[i_arr][j_arr] = 0; // alten Ort leeren
            }
        }
    }

    // in welche Richtung geschlagen werden soll
    public boolean setzeDame() {
        if (i_arr > newI) { // nach oben (weiß)
            if (j_arr < newJ) { // nach rechts
                this.richtung = 1;
            } else {
                this.richtung = -1;
            }
            return dameSchlagenOben();
        } else { // nach unten (schwarz)
            if (j_arr < newJ) { // nach rechts
                this.richtung = 1;
            } else { // nach links
                this.richtung = -1;
            }
            return dameSchlagenUnten();
        }
    }

    public boolean dameLiegtAufDerSelbenDiagonale() {
        // wenn neues Feld auf der selben Diagonale liegt
        return Math.abs(i_arr - newI) == Math.abs(j_arr - newJ);
    }

    public boolean pruefeSelbeFarbe(int i) {
        if (pgn[i][j_temp] < 0) { // ein Feld auf der Diagonalen (schwarze Figur)
            return aktuelleFigur < 0; // schwarz schlägt schwarz
        }
        else if (pgn[i][j_temp] > 0) { // ein Feld auf der Diagonalen (weiße Figur)
            return aktuelleFigur > 0; // weiß schlägt weiß
        }

        return false;
    }

    // schlägt den Stein
    public boolean dameSchlagenOben() { // richtung: -1 = oben links; 1 = oben rechts
        j_temp = (j_arr + richtung);
        besetzteFelder = 0;
        i_schlagenMitte = -1;
        j_schlagenMitte = -1;

        // läuft die Diagonale nach oben ab und schaut welche Felder besetzt sind
        for (int i = (i_arr-1); i > newI; i--) {

            if (pgn[i][j_temp] != 0) { // wenn Feld besetzt ist
                if (pruefeSelbeFarbe(i)) {
                    return true;
                }
                besetzteFelder++;
            }

            // wenn man schlagen kann indizes vom geschlagenen Stein setzen
            if ((pgn[i][j_temp] != 0) && (besetzteFelder == 1) ) {
                i_schlagenMitte = i;
                j_schlagenMitte = j_temp;
            }

            j_temp += richtung;
        }

        // man kann nicht schlagen
        if (besetzteFelder == 1) { // schlagen
            schlagenDame(i_schlagenMitte, j_schlagenMitte, richtung, -1);
            return true;
        } else return besetzteFelder > 1; // true wenn man nicht zeichnen soll
    }

    public boolean dameSchlagenUnten() { // richtung: -1 = unten links; 1 = unten rechts
        j_temp = (j_arr + richtung);
        besetzteFelder = 0;
        i_schlagenMitte = -1;
        j_schlagenMitte = -1;

        // läuft die Diagonale nach unten ab und schaut welche Felder besetzt sind
        // prüft NICHT ob auf dem neuen Feld ein Stein liegt
        for (int i = (i_arr+1); i < newI; i++) {
            if (pgn[i][j_temp] != 0) { // wenn Feld besetzt ist
                besetzteFelder++;
                if (pruefeSelbeFarbe(i)) {
                    return true;
                }
            }


            // wenn man schlagen kann indizes vom geschlagenen Stein setzen
            if ((pgn[i][j_temp] != 0) && (besetzteFelder == 1) ) {
                i_schlagenMitte = i;
                j_schlagenMitte = j_temp;
            }

            j_temp += richtung;
        }

        if (besetzteFelder == 1) { // schlagen
            schlagenDame(i_schlagenMitte, j_schlagenMitte, richtung, 1);
            return true;
        } else return besetzteFelder > 1; // true wenn man nicht zeichnen soll
    }

    // wenn Dame eine Figur schlägt alles zeichnen
    public void schlagenDame(int i_schlagenMitte, int j_schlagenMitte, int horizontaleRichtung, int vertikaleRichtung) {
        pgn[i_schlagenMitte+vertikaleRichtung][j_schlagenMitte + horizontaleRichtung] = pgn[i_arr][j_arr]; // Dame auf leeres Feld zeichnen
        pgn[i_arr][j_arr] = 0; // alten Ort leeren
        pgn[i_schlagenMitte][j_schlagenMitte] = 0; // geschlagene Figur entfernen
    }

    // ---------------------------------------------------------------------------------------------------
    // Bauern:




        public void checkBauerSchlagen() {
            if (i_arr > newI) { // nach oben (weiß)
                if (j_arr < newJ) { // nach rechts
                    checkObenSchlagen(1);
                } else {
                    checkObenSchlagen(-1);
                }
            }
            else { // nach unten (schwarz)
                if (j_arr < newJ) { // nach rechts
                    checkUntenSchlagen(1);
                } else { // nach links
                    checkUntenSchlagen(-1);
                }
            }
        }

        public void checkObenSchlagen(int richtung) { // für weiß (schwarz schlagen)
            try {
                if (pgn[i_arr - 1][j_arr + richtung] < 0) { // oben links oder rechts besetzt
                    if ((pgn[i_arr - 2][j_arr + (richtung*2)] == 0)) { // ob geschlagen werden kann (zweites Feld frei)
                        schlagenMitte_i = (i_arr + 1);
                        schlagenMitte_j = (j_arr + richtung);
                        bauerSchlagenOben(richtung);
                    }
                } else {
                    checkBauerBewegen(-1, richtung);
                }
            } catch (Exception ignored) {}
        }

        public void checkUntenSchlagen(int richtung) { // für schwarz (weiß schlagen)
            try {
                if (pgn[i_arr + 1][j_arr + richtung] > 0) { // oben links oder rechts besetzt
                    if ((pgn[i_arr + 2][j_arr + (richtung*2)] == 0)) { // ob geschlagen werden kann (zweites Feld frei)
                        schlagenMitte_i = (i_arr + 1);
                        schlagenMitte_j = (j_arr + richtung);
                        bauerSchlagenUnten(richtung);
                    }
                } else {
                    checkBauerBewegen(1, richtung);
                }
            } catch (Exception ignored) {}
        }

        // KANN MAN ZUSAMMENFASSEN

        public void bauerSchlagenOben(int richtung) {
            // wenn neues Feld auf der richtigen i- und j-Ebene liegt
            if ((Math.abs(newI - i_arr) == 2) && (Math.abs(newJ - j_arr)) == 2) {
                pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf leeres Feld zeichnen
                pgn[i_arr-1][j_arr+richtung] = 0; // geschlagene Figur entfernen
                pgn[i_arr][j_arr] = 0; // alten Ort leeren
                checkBauerZuDame();
            }
        }

        public void bauerSchlagenUnten(int richtung) {
            // wenn neues Feld auf der richtigen i- und j-Ebene liegt
            if (Math.abs(newI - i_arr) == 2 && (Math.abs(newJ - j_arr)) == 2) {
                pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf leeres Feld zeichnen
                pgn[i_arr+1][j_arr+richtung] = 0; // geschlagene Figur entfernen
                pgn[i_arr][j_arr] = 0; // alten Ort leeren
                checkBauerZuDame();
            }
        }

        public void checkBauerBewegen(int richtungVertikal, int richtungHorizontal) {
            try {
                bewegeBauer(richtungVertikal, richtungHorizontal);
            } catch (Exception ignored) {}
        }

        public void bewegeBauer(int richtungVertikal, int richtungHorizontal) throws Exception {
            // Feld frei, richtige i-Ebene, richtige j-Ebene
            if ((pgn[i_arr+richtungVertikal][j_arr+richtungHorizontal]==0) && (Math.abs(newI-i_arr)==1)
                    && ((j_arr+richtungHorizontal)==newJ) && pruefeRichtigeBauerRichtung()) {
                bauerBewegen();
            }
        }

        public void bauerBewegen() {
            pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf neues Feld setzen
            pgn[i_arr][j_arr] = 0; // altes Feld der Figur leeren
            checkBauerZuDame();
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





    // ob neues Feld an das aktuelle angrenzt
   /* public void checkAngrenzend(int richtung) {
        // wenn erstes Feld besetzt
        if (pgn[i_arr-1][j_arr+richtung] < 0) {
            if (pgn[i_arr-2][j_arr+(richtung*2)] == 0) { // wenn zweites Feld frei -> schlagen
                // schlagen...
            } else { // Bauer bewegen

            }
        }
    }

    public void bewegeBauer() {
        pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf neues Feld setzen
        pgn[i_arr][j_arr] = 0;
    }

    public void setBauern() {
        if (i_arr > newI) { // nach oben (weiß)
            if (j_arr < newJ) { // nach rechts
                checkObenSchlagen(1);
            } else {
                checkObenSchlagen(-1);
            }
        }
        else { // nach unten (schwarz)
            if (j_arr < newJ) { // nach rechts
                checkUntenSchlagen(1);
            } else { // nach links
                checkUntenSchlagen(-1);
            }
        }
    }

    // Bauer - Figur nach oben schlagen
    public boolean setSchlagOben() {
        // oben schlagen - prüfen
        if (( weiss_schlagen_i == newI) && (weiss_schlagen_j == newJ)) {
            pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf leeres Feld zeichnen
            pgn[schlagenMitte_i][schlagenMitte_j] = 0; // geschlagene Figur entfernen
            pgn[i_arr][j_arr] = 0; // alten Ort leeren
            return true;
        }
        return false;
    }

    // Bauer - Figur nach unten schlagen
    public boolean setSchlagUnten() {
        // unten schlagen - prüfen
        if (( black_schlagen_i == newI) && (black_schlagen_j == newJ)) {
            pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf leeres Feld zeichnen
            pgn[schlagenMitte_i][schlagenMitte_j] = 0; // geschlagene Figur entfernen
            pgn[i_arr][j_arr] = 0; // alten Ort leeren
            return true;
        }
        return false;
    }

    // Bauer (ohne schlagen) auf neues Feld zeichnen (weiß)
    public void setBewegenOben(int richtung) {
        if (newI == (i_arr-1) && (newJ == (j_arr+richtung))) {
            pgn[newI][newJ] = pgn[i_arr][j_arr]; // Figur auf neues Feld setzen
            pgn[i_arr][j_arr] = 0; // altes Feld der Figur leeren
        }
    }

    // Bauer (ohne schlagen) auf neues Feld zeichnen (schwarz)
    public void setBewegenUnten(int richtung) {
        if(newI == (i_arr+1) && (newJ == (j_arr+richtung))) { //
            pgn[newI][newJ] = pgn[i_arr][j_arr];
            pgn[i_arr][j_arr] = 0;
        }
    }

    /*public void checkSchlagenBauer(int richtungVertikal, int richtungHorizontal) {
        try {
            if ( ((pgn[i_arr +richtungVertikal][j_arr + richtungHorizontal] == 1)) || (pgn[i_arr - 1][j_arr + richtung] == 2) ) { // oben links oder rechts
                schlagenMitte_i = i_arr-1;
                schlagenMitte_j = j_arr+richtung;
                if ((pgn[i_arr - 2][j_arr + (richtung*2)] == 0)) { // ob geschlagen werden kann (zweites Feld frei)
                    weiss_schlagen_i = i_arr - 2;
                    weiss_schlagen_j = j_arr + (richtung*2);
                }
            }
        } catch(Exception ignored) {}
    }

    // Bauer schlagen prüfen
    public void checkObenSchlagen(int richtung) { // -1 = links; 1 = rechts
        try {
            if ( ((pgn[i_arr - 1][j_arr + richtung] == -1)) || (pgn[i_arr - 1][j_arr + richtung] == -2) ) { // oben links oder rechts
                schlagenMitte_i = i_arr-1;
                schlagenMitte_j = j_arr+richtung;
                if ((pgn[i_arr - 2][j_arr + (richtung*2)] == 0)) { // ob geschlagen werden kann (zweites Feld frei)
                    weiss_schlagen_i = i_arr - 2;
                    weiss_schlagen_j = j_arr + (richtung*2);
                }
            }
        } catch (Exception ignored) {}
    }

    // Bauer schlagen prüfen
    public void checkUntenSchlagen(int richtung) { // 1 = rechts; -1 = links
        try {
            if ((pgn[i_arr + 1][j_arr + richtung] < 0)) { // unten links oder rechts
                schlagenMitte_i = i_arr+1;
                schlagenMitte_j = j_arr+richtung;
                if ((pgn[i_arr + 2][j_arr + (richtung*2)] == 0)) {
                    black_schlagen_i = i_arr + 2;
                    black_schlagen_j = j_arr + (richtung*2);
                }
            }
        } catch (Exception ignored) {}
    }

    // ENTFÄLLT!!!!!
    /*public void setSchlagenDame(int[][] temp) {
        pgn[temp[0][0]-1][temp[1][0]-1] = pgn[i_arr][j_arr]; // Dame auf leeres Feld zeichnen
        pgn[i_arr][j_arr] = 0; // alten Ort leeren
        pgn[temp[0][0]][temp[1][0]] = 0; // geschlagene Figur entfernen
    }

    public void checkSchlagenDame() {
        int[][] temp = checkVerhinderungOL();
        if (temp!=null) {
            setSchlagenDame(temp);
        }
    }

    public int[][] checkVerhinderungOL() {
        int j_temp = 1;
        //for (int i=newI; i<(i_arr-1); i++) { // von oben nach unten

        for(int i=(i_arr); i>newI; i--) { // von unten nach oben prüfen
            if (!(pgn[i - 1][(j_arr - j_temp)] == 0)) { // wenn es belegt ist
                System.out.println((i-2)+" - "+((j_arr-j_temp)+1));
                return new int[][] { // der Ort an dem eine Figur steht
                    {i-1},
                    {j_arr-j_temp}
                };
            }
            j_temp++;
        }
        return null;
    }*/

// Ab hier eigentlich unnötig

    /*public void diagonaleLinksOben() {
        // linke Diagonale oben
        try {
            if ( (pgn[i_arr-zaehlerDoppelt][j_arr-zaehler] > 0) ) {
                System.out.println((i_arr-zaehlerDoppelt)+"-"+(j_arr-zaehler)+" -> Feld: BELEGT - LINKS");
                this.zaehler++;
                this.zaehlerDoppelt++;
            }
            if ( (pgn[i_arr-zaehlerDoppelt][j_arr-zaehler] == 0) ) {
                System.out.println((i_arr-zaehlerDoppelt)+"-"+(j_arr-zaehler)+" -> Feld: FREI - LINKS");

                this.zaehler++;
                this.zaehlerDoppelt++;
            }

            diagonaleLinksOben();

        } catch(Exception ignored) {}
    }

    public void diagonaleRechtsOben() {
        // rechte Diagonale oben
        try {
            if ( (pgn[i_arr-zaehlerDoppelt][j_arr+zaehler] > 0) ) {
                System.out.println((i_arr-zaehlerDoppelt)+"-"+(j_arr+zaehler)+" -> Feld: BELEGT - RECHTS");
                this.zaehler++;
                this.zaehlerDoppelt++;
            }
            if ( (pgn[i_arr-zaehlerDoppelt][j_arr+zaehler] == 0) ) {
                System.out.println((i_arr-zaehlerDoppelt)+"-"+(j_arr+zaehler)+" -> Feld: FREI - RECHTS");

                this.zaehler++;
                this.zaehlerDoppelt++;
            }


            diagonaleRechtsOben();

        } catch(Exception ignored) {}
    }



    public void setAttributes(int i, int j, int[][] pgn) {
        System.out.println("Attributes: i_arr: "+i+"-"+j);
        this.i_arr = i;
        this.j_arr = j;
        this.pgn = pgn;
        this.i_arrTestWHITE = i_arr;//-1;
        this.i_arrTestBLACK = i_arr;//+1;

        /*if (pgn[i_arr][j_arr] != 0) {
            getSpielzuege();
            pruefeSchlagWHITE();
        }*/
    /*}

    public void pruefeSchlagWHITE() { // weiße Steine
        for (int j : schlagen_linkeDiagonale) {
            System.out.print(j + "|");
        }
        System.out.println("\ni_arrTest: "+i_arrTestWHITE);

        // oben links
        if ((schlagen_linkeDiagonale[i_arrTestWHITE] == 1
            || schlagen_linkeDiagonale[i_arrTestWHITE] == 2)
            && schlagen_linkeDiagonale[i_arrTestWHITE-1] == 0) {

            pgn[i_arr][j_arr] = 0;

            schlagen_i = i_arr-1;
            schlagen_j = j_arr-1;

            System.out.println("Oben links Schlagbar");
            //setSchlagbar -> Leeres Feld anklickbar machen
        }
        // oben rechts
        if ((schlagen_rechteDiagonale[i_arrTestWHITE] == 1
                || schlagen_rechteDiagonale[i_arrTestWHITE] == 2)
                && schlagen_rechteDiagonale[i_arrTestWHITE-1] == 0) {

            schlagen_i = i_arr-1;
            schlagen_j = j_arr+1;

            System.out.println("Oben rechts Schlagbar");
            //setSchlagbar -> Leeres Feld anklickbar machen
        }
        else {
            System.out.println("NICHT schlagbar");
        }
    }

    public void pruefeSchlagBLACK() { // schwarze Steine
        for (int j : schlagen_linkeDiagonale) {
            System.out.print(j + "-");
        }
        System.out.println("\ni_arrTest: "+i_arrTestBLACK);

        if ((schlagen_linkeDiagonale[i_arrTestBLACK] == 3
                || schlagen_linkeDiagonale[i_arrTestBLACK] == 4)
                && schlagen_linkeDiagonale[i_arrTestBLACK+1] == 0) {

            System.out.println("Unten rechts Schlagbar");
            //setSchlagbar -> Leeres Feld anklickbar machen
        }
        else {
            System.out.println("NICHT schlagbar");
        }
    }


    public void getSpielzuege() {
        schlagen_linkeDiagonale = new int[] {
                -1,-1,-1,-1,-1,-1,-1,-1 // 1 = besetzt ; 0 = frei ; -1 = OutOfBounds
        };
        schlagen_rechteDiagonale = new int[] {
                -1,-1,-1,-1,-1,-1,-1,-1 // 1 = besetzt ; 0 = frei
        };

        schlagen_linkeDiagonale[i_arr] = 8;
        schlagen_rechteDiagonale[i_arr] = 8;

        switch (pgn[i_arr][j_arr]) {
            case 1:
                //System.out.println("BAUER SCHWARZ - i:"+i_arr+" - j:"+j_arr);

                //pruefeSpielzuegeBLACK();
                pruefeSchlagBLACK();
                break;

            case 2:
                //System.out.println("DAME SCHWARZ - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeDameBLACK();
                break;

            case 3:
                //System.out.println("BAUER WEIß - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeWHITE();
                break;

            case 4:
                //System.out.println("DAME WEIß - i:"+i_arr+" - j:"+j_arr);
                pruefeSpielzuegeDameWHITE();
                break;

            default:
                //System.out.println("NICHTS");

        }
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

    public void diagonaleLROben(int wert) {
        int index_links = i_arr-1;
        int index_rechts = i_arr+1;

        try {

            // MUSS GESPIEGELT WERDEN

            int zaehler = (j_arr + wert);
            // nach oben links oder rechts -> weiß
            for (int i=i_arr-1; i>=0; i--) {
                if (pgn[i][zaehler] == 0) {
                    if (wert<0) { // wird kleiner -> oben links
                        schlagen_linkeDiagonale[i] = 0;
                        //schlagen_linkeDiagonale[index_links] = 0;
                        index_links--;
                        //System.out.println("o_Diagonal Links: "+i+" - "+zaehler);

                    } else { // wird größer -> oben rechts

                        schlagen_rechteDiagonale[i] = 0;
                        //System.out.println("o_Diagonal Rechts: "+i+" - "+zaehler);

                    }
                } else if (pgn[i][zaehler] > 0){
                    if (wert<0) {

                        //System.out.println("SETZE 1-o1");
                        schlagen_linkeDiagonale[i] = 1;
                        //System.out.println("o_Diagonal Links: "+i+" - "+zaehler+" BESETZT!!!");

                    } else {

                        //System.out.println("SETZE 1-o2");
                        schlagen_rechteDiagonale[i] = 1;
                        //System.out.println("o_Diagonal Rechts: "+i+" - "+zaehler+" BESETZT!!!");

                    }
                }
                zaehler += wert;
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
    }

    public void diagonaleLRUnten(int wert) {
        int zaehler = (j_arr - wert);
        try {
            // nach unten links oder rechts -> schwarz
            for (int i = i_arr + 1; i < 8; i++) {
                if (pgn[i][zaehler] == 0) {

                    if (wert < 0) {
                        schlagen_linkeDiagonale[i] = 0;
                        //System.out.println("u_Diagonal Links: " + i + " - " + zaehler);
                    } else {
                        schlagen_rechteDiagonale[i] = 0;
                        //System.out.println("u_Diagonal Rechts: " + i + " - " + zaehler);
                    }
                } else if (pgn[i][zaehler] > 0) {

                    if (wert < 0) {
                        //System.out.println("SETZE 1-u1");
                        schlagen_linkeDiagonale[i] = 1;
                        //System.out.println("u_Diagonal Links: " + i + " - " + zaehler + " BESETZT!!!");
                    } else {
                        //System.out.println("SETZE 1-u2");
                        schlagen_rechteDiagonale[i] = 1;
                        //System.out.println("u_Diagonal Rechts: " + i + " - " + zaehler + " BESETZT!!!");
                    }
                }
                zaehler -= wert;
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
    }
*/}