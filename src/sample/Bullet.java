package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet extends Pane {

    private Rectangle body;
    private int x;
    private int y;

    public Bullet(int x, int y) {
        this.body = new Rectangle(20, 20, Color.RED);
        this.x = x;
        this.y = y;
        setX(getX());
        setY(getY());
        getChildren().addAll(body);
    }

    public void move(int moveY) {
        setY(getY() + moveY);
    }

    public Rectangle getBody() {
        return body;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
        this.setTranslateX(x);
    }

    public void setY(int y) {
        this.y = y;
        this.setTranslateY(y);
    }
}
