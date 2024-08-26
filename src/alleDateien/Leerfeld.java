package alleDateien;

public class Leerfeld extends Spielstein {
    private int pos_x;
    private int pos_y;

    private int farbe = 0;

    public Leerfeld(int pos_x, int pos_y) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }

    @Override
    public int getFarbe() {
        return farbe;
    }
}
