package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Player extends Pane {
    private String name;
    private int hp;
    private ImageView body;
    private int xPos;
    private int yPos;
    private Label labelName;
    private ProgressBar progressBar;

    private Image squirrelImage = new Image("sample/img/squirrel.png");
    private int count = 3;
    private int columns = 3;
    private int offsetX = 96;
    private int offsetY = 33;
    private int width = 16;
    private int height = 16;

    public SpriteAnimation animation;

    public Player(String name) {
        this.name = name;

        labelName = new Label();
        labelName.setText(name);
        labelName.setTranslateY(-5);

        this.hp = 100;
        this.body = new ImageView(squirrelImage);
        body.setFitHeight(40);
        body.setFitWidth(40);
        body.setViewport(new Rectangle2D( offsetX, offsetY, width, height));
        animation = new SpriteAnimation(this.body, Duration.millis(200), count, columns, offsetX, offsetY, width, height);
        getChildren().addAll(body, labelName);
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

    public ImageView getBody() {
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

    public void setBody(ImageView body) {
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
