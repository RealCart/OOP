package com.example.oop.payments;

public class CreditCardPayment implements Payment {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void processPayment(double amount) {
        System.out.println("Обработка платежа на сумму " + amount + " с использованием кредитной карты " + cardNumber);
    }
}
