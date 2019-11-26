package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private Map<KeyCode, Boolean> keys = new HashMap<>();

    private Pane root = new Pane();

    private Player player = new Player("Roma");

    private final int WIDTH = 1300;
    private final int HEIGHT = 700;

    private AnimationTimer timerBullet;

    @Override
    public void start(Stage primaryStage) throws Exception{

        root.setPrefSize(WIDTH,HEIGHT);

        player.setX(WIDTH / 2);
        player.setY(HEIGHT - 50);

        root.getChildren().addAll(player);

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moving(5);
                randomBullet();
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

    public void randomBullet() {
        int random = (int) Math.floor(Math.random() * 50);
        int x = (int) Math.floor(Math.random() * 1200 + 100);

        if (random == 5) {
            Bullet bullet = new Bullet();
            bullet.setX(x);
            bullet.setY(20);
            root.getChildren().addAll(bullet);
            moveBullet(bullet);
        }
    }

    public void moveBullet(Bullet bullet) {
        timerBullet = new AnimationTimer() {
            @Override
            public void handle(long now) {
                bullet.move(2);
                if (bullet.getY() > HEIGHT) {
                    root.getChildren().removeAll(bullet);
                }
            }
        };
        timerBullet.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
