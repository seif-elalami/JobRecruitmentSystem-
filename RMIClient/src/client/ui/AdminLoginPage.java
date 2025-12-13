package client.ui;

import client.RMIClient;
import shared.interfaces.IAuthService;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AdminLoginPage extends JFrame {

    private RMIClient rmiClient;
    private IAuthService authService;
    private JPasswordField passkeyField;

    public AdminLoginPage(RMIClient rmiClient) {
        this.rmiClient = rmiClient;
        this.authService = rmiClient.getAuthService();

        setTitle("Admin Login - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        // Main panel with gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(155, 89, 182),
                        getWidth(), getHeight(), new Color(108, 52, 131));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);
        add(mainPanel);

        // Close button
        JButton closeBtn = new JButton("✕");
        closeBtn.setBounds(760, 10, 30, 30);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        closeBtn.setBackground(Color.WHITE);
        closeBtn.setForeground(new Color(155, 89, 182));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> {
            dispose();
            new SignInPage(rmiClient);
        });
        mainPanel.add(closeBtn);

        // Card panel
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        cardPanel.setLayout(null);
        cardPanel.setBounds(50, 80, 700, 450);
        cardPanel.setOpaque(false);
        mainPanel.add(cardPanel);

        // Crown icon
        JLabel crownIcon = new JLabel("👑");
        crownIcon.setFont(new Font("Arial", Font.PLAIN, 60));
        crownIcon.setBounds(300, 20, 100, 70);
        cardPanel.add(crownIcon);

        // Title
        JLabel titleLabel = new JLabel("Admin Access");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(155, 89, 182));
        titleLabel.setBounds(50, 85, 600, 40);
        cardPanel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Enter your passkey for quick access");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setBounds(50, 125, 600, 20);
        cardPanel.add(subtitleLabel);

        // Passkey label
        JLabel passkeyLabel = new JLabel("🔑 Admin Passkey");
        passkeyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passkeyLabel.setForeground(new Color(44, 62, 80));
        passkeyLabel.setBounds(50, 160, 600, 20);
        cardPanel.add(passkeyLabel);

        // Passkey field
        passkeyField = new JPasswordField();
        passkeyField.setBounds(50, 185, 600, 45);
        passkeyField.setFont(new Font("Arial", Font.PLAIN, 16));
        passkeyField.setBackground(new Color(236, 240, 241));
        passkeyField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        passkeyField.setEchoChar('•');
        passkeyField.addActionListener(e -> loginWithPasskey());
        cardPanel.add(passkeyField);

        // Login button
        JButton loginBtn = new JButton("🚀 Access Admin Panel");
        loginBtn.setBounds(50, 250, 600, 45);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setBackground(new Color(155, 89, 182));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> loginWithPasskey());
        cardPanel.add(loginBtn);

        // Info text
        JLabel infoLabel = new JLabel("<html><center>⚠️ Passkey is stored securely in the database<br>" +
                "Only administrators have access to this page</center></html>");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        infoLabel.setForeground(new Color(149, 165, 166));
        infoLabel.setBounds(50, 310, 600, 50);
        cardPanel.add(infoLabel);

        // Back button
        JButton backBtn = new JButton("⬅ Back to Sign In");
        backBtn.setBounds(50, 375, 600, 40);
        backBtn.setFont(new Font("Arial", Font.BOLD, 12));
        backBtn.setBackground(new Color(189, 195, 199));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            dispose();
            new SignInPage(rmiClient);
        });
        cardPanel.add(backBtn);

        setVisible(true);
        passkeyField.requestFocus();
    }

    private void loginWithPasskey() {
        String passkey = new String(passkeyField.getPassword());

        if (passkey.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your passkey", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Use the new loginWithPasskey method for admin authentication
            Session session = authService.loginWithPasskey("admin@jobsystem.com", passkey);
            if (session != null && "Admin".equalsIgnoreCase(session.getRole())) {
                JOptionPane.showMessageDialog(this, "✅ Welcome Admin!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new AdminMenuGUI(rmiClient, session);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid passkey", "Error", JOptionPane.ERROR_MESSAGE);
                passkeyField.setText("");
                passkeyField.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            passkeyField.setText("");
            passkeyField.requestFocus();
        }
    }
}
