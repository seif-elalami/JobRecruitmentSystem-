package client.ui;

import client.RMIClient;
import shared.interfaces.IAuthService;
import shared.models.Session;
import shared.models.User;
import shared.utils.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPage extends JFrame {

    private RMIClient rmiClient;
    private IAuthService authService;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;

    public RegisterPage(RMIClient rmiClient) {
        this.rmiClient = rmiClient;
        this.authService = rmiClient.getAuthService();

        // Frame setup
        setTitle("Create Account - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        // Main panel with modern gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(155, 89, 182), 
                        getWidth(), getHeight(), new Color(52, 152, 219));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Subtle overlay pattern
                g2d.setColor(new Color(255, 255, 255, 5));
                for (int i = 0; i < getWidth(); i += 50) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
            }
        };
        mainPanel.setLayout(null);
        add(mainPanel);

        // Close button
        JButton closeBtn = new JButton("✕");
        closeBtn.setBounds(960, 10, 30, 30);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        closeBtn.setBackground(Color.WHITE);
        closeBtn.setForeground(new Color(155, 89, 182));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));
        mainPanel.add(closeBtn);

        // Card panel (white container)
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.setColor(new Color(236, 240, 241));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            }
        };
        cardPanel.setLayout(null);
        cardPanel.setBounds(50, 40, 900, 820);
        cardPanel.setOpaque(false);
        mainPanel.add(cardPanel);

        // Pencil icon
        JLabel iconLabel = new JLabel("✏️");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        iconLabel.setBounds(425, 15, 50, 50);
        cardPanel.add(iconLabel);

        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(155, 89, 182));
        titleLabel.setBounds(30, 65, 840, 40);
        cardPanel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Join our job recruitment platform");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setBounds(30, 105, 840, 20);
        cardPanel.add(subtitleLabel);

        int yPos = 140;
        int spacing = 80;

        // Full Name
        JLabel nameLabel = new JLabel("👤 Full Name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setForeground(new Color(44, 62, 80));
        nameLabel.setBounds(30, yPos, 400, 20);
        cardPanel.add(nameLabel);

        nameField = createModernTextField();
        nameField.setBounds(30, yPos + 25, 820, 40);
        nameField.setText("Enter your full name");
        nameField.setForeground(new Color(189, 195, 199));
        nameField.addFocusListener(createPlaceholderFocusListener(nameField, "Enter your full name"));
        cardPanel.add(nameField);
        yPos += spacing;

        // Email
        JLabel emailLabel = new JLabel("📧 Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        emailLabel.setForeground(new Color(44, 62, 80));
        emailLabel.setBounds(30, yPos, 400, 20);
        cardPanel.add(emailLabel);

        emailField = createModernTextField();
        emailField.setBounds(30, yPos + 25, 820, 40);
        emailField.setText("Enter your email");
        emailField.setForeground(new Color(189, 195, 199));
        emailField.addFocusListener(createPlaceholderFocusListener(emailField, "Enter your email"));
        cardPanel.add(emailField);
        yPos += spacing;

        // Phone
        JLabel phoneLabel = new JLabel("📱 Phone Number");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 12));
        phoneLabel.setForeground(new Color(44, 62, 80));
        phoneLabel.setBounds(30, yPos, 400, 20);
        cardPanel.add(phoneLabel);

        JLabel phoneHint = new JLabel("Format: 01234567890 (11 digits starting with 0)");
        phoneHint.setFont(new Font("Arial", Font.ITALIC, 10));
        phoneHint.setForeground(new Color(189, 195, 199));
        phoneHint.setBounds(30, yPos + 20, 400, 12);
        cardPanel.add(phoneHint);

        phoneField = createModernTextField();
        phoneField.setBounds(30, yPos + 33, 820, 40);
        phoneField.setText("Enter your phone number");
        phoneField.setForeground(new Color(189, 195, 199));
        phoneField.addFocusListener(createPlaceholderFocusListener(phoneField, "Enter your phone number"));
        cardPanel.add(phoneField);
        yPos += spacing;

        // Password
        JLabel passwordLabel = new JLabel("🔒 Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(44, 62, 80));
        passwordLabel.setBounds(30, yPos, 400, 20);
        cardPanel.add(passwordLabel);

        JLabel passHint = new JLabel("Minimum 6 characters");
        passHint.setFont(new Font("Arial", Font.ITALIC, 10));
        passHint.setForeground(new Color(189, 195, 199));
        passHint.setBounds(30, yPos + 20, 400, 12);
        cardPanel.add(passHint);

        passwordField = new JPasswordField();
        passwordField.setBounds(30, yPos + 33, 820, 40);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(236, 240, 241));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(236, 240, 241), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        passwordField.setCaretColor(new Color(155, 89, 182));
        cardPanel.add(passwordField);
        yPos += spacing;

        // Confirm Password
        JLabel confirmLabel = new JLabel("✓ Confirm Password");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 12));
        confirmLabel.setForeground(new Color(44, 62, 80));
        confirmLabel.setBounds(30, yPos, 400, 20);
        cardPanel.add(confirmLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(30, yPos + 25, 820, 40);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setBackground(new Color(236, 240, 241));
        confirmPasswordField.setForeground(Color.BLACK);
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(236, 240, 241), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        confirmPasswordField.setCaretColor(new Color(155, 89, 182));
        cardPanel.add(confirmPasswordField);
        yPos += spacing - 10;

        // Role
        JLabel roleLabel = new JLabel("👥 Select Role");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        roleLabel.setForeground(new Color(44, 62, 80));
        roleLabel.setBounds(30, yPos, 400, 20);
        cardPanel.add(roleLabel);

        roleComboBox = new JComboBox<>(new String[]{"Applicant", "Recruiter"});
        roleComboBox.setBounds(30, yPos + 25, 820, 40);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setBackground(new Color(236, 240, 241));
        roleComboBox.setForeground(Color.BLACK);
        roleComboBox.setBorder(BorderFactory.createLineBorder(new Color(236, 240, 241), 2));
        cardPanel.add(roleComboBox);

        // Register Button
        AnimatedButton registerButton = new AnimatedButton("Create Account", new Color(155, 89, 182), new Color(142, 68, 173));
        registerButton.setBounds(30, 725, 820, 50);
        registerButton.addActionListener(e -> handleRegister());
        cardPanel.add(registerButton);

        // Back button
        AnimatedButton backButton = new AnimatedButton("← Back", new Color(149, 165, 166), new Color(120, 144, 156));
        backButton.setBounds(30, 785, 390, 30);
        backButton.addActionListener(e -> {
            dispose();
            new SignInPage(rmiClient);
        });
        cardPanel.add(backButton);

        setVisible(true);
    }

    private JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(new Color(236, 240, 241));
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(236, 240, 241), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        field.setCaretColor(new Color(155, 89, 182));
        return field;
    }

    private FocusAdapter createPlaceholderFocusListener(JTextField field, String placeholder) {
        return new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(189, 195, 199));
                }
            }
        };
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (name.isEmpty() || name.equals("Enter your full name")) {
            JOptionPane.showMessageDialog(this, "❌ Please enter your full name", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtil.isValidName(name)) {
            JOptionPane.showMessageDialog(this, "❌ Name must be 2-100 characters", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (email.isEmpty() || email.equals("Enter your email")) {
            JOptionPane.showMessageDialog(this, "❌ Please enter your email", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "❌ Invalid email format", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (phone.isEmpty() || phone.equals("Enter your phone number")) {
            JOptionPane.showMessageDialog(this, "❌ Please enter your phone number", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtil.validatePhone(phone)) {
            JOptionPane.showMessageDialog(this, "❌ Invalid phone format. Use: 01234567890", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Please enter a password", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "❌ Password must be at least 6 characters", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "❌ Passwords do not match", "Validation Error", JOptionPane.WARNING_MESSAGE);
            confirmPasswordField.setText("");
            return;
        }

        try {
            User user = new User(name, password, email, role);
            user.setPhone(phone);
            Session session = authService.register(user);

            if (session != null) {
                JOptionPane.showMessageDialog(this, "✅ Registration successful! Welcome " + name + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                
                // Route based on role
                String userRole = session.getRole().toLowerCase();
                if (userRole.contains("applicant")) {
                    new ApplicantMenuGUI(rmiClient, session);
                } else if (userRole.contains("recruiter")) {
                    new RecruiterMenuGUI(rmiClient, session);
                } else if (userRole.contains("admin")) {
                    new AdminMenuGUI(rmiClient, session);
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ Registration failed. Email may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Animated Button Class
    private static class AnimatedButton extends JButton {
        private Color baseColor;
        private Color hoverColor;
        private float scale = 1.0f;
        private boolean isHovered = false;

        public AnimatedButton(String text, Color baseColor, Color hoverColor) {
            this.baseColor = baseColor;
            this.hoverColor = hoverColor;
            setText(text);
            setFont(new Font("Arial", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setBackground(baseColor);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    scale = 1.03f;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    scale = 1.0f;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int x = (int) ((width - width * scale) / 2);
            int y = (int) ((height - height * scale) / 2);
            int scaledWidth = (int) (width * scale);
            int scaledHeight = (int) (height * scale);

            g2d.setColor(isHovered ? hoverColor : baseColor);
            g2d.fillRoundRect(x, y, scaledWidth, scaledHeight, 12, 12);

            g2d.setColor(new Color(255, 255, 255, isHovered ? 150 : 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x, y, scaledWidth, scaledHeight, 12, 12);

            if (isHovered) {
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(x + 2, y + 2, scaledWidth - 4, scaledHeight - 4, 12, 12);
            }

            FontMetrics fm = g2d.getFontMetrics();
            int textX = (width - fm.stringWidth(getText())) / 2;
            int textY = ((height - fm.getHeight()) / 2) + fm.getAscent();

            g2d.setColor(Color.WHITE);
            g2d.setFont(getFont());
            g2d.drawString(getText(), textX, textY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                RMIClient rmiClient = new RMIClient();
                new RegisterPage(rmiClient);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to connect: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
