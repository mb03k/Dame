package neueKlassen.mvc;

import neueKlassen.Spielstein;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class SpielLogik {

    private int[][] pgn;
    private int y_arr;
    private int x_arr;
    private Spielstein[][] steinpgn;

    private int aktuelleFigur;
    private Spielstein aktuellerStein;

    private int richtungHorizontal;
    private int richtungVertikal;

    private int newY;
    private int newX;
    private int[] zielpunkt;
    private int bewegungspfadIndex;

    private int geschlagenerSteinY;
    private int geschlagenerSteinX;

    private int bauerSchlagenMittleresFeld;

    private int x_temp;
    private int besetzteFelder;

    private String werIstDran = "w"; // w -> weiß ; s -> schwarz
    private int zugFarbe = 1;

    //SpielLogikEnde ende = new SpielLogikEnde();

    public SpielLogik() {
    }

    public void setAttributes(int y, int x, int[][] pgn, Spielstein[][] steinpgn) {
        this.pgn = pgn;
        this.steinpgn = steinpgn;
        this.y_arr = y;
        this.x_arr = x;
        this.aktuelleFigur = pgn[y][x];
        this.aktuellerStein = steinpgn[y][x];
    }


    public int[][] schlageOderBewege(int newY, int newX) { // wenn leeres Feld angeklickt wird
        this.newY = newY;
        this.newX = newX;
        this.zielpunkt = new int[]{newY, newX};

        if (zugIstMoeglich() && richtigeFarbe() && testeZugzwang()) {
            zieheFigur();
            return pgn;
        }

//        if (pgn != null && neuesFeldLiegtAufDiagonale() && spielerIstDranUndRichtigeFigur()) {
//            setFigurSchlagenIndizes();
//
//            // if (zugzwang) {
//            //      schlagenPrüfenUndSo();
//            // }
//
//            setzeFiguren();
//            return pgn;
//        }

        return null; //noch return pgn einfügen, damit das neue pgn zurückgegeben wird
    }

    private boolean testeZugzwang() {
        List<Spielstein> steineUnterZugzwang = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                steineUnterZugzwang = fuelleListeZugzwang(i, j, steineUnterZugzwang);
            }
        }
        if (steineUnterZugzwang.size() > 0) {
            return steineUnterZugzwang.contains(this.aktuellerStein);
        }
        return true;
    }

    private List<Spielstein> fuelleListeZugzwang(int i, int j, List<Spielstein> steineUnterZugzwang) {
        if (steinpgn[i][j].getFarbe() == this.zugFarbe && steinpgn[i][j].getZugzwang()) {
            steineUnterZugzwang.add(steinpgn[i][j]);
        }
        return steineUnterZugzwang;
    }

    private boolean richtigeFarbe() {
        return this.zugFarbe == this.aktuellerStein.getFarbe();
    }

    private boolean zugIstMoeglich() {
        boolean moeglich = false;
        boolean schlagIstmoeglich = false;
        this.bewegungspfadIndex = 0;
        for (int[] moeglichesZiel: this.aktuellerStein.getBewegungsziele()) {
            if (Arrays.equals(moeglichesZiel, this.zielpunkt)) {
                moeglich = true;
                break;
            }
            this.bewegungspfadIndex++;
        }
        for (int[] moeglichesZiel: this.aktuellerStein.getBewegungsziele()) {
            if (abs(moeglichesZiel[0] - this.y_arr) > 1) {
                schlagIstmoeglich = true;
            }
        }
        if (schlagIstmoeglich && abs(this.zielpunkt[0] - this.y_arr) < 2) {
            moeglich = false;
        }
        return moeglich;
    }

    private void zieheFigur() {
        //Pfad abgehen und alle figuren auf dem Weg werden entfernen
        List<Integer> bewegungspfad = this.aktuellerStein.getBewegungspfade().get(this.bewegungspfadIndex);
        List<int[]> zuEntfernendeSteine = new ArrayList<>();
        int posX = this.x_arr;
        int posY = this.y_arr;
        for (int schritt: bewegungspfad) {
            if (abs(schritt) > 1) {
                posX += schritt - (schritt / abs(schritt));
                posY += (abs(schritt) - 1) * this.aktuellerStein.getLaufrichtung();
                int[] entfernendeFigur = {posY, posX};
                zuEntfernendeSteine.add(entfernendeFigur);
                posX += schritt / abs(schritt);
                posY += 1 * this.aktuellerStein.getLaufrichtung();
            }
        }
        //aktualisiere pgn
        for (int[] stein: zuEntfernendeSteine) {
            this.pgn[stein[0]][stein[1]] = 0;
        }
        this.pgn[newY][newX] = this.aktuellerStein.getFarbe();
        this.pgn[y_arr][x_arr] = 0;

        this.werIstDran = aendereAktuellenSpieler();
    }

    public String aendereAktuellenSpieler() {
        if (werIstDran.equals("w")) { // weiß hat gespielt, jetzt schwarz
            this.zugFarbe = -1;
            return "s";
        }
        this.zugFarbe = 1;
        return "w";
    }

//    public boolean spielerIstDranUndRichtigeFigur() {
//        if (werIstDran.equals("w") && !istSchwarzeFigur()) {
//            return true;
//        }
//        else return werIstDran.equals("s") && istSchwarzeFigur();
//    }
//
//    public boolean neuesFeldLiegtAufDiagonale() {
//        return (abs(y_arr-newY)== abs(x_arr-newX));
//    }
//
//    public void setzeFiguren() {
//        switch (aktuelleFigur) {
//            case 1:
//            case -1: // Bauern
//                waehleBauerAktion();
//                break;
//
//            case 2:
//            case -2: // Damen
//                waehleDameAktion();
//                break;
//
//            default:
//        }
//    }

    // zentrale Auswahl
//    public void waehleBauerAktion() {
//        //setFigurSchlagenIndizes();
//
//        if (moechteBauerBewegen() && bewegtInRichtigeRichtung()) {
//            figurBewegenZeichnen();
//        }
//        else if (moechteBauerSchlagen() && bauerSchlagenMitteBesetzt()) {
//            geschlagenerSteinY = y_arr+richtungVertikal;
//            geschlagenerSteinX = x_arr+richtungHorizontal;
//            figurSchlagenZeichnen();
//        }
//    }

//    public boolean bewegtInRichtigeRichtung() {
//        if (istSchwarzeFigur() && neuesFeldUnterUrsprung()) { // schwarze Figur will nach oben
//            return true;
//        }
//        return (!istSchwarzeFigur()) && (!neuesFeldUnterUrsprung());
//    }
//
//    public boolean moechteBauerBewegen() {
//        return ((abs(newY-y_arr)==1) && (abs(newX-x_arr)==1));
//    }

//    public boolean moechteBauerSchlagen() {
//        return ((abs(newY-y_arr)==2) && (abs(newX-x_arr)==2));
//    }
//
//    public boolean neuesFeldUnterUrsprung() {
//        return y_arr < newY;
//    }
//
//    public boolean neuesFeldRechtsNebenUrsprung() {
//        return x_arr < newX;
//    }
//
//    public boolean bauerSchlagenMitteBesetzt() {
//        try {
//            setBauerSchlagenMittleresFeld();
//            if (istSchwarzeFigur() && (bauerSchlagenMittleresFeld > 0) && (neuesFeldUnterUrsprung())) {
//                return true;
//            }
//            else {
//                return !istSchwarzeFigur() && (bauerSchlagenMittleresFeld < 0) && !neuesFeldUnterUrsprung();
//            }
//        } catch (Exception ignored) {}
//
//        return false;
//    }
//
//    public boolean istSchwarzeFigur() {
//        return aktuelleFigur < 0;
//    }
//
//    public void setFigurSchlagenIndizes() {
//        if (neuesFeldUnterUrsprung()) { // nach unten
//            this.richtungVertikal = 1;
//        } else { // nach oben
//            this.richtungVertikal = -1;
//        }
//
//        if (neuesFeldRechtsNebenUrsprung()) { // nach rechts
//            this.richtungHorizontal = 1;
//        } else {
//            this.richtungHorizontal = -1;
//        }
//    }
//
//    public void setBauerSchlagenMittleresFeld() throws Exception {
//        bauerSchlagenMittleresFeld = pgn[y_arr+richtungVertikal][x_arr+richtungHorizontal];
//    }
//
//    public void checkBauerZuDame() {
//        if (newY == 0 || newY == 7) {
//            if (istSchwarzeFigur()) {
//                pgn[newY][newX] = -2;
//            } else {
//                pgn[newY][newX] = 2;
//            }
//        }
//    }
//
//    public void figurBewegenZeichnen() {
//        pgn[newY][newX] = pgn[y_arr][x_arr]; // Figur auf neues Feld zeichnen
//        pgn[y_arr][x_arr] = 0; // alten Ort leeren
//
//        y_arr = newY;
//        x_arr = newX;
//
//        werIstDran = aendereAktuellenSpieler(); // fliegt noch raus
//        checkBauerZuDame();
//    }
//
//    public void figurSchlagenZeichnen() {
//        pgn[newY][newX] = pgn[y_arr][x_arr]; // Figur auf neues Feld zeichnen
//        pgn[geschlagenerSteinY][geschlagenerSteinX] = 0; // geschlagene Figur entfernen
//        pgn[y_arr][x_arr] = 0; // alten Ort leeren
//
//        y_arr = newY;
//        x_arr = newX;
//
//        //werIstDran = aendereAktuellenSpieler(); // fliegt noch raus
//        checkBauerZuDame();
//        checkZugzwang();
//    }

    // -----------------------------------------------------------------

//    public void waehleDameAktion() {
//        if (dameRichtungSchlagenPruefen()) {
//            figurSchlagenZeichnen();
//        } else if (pruefeDameBewegen()) {
//            figurBewegenZeichnen();
//        }
//    }
//
//    public boolean dameRichtungSchlagenPruefen() {
//        if (neuesFeldUnterUrsprung()) { // nach unten
//            if (neuesFeldRechtsNebenUrsprung()) {
//                this.richtungHorizontal = 1;
//            } else {
//                this.richtungHorizontal = -1;
//            }
//
//            return pruefeDameSchlagenUnten();
//        }
//
//        else { // nach oben
//            if (neuesFeldRechtsNebenUrsprung()) {
//                this.richtungHorizontal = 1;
//            } else {
//                this.richtungHorizontal = -1;
//            }
//
//            return pruefeDameSchlagenOben();
//        }
//    }
//
//    public boolean pruefeDameSchlagenOben() {
//        x_temp = (x_arr + richtungHorizontal);
//        besetzteFelder = 0;
//        geschlagenerSteinY = -1;
//        geschlagenerSteinX = -1;
//
//        // läuft die Diagonale nach oben ab und schaut welche Felder besetzt sind
//        for (int i = (y_arr - 1); i > newY; i--) {
//            if (dameSchlagenForSchleife(i)) {
//                return false;
//            }
//        }
//
//        if (besetzteFelder == 1) {
//            this.newX = (geschlagenerSteinX + richtungHorizontal);
//            this.newY = (geschlagenerSteinY + richtungVertikal);
//            return true;
//        }
//
//        return false;
//    }
//
//    public boolean pruefeDameSchlagenUnten() { // richtung: -1 = unten links; 1 = unten rechts
//        x_temp = (x_arr + richtungHorizontal);
//        besetzteFelder = 0;
//        geschlagenerSteinY = -1;
//        geschlagenerSteinX = -1;
//
//        // läuft die Diagonale nach unten ab und schaut welche Felder besetzt sind
//        // prüft NICHT ob auf dem neuen Feld ein Stein liegt
//        for (int i = (y_arr + 1); i < newY; i++) {
//            if (dameSchlagenForSchleife(i)) {
//                return false;
//            }
//        }
//
//        if (besetzteFelder == 1) {
//            this.newX = (geschlagenerSteinX + richtungHorizontal);
//            this.newY = (geschlagenerSteinY + richtungVertikal);
//            return true;
//        }
//
//        return false;
//    }
//
//    public boolean dameSchlagenForSchleife(int i) {
//        if (pgn[i][x_temp] != 0) { // wenn Feld besetzt ist
//            besetzteFelder++;
//            if (figurenSindSelbeFarbe(i)) {
//                return true;
//            }
//        }
//
//        // wenn man schlagen kann indizes vom geschlagenen Stein setzen
//        if ((pgn[i][x_temp] != 0) && (besetzteFelder == 1)) {
//            geschlagenerSteinY = i;
//            geschlagenerSteinX = x_temp;
//        }
//
//        x_temp += richtungHorizontal;
//
//        return false;
//    }
//
//
//
//    public boolean figurenSindSelbeFarbe(int i) {
//        if (pgn[i][x_temp] < 0) {
//            return aktuelleFigur < 0; // beide figuren sind schwarz -> return true
//        } else if (pgn[i][x_temp] > 0) {
//            return aktuelleFigur > 0; // beide figuren sind weiß -> return true
//        }
//        return false;
//    }
//
//    public boolean pruefeDameBewegen() {
//        return besetzteFelder<1;
//    }

    public String getWerIstDran() {
        if (this.werIstDran.equals("w")) {
            return "Weiß";
        } else {
            return "Schwarz";
        }
    }



    // ---------------------------------------------------------
    // Zugzwang

    private boolean zugzwang;

//    public void checkZugzwang() {
//        System.out.println("CHECKZUGZWANG()");
//        zugzwang = false;
//
//        if ((aktuelleFigur == -1 || aktuelleFigur == 1)) { // ob bauer
//            newY = (y_arr + (richtungVertikal * 2)); // nach oben / unten
//            newX = (x_arr + 2); // nach links / rechts
//
//            // rechts schlagen
//            if (!checkOutOfBounds()) {
//                if (moechteBauerSchlagen() && bauerSchlagenMitteBesetzt() && !neuesFeldIstBesetzt()) { // bauer kann rechts schlagen
//                    setFigurSchlagenIndizes();
//                    System.out.println("Rechts schlagbar: " + newY + "-" + newX);
//                    zugzwang = true;
//                }
//            }
//
//            newX = (x_arr - 2);
//            if (!checkOutOfBounds()) {
//                // links schlagen
//                if (moechteBauerSchlagen() && bauerSchlagenMitteBesetzt() && !neuesFeldIstBesetzt()) {
//                    setFigurSchlagenIndizes();
//                    System.out.println("links schlagbar: " + newY + "-" + newX);
//                    zugzwang = true;
//                }
//
//                System.out.println("---");
//            }
//
//            spielerAendern();
//        }
//
//        else{ // ob bauer
//            newY = (y_arr + (richtungVertikal * 2)); // nach oben / unten
//            newX = (x_arr + 2); // nach links / rechts
//
//            // rechts schlagen
//            if (!checkOutOfBounds()) {
//                if (dameRichtungSchlagenPruefen()) { // bauer kann rechts schlagen
//                    System.out.println("Rechts schlagbar: " + newY + "-" + newX);
//                    zugzwang = true;
//                }
//            }
//
//            newX = (x_arr - 2);
//            if (!checkOutOfBounds()) {
//                // links schlagen
//                if (dameRichtungSchlagenPruefen()) {
//                    System.out.println("links schlagbar: " + newY + "-" + newX);
//                    zugzwang = true;
//                }
//
//                System.out.println("---");
//            }
//
//            spielerAendern();
//        }
//
//    }

//    public void spielerAendern() {
//        if (!zugzwang) {
//            werIstDran = aendereAktuellenSpieler();
//        }
//    }
//
//    public boolean neuesFeldIstBesetzt() {
//        return pgn[newY][newX] != 0;
//    }
//
//    public boolean checkOutOfBounds() {
//        return (newY < 0 || newY > 7 || newX < 0 || newX > 7);
//    }












    public void aktiviereFeld(int i, int j, Spielstein[][] steinpgn, int[][] pgn) {
        System.out.println("Klick! " + i + " , " + j);
        aktiviereSpielstein(i, j, steinpgn, pgn);

    }

    private void aktiviereSpielstein(int i, int j, Spielstein[][] steinpgn, int[][] pgn) {
        List<int[]> bewegungsziele = steinpgn[i][j].getBewegungsziele();
        markiereZieleFarbig(bewegungsziele);
        setAttributes(i, j, pgn, steinpgn);
        if (bewegungsziele.size() == 0) {
            return;
        }


    }

    private void markiereZieleFarbig(List<int[]> bewegungsziele) {
        SpielGUI.markiereZieleFarbig(bewegungsziele);
    }

}
