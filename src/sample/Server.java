package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static int clients;
    public static List<Socket> clientsSockets = new ArrayList<>();
    public static List<Connection> connections = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        final int PORT = 1234;
        ServerSocket ss = new ServerSocket(PORT);

        while (clientsSockets.size() != 2) {
            Socket s = ss.accept();
            clientsSockets.add(s);
            clients = clientsSockets.size();
            System.out.println("New client connected." + " Clients: " + clients);
            Connection connection = new Connection(s, clients);
            connections.add(connection);
            (new Thread(connection)).start();
        }
    }
}