package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static int clients;

    public static void main(String[] args) throws IOException, InterruptedException {
        final int PORT = 1234;
        ServerSocket ss = new ServerSocket(PORT);
        List<Socket> clientsSockets = new ArrayList<>();

        while (clientsSockets.size() != 2) {
            Socket s = ss.accept();
            clientsSockets.add(s);
            clients = clientsSockets.size();
            System.out.println("New client connected." + " Clients: " + clients);
            (new Thread(new Connection(s))).start();
        }
    }
}