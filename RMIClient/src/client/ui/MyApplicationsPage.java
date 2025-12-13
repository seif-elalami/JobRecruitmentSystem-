package client.ui;

import client.RMIClient;
import shared.interfaces.IApplicationService;
import shared.interfaces.IJobService;
import shared.models.Application;
import shared.models.ApplicationState;
import shared.models.Job;
import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyApplicationsPage extends JFrame {
    private final RMIClient rmiClient;
    private final IApplicationService applicationService;
    private final IJobService jobService;
    private final Session session;
    private final DefaultListModel<Application> model = new DefaultListModel<>();
    private final JList<Application> list = new JList<>(model);
    private final JTextArea details = new JTextArea();
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public MyApplicationsPage(RMIClient rmiClient, Session session) throws Exception {
        this.rmiClient = rmiClient;
        this.session = session;
        this.applicationService = rmiClient.getApplicationService();
        this.jobService = rmiClient.getJobService();
        
        setTitle("My Applications - Job Recruitment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        load();
        setVisible(true);
    }

    private void initUI() {
        setSize(900, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(28, 48, 74));
        JLabel title = new JLabel("My Applications", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        header.add(title, BorderLayout.WEST);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(64);
        list.setCellRenderer((lst, value, index, isSelected, cellHasFocus) -> {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            panel.setBackground(isSelected ? new Color(224, 236, 249) : Color.WHITE);
            JLabel lbl = new JLabel(value.getJobId());
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JLabel sub = new JLabel("Status: " + value.getStatus());
            sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            sub.setForeground(new Color(100, 110, 120));
            panel.add(lbl, BorderLayout.NORTH);
            panel.add(sub, BorderLayout.SOUTH);
            return panel;
        });
        list.addListSelectionListener(e -> showDetails(list.getSelectedValue()));

        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        details.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton refresh = primaryButton("Refresh");
        refresh.addActionListener(e -> load());
        
        JButton backBtn = secondaryButton("Back to Dashboard");
        backBtn.addActionListener(e -> {
            dispose();
            new ApplicantMenuGUI(rmiClient, session);
        });

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(Color.WHITE);
        left.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 235)),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        left.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 235)),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        center.add(new JScrollPane(details), BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(shell.getBackground());
        footer.add(backBtn);
        footer.add(refresh);

        shell.add(header, BorderLayout.NORTH);
        shell.add(left, BorderLayout.WEST);
        shell.add(center, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void load() {
        model.clear();
        details.setText("");
        try {
            List<Application> apps = applicationService.getApplicationsByApplicantId(session.getUserId());
            for (Application app : apps) model.addElement(app);
            if (model.isEmpty()) {
                details.setText("No applications submitted.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading applications: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDetails(Application app) {
        if (app == null) return;
        try {
            Job job = jobService.getJobById(app.getJobId());
            ApplicationState state = app.getCurrentState();
            
            StringBuilder sb = new StringBuilder();
            sb.append("Job: ").append(job != null ? job.getTitle() : "Unknown").append(" (ID: ").append(app.getJobId()).append(")\n\n");
            
            // Show State Pattern information
            sb.append("═══ APPLICATION STATE (State Pattern) ═══\n");
            sb.append("Current State: ").append(state.getStateName()).append("\n");
            sb.append("Is Final State: ").append(state.isFinalState() ? "Yes" : "No").append("\n");
            sb.append("Is Accepted: ").append(state.isAccepted() ? "Yes" : "No").append("\n");
            sb.append("Is Rejected: ").append(state.isRejected() ? "Yes" : "No").append("\n\n");
            
            sb.append("═══ APPLICATION DETAILS ═══\n");
            sb.append("Applied On: ").append(app.getApplicationDate() != null ? df.format(app.getApplicationDate()) : "N/A").append("\n");
            sb.append("Application ID: ").append(app.getApplicationId()).append("\n\n");
            
            sb.append("Cover Letter:\n").append(app.getCoverLetter() != null ? app.getCoverLetter() : "No cover letter provided");
            
            details.setText(sb.toString());
            details.setCaretPosition(0);
        } catch (Exception ex) {
            details.setText("Error loading details: " + ex.getMessage());
        }
    }

    private JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return btn;
    }
    
    private JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(149, 165, 166));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return btn;
    }
}
