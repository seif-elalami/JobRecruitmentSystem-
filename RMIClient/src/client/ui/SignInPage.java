package client.ui;

import client.RMIClient;
import shared.interfaces.IAuthService;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        setSize(1000, 750);
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
                GradientPaint gradient = new GradientPaint(0, 0, new Color(20, 33, 61), 
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
        closeBtn.setForeground(new Color(20, 33, 61));
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
        cardPanel.setBounds(100, 60, 800, 630);
        cardPanel.setOpaque(false);
        mainPanel.add(cardPanel);

        // Lock icon
        JLabel iconLabel = new JLabel("🔐");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        iconLabel.setBounds(375, 30, 50, 60);
        cardPanel.add(iconLabel);

        // Title
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(20, 33, 61));
        titleLabel.setBounds(50, 95, 700, 50);
        cardPanel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setBounds(50, 145, 700, 25);
        cardPanel.add(subtitleLabel);

        // Email Label
        JLabel emailLabel = new JLabel("📧 Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        emailLabel.setForeground(new Color(44, 62, 80));
        emailLabel.setBounds(50, 190, 400, 20);
        cardPanel.add(emailLabel);

        // Email Field
        emailField = createModernTextField();
        emailField.setBounds(50, 215, 700, 45);
        emailField.setText("Enter your email");
        emailField.setForeground(new Color(189, 195, 199));
        emailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Enter your email")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Enter your email");
                    emailField.setForeground(new Color(189, 195, 199));
                }
            }
        });
        cardPanel.add(emailField);

        // Password Label
        JLabel passwordLabel = new JLabel("🔒 Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(44, 62, 80));
        passwordLabel.setBounds(50, 275, 400, 20);
        cardPanel.add(passwordLabel);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(50, 300, 700, 45);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(236, 240, 241));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(236, 240, 241), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        passwordField.setCaretColor(new Color(52, 152, 219));
        cardPanel.add(passwordField);

        // Role Label
        JLabel roleLabel = new JLabel("👤 Select Role");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        roleLabel.setForeground(new Color(44, 62, 80));
        roleLabel.setBounds(50, 360, 400, 20);
        cardPanel.add(roleLabel);

        // Role ComboBox
        roleComboBox = new JComboBox<>(new String[]{"Applicant", "Recruiter", "Admin"});
        roleComboBox.setBounds(50, 385, 700, 45);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setBackground(new Color(236, 240, 241));
        roleComboBox.setForeground(Color.BLACK);
        roleComboBox.setBorder(BorderFactory.createLineBorder(new Color(236, 240, 241), 2));
        cardPanel.add(roleComboBox);

        // Sign In Button
        AnimatedButton signInButton = new AnimatedButton("Sign In", new Color(52, 152, 219), new Color(41, 128, 185));
        signInButton.setBounds(50, 460, 700, 55);
        signInButton.addActionListener(e -> handleSignIn());
        cardPanel.add(signInButton);

        // Bottom links panel
        JPanel linksPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(236, 240, 241));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        linksPanel.setLayout(null);
        linksPanel.setBounds(0, 540, 800, 90);
        linksPanel.setOpaque(false);
        cardPanel.add(linksPanel);

        // Divider
        JSeparator separator = new JSeparator();
        separator.setBounds(0, 0, 800, 1);
        linksPanel.add(separator);

        // Register Link
        JLabel registerLabel = new JLabel("Don't have an account?");
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLabel.setForeground(new Color(127, 140, 141));
        registerLabel.setBounds(50, 20, 200, 20);
        linksPanel.add(registerLabel);

        JButton registerLink = new JButton("Create one");
        registerLink.setBounds(240, 15, 100, 30);
        registerLink.setFont(new Font("Arial", Font.BOLD, 12));
        registerLink.setForeground(new Color(52, 152, 219));
        registerLink.setBackground(Color.WHITE);
        registerLink.setBorderPainted(false);
        registerLink.setContentAreaFilled(false);
        registerLink.setFocusPainted(false);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addActionListener(e -> {
            dispose();
            new RegisterPage(rmiClient);
        });
        linksPanel.add(registerLink);

        // Admin passkey shortcut
        JButton adminPasskeyBtn = new JButton("👑 Admin Passkey");
        adminPasskeyBtn.setBounds(50, 50, 350, 35);
        adminPasskeyBtn.setFont(new Font("Arial", Font.BOLD, 12));
        adminPasskeyBtn.setBackground(new Color(155, 89, 182));
        adminPasskeyBtn.setForeground(Color.WHITE);
        adminPasskeyBtn.setBorderPainted(false);
        adminPasskeyBtn.setFocusPainted(false);
        adminPasskeyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminPasskeyBtn.addActionListener(e -> {
            dispose();
            new AdminLoginPage(rmiClient);
        });
        linksPanel.add(adminPasskeyBtn);

        // Back button
        AnimatedButton backButton = new AnimatedButton("← Back", new Color(149, 165, 166), new Color(120, 144, 156));
        backButton.setBounds(50, 90, 350, 35);
        linksPanel.add(backButton);
        backButton.addActionListener(e -> {
            dispose();
            new WelcomePage(rmiClient);
        });

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
        field.setCaretColor(new Color(52, 152, 219));
        return field;
    }

    private void handleSignIn() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (email.isEmpty() || email.equals("Enter your email")) {
            JOptionPane.showMessageDialog(this, "❌ Please enter your email", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Please enter your password", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Session session = authService.login(email, password);

            if (session != null) {
                if (!session.getRole().equalsIgnoreCase(role)) {
                    JOptionPane.showMessageDialog(this,
                            "❌ Role mismatch. Your account is: " + session.getRole(),
                            "Role Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dispose();
                openRoleBasedMenu(session);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid email or password", "Auth Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRoleBasedMenu(Session session) {
        String role = session.getRole().toLowerCase();
        if (role.contains("applicant")) {
            new ApplicantMenuGUI(rmiClient, session);
        } else if (role.contains("recruiter")) {
            new RecruiterMenuGUI(rmiClient, session);
        } else if (role.contains("admin")) {
            new AdminMenuGUI(rmiClient, session);
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
                new SignInPage(rmiClient);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to connect: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
