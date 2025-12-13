package client.ui;

import shared.interfaces.IAuthService;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;

public class RecruiterChangePasswordPage extends JDialog {
    private final Session session;
    private final IAuthService authService;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public RecruiterChangePasswordPage(Frame owner, Session session, IAuthService authService) {
        super(owner, "Change Password", true);
        this.session = session;
        this.authService = authService;
        initUI();
    }

    private void initUI() {
        setSize(500, 400);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(211, 47, 47));
        JLabel title = new JLabel("🔐 Change Password", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Update your password securely");
        subtitle.setForeground(new Color(255, 200, 200));
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

        // Old password field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel oldPasswordLabel = new JLabel("Current Password:");
        oldPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(oldPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        oldPasswordField = new JPasswordField(20);
        oldPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(oldPasswordField, gbc);

        // New password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(newPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(newPasswordField, gbc);

        // Confirm password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(confirmPasswordField, gbc);

        // Info panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        JLabel infoLabel = new JLabel("<html>Password must be at least 6 characters long</html>");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(new Color(150, 150, 150));
        formPanel.add(infoLabel, gbc);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(shell.getBackground());

        JButton updateBtn = new JButton("🔑 Update Password");
        updateBtn.setBackground(new Color(211, 47, 47));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBorderPainted(false);
        updateBtn.addActionListener(e -> changePassword());
        footer.add(updateBtn);

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

    private void changePassword() {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validation
        if (oldPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Please enter your current password", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Please enter a new password", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "❌ Password must be at least 6 characters long", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "❌ Passwords do not match", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean success = authService.changePassword(session.getUserEmail(), oldPassword, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Incorrect current password or update failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error changing password: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
