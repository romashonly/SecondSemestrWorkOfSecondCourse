package sample;

import javafx.animation.AnimationTimer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public class Connection implements Runnable {

    private Socket socket;
    private int id;

    public Connection(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            while (Server.clients != 2) {
                dataOutputStream.writeUTF(Integer.toString(Server.clients));
            }

            dataOutputStream.writeUTF(Integer.toString(Server.clients));

            startListenerOfMoving();
            startListenerOfBullets();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startListenerOfMoving() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                    while (true) {
                        int xPos = dataInputStream.readInt();
                        int yPos = dataInputStream.readInt();

                        if (Server.connections.get(0).getId() == id) {
                            DataOutputStream dataOutputStreamOfSecondSocket = new DataOutputStream(Server.connections.get(1).getSocket().getOutputStream());
                            dataOutputStreamOfSecondSocket.writeInt(xPos);
                            dataOutputStreamOfSecondSocket.writeInt(yPos);
                        }
                        else  {
                            DataOutputStream dataOutputStreamOfSecondSocket = new DataOutputStream(Server.connections.get(0).getSocket().getOutputStream());
                            dataOutputStreamOfSecondSocket.writeInt(xPos);
                            dataOutputStreamOfSecondSocket.writeInt(yPos);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startListenerOfBullets() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public Socket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }
}
