package main;
import javax.swing.*;
import java.awt.*;
    
public class GamePanel extends JPanel implements Runnable{
    //resolution
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    //frame per second
    final int FPS = 60;

    public static KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    PlayManager pm;

    public GamePanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);;
        this.setLayout(null);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        pm = new PlayManager();
    }

    public void lauchGame(){
        gameThread = new Thread(this); 
        gameThread.start();
    }

    @Override
    public void run() {
        //game loop
        double drawInterval = 1000000000.0/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while(gameThread != null){
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;

            if(delta > 1){
                update();
                repaint();
                delta--;
            }
        }
        
    }

    public void update(){
        pm.update();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        pm.draw(g2);
    }
}