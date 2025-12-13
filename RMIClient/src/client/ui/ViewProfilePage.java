package client.ui;

import shared.interfaces.IApplicantService;
import shared.models.Applicant;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;

public class ViewProfilePage extends JDialog {
    private final IApplicantService applicantService;
    private final Session session;

    public ViewProfilePage(Frame owner, IApplicantService applicantService, Session session) {
        super(owner, "My Profile", true);
        this.applicantService = applicantService;
        this.session = session;
        initUI();
        load();
    }

    private final JTextArea details = new JTextArea();

    private void initUI() {
        setSize(520, 420);
        setLocationRelativeTo(getOwner());
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

        shell.add(header, BorderLayout.NORTH);
        shell.add(body, BorderLayout.CENTER);
        add(shell, BorderLayout.CENTER);
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
