package client.ui;

import shared.interfaces.IRecruiterService;
import shared.models.Session;
import shared.models.Recruiter;

import javax.swing.*;
import java.awt.*;

public class RecruiterViewProfilePage extends JDialog {
    private final Session session;
    private final IRecruiterService recruiterService;
    private JLabel emailLabel;
    private JLabel usernameLabel;
    private JLabel phoneLabel;
    private JLabel companyLabel;
    private JLabel departmentLabel;
    private JLabel positionLabel;
    private JLabel createdAtLabel;
    private JLabel statusLabel;

    public RecruiterViewProfilePage(Frame owner, Session session, IRecruiterService recruiterService) {
        super(owner, "View My Profile", true);
        this.session = session;
        this.recruiterService = recruiterService;
        initUI();
        loadProfile();
    }

    private void initUI() {
        setSize(600, 550);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        JLabel title = new JLabel("👤 My Profile", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("View your recruiter profile information");
        subtitle.setForeground(new Color(180, 210, 255));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        header.add(titlePanel, BorderLayout.WEST);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel emailTitleLabel = new JLabel("Email:");
        emailTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailTitleLabel.setForeground(new Color(100, 100, 100));
        contentPanel.add(emailTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        emailLabel = new JLabel("Loading...");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        emailLabel.setForeground(new Color(50, 50, 50));
        contentPanel.add(emailLabel, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel usernameTitleLabel = new JLabel("Username:");
        usernameTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameTitleLabel.setForeground(new Color(100, 100, 100));
        contentPanel.add(usernameTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        usernameLabel = new JLabel("Loading...");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usernameLabel.setForeground(new Color(50, 50, 50));
        contentPanel.add(usernameLabel, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel phoneTitleLabel = new JLabel("Phone:");
        phoneTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        phoneTitleLabel.setForeground(new Color(100, 100, 100));
        contentPanel.add(phoneTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        phoneLabel = new JLabel("Loading...");
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        phoneLabel.setForeground(new Color(50, 50, 50));
        contentPanel.add(phoneLabel, gbc);

        // Company
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel companyTitleLabel = new JLabel("Company:");
        companyTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        companyTitleLabel.setForeground(new Color(100, 100, 100));
        contentPanel.add(companyTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        companyLabel = new JLabel("Loading...");
        companyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        companyLabel.setForeground(new Color(50, 50, 50));
        contentPanel.add(companyLabel, gbc);

        // Department
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        JLabel departmentTitleLabel = new JLabel("Department:");
        departmentTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        departmentTitleLabel.setForeground(new Color(100, 100, 100));
        contentPanel.add(departmentTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        departmentLabel = new JLabel("Loading...");
        departmentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        departmentLabel.setForeground(new Color(50, 50, 50));
        contentPanel.add(departmentLabel, gbc);

        // Position
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        JLabel positionTitleLabel = new JLabel("Position:");
        positionTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        positionTitleLabel.setForeground(new Color(100, 100, 100));
        contentPanel.add(positionTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        positionLabel = new JLabel("Loading...");
        positionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        positionLabel.setForeground(new Color(50, 50, 50));
        contentPanel.add(positionLabel, gbc);

        // Member Since
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        JLabel createdAtTitleLabel = new JLabel("Member Since:");
        createdAtTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        createdAtTitleLabel.setForeground(new Color(100, 100, 100));
        contentPanel.add(createdAtTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        createdAtLabel = new JLabel("Loading...");
        createdAtLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        createdAtLabel.setForeground(new Color(50, 50, 50));
        contentPanel.add(createdAtLabel, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        JLabel statusTitleLabel = new JLabel("Account Status:");
        statusTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusTitleLabel.setForeground(new Color(100, 100, 100));
        contentPanel.add(statusTitleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        statusLabel = new JLabel("Loading...");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(50, 50, 50));
        contentPanel.add(statusLabel, gbc);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235)));

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(shell.getBackground());
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(52, 152, 219));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorderPainted(false);
        closeBtn.addActionListener(e -> dispose());
        footer.add(closeBtn);

        shell.add(header, BorderLayout.NORTH);
        shell.add(scrollPane, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void loadProfile() {
        SwingUtilities.invokeLater(() -> {
            try {
                Recruiter recruiter = recruiterService.getRecruiterByEmail(session.getUserEmail());
                if (recruiter != null) {
                    emailLabel.setText(recruiter.getEmail() != null ? recruiter.getEmail() : "N/A");
                    usernameLabel.setText(recruiter.getUsername() != null ? recruiter.getUsername() : "N/A");
                    phoneLabel.setText(recruiter.getPhone() != null ? recruiter.getPhone() : "N/A");
                    companyLabel.setText(recruiter.getCompany() != null ? recruiter.getCompany() : "N/A");
                    departmentLabel.setText(recruiter.getDepartment() != null ? recruiter.getDepartment() : "N/A");
                    positionLabel.setText(recruiter.getPosition() != null ? recruiter.getPosition() : "N/A");
                    createdAtLabel.setText(recruiter.getCreatedAt() != null ? recruiter.getCreatedAt().toString() : "N/A");
                    statusLabel.setText(recruiter.isActive() ? "Active" : "Inactive");
                    System.out.println("✅ Profile loaded successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Recruiter profile not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error loading profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }
}
