package app.springdev.scheduler.alert;

public interface AlertSender {
    void send(String title, String message);
}
