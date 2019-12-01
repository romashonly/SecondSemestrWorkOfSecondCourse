package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

import static sample.Size.HEIGHT;
import static sample.Size.WIDTH;

public class GameController {

    private Socket socket;
    private int idOfClient;

    private Map<KeyCode, Boolean> keys = new HashMap<>();
    private Pane gameRoot;
    private Scene gameScene;

    private AnimationTimer timer;

    private int xStartPositionPlayer = WIDTH / 2;
    private int yStartPositionPlayer = HEIGHT - 50;

    private int speedOfPlayer = 5;
    private int speedOfBullet = 2;

    private int leftBorderOfPlayer = 1300;
    private int rightBorderOfPlayer = 0;

    private int leftBorderOfBullet = 0;
    private int rightBorderOfBullet = 1300;

    private int frequencyOfBullets = 5;

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
        gameRoot.setPrefSize(WIDTH, HEIGHT);

        labelOfFirstPlayer.setText(playerFirst.getName());
        progressBarOfFirstPlayer.setTranslateY(20);

        playerFirst.setLabelName(labelOfFirstPlayer);
        playerFirst.setProgressBar(progressBarOfFirstPlayer);

        gameRoot.getChildren().addAll(labelOfFirstPlayer, progressBarOfFirstPlayer);
        gameRoot.getChildren().addAll(playerFirst);

        labelOfSecondPlayer.setText(playerSecond.getName());
        labelOfSecondPlayer.setTranslateX(WIDTH - 100);

        progressBarOfSecondPlayer.setTranslateY(20);
        progressBarOfSecondPlayer.setTranslateX(WIDTH - 100);

        playerSecond.setLabelName(labelOfSecondPlayer);
        playerSecond.setProgressBar(progressBarOfSecondPlayer);

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


                try {
                    checkCollisions(bullet, playerFirst, this);
                    checkCollisions(bullet, playerSecond, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bullet.getY() > HEIGHT) {
                    gameRoot.getChildren().removeAll(bullet);
                    this.stop();
                }
            }
        };

        timerBullet.start();
    }

    private void checkCollisions(Bullet bullet, Player player, AnimationTimer timer) throws IOException {
        if (player.getBoundsInParent().intersects(bullet.getBoundsInParent())) {
//  Игра закончится при первом касании препятствия
            player.minusHP(powerOfBullet);

            if (!player.isAlive()) {
                gameOver();
            }

            gameRoot.getChildren().removeAll(bullet);
            timer.stop();
        }
    }

    public void gameOver() throws IOException {
        timer.stop();
        gameRoot.getChildren().removeAll(labelOfFirstPlayer, progressBarOfFirstPlayer, labelOfSecondPlayer, progressBarOfSecondPlayer);

        Label resultLabel = new Label();
        resultLabel.setTranslateX(WIDTH / 2 - 10);
        resultLabel.setTranslateY(HEIGHT / 2);

        Button goOut = new Button();
        goOut.setText("Выйти");
        goOut.setTranslateX(WIDTH / 2 - 10);
        goOut.setTranslateY(HEIGHT / 2 + 10);

        goOut.setOnAction(event ->  {
            Main.setStartScene();
        });

        if (playerFirst.isAlive()) {
            resultLabel.setText("Winner !!!");
        }
        else {
            resultLabel.setText("Loser :(");
        }

        gameRoot.getChildren().addAll(resultLabel, goOut);

//        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//        dataOutputStream.writeUTF("over");
    }
}
