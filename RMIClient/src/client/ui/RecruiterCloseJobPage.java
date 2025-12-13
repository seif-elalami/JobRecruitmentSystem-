package client.ui;

import shared.interfaces.IJobService;
import shared.models.Job;
import shared.models.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecruiterCloseJobPage extends JDialog {
    private final Session session;
    private final IJobService jobService;
    private JTable jobsTable;
    private List<Job> currentJobs;

    public RecruiterCloseJobPage(Frame owner, Session session, IJobService jobService) {
        super(owner, "Close Job Posting", true);
        this.session = session;
        this.jobService = jobService;
        initUI();
        loadMyJobs();
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
        header.setBackground(new Color(211, 47, 47));
        JLabel title = new JLabel("🔒 Close Job Posting", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Select a job to close and stop receiving applications");
        subtitle.setForeground(new Color(255, 200, 200));
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

        String[] columns = {"Job ID", "Title", "Company", "Location", "Status", "Posted Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jobsTable = new JTable(model);
        jobsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jobsTable.setRowHeight(28);
        jobsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        jobsTable.getTableHeader().setBackground(new Color(240, 242, 245));
        jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(jobsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(shell.getBackground());

        JLabel countLabel = new JLabel("Open Jobs: 0");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(Box.createHorizontalGlue());
        footer.add(countLabel);

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(76, 175, 80));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorderPainted(false);
        refreshBtn.addActionListener(e -> {
            loadMyJobs();
            countLabel.setText("Open Jobs: " + jobsTable.getRowCount());
        });
        footer.add(refreshBtn);

        JButton closeJobBtn = new JButton("🔒 Close Selected Job");
        closeJobBtn.setBackground(new Color(211, 47, 47));
        closeJobBtn.setForeground(Color.WHITE);
        closeJobBtn.setBorderPainted(false);
        closeJobBtn.addActionListener(e -> closeSelectedJob());
        footer.add(closeJobBtn);

        JButton cancelBtn = new JButton("Close");
        cancelBtn.setBackground(new Color(52, 152, 219));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBorderPainted(false);
        cancelBtn.addActionListener(e -> dispose());
        footer.add(cancelBtn);

        shell.add(header, BorderLayout.NORTH);
        shell.add(tablePanel, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void loadMyJobs() {
        try {
            DefaultTableModel model = (DefaultTableModel) jobsTable.getModel();
            model.setRowCount(0);

            currentJobs = jobService.getJobsByRecruiterId(session.getUserId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            int openCount = 0;
            for (Job job : currentJobs) {
                // Only show open jobs
                if (job.getStatus() != null && job.getStatus().equalsIgnoreCase("OPEN")) {
                    String postedDate = job.getPostedDate() != null ? sdf.format(job.getPostedDate()) : "N/A";
                    model.addRow(new Object[]{
                        job.getJobId() != null ? job.getJobId().substring(0, Math.min(8, job.getJobId().length())) + "..." : "N/A",
                        job.getTitle() != null ? job.getTitle() : "N/A",
                        job.getCompany() != null ? job.getCompany() : "N/A",
                        job.getLocation() != null ? job.getLocation() : "N/A",
                        job.getStatus() != null ? job.getStatus() : "N/A",
                        postedDate
                    });
                    openCount++;
                }
            }

            System.out.println("✅ Loaded " + openCount + " open jobs");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading jobs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void closeSelectedJob() {
        int row = jobsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "❌ Please select a job to close", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Job selectedJob = null;
            String selectedJobId = (String) jobsTable.getValueAt(row, 0);
            
            // Find the full job object
            for (Job job : currentJobs) {
                if (job.getJobId().startsWith(selectedJobId.replace("...", ""))) {
                    selectedJob = job;
                    break;
                }
            }

            if (selectedJob != null) {
                boolean success = jobService.closeJob(selectedJob.getJobId());
                if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Job posting closed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadMyJobs();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to close job", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error closing job: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
