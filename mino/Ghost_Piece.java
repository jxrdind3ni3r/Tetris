package mino;
import java.awt.Color;
import main.PlayManager;

public class Ghost_Piece extends Mino {
    public Ghost_Piece() { 
        create(new Color(255, 255, 255, 100));
    }

    public void updateInfo(Mino currentMino) {
        for (int i = 0; i < 4; i++) {
            b[i].x = currentMino.b[i].x;
            b[i].y = currentMino.b[i].y;
            Color c = currentMino.b[0].c;
            b[i].c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 80);
        }
        
        while (true) {
            boolean collision = false;
            for (Block block : b) {
                if (block.y + Block.SIZE >= PlayManager.bottom_y) {
                    collision = true;
                    break;
                }
                for (Block sb : PlayManager.staticBlocks) {
                    if (block.x == sb.x && block.y + Block.SIZE == sb.y) {
                        collision = true;
                        break;
                    }
                }
            }
            if (collision) {
                break;
            }
            for (int i = 0; i < 4; i++) {
                b[i].y += Block.SIZE;
            }
        }
    }
}