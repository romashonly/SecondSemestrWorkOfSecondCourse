package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Bullet extends Pane {

//    private Rectangle body;
    private ImageView body;
    private int x;
    private int y;

    public Bullet(int x, int y) {
        this.body = new ImageView(new Image("sample/img/a.png"));
        body.setFitHeight(30);
        body.setFitWidth(30);
        this.x = x;
        this.y = y;
        setX(getX());
        setY(getY());
        getChildren().addAll(body);
    }

    public void move(int moveY) {
        setY(getY() + moveY);
    }

    public ImageView getBody() {
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
