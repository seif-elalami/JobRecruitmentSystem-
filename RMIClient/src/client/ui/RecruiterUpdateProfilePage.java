package client.ui;

import shared.interfaces.IRecruiterService;
import shared.models.Session;
import shared.models.Recruiter;

import javax.swing.*;
import java.awt.*;

public class RecruiterUpdateProfilePage extends JDialog {
    private final Session session;
    private final IRecruiterService recruiterService;
    private JTextField phoneField;
    private JTextField companyField;
    private JTextField departmentField;

    public RecruiterUpdateProfilePage(Frame owner, Session session, IRecruiterService recruiterService) {
        super(owner, "Update Profile", true);
        this.session = session;
        this.recruiterService = recruiterService;
        initUI();
        loadProfileData();
    }

    private void initUI() {
        setSize(600, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(76, 175, 80));
        JLabel title = new JLabel("✏️ Update Profile", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Update your phone, company, and department information");
        subtitle.setForeground(new Color(200, 255, 200));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        header.add(titlePanel, BorderLayout.WEST);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Phone field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        phoneField = new JTextField(20);
        phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(phoneField, gbc);

        // Company field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel companyLabel = new JLabel("Company:");
        companyLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(companyLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        companyField = new JTextField(20);
        companyField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(companyField, gbc);

        // Department field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel departmentLabel = new JLabel("Department:");
        departmentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(departmentLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        departmentField = new JTextField(20);
        departmentField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(departmentField, gbc);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(shell.getBackground());

        JButton saveBtn = new JButton("💾 Save Changes");
        saveBtn.setBackground(new Color(76, 175, 80));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorderPainted(false);
        saveBtn.addActionListener(e -> saveProfile());
        footer.add(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(52, 152, 219));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBorderPainted(false);
        cancelBtn.addActionListener(e -> dispose());
        footer.add(cancelBtn);

        shell.add(header, BorderLayout.NORTH);
        shell.add(formPanel, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void loadProfileData() {
        SwingUtilities.invokeLater(() -> {
            try {
                Recruiter recruiter = recruiterService.getRecruiterByEmail(session.getUserEmail());
                if (recruiter != null) {
                    phoneField.setText(recruiter.getPhone() != null ? recruiter.getPhone() : "");
                    companyField.setText(recruiter.getCompany() != null ? recruiter.getCompany() : "");
                    departmentField.setText(recruiter.getDepartment() != null ? recruiter.getDepartment() : "");
                    System.out.println("✅ Profile data loaded");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void saveProfile() {
        try {
            Recruiter recruiter = recruiterService.getRecruiterByEmail(session.getUserEmail());
            if (recruiter != null) {
                recruiter.setPhone(phoneField.getText());
                recruiter.setCompany(companyField.getText());
                recruiter.setDepartment(departmentField.getText());

                boolean success = recruiterService.updateRecruiter(recruiter);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("✅ Profile updated");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to update profile", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
