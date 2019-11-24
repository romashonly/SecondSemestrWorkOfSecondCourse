package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Pane {
    private String name;
    private int hp;
    private Rectangle body;
    private int x;
    private int y;

    public Player(String name) {
        this.name = name;
        this.hp = 100;
        this.body = new Rectangle(20, 20, Color.BLACK);
        getChildren().addAll(body);
    }

    public void minusHP() {
        setHp(getHp() - 5);
    }

    public void move(int moveX) {
        setX(getX() + moveX);
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setBody(Rectangle body) {
        this.body = body;
    }

    public void setX(int x) {
        this.x = x;
        this.setTranslateX(x);
    }

    public void setY(int y) {
        this.setTranslateY(y);
    }
}
