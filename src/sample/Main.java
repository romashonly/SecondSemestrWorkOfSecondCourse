package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private Map<KeyCode, Boolean> keys = new HashMap<>();

    private Pane root = new Pane();

    private Player player = new Player("Roma");

    private final int WIDTH = 1300;
    private final int HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception{

//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
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
                moving();
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

    public void moving() {
        if (isPressed(KeyCode.RIGHT)) {
            player.move(2);
        }
        else if (isPressed(KeyCode.LEFT)) {
            player.move(-2);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
