package client.ui;

import shared.interfaces.IApplicationService;
import shared.interfaces.IJobService;
import shared.models.Application;
import shared.models.Job;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;

public class ApplyJobPage extends JDialog {
    private final IJobService jobService;
    private final IApplicationService applicationService;
    private final Session session;

    private final JTextField jobIdField = new JTextField();
    private final JTextArea coverLetterArea = new JTextArea();

    public ApplyJobPage(Frame owner, IJobService jobService, IApplicationService applicationService, Session session) {
        super(owner, "Apply to Job", true);
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.session = session;
        initUI();
    }

    private void initUI() {
        setSize(640, 520);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(28, 48, 74));
        JLabel title = new JLabel("Apply to Job", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        header.add(title, BorderLayout.WEST);

        JPanel formCard = new JPanel(new GridLayout(0, 1, 10, 10));
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 235)),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        formCard.add(styledLabel("Job ID:"));
        formCard.add(styledField(jobIdField));
        formCard.add(styledLabel("Cover Letter:"));
        coverLetterArea.setLineWrap(true);
        coverLetterArea.setWrapStyleWord(true);
        coverLetterArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane coverScroll = new JScrollPane(coverLetterArea);
        formCard.add(coverScroll);

        JButton submit = primaryButton("Submit Application");
        submit.addActionListener(e -> submit());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(shell.getBackground());
        footer.add(submit);

        shell.add(header, BorderLayout.NORTH);
        shell.add(formCard, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void submit() {
        String jobId = jobIdField.getText().trim();
        if (jobId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Job ID required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Job job = jobService.getJobById(jobId);
            if (job == null) {
                JOptionPane.showMessageDialog(this, "Job not found", "Apply", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!"OPEN".equalsIgnoreCase(job.getStatus())) {
                JOptionPane.showMessageDialog(this, "Job is not open", "Apply", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String cover = coverLetterArea.getText();
            Application app = new Application(jobId, session.getUserId(), cover);
            String appId = applicationService.SubmitApplication(app);
            JOptionPane.showMessageDialog(this, "Application submitted. ID: " + appId, "Apply", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Apply", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(60, 70, 80));
        return lbl;
    }

    private JTextField styledField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 235)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return field;
    }

    private JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return btn;
    }
}
