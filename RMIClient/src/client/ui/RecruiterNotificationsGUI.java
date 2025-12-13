package client.ui;

import client.RMIClient;
import shared.models.Notification;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecruiterNotificationsGUI extends JFrame {

    private RMIClient rmiClient;
    private Session session;
    private JPanel listPanel;

    public RecruiterNotificationsGUI(RMIClient rmiClient, Session session) {
        this.rmiClient = rmiClient;
        this.session = session;

        // Frame setup
        setTitle("Notifications - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        // Main panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient similar to Recruiter Menu (Green)
                GradientPaint gradient = new GradientPaint(0, 0, new Color(46, 204, 113), 
                        getWidth(), getHeight(), new Color(39, 174, 96));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);
        add(mainPanel);

        // Header Title
        JLabel titleLabel = new JLabel("🔔 Recruiter Notifications");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 40, 500, 50);
        mainPanel.add(titleLabel);

        // Back Button
        JButton backBtn = new JButton("← Back to Dashboard");
        backBtn.setBounds(950, 50, 200, 30);
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(new Color(231, 76, 60)); // Red accent
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            dispose();
            new RecruiterMenuGUI(rmiClient, session);
        });
        mainPanel.add(backBtn);

        // Scroll pane for notifications
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(236, 240, 241));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBounds(50, 120, 1100, 630);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane);

        // Load notifications
        loadNotifications();

        setVisible(true);
    }

    private void loadNotifications() {
        listPanel.removeAll();

        try {
            // Use getRecruiterService() to fetch notifications
            // RecruiterService may not expose notifications in the current interface; use ApplicantService for now
            List<Notification> notifications = rmiClient.getApplicantService().getNotifications(session.getUserId());

            if (notifications.isEmpty()) {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setBackground(new Color(236, 240, 241));
                emptyPanel.setPreferredSize(new Dimension(1000, 100));
                
                JLabel emptyLabel = new JLabel("No scheduled interviews updates! 📅");
                emptyLabel.setFont(new Font("Arial", Font.ITALIC, 18));
                emptyLabel.setForeground(Color.GRAY);
                emptyPanel.add(emptyLabel);
                
                listPanel.add(emptyPanel);
            } else {
                for (Notification notification : notifications) {
                    addNotificationCard(notification);
                    listPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load notifications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void addNotificationCard(Notification notification) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(1080, 80));
        card.setPreferredSize(new Dimension(1080, 80));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Icon
        JLabel iconLabel = new JLabel("📝");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        card.add(iconLabel, BorderLayout.WEST);

        // Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.setBackground(Color.WHITE);

        JLabel msgLabel = new JLabel(notification.getMessage());
        msgLabel.setFont(new Font("Arial", Font.BOLD, 16));
        msgLabel.setForeground(new Color(44, 62, 80));
        contentPanel.add(msgLabel);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        JLabel dateLabel = new JLabel(sdf.format(notification.getDate()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);
        contentPanel.add(dateLabel);

        card.add(contentPanel, BorderLayout.CENTER);

        listPanel.add(card);
    }
}
