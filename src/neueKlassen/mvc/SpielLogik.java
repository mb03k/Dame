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
    private List<Integer> zielpunktListe;
    private int bewegungspfadIndex;

    private int geschlagenerSteinY;
    private int geschlagenerSteinX;

    private int bauerSchlagenMittleresFeld;

    private int x_temp;
    private int besetzteFelder;

    private int zugFarbe = 1; // 1 -> weiß ; -1 -> schwarz

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
        this.zielpunkt = new int[]{newY, newX}; //Hier schauen ob ersetzbar durch zielpunktListe
        this.zielpunktListe = new ArrayList<>(Arrays.asList(newY, newX));

        if (zugIstMoeglich() && richtigeFarbe() && testeZugzwang()) {
            zieheFigur();
            return pgn;
        }

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
        for (int[] moeglichesZiel: this.aktuellerStein.getBewegungsziele()) {
            if (Arrays.equals(moeglichesZiel, this.zielpunkt)) {
                moeglich = true;
                break;
            }
        }
        return moeglich;
    }

    private void zieheFigur() {
        //Pfad abgehen und alle Figuren auf dem Weg werden entfernt
        List<int[]> bewegungspfad = this.aktuellerStein.getBewegungszieleMitPfad().get(this.zielpunktListe);
        List<int[]> zuEntfernendeSteine = new ArrayList<>();
        int posX = this.x_arr;
        int posY = this.y_arr;

        if (bewegungspfadIstEnthalten(bewegungspfad, this.aktuellerStein.getBewegungspfadeSchlagen())) { //nur fürs Schlagen Figuren entfernen //contains() reicht nicht da ungleiche Objektreferenzen
            for (int[] schritt: bewegungspfad) {
                posX += schritt[1] - schritt[1] / abs(schritt[1]);
                posY += schritt[0] - schritt[0] / abs(schritt[0]);
                int[] entfernendeFigur = {posY, posX};
                zuEntfernendeSteine.add(entfernendeFigur);
                posX += schritt[1] / abs(schritt[1]);
                posY += schritt[0] / abs(schritt[0]);
                //als nächstes Prüfen, warum eine dame spielsteine wegnehmen kann und ein Bauer nicht mehr.. hier irgendwo fehler
            }
        }
        //aktualisiere pgn
        for (int[] stein: zuEntfernendeSteine) {
            this.pgn[stein[0]][stein[1]] = 0;
        }
        switch (newY) {
            case 0:
            case 7:
                this.pgn[newY][newX] = 2 * pgn[y_arr][x_arr] / abs(pgn[y_arr][x_arr]);
                break;
            default:
                this.pgn[newY][newX] = pgn[y_arr][x_arr];
                break;
        }
        this.pgn[y_arr][x_arr] = 0;

        this.zugFarbe = aendereAktuellenSpieler();
        //Als nächstes muss ein Bauer zur Dame werden, wenn er auf hinterster Linie
    }

    private boolean bewegungspfadIstEnthalten(List<int[]> bewegungspfad, List<List<int[]>> bewegungspfadeSchlagen) {
        boolean enthalten = false;
        for (List<int[]> pfad: bewegungspfadeSchlagen) {
            if (listenSindGleich(pfad, bewegungspfad)) {
                enthalten = true;
                break;
            }
        }
        return enthalten;
    }

    public static boolean listenSindGleich(List<int[]> list1, List<int[]> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            if (!Arrays.equals(list1.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    
    public int aendereAktuellenSpieler() {
        if (this.zugFarbe == 1) { // weiß hat gespielt, jetzt schwarz
            return -1;
        }
        return 1;
    }
    
    public String getWerIstDran() {
        if (this.zugFarbe == 1) {
            return "Weiß";
        } else {
            return "Schwarz";
        }
    }
    
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

