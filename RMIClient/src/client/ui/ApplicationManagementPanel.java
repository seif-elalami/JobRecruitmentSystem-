package client.ui;

import client.RMIClient;
import shared.models.Application;
import shared.models.Job;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationManagementPanel extends JPanel {
    private RMIClient client;
    private Session session;

    public ApplicationManagementPanel(RMIClient client, Session session) {
        this.client = client;
        this.session = session;
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Application Management", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10,10));
        JButton viewAllBtn = new JButton("View All Applications");
        JButton viewByJobBtn = new JButton("View Apps for Specific Job");
        JButton reviewBtn = new JButton("Review Application");
        buttonPanel.add(viewAllBtn);
        buttonPanel.add(viewByJobBtn);
        buttonPanel.add(reviewBtn);
        add(buttonPanel, BorderLayout.CENTER);

        viewAllBtn.addActionListener(e -> viewAllApplications());
        viewByJobBtn.addActionListener(e -> viewApplicationsForSpecificJob());
        reviewBtn.addActionListener(e -> reviewApplication());
    }

    private void viewAllApplications() {
        try {
            List<Application> applications = client.getApplicationService().getAllApplications();
            // If you want ONLY this recruiter's jobs:
            List<Job> jobs = client.getJobService().getJobsByRecruiterId(session.getUserId());
            List<String> myJobIds = jobs.stream().map(Job::getJobId).collect(Collectors.toList());
            applications = applications.stream()
                .filter(app -> myJobIds.contains(app.getJobId()))
                .collect(Collectors.toList());

            if (applications.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No applications found for your posted jobs.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (Application app : applications) {
                sb.append(i++).append(". Application ID: ").append(app.getApplicationId()).append("\n");
                sb.append("   Job ID: ").append(app.getJobId()).append("\n");
                sb.append("   Applicant ID: ").append(app.getApplicantId()).append("\n");
                sb.append("   Status: ").append(app.getStatus()).append("\n");
                sb.append("------------------------------------\n");
            }
            JTextArea area = new JTextArea(sb.toString(), 15, 40);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "All Applications for Your Jobs", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void viewApplicationsForSpecificJob() {
        try {
            String jobId = JOptionPane.showInputDialog(this, "Enter Job ID to view its applications:");
            if (jobId == null || jobId.trim().isEmpty()) {
                // User cancelled or empty input
                return;
            }
            jobId = jobId.trim();
    
            // Fetch applications for the provided job ID
            java.util.List<Application> applications = client.getRecruiterService().getApplicationsForJob(jobId);
    
            if (applications == null || applications.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No applications found for Job ID " + jobId + ".");
                return;
            }
    
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (Application app : applications) {
                sb.append(i++).append(". Application ID: ").append(app.getApplicationId()).append("\n");
                sb.append("   Applicant ID: ").append(app.getApplicantId()).append("\n");
                sb.append("   Status: ").append(app.getStatus()).append("\n\n");
            }
            JTextArea area = new JTextArea(sb.toString(), 15, 40);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Applications for Job ID: " + jobId, JOptionPane.INFORMATION_MESSAGE);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // --- 3. Review Application ---
    private void reviewApplication() {
        try {
            String appId = JOptionPane.showInputDialog(this, "Enter Application ID to review:");
            if (appId == null || appId.trim().isEmpty()) return;

            Application app = client.getApplicationService().getApplicationById(appId);
            if (app == null) {
                JOptionPane.showMessageDialog(this, "Application not found!");
                return;
            }
            // Optional: Show details
            String msg = "Applicant ID: " + app.getApplicantId() +
                    "\nJob ID: " + app.getJobId() +
                    "\nStatus: " + app.getStatus() +
                    (app.getCoverLetter() != null ? "\nCover Letter:\n" + app.getCoverLetter() : "");
            JOptionPane.showMessageDialog(this, msg, "Application Details", JOptionPane.INFORMATION_MESSAGE);

            String[] options = {"Approve", "Reject", "Under Review", "Cancel"};
            int c = JOptionPane.showOptionDialog(this, "Update status of Application " + appId, "Review",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (c == 3 || c == JOptionPane.CLOSED_OPTION) return;
            String newStatus = switch (c) {
                case 0 -> "ACCEPTED";
                case 1 -> "REJECTED";
                case 2 -> "UNDER_REVIEW";
                default -> app.getStatus();
            };

            boolean ok = client.getApplicationService().updateApplicationStatus(appId, newStatus);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Application status updated to: " + newStatus);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update application status.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}