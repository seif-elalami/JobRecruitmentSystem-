package client.ui;

import client.RMIClient;
import shared.interfaces.IApplicantService;
import shared.models.Applicant;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;

public class ViewProfilePage extends JFrame {
    private final RMIClient rmiClient;
    private final IApplicantService applicantService;
    private final Session session;

    public ViewProfilePage(RMIClient rmiClient, Session session) throws Exception {
        this.rmiClient = rmiClient;
        this.session = session;
        this.applicantService = rmiClient.getApplicantService();

        setTitle("My Profile - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        load();
        setVisible(true);
    }

    private final JTextArea details = new JTextArea();

    private void initUI() {
        setSize(520, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(28, 48, 74));
        JLabel title = new JLabel("My Profile", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        header.add(title, BorderLayout.WEST);

        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        details.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 235)),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        body.add(new JScrollPane(details), BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        footer.setBackground(new Color(244, 247, 252));
        JButton backBtn = secondaryButton("Back to Dashboard");
        backBtn.addActionListener(e -> {
            dispose();
            try {
                new ApplicantMenuGUI(rmiClient, session);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        footer.add(backBtn);

        shell.add(header, BorderLayout.NORTH);
        shell.add(body, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);
        add(shell, BorderLayout.CENTER);
    }

    private JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(new Color(28, 48, 74));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(28, 48, 74), 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        btn.setFocusPainted(false);
        return btn;
    }

    private void load() {
        try {
            Applicant profile = applicantService.getApplicantById(session.getUserId());
            if (profile == null) {
                details.setText("Profile not found");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Email: ").append(profile.getEmail()).append("\n");
            sb.append("Phone: ").append(profile.getPhone()).append("\n");
            sb.append("Skills: ").append(profile.getSkills()).append("\n");
            sb.append("Education: ").append(profile.getEducation()).append("\n");
            sb.append("Experience: ").append(profile.getYearsExperience()).append(" years\n");
            sb.append("Resume: ").append(profile.getResume()).append("\n");
            details.setText(sb.toString());
            details.setCaretPosition(0);
        } catch (Exception ex) {
            details.setText("Error loading profile: " + ex.getMessage());
        }
    }
}