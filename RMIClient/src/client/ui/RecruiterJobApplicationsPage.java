package client.ui;

import shared.interfaces.IApplicationService;
import shared.interfaces.IJobService;
import shared.interfaces.IRecruiterService;
import shared.interfaces.IAuthService;
import shared.models.Application;
import shared.models.Job;
import shared.models.Session;
import shared.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecruiterJobApplicationsPage extends JDialog {
    private final Session session;
    private final IApplicationService applicationService;
    private final IJobService jobService;
    private final IRecruiterService recruiterService;
    private final IAuthService authService;
    private JComboBox<String> jobComboBox;
    private JTable applicationsTable;
    private List<Application> currentApplications;
    private List<Job> recruiterJobs;

    public RecruiterJobApplicationsPage(Frame owner, Session session, IApplicationService applicationService, IJobService jobService, IRecruiterService recruiterService) {
        super(owner, "Applications for Specific Job", true);
        this.session = session;
        this.applicationService = applicationService;
        this.jobService = jobService;
        this.recruiterService = recruiterService;
        this.authService = null; // Will get from context
        initUI();
        loadRecruiterJobs();
    }

    private void initUI() {
        setSize(1000, 650);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(156, 39, 176));
        JLabel title = new JLabel("🎯 Applications for Specific Job", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Select a job and view its applications");
        subtitle.setForeground(new Color(230, 200, 255));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        header.add(titlePanel, BorderLayout.WEST);

        // Filter panel
        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));
        filterPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235)));

        JLabel jobLabel = new JLabel("Select Job:");
        jobLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterPanel.add(jobLabel);

        jobComboBox = new JComboBox<>();
        jobComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jobComboBox.setPreferredSize(new Dimension(300, 25));
        jobComboBox.addActionListener(e -> loadJobApplications());
        filterPanel.add(jobComboBox);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 235)));

        String[] columns = {"App ID", "Applicant Email", "Status", "Application Date"};
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
            loadJobApplications();
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
        shell.add(filterPanel, BorderLayout.NORTH);
        shell.add(tablePanel, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        // Adjust layout
        JPanel centerAndFooter = new JPanel(new BorderLayout());
        centerAndFooter.add(filterPanel, BorderLayout.NORTH);
        centerAndFooter.add(tablePanel, BorderLayout.CENTER);

        shell.remove(filterPanel);
        shell.add(centerAndFooter, BorderLayout.CENTER);
        shell.remove(footer);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void loadRecruiterJobs() {
        try {
            recruiterJobs = jobService.getJobsByRecruiterId(session.getUserId());
            jobComboBox.removeAllItems();

            for (Job job : recruiterJobs) {
                String jobLabel = job.getTitle() + " (" + job.getStatus() + ")";
                jobComboBox.addItem(jobLabel);
            }

            if (!recruiterJobs.isEmpty()) {
                loadJobApplications();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading jobs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadJobApplications() {
        try {
            int selectedIndex = jobComboBox.getSelectedIndex();
            if (selectedIndex < 0 || selectedIndex >= recruiterJobs.size()) {
                return;
            }

            Job selectedJob = recruiterJobs.get(selectedIndex);
            DefaultTableModel model = (DefaultTableModel) applicationsTable.getModel();
            model.setRowCount(0);

            currentApplications = applicationService.getApplicationsByJobId(selectedJob.getJobId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Application app : currentApplications) {
                try {
                    String appDate = app.getApplicationDate() != null ? sdf.format(app.getApplicationDate()) : "N/A";
                    model.addRow(new Object[]{
                        app.getApplicationId() != null ? app.getApplicationId().substring(0, Math.min(8, app.getApplicationId().length())) + "..." : "N/A",
                        app.getApplicantId() != null ? app.getApplicantId() : "Unknown",
                        app.getStatus() != null ? app.getStatus() : "APPLIED",
                        appDate
                    });
                } catch (Exception e) {
                    System.err.println("Error loading application data: " + e.getMessage());
                }
            }

            System.out.println("✅ Loaded " + currentApplications.size() + " applications for job");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading applications: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
