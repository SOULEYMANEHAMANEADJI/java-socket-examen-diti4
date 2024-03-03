package isi.dev.javasocketexam.server;

import isi.dev.javasocketexam.dao.CommentaireImpl;
import isi.dev.javasocketexam.dao.ICommentaire;
import isi.dev.javasocketexam.entities.Commentaire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private List<ClientHandler> clients;
    private List<String> previousMessages;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private ICommentaire commentaireDao;
    private List<Commentaire> commentaires;
    public ClientHandler(Socket socket, List<ClientHandler> clients, List<String> previousMessages) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.previousMessages = previousMessages;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);

            // Initialiser le DAO pour les commentaires
            commentaireDao = new CommentaireImpl();
            commentaires = commentaireDao.getAllWithMembre();

            // Envoyer les anciens commentaires à tous les utilisateurs connectés
            sendPreviousMessages();

            // Ajouter le nouvel utilisateur à la liste des clients
            clients.add(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                if (msg.equalsIgnoreCase("quit")) {
                    break;
                }

                synchronized (previousMessages) {
                    previousMessages.add(msg);
                }

                broadcastMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
                socket.close();
                removeClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendPreviousMessages() {
        for (Commentaire commentaire : commentaires) {
            String message = commentaire.getMembre().getName() + ": " + commentaire.getMessage();
            writer.println(message);
        }
    }

    private void broadcastMessage(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.writer.println(message);
            }
        }
    }

    private void removeClient(ClientHandler client) {
        synchronized (clients) {
            clients.remove(client);
        }
    }
}