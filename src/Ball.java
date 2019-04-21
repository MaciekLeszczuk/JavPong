import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;


public class Ball implements Runnable {

    //global variables
    int x, y, xDirection, yDirection;

    Paddle p1 = new Paddle(10, 180, 1);
    Paddle p2 = new Paddle(480, 180, 2);
    Paddle scoreboard = new Paddle(190,35, 3);

    Rectangle ball;


    public Ball(int x, int y){
        this.x = x;
        this.y = y;

        //Set ball moving randomly
        Random r = new Random();

        int rXDir = r.nextInt(1);
        if (rXDir == 0)
            rXDir--;
        setXDirection(rXDir);

        int rYDir = r.nextInt(1);
        if (rYDir == 0)
            rYDir--;
        setYDirection(rYDir);

        //create "ball"
        ball = new Rectangle(this.x, this.y, 15, 15);
    }

    public void setXDirection(int xDir){
        xDirection = xDir;
    }
    public void setYDirection(int yDir){
        yDirection = yDir;
    }

    public void draw(Graphics g) {
        g.setColor(Color.green);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);
    }

    public void collision(){
        if(ball.intersects(p1.paddle)) {
            setXDirection(+1);
            if(p1.getYDirection()==1){
                setYDirection(1);
            }
            if(p1.getYDirection()==-1){
                setYDirection(-1);
            }
        }
        if(ball.intersects(p2.paddle)) {
            setXDirection(-1);
            if(p2.getYDirection()==1){
                setYDirection(1);
            }
            if(p2.getYDirection()==-1){
                setYDirection(-1);
            }
        }
        if(ball.intersects(scoreboard.scoreboard) && ball.y >= 65){
            setYDirection(+1);
        }
        if(ball.intersects(scoreboard.scoreboard) && ball.y < 65  && ball.x < 250){
            setXDirection(-1);
        }
        if(ball.intersects(scoreboard.scoreboard) && ball.y < 65  && ball.x > 250){
            setXDirection(1);
        }
    }
    public void move() {
        collision();
        ball.x += xDirection;
        ball.y += yDirection;
        //bounce the ball when it hits the edge of the screen
        if (ball.x <= 0) {
            setXDirection(+1);
            p2.addScore();

        }
        if (ball.x >= 485) {
            setXDirection(-1);
            p1.addScore();
        }

        if (ball.y <= 15) {
            setYDirection(+1);
        }

        if (ball.y >= 385) {
            setYDirection(-1);
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                move();
                Thread.sleep(5);
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

}