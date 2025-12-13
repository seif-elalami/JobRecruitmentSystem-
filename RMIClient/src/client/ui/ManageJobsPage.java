package client.ui;

import shared.interfaces.IJobService;
import shared.models.Job;
import shared.models.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ManageJobsPage extends JDialog {
    private final IJobService jobService;
    private final Session session;
    private JTable jobsTable;

    public ManageJobsPage(Frame owner, IJobService jobService, Session session) {
        super(owner, "Manage Job Postings", true);
        this.jobService = jobService;
        this.session = session;
        initUI();
        loadJobs();
    }

    private void initUI() {
        setSize(1100, 650);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(46, 125, 50));
        JLabel title = new JLabel("💼 Active Job Postings", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Manage all open positions posted by recruiters");
        subtitle.setForeground(new Color(200, 220, 200));
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

        String[] columns = {"Job ID", "Title", "Company", "Location", "Salary Range", "Posted Date"};
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

        JScrollPane scrollPane = new JScrollPane(jobsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(shell.getBackground());

        JLabel countLabel = new JLabel("Jobs: 0");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(Box.createHorizontalGlue());
        footer.add(countLabel);

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(76, 175, 80));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorderPainted(false);
        refreshBtn.addActionListener(e -> {
            loadJobs();
            countLabel.setText("Jobs: " + jobsTable.getRowCount());
        });
        footer.add(refreshBtn);

        JButton deleteBtn = new JButton("🗑️ Delete Selected");
        deleteBtn.setBackground(new Color(211, 47, 47));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBorderPainted(false);
        deleteBtn.addActionListener(e -> {
            int row = jobsTable.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this job posting?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ((DefaultTableModel) jobsTable.getModel()).removeRow(row);
                    JOptionPane.showMessageDialog(this, "✅ Job deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a job to delete", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        footer.add(deleteBtn);

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

    private void loadJobs() {
        try {
            DefaultTableModel model = (DefaultTableModel) jobsTable.getModel();
            model.setRowCount(0);

            List<Job> jobs = jobService.getAllJobs();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Job job : jobs) {
                String postedDate = job.getPostedDate() != null ? sdf.format(job.getPostedDate()) : "N/A";
                String salary = job.getSalary() > 0 ? "$" + String.format("%.2f", job.getSalary()) : "Negotiable";
                model.addRow(new Object[]{
                    job.getJobId() != null ? job.getJobId().substring(0, Math.min(8, job.getJobId().length())) + "..." : "N/A",
                    job.getTitle() != null ? job.getTitle() : "N/A",
                    job.getCompany() != null ? job.getCompany() : "N/A",
                    job.getLocation() != null ? job.getLocation() : "N/A",
                    salary,
                    postedDate
                });
            }

            System.out.println("✅ Loaded " + jobs.size() + " jobs");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading jobs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}