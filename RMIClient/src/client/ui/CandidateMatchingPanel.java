package client.ui;

import client.RMIClient;
import shared.interfaces.ICandidateView;
import shared.models.Session;
import shared.models.Applicant;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CandidateMatchingPanel extends JPanel {
    private final RMIClient client;
    private final Session session;

    public CandidateMatchingPanel(RMIClient client, Session session) {
        this.client = client;
        this.session = session;
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Candidate Matching & Search", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton matchBtn = new JButton("Match Candidates to Job (View CVs)");
        JButton viewDetailBtn = new JButton("View Candidate Details");
        JButton searchBySkillBtn = new JButton("Search Candidates by Skills");
        JButton searchByExperienceBtn = new JButton("Search by Experience Level");
        buttonPanel.add(matchBtn);
        buttonPanel.add(viewDetailBtn);
        buttonPanel.add(searchBySkillBtn);
        buttonPanel.add(searchByExperienceBtn);

        add(buttonPanel, BorderLayout.CENTER);

        matchBtn.addActionListener(e -> matchFinalCandidateToJob());
        viewDetailBtn.addActionListener(e -> viewCandidateDetails());
        searchBySkillBtn.addActionListener(e -> searchCandidatesBySkills());
        searchByExperienceBtn.addActionListener(e -> searchByExperienceLevel());
    }

  
    private void matchFinalCandidateToJob() {
        try {
            // 1. Ask for Job ID
            String jobId = JOptionPane.showInputDialog(this, "Enter Job ID:");
            if (jobId == null || jobId.trim().isEmpty()) return;
            jobId = jobId.trim();
    
            // 2. Fetch ALL applications for this job
            List<shared.models.Application> allApplications = client.getRecruiterService().getApplicationsForJob(jobId);
            if (allApplications == null || allApplications.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No applications found for this job.");
                return;
            }
    
            // 3. Filter to show ONLY ACCEPTED applications (final matching step)
            List<shared.models.Application> acceptedApplications = allApplications.stream()
                .filter(a -> {
                    String status = a.getStatus();
                    return status != null && (status.equalsIgnoreCase("ACCEPTED") || 
                                             status.equalsIgnoreCase("ACCEPT"));
                })
                .collect(java.util.stream.Collectors.toList());
    
            if (acceptedApplications.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No ACCEPTED applications found for this job.\n\n" +
                    "Please review and accept applications first using\n" +
                    "the 'Application Management' section.",
                    "No Accepted Candidates", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
    
            // 4. Build candidate list with full details
            String[] candidateChoices = new String[acceptedApplications.size()];
            for (int i = 0; i < acceptedApplications.size(); i++) {
                shared.models.Application a = acceptedApplications.get(i);
                String name = "Unknown";
                String email = "Unknown";
                try {
                    // Use recruiter service to get candidate info (works with users collection)
                    shared.interfaces.ICandidateView candidate = client.getRecruiterService().getCandidateById(a.getApplicantId());
                    if (candidate != null) {
                        name = candidate.getName();
                        email = candidate.getEmail();
                    } else {
                        // Fallback: try to get user info from auth service
                        try {
                            shared.models.User user = client.getAuthService().getUserById(a.getApplicantId());
                            if (user != null) {
                                name = user.getUsername();
                                email = user.getEmail();
                            }
                        } catch (Exception e2) {
                            System.err.println("Could not retrieve applicant info: " + e2.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error getting candidate info for ID " + a.getApplicantId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
                candidateChoices[i] = name + " - " + email + " (ID: " + a.getApplicantId() + ")";
            }
    
            // 5. Show selection dialog for final matching
            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Select the FINAL candidate to match for this job:\n" +
                    "(Only ACCEPTED applications are shown)",
                    "Final Candidate Matching",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    candidateChoices,
                    candidateChoices.length > 0 ? candidateChoices[0] : null
            );
            if (selected == null || selected.trim().isEmpty()) return;
    
            // 6. Extract the applicant ID from the selected string
            String selectedApplicantId = selected.substring(selected.lastIndexOf("(ID: ") + 5, selected.lastIndexOf(")"));
    
            // 7. Find matching application
            shared.models.Application selectedApp = acceptedApplications.stream()
                .filter(a -> a.getApplicantId().equals(selectedApplicantId.trim()))
                .findFirst().orElse(null);
    
            if (selectedApp == null) {
                JOptionPane.showMessageDialog(this, "Application not found for this candidate.");
                return;
            }
    
            // 8. Get full candidate details (CV)
            shared.interfaces.ICandidateView candidate = null;
            try {
                candidate = client.getRecruiterService().getCandidateById(selectedApplicantId.trim());
            } catch (Exception e) {
                System.err.println("Error getting candidate details: " + e.getMessage());
            }
    
            // 9. Show candidate CV and details
            StringBuilder cvDetails = new StringBuilder();
            cvDetails.append("═══════════════════════════════════════\n");
            cvDetails.append("   FINAL CANDIDATE SELECTION\n");
            cvDetails.append("═══════════════════════════════════════\n\n");
            
            if (candidate != null) {
                cvDetails.append("🆔 ID:           ").append(candidate.getId()).append("\n");
                cvDetails.append("👤 Name:         ").append(candidate.getName()).append("\n");
                cvDetails.append("📧 Email:        ").append(candidate.getEmail()).append("\n");
                cvDetails.append("📱 Phone:        ").append(candidate.getPhone()).append("\n");
                cvDetails.append("🎓 Education:    ").append(candidate.getEducation() != null ? candidate.getEducation() : "Not specified").append("\n");
                cvDetails.append("💼 Experience:   ").append(candidate.getExperience()).append(" years\n");
                cvDetails.append("🛠️  Skills:       ");
                if (candidate.getSkills() != null && !candidate.getSkills().isEmpty()) {
                    cvDetails.append(String.join(", ", candidate.getSkills()));
                } else {
                    cvDetails.append("Not specified");
                }
                cvDetails.append("\n");
                cvDetails.append("📄 Resume:       ").append(candidate.getResume() != null && !candidate.getResume().isEmpty() ? candidate.getResume() : "Not uploaded").append("\n");
            } else {
                cvDetails.append("⚠️  Could not load full candidate details\n");
                cvDetails.append("   Applicant ID: ").append(selectedApplicantId).append("\n");
            }
            
            cvDetails.append("\n═══════════════════════════════════════\n");
            cvDetails.append("Application Details:\n");
            cvDetails.append("   Application ID: ").append(selectedApp.getApplicationId()).append("\n");
            cvDetails.append("   Status:        ").append(selectedApp.getStatus()).append("\n");
            if (selectedApp.getCoverLetter() != null && !selectedApp.getCoverLetter().isEmpty()) {
                cvDetails.append("\n   Cover Letter:\n   ").append(selectedApp.getCoverLetter().replace("\n", "\n   ")).append("\n");
            }
            
            // Show CV in scrollable text area
            JTextArea cvArea = new JTextArea(cvDetails.toString(), 20, 50);
            cvArea.setEditable(false);
            cvArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(cvArea), 
                "Candidate CV - Final Matching", JOptionPane.INFORMATION_MESSAGE);
    
            // 10. Confirm final matching/selection
            JOptionPane.showMessageDialog(this,
                "✅ Candidate HIRED Successfully!\n\n" +
                "Candidate: " + (candidate != null ? candidate.getName() : selectedApplicantId) + "\n" +
                "Job ID: " + jobId + "\n\n" +
                "✔ Job closed\n" +
                "✔ Application marked as HIRED\n" +
                "✔ Saved to database",
                "Hiring Complete",
                JOptionPane.INFORMATION_MESSAGE
            );
    
            boolean success = client.getRecruiterService()
            .matchFinalCandidateToJob(jobId, selectedApplicantId);
    
    if (!success) {
        JOptionPane.showMessageDialog(this,
                "❌ Failed to save final hire in database.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }
    
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    private void viewCandidateDetails() {
        try {
            String candidateId = JOptionPane.showInputDialog(this, "Enter Candidate ID:");
            if (candidateId == null || candidateId.trim().isEmpty()) return;
            ICandidateView candidate = client.getRecruiterService().getCandidateById(candidateId.trim());
            if (candidate == null) {
                JOptionPane.showMessageDialog(this, "Candidate not found.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Candidate ID: ").append(candidate.getId()).append("\n")
              .append("Name: ").append(candidate.getName()).append("\n")
              .append("Email: ").append(candidate.getEmail()).append("\n")
              .append("Phone: ").append(candidate.getPhone()).append("\n")
              .append("Skills: ").append(String.join(", ", candidate.getSkills())).append("\n")
              .append("Experience: ").append(candidate.getExperience()).append("\n")
              .append("Education: ").append(candidate.getEducation()).append("\n")
              .append("Resume:\n").append(candidate.getResume() == null ? "N/A" : candidate.getResume());

            JTextArea area = new JTextArea(sb.toString(), 18, 50);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Candidate Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

  
    private void searchCandidatesBySkills() {
        try {
            String skills = JOptionPane.showInputDialog(this, "Enter skills (comma-separated):");
            if (skills == null || skills.trim().isEmpty()) return;
            List<ICandidateView> candidates = client.getRecruiterService().searchCandidatesBySkillsReadOnly(skills.trim());
            showCandidateList(candidates, "Candidates with Skills: " + skills);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }


    private void searchByExperienceLevel() {
        try {
            String years = JOptionPane.showInputDialog(this, "Enter minimum experience in years (as a number):");
            if (years == null || years.trim().isEmpty()) return;
            int minYears;
            try {
                minYears = Integer.parseInt(years.trim());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number of years.");
                return;
            }
            List<ICandidateView> candidates = client.getRecruiterService().searchCandidatesByMinExperience(minYears);
            showCandidateList(candidates, "Candidates with at least " + minYears + " years experience");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Helper for showing lists
    private void showCandidateList(List<ICandidateView> candidates, String title) {
        if (candidates == null || candidates.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No candidates found.", title, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (ICandidateView c : candidates) {
            sb.append(i++).append(". ID: ").append(c.getId()).append("\n")
                .append("   Name: ").append(c.getName()).append("\n")
                .append("   Skills: ").append(String.join(", ", c.getSkills())).append("\n")
                .append("   Experience: ").append(c.getExperience()).append("\n\n");
        }
        JTextArea area = new JTextArea(sb.toString(), 16, 48);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), title, JOptionPane.INFORMATION_MESSAGE);
    }
}