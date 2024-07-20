import javax.swing.*;

public class DebugModus extends SpielGUI{

    private int figur;

    public void setFigur(int figur) {
        System.out.println("setFigur: "+figur);
        this.figur = figur;
    }

    public int getDebugFigur() {
        switch(figur) {
            case 1:
                System.out.println("Switch 1");
                return 1;

            case 2:
                System.out.println("Switch 2");
                return 2;

            case 3:
                System.out.println("Switch 3");
                return 3;

            case 4:
                System.out.println("Switch 4");
                return 4;

            default:
                System.out.println("Switch 0");
                return 0;
        }
    }
}
