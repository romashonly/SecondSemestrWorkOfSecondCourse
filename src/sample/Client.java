package sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;

public class Client {

    public void connectWithServer() throws IOException, InterruptedException {
        final String HOST = "localhost";
        final int PORT = 1234;
        Socket s = new Socket(HOST, PORT);
        InputStream is = s.getInputStream();

        int x = is.read();
        while (x != 2) {
            x = is.read();
        }
    }

}
