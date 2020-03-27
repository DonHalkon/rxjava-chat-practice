package model;


import static model.User.serverUser;

public class Message {
    private final String message;
    private final User from;

    public Message(String message, User from) {
        this.message = message;
        this.from = from != null ? from : serverUser;
    }

    public Message(String message) {
        this.message = message;
        this.from = serverUser;
    }

    @Override
    public String toString() {
        return from.getUsername() + ">> " + message;
    }
}
