package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class Main extends Application {

    private Stage stage;

    private Pane startRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane gameOverRoot = new Pane();

    private Scene startScene = new Scene(startRoot);
    private Scene gameScene = new Scene(gameRoot);
    private Scene gameOverScene = new Scene(gameOverRoot);


    @Override
    public void start(Stage primaryStage){

        stage = primaryStage;

        primaryStage.setTitle("Game");

        setStartScene();

        primaryStage.show();
    }

    public void setStartScene() {

        startRoot.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        startRoot.setPrefSize(Size.WIDTH, Size.HEIGHT);

        TextField textField = new TextField();
        textField.setText("Your name");

        Button button = new Button();
        button.setText("START");

        button.setTranslateX(Size.WIDTH / 2);
        button.setTranslateY(Size.HEIGHT / 2);

        button.setOnAction(event ->  {
                try {
                    setGameScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });

        startRoot.getChildren().addAll(button, textField);
        stage.setScene(startScene);
    }

    public void setGameScene() throws IOException {

        Client client = new Client();
        Socket socket = client.connectWithServer();
        System.out.println(client.getId());

        Player playerFirst = new Player("Roma", Color.BLACK);
        Player playerSecond = new Player(client.getNameOFSecondPlayer(), Color.GREEN);

        GameController gameController = new GameController(gameRoot, gameScene, playerFirst, playerSecond, socket, client.getId());
        gameController.startGame();

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
