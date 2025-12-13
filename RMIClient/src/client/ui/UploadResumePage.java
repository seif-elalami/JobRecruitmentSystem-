package client.ui;

import client.RMIClient;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UploadResumePage extends JFrame {
    private final RMIClient rmiClient;
    private final Session session;
    private final JTextArea resumeArea = new JTextArea();

    public UploadResumePage(RMIClient rmiClient, Session session) throws Exception {
        this.rmiClient = rmiClient;
        this.session = session;
        setTitle("Upload Resume - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
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
        JLabel title = new JLabel("Upload Resume", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        header.add(title, BorderLayout.WEST);

        resumeArea.setLineWrap(true);
        resumeArea.setWrapStyleWord(true);
        resumeArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resumeArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 235)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.add(new JScrollPane(resumeArea), BorderLayout.CENTER);

        JButton uploadTextBtn = primaryButton("Save Text");
        uploadTextBtn.addActionListener(e -> uploadText());

        JButton importFileBtn = secondaryButton("Import From File...");
        importFileBtn.addActionListener(e -> importFromFile());

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
        footer.add(importFileBtn);
        footer.add(uploadTextBtn);

        shell.add(header, BorderLayout.NORTH);
        shell.add(body, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void uploadText() {
        try {
            String text = resumeArea.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter resume text or import a file", "Upload Resume", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean ok = rmiClient.getApplicantService().uploadResume(session.getUserId(), text);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Resume uploaded successfully", "Upload Resume", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new ApplicantMenuGUI(rmiClient, session);
            } else {
                JOptionPane.showMessageDialog(this, "Upload failed", "Upload Resume", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importFromFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(chooser.getSelectedFile().getAbsolutePath()));
                String content = new String(bytes);
                resumeArea.setText(content);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to read file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        return btn;
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
