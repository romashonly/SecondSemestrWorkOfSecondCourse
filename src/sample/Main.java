package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    private Map<KeyCode, Boolean> keys = new HashMap<>();

    private Pane root = new Pane();

    private final int WIDTH = 1300;
    private final int HEIGHT = 700;

    private int xStartPositionPlayer = WIDTH / 2;
    private int yStartPositionPlayer = HEIGHT - 50;

    private int speedOfPlayer = 5;
    private int speedOfBullet = 2;

    private int leftBorderOfPlayer = 1300;
    private int rightBorderOfPlayer = 0;

    private int leftBorderOfBullet = 0;
    private int rightBorderOfBullet = 1300;

    private int frequencyOfBullets = 1;

    private int powerOfBullet = 20;

    private ProgressBar progressBar = new ProgressBar(1);
    private Label label = new Label();

    private Player player = new Player("Roma", xStartPositionPlayer, yStartPositionPlayer);

    private List<Bullet> bullets = new ArrayList<>();

    private boolean isTouch = false;

    @Override
    public void start(Stage primaryStage) throws Exception{

        root.setPrefSize(WIDTH,HEIGHT);

        label.setText(player.getName());

        progressBar.setTranslateY(20);

        root.getChildren().addAll(label, progressBar);
        root.getChildren().addAll(player);

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                moving(speedOfPlayer);
                randomBullet(speedOfBullet, frequencyOfBullets);
            }
        };

        timer.start();

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public boolean isPressed(KeyCode keyCode) {
        return keys.getOrDefault(keyCode, false);
    }

    public void moving(int speed) {
        if (isPressed(KeyCode.RIGHT)) {
            player.move(speed);
        }
        else if (isPressed(KeyCode.LEFT)) {
            player.move(speed * -1);
        }
    }

    public void randomBullet(int speed, int frequency) {
        int random = (int) Math.floor(Math.random() * 100);
        int x = (int) Math.floor(Math.random() * rightBorderOfBullet + leftBorderOfBullet);

        if (random <= frequency) {
            Bullet bullet = new Bullet(x, -20);
            root.getChildren().addAll(bullet);
            moveBullet(bullet, speed);
        }
    }

    public void moveBullet(Bullet bullet, int speed) {

        AnimationTimer timerBullet = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Bullet thisBullet = bullet;
                thisBullet.move(speed);
                if (player.getBoundsInParent().intersects(thisBullet.getBoundsInParent())) {
                    player.minusHP(powerOfBullet);
                    progressBar.setProgress(player.getHp() * 1.0 / 100);

                    if (player.getHp() <= 0) {
                        gameOver();
                    }

                    root.getChildren().removeAll(thisBullet);
                    this.stop();
                }
                if (thisBullet.getY() > HEIGHT) {
                    root.getChildren().removeAll(thisBullet);
                    this.stop();
                }
            }
        };
        timerBullet.start();
    }

    public void gameOver() {
        player.setHp(100);
        progressBar.setProgress(1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
