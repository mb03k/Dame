package neueKlassen;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static neueKlassen.mvc.SpielData.aktuellepgn;


public class Bauer extends Spielstein {
    private int pos_x;
    private int pos_y;

    private int farbe;

    private int laufrichtung;

    private Boolean zugzwang;
    private Boolean bewegungsfaehig;

    private List<int[]> bewegungsziele = new ArrayList<>();
    private List<List<Integer>> bewegungspfade = new ArrayList<>();



    public Bauer(int pos_x, int pos_y, int farbe) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.farbe = farbe;
        switch (this.farbe) {
            case 1:
                this.laufrichtung = -1; //weiß
                break;
            case -1:
                this.laufrichtung = 1; //schwarz
        }
        setBewegungsziele(); //ermittle mögliche Zielkoordinaten "bewegungsziele", die Bewegungspfade durch eine Liste aller schritte für jeden Pfad, unf prüfe ob Zugzwang besteht oder bewegungsfähigkeit gegeben ist
    }



    public void setBewegungsziele() {
        this.zugzwang = false;
        setBewegungspfade();

        for (List<Integer> pfad : this.bewegungspfade) {
            findeZielposition(pfad);
        }

        if (this.bewegungsziele.isEmpty()) {
            this.bewegungsfaehig = false;
        }
        else {
            this.bewegungsfaehig = true;
        }

    }

    private void findeZielposition(List<Integer> pfad) {
        int[] ziel = {this.pos_x, this.pos_y};
        for (int schritt: pfad) {
            ziel[1] += schritt;
            ziel[0] += abs(schritt) * this.laufrichtung;
        }
        this.bewegungsziele.add(ziel);
    }



    private void setBewegungspfade() {
        pruefeEinenSchritt(pos_x, pos_y); //Methode ruft sich selbst neu auf für jeden Schritt und speichert alle Pfade ab
    }

    // Methoden um Liste "bewegungspfade" zu bestimmen. Methoden rufen sich selbst neu auf und durchlaufen jeden Pfad.
    private void pruefeEinenSchritt(int x, int y) {
        int[] sprung = {-1, 1}; //y-Richtung (1), y-Richtung (2)
        pruefeBewegungsfaehigkeiten(sprung, x, y);
        verfolgeBewegungsfaehigkeiten(sprung, x, y);
    }

    private void pruefeEinenSchritt(int x, int y, List<Integer> pfadAlt) {
        int[] sprung = {-1, 1}; //y-Richtung (1), y-Richtung (2)
        pruefeBewegungsfaehigkeiten(sprung, x, y);
        verfolgeBewegungsfaehigkeiten(sprung, x, y, pfadAlt);
    }

    // Step 2
    private void verfolgeBewegungsfaehigkeiten(int[] sprungweiten, int x, int y) {
        for (int i = 0; i < 2; i++) {
            if (abs(sprungweiten[i]) == 1) {
                List<Integer> pfad = new ArrayList<>();
                pfad.add(sprungweiten[i]);
                this.bewegungspfade.add(pfad);
            }
            else if (abs(sprungweiten[i]) == 2) {
                List<Integer> pfad = new ArrayList<>();
                pfad.add(sprungweiten[i]);
                int neu_x = x + 2 * this.laufrichtung;
                int neu_y = y + sprungweiten[i];
                pruefeEinenSchritt(neu_x, neu_y, pfad);
            }

        }
    }

    private void verfolgeBewegungsfaehigkeiten(int[] sprungweiten, int x, int y, List<Integer> pfadAlt) {
        if (abs(sprungweiten[0]) == 2 || abs(sprungweiten[1]) == 2) {
            for (int i = 0; i < 2; i++) {
                testeVerzweigung(sprungweiten, x, y, pfadAlt, i);
            }
        }
        else {
            this.bewegungspfade.add(pfadAlt);
        }
    }

    private void testeVerzweigung(int[] sprungweiten, int x, int y, List<Integer> pfadAlt, int i) {
        if (abs(sprungweiten[i]) == 2) {
            List<Integer> pfad = pfadAlt;
            pfad.add(sprungweiten[i]);
            int neu_x = x + 2 * this.laufrichtung;
            int neu_y = y + sprungweiten[i];
            pruefeEinenSchritt(neu_x, neu_y, pfad);
        }
    }

    //Step 1
    private void pruefeBewegungsfaehigkeiten(int[] sprungweiten,int x, int y) {
        for (int i = 0; i < 2; i++) {
            pruefeBewegungsfaehigkeit(sprungweiten, i, x, y);
        }
    }

    private void pruefeBewegungsfaehigkeit(int[] sprungweiten, int i, int x, int y) {
        try {
            switch (aktuellepgn[x + this.laufrichtung][y + sprungweiten[i]] * this.laufrichtung) {
                case 2:
                case 1:
                    sprungweiten[i] *= 2;
                    schlagenOderNichtSchlagen(sprungweiten, i, x, y);
                    break;
                case 0:
                    break;
                case -1:
                case -2:
                    sprungweiten[i] = 0; //Weg ist von eigenem Spielstein versperrt
                    break;
                default:
                    System.out.println("Unbekannter Spielstein");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            sprungweiten[i] = 0;
        }
    }

    private void schlagenOderNichtSchlagen(int[] sprungweiten, int i, int x, int y) {
        if (aktuellepgn[x + 2 * this.laufrichtung][y + sprungweiten[i]] != 0) {
            sprungweiten[i] = 0;
        }
        else {
            this.zugzwang = true;
            //Jetzt könnte er sich merken, welches Feld er überspringen würde und welche Figur er löschen müsste... oder man macht das erst wenn die entscheidung getroffen ist anhand des Weges
        }
    }






    @Override
    public Boolean getZugzwang() {
        return zugzwang;
    }

    @Override
    public List<int[]> getBewegungsziele() {
        return bewegungsziele;
    }

    public Boolean getBewegungsfaehig() {
        return bewegungsfaehig;
    }

    @Override
    public List<List<Integer>> getBewegungspfade() {
        return bewegungspfade;
    }

    @Override
    public int getLaufrichtung() {
        return laufrichtung;
    }

    @Override
    public int getFarbe() {
        return farbe;
    }




}
