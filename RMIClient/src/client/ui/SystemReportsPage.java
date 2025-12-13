package client.ui;

import shared.models.Session;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemReportsPage extends JDialog {
    private final Session session;

    public SystemReportsPage(Frame owner, Session session) {
        super(owner, "System Reports", true);
        this.session = session;
        initUI();
    }

    private void initUI() {
        setSize(800, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(255, 152, 0));
        JLabel title = new JLabel("📊 System Reports", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        header.add(title, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 235)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        // Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = sdf.format(new Date());

        addReportSection(content, "📈 System Statistics", new String[]{
            "Total Users: 127",
            "Active Applicants: 95",
            "Registered Recruiters: 28",
            "System Admins: 1"
        });

        addReportSection(content, "💼 Job Postings", new String[]{
            "Total Jobs Posted: 45",
            "Active Listings: 38",
            "Closed Positions: 7",
            "Average Applications per Job: 12"
        });

        addReportSection(content, "📋 Application Analytics", new String[]{
            "Total Applications: 540",
            "Pending Review: 120",
            "Approved: 280",
            "Rejected: 140"
        });

        addReportSection(content, "🔐 System Activity", new String[]{
            "Report Generated: " + currentDate,
            "Last System Backup: 2024-12-13 03:00:00",
            "Database Size: 8.4 MB",
            "System Uptime: 99.8%"
        });

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(shell.getBackground());

        JButton exportBtn = new JButton("📥 Export PDF");
        exportBtn.setBackground(new Color(76, 175, 80));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setBorderPainted(false);
        exportBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Report export feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE));
        footer.add(exportBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(52, 152, 219));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorderPainted(false);
        closeBtn.addActionListener(e -> dispose());
        footer.add(closeBtn);

        shell.add(header, BorderLayout.NORTH);
        shell.add(scrollPane, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }

    private void addReportSection(JPanel parent, String title, String[] items) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(245, 248, 255));
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(40, 50, 65));
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(8));

        for (String item : items) {
            JLabel itemLabel = new JLabel("  • " + item);
            itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            itemLabel.setForeground(new Color(80, 90, 100));
            section.add(itemLabel);
        }

        parent.add(section);
        parent.add(Box.createVerticalStrut(12));
    }
}
