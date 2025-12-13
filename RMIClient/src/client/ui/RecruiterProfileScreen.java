package client.ui;

import client.RMIClient;
import shared.models.Session;
import shared.models.Recruiter;

import javax.swing.*;
import java.awt.*;

public class RecruiterProfileScreen extends JFrame {

    public RecruiterProfileScreen(RMIClient rmiClient, Session session) {
        setTitle("Recruiter Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton viewProfileBtn = new JButton("View My Profile");
        JButton updateProfileBtn = new JButton("Update Profile");
        JButton changePasswordBtn = new JButton("Change Password");
        JButton backBtn = new JButton("Back");

        viewProfileBtn.addActionListener(e -> viewProfile(rmiClient, session));
        updateProfileBtn.addActionListener(e -> updateRecruiter(rmiClient, session));
        changePasswordBtn.addActionListener(e -> changePassword(rmiClient, session));
        backBtn.addActionListener(e -> {
            dispose();
            new RecruiterMenuGUI(rmiClient, session); // Replace with your main menu class
        });

        mainPanel.add(viewProfileBtn);
        mainPanel.add(updateProfileBtn);
        mainPanel.add(changePasswordBtn);
        mainPanel.add(backBtn);

        add(mainPanel);
        setVisible(true);
    }

    private void viewProfile(RMIClient rmiClient, Session session) {
        try {
            Recruiter recruiter = rmiClient.getRecruiterService().getRecruiterById(session.getUserId());
            if (recruiter == null) {
                JOptionPane.showMessageDialog(this, "Profile not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Name: ").append(recruiter.getUsername()).append("\n")
              .append("Email: ").append(recruiter.getEmail()).append("\n")
              .append("Phone: ").append(recruiter.getPhone()).append("\n")
              .append("Company: ").append(recruiter.getCompany()).append("\n")
              .append("Department: ").append(recruiter.getDepartment()).append("\n")
              .append("Position: ").append(recruiter.getPosition()).append("\n")
              .append("Description: ").append(recruiter.getDescription()).append("\n");
            JOptionPane.showMessageDialog(this, sb.toString(), "My Profile", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to fetch profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecruiter(RMIClient rmiClient, Session session) {
        try {
            // Fetch current recruiter info
            Recruiter recruiter = rmiClient.getRecruiterService().getRecruiterById(session.getUserId());
            if (recruiter == null) {
                JOptionPane.showMessageDialog(this, "Profile not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextField phoneField = new JTextField(recruiter.getPhone());
            JTextField companyField = new JTextField(recruiter.getCompany());
            JTextField departmentField = new JTextField(recruiter.getDepartment());
            JTextField positionField = new JTextField(recruiter.getPosition());
            JTextField descField = new JTextField(recruiter.getDescription());

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Phone:"));
            panel.add(phoneField);
            panel.add(new JLabel("Company:"));
            panel.add(companyField);
            panel.add(new JLabel("Department:"));
            panel.add(departmentField);
            panel.add(new JLabel("Position:"));
            panel.add(positionField);
            panel.add(new JLabel("Description:"));
            panel.add(descField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Update Profile", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                recruiter.setPhone(phoneField.getText().trim());
                recruiter.setCompany(companyField.getText().trim());
                recruiter.setDepartment(departmentField.getText().trim());
                recruiter.setPosition(positionField.getText().trim());
                recruiter.setDescription(descField.getText().trim());

                boolean success = rmiClient.getRecruiterService().updateRecruiter(recruiter);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update profile.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to update profile: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changePassword(RMIClient rmiClient, Session session) {
        // You can show a dialog to enter old password, new password, confirm new password
        // On submit, call a backend method to update the password
        JOptionPane.showMessageDialog(this, "Change Password feature not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
