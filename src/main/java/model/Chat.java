package model;

import java.util.HashMap;

public class Chat {
    private final HashMap<Long, User> users;

    public Chat() {
        this.users = new HashMap<>();
    }

    public void broadcast(Long userId, String message) {
        User sender = users.get(userId);
        users.forEach((id, user) -> {
            if (!userId.equals(id)) user.receiveMessage(sender.getUsername() + ": " + message);
        });
    }

    public void broadcast(String message) {
        users.values().forEach(user -> user.receiveMessage(message));
    }

    public void registerNewUser(User user) {
        users.putIfAbsent(user.getId(), user);
        user.receiveMessage("SERVER: WELCOME " + user.getUsername());
        this.broadcast("SERVER: NEW USER '" + user.getUsername() + "' JOINED");
    }

    public void removeUser(Long id) {
        final User user = users.remove(id);
        this.broadcast("SERVER: '" + user.getUsername() + "' left chat.");
    }

    public void receiveMessage(long userId, String message) {
        broadcast(userId, message);
    }
}
