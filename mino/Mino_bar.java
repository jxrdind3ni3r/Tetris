package mino;
import java.awt.Color;

public class Mino_bar extends Mino {
    public Mino_bar() { create(Color.CYAN); }

    public void setXY(int x, int y) {
        b[0].x = x;      b[0].y = y;
        b[1].x = x;      b[1].y = y - Block.SIZE;
        b[2].x = x;      b[2].y = y + Block.SIZE;
        b[3].x = x;      b[3].y = y + Block.SIZE * 2;
    }
}