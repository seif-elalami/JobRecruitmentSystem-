package client.ui;

import shared.interfaces.IApplicationService;
import shared.models.Application;
import shared.models.ApplicationState;
import shared.models.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ManageApplicationsPage extends JDialog {
    private final IApplicationService applicationService;
    private final Session session;
    private JTable applicationsTable;
    private List<Application> currentApplications; // Store loaded applications for updates

    public ManageApplicationsPage(Frame owner, IApplicationService applicationService, Session session) {
        super(owner, "Manage Job Applications", true);
        this.applicationService = applicationService;
        this.session = session;
        initUI();
        loadApplications();
    }

    private void initUI() {
        setSize(1100, 650);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 103, 210));
        JLabel title = new JLabel("📋 Application Review Center", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Review and manage all applicant submissions");
        subtitle.setForeground(new Color(180, 210, 255));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        header.add(titlePanel, BorderLayout.WEST);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235)));

        String[] columns = {"App ID", "Applicant ID", "Job ID", "Status", "Application Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        applicationsTable = new JTable(model);
        applicationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        applicationsTable.setRowHeight(28);
        applicationsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        applicationsTable.getTableHeader().setBackground(new Color(240, 242, 245));

        JScrollPane scrollPane = new JScrollPane(applicationsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(shell.getBackground());

        JLabel countLabel = new JLabel("Applications: 0");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(Box.createHorizontalGlue());
        footer.add(countLabel);

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(76, 175, 80));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorderPainted(false);
        refreshBtn.addActionListener(e -> {
            loadApplications();
            countLabel.setText("Applications: " + applicationsTable.getRowCount());
        });
        footer.add(refreshBtn);

        JButton approveBtn = new JButton("✅ Accept");
        approveBtn.setBackground(new Color(76, 175, 80));
        approveBtn.setForeground(Color.WHITE);
        approveBtn.setBorderPainted(false);
        approveBtn.addActionListener(e -> {
            int row = applicationsTable.getSelectedRow();
            if (row >= 0) {
                try {
                    Application selectedApp = currentApplications.get(row);
                    boolean success = applicationService.updateApplicationStatus(selectedApp.getApplicationId(), "ACCEPTED");
                    if (success) {
                        applicationsTable.setValueAt("ACCEPTED", row, 3);
                        JOptionPane.showMessageDialog(this, "✅ Application approved and database updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadApplications(); // Refresh to show updated data
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Failed to update application status", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Error updating application: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an application", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        footer.add(approveBtn);

        JButton rejectBtn = new JButton("❌ Reject");
        rejectBtn.setBackground(new Color(211, 47, 47));
        rejectBtn.setForeground(Color.WHITE);
        rejectBtn.setBorderPainted(false);
        rejectBtn.addActionListener(e -> {
            int row = applicationsTable.getSelectedRow();
            if (row >= 0) {
                try {
                    Application selectedApp = currentApplications.get(row);
                    boolean success = applicationService.updateApplicationStatus(selectedApp.getApplicationId(), "REJECTED");
                    if (success) {
                        applicationsTable.setValueAt("REJECTED", row, 3);
                        JOptionPane.showMessageDialog(this, "❌ Application rejected and database updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadApplications(); // Refresh to show updated data
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Failed to update application status", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Error updating application: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an application", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        footer.add(rejectBtn);

        JButton pendingBtn = new JButton("⏳ Set Pending");
        pendingBtn.setBackground(new Color(100, 100, 100));
        pendingBtn.setForeground(Color.WHITE);
        pendingBtn.setBorderPainted(false);
        pendingBtn.addActionListener(e -> updateSelectedStatus("APPLIED"));
        footer.add(pendingBtn);

        JButton reviewBtn = new JButton("🔍 Under Review");
        reviewBtn.setBackground(new Color(52, 152, 219));
        reviewBtn.setForeground(Color.WHITE);
        reviewBtn.setBorderPainted(false);
        reviewBtn.addActionListener(e -> updateSelectedStatus("UNDER_REVIEW"));
        footer.add(reviewBtn);

        JButton detailsBtn = new JButton("📄 View Details");
        detailsBtn.setBackground(new Color(108, 92, 231));
        detailsBtn.setForeground(Color.WHITE);
        detailsBtn.setBorderPainted(false);
        detailsBtn.addActionListener(e -> {
            int row = applicationsTable.getSelectedRow();
            if (row >= 0) {
                Application app = currentApplications.get(row);
                showApplicationDetails(app);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an application", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        footer.add(detailsBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(52, 152, 219));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorderPainted(false);
        closeBtn.addActionListener(e -> dispose());
        footer.add(closeBtn);

        shell.add(header, BorderLayout.NORTH);
        shell.add(tablePanel, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void loadApplications() {
        try {
            DefaultTableModel model = (DefaultTableModel) applicationsTable.getModel();
            model.setRowCount(0);

            currentApplications = applicationService.getAllApplications(); // Store for updates
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Application app : currentApplications) {
                String appDate = app.getApplicationDate() != null ? sdf.format(app.getApplicationDate()) : "N/A";
                model.addRow(new Object[]{
                    app.getApplicationId() != null ? app.getApplicationId().substring(0, Math.min(8, app.getApplicationId().length())) + "..." : "N/A",
                    app.getApplicantId() != null ? app.getApplicantId().substring(0, Math.min(8, app.getApplicantId().length())) + "..." : "N/A",
                    app.getJobId() != null ? app.getJobId().substring(0, Math.min(8, app.getJobId().length())) + "..." : "N/A",
                    app.getStatus() != null ? app.getStatus() : "APPLIED",
                    appDate
                });
            }

            System.out.println("✅ Loaded " + currentApplications.size() + " applications");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading applications: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateSelectedStatus(String status) {
        int row = applicationsTable.getSelectedRow();
        if (row >= 0) {
            try {
                Application selectedApp = currentApplications.get(row);
                boolean success = applicationService.updateApplicationStatus(selectedApp.getApplicationId(), status);
                if (success) {
                    applicationsTable.setValueAt(status, row, 3);
                    JOptionPane.showMessageDialog(this, "✅ Status updated to " + status, "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadApplications();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to update status", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an application", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showApplicationDetails(Application app) {
        StringBuilder sb = new StringBuilder();
        sb.append("═══ APPLICATION DETAILS ═══\n\n");
        sb.append("Application ID: ").append(app.getApplicationId()).append("\n");
        sb.append("Applicant ID: ").append(app.getApplicantId()).append("\n");
        sb.append("Job ID: ").append(app.getJobId()).append("\n");
        sb.append("Status: ").append(app.getStatus()).append("\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (app.getApplicationDate() != null) {
            sb.append("Application Date: ").append(sdf.format(app.getApplicationDate())).append("\n");
        }
        sb.append("\nCover Letter:\n").append(app.getCoverLetter() != null ? app.getCoverLetter() : "N/A").append("\n\n");

        ApplicationState state = app.getCurrentState();
        if (state != null) {
            sb.append("═══ APPLICATION STATE (State Pattern) ═══\n");
            sb.append("Current State: ").append(state.getStateName()).append("\n");
            sb.append("Is Final State: ").append(state.isFinalState() ? "Yes" : "No").append("\n");
            sb.append("Is Accepted: ").append(state.isAccepted() ? "Yes" : "No").append("\n");
            sb.append("Is Rejected: ").append(state.isRejected() ? "Yes" : "No").append("\n");
        } else {
            sb.append("\n⚠️ State Pattern: Not initialized\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Application Details - State Pattern", JOptionPane.INFORMATION_MESSAGE);
    }
}