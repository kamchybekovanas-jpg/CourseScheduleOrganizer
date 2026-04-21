package models;

public abstract class User {
    // Инкапсуляция: поля приватные
    private int id;
    private String username;
    private String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Геттеры для доступа к данным
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    // Абстрактный метод для Полиморфизма
    public abstract String getRoleName();
}