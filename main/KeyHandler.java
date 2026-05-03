package main;
import java.awt.event.*;

public class KeyHandler implements KeyListener {
    boolean leftPress, rightPress, upPress, downPress;

    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_LEFT)  leftPress  = true;
        if(code == KeyEvent.VK_RIGHT) rightPress = true;
        if(code == KeyEvent.VK_UP)    upPress    = true;
        if(code == KeyEvent.VK_DOWN)  downPress  = true;
    }

    @Override
    public void keyReleased(KeyEvent e) { 
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_LEFT)  leftPress  = false;
        if(code == KeyEvent.VK_RIGHT) rightPress = false;
        if(code == KeyEvent.VK_UP)    upPress    = false;
        if(code == KeyEvent.VK_DOWN)  downPress  = false;
    }
}