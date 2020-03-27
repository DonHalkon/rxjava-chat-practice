package client;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

    static {
        try {
            messages = Files.readAllLines(Paths.get(Client.class.getClassLoader().getResource("manual.txt").getPath()));
            length = messages.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String username = args.length > 0 ? args[0] : "";
        try (Socket socket = new Socket("localhost", 8080);
             Scanner userInput = new Scanner(System.in);
             Scanner serverResponses = new Scanner(socket.getInputStream())) {

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(username);

            Observable
                    .fromStream(userInput.tokens())
                    .subscribeOn(Schedulers.io())
                    .subscribe(printWriter::println, Throwable::printStackTrace);

            serverResponses.useDelimiter("\n");
            Observable.fromStream(serverResponses.tokens()).subscribe(System.out::println);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
