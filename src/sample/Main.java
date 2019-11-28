package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private Stage stage;

    private Pane startRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane gameOverRoot = new Pane();

    private Scene startScene = new Scene(startRoot);;
    private Scene gameScene = new Scene(gameRoot);;
    private Scene gameOverScene = new Scene(gameOverRoot);;


    @Override
    public void start(Stage primaryStage) throws Exception{

        stage = primaryStage;

        primaryStage.setTitle("Game");

        setStartScene();

        primaryStage.show();
    }

    public void setStartScene() {

        startRoot.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        startRoot.setPrefSize(Size.WIDTH, Size.HEIGHT);

        Button button = new Button();
        button.setText("START");

        button.setTranslateX(Size.WIDTH / 2);
        button.setTranslateY(Size.HEIGHT / 2);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setGameScene();
            }
        });

        startRoot.getChildren().addAll(button);
        stage.setScene(startScene);
    }

    public void setGameScene() {

        Player playerFirst = new Player("Roma", Color.BLACK);
        Player playerSecond = new Player("Chik", Color.GREEN);

        GameController gameController = new GameController(gameRoot, gameScene, playerFirst, playerSecond);
        gameController.starGame();

        stage.setScene(gameScene);
    }

    public void setGameOverScene() {
        gameOverRoot.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        gameOverRoot.setPrefSize(Size.WIDTH, Size.HEIGHT);
        stage.setScene(gameOverScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
