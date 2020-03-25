package model;

import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class User {
    private final Long id;
    private final String username;
    private final PrintWriter printWriter;
    private final BlockingQueue<Message> messagesQueue;
    public final static User serverUser = new User(null, "SERVER", null);

    public User(Long id, String username, PrintWriter printWriter) {
        this.id = id;
        this.username = username;
        this.printWriter = printWriter;
        this.messagesQueue = new ArrayBlockingQueue<>(100);
        new Thread(() -> {
            while (true) {
                try {
                    printWriter.println(messagesQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void receiveMessage(Message message) {
        this.messagesQueue.offer(message);
    }
}
