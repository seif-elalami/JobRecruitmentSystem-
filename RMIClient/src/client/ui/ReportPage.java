package client.ui;

import client.RMIClient;
import shared.interfaces.IReportService;
import shared.interfaces.IJobService;
import shared.models.Job;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.Collections;

/**
 * Simple GUI to generate reports (Simple, Detailed, Filtered)
 */
public class ReportPage extends JFrame {

    private final RMIClient rmiClient;
    private final IReportService reportService;
    private final IJobService jobService;

    public ReportPage(RMIClient rmiClient) throws Exception {
        this.rmiClient = rmiClient;
        this.reportService = rmiClient.getReportService();
        this.jobService = rmiClient.getJobService();

        setTitle("Generate Reports - Job Recruitment System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(920, 520));
        setResizable(true);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(41, 128, 185));
        setContentPane(main);

        // Header
        JLabel header = new JLabel("Generate Reports", SwingConstants.CENTER);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 32));
        header.setBorder(BorderFactory.createEmptyBorder(24, 12, 12, 12));
        main.add(header, BorderLayout.NORTH);

        // Center grid with three cards/buttons
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 18, 18, 18);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Simple Report button
        JButton simpleBtn = createCardButton("Simple Report", "Quick summary of jobs and counts", new Color(52, 152, 219));
        simpleBtn.setToolTipText("Generate a short summary report for all jobs (Alt+S)");
        simpleBtn.setMnemonic('S');
        simpleBtn.getAccessibleContext().setAccessibleName("Simple Report");
        simpleBtn.addActionListener((ActionEvent e) -> onSimpleReport());
        gbc.gridx = 0; gbc.gridy = 0;
        center.add(simpleBtn, gbc);

        // Detailed Report button
        JButton detailedBtn = createCardButton("Detailed Report", "Full listing of matching jobs and details", new Color(46, 204, 113));
        detailedBtn.setToolTipText("Generate a detailed report listing all job fields (Alt+D)");
        detailedBtn.setMnemonic('D');
        detailedBtn.getAccessibleContext().setAccessibleName("Detailed Report");
        detailedBtn.addActionListener((ActionEvent e) -> onDetailedReport());
        gbc.gridx = 1; gbc.gridy = 0;
        center.add(detailedBtn, gbc);

        // Filtered Report button
        JButton filteredBtn = createCardButton("Filtered Report", "Generate report by salary or location", new Color(155, 89, 182));
        filteredBtn.setToolTipText("Open a dialog to generate a report filtered by salary range or location (Alt+F)");
        filteredBtn.setMnemonic('F');
        filteredBtn.getAccessibleContext().setAccessibleName("Filtered Report");
        filteredBtn.addActionListener((ActionEvent e) -> onFilteredReport());
        gbc.gridx = 2; gbc.gridy = 0;
        center.add(filteredBtn, gbc);

        main.add(center, BorderLayout.CENTER);

        // Back button
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        JButton back = new JButton("Back");
        back.addActionListener(ev -> dispose());
        footer.add(back);
        main.add(footer, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createCardButton(String title, String subtitle, Color bg) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel t = new JLabel(title, SwingConstants.LEFT);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.add(t, BorderLayout.NORTH);

        JLabel s = new JLabel("<html><div style='width:320px;'>" + subtitle + "</div></html>", SwingConstants.LEFT);
        s.setForeground(new Color(225, 238, 245));
        s.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.add(s, BorderLayout.CENTER);

        btn.setPreferredSize(new Dimension(260, 220));
        return btn;
    }

    private void onSimpleReport() {
        try {
            java.util.List<Job> items = jobService.getAllJobs();
            String report = reportService.generateSimpleReport(items, "Simple Report");
            showReportDialog("Simple Report", report);
        } catch (RemoteException re) {
            showError(re);
        }
    }

    private void onDetailedReport() {
        try {
            java.util.List<Job> items = jobService.getAllJobs();
            String report = reportService.generateDetailedReport(items, "Detailed Report");
            showReportDialog("Detailed Report", report);
        } catch (RemoteException re) {
            showError(re);
        }
    }

    private void onFilteredReport() {
        // dialog with min/max salary and location
        JDialog dlg = new JDialog(this, "Filtered Report", true);
        dlg.setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;

        form.add(new JLabel("Min Salary:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField minField = new JTextField();
        form.add(minField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        form.add(new JLabel("Max Salary:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField maxField = new JTextField();
        form.add(maxField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        form.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField locField = new JTextField();
        form.add(locField, gbc);

        dlg.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton generate = new JButton("Generate");
        JButton cancel = new JButton("Cancel");
        actions.add(cancel);
        actions.add(generate);
        dlg.add(actions, BorderLayout.SOUTH);

        cancel.addActionListener(e -> dlg.dispose());

        generate.addActionListener(e -> {
            String loc = locField.getText().trim();
            String minS = minField.getText().trim();
            String maxS = maxField.getText().trim();

            try {
                if (!loc.isEmpty()) {
                    java.util.List<Job> byLoc = jobService.getJobsByLocation(loc);
                    String report = reportService.generateFilteredReportByLocation(byLoc, "Filtered by location: " + loc, loc);
                    dlg.dispose();
                    showReportDialog("Filtered Report (Location: " + loc + ")", report);
                    return;
                }

                if (!minS.isEmpty() && !maxS.isEmpty()) {
                    double min = Double.parseDouble(minS);
                    double max = Double.parseDouble(maxS);
                    java.util.List<Job> all = jobService.getAllJobs();
                    String report = reportService.generateFilteredReportBySalary(all, String.format("Filtered by salary %.0f - %.0f", min, max), min, max);
                    dlg.dispose();
                    showReportDialog(String.format("Filtered Report (Salary %.0f - %.0f)", min, max), report);
                    return;
                }

                JOptionPane.showMessageDialog(dlg, "Please provide a location or both min and max salary.", "Input", JOptionPane.WARNING_MESSAGE);

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dlg, "Salary must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (RemoteException re) {
                dlg.dispose();
                showError(re);
            }
        });

        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void showReportDialog(String title, String report) {
        JDialog dlg = new JDialog(this, title, true);
        dlg.setLayout(new BorderLayout());
        JTextArea area = new JTextArea(report != null ? report : "(no data)");
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(760, 420));
        dlg.add(sp, BorderLayout.CENTER);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton close = new JButton("Close");
        close.addActionListener(e -> dlg.dispose());
        p.add(close);
        dlg.add(p, BorderLayout.SOUTH);

        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Report Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    // quick test runner when running this file standalone
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                RMIClient client = new RMIClient();
                new ReportPage(client);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to open ReportPage: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
