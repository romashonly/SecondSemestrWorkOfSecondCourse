package sample;

import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client {

    private int id = 0;
    private String nameOFSecondPlayer = "...";
    private String nameOfPlayer;

    public Client(String nameOfPlayer) {
        this.nameOfPlayer = nameOfPlayer;
    }

    public Socket connectWithServer() throws IOException {

        final String HOST = "localhost";
        final int PORT = 1234;
        Socket socket = new Socket(HOST, PORT);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        String message = dataInputStream.readUTF();
        this.id = Integer.parseInt(message);
        while (!message.equals("2")) {
            message = dataInputStream.readUTF();
        }

        dataOutputStream.writeUTF(getNameOfPlayer());

        nameOFSecondPlayer = dataInputStream.readUTF();

        return socket;
    }

    public String getNameOfPlayer() {
        return nameOfPlayer;
    }

    public String getNameOFSecondPlayer() {
        return nameOFSecondPlayer;
    }

    public int getId() {
        return id;
    }
}
