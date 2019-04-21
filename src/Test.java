import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.sql.Time;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.*;



public class Test extends JPanel implements KeyListener, ActionListener {

    private int height, width;
    private Timer t = new Timer(5, this);
    //private Timer timer;
    private boolean first;
    private boolean displayWinMessage;
    private boolean displayWinMessage2;

    private HashSet<String> keys = new HashSet<String>();
    private HashSet<String> keys2 = new HashSet<String>();

    // pad
    private final int SPEED = 1;
    private int padH = 10, padW = 40;
    private int bottomPadX, topPadX;
    private int inset = 10;

    // ball
    private double ballX, ballY, velX = 1, velY = 1, ballSize = 20;

    // score
    private int scoreTop, scoreBottom;
    private Thread thread;
    private Thread thread2;
    private Thread thread3;

    public Test() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        first = true;
        t.setInitialDelay(300);
        t.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        height = getHeight();
        width = getWidth();

        // initial positioning
        if (first) {
            bottomPadX = width / 2 - padW / 2;
            topPadX = bottomPadX;
            ballX = width / 2 - ballSize / 2;
            ballY = height / 2 - ballSize / 2;
            first = false;
        }

        // bottom pad
        Rectangle2D bottomPad = new Rectangle(bottomPadX, height - padH - inset, padW, padH);
        g2d.fill(bottomPad);

        // top pad
        Rectangle2D topPad = new Rectangle(topPadX, inset, padW, padH);
        g2d.fill(topPad);

        // ball
        Ellipse2D ball = new Ellipse2D.Double(ballX, ballY, ballSize, ballSize);
        g2d.fill(ball);

        // scores
        String scoreB = "Player1 SCORE: " + Integer.toString(scoreBottom);
        String scoreT = "Player2 SCORE: " + Integer.toString(scoreTop);
        g2d.drawString(scoreB, 10, height - 40);
        g2d.drawString(scoreT, 10, 40);
        // messages
        String player1Won = "Player1 Won!";
        String player2Won = "Player2 Won!";
        if(displayWinMessage){
            g2d.drawString(player1Won,width/2 - 20,height/2 + 50);
            t.stop();
            int delay = 2000;
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    t.start();
                }
            };
            new Timer(delay, taskPerformer).start();
            displayWinMessage = false;
        }
        if(displayWinMessage2){
            g2d.drawString(player2Won,width/2 - 20,height/2 + 50);
            t.stop();
            int delay = 2000;
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    t.start();
                }
            };
            new Timer(delay, taskPerformer).start();
            displayWinMessage2 = false;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {


        Runnable ball = new Runnable() {
            @Override
            public void run() {
                // side walls
                if (ballX < 0 || ballX > width - ballSize) {
                    velX = -velX;
                }
                // top / down walls
                if (ballY < 0) {
                    velY = -velY;
                    ++ scoreBottom;
                    ballX = width / 2 - ballSize / 2;
                    ballY = height / 2 - ballSize / 2;
                    displayWinMessage = true;

                }

                if (ballY + ballSize > height) {
                    velY = -velY;
                    ++ scoreTop;
                    ballX = width / 2 - ballSize / 2;
                    ballY = height / 2 - ballSize / 2;
                    displayWinMessage2 = true;
                }
                // bottom pad
                if (ballY + ballSize >= height - padH - inset && velY > 0)
                    if (ballX + ballSize >= bottomPadX && ballX <= bottomPadX + padW)
                        velY = -velY;

                // top pad
                if (ballY <= padH + inset && velY < 0)
                    if (ballX + ballSize >= topPadX && ballX <= topPadX + padW)
                        velY = -velY;

                ballX += velX;
                ballY += velY;
            }
        };


        // pressed keys


        Runnable player1 = new Runnable() {
            @Override
            public void run() {
                //System.out.println("Run called by: " + Thread.currentThread().getName());
                if (keys.size() == 1) {
                    if (keys.contains("LEFT")) {
                        bottomPadX -= (bottomPadX > 0) ? SPEED : 0;
                    } else if (keys.contains("RIGHT")) {
                        bottomPadX += (bottomPadX < width - padW) ? SPEED : 0;
                    }
                }
            }
        };

        Runnable player2 = new Runnable() {
            @Override
            public void run() {
                if (keys2.size() == 1){
                    if (keys2.contains("D")) {
                        topPadX += (topPadX < width - padW) ? SPEED : 0;
                    } else if(keys2.contains("A")){
                        topPadX -= (topPadX > 0) ? SPEED : 0;
                    }
                }
            }
        };


        thread = new Thread(player1);
        thread2 = new Thread(player2);
        thread3 = new Thread(ball);
        thread.start();
        //System.out.println("Active threads: "+ Thread.activeCount()); //debug
        thread2.start();
        //System.out.println("Active threads: "+ Thread.activeCount()); //debug
        thread3.start();
        try {
            thread.join();
            thread2.join();
            thread3.join();
        }catch(Exception exception){

        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_LEFT:
                keys.add("LEFT");
                break;
            case KeyEvent.VK_RIGHT:
                keys.add("RIGHT");
                break;
            case KeyEvent.VK_D:
                keys2.add("D");
                break;
            case KeyEvent.VK_A:
                keys2.add("A");
                break;
            case KeyEvent.VK_SPACE:
                keys.add("SPACE");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_LEFT:
                keys.remove("LEFT");
                break;
            case KeyEvent.VK_RIGHT:
                keys.remove("RIGHT");
                break;
            case KeyEvent.VK_D:
                keys2.remove("D");
                break;
            case KeyEvent.VK_A:
                keys2.remove("A");
                break;
            case KeyEvent.VK_SPACE:
                keys.remove("SPACE");
                break;
        }
    }
}


