package client.ui;

import client.RMIClient;
import shared.interfaces.IAuthService;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInPage extends JFrame {

    private RMIClient rmiClient;
    private IAuthService authService;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public SignInPage(RMIClient rmiClient) {
        this.rmiClient = rmiClient;
        this.authService = rmiClient.getAuthService();

        // Frame setup
        setTitle("Sign In - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(41, 128, 185), 0, getHeight(),
                        new Color(52, 73, 94));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.setLayout(null);
        add(mainPanel);

        // Title Label
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 40, 400, 40);
        mainPanel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        subtitleLabel.setBounds(50, 85, 400, 20);
        mainPanel.add(subtitleLabel);

        // Email Label
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(50, 130, 400, 20);
        mainPanel.add(emailLabel);

        // Email Field
        emailField = new JTextField();
        emailField.setBounds(50, 155, 400, 40);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBackground(new Color(236, 240, 241));
        emailField.setForeground(Color.BLACK);
        emailField.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        mainPanel.add(emailField);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, 210, 400, 20);
        mainPanel.add(passwordLabel);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(50, 235, 400, 40);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(236, 240, 241));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        mainPanel.add(passwordField);

        // Role Label
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setBounds(50, 290, 400, 20);
        mainPanel.add(roleLabel);

        // Role ComboBox
        roleComboBox = new JComboBox<>(new String[]{"Applicant", "Recruiter", "Admin"});
        roleComboBox.setBounds(50, 315, 400, 40);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setBackground(new Color(236, 240, 241));
        roleComboBox.setForeground(Color.BLACK);
        mainPanel.add(roleComboBox);

        // Sign In Button
        JButton signInButton = new JButton("Sign In");
        signInButton.setBounds(50, 390, 400, 45);
        signInButton.setFont(new Font("Arial", Font.BOLD, 16));
        signInButton.setBackground(new Color(46, 204, 113));
        signInButton.setForeground(Color.WHITE);
        signInButton.setBorderPainted(false);
        signInButton.setFocusPainted(false);
        signInButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignIn();
            }
        });

        mainPanel.add(signInButton);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(50, 450, 190, 40);
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new WelcomePage(rmiClient);
            }
        });

        mainPanel.add(backButton);

        // Register Link
        JButton registerLink = new JButton("Don't have an account? Register here");
        registerLink.setBounds(260, 450, 190, 40);
        registerLink.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLink.setBackground(new Color(52, 152, 219));
        registerLink.setForeground(Color.WHITE);
        registerLink.setBorderPainted(false);
        registerLink.setFocusPainted(false);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegisterPage(rmiClient);
            }
        });

        mainPanel.add(registerLink);

        setVisible(true);
    }

    /**
     * Handle sign in action
     * 
     * Password Verification Flow:
     * 1. GUI: User enters email and plain password
     * 2. GUI: Plain password sent to server via RMI
     * 3. Server: Fetches BCrypt hash from MongoDB
     * 4. Server: PasswordUtil.verifyPassword() calls BCrypt.checkpw()
     * 5. Server: Compares plain password against BCrypt hash
     * 6. Server: Returns Session if password matches, error otherwise
     */
    private void handleSignIn() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        // Validation
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "❌ Please enter your email address",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "❌ Please enter your password",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Show loading message
            JOptionPane.showMessageDialog(this,
                    "⏳ Authenticating...",
                    "Sign In",
                    JOptionPane.INFORMATION_MESSAGE);

            // Call authentication service
            Session session = authService.login(email, password);

            if (session != null) {
                // Verify role matches (case-insensitive comparison)
                String sessionRole = session.getRole();
                if (sessionRole == null || sessionRole.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "❌ Error: User role is missing. Please contact support.",
                            "Role Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Normalize both roles for comparison
                String normalizedSessionRole = sessionRole.toUpperCase().trim();
                String normalizedSelectedRole = role.toUpperCase().trim();

                // Map common role variations
                if (normalizedSelectedRole.equals("RECRUITER") || normalizedSelectedRole.contains("RECRUIT")) {
                    normalizedSelectedRole = "RECRUITER";
                } else if (normalizedSelectedRole.equals("APPLICANT")) {
                    normalizedSelectedRole = "APPLICANT";
                } else if (normalizedSelectedRole.equals("ADMIN") || normalizedSelectedRole.equals("ADMINISTRATOR")) {
                    normalizedSelectedRole = "ADMIN";
                }

                // Normalize session role to standard format
                if (normalizedSessionRole.contains("RECRUITER") || normalizedSessionRole.contains("RECRUIT")) {
                    normalizedSessionRole = "RECRUITER";
                } else if (normalizedSessionRole.contains("APPLICANT")) {
                    normalizedSessionRole = "APPLICANT";
                } else if (normalizedSessionRole.contains("ADMIN")) {
                    normalizedSessionRole = "ADMIN";
                }

                if (!normalizedSessionRole.equals(normalizedSelectedRole)) {
                    JOptionPane.showMessageDialog(this,
                            "❌ The selected role does not match your account role.\n" +
                            "Selected: " + role + "\n" +
                            "Your role: " + sessionRole + "\n\n" +
                            "Please select the correct role and try again.",
                            "Role Mismatch",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Open appropriate menu based on role
                dispose();
                openRoleBasedMenu(session);

            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ Invalid email or password",
                        "Authentication Failed",
                        JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error: " + e.getMessage(),
                    "Sign In Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Open menu based on user role
     */
    private void openRoleBasedMenu(Session session) {
        if (session == null || session.getRole() == null) {
            JOptionPane.showMessageDialog(null,
                    "❌ Error: Invalid session or role",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            new WelcomePage(rmiClient);
            return;
        }

        String role = session.getRole().toUpperCase().trim();

        // Normalize role to handle variations
        if (role.contains("RECRUITER") || role.contains("RECRUIT")) {
            // Open Recruiter Menu
            new RecruiterMenuGUI(rmiClient, session);
        } else if (role.contains("APPLICANT")) {
            // Open Applicant Menu
            new ApplicantMenuGUI(rmiClient, session);
        } else if (role.contains("ADMIN")) {
            // Open Admin Menu
            new AdminMenuGUI(rmiClient, session);
        } else {
            // Unknown role - show error and go back to welcome page
            JOptionPane.showMessageDialog(null,
                    "❌ Error: Unknown user role: " + session.getRole() + "\nPlease contact support.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            new WelcomePage(rmiClient);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    RMIClient rmiClient = new RMIClient();
                    new SignInPage(rmiClient);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Failed to connect to server: " + e.getMessage(),
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
