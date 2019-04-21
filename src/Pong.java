import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;


public class Pong extends JFrame {

    //screen size variables.
    int gWidth = 500;
    int gHeight = 400;
    Dimension screenSize = new Dimension(gWidth, gHeight);

    Image dbImage;
    Graphics dbGraphics;

    //ball object
    static Ball b = new Ball(250, 200);


    //constructor for window
    public Pong() {
        this.setTitle("Pong!");
        this.setSize(screenSize);
        this.setResizable(false);
        this.setVisible(true);
        this.setBackground(Color.DARK_GRAY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new KeyAdapter() {
            //Method for the key pressed
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                switch (code) {
                    case KeyEvent.VK_UP:
                        b.p2.setYDirection(-1);
                        break;
                    case KeyEvent.VK_DOWN:
                        b.p2.setYDirection(1);
                        break;
                    case KeyEvent.VK_W:
                        b.p1.setYDirection(-1);
                        break;
                    case KeyEvent.VK_S:
                        b.p1.setYDirection(1);
                        break;
                }
            }

            // Method for the key released
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                switch (code) {
                    case KeyEvent.VK_UP:
                        b.p2.setYDirection(0);
                        break;
                    case KeyEvent.VK_DOWN:
                        b.p2.setYDirection(0);
                        break;
                    case KeyEvent.VK_W:
                        b.p1.setYDirection(0);
                        break;
                    case KeyEvent.VK_S:
                        b.p1.setYDirection(0);
                        break;

                }
            }
        });
    }

    public static void main(String[] args) {
        Pong pg = new Pong();

        //create and start threads.
        Thread ball = new Thread(b);
        ball.start();
        Thread p1 = new Thread(b.p1);
        Thread p2 = new Thread(b.p2);
        Thread scoreboard = new Thread(b.scoreboard);
        p2.start();
        p1.start();

    }

    @Override
    public void paint(Graphics g) {
        dbImage = createImage(getWidth(), getHeight());
        dbGraphics = dbImage.getGraphics();
        draw(dbGraphics);
        g.drawImage(dbImage, 0, 0, this);
    }

    public void draw(Graphics g) {
        b.scoreboard.draw(g);
        g.setColor(Color.black);
        g.drawString("Player Blue: "+ b.p1.getScore(), 200, 50);
        g.setColor(Color.black);
        g.drawString("Player Red: "+ b.p2.getScore(), 200, 70);
        b.draw(g);
        b.p1.draw(g);
        b.p2.draw(g);
        repaint();
    }
}