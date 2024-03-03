package isi.dev.javasocketexam.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int MAX_CLIENTS = 4;
    private static List<ClientHandler> clients = new ArrayList<>();
    private static List<String> previousMessages = new ArrayList<>();

    public static int connectedClients = 0;

    public static void main(String[] args) {
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Serveur en attente de connexions...");

            while (true) {
                if (clients.size() < MAX_CLIENTS) {
                    Socket socket = serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String username = reader.readLine();
                    System.out.println(username + " s'est connecté");
                    ClientHandler clientThread = new ClientHandler(socket, clients, previousMessages);
                    clients.add(clientThread);
                    clientThread.start();
                } else {
                    System.out.println("Nombre maximum de clients atteint !");
                    Socket socket = serverSocket.accept();
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int getMaxClients() {
        return MAX_CLIENTS;
    }
    public static synchronized void incrementConnectedClients() {
        connectedClients++;
    }
}
