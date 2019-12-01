package sample;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Pane {
    private String name;
    private int hp;
    private Rectangle body;
    private int xPos;
    private int yPos;
    private Label labelName;
    private ProgressBar progressBar;

    public Player(String name, Color color) {
        this.name = name;
        this.hp = 100;
        this.body = new Rectangle(20, 20, color);
        getChildren().addAll(body);
    }

    public void minusHP(int power) {
        setHp(getHp() - power);
        progressBar.setProgress(getHp() * 1.0 / 100);
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
        this.yPos = yPos;
        this.setTranslateY(yPos);
    }

    public Label getLabelName() {
        return labelName;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setLabelName(Label labelName) {
        this.labelName = labelName;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public boolean isAlive() {
        return getHp() > 0;
    }
}
