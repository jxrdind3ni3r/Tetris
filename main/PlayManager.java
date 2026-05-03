package main;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import mino.*;

public class PlayManager {
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x, right_x, top_y, bottom_y;

    Mino currentMino;
    Mino nextMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    final int NEXT_MINO_X;
    final int NEXT_MINO_Y;

    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    boolean gameOver = false;
    int score = 0, level = 1, lines = 0;

    int autoDropCounter = 0;
    int autoDropInterval = 60; // frames (giảm khi level tăng)

    int leftKeyCounter = 0;
    int rightKeyCounter = 0;

    Random random = new Random();

    public PlayManager() {
        left_x  = (GamePanel.WIDTH  / 2) - (WIDTH  / 2);
        right_x = left_x + WIDTH;
        top_y   = (GamePanel.HEIGHT / 2) - (HEIGHT / 2);
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y  + Block.SIZE;
        NEXT_MINO_X  = right_x + 175;
        NEXT_MINO_Y  = bottom_y - 100;

        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);
    }

    private Mino pickMino() {
        switch(random.nextInt(7)) {
            case 0: return new Mino_L1();
            case 1: return new Mino_L2();
            case 2: return new Mino_bar();
            case 3: return new Mino_Plus();
            case 4: return new Mino_s();
            case 5: return new Mino_z1();
            default: return new Mino_z2();
        }
    }

    public void update() {
        if(gameOver) return;

        // Xoay — chỉ 1 lần mỗi lần nhấn
        if(GamePanel.keyH.upPress) {
            currentMino.checkRotation();
            GamePanel.keyH.upPress = false;
        }

        // Di trái 
        if(GamePanel.keyH.leftPress) {
            leftKeyCounter++;
            if(leftKeyCounter == 1 || (leftKeyCounter > 12 && leftKeyCounter % 4 == 0))
                if(!checkLeftCollision()) currentMino.updateXY(0);
        } else { leftKeyCounter = 0; }

        // Di phải
        if(GamePanel.keyH.rightPress) {
            rightKeyCounter++;
            if(rightKeyCounter == 1 || (rightKeyCounter > 12 && rightKeyCounter % 4 == 0))
                if(!checkRightCollision()) currentMino.updateXY(1);
        } else { rightKeyCounter = 0; }

        // Tự rơi xuống 
        autoDropCounter++;
        int interval = GamePanel.keyH.downPress ? 3 : autoDropInterval;
        if(autoDropCounter >= interval) {
            if(!checkBottomCollision()) {
                currentMino.updateXY(2);
            } else {
                landMino();
            }
            autoDropCounter = 0;
        }
    }

    private boolean checkLeftCollision() {
        for(Block b : currentMino.b) {
            if(b.x - Block.SIZE < left_x) return true;
            for(Block sb : staticBlocks)
                if(b.y == sb.y && b.x - Block.SIZE == sb.x) return true;
        }
        return false;
    }

    private boolean checkRightCollision() {
        for(Block b : currentMino.b) {
            if(b.x + Block.SIZE >= right_x) return true;
            for(Block sb : staticBlocks)
                if(b.y == sb.y && b.x + Block.SIZE == sb.x) return true;
        }
        return false;
    }

    private boolean checkBottomCollision() {
        for(Block b : currentMino.b) {
            if(b.y + Block.SIZE >= bottom_y) return true;
            for(Block sb : staticBlocks)
                if(b.x == sb.x && b.y + Block.SIZE == sb.y) return true;
        }
        return false;
    }

    private void landMino() {
        for(Block b : currentMino.b) staticBlocks.add(b);
        checkLine();

        // Spawn mino tiếp theo
        currentMino = nextMino;
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);

        // Kiểm tra game over
        for(Block b : currentMino.b)
            for(Block sb : staticBlocks)
                if(b.x == sb.x && b.y == sb.y) { gameOver = true; return; }
    }

    private void checkLine() {
        int columns = WIDTH / Block.SIZE; // = 12
        int linesCleared = 0;
        boolean found;
        do {
            found = false;
            for(int y = top_y; y < bottom_y; y += Block.SIZE) {
                final int checkY = y;
                long count = staticBlocks.stream().filter(b -> b.y == checkY).count();
                if(count >= columns) {
                    found = true;
                    linesCleared++;
                    staticBlocks.removeIf(b -> b.y == checkY);
                    for(Block b : staticBlocks)
                        if(b.y < checkY) b.y += Block.SIZE;
                    break;
                }
            }
        } while(found);

        int[] scoreTable = {0, 100, 300, 500, 800};
        if(linesCleared >= 1 && linesCleared <= 4)
            score += scoreTable[linesCleared] * level;
        lines += linesCleared;
        level = lines / 10 + 1;
        autoDropInterval = Math.max(5, 60 - (level - 1) * 5);
    }

    public void draw(Graphics2D g2) {
        // Khung play area
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        // Khung next mino
        int nx = right_x + 100;
        int ny = bottom_y - 200;
        g2.drawRect(nx, ny, 200, 200);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("NEXT", nx + 68, ny - 10);

        // Score / Level / Lines
        int infoX = right_x + 110;
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("SCORE",        infoX, top_y + 50);
        g2.setFont(new Font("Arial", Font.BOLD, 26));
        g2.drawString("" + score,     infoX, top_y + 85);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("LEVEL: " + level, infoX, top_y + 140);
        g2.drawString("LINES: " + lines, infoX, top_y + 170);

        // button guide
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        int hx = left_x - 180;
        g2.drawString("← → Di chuyển", hx, top_y + 60);
        g2.drawString("↑  Xoay",        hx, top_y + 90);
        g2.drawString("↓  Rơi nhanh",   hx, top_y + 120);

        // draw placed block
        for(Block b : staticBlocks) b.draw(g2);


        // draw curr mino and next mino
        if(currentMino != null) currentMino.draw(g2);
        if(nextMino != null) nextMino.draw(g2);



        // game end when block reach out of rec
        if(gameOver) {
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRect(left_x, top_y, WIDTH, HEIGHT);
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            g2.drawString("GAME", left_x + 70, top_y + HEIGHT/2 - 25);
            g2.drawString("OVER", left_x + 70, top_y + HEIGHT/2 + 35);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 22));
            g2.drawString("Score: " + score, left_x + 95, top_y + HEIGHT/2 + 80);
        }
    }
}