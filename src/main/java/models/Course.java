package models;

public class Course {
    private int id;
    private String courseName;
    private String instructor;
    private String scheduleTime;
    private String dayOfWeek;

    public Course(int id, String courseName, String instructor, String scheduleTime, String dayOfWeek) {
        this.id = id;
        setCourseName(courseName); // Используем сеттер для валидации
        this.instructor = instructor;
        this.scheduleTime = scheduleTime;
        this.dayOfWeek = dayOfWeek;
    }

    // Валидация (Требование проекта: Input Validation)
    public void setCourseName(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название курса не может быть пустым!");
        }
        this.courseName = courseName;
    }

    public int getId() { return id; }
    public String getCourseName() { return courseName; }
    public String getInstructor() { return instructor; }
    public String getScheduleTime() { return scheduleTime; }
    public String getDayOfWeek() { return dayOfWeek; }
}