package client.ui;

import client.RMIClient;
import shared.interfaces.IAuthService;
import shared.models.User;
import shared.models.Session;
import shared.utils.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPage extends JFrame {

    private RMIClient rmiClient;
    private IAuthService authService;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField phoneField;
    private JComboBox<String> roleComboBox;
    private JTextArea termsArea;

    public RegisterPage(RMIClient rmiClient) {
        this.rmiClient = rmiClient;
        this.authService = rmiClient.getAuthService();

        // Frame setup
        setTitle("Register - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create scroll pane for main content
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

        // Create scrollable panel
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainPanel.setPreferredSize(new Dimension(550, 850));

        add(scrollPane);

        // Title Label
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 20, 450, 40);
        mainPanel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Join our platform today");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        subtitleLabel.setBounds(50, 65, 450, 20);
        mainPanel.add(subtitleLabel);

        int yPos = 110;
        int fieldHeight = 40;
        int labelHeight = 20;
        int spacing = 10;
        int fieldWidth = 450;

        // Name Label
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(50, yPos, fieldWidth, labelHeight);
        mainPanel.add(nameLabel);
        yPos += labelHeight + 5;

        // Name Field
        nameField = createStyledTextField();
        nameField.setBounds(50, yPos, fieldWidth, fieldHeight);
        mainPanel.add(nameField);
        yPos += fieldHeight + spacing;

        // Email Label
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(50, yPos, fieldWidth, labelHeight);
        mainPanel.add(emailLabel);
        yPos += labelHeight + 5;

        // Email Field
        emailField = createStyledTextField();
        emailField.setBounds(50, yPos, fieldWidth, fieldHeight);
        mainPanel.add(emailField);
        yPos += fieldHeight + spacing;

        // Phone Label
        JLabel phoneLabel = new JLabel("Phone Number (01234567890):");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        phoneLabel.setForeground(Color.WHITE);
        phoneLabel.setBounds(50, yPos, fieldWidth, labelHeight);
        mainPanel.add(phoneLabel);
        yPos += labelHeight + 5;

        // Phone Field
        phoneField = createStyledTextField();
        phoneField.setBounds(50, yPos, fieldWidth, fieldHeight);
        mainPanel.add(phoneField);
        yPos += fieldHeight + spacing;

        // Role Label
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setBounds(50, yPos, fieldWidth, labelHeight);
        mainPanel.add(roleLabel);
        yPos += labelHeight + 5;

        // Role ComboBox
        roleComboBox = new JComboBox<>(new String[]{"Applicant", "Recruiter"});
        roleComboBox.setBounds(50, yPos, fieldWidth, fieldHeight);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setBackground(new Color(236, 240, 241));
        roleComboBox.setForeground(Color.BLACK);
        mainPanel.add(roleComboBox);
        yPos += fieldHeight + spacing;

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, yPos, fieldWidth, labelHeight);
        mainPanel.add(passwordLabel);
        yPos += labelHeight + 5;

        // Password Field
        passwordField = createStyledPasswordField();
        passwordField.setBounds(50, yPos, fieldWidth, fieldHeight);
        mainPanel.add(passwordField);
        yPos += fieldHeight + spacing;

        // Confirm Password Label
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setBounds(50, yPos, fieldWidth, labelHeight);
        mainPanel.add(confirmLabel);
        yPos += labelHeight + 5;

        // Confirm Password Field
        confirmPasswordField = createStyledPasswordField();
        confirmPasswordField.setBounds(50, yPos, fieldWidth, fieldHeight);
        mainPanel.add(confirmPasswordField);
        yPos += fieldHeight + spacing + 10;

        // Terms and Conditions
        JLabel termsLabel = new JLabel("Terms & Conditions:");
        termsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        termsLabel.setForeground(Color.WHITE);
        termsLabel.setBounds(50, yPos, fieldWidth, labelHeight);
        mainPanel.add(termsLabel);
        yPos += labelHeight + 5;

        // Terms Text Area
        termsArea = new JTextArea(
                "By registering, you agree to:\n" +
                "• Our Terms of Service\n" +
                "• Privacy Policy\n" +
                "• Responsible use of the platform\n" +
                "• All content must be truthful and accurate");
        termsArea.setEditable(false);
        termsArea.setLineWrap(true);
        termsArea.setWrapStyleWord(true);
        termsArea.setFont(new Font("Arial", Font.PLAIN, 11));
        termsArea.setBackground(new Color(52, 73, 94));
        termsArea.setForeground(new Color(236, 240, 241));
        termsArea.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 1));
        termsArea.setBounds(50, yPos, fieldWidth, 80);
        mainPanel.add(termsArea);
        yPos += 90;

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(50, yPos, fieldWidth, 45);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });

        mainPanel.add(registerButton);
        yPos += 50;

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(50, yPos, 217, 40);
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

        // Sign In Link
        JButton signInLink = new JButton("Already have an account? Sign In");
        signInLink.setBounds(283, yPos, 217, 40);
        signInLink.setFont(new Font("Arial", Font.PLAIN, 12));
        signInLink.setBackground(new Color(52, 152, 219));
        signInLink.setForeground(Color.WHITE);
        signInLink.setBorderPainted(false);
        signInLink.setFocusPainted(false);
        signInLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        signInLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SignInPage(rmiClient);
            }
        });

        mainPanel.add(signInLink);

        setVisible(true);
    }

    /**
     * Create styled text field
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(new Color(236, 240, 241));
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        return field;
    }

    /**
     * Create styled password field
     */
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(new Color(236, 240, 241));
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        return field;
    }

    /**
     * Handle registration action
     *
     * Password Flow:
     * 1. GUI: User enters plain password in password field
     * 2. GUI: Plain password sent to server via RMI (should use HTTPS in production)
     * 3. Server: AuthServiceImpl.register() receives plain password
     * 4. Server: PasswordUtil.hashPassword() hashes with BCrypt (auto-generates salt)
     * 5. Database: Only BCrypt hash is stored (never plain password)
     *
     * Login Verification:
     * 1. GUI: User enters plain password
     * 2. Server: AuthServiceImpl.login() receives plain password
     * 3. Server: PasswordUtil.verifyPassword() uses BCrypt.checkpw() to verify
     * 4. Server: Compares plain password against stored BCrypt hash
     */
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        // Validation
        if (name.isEmpty()) {
            showError("Please enter your full name");
            return;
        }

        if (email.isEmpty()) {
            showError("Please enter your email address");
            return;
        }

        if (!email.contains("@")) {
            showError("Please enter a valid email address");
            return;
        }

        if (phone.isEmpty()) {
            showError("Please enter your phone number");
            return;
        }

        if (!ValidationUtil.validatePhone(phone)) {
            showError("Invalid phone number. Must start with 0 and be exactly 11 digits.\nExample: 01234567890");
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter a password");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters long");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            confirmPasswordField.setText("");
            return;
        }

        try {
            // Show loading message
            JOptionPane.showMessageDialog(this,
                    "⏳ Creating account...",
                    "Registration",
                    JOptionPane.INFORMATION_MESSAGE);

            // Normalize role to uppercase for consistency
            String normalizedRole = role.toUpperCase().trim();
            if (normalizedRole.equals("RECRUITER") || normalizedRole.contains("RECRUIT")) {
                normalizedRole = "RECRUITER";
            } else if (normalizedRole.equals("APPLICANT") || normalizedRole.contains("APPLICANT")) {
                normalizedRole = "APPLICANT";
            } else if (normalizedRole.equals("ADMIN") || normalizedRole.contains("ADMIN")) {
                normalizedRole = "ADMIN";
            }

            // Create user object
            User newUser = new User(name, password, email, normalizedRole);
            newUser.setPhone(phone);

            // Call registration service
            Session registered = authService.register(newUser);

            if (registered != null) {
                JOptionPane.showMessageDialog(this,
                        "✅ Registration successful!\n\nYour account has been created.\nPlease sign in now.",
                        "Registration Complete",
                        JOptionPane.INFORMATION_MESSAGE);

                // Open sign in page
                dispose();
                new SignInPage(rmiClient);

            } else {
                showError("Registration failed. Please try again or contact support.");
            }

        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                "❌ " + message,
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    RMIClient rmiClient = new RMIClient();
                    new RegisterPage(rmiClient);
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
