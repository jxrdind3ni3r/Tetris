package mino;
import java.awt.*;

public class Mino_z2 extends Mino {
    public Mino_z2() { create(Color.RED); }

    public void setXY(int x, int y) {
        b[0].x = x;              
        b[0].y = y;
        b[1].x = x + Block.SIZE; 
        b[1].y = y;
        b[2].x = x;              
        b[2].y = y + Block.SIZE;
        b[3].x = x - Block.SIZE; 
        b[3].y = y + Block.SIZE;
    }
}