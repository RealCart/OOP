package notifications;

public class EmailNotification implements Notification {
    @Override
    public void sendNotification(String message, String recipient) {
        System.out.println("Отправка email уведомления: '" + message + "' на " + recipient);
    }
}
