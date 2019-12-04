package sample;

import javafx.animation.AnimationTimer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Connection implements Runnable {

    private Socket socket;
    private int id;

    private Thread listenerOfMoving;
    private Thread listenerOfGameOver;

    public Connection(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            while (Server.clients != 2) {
                dataOutputStream.writeUTF(Integer.toString(Server.clients));
            }

            dataOutputStream.writeUTF(Integer.toString(Server.clients));

            String name = dataInputStream.readUTF();

            if (Server.connections.get(0).getId() == id) {
                DataOutputStream dataOutputStreamOfSecondSocket = new DataOutputStream(Server.connections.get(1).getSocket().getOutputStream());
                dataOutputStreamOfSecondSocket.writeUTF(name);
            }
            else  {
                DataOutputStream dataOutputStreamOfSecondSocket = new DataOutputStream(Server.connections.get(0).getSocket().getOutputStream());
                dataOutputStreamOfSecondSocket.writeUTF(name);
            }

            listenerOfMoving = startListenerOfMoving();
            listenerOfMoving.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Thread startListenerOfMoving() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                    while (true) {
                        int xPos = dataInputStream.readInt();
                        int yPos = dataInputStream.readInt();
//                        String string = dataInputStream.readUTF();
//
//                        if (check) {
//                            Server.setClients(0);
//                            Server.setClientsSockets(new ArrayList<>());
//                            Server.setConnections(new ArrayList<>());
//                            for (Thread thread :
//                                    Server.threads) {
//                                thread.interrupt();
//                            }
//                            Server.setThreads(new ArrayList<>());
//                            listenerOfMoving.interrupt();
//
//                            Server.waitingClients();
//                        }

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
        });
    }

    public Socket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }
}
