package Database;

import models.Admin;
import models.Course;
import models.Student;
import models.User;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/schedule_db";
    private static final String USER = "postgres";
    private static final String PASS = "admin123";

    public User authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                if (role.equals("ADMIN")) {
                    return new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                } else {
                    return new Student(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка базы данных: " + e.getMessage());
        }
        return null; // Если логин/пароль неверные
    }

    public void addCourse(Course course) {
        String query = "INSERT INTO courses (course_name, instructor, schedule_time, day_of_week) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getInstructor());
            stmt.setString(3, course.getScheduleTime());
            stmt.setString(4, course.getDayOfWeek());
            stmt.executeUpdate();
            System.out.println("Курс успешно добавлен в базу!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. Метод CRUD: Чтение всех курсов (Read)
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("course_name"),
                        rs.getString("instructor"),
                        rs.getString("schedule_time"),
                        rs.getString("day_of_week")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // 4. ТРЕБОВАНИЕ НА 10 БАЛЛОВ: Экспорт в CSV файл
    public void exportToCSV(String filePath) {
        List<Course> courses = getAllCourses();
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            writer.println("ID,CourseName,Instructor,Time,Day"); // Заголовок
            for (Course c : courses) {
                writer.printf("%d,%s,%s,%s,%s\n",
                        c.getId(), c.getCourseName(), c.getInstructor(),
                        c.getScheduleTime(), c.getDayOfWeek());
            }
            System.out.println("Данные успешно экспортированы в: " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Ошибка экспорта: " + e.getMessage());
        }
    }

    // 5. ТРЕБОВАНИЕ НА 10 БАЛЛОВ: Импорт из CSV файла
    public void importFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Пропускаем первую строку (заголовок)

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    // Создаем объект курса (ID = 0, так как база сама выдаст ID)
                    Course newCourse = new Course(0, data[1], data[2], data[3], data[4]);
                    addCourse(newCourse); // Сохраняем в базу
                }
            }
            System.out.println("Данные успешно импортированы из: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка импорта: " + e.getMessage());
        }
    }
    // Метод CRUD: Обновление (Update)
    public void updateCourse(int id, String newTime) {
        String query = "UPDATE courses SET schedule_time = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newTime);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка обновления: " + e.getMessage());
        }
    }

    // Метод CRUD: Удаление (Delete)
    public void deleteCourse(int id) {
        String query = "DELETE FROM courses WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка удаления: " + e.getMessage());
        }
    }
}