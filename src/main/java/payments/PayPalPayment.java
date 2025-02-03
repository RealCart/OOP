package payments;

public class PayPalPayment implements Payment {
    private String emailAccount;

    public PayPalPayment(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    @Override
    public void processPayment(double amount) {
        System.out.println("Обработка платежа на сумму " + amount + " через PayPal аккаунт " + emailAccount);
    }
}
