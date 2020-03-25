package server;


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
                new Thread(() -> registerNewUser(socket)).start();
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
            final long userId = counter.getAndAdd(1);
            if (username.isBlank()) username = "Agent " + userId;
            Thread.currentThread().setName(username + "-Thread");
            User user = new User(userId, username, printWriter);
            chat.registerNewUser(user);

            reader.lines().forEach(message -> chat.receiveMessage(userId, message));

            chat.removeUser(user.getId());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("exit: " + Thread.currentThread().getName());
        }
    }
}
