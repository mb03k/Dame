import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MausKlickListener implements MouseListener {

    private int i_arr;
    private int j_arr;

    public MausKlickListener(int i, int j) {
        this.i_arr = i;
        this.j_arr = j;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //new SpielLogik(i_arr, j_arr);
        System.out.println("MausKlickListener - WTFFFFF");
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
