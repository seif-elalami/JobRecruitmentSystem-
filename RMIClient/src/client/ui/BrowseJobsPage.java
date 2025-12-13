package client.ui;

import shared.interfaces.IJobService;
import shared.models.Job;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BrowseJobsPage extends JDialog {
    private final IJobService jobService;
    private final DefaultListModel<Job> model = new DefaultListModel<>();
    private final JList<Job> list = new JList<>(model);
    private final JTextArea details = new JTextArea();

    public BrowseJobsPage(Frame owner, IJobService jobService) {
        super(owner, "Browse Jobs", true);
        this.jobService = jobService;
        initUI();
        loadJobs();
    }

    private void initUI() {
        setSize(900, 620);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(28, 48, 74));
        JLabel title = new JLabel("Browse Jobs", SwingConstants.LEFT);
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
            JLabel lbl = new JLabel(value.getTitle());
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JLabel sub = new JLabel("ID: " + value.getJobId());
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
        refresh.addActionListener(e -> loadJobs());

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
        footer.add(refresh);

        shell.add(header, BorderLayout.NORTH);
        shell.add(left, BorderLayout.WEST);
        shell.add(center, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void loadJobs() {
        model.clear();
        details.setText("");
        try {
            List<Job> jobs = jobService.getAllJobs();
            for (Job job : jobs) {
                if ("OPEN".equalsIgnoreCase(job.getStatus())) {
                    model.addElement(job);
                }
            }
            if (model.isEmpty()) {
                details.setText("No open jobs available.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading jobs: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDetails(Job job) {
        if (job == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append(job.getTitle()).append("\nID: ").append(job.getJobId()).append("\n\n");
        sb.append("Company: ").append(job.getCompany()).append("\n");
        sb.append("Status: ").append(job.getStatus()).append("\n");
        sb.append("Description: \n").append(job.getDescription()).append("\n\n");
        if (job.getRequirements() != null) {
            sb.append("Requirements: ").append(String.join(", ", job.getRequirements()));
        }
        details.setText(sb.toString());
        details.setCaretPosition(0);
    }

    private JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return btn;
    }
}
