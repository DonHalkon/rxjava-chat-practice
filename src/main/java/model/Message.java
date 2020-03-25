package model;


import static model.User.serverUser;

public class Message {
    private final String message;
    private final User from;
    private final User to;

    public Message(String message, User from, User to) {
        this.message = message;
        this.from = from != null ? from : serverUser;
        this.to = to;
    }

    public Message setTo(User to) {
        return new Message(this.message, this.from, to);
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }

    public boolean toAll() {
        return to == null;
    }

    @Override
    public String toString() {
        return from.getUsername() + ">> " + message;
    }
}
