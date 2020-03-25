package client;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Client {
    private static final Random r = new Random();
    private static List<String> messages;
    private static int length;

    public static void main(String[] args) {
        String username = args.length > 0 ? args[0] : "";
        try (Socket socket = new Socket("localhost", 8080)) {
            messages = Files.readAllLines(Paths.get(Client.class.getClassLoader().getResource("manual.txt").getPath()));
            length = messages.size();

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(username);
            new Thread(() -> {
                //for manual input

//                try (Scanner userInput = new Scanner(System.in)) {
//                    while (userInput.hasNextLine()) {
//                        final String message = userInput.nextLine();
//                        Thread.sleep(r.nextInt(5000)); // slow network imitation stub
//                        printWriter.println(message);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                while (true) {
                    try {
                        Thread.sleep(r.nextInt(10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    printWriter.println(messages.get(r.nextInt(length)));
                }
            }).start();

            try (Scanner scanner = new Scanner(socket.getInputStream())) {
                String line = null;
                while (scanner.hasNextLine()) {
//                    line = scanner.nextLine();
                    System.out.println(scanner.nextLine());
                }
//                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
