package model;

import java.io.PrintWriter;

public class User {
    private final Long id;
    private final String username;
    private final PrintWriter printWriter;

    public User(Long id, String username, PrintWriter printWriter) {
        this.id = id;
        this.username = username;
        this.printWriter = printWriter;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void receiveMessage(String message) {
        printWriter.println(message);
        printWriter.flush();
    }
}
