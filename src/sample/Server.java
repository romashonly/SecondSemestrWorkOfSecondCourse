package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static ServerSocket ss;
    public static int clients;
    public static List<Socket> clientsSockets = new ArrayList<>();
    public static List<Connection> connections = new ArrayList<>();
    public static List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        final int PORT = 1234;
        ss = new ServerSocket(PORT);

        waitingClients();

//        ss.close();
    }

    public static void waitingClients() throws IOException {

        while (true) {
            Socket s = ss.accept();
            clientsSockets.add(s);
            clients = clientsSockets.size() ;
            System.out.println("New client connected." + " Clients: " + clients);
            Connection connection = new Connection(s, clients);
            connections.add(connection);
            Thread thread = (new Thread(connection));
            threads.add(thread);
            thread.start();
        }
    }

    public static void setClients(int clients) {
        Server.clients = clients;
    }

    public static void setClientsSockets(List<Socket> clientsSockets) {
        Server.clientsSockets = clientsSockets;
    }

    public static void setConnections(List<Connection> connections) {
        Server.connections = connections;
    }

    public static void setThreads(List<Thread> threads) {
        Server.threads = threads;
    }
}