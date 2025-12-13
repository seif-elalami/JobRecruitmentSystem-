package client.ui;

import shared.interfaces.IApplicationService;
import shared.interfaces.IAuthService;
import shared.interfaces.IJobService;
import shared.models.Application;
import shared.models.Job;
import shared.models.Session;
import shared.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecruiterViewApplicationsPage extends JDialog {
    private final Session session;
    private final IApplicationService applicationService;
    private final IAuthService authService;
    private final IJobService jobService;
    private JTable applicationsTable;
    private List<Application> currentApplications;

    public RecruiterViewApplicationsPage(Frame owner, Session session, IApplicationService applicationService, IAuthService authService, IJobService jobService) {
        super(owner, "All Applications", true);
        this.session = session;
        this.applicationService = applicationService;
        this.authService = authService;
        this.jobService = jobService;
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

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 103, 210));
        JLabel title = new JLabel("📊 All Applications", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("View all applications received");
        subtitle.setForeground(new Color(180, 210, 255));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        header.add(titlePanel, BorderLayout.WEST);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235)));

        String[] columns = {"App ID", "Applicant Email", "Job Title", "Status", "Application Date"};
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

        // Footer
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

            currentApplications = applicationService.getAllApplications();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Application app : currentApplications) {
                try {
                    // Get applicant email
                    User applicant = authService.getUserById(app.getApplicantId());
                    String applicantEmail = applicant != null && applicant.getEmail() != null ? applicant.getEmail() : "Unknown";

                    // Get job title
                    Job job = jobService.getJobById(app.getJobId());
                    String jobTitle = job != null && job.getTitle() != null ? job.getTitle() : "Unknown";

                    String appDate = app.getApplicationDate() != null ? sdf.format(app.getApplicationDate()) : "N/A";
                    model.addRow(new Object[]{
                        app.getApplicationId() != null ? app.getApplicationId().substring(0, Math.min(8, app.getApplicationId().length())) + "..." : "N/A",
                        applicantEmail,
                        jobTitle,
                        app.getStatus() != null ? app.getStatus() : "APPLIED",
                        appDate
                    });
                } catch (Exception e) {
                    System.err.println("Error loading application data: " + e.getMessage());
                }
            }

            System.out.println("✅ Loaded " + currentApplications.size() + " applications");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading applications: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
