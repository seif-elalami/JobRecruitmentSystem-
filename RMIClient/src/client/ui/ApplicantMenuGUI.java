package client.ui;

import client.RMIClient;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ApplicantMenuGUI extends JFrame {

    private RMIClient rmiClient;
    private Session session;

    public ApplicantMenuGUI(RMIClient rmiClient, Session session) {
        this.rmiClient = rmiClient;
        this.session = session;

        setTitle("Applicant Dashboard - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(20, 33, 61), 
                        getWidth(), getHeight(), new Color(52, 152, 219));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(255, 255, 255, 5));
                for (int i = 0; i < getWidth(); i += 50) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
            }
        };
        mainPanel.setLayout(null);
        add(mainPanel);

        JButton closeBtn = new JButton("✕");
        closeBtn.setBounds(1160, 10, 30, 30);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        closeBtn.setBackground(Color.WHITE);
        closeBtn.setForeground(new Color(52, 152, 219));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        mainPanel.add(closeBtn);

        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, new Color(29, 45, 68), getWidth(), 0, new Color(58, 123, 213));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        headerPanel.setLayout(null);
        headerPanel.setBounds(0, 0, 1200, 90);
        headerPanel.setOpaque(false);
        mainPanel.add(headerPanel);

        JLabel welcomeLabel = new JLabel("👋 Welcome, " + session.getUserEmail() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(40, 22, 600, 46);
        headerPanel.add(welcomeLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setBounds(1060, 24, 100, 40);
        logoutBtn.addActionListener(e -> logout());
        headerPanel.add(logoutBtn);

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

        JLabel titleLabel = new JLabel("📋 My Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBounds(50, 25, 400, 40);
        contentPanel.add(titleLabel);

        String[][] features = {
            {"🔍", "Browse Jobs", "Explore job\nopportunities"},
            {"📄", "Upload Resume", "Manage your\nresume"},
            {"💼", "My Applications", "Track your\napplications"},
            {"🔔", "Notifications", "View your system\nnotifications"},
            {"👤", "Update Profile", "Edit your profile\ninformation"},
            {"📘", "View Resume", "See your\nlatest resume"},
            {"👀", "View Profile", "View your profile\ndetails"}
        };

        int x = 50;
        int y = 85;
        int width = 340;
        int height = 190;

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
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2d.setColor(new Color(0, 0, 0, 25));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 18, 18);
            }
        };
        cardPanel.setLayout(null);
        cardPanel.setBounds(x, y, width, height);
        cardPanel.setOpaque(false);
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                cardPanel.setBackground(new Color(245, 250, 255));
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

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setBounds(24, 18, 60, 60);
        cardPanel.add(iconLabel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBounds(20, 82, 280, 22);
        cardPanel.add(titleLabel);

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(91, 105, 120));
        descLabel.setBounds(20, 108, 280, 60);
        cardPanel.add(descLabel);

        parent.add(cardPanel);
    }

    private void handleFeatureClick(int index) {
        switch(index) {
            case 0:

                dispose();
                try {
                    new BrowseJobsPage(rmiClient, session);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error opening Browse Jobs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    new ApplicantMenuGUI(rmiClient, session);
                }
                break;
            case 1:

                dispose();
                try {
                    new UploadResumePage(rmiClient, session);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error opening Upload Resume: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    new ApplicantMenuGUI(rmiClient, session);
                }
                break;
            case 2:

                dispose();
                try {
                    new MyApplicationsPage(rmiClient, session);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error opening Applications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    new ApplicantMenuGUI(rmiClient, session);
                }
                break;
            case 3:

                dispose();
                new ApplicantNotificationsGUI(rmiClient, session);
                break;
            case 4:

                dispose();
                try {
                    new UpdateProfilePage(rmiClient, session);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error opening Update Profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    new ApplicantMenuGUI(rmiClient, session);
                }
                break;
            case 5:

                dispose();
                try {
                    new ViewResumePage(rmiClient, session);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error opening View Resume: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    new ApplicantMenuGUI(rmiClient, session);
                }
                break;
            case 6:

                dispose();
                try {
                    new ViewProfilePage(rmiClient, session);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error opening Profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    new ApplicantMenuGUI(rmiClient, session);
                }
                break;
        }
    }

    private void logout() {
        dispose();
        try {
            new WelcomePage(rmiClient);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                RMIClient rmiClient = new RMIClient();
                Session mockSession = new Session("User", "user@example.com", "Applicant");
                new ApplicantMenuGUI(rmiClient, mockSession);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to start: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}