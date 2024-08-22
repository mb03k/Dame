package neueKlassen;

import java.util.ArrayList;
import java.util.List;

public abstract class Spielstein {
    public void setzeFigur() {}
    public void schlageFigur() {}
    public boolean istBewegungsfaehig() {
        return true;
    }

    public List<int[]> getBewegungsziele() {
        return new ArrayList<>();
    }

    public List<List<Integer>> getBewegungspfade() {
        return new ArrayList<>();
    }

    public int getLaufrichtung() {
        return 0;
    }

    public int getFarbe() {
        return 0;
    }

    public Boolean getZugzwang() {
        return false;
    }
}
