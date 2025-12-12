package client.ui;

import client.RMIClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class WelcomePage extends JFrame {

    private RMIClient rmiClient;
    private JPanel mainPanel;

    public WelcomePage(RMIClient rmiClient) {
        this.rmiClient = rmiClient;

        // Frame setup
        setTitle("Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);

        // Create main panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(41, 128, 185), 0, getHeight(),
                        new Color(52, 73, 94));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.setLayout(null);
        add(mainPanel);

        // Title Label
        JLabel titleLabel = new JLabel("Job Recruitment System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(100, 80, 700, 60);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        // Subtitle Label
        JLabel subtitleLabel = new JLabel("Find your perfect job or hire the best talent");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        subtitleLabel.setBounds(100, 150, 700, 30);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitleLabel);

        // Sign In Button
        JButton signInButton = createStyledButton("Sign In", new Color(46, 204, 113), 200, 280, 250, 60);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignInPage();
            }
        });
        mainPanel.add(signInButton);

        // Register Button
        JButton registerButton = createStyledButton("Register", new Color(231, 76, 60), 450, 280, 250, 60);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterPage();
            }
        });
        mainPanel.add(registerButton);

        // Features Panel
        JPanel featuresPanel = new JPanel();
        featuresPanel.setBackground(new Color(52, 73, 94));
        featuresPanel.setLayout(new GridLayout(1, 3, 20, 20));
        featuresPanel.setBounds(50, 400, 800, 120);

        // Feature 1
        addFeature(featuresPanel, "💼", "For Applicants", "Browse and apply to exciting job opportunities");

        // Feature 2
        addFeature(featuresPanel, "👥", "For Recruiters", "Post jobs and find the perfect candidates");

        // Feature 3
        addFeature(featuresPanel, "🔐", "Secure Platform", "Your data is protected and confidential");

        mainPanel.add(featuresPanel);

        // Footer
        JLabel footerLabel = new JLabel("© 2025 Job Recruitment System. All rights reserved.");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(149, 165, 166));
        footerLabel.setBounds(0, 550, 900, 20);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(footerLabel);

        setVisible(true);
    }

    /**
     * Create a styled button with custom appearance
     */
    private JButton createStyledButton(String text, Color bgColor, int x, int y, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw button background
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Draw text
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                g2d.drawString(getText(), textX, textY);
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    /**
     * Add feature card to the features panel
     */
    private void addFeature(JPanel parent, String icon, String title, String description) {
        JPanel featureCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background
                g2d.setColor(new Color(44, 62, 80));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Card border
                g2d.setColor(new Color(52, 152, 219));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };

        featureCard.setLayout(new BoxLayout(featureCard, BoxLayout.Y_AXIS));
        featureCard.setOpaque(false);

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        descLabel.setForeground(new Color(189, 195, 199));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        featureCard.add(Box.createVerticalStrut(10));
        featureCard.add(iconLabel);
        featureCard.add(Box.createVerticalStrut(5));
        featureCard.add(titleLabel);
        featureCard.add(Box.createVerticalStrut(5));
        featureCard.add(descLabel);
        featureCard.add(Box.createVerticalStrut(10));

        parent.add(featureCard);
    }

    /**
     * Open Sign In Page
     */
    private void openSignInPage() {
        dispose();
        new SignInPage(rmiClient);
    }

    /**
     * Open Register Page
     */
    private void openRegisterPage() {
        dispose();
        new RegisterPage(rmiClient);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    RMIClient rmiClient = new RMIClient();
                    new WelcomePage(rmiClient);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Failed to connect to server: " + e.getMessage(),
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });
    }
}
