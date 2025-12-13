package client.ui;

import client.RMIClient;
import shared.models.Job;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JobManagementPanel extends JPanel {
    private RMIClient client;
    private Session session;

    public JobManagementPanel(RMIClient client, Session session) {
        this.client = client;
        this.session = session;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Job Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton createJobBtn = new JButton("Create Job Posting");
        JButton viewJobsBtn = new JButton("View My Job Postings");
        JButton closeJobBtn = new JButton("Close Job Posting");

        buttonPanel.add(createJobBtn);
        buttonPanel.add(viewJobsBtn);
        buttonPanel.add(closeJobBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // Button actions
        createJobBtn.addActionListener(e -> createJob());
        viewJobsBtn.addActionListener(e -> viewJobs());
        closeJobBtn.addActionListener(e -> closeJob());
    }

    // 1. Create Job Posting GUI
    private void createJob() {
        JTextField jobTitleField = new JTextField();
        JTextField jobDescField = new JTextField();
        JTextField companyField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextArea requirementsArea = new JTextArea(5, 20);
        JScrollPane requirementsScroll = new JScrollPane(requirementsArea);
    
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Job Title:"));        panel.add(jobTitleField);
        panel.add(new JLabel("Job Description:"));  panel.add(jobDescField);
        panel.add(new JLabel("Company:"));          panel.add(companyField);
        panel.add(new JLabel("Location:"));         panel.add(locationField);
        panel.add(new JLabel("Salary:"));           panel.add(salaryField);
        panel.add(new JLabel("Requirements (one per line):")); panel.add(requirementsScroll);
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Create Job Posting", JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION) {
            try {
                String title = jobTitleField.getText().trim();
                String desc = jobDescField.getText().trim();
                String company = companyField.getText().trim();
                String location = locationField.getText().trim();
                double salary = Double.parseDouble(salaryField.getText().trim());
                String requirementsText = requirementsArea.getText().trim();
    
                // Validate required fields
                if (title.isEmpty() || desc.isEmpty() || company.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (requirementsText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter at least one requirement.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                // Split requirements into a list by lines, remove any empty lines
                String[] reqArray = requirementsText.split("\\R");
                java.util.List<String> requirements = new java.util.ArrayList<>();
                for (String req : reqArray) {
                    String r = req.trim();
                    if (!r.isEmpty()) requirements.add(r);
                }
                if (requirements.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter at least one requirement.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                Job job = new Job(title, desc, requirements, session.getUserId());
                job.setCompany(company);
                job.setLocation(location);
                job.setSalary(salary);
                String jobId = client.getJobService().createJob(job);
    
                JOptionPane.showMessageDialog(this, "Job posted successfully with ID: " + jobId, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Salary must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 2. View My Job Postings
    private void viewJobs() {
        try {
            List<Job> jobs = client.getJobService().getJobsByRecruiterId(session.getUserId());
            if(jobs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You have not posted any jobs yet.");
            } else {
                StringBuilder sb = new StringBuilder();
                for(Job job : jobs) {
                    sb.append("Title: ").append(job.getTitle()).append("\n");
                    sb.append("Company: ").append(job.getCompany()).append("\n");
                    sb.append("Location: ").append(job.getLocation()).append("\n");
                    sb.append("Salary: ").append(job.getSalary()).append("\n");
                    sb.append("Status: ").append(job.getStatus()).append("\n");
                    sb.append("------------\n");
                }
                JTextArea area = new JTextArea(sb.toString(), 15, 40);
                area.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "My Job Postings", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // 3. Close Job Posting GUI
    private void closeJob() {
        try {
            List<Job> jobs = client.getJobService().getJobsByRecruiterId(session.getUserId());
            jobs.removeIf(job -> !"OPEN".equalsIgnoreCase(job.getStatus()));
            if(jobs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No open job postings to close!");
                return;
            }
            String[] jobTitles = jobs.stream().map(job -> job.getTitle() + " (ID: " + job.getJobId() + ")").toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(this, "Select Job Posting to Close:", "Close Job", JOptionPane.PLAIN_MESSAGE, null, jobTitles, jobTitles[0]);
            if(selected != null) {
                for(Job job : jobs) {
                    if(selected.contains(job.getJobId())) {
                        boolean closed = client.getJobService().closeJob(job.getJobId());
                        if(closed) {
                            JOptionPane.showMessageDialog(this, "Job closed.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Could not close job.");
                        }
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}