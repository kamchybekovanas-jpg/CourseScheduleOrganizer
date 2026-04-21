package models;

// Наследование: Admin расширяет User
public class Admin extends User {

    public Admin(int id, String username, String password) {
        super(id, username, password);
    }

    // Полиморфизм: переопределение метода
    @Override
    public String getRoleName() {
        return "ADMIN";
    }
}