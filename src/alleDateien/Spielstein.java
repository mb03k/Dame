package alleDateien;

import java.util.ArrayList;
import java.util.HashMap;
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

    public HashMap<List<Integer>, List<int[]>> getBewegungszieleMitPfad() {
        return null;
    }

    public List<List<int[]>> getBewegungspfadeSchlagen() {
        return null;
    }
}
