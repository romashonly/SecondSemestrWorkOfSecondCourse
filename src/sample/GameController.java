package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class GameController {

    private Map<KeyCode, Boolean> keys = new HashMap<>();
    private Pane gameRoot;
    private Scene gameScene;

    private AnimationTimer timer;

    private final int WIDTH = 1300;
    private final int HEIGHT = 700;

    private int xStartPositionPlayer = WIDTH / 2;
    private int yStartPositionPlayer = HEIGHT - 50;

    private int speedOfPlayer = 5;
    private int speedOfBullet = 7;

    private int leftBorderOfPlayer = 1300;
    private int rightBorderOfPlayer = 0;

    private int leftBorderOfBullet = 0;
    private int rightBorderOfBullet = 1300;

    private int frequencyOfBullets = 15;

    private int powerOfBullet = 20;

    private ProgressBar progressBar = new ProgressBar(1);
    private Label label = new Label();

    private Player playerFirst;
    private Player playerSecond;

    public GameController(Pane gameRoot, Scene gameScene, Player playerFirst, Player playerSecond) {
        this.gameRoot = gameRoot;
        this.gameScene = gameScene;
        this.playerFirst = playerFirst;
        playerFirst.setxPos(xStartPositionPlayer);
        playerFirst.setyPos(yStartPositionPlayer);
        this.playerSecond = playerSecond;
        playerSecond.setxPos(xStartPositionPlayer);
        playerSecond.setyPos(yStartPositionPlayer);
    }

    public void starGame() {
        gameRoot.setPrefSize(WIDTH,HEIGHT);

        label.setText(playerFirst.getName());

        progressBar.setTranslateY(20);

        gameRoot.getChildren().addAll(label, progressBar);
        gameRoot.getChildren().addAll(playerFirst);

        gameScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        gameScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                moving(speedOfPlayer);
                randomBullet(speedOfBullet, frequencyOfBullets);
            }
        };

        timer.start();
    }

    public boolean isPressed(KeyCode keyCode) {
        return keys.getOrDefault(keyCode, false);
    }

    public void moving(int speed) {
        if (isPressed(KeyCode.RIGHT)) {
            playerFirst.move(speed);
        }
        else if (isPressed(KeyCode.LEFT)) {
            playerFirst.move(speed * -1);
        }
    }

    public void randomBullet(int speed, int frequency) {
        int random = (int) Math.floor(Math.random() * 100);
        int x = (int) Math.floor(Math.random() * rightBorderOfBullet + leftBorderOfBullet);

        if (random <= frequency) {
            Bullet bullet = new Bullet(x, -20);
            gameRoot.getChildren().addAll(bullet);
            moveBullet(bullet, speed);
        }
    }

    public void moveBullet(Bullet bullet, int speed) {

        AnimationTimer timerBullet = new AnimationTimer() {
            @Override
            public void handle(long now) {
                bullet.move(speed);
                if (playerFirst.getBoundsInParent().intersects(bullet.getBoundsInParent())) {
//  Игра закончится при первом касании препятствия
                    playerFirst.minusHP(powerOfBullet);
                    progressBar.setProgress(playerFirst.getHp() * 1.0 / 100);

                    if (playerFirst.getHp() <= 0) {
                        gameOver();
                    }

                    gameRoot.getChildren().removeAll(bullet);
                    this.stop();
                }
                if (bullet.getY() > HEIGHT) {
                    gameRoot.getChildren().removeAll(bullet);
                    this.stop();
                }
            }
        };

        timerBullet.start();
    }

    public void gameOver() {
        playerFirst.setHp(100);
        progressBar.setProgress(1);
        timer.stop();
        gameRoot.getChildren().removeAll(label, progressBar);

//        setGameOverScene();
    }
}
