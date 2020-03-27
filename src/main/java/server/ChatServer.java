package server;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.User;
import model.Chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

public class ChatServer {

    private static AtomicLong counter = new AtomicLong(1);
    private static Chat chat = new Chat();

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        System.out.println("Starting server");
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started: localhost:8080");
            while (true) {
                final Socket socket = serverSocket.accept();
                registerNewUser(socket);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void registerNewUser(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("SERVER: CONNECTION ESTABLISHED");
            String username = reader.readLine();
            if (username.isBlank()) username = "Agent_" + counter.getAndAdd(1);
            User user = new User(username, printWriter, chat);

            final Observable<String> userMessages = Observable
                    .fromStream(reader.lines())
                    .subscribeOn(Schedulers.io());
            chat.registerNewUser(user, userMessages);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("exit: " + Thread.currentThread().getName());
        }
    }
}
