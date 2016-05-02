package szamhalokzh;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final PrintWriter toServer;
    private final Scanner fromServer;
    private final Scanner keyboardInput;
    private final Socket client;

    private int score = 0;

    public Client() throws IOException {
        client = new Socket("localhost", 3333);
        toServer = new PrintWriter(client.getOutputStream(), true);
        fromServer = new Scanner(client.getInputStream());
        keyboardInput = new Scanner(System.in);

        new Thread() {
            @Override
            public void run() {

                String card1 = fromServer.nextLine();
                //System.out.println(card1);
                String card2 = fromServer.nextLine();
                //System.out.println(card2);
                processCard(card1);
                processCard(card2);

                while (true) {
                    if (process() == 0) {
                        break;
                    }
                }
                try {
                    toServer.close();
                    fromServer.close();
                    keyboardInput.close();
                    client.close();
                } catch (IOException ex) {
                    System.out.println("Some error occurred when closing streams.");
                }
            }

        }.start();

    }

    private int process() {
        int status = 1;
        System.out.println("Waiting for input...");
        String userInput = keyboardInput.nextLine();
        if ("kerek".equals(userInput)) {
            //nothing to do
        } else if ("megallok".equals(userInput)) {
            status = 0;
        } else {
            //input validálás?
        }
        toServer.println(userInput);
        status = processCard(fromServer.nextLine());
        return status;
    }

    private int processCard(String s) {
        int i = 1;
        if (s.contains(",")) {
            //lapot kapott
            String[] sp = s.split(",");
            String cardName = sp[0];
            int cardValue = Integer.parseInt(sp[1]);
            score += cardValue;
            System.out.println("Got: " + cardName + " " + cardValue);
            System.out.println("Total score: " + score);
        } else {
            //nyert vagy vesztett
            i = 0;
            System.out.println(s);
        }
        return i;
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }

}
