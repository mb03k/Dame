public class SpielLogikZugzwang {

    final private SpielLogik logik = new SpielLogik();

    public SpielLogikZugzwang() {

    }

    /*
        Beachten:
        Man darf nur mit DER SELBEN Figur erneut schlagen
     */

    public boolean checkBauerWeiss() {
        if (logik.moechteBauerSchlagen()) {
            return true;
        }
        return false;
    }

    public void checkBauerSchwarz() {

    }

}
