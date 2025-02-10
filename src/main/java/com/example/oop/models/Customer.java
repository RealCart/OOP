package com.example.oop.models;

public class Customer extends User {
    public Customer(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void displayRole() {
        System.out.println("Роль: Клиент");
    }
}
