package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sample.Size.HEIGHT;
import static sample.Size.WIDTH;

public class GameController {

    private Socket socket;
    private int idOfClient;

    private Map<KeyCode, Boolean> keys = new HashMap<>();
    private Pane gameRoot;
    private Scene gameScene;

    private AnimationTimer playerTimer;
    private List<AnimationTimer> bulletTimers = new ArrayList<>();

    private int xStartPositionPlayer = WIDTH / 2;
    private int yStartPositionPlayer = HEIGHT - 131;

    private int speedOfPlayer = 5;
    private int speedOfBullet = 2;

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

    private Label resultLabel;
    private Button newGame;

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
        gameScene.setOnKeyReleased(event -> {
            keys.put(event.getCode(), false);
            playerFirst.animation.stop();
        });

        playerTimer = new AnimationTimer() {

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
                    playerSecond.animation.play();
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
                        randomBullet(frequencyOfBullets, randomForFreq, xPosOfBullet);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        playerTimer.start();
    }

    public boolean isPressed(KeyCode keyCode) {
        return keys.getOrDefault(keyCode, false);
    }

    public void moving(int speed) {

        if (isPressed(KeyCode.RIGHT)) {
            if (playerFirst.getBoundsInParent().intersects(WIDTH, 0, 1, HEIGHT)) {
                playerFirst.setxPos(WIDTH - 20);
            }
            else {
                playerFirst.setScaleX(-1);
                playerFirst.animation.play();
                playerFirst.move(speed);
            }
        }
        else if (isPressed(KeyCode.LEFT)) {

            if (playerFirst.getBoundsInParent().intersects(-1, 0, 1, HEIGHT)) {
                playerFirst.setxPos(0);
            }
            else {
                playerFirst.setScaleX(1);
                playerFirst.animation.play();
                playerFirst.move(speed * -1);
            }
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

                    checkCollisions(bullet, playerFirst, this);
                    checkCollisions(bullet, playerSecond, this);

                if (bullet.getY() > HEIGHT) {
                    gameRoot.getChildren().removeAll(bullet);
                    this.stop();
                }
            }
        };

        timerBullet.start();
    }

    private void checkCollisions(Bullet bullet, Player player, AnimationTimer timer) {
        if (player.getBoundsInParent().intersects(bullet.getBoundsInParent())) {
            player.minusHP(powerOfBullet);

            if (!player.isAlive()) {
                gameOver();
            }

            gameRoot.getChildren().removeAll(bullet);
            timer.stop();
        }
    }

    public void gameOver() {

        for (AnimationTimer timer :
                bulletTimers) {
            timer.stop();
        }


        playerTimer.stop();

        gameRoot.getChildren().removeAll(labelOfFirstPlayer, progressBarOfFirstPlayer, labelOfSecondPlayer, progressBarOfSecondPlayer, playerFirst, playerSecond);

        resultLabel = new Label();
        resultLabel.setTranslateX(WIDTH / 2 - 10);
        resultLabel.setTranslateY(HEIGHT / 2);

        newGame = new Button();
        newGame.setText("Заново");
        newGame.setTranslateX(WIDTH / 2 - 10);
        newGame.setTranslateY(HEIGHT / 2 + 20);

        newGame.setOnAction(event ->  {

            Player playerF = new Player(playerFirst.getName());
            Player playerS = new Player(playerSecond.getName());

            GameController gameController = new GameController(gameRoot, gameScene, playerF, playerS, socket, idOfClient);
            gameController.startGame();

            gameRoot.getChildren().removeAll(resultLabel, newGame);

            Image image = new Image("sample/img/background.png");

            gameRoot.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

            Main.stage.setScene(gameScene);
        });

        if (playerFirst.isAlive()) {
            resultLabel.setText("Win !!!");
        }
        else {
            resultLabel.setText("Lose :(");
        }

        gameRoot.getChildren().addAll(resultLabel, newGame);

    }
}
