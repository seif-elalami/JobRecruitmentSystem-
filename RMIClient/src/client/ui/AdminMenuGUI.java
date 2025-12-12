package client.ui;

import client.RMIClient;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminMenuGUI extends JFrame {

    @SuppressWarnings("unused")
    private RMIClient rmiClient;
    @SuppressWarnings("unused")
    private Session session;

    public AdminMenuGUI(RMIClient rmiClient, Session session) {
        this.rmiClient = rmiClient;
        this.session = session;

        // Frame setup
        setTitle("Admin Dashboard - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        // Main panel with gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(155, 89, 182), 
                        getWidth(), getHeight(), new Color(108, 52, 131));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Subtle overlay pattern
                g2d.setColor(new Color(255, 255, 255, 5));
                for (int i = 0; i < getWidth(); i += 50) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
            }
        };
        mainPanel.setLayout(null);
        add(mainPanel);

        // Close button
        JButton closeBtn = new JButton("✕");
        closeBtn.setBounds(1160, 10, 30, 30);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        closeBtn.setBackground(Color.WHITE);
        closeBtn.setForeground(new Color(155, 89, 182));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        mainPanel.add(closeBtn);

        // Header panel
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(44, 62, 80));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(null);
        headerPanel.setBounds(0, 0, 1200, 90);
        headerPanel.setOpaque(false);
        mainPanel.add(headerPanel);

        // Welcome text
        JLabel welcomeLabel = new JLabel("👑 Welcome, Admin (" + session.getUserEmail() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(40, 20, 600, 50);
        headerPanel.add(welcomeLabel);

        // Content panel
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(236, 240, 241));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 90, 1200, 710);
        contentPanel.setOpaque(false);
        mainPanel.add(contentPanel);

        // Title
        JLabel titleLabel = new JLabel("🛡️ System Administration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBounds(50, 25, 400, 40);
        contentPanel.add(titleLabel);

        // Features grid (2x3 layout)
        String[][] features = {
            {"👥", "Manage Users", "Create, edit, and\ndelete user accounts"},
            {"📊", "View Reports", "Generate system\nanalytics and reports"},
            {"💼", "Manage Jobs", "Monitor all job\npostings in system"},
            {"📋", "Manage Applications", "Review and manage\nall applications"},
            {"🔐", "Permissions", "Control user roles\nand permissions"},
            {"📝", "View Logs", "System logs and\naudit trail"}
        };

        int x = 50;
        int y = 85;
        int width = 320;
        int height = 180;

        for (int i = 0; i < 6; i++) {
            int row = i / 3;
            int col = i % 3;
            int posX = x + col * (width + 20);
            int posY = y + row * (height + 20);

            createFeatureCard(contentPanel, features[i][0], features[i][1], features[i][2], posX, posY, width, height, i);
        }

        setVisible(true);
    }

    private void createFeatureCard(JPanel parent, String icon, String title, String description, 
                                   int x, int y, int width, int height, int index) {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(new Color(189, 195, 199));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        cardPanel.setLayout(null);
        cardPanel.setBounds(x, y, width, height);
        cardPanel.setOpaque(false);
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                cardPanel.setBackground(new Color(236, 240, 241));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                cardPanel.setBackground(Color.WHITE);
            }
            @Override
            public void mouseClicked(MouseEvent evt) {
                handleFeatureClick(index);
            }
        });

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 50));
        iconLabel.setBounds(20, 15, 60, 60);
        cardPanel.add(iconLabel);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBounds(20, 80, 280, 20);
        cardPanel.add(titleLabel);

        // Description
        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(127, 140, 141));
        descLabel.setBounds(20, 105, 280, 60);
        cardPanel.add(descLabel);

        parent.add(cardPanel);
    }

    private void handleFeatureClick(int index) {
        switch(index) {
            case 0:
                JOptionPane.showMessageDialog(this, "👥 Manage Users feature coming soon!", "Feature", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 1:
                JOptionPane.showMessageDialog(this, "📊 View Reports feature coming soon!", "Feature", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(this, "💼 Manage Jobs feature coming soon!", "Feature", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 3:
                JOptionPane.showMessageDialog(this, "📋 Manage Applications feature coming soon!", "Feature", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 4:
                JOptionPane.showMessageDialog(this, "🔐 Permissions feature coming soon!", "Feature", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 5:
                JOptionPane.showMessageDialog(this, "📝 View Logs feature coming soon!", "Feature", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                RMIClient rmiClient = new RMIClient();
                Session mockSession = new Session("Admin", "admin@example.com", "Admin");
                new AdminMenuGUI(rmiClient, mockSession);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to start: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
