//Importing all required libraries
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

//That's the class paddle, that has of course paddles of both players, but also the scoreboard (so we can make collisions of ball with it)
public class Paddle implements Runnable{ //Runnable allows us to execute this class in the thread (to be exact, if we create thread with that class, it will execute method run() implemented in it)

    private int x, y, yDirection, id, score;  //Creating variables needed to operate

    Rectangle paddle;  //Creating rectangle for paddle
    Rectangle scoreboard; //Creating rectangle for scoreboard

    public Paddle(int x, int y, int id){  //This constructor has three inputs - X and Y are initial positions of the objects, while ID tells us if that's the paddles (if yes, then which player) or scoreboard
        this.x = x;
        this.y = y;
        this.id = id;
        paddle = new Rectangle(x, y, 10, 50);
        scoreboard = new Rectangle(x,y,90,40);
    }

    public void setYDirection(int yDir) { //KeyListener uses this method to set direction of Y movement of the paddles
        yDirection = yDir;
    }
    public int getYDirection(){ //Class Ball need this to see if the paddle is going up, or down
        return yDirection;
    }
    public void move() {  //This method adds yDirection to the current position of the paddle
        paddle.y += yDirection;
    }
    public void addScore(){  //If Ball detects intersection with wall behind the paddle, it uses this method to add one point to the current score of opposite player
        score++;
    }
    public int getScore(){  //We use this method to show current score of the players
        return score;
    }
    public boolean hasFinished(){ //If any of the players will achieve 3 points, than he/she wins the whole game
        if(score == 3){
            return true;
        }
        return false;
    }
    public void draw(Graphics g) { //Method used in class Pong to draw objects
        switch(id) {  //Here we are checking the ID of the object, so we will know the properties (like the color or dimensions) with we have to draw it
            case 1:
                g.setColor(Color.blue);  //Player 1 paddle is blue
                g.fillRect(paddle.x, paddle.y, paddle.width, paddle.height);
                break;
            case 2:
                g.setColor(Color.red); //Player 2 has the red paddle
                g.fillRect(paddle.x, paddle.y, paddle.width, paddle.height);
                break;
            case 3:
                g.setColor(Color.white);  //And the scoreboard is white
                g.fillRect(scoreboard.x,scoreboard.y,scoreboard.width,scoreboard.height);
        }
    }
    @Override
    public void run() { //Method that is being executed by threads
        try {
            while(true) {
                move();
                Thread.sleep(5); //Changing this number we can basically set how often paddles will be "refreshed", so the lower the number, the faster they will go on the screen
            }
        } catch(Exception e) {  //Catching and printing errors, that can occur while joining threads
            System.err.println(e.getMessage());
        }
    }
}