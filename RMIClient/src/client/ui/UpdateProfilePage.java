package client.ui;

import client.RMIClient;
import shared.interfaces.IApplicantService;
import shared.models.Applicant;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateProfilePage extends JFrame {
    private final RMIClient rmiClient;
    private final IApplicantService applicantService;
    private final Session session;

    private final JTextField phoneField = new JTextField();
    private final JTextField skillsField = new JTextField();
    private final JTextField educationField = new JTextField();
    private final JTextField expField = new JTextField();
    private final JTextArea resumeArea = new JTextArea();

    public UpdateProfilePage(RMIClient rmiClient, Session session) throws Exception {
        this.rmiClient = rmiClient;
        this.session = session;
        this.applicantService = rmiClient.getApplicantService();
        
        setTitle("Update Profile - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        load();
        setVisible(true);
    }

    private void initUI() {
        setSize(720, 660);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(16, 16));
        shell.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        shell.setBackground(new Color(245, 248, 255));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(23, 43, 77));
        JLabel title = new JLabel("Update Profile ✨", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Make it yours — phone, skills, and more", SwingConstants.LEFT);
        subtitle.setForeground(new Color(200, 215, 235));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JPanel titleWrap = new JPanel(new GridLayout(0,1));
        titleWrap.setOpaque(false);
        titleWrap.add(title);
        titleWrap.add(subtitle);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        header.add(titleWrap, BorderLayout.WEST);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 235)),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        addFormField(form, "Phone (11 digits)", phoneField, "e.g., 01234567890");
        addFormField(form, "Skills (comma-separated)", skillsField, "e.g., Java, MongoDB, RMI");
        addFormField(form, "Education", educationField, "e.g., BSc in Computer Science");
        addFormField(form, "Years of Experience", expField, "e.g., 3");

        JLabel resumeLbl = styledLabel("Resume Summary");
        resumeLbl.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        resumeArea.setLineWrap(true);
        resumeArea.setWrapStyleWord(true);
        resumeArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resumeArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 235)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        resumeArea.setToolTipText("Write a short summary of your experience, goals, and skills");
        JScrollPane resumeScroll = new JScrollPane(resumeArea);
        resumeScroll.setBorder(BorderFactory.createEmptyBorder());
        form.add(resumeLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(resumeScroll);

        JButton save = primaryButton("Save Changes");
        save.setToolTipText("Save your profile updates");
        save.addActionListener(e -> save());
        addHoverEffect(save);

        JButton backBtn = secondaryButton("Back to Dashboard");
        backBtn.addActionListener(e -> {
            dispose();
            try {
                new ApplicantMenuGUI(rmiClient, session);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        footer.setBackground(shell.getBackground());
        footer.add(backBtn);
        footer.add(save);

        shell.add(header, BorderLayout.NORTH);
        shell.add(form, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void load() {
        try {
            Applicant profile = applicantService.getApplicantById(session.getUserId());
            if (profile == null) {
                profile = new Applicant();
                profile.setId(session.getUserId());
                profile.setEmail(session.getUserEmail());
            }
            phoneField.setText(profile.getPhone() != null ? profile.getPhone() : "");
            skillsField.setText(profile.getSkills() != null ? profile.getSkills() : "");
            educationField.setText(profile.getEducation() != null ? profile.getEducation() : "");
            expField.setText(Integer.toString(profile.getYearsExperience()));
            resumeArea.setText(profile.getResume() != null ? profile.getResume() : "");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void save() {
        try {
            Applicant profile = applicantService.getApplicantById(session.getUserId());
            if (profile == null) {
                profile = new Applicant();
                profile.setId(session.getUserId());
                profile.setEmail(session.getUserEmail());
            }
            if (profile.getId() == null || profile.getId().isEmpty()) profile.setId(session.getUserId());
            if (profile.getEmail() == null || profile.getEmail().isEmpty()) profile.setEmail(session.getUserEmail());
            if (!phoneField.getText().trim().isEmpty()) profile.setPhone(phoneField.getText().trim());
            String skills = skillsField.getText();
            if (skills != null) {
                String[] parts = skills.split("\s*,\s*");
                List<String> skillList = new ArrayList<>();
                for (String p : parts) if (!p.isEmpty()) skillList.add(p);
                profile.setSkills(skillList);
            }
            profile.setEducation(educationField.getText().trim());
            try {
                int years = Integer.parseInt(expField.getText().trim());
                profile.setExperience(years);
            } catch (NumberFormatException ignored) { }
            profile.setResume(resumeArea.getText().trim());

            boolean ok = applicantService.updateApplicant(profile);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Profile updated", "Update Profile", JOptionPane.INFORMATION_MESSAGE);
                // Navigate back to Applicant dashboard instead of closing app
                dispose();
                try {
                    new ApplicantMenuGUI(rmiClient, session);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error returning: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Update failed", "Update Profile", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(40, 50, 65));
        return lbl;
    }

    private JTextField styledField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 235)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        return field;
    }

    private JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        return btn;
    }

    private JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(new Color(28, 48, 74));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(28, 48, 74), 1),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        btn.setFocusPainted(false);
        return btn;
    }

    private void addFormField(JPanel form, String label, JTextField field, String placeholder) {
        JLabel lbl = styledLabel(label);
        lbl.setIcon(UIManager.getIcon("FileView.fileIcon"));
        field.setToolTipText(placeholder);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        form.add(lbl);
        form.add(Box.createVerticalStrut(6));
        form.add(styledField(field));
        form.add(Box.createVerticalStrut(12));
    }

    private void addHoverEffect(JButton btn) {
        Color base = btn.getBackground();
        Color hover = new Color(41, 128, 185);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(base); }
        });
    }
}
