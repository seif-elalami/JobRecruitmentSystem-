package client.ui;

import client.RMIClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class WelcomePage extends JFrame {

    private RMIClient rmiClient;
    private JPanel mainPanel;

    public WelcomePage(RMIClient rmiClient) {
        this.rmiClient = rmiClient;

        // Frame setup
        setTitle("Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 1200, 800, 0, 0));

        // Create main panel with animated gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Modern gradient background (Deep blue to purple to teal)
                GradientPaint gradient = new GradientPaint(0, 0, new Color(20, 33, 61), 
                        getWidth(), getHeight(), new Color(52, 152, 219));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add subtle overlay pattern
                g2d.setColor(new Color(255, 255, 255, 5));
                for (int i = 0; i < getWidth(); i += 50) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
            }
        };

        mainPanel.setLayout(null);
        add(mainPanel);

        // Logo/Icon area
        JLabel logoLabel = new JLabel("💼");
        logoLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        logoLabel.setBounds(550, 80, 100, 100);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(logoLabel);

        // Title Label
        JLabel titleLabel = new JLabel("Job Recruitment System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 52));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(150, 170, 900, 70);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        // Subtitle Label
        JLabel subtitleLabel = new JLabel("Find Your Perfect Job or Hire Exceptional Talent");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        subtitleLabel.setBounds(150, 245, 900, 30);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitleLabel);

        // Divider line
        JPanel dividerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(52, 152, 219));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            }
        };
        dividerPanel.setBounds(350, 290, 500, 3);
        dividerPanel.setOpaque(false);
        mainPanel.add(dividerPanel);

        // Sign In Button with animation
        AnimatedButton signInButton = new AnimatedButton("🔐 Sign In", new Color(52, 152, 219), new Color(41, 128, 185));
        signInButton.setBounds(200, 360, 250, 70);
        signInButton.addActionListener(e -> openSignInPage());
        mainPanel.add(signInButton);

        // Register Button with animation
        AnimatedButton registerButton = new AnimatedButton("✏️ Register", new Color(46, 204, 113), new Color(39, 174, 96));
        registerButton.setBounds(475, 360, 250, 70);
        registerButton.addActionListener(e -> openRegisterPage());
        mainPanel.add(registerButton);

        // Admin Login Button with animation
        AnimatedButton adminButton = new AnimatedButton("🛡️ Admin Login", new Color(231, 76, 60), new Color(192, 57, 43));
        adminButton.setBounds(750, 360, 250, 70);
        adminButton.addActionListener(e -> openAdminLoginPage());
        mainPanel.add(adminButton);

        // Feature Cards
        createFeatureCard(mainPanel, "👤", "For Applicants", "Discover exciting opportunities\nand manage your applications", 100, 520);
        createFeatureCard(mainPanel, "👔", "For Recruiters", "Post jobs and connect with\nqualified candidates", 450, 520);
        createFeatureCard(mainPanel, "🛡️", "Secure & Reliable", "Your data is protected with\nmodern security measures", 800, 520);

        // Footer
        JLabel footerLabel = new JLabel("© 2025 Job Recruitment System | Secure • Reliable • Innovative");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(149, 165, 166));
        footerLabel.setBounds(0, 760, 1200, 20);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(footerLabel);

        setVisible(true);
    }

    private void createFeatureCard(JPanel parent, String icon, String title, String description, int x, int y) {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(44, 62, 80));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(new Color(52, 152, 219));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        cardPanel.setLayout(null);
        cardPanel.setBounds(x, y, 280, 150);
        cardPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        iconLabel.setBounds(10, 15, 50, 50);
        cardPanel.add(iconLabel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(70, 15, 200, 25);
        cardPanel.add(titleLabel);

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(236, 240, 241));
        descLabel.setBounds(70, 40, 200, 100);
        cardPanel.add(descLabel);

        parent.add(cardPanel);
    }

    private static class AnimatedButton extends JButton {
        private Color baseColor;
        private Color hoverColor;
        private float scale = 1.0f;
        private boolean isHovered = false;

        public AnimatedButton(String text, Color baseColor, Color hoverColor) {
            this.baseColor = baseColor;
            this.hoverColor = hoverColor;
            setText(text);
            setFont(new Font("Arial", Font.BOLD, 18));
            setForeground(Color.WHITE);
            setBackground(baseColor);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Mouse listener for hover animation
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    animateButton(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    animateButton(false);
                }
            });
        }

        private void animateButton(boolean hover) {
            if (hover) {
                scale = 1.05f;
                setBackground(hoverColor);
            } else {
                scale = 1.0f;
                setBackground(baseColor);
            }
            repaint();
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

            // Draw button background with rounded corners
            g2d.setColor(isHovered ? hoverColor : baseColor);
            g2d.fillRoundRect(x, y, scaledWidth, scaledHeight, 15, 15);

            // Draw border
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x, y, scaledWidth, scaledHeight, 15, 15);

            // Draw shadow effect when hovered
            if (isHovered) {
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(x + 2, y + 2, scaledWidth - 4, scaledHeight - 4, 15, 15);
            }

            // Draw text
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (width - fm.stringWidth(getText())) / 2;
            int textY = ((height - fm.getHeight()) / 2) + fm.getAscent();

            g2d.setColor(Color.WHITE);
            g2d.setFont(getFont());
            g2d.drawString(getText(), textX, textY);
        }
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

    /**
     * Open Admin Login Page
     */
    private void openAdminLoginPage() {
        dispose();
        try {
            new AdminLoginPage(rmiClient);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening admin login: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
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
        });
    }
}
