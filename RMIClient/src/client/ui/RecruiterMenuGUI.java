package client.ui;

import client.RMIClient;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;

public class RecruiterMenuGUI extends JFrame {

    public RecruiterMenuGUI(RMIClient rmiClient, Session session) {
        setTitle("Recruiter Home - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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

        // Center panel - Main menu buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new GridLayout(4, 1, 30, 30));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JButton profileBtn = new JButton("Recruiter Profile");
        JButton jobMgmtBtn = new JButton("Job Management");
        JButton appMgmtBtn = new JButton("Application Management");
        JButton candidateBtn = new JButton("Candidate Matching & Search");

        profileBtn.setFont(new Font("Arial", Font.BOLD, 18));
        jobMgmtBtn.setFont(new Font("Arial", Font.BOLD, 18));
        appMgmtBtn.setFont(new Font("Arial", Font.BOLD, 18));
        candidateBtn.setFont(new Font("Arial", Font.BOLD, 18));

        profileBtn.addActionListener(e -> {
            dispose();
            new RecruiterProfileScreen(rmiClient, session);
        });
        jobMgmtBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Job Management screen not implemented yet."));
        appMgmtBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Application Management screen not implemented yet."));
        candidateBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Candidate Matching & Search screen not implemented yet."));

        centerPanel.add(profileBtn);
        centerPanel.add(jobMgmtBtn);
        centerPanel.add(appMgmtBtn);
        centerPanel.add(candidateBtn);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

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
}
