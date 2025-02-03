package notifications;

public class SMSNotification implements Notification {
    @Override
    public void sendNotification(String message, String recipient) {
        System.out.println("Отправка SMS уведомления: '" + message + "' на " + recipient);
    }
}
