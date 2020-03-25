package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String username = args.length > 0 ? args[0] : "";
        try (Socket socket = new Socket("localhost", 8080)) {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(username);
            printWriter.flush();
            new Thread(() -> {
                try (Scanner userInput = new Scanner(System.in)) {
                    while (userInput.hasNextLine()) {
                        printWriter.println(userInput.nextLine());
                        printWriter.flush();
                    }
                }
            }).start();

            try (Scanner scanner = new Scanner(socket.getInputStream())) {
                while (scanner.hasNextLine()) System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
