package ui;

import Database.DatabaseManager;
import models.Course;
import models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class DashboardUI {
    private User currentUser;
    private DatabaseManager db;
    private JFrame frame;
    private JPanel coursesContainer;

    // Наша стильная палитра
    private final Color BG_DARK = new Color(18, 18, 18);
    private final Color CARD_DARK = new Color(28, 28, 28);
    private final Color NEON_GREEN = new Color(163, 230, 53);
    private final Color TEXT_MUTED = new Color(160, 160, 160);

    public DashboardUI(User user, DatabaseManager db) {
        this.currentUser = user;
        this.db = db;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Frizt Course Manager PRO");
        frame.setSize(950, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BG_DARK);
        frame.setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_DARK);
        topPanel.setBorder(new EmptyBorder(20, 30, 10, 30));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Привет, " + currentUser.getUsername());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        JLabel roleLabel = new JLabel("Роль: " + currentUser.getRoleName());
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roleLabel.setForeground(NEON_GREEN);
        headerPanel.add(titleLabel);
        headerPanel.add(roleLabel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        JButton btnRefresh = createStyledButton("Обновить", CARD_DARK, Color.WHITE);
        JButton btnExport = createStyledButton("CSV Экспорт", CARD_DARK, Color.WHITE);
        JButton btnAdd = createStyledButton("Добавить", NEON_GREEN, Color.BLACK);
        JButton btnDelete = createStyledButton("Удалить", new Color(255, 69, 58), Color.WHITE);

        buttonsPanel.add(btnRefresh);
        buttonsPanel.add(btnExport);

        if (!currentUser.getRoleName().equals("STUDENT")) {
            buttonsPanel.add(btnAdd);
            buttonsPanel.add(btnDelete);
        }

        topPanel.add(headerPanel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);

        coursesContainer = new JPanel();
        coursesContainer.setLayout(new BoxLayout(coursesContainer, BoxLayout.Y_AXIS));
        coursesContainer.setBackground(BG_DARK);
        coursesContainer.setBorder(new EmptyBorder(10, 30, 20, 30));

        JScrollPane scrollPane = new JScrollPane(coursesContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadCourses());

        btnAdd.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Название курса:");
            String inst = JOptionPane.showInputDialog("Преподаватель:");
            String time = JOptionPane.showInputDialog("Время:");
            String day = JOptionPane.showInputDialog("День недели:");
            if (name != null && !name.trim().isEmpty()) {
                db.addCourse(new Course(0, name, inst, time, day));
                loadCourses();
            }
        });

        btnDelete.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Введите ID курса для удаления:");
            if (idStr != null && !idStr.isEmpty()) {
                db.deleteCourse(Integer.parseInt(idStr));
                loadCourses();
            }
        });

        btnExport.addActionListener(e -> {
            db.exportToCSV("courses.csv");
            JOptionPane.showMessageDialog(frame, "Успешно сохранено в файл courses.csv", "Экспорт", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setVisible(true);
        loadCourses();
    }

    private void loadCourses() {
        coursesContainer.removeAll();
        List<Course> courses = db.getAllCourses();

        for (Course c : courses) {
            coursesContainer.add(createDesktopCourseCard(c));
            coursesContainer.add(Box.createVerticalStrut(10));
        }

        coursesContainer.revalidate();
        coursesContainer.repaint();
    }

    private JPanel createDesktopCourseCard(Course course) {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new GridLayout(1, 5, 10, 0));
        card.setBackground(CARD_DARK);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setMaximumSize(new Dimension(900, 70));

        // 1. Название
        JLabel nameLabel = new JLabel(course.getCourseName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);

        // 2. Преподаватель
        JLabel instLabel = new JLabel(course.getInstructor());
        instLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instLabel.setForeground(TEXT_MUTED);

        // 3. День недели
        JLabel dayLabel = new JLabel(course.getDayOfWeek());
        dayLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dayLabel.setForeground(TEXT_MUTED);

        // 4. Время
        JLabel timeLabel = new JLabel(course.getScheduleTime());
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        timeLabel.setForeground(NEON_GREEN);

        // 5. ID (Справа)
        JLabel idLabel = new JLabel("ID: " + course.getId(), SwingConstants.RIGHT);
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        idLabel.setForeground(Color.DARK_GRAY);

        card.add(nameLabel);
        card.add(instLabel);
        card.add(dayLabel);
        card.add(timeLabel);
        card.add(idLabel);

        return card;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        return btn;
    }

    class RoundedPanel extends JPanel {
        private int radius;
        public RoundedPanel(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}