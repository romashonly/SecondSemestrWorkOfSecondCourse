package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Pane {
    private String name;
    private int hp;
    private Rectangle body;
    private int xPos;
    private int yPos;

    public Player(String name, int xPos, int yPos) {
        this.name = name;
        this.hp = 100;
        this.body = new Rectangle(20, 20, Color.BLACK);
        this.xPos = xPos;
        this.yPos = yPos;
        getChildren().addAll(body);
        setxPos(getyPos());
        setyPos(getxPos());
    }

    public void minusHP(int power) {
        setHp(getHp() - power);
    }

    public void move(int moveX) {
        setxPos(getxPos() + moveX);
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

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
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

    public void setxPos(int xPos) {
        this.xPos = xPos;
        this.setTranslateX(xPos);
    }

    public void setyPos(int yPos) {
        this.setTranslateY(yPos);
    }
}
