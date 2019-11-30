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

    public Socket connectWithServer() throws IOException {

        final String HOST = "localhost";
        final int PORT = 1234;
        Socket socket = new Socket(HOST, PORT);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        String message = dataInputStream.readUTF();
        this.id = Integer.parseInt(message);
        while (!message.equals("2")) {
            message = dataInputStream.readUTF();
        }

//        nameOFSecondPlayer = dataInputStream.readUTF();

        return socket;
    }

    public String getNameOFSecondPlayer() {
        return nameOFSecondPlayer;
    }

    public int getId() {
        return id;
    }
}
