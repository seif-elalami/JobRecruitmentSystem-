package client.ui;

import shared.interfaces.IJobService;
import shared.interfaces.IRecruiterService;
import shared.models.Job;
import shared.models.Session;
import shared.models.User;
import shared.interfaces.IAuthService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RecruiterCreateJobPage extends JDialog {
    private final Session session;
    private final IJobService jobService;
    private final IRecruiterService recruiterService;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField companyField;
    private JTextField locationField;
    private JTextField salaryField;
    private JTextArea requirementsArea;

    public RecruiterCreateJobPage(Frame owner, Session session, IJobService jobService, IRecruiterService recruiterService) {
        super(owner, "Create Job Posting", true);
        this.session = session;
        this.jobService = jobService;
        this.recruiterService = recruiterService;
        initUI();
    }

    private void initUI() {
        setSize(700, 750);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 152, 219));
        JLabel title = new JLabel("📝 Create Job Posting", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Post a new job opportunity");
        subtitle.setForeground(new Color(180, 220, 255));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        header.add(titlePanel, BorderLayout.WEST);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel titleLabel = new JLabel("Job Title:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        titleField = new JTextField(30);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        formPanel.add(descLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(4, 30);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        formPanel.add(descScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel companyLabel = new JLabel("Company:");
        companyLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        formPanel.add(companyLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        companyField = new JTextField(30);
        companyField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        formPanel.add(companyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        formPanel.add(locationLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        locationField = new JTextField(30);
        locationField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        formPanel.add(locationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel salaryLabel = new JLabel("Salary ($):");
        salaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        formPanel.add(salaryLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        salaryField = new JTextField(30);
        salaryField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        formPanel.add(salaryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        JLabel reqLabel = new JLabel("Requirements:");
        reqLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        formPanel.add(reqLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        requirementsArea = new JTextArea(4, 30);
        requirementsArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        requirementsArea.setLineWrap(true);
        requirementsArea.setWrapStyleWord(true);
        JScrollPane reqScroll = new JScrollPane(requirementsArea);
        formPanel.add(reqScroll, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(shell.getBackground());

        JButton postBtn = new JButton("📤 Post Job");
        postBtn.setBackground(new Color(76, 175, 80));
        postBtn.setForeground(Color.WHITE);
        postBtn.setBorderPainted(false);
        postBtn.addActionListener(e -> createJob());
        footer.add(postBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(52, 152, 219));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBorderPainted(false);
        cancelBtn.addActionListener(e -> dispose());
        footer.add(cancelBtn);

        shell.add(header, BorderLayout.NORTH);
        shell.add(scrollPane, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void createJob() {

        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Please enter a job title", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Please enter a job description", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double salary = 0;
            if (!salaryField.getText().isEmpty()) {
                salary = Double.parseDouble(salaryField.getText());
            }

            List<String> requirements = new ArrayList<>();
            String[] reqs = requirementsArea.getText().split("\n");
            for (String req : reqs) {
                if (!req.trim().isEmpty()) {
                    requirements.add(req.trim());
                }
            }

            Job job = new Job(
                titleField.getText(),
                descriptionArea.getText(),
                requirements,
                session.getUserId()
            );
            job.setCompany(companyField.getText());
            job.setLocation(locationField.getText());
            job.setSalary(salary);
            job.setStatus("OPEN");

            String jobId = jobService.createJob(job);
            if (jobId != null && !jobId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "✅ Job posting created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to create job posting", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Invalid salary amount", "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error creating job: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}