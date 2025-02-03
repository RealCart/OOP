package models;

public class Admin extends User {
    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void displayRole() {
        System.out.println("Роль: Администратор");
    }
}

