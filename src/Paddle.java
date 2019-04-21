import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Paddle implements Runnable{

    int x, y, yDirection, id, score;
    int p1Y, p1X;

    Rectangle paddle;
    Rectangle scoreboard;

    public Paddle(int x, int y, int id){
        this.x = x;
        this.y = y;
        this.id = id;
        paddle = new Rectangle(x, y, 10, 50);
        scoreboard = new Rectangle(x,y,90,40);
    }

    public void setYDirection(int yDir) {
        yDirection = yDir;
    }

    public void move() {
        paddle.y += yDirection;
    }
    public void addScore(){
        score++;
    }
    public int getScore(){
        return score;
    }
    public int getYDirection(){
        return yDirection;
    }

    public void draw(Graphics g) {

        switch(id) {

            case 1:
                g.setColor(Color.blue);
                g.fillRect(paddle.x, paddle.y, paddle.width, paddle.height);
                break;
            case 2:
                g.setColor(Color.red);
                g.fillRect(paddle.x, paddle.y, paddle.width, paddle.height);
                break;
            case 3:
                g.setColor(Color.white);
                g.fillRect(scoreboard.x,scoreboard.y,scoreboard.width,scoreboard.height);
        }
    }
    @Override
    public void run() {
        try {
            while(true) {
                move();
                Thread.sleep(7);
            }
        } catch(Exception e) { System.err.println(e.getMessage()); }
    }
}