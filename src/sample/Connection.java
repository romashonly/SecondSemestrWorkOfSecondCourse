package sample;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public class Connection implements Runnable {
    private Socket socket;
    public Connection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        OutputStream os = null;
        try {
            os = socket.getOutputStream();

            while (Server.clients != 2) {
                os.write(Server.clients);
            }

            os.write(Server.clients);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
