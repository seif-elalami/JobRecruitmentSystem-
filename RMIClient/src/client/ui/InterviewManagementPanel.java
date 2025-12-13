package client.ui;

import client.RMIClient;
import shared.models.Session;
import shared.models.Interview;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class InterviewManagementPanel extends JPanel {
    private final RMIClient client;
    private final Session session;

    public InterviewManagementPanel(RMIClient client, Session session) {
        this.client = client;
        this.session = session;
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Interview Management", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton scheduleBtn = new JButton("Schedule Interview");
        JButton viewMyInterviewsBtn = new JButton("View My Interviews");
        JButton viewDetailsBtn = new JButton("View Interview Details");
        JButton updateBtn = new JButton("Update Interview");
        JButton cancelBtn = new JButton("Cancel Interview");

        buttonPanel.add(scheduleBtn);
        buttonPanel.add(viewMyInterviewsBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);

        add(buttonPanel, BorderLayout.CENTER);

        scheduleBtn.addActionListener(e -> scheduleInterview());
        viewMyInterviewsBtn.addActionListener(e -> viewMyInterviews());
        viewDetailsBtn.addActionListener(e -> viewInterviewDetails());
        updateBtn.addActionListener(e -> updateInterview());
        cancelBtn.addActionListener(e -> cancelInterview());
    }

    private void scheduleInterview() {
        try {

            String jobId = JOptionPane.showInputDialog(this, "Enter Job ID:");
            if (jobId == null || jobId.trim().isEmpty()) return;
            jobId = jobId.trim();

            try {
                if (client.getJobService().getJobById(jobId) == null) {
                    JOptionPane.showMessageDialog(this, "Job not found with ID: " + jobId);
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error validating job: " + e.getMessage());
                return;
            }

            String applicantId = JOptionPane.showInputDialog(this, "Enter Applicant (Candidate) ID:");
            if (applicantId == null || applicantId.trim().isEmpty()) return;
            applicantId = applicantId.trim();

            try {
                if (client.getRecruiterService().getCandidateById(applicantId) == null) {
                    JOptionPane.showMessageDialog(this, "Candidate not found with ID: " + applicantId);
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error validating candidate: " + e.getMessage());
                return;
            }

            String dateStr = JOptionPane.showInputDialog(this, "Enter Interview Date (DD/MM/YYYY):");
            if (dateStr == null || dateStr.trim().isEmpty()) return;
            dateStr = dateStr.trim();

            String timeStr = JOptionPane.showInputDialog(this, "Enter Interview Time (HH:MM, 24-hour format):");
            if (timeStr == null || timeStr.trim().isEmpty()) return;
            timeStr = timeStr.trim();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            java.util.Date scheduledDate;
            try {
                scheduledDate = sdf.parse(dateStr + " " + timeStr);

                if (scheduledDate.before(new java.util.Date())) {
                    JOptionPane.showMessageDialog(this, "Error: Interview date and time must be in the future.");
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid date/time format. Please use DD/MM/YYYY and HH:MM");
                return;
            }

            String location = JOptionPane.showInputDialog(this, "Enter Location (e.g., 'Online - Zoom' or 'Office - Room 301'):");
            if (location == null || location.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Location is required.");
                return;
            }
            location = location.trim();

            String notes = JOptionPane.showInputDialog(this, "Enter Notes (Optional, press Cancel to skip):");
            if (notes == null) notes = "";

            Interview interview = new Interview(jobId, applicantId, session.getUserId(), scheduledDate, location);
            if (!notes.isEmpty()) {
                interview.setNotes(notes);
            }

            String interviewId = client.getRecruiterService().createInterview(interview);

            JOptionPane.showMessageDialog(this, 
                "✅ Interview scheduled successfully!\n" +
                "Interview ID: " + interviewId + "\n" +
                "Date & Time: " + sdf.format(scheduledDate) + "\n" +
                "Location: " + location,
                "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewMyInterviews() {
        try {
            List<Interview> interviews = client.getRecruiterService().getMyInterviews(session.getUserId());

            if (interviews == null || interviews.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No interviews found.", "My Interviews", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            sb.append("Found ").append(interviews.size()).append(" interview(s):\n\n");

            for (int i = 0; i < interviews.size(); i++) {
                Interview interview = interviews.get(i);
                sb.append("Interview ").append(i + 1).append(":\n");
                sb.append("  ID: ").append(interview.getInterviewId()).append("\n");
                sb.append("  Job ID: ").append(interview.getJobId()).append("\n");
                sb.append("  Applicant ID: ").append(interview.getApplicantId()).append("\n");
                sb.append("  Date & Time: ").append(sdf.format(interview.getScheduledDate())).append("\n");
                sb.append("  Location: ").append(interview.getLocation()).append("\n");
                sb.append("  Status: ").append(interview.getStatus()).append("\n");
                if (interview.getNotes() != null && !interview.getNotes().isEmpty()) {
                    sb.append("  Notes: ").append(interview.getNotes()).append("\n");
                }
                sb.append("\n");
            }

            JTextArea area = new JTextArea(sb.toString(), 20, 50);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "My Interviews", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewInterviewDetails() {
        try {
            String interviewId = JOptionPane.showInputDialog(this, "Enter Interview ID:");
            if (interviewId == null || interviewId.trim().isEmpty()) return;
            interviewId = interviewId.trim();

            Interview interview = client.getRecruiterService().getInterviewById(interviewId);

            if (interview == null) {
                JOptionPane.showMessageDialog(this, "Interview not found.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            StringBuilder sb = new StringBuilder();
            sb.append("Interview Details:\n\n");
            sb.append("Interview ID: ").append(interview.getInterviewId()).append("\n");
            sb.append("Job ID: ").append(interview.getJobId()).append("\n");
            sb.append("Applicant ID: ").append(interview.getApplicantId()).append("\n");
            sb.append("Recruiter ID: ").append(interview.getRecruiterId()).append("\n");
            sb.append("Date & Time: ").append(sdf.format(interview.getScheduledDate())).append("\n");
            sb.append("Location: ").append(interview.getLocation()).append("\n");
            sb.append("Status: ").append(interview.getStatus()).append("\n");
            if (interview.getNotes() != null && !interview.getNotes().isEmpty()) {
                sb.append("Notes: ").append(interview.getNotes()).append("\n");
            }

            JTextArea area = new JTextArea(sb.toString(), 12, 40);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Interview Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateInterview() {
        try {
            String interviewId = JOptionPane.showInputDialog(this, "Enter Interview ID to update:");
            if (interviewId == null || interviewId.trim().isEmpty()) return;
            interviewId = interviewId.trim();

            Interview interview = client.getRecruiterService().getInterviewById(interviewId);
            if (interview == null) {
                JOptionPane.showMessageDialog(this, "Interview not found.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String currentInfo = "Current Interview Details:\n" +
                    "Date & Time: " + sdf.format(interview.getScheduledDate()) + "\n" +
                    "Location: " + interview.getLocation() + "\n" +
                    "Status: " + interview.getStatus() + "\n" +
                    "Notes: " + (interview.getNotes() != null ? interview.getNotes() : "None");

            JOptionPane.showMessageDialog(this, currentInfo, "Current Interview", JOptionPane.INFORMATION_MESSAGE);

            String dateStr = JOptionPane.showInputDialog(this, "Enter New Date (DD/MM/YYYY) or press Cancel to keep current:");
            String timeStr = null;
            java.util.Date newDate = interview.getScheduledDate();

            if (dateStr != null && !dateStr.trim().isEmpty()) {
                timeStr = JOptionPane.showInputDialog(this, "Enter New Time (HH:MM) or press Cancel to keep current:");
                if (timeStr != null && !timeStr.trim().isEmpty()) {
                    try {
                        newDate = sdf.parse(dateStr.trim() + " " + timeStr.trim());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Invalid date/time format.");
                        return;
                    }
                }
            }

            String location = JOptionPane.showInputDialog(this, "Enter New Location or press Cancel to keep current:");
            if (location == null) location = interview.getLocation();
            else if (location.trim().isEmpty()) location = interview.getLocation();
            else location = location.trim();

            String notes = JOptionPane.showInputDialog(this, "Enter New Notes or press Cancel to keep current:");
            if (notes == null) notes = interview.getNotes();
            else if (notes.trim().isEmpty()) notes = interview.getNotes();
            else notes = notes.trim();

            interview.setScheduledDate(newDate);
            interview.setLocation(location);
            if (notes != null) interview.setNotes(notes);

            boolean success = client.getRecruiterService().updateInterview(interview);

            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Interview updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update interview.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelInterview() {
        try {
            String interviewId = JOptionPane.showInputDialog(this, "Enter Interview ID to cancel:");
            if (interviewId == null || interviewId.trim().isEmpty()) return;
            interviewId = interviewId.trim();

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to cancel this interview?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            boolean success = client.getRecruiterService().cancelInterview(interviewId);

            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Interview cancelled successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel interview.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}