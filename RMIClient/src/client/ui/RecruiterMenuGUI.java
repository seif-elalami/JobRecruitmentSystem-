package client.ui;

import client.RMIClient;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;

/**
 * Recruiter Menu GUI - Swing based interface
 * This will eventually replace the console-based RecruiterMenu
 */
public class RecruiterMenuGUI extends JFrame {

    private RMIClient rmiClient;
    private Session session;

    public RecruiterMenuGUI(RMIClient rmiClient, Session session) {
        this.rmiClient = rmiClient;
        this.session = session;

        // Frame setup
        setTitle("Recruiter Dashboard - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel welcomeLabel = new JLabel("Welcome, " + session.getUserEmail() + " (Recruiter)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);

        headerPanel.add(welcomeLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center panel - Menu
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createTitledBorder("Recruiter Features"));
        centerPanel.setLayout(new GridLayout(3, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Feature buttons
        addFeatureButton(centerPanel, "📋 Post Job", "Create and publish new job listings");
        addFeatureButton(centerPanel, "👥 View Applications", "Review applications from candidates");
        addFeatureButton(centerPanel, "🔍 Search Candidates", "Find candidates by skills and experience");
        addFeatureButton(centerPanel, "📅 Schedule Interview", "Schedule interviews with applicants");
        addFeatureButton(centerPanel, "📊 Job Postings", "Manage your active job postings");
        addFeatureButton(centerPanel, "🔐 Settings", "Account settings and preferences");

        mainPanel.add(new JScrollPane(centerPanel), BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setPreferredSize(new Dimension(0, 50));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(e -> {
            dispose();
            new WelcomePage(rmiClient);
        });

        footerPanel.add(logoutButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Add feature button to panel
     */
    private void addFeatureButton(JPanel parent, String title, String description) {
        JPanel buttonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(236, 240, 241));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.setColor(new Color(52, 152, 219));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(127, 140, 141));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(titleLabel);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(descLabel);
        buttonPanel.add(Box.createVerticalGlue());

        parent.add(buttonPanel);
    }
}
