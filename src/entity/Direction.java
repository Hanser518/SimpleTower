package entity;

import java.awt.*;

public class Direction extends Point {

    public int direction;

    public Direction(int x, int y, int dir) {
        super(x, y);
        direction = dir;
    }

    public Direction(Point p, int dir) {
        super(p);
        direction = dir;
    }

    public Direction(Point p){
        super(p);
        direction = -1;
    }

    public Direction(){
        direction = -1;
    }

    public boolean equal(Direction other){
        return other.direction == this.direction && other.x == this.x && other.y == this.y;
    }

    public boolean like(Direction other){
        return other.x == this.x && other.y == this.y;
    }
}
