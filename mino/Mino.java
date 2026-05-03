package mino;
import java.awt.*;
import main.PlayManager;

public class Mino {
    public Block b[]     = new Block[4];
    public Block tempB[] = new Block[4];

    public void create(Color c) {
        b[0] = new Block(c); b[1] = new Block(c);
        b[2] = new Block(c); b[3] = new Block(c);
        tempB[0] = new Block(c); tempB[1] = new Block(c);
        tempB[2] = new Block(c); tempB[3] = new Block(c);
    }

    public void setXY(int x, int y) {}

    // direction: 0=trái, 1=phải, 2=xuống
    public void updateXY(int direction) {
        switch(direction) {
            case 0: for(Block block : b) block.x -= Block.SIZE; break;
            case 1: for(Block block : b) block.x += Block.SIZE; break;
            case 2: for(Block block : b) block.y += Block.SIZE; break;
        }
    }

    public void checkRotation() {
        // Lưu vị trí hiện tại vào tempB
        for(int i = 0; i < 4; i++) {
            tempB[i].x = b[i].x;
            tempB[i].y = b[i].y;
        }
        // Tính vị trí mới sau khi xoay
        for(int i = 1; i < 4; i++) {
            int dx = (b[i].x - b[0].x) / Block.SIZE;
            int dy = (b[i].y - b[0].y) / Block.SIZE;
            tempB[i].x = b[0].x + (-dy) * Block.SIZE;
            tempB[i].y = b[0].y + dx * Block.SIZE;
        }

        // điều chỉnh nếu tràn ra ngoài
        int shiftX = 0;
        for(Block tb : tempB) {
            if(tb.x < PlayManager.left_x)
                shiftX = Math.max(shiftX, PlayManager.left_x - tb.x);
        }
        if(shiftX == 0) {
            for(Block tb : tempB) {
                if(tb.x + Block.SIZE > PlayManager.right_x)
                    shiftX = Math.min(shiftX, PlayManager.right_x - tb.x - Block.SIZE);
            }
        }

        // Hủy xoay nếu đụng đáy hoặc block tĩnh
        for(Block tb : tempB) {
            if(tb.y + Block.SIZE > PlayManager.bottom_y) return;
            for(Block sb : PlayManager.staticBlocks) {
                if(tb.x + shiftX == sb.x && tb.y == sb.y) return;
            }
        }

        // Áp dụng xoay
        for(int i = 0; i < 4; i++) {
            b[i].x = tempB[i].x + shiftX;
            b[i].y = tempB[i].y;
        }
    }

    public void update() {}

    public void draw(Graphics2D g2) {
        for(Block block : b) block.draw(g2);
    }
}