package model;

import java.util.concurrent.*;

public class Chat {
    private final ConcurrentHashMap<Long, User> users;
    private final BlockingQueue<Message> messages;

    public Chat() {

        this.users = new ConcurrentHashMap<>();
        this.messages = new ArrayBlockingQueue<>(200);

        new Thread(() -> {
            Thread.currentThread().setName("Chat-Thread");
            while (true) {
                try {
                    sendMessage(messages.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void sendMessage(Message message) {
        if (message.toAll()) {
            User sender = message.getFrom();
            users.forEach((id, user) -> {
                if (!id.equals(sender.getId())) sendToUser(message.setTo(user));
            });
        } else {
            sendToUser(message);
        }
    }

    private void sendToUser(Message message) {
        message.getTo().receiveMessage(message);
    }

    public void registerNewUser(User user) {
        users.putIfAbsent(user.getId(), user);
        messages.offer(new Message("WELCOME " + user.getUsername(), null, user));
        messages.offer(new Message("NEW USER '" + user.getUsername() + "' JOINED", null, null));
    }

    public void removeUser(Long id) {
        final User user = users.remove(id);
        messages.offer(new Message("USER '" + user.getUsername() + "' HAS LEFT CHAT", null, null));
    }

    public void receiveMessage(long fromUserId, String message) {
        final User from = users.get(fromUserId);
        final Message finalMessage = new Message(message, from, null);
        messages.offer(finalMessage);
    }
}
