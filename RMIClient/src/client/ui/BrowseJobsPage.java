package client.ui;

import client.RMIClient;
import shared.interfaces.IJobService;
import shared.models.Job;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BrowseJobsPage extends JFrame {
    private final RMIClient rmiClient;
    private final Session session;
    private final IJobService jobService;
    private final DefaultListModel<Job> model = new DefaultListModel<>();
    private final JList<Job> list = new JList<>(model);
    private final JTextArea details = new JTextArea();

    public BrowseJobsPage(RMIClient rmiClient, Session session) throws Exception {
        this.rmiClient = rmiClient;
        this.session = session;
        this.jobService = rmiClient.getJobService();
        
        setTitle("Browse Jobs - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        loadJobs();
        setVisible(true);
    }

    private void initUI() {
        setSize(900, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(28, 48, 74));
        JLabel title = new JLabel("Browse Jobs", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        header.add(title, BorderLayout.WEST);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(64);
        list.setCellRenderer((lst, value, index, isSelected, cellHasFocus) -> {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            panel.setBackground(isSelected ? new Color(224, 236, 249) : Color.WHITE);
            JLabel lbl = new JLabel(value.getTitle());
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JLabel sub = new JLabel("ID: " + value.getJobId());
            sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            sub.setForeground(new Color(100, 110, 120));
            panel.add(lbl, BorderLayout.NORTH);
            panel.add(sub, BorderLayout.SOUTH);
            return panel;
        });
        list.addListSelectionListener(e -> showDetails(list.getSelectedValue()));

        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        details.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton refresh = primaryButton("Refresh");
        refresh.addActionListener(e -> loadJobs());
        
        JButton applyBtn = primaryButton("Apply for Job");
        applyBtn.addActionListener(e -> applyForJob());
        
        JButton backBtn = secondaryButton("Back to Dashboard");
        backBtn.addActionListener(e -> {
            dispose();
            new ApplicantMenuGUI(rmiClient, session);
        });

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(Color.WHITE);
        left.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 235)),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        left.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 235)),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        center.add(new JScrollPane(details), BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(shell.getBackground());
        footer.add(backBtn);
        footer.add(refresh);
        footer.add(applyBtn);

        shell.add(header, BorderLayout.NORTH);
        shell.add(left, BorderLayout.WEST);
        shell.add(center, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void loadJobs() {
        model.clear();
        details.setText("");
        try {
            List<Job> jobs = jobService.getAllJobs();
            for (Job job : jobs) {
                if ("OPEN".equalsIgnoreCase(job.getStatus())) {
                    model.addElement(job);
                }
            }
            if (model.isEmpty()) {
                details.setText("No open jobs available.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading jobs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDetails(Job job) {
        if (job == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append(job.getTitle()).append("\nID: ").append(job.getJobId()).append("\n\n");
        sb.append("Company: ").append(job.getCompany()).append("\n");
        sb.append("Status: ").append(job.getStatus()).append("\n");
        sb.append("Description: \n").append(job.getDescription()).append("\n\n");
        if (job.getRequirements() != null) {
            sb.append("Requirements: ").append(String.join(", ", job.getRequirements()));
        }
        details.setText(sb.toString());
        details.setCaretPosition(0);
    }

    private void applyForJob() {
        Job selectedJob = list.getSelectedValue();
        if (selectedJob == null) {
            JOptionPane.showMessageDialog(this, "Please select a job first!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String coverLetter = JOptionPane.showInputDialog(this, 
            "Enter your cover letter for:\n" + selectedJob.getTitle(), 
            "Apply for Job", 
            JOptionPane.QUESTION_MESSAGE);
            
        if (coverLetter != null && !coverLetter.trim().isEmpty()) {
            try {
                rmiClient.getApplicationService().CreateApplication(
                    new shared.models.Application(selectedJob.getJobId(), session.getUserId(), coverLetter)
                );
                JOptionPane.showMessageDialog(this, 
                    "Application submitted successfully!\nStatus: APPLIED (Pending Review)", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error submitting application: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return btn;
    }
    
    private JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(149, 165, 166));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return btn;
    }
}
