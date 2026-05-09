package mino;
import java.awt.*;
import main.PlayManager;

public class Mino {
    public enum MinoType { L1, L2, BAR, PLUS, SQUARE, Z1, Z2 }
    
    public Block b[]     = new Block[4];
    public Block tempB[] = new Block[4];
    public final MinoType type;

    public Mino(MinoType type) {
        this.type = type;
        Color c;
        switch(type) {
            case L1: c = Color.orange; break;
            case L2: c = Color.GREEN; break;
            case BAR: c = Color.CYAN; break;
            case PLUS: c = Color.BLUE; break;
            case SQUARE: c = Color.YELLOW; break;
            case Z1: c = Color.BLUE; break;
            case Z2: c = Color.RED; break;
            default: c = Color.WHITE; break;
        }
        b[0] = new Block(c); b[1] = new Block(c);
        b[2] = new Block(c); b[3] = new Block(c);
        tempB[0] = new Block(c); tempB[1] = new Block(c);
        tempB[2] = new Block(c); tempB[3] = new Block(c);
    }

    public void setXY(int x, int y) {
        b[0].x = x;
        b[0].y = y;
        switch(type) {
            case L1:
                b[1].x = b[0].x;              b[1].y = b[0].y - Block.SIZE;
                b[2].x = b[0].x;              b[2].y = b[0].y + Block.SIZE;
                b[3].x = b[0].x + Block.SIZE; b[3].y = b[0].y + Block.SIZE;
                break;
            case L2:
                b[1].x = b[0].x;              b[1].y = b[0].y - Block.SIZE;
                b[2].x = b[0].x;              b[2].y = b[0].y + Block.SIZE;
                b[3].x = b[0].x - Block.SIZE; b[3].y = b[0].y + Block.SIZE;
                break;
            case BAR:
                b[1].x = b[0].x;              b[1].y = b[0].y - Block.SIZE;
                b[2].x = b[0].x;              b[2].y = b[0].y + Block.SIZE;
                b[3].x = b[0].x;              b[3].y = b[0].y + Block.SIZE * 2;
                break;
            case PLUS:
                b[1].x = b[0].x;              b[1].y = b[0].y - Block.SIZE;
                b[2].x = b[0].x + Block.SIZE; b[2].y = b[0].y;
                b[3].x = b[0].x - Block.SIZE; b[3].y = b[0].y;
                break;
            case SQUARE:
                b[1].x = b[0].x;              b[1].y = b[0].y + Block.SIZE;
                b[2].x = b[0].x + Block.SIZE; b[2].y = b[0].y;
                b[3].x = b[0].x + Block.SIZE; b[3].y = b[0].y + Block.SIZE;
                break;
            case Z1:
                b[1].x = b[0].x - Block.SIZE; b[1].y = b[0].y;
                b[2].x = b[0].x;              b[2].y = b[0].y + Block.SIZE;
                b[3].x = b[0].x + Block.SIZE; b[3].y = b[0].y + Block.SIZE;
                break;
            case Z2:
                b[1].x = b[0].x + Block.SIZE; b[1].y = b[0].y;
                b[2].x = b[0].x;              b[2].y = b[0].y + Block.SIZE;
                b[3].x = b[0].x - Block.SIZE; b[3].y = b[0].y + Block.SIZE;
                break;
        }
    }

    // direction: 0=trái, 1=phải, 2=xuống
    public void updateXY(int direction) {
        switch(direction) {
            case 0: for(Block block : b) block.x -= Block.SIZE; break;
            case 1: for(Block block : b) block.x += Block.SIZE; break;
            case 2: for(Block block : b) block.y += Block.SIZE; break;
        }
    }

    public void checkRotation() {
        if (type == MinoType.SQUARE) return;
        
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