package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

import static sample.Size.HEIGHT;
import static sample.Size.WIDTH;

public class Main extends Application {

    public static Stage stage;

    private static Pane startRoot = new Pane();
    private static Pane gameRoot = new Pane();
    private static Pane gameOverRoot = new Pane();

    private static Scene startScene = new Scene(startRoot);
    private static Scene gameScene = new Scene(gameRoot);
    private static Scene gameOverScene = new Scene(gameOverRoot);


    @Override
    public void start(Stage primaryStage){

        stage = primaryStage;

        primaryStage.setTitle("Game");

        setStartScene();

        primaryStage.show();
    }

    public static void setStartScene() {

        startRoot.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        startRoot.setPrefSize(Size.WIDTH, Size.HEIGHT);

        Button button = new Button();
        button.setText("START");

        button.setTranslateX(WIDTH / 2);
        button.setTranslateY(HEIGHT / 2 + 40);

        Label ipLabel = new Label();
        ipLabel.setText("Введите ip: ");
        ipLabel.setTranslateX(WIDTH / 2 - 50);
        ipLabel.setTranslateY(HEIGHT / 2 - 120);

        TextField ipTextField = new TextField();
        ipTextField.setTranslateX(WIDTH / 2 - 50);
        ipTextField.setTranslateY(HEIGHT / 2 - 100);

        Label nameLabel = new Label();
        nameLabel.setText("Введите свое имя: ");
        nameLabel.setTranslateX(WIDTH / 2 - 50);
        nameLabel.setTranslateY(HEIGHT / 2 - 60);

        TextField nameTextField = new TextField();
        nameTextField.setTranslateX(WIDTH / 2 - 50);
        nameTextField.setTranslateY(HEIGHT / 2 - 40);

        button.setOnAction(event ->  {

            try {
                setGameScene(nameTextField.getText(), ipTextField.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        Image image = new Image("sample/img/background.png");

        startRoot.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        startRoot.getChildren().addAll(button, ipLabel, ipTextField, nameLabel, nameTextField);
        stage.setScene(startScene);
    }

    public static void setGameScene(String nameOfPlayer, String host) throws IOException {

        Client client = new Client(nameOfPlayer, host);
        Socket socket = client.connectWithServer();

        Player playerFirst = new Player(nameOfPlayer);
        Player playerSecond = new Player(client.getNameOFSecondPlayer());

        GameController gameController = new GameController(gameRoot, gameScene, playerFirst, playerSecond, socket, client.getId());
        gameController.startGame();

        Image image = new Image("sample/img/background.png");

        gameRoot.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        stage.setScene(gameScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
