//Importing all required libraries
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;


public class Pong extends JFrame { //Thanks to using JFrame framework I can display graphic objects, and create windows

    //screen size //Here I'm setting size of the window, in which game is displayed
    private int gWidth = 500;
    private int gHeight = 400;
    //Graphics creation //And here I'm creating that window
    private  Dimension screenSize = new Dimension(gWidth, gHeight);
    private Image dbImage;  //Image and Graphics are used to display elements of the game on the screen
    private Graphics dbGraphics;

    //Objects  //Here I'm creating every object used in the game. Every object has been created in their own thread
    private static Ball b = new Ball(250, 200);
    private static Thread ball = new Thread(b);
    private static Thread p1 = new Thread(b.p1);
    private static Thread p2 = new Thread(b.p2);
    private static Thread scoreboard = new Thread(b.scoreboard);


    //constructor for window
    public Pong() {
        this.setTitle("Pong!");  //Title of the window
        this.setSize(screenSize); //Size of the window
        this.setResizable(false);  //Disabling resizing of the window
        this.setVisible(true); //And setting the windows visible
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Here I can set behaviour of my program after I close the window with it. Of course here the best option is that it will exit, when I close it
        this.addKeyListener(new KeyAdapter() {  //Adding KeyListeners to be able to read input from the keyboard, and then passing it to the program itself.
                                                // KeyAdapter works in threads by their own, so even if I don't create thread for every signle object, everything should work just fine (as far as steering goes)
            //Listener for keys being pressed down
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();  //getKeyCode() gets the code, that tells us which key was pressed
                switch (code) {
                    case KeyEvent.VK_UP:
                        b.p2.setYDirection(-1); //Arrow UP will set Y direction of the right paddle upwards (so it's Y coordinate will be getting smaller)
                        break;
                    case KeyEvent.VK_DOWN:
                        b.p2.setYDirection(1); //Same thing for arrow DOWN, so the right paddle can go downwards
                        break;
                    case KeyEvent.VK_W:
                        b.p1.setYDirection(-1); //Exactly the same operation for W and S keys for left paddle
                        break;
                    case KeyEvent.VK_S:
                        b.p1.setYDirection(1);
                        break;
                }
            }
            // Method for the key released
            public void keyReleased(KeyEvent e) { //We need to KeyListeners also for keys being released, so we can stop the movement of paddles
                int code = e.getKeyCode();
                switch (code) {
                    case KeyEvent.VK_UP:
                        b.p2.setYDirection(0); //We are just setting Y direction of paddle to zero, so it won't be changing
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
    public static void main(String[] args) { //The main function of the program
        new Pong();  //Creating of new Pong object (so basically the whole game)
        //Starting all the threads
        ball.start();
        p2.start();
        p1.start();
        scoreboard.start();
        try{ //Joining all the threads, so they will stay synchronized in the one frame
            p1.join();
            p2.join();
            ball.join();
            scoreboard.join();
        }
        catch(Exception e){ //Catching and printing errors, that can occur while joining threads
            System.out.println(e);
        }
    }

    @Override
    public void paint(Graphics g) { //Overriding the method Pain, so it will work as we need
        dbImage = createImage(getWidth(), getHeight()); //Creating new Image
        dbGraphics = dbImage.getGraphics();
        draw(dbGraphics); //Drawing the image on the screen
        g.drawImage(dbImage, 0, 0, this);
    }

    public void draw(Graphics g) { //Method used for drawing every single object on the screen
        if(b.whoWon() == 0) { //IF that is checking if the game is still on (so no one won)
            setBackground(Color.GRAY); //Setting the background color
            b.scoreboard.draw(g); //Drawing the scoreboard
            g.setColor(Color.black); //Change of color
            g.drawString("Player Blue: " + b.p1.getScore(), 200, 50); //Result of player 1
            g.setColor(Color.black);
            g.drawString("Player Red: " + b.p2.getScore(), 200, 70); //Result of player 2
            b.draw(g); //Ball
            b.p1.draw(g); //Player 1
            b.p2.draw(g); //Player 2
        }
        if(b.whoWon() != 0){ //If someone already won
            setBackground(Color.white); //Change of color
            g.drawString("GAME FINISHED, won Player " + b.whoWon(), 170, 200);  //Final screen
            ball.stop(); //Stopping ball thread, so the game won't restart
        }
        repaint(); //Some sort of "refresh" of (as the name suggest) "repainting" of the whole frame
    }
}