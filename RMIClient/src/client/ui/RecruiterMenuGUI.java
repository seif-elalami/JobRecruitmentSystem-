package client.ui;

import client.RMIClient;
import shared.interfaces.IAuthService;
import shared.interfaces.IJobService;
import shared.interfaces.IApplicationService;
import shared.interfaces.IRecruiterService;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;

public class RecruiterMenuGUI extends JFrame {
    private final RMIClient rmiClient;
    private final Session session;
    private IAuthService authService;
    private IJobService jobService;
    private IApplicationService applicationService;
    private IRecruiterService recruiterService;

    public RecruiterMenuGUI(RMIClient rmiClient, Session session) {
        this.rmiClient = rmiClient;
        this.session = session;
        
        // Get service references
        try {
            this.authService = rmiClient.getAuthService();
            this.jobService = rmiClient.getJobService();
            this.applicationService = rmiClient.getApplicationService();
            this.recruiterService = rmiClient.getRecruiterService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to services: " + e.getMessage());
            System.exit(1);
        }

        setTitle("Recruiter Portal - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(244, 247, 252));
        add(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        JLabel welcomeLabel = new JLabel("👔 Recruiter Portal - Welcome, " + session.getUserEmail());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center panel with scrollable menu
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(244, 247, 252));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(244, 247, 252));

        // ==================== RECRUITER PROFILE SECTION ====================
        menuPanel.add(createSectionHeader("👤 RECRUITER PROFILE"));
        menuPanel.add(createMenuButton("View My Profile", e -> openViewProfile()));
        menuPanel.add(createMenuButton("Update Profile", e -> openUpdateProfile()));
        menuPanel.add(createMenuButton("Change Password", e -> openChangePassword()));
        menuPanel.add(Box.createVerticalStrut(15));

        // ==================== JOB MANAGEMENT SECTION ====================
        menuPanel.add(createSectionHeader("💼 JOB MANAGEMENT"));
        menuPanel.add(createMenuButton("Create Job Posting", e -> openCreateJob()));
        menuPanel.add(createMenuButton("View My Job Postings", e -> openViewMyJobs()));
        menuPanel.add(createMenuButton("Close Job Posting", e -> openCloseJob()));
        menuPanel.add(Box.createVerticalStrut(15));

        // ==================== APPLICATION MANAGEMENT SECTION ====================
        menuPanel.add(createSectionHeader("📋 APPLICATION MANAGEMENT"));
        menuPanel.add(createMenuButton("View All Applications", e -> openViewAllApplications()));
        menuPanel.add(createMenuButton("View Applications for Specific Job", e -> openViewJobApplications()));
        menuPanel.add(createMenuButton("Review Application", e -> openReviewApplication()));
        menuPanel.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setPreferredSize(new Dimension(0, 50));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
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

    private JPanel createSectionHeader(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(244, 247, 252));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));
        
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(41, 128, 185));
        panel.add(label, BorderLayout.WEST);
        
        JSeparator separator = new JSeparator();
        separator.setBackground(new Color(200, 200, 200));
        
        panel.add(separator, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createMenuButton(String text, java.awt.event.ActionListener listener) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 235)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(41, 128, 185));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(235, 242, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.WHITE);
            }
        });
        
        panel.add(button, BorderLayout.WEST);
        
        return panel;
    }

    // ==================== RECRUITER PROFILE METHODS ====================
    private void openViewProfile() {
        new RecruiterViewProfilePage(this, session, recruiterService);
    }

    private void openUpdateProfile() {
        new RecruiterUpdateProfilePage(this, session, recruiterService);
    }

    private void openChangePassword() {
        new RecruiterChangePasswordPage(this, session, authService);
    }

    // ==================== JOB MANAGEMENT METHODS ====================
    private void openCreateJob() {
        new RecruiterCreateJobPage(this, session, jobService, recruiterService);
    }

    private void openViewMyJobs() {
        new RecruiterViewJobsPage(this, session, jobService);
    }

    private void openCloseJob() {
        new RecruiterCloseJobPage(this, session, jobService);
    }

    // ==================== APPLICATION MANAGEMENT METHODS ====================
    private void openViewAllApplications() {
        new RecruiterViewApplicationsPage(this, session, applicationService, authService, jobService);
    }

    private void openViewJobApplications() {
        new RecruiterJobApplicationsPage(this, session, applicationService, jobService, recruiterService);
    }

    private void openReviewApplication() {
        new RecruiterReviewApplicationPage(this, session, applicationService);
    }
}

