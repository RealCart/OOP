package models;

public abstract class User {
    private String name;
    private String email;
    private String password;
    private double balance;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = 15000.0;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setBalance(double balance) { this.balance = balance; }

    public void deductBalance(double amount) {
        if(amount > balance) {
            throw new IllegalArgumentException("Недостаточно средств на балансе");
        }
        this.balance -= amount;
    }

    public abstract void displayRole();
}

