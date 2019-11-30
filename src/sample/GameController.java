package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GameController {

    private Socket socket;
    private int idOfClient;

    private Map<KeyCode, Boolean> keys = new HashMap<>();
    private Pane gameRoot;
    private Scene gameScene;

    private AnimationTimer timer;

    private int xStartPositionPlayer = Size.WIDTH / 2;
    private int yStartPositionPlayer = Size.HEIGHT - 50;

    private int speedOfPlayer = 5;
    private int speedOfBullet = 2;

    private int leftBorderOfPlayer = 1300;
    private int rightBorderOfPlayer = 0;

    private int leftBorderOfBullet = 0;
    private int rightBorderOfBullet = 1300;

    private int frequencyOfBullets = 15;

    private int powerOfBullet = 10;

    private ProgressBar progressBarOfFirstPlayer = new ProgressBar(1);
    private Label labelOfFirstPlayer = new Label();

    private ProgressBar progressBarOfSecondPlayer = new ProgressBar(1);
    private Label labelOfSecondPlayer = new Label();

    private Player playerFirst;
    private Player playerSecond;

    public GameController(Pane gameRoot, Scene gameScene, Player playerFirst, Player playerSecond, Socket socket, int id) {

        this.socket = socket;
        this.idOfClient = id;
        this.gameRoot = gameRoot;
        this.gameScene = gameScene;

        this.playerFirst = playerFirst;
        playerFirst.setxPos(xStartPositionPlayer);
        playerFirst.setyPos(yStartPositionPlayer);

        this.playerSecond = playerSecond;
        playerSecond.setxPos(xStartPositionPlayer);
        playerSecond.setyPos(yStartPositionPlayer);
    }

    public void startGame() {
        gameRoot.setPrefSize(Size.WIDTH, Size.HEIGHT);

        labelOfFirstPlayer.setText(playerFirst.getName());

        progressBarOfFirstPlayer.setTranslateY(20);

        gameRoot.getChildren().addAll(labelOfFirstPlayer, progressBarOfFirstPlayer);
        gameRoot.getChildren().addAll(playerFirst);

        labelOfSecondPlayer.setText(playerSecond.getName());
        labelOfSecondPlayer.setTranslateX(Size.WIDTH - 100);

        progressBarOfSecondPlayer.setTranslateY(20);
        progressBarOfSecondPlayer.setTranslateX(Size.WIDTH - 100);

        gameRoot.getChildren().addAll(labelOfSecondPlayer, progressBarOfSecondPlayer);
        gameRoot.getChildren().addAll(playerSecond);

        gameScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        gameScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                try {

                    moving(speedOfPlayer);

                    int xPosOfBullet;
                    int randomForFreq;

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeInt(playerFirst.getxPos());
                    dataOutputStream.writeInt(playerFirst.getyPos());

                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    playerSecond.setxPos(dataInputStream.readInt());
                    playerSecond.setyPos(dataInputStream.readInt());

                    if (idOfClient == 1) {
                        randomBullet(frequencyOfBullets, dataOutputStream);
                    }
                    else {
                    /*
                    Получение икс координаты от первого игрока для создание препятствия
                     */
//                    createBullet(100);
                        randomForFreq = dataInputStream.readInt();
                        xPosOfBullet = dataInputStream.readInt();
                        randomBullet(frequencyOfBullets, (int) randomForFreq, xPosOfBullet);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    public void randomBullet(int frequency, DataOutputStream dataOutputStream) throws IOException {
        int random = (int) Math.floor(Math.random() * 100);
        int x = (int) Math.floor(Math.random() * rightBorderOfBullet + leftBorderOfBullet);

        dataOutputStream.writeInt(random);
        dataOutputStream.writeInt(x);

        if (random <= frequency) {
            Bullet bullet = new Bullet(x, -20);
            gameRoot.getChildren().addAll(bullet);
            moveBullet(bullet, speedOfBullet);
        }
    }

    public void randomBullet(int frequency, int randomForFreq, int xPos) {

        if (randomForFreq <= frequency) {
            Bullet bullet = new Bullet(xPos, -20);
            gameRoot.getChildren().addAll(bullet);
            moveBullet(bullet, speedOfBullet);
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
                    progressBarOfFirstPlayer.setProgress(playerFirst.getHp() * 1.0 / 100);

                    if (playerFirst.getHp() <= 0) {
                        gameOver();
                    }

                    gameRoot.getChildren().removeAll(bullet);
                    this.stop();
                }
                if (bullet.getY() > Size.HEIGHT) {
                    gameRoot.getChildren().removeAll(bullet);
                    this.stop();
                }
            }
        };

        timerBullet.start();
    }

    public void gameOver() {
        playerFirst.setHp(100);
        progressBarOfFirstPlayer.setProgress(1);
        timer.stop();
        gameRoot.getChildren().removeAll(labelOfFirstPlayer, progressBarOfFirstPlayer);

//        setGameOverScene();
    }
}
