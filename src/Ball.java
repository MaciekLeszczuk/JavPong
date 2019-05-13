//Importing all required libraries
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

//Class that contains ball, and also creates paddles
public class Ball implements Runnable {

    private int x, y, xDirection, yDirection; //Creating variables needed to operate

    Paddle p1 = new Paddle(10, 180, 1); //Creating paddle and scoreboard objects (with corresponding IDs
    Paddle p2 = new Paddle(480, 180, 2);
    Paddle scoreboard = new Paddle(190,35, 3);

    Rectangle ball;


    public Ball(int x, int y){
        this.x = x;
        this.y = y;

        Random r = new Random();  //Initial move of the balls will be random
        int rXDir = r.nextInt(2); //Generating random int from 0 to 1
        if (rXDir == 0) //If int = 0, we are changing it to -1
            rXDir--;
        setXDirection(rXDir); //Setting X direction of the ball to generated random int

        int rYDir = r.nextInt(2); //Same thing as above, but for Y direction
        if (rYDir == 0)
            rYDir--;
        setYDirection(rYDir);

        ball = new Rectangle(this.x, this.y, 15, 15);  //For now we are creating Ball as an rectangle, but we will display it as an oval
    }

    public void setXDirection(int xDir){ //Setting X direction of the ball
        xDirection = xDir;
    }
    public void setYDirection(int yDir){ //Setting Y direction of the ball
        yDirection = yDir;
    }

    public void draw(Graphics g) {  //Drawing the ball
        g.setColor(Color.green);  //Changing the color to green
        g.fillOval(ball.x, ball.y, ball.width, ball.height);  //Drawing the ball as an oval
    }

    public void collision(){  //Method collision handles collisions between ball, paddles and scoreboard
        if(ball.intersects(p1.paddle)) {  //If object ball intersects if object paddle
            setXDirection(+1);      //We are changing it X direction
            if(p1.getYDirection()==1){ //And if the paddle is moving to the down
                setYDirection(1); //We are setting Y direction of the ball also to the down
            }
            if(p1.getYDirection()==-1){ //And of course the same goes for the paddle moving upwards
                setYDirection(-1);
            }
        }
        if(ball.intersects(p2.paddle)) { //Same thing as above, but this time for player two
            setXDirection(-1);
            if(p2.getYDirection()==1){
                setYDirection(1);
            }
            if(p2.getYDirection()==-1){
                setYDirection(-1);
            }
        }
        if(ball.intersects(scoreboard.scoreboard) && ball.y >= 65){  //If ball intersects with side of the scoreboard, then we change it Y direction
            setYDirection(+1);
        }
        if(ball.intersects(scoreboard.scoreboard) && ball.y < 65  && ball.x < 250){ //But if it intersects with bottom of the scoreboard, we are changing it X direction (basing on the location of the ball)
            setXDirection(-1);
        }
        if(ball.intersects(scoreboard.scoreboard) && ball.y < 65  && ball.x > 250){
            setXDirection(1);
        }
    }
    public void move() { //Method used to move the ball
        collision();  //Before moving the ball, we need to check if it isn't intersecting with anything
        if (ball.x <= 0) { //After that we should check, if the ball isn't hitting the wall behind the paddle, if yes, then we are adding one point to the score of opposite player
            setXDirection(+1);
            p2.addScore();
        }
        if (ball.x >= 485) {
            setXDirection(-1);
            p1.addScore();
        }

        if (ball.y <= 15) { //We also need to check if the ball isn't hitting the top or bottom wall of the window, and if yes, than change it Y direction
            setYDirection(+1);
        }
        if (ball.y >= 385) {
            setYDirection(-1);
        }
        ball.x += xDirection; //After all of that checking we can finally move ball
        ball.y += yDirection;
    }
    public int whoWon(){  //Method used for checking if anyone hasn't already won the game
        if(p1.hasFinished()){
            return 1; //If player 1 won, than we are returning 1
        }
        if(p2.hasFinished()){
            return 2; //If player 2, then output will be 2
        }
        return 0; //If we still didn't have the winner, output is 0
        //Those output values are used to display which player won the game on the final screen
    }
    @Override
    public void run() { //And mandatory run() method, that is being executed
        try {
            while(true) {
                move();
                Thread.sleep(5); //Just like in the Paddle, we can set thread to sleep for longer period of time, so our ball will move slower
            }
        }
        catch(Exception e) {  //Catching and printing errors, that can occur while joining threads
            System.out.println(e);
        }
    }

}