package szamhalokzh;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private final ServerSocket server;

    public Server() throws IOException {
        this.server = new ServerSocket(3333);
        System.out.println("server running on port 3333");
    }

    private void handleClients() {
        while (true) {
            try {
                new Handler(server.accept()).start();
                System.out.println("SERVER-LOG: Client connected");
            } catch (IOException e) {
                System.out.println("SERVER-LOG: Hiba a kliensek fogadasakor.");
                break;
            }
        }
    }

    private static class Handler extends Thread {

        private final Socket client;
        private final PrintWriter pw;
        private final Scanner sc;
        private final List<Card> cards;
        private Random random;
        private int score = 0;

        public Handler(Socket accept) throws IOException {
            this.client = accept;
            pw = new PrintWriter(client.getOutputStream(), true);
            sc = new Scanner(client.getInputStream());
            cards = new LinkedList<>();
            for (int i = 2; i <= 10; i++) {
                cards.add(new Card(Integer.toString(i), i));
            }
            cards.add(new Card("J", 10));
            cards.add(new Card("Q", 10));
            cards.add(new Card("K", 10));
            cards.add(new Card("A", 10));

        }

        @Override
        public void run() {
            try {
                sendCard(cards.get(getRandomCard()));
                sendCard(cards.get(getRandomCard()));
                while (true) {
                    String fromClient = sc.nextLine();
                    if ("kerek".equals(fromClient)) {
                        sendCard(cards.get(getRandomCard()));
                    } else if ("megallok".equals(fromClient)) {
                        pw.println((score < 22) ? "nyert" : "vesztett");
                        break;
                    } else {
                        pw.println("Ilyet nem mondhatsz.");
                    }
                }

                client.close();
                pw.close();
                sc.close();
            } catch (Exception ex) {
                System.out.println("some error occurred");
            }
        }

        private int getRandomCard() {
            //return random.nextInt((cards.size() - 0) + 1) + 0;
            return 0 + (int) (Math.random() * ((12 - 0) + 1));
        }

        private void sendCard(Card card) {
            pw.println(card.asMessage());
            score += card.value;
            System.out.println("Sent: " + card.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.handleClients();
    }

}
