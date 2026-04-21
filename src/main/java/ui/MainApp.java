package ui;

import Database.DatabaseManager;
import models.User;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class MainApp {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize modern UI");
        }

        DatabaseManager db = new DatabaseManager();

        JFrame frame = new JFrame("Вход в систему");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel(" Логин:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel(" Пароль:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Войти");

        loginButton.setBackground(new Color(163, 230, 53));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(new JLabel(""));
        frame.add(loginButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            User user = db.authenticate(username, password);

            if (user != null) {
                frame.dispose();
                new DashboardUI(user, db);
            } else {
                JOptionPane.showMessageDialog(frame, "Ошибка! Проверь логин/пароль или базу данных.");
            }
        });

        frame.setVisible(true);
    }
}