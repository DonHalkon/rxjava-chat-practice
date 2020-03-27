package model;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.*;

public class Chat {
    private final BlockingQueue<Message> messages;
    private final ConnectableObservable<Object> observable;

    public Chat() {
        this.messages = new ArrayBlockingQueue<>(200);
        observable = Observable.create(subscriber -> {
            while (!subscriber.isDisposed()) {
                subscriber.onNext(messages.take());
            }
        }).subscribeOn(Schedulers.single()).publish();
        observable.connect();
    }

    public void broadcast(Message message) {
        messages.offer(message);
    }

    public void registerNewUser(User user, Observable<String> userMessages) {
        userMessages
                .doOnComplete(() -> broadcast(new Message("USER '" + user.getUsername() + "' HAS LEFT CHAT")))
                .subscribe(message -> receiveMessage(user, message), Throwable::printStackTrace);

        broadcast(new Message("WELCOME " + user.getUsername()));
        broadcast(new Message("NEW USER '" + user.getUsername() + "' JOINED"));
    }

    private void receiveMessage(User fromUser, String message) {
        final Message finalMessage = new Message(message, fromUser);
        broadcast(finalMessage);
    }

    public ConnectableObservable<Object> getObservable() {
        return observable;
    }
}
