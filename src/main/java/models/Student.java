package models;

// Наследование
public class Student extends User {

    public Student(int id, String username, String password) {
        super(id, username, password);
    }

    // Полиморфизм
    @Override
    public String getRoleName() {
        return "STUDENT";
    }
}