package model;

import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.PrintWriter;

public class User {
    private final String username;
    public final static User serverUser = new User("SERVER");

    private User(String username) {
        this.username = username;
    }

    public User(String username, PrintWriter printWriter, Chat chat) {
        this.username = username;
        chat.getObservable().subscribeOn(Schedulers.io()).subscribe(printWriter::println);
    }

    public String getUsername() {
        return username;
    }
}
