package client.ui;

import client.RMIClient;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;

public class ViewResumePage extends JFrame {
    private final RMIClient rmiClient;
    private final Session session;
    private final JTextArea area = new JTextArea();

    public ViewResumePage(RMIClient rmiClient, Session session) throws Exception {
        this.rmiClient = rmiClient;
        this.session = session;
        setTitle("My Resume - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        load();
        setVisible(true);
    }

    private void initUI() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(28, 48, 74));
        JLabel title = new JLabel("My Resume", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        header.add(title, BorderLayout.WEST);

        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 235)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.add(new JScrollPane(area), BorderLayout.CENTER);

        JButton backBtn = secondaryButton("Back to Dashboard");
        backBtn.addActionListener(e -> {
            dispose();
            try {
                new ApplicantMenuGUI(rmiClient, session);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        footer.setBackground(shell.getBackground());
        footer.add(backBtn);

        shell.add(header, BorderLayout.NORTH);
        shell.add(body, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void load() {
        try {
            String content = rmiClient.getApplicantService().getResume(session.getUserId());
            if (content == null || content.trim().isEmpty()) {
                area.setText("No resume uploaded yet.");
            } else {
                area.setText(content);
                area.setCaretPosition(0);
            }
        } catch (Exception ex) {
            area.setText("Error loading resume: " + ex.getMessage());
        }
    }

    private JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(new Color(28, 48, 74));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(28, 48, 74), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        btn.setFocusPainted(false);
        return btn;
    }
}