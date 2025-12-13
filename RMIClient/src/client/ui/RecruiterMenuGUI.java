package client.ui;

import client.RMIClient;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;

public class RecruiterMenuGUI extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public RecruiterMenuGUI(RMIClient rmiClient, Session session) {
        setTitle("Recruiter Home - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel homePanel = new JPanel(new BorderLayout(10, 10));
        homePanel.setBackground(new Color(236, 240, 241));
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        JLabel welcomeLabel = new JLabel("Welcome, " + session.getUserEmail() + " (Recruiter)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);
        homePanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new GridLayout(5, 1, 30, 30));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JButton profileBtn = new JButton("Recruiter Profile");
        JButton jobMgmtBtn = new JButton("Job Management");
        JButton appMgmtBtn = new JButton("Application Management");
        JButton candidateBtn = new JButton("Candidate Matching & Search");
        JButton interviewBtn = new JButton("Interview Management");

        profileBtn.setFont(new Font("Arial", Font.BOLD, 18));
        jobMgmtBtn.setFont(new Font("Arial", Font.BOLD, 18));
        appMgmtBtn.setFont(new Font("Arial", Font.BOLD, 18));
        candidateBtn.setFont(new Font("Arial", Font.BOLD, 18));
        interviewBtn.setFont(new Font("Arial", Font.BOLD, 18));

        profileBtn.addActionListener(e -> {
            dispose();
            new RecruiterProfileScreen(rmiClient, session);
        });

        jobMgmtBtn.addActionListener(e -> cardLayout.show(cardPanel, "JobManagement"));

        appMgmtBtn.addActionListener(e -> cardLayout.show(cardPanel, "AppManagement"));

        candidateBtn.addActionListener(e -> cardLayout.show(cardPanel, "CandidateMatching"));

        interviewBtn.addActionListener(e -> cardLayout.show(cardPanel, "InterviewManagement"));

        centerPanel.add(profileBtn);
        centerPanel.add(jobMgmtBtn);
        centerPanel.add(appMgmtBtn);
        centerPanel.add(candidateBtn);
        centerPanel.add(interviewBtn);

        homePanel.add(centerPanel, BorderLayout.CENTER);

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
        homePanel.add(footerPanel, BorderLayout.SOUTH);

        JobManagementPanel jobPanel = new JobManagementPanel(rmiClient, session);
        JButton backBtnJob = new JButton("← Back to Menu");
        backBtnJob.setFont(new Font("Arial", Font.PLAIN, 14));
        backBtnJob.addActionListener(e -> cardLayout.show(cardPanel, "HomeMenu"));
        JPanel jobPanelWithBack = new JPanel(new BorderLayout());
        jobPanelWithBack.add(backBtnJob, BorderLayout.NORTH);
        jobPanelWithBack.add(jobPanel, BorderLayout.CENTER);

        ApplicationManagementPanel appManagementPanel = new ApplicationManagementPanel(rmiClient, session);
        JButton backBtnAppMgmt = new JButton("← Back to Menu");
        backBtnAppMgmt.setFont(new Font("Arial", Font.PLAIN, 14));
        backBtnAppMgmt.addActionListener(e -> cardLayout.show(cardPanel, "HomeMenu"));
        JPanel appMgmtPanelWithBack = new JPanel(new BorderLayout());
        appMgmtPanelWithBack.add(backBtnAppMgmt, BorderLayout.NORTH);
        appMgmtPanelWithBack.add(appManagementPanel, BorderLayout.CENTER);

        CandidateMatchingPanel candidatePanel = new CandidateMatchingPanel(rmiClient, session);
        JButton backBtnCandidate = new JButton("← Back to Menu");
        backBtnCandidate.setFont(new Font("Arial", Font.PLAIN, 14));
        backBtnCandidate.addActionListener(e -> cardLayout.show(cardPanel, "HomeMenu"));
        JPanel candidatePanelWithBack = new JPanel(new BorderLayout());
        candidatePanelWithBack.add(backBtnCandidate, BorderLayout.NORTH);
        candidatePanelWithBack.add(candidatePanel, BorderLayout.CENTER);

        InterviewManagementPanel interviewPanel = new InterviewManagementPanel(rmiClient, session);
        JButton backBtnInterview = new JButton("← Back to Menu");
        backBtnInterview.setFont(new Font("Arial", Font.PLAIN, 14));
        backBtnInterview.addActionListener(e -> cardLayout.show(cardPanel, "HomeMenu"));
        JPanel interviewPanelWithBack = new JPanel(new BorderLayout());
        interviewPanelWithBack.add(backBtnInterview, BorderLayout.NORTH);
        interviewPanelWithBack.add(interviewPanel, BorderLayout.CENTER);

        cardPanel.add(homePanel, "HomeMenu");
        cardPanel.add(jobPanelWithBack, "JobManagement");
        cardPanel.add(appMgmtPanelWithBack, "AppManagement");
        cardPanel.add(candidatePanelWithBack, "CandidateMatching");
        cardPanel.add(interviewPanelWithBack, "InterviewManagement");

        add(cardPanel);

        cardLayout.show(cardPanel, "HomeMenu");
        setVisible(true);
    }
}