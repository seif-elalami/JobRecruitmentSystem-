package client.ui;

import shared.interfaces.IAuthService;
import shared.models.Session;
import shared.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ManageUsersPage extends JDialog {
    private final IAuthService authService;
    private final Session session;
    private JTable usersTable;

    public ManageUsersPage(Frame owner, IAuthService authService, Session session) {
        super(owner, "Manage Users", true);
        this.authService = authService;
        this.session = session;
        initUI();
        loadUsers();
    }

    private void initUI() {
        setSize(950, 650);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel shell = new JPanel(new BorderLayout(12, 12));
        shell.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        shell.setBackground(new Color(244, 247, 252));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(155, 89, 182));
        JLabel title = new JLabel("👥 System Users", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Total applicants, recruiters, and admins enrolled in the system");
        subtitle.setForeground(new Color(220, 220, 220));
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

        String[] columns = {"User ID", "Name", "Email", "Role", "Phone", "Status", "Joined"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        usersTable = new JTable(model);
        usersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usersTable.setRowHeight(28);
        usersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        usersTable.getTableHeader().setBackground(new Color(240, 242, 245));
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(shell.getBackground());

        JLabel countLabel = new JLabel("Users: 0");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(Box.createHorizontalGlue());
        footer.add(countLabel);

        JButton addUserBtn = new JButton("➕ Add User");
        addUserBtn.setBackground(new Color(76, 175, 80));
        addUserBtn.setForeground(Color.WHITE);
        addUserBtn.setBorderPainted(false);
        addUserBtn.addActionListener(e -> showAddUserDialog(countLabel));
        footer.add(addUserBtn);

        JButton deleteUserBtn = new JButton("🗑️ Delete User");
        deleteUserBtn.setBackground(new Color(211, 47, 47));
        deleteUserBtn.setForeground(Color.WHITE);
        deleteUserBtn.setBorderPainted(false);
        deleteUserBtn.addActionListener(e -> deleteSelectedUser(countLabel));
        footer.add(deleteUserBtn);

        JButton deactivateBtn = new JButton("🚫 Deactivate");
        deactivateBtn.setBackground(new Color(255, 152, 0));
        deactivateBtn.setForeground(Color.WHITE);
        deactivateBtn.setBorderPainted(false);
        deactivateBtn.addActionListener(e -> toggleActiveSelectedUser(false, countLabel));
        footer.add(deactivateBtn);

        JButton activateBtn = new JButton("✅ Activate");
        activateBtn.setBackground(new Color(76, 175, 80));
        activateBtn.setForeground(Color.WHITE);
        activateBtn.setBorderPainted(false);
        activateBtn.addActionListener(e -> toggleActiveSelectedUser(true, countLabel));
        footer.add(activateBtn);

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(76, 175, 80));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorderPainted(false);
        refreshBtn.addActionListener(e -> {
            loadUsers();
            countLabel.setText("Users: " + usersTable.getRowCount());
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

    private void loadUsers() {
        try {
            DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
            model.setRowCount(0);

            List<User> users = authService.getAllUsers();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (User user : users) {
                String createdDate = user.getCreatedAt() != null ? sdf.format(user.getCreatedAt()) : "N/A";
                model.addRow(new Object[]{
                    user.getUserId() != null ? user.getUserId().substring(0, Math.min(8, user.getUserId().length())) + "..." : "N/A",
                    user.getUsername() != null ? user.getUsername() : "N/A",
                    user.getEmail() != null ? user.getEmail() : "N/A",
                    user.getRole() != null ? user.getRole() : "N/A",
                    user.getPhone() != null ? user.getPhone() : "N/A",
                    user.isActive() ? "Active" : "Inactive",
                    createdDate
                });
            }

            System.out.println("✅ Loaded " + users.size() + " users");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void showAddUserDialog(JLabel countLabel) {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"APPLICANT", "RECRUITER", "ADMIN"});
        JTextField phoneField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.add(new JLabel("Name")); panel.add(nameField);
        panel.add(new JLabel("Email")); panel.add(emailField);
        panel.add(new JLabel("Password")); panel.add(passwordField);
        panel.add(new JLabel("Role")); panel.add(roleBox);
        panel.add(new JLabel("Phone")); panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                User user = new User();
                user.setUsername(nameField.getText().trim());
                user.setEmail(emailField.getText().trim());
                user.setPassword(new String(passwordField.getPassword()));
                user.setRole((String) roleBox.getSelectedItem());
                user.setPhone(phoneField.getText().trim());

                Session created = authService.register(user);
                if (created != null) {
                    JOptionPane.showMessageDialog(this, "✅ User created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                    countLabel.setText("Users: " + usersTable.getRowCount());
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to create user", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error creating user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void deleteSelectedUser(JLabel countLabel) {
        int row = usersTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String userIdShort = (String) usersTable.getValueAt(row, 0);
        String email = (String) usersTable.getValueAt(row, 2);

        int confirm = JOptionPane.showConfirmDialog(this, "Delete user '" + email + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {

            User user = authService.getUserByEmail(email);
            if (user == null || user.getUserId() == null) {
                JOptionPane.showMessageDialog(this, "❌ Could not resolve user ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean deleted = authService.deleteUser(user.getUserId());

            if (deleted) {
                JOptionPane.showMessageDialog(this, "🗑️ User deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
                countLabel.setText("Users: " + usersTable.getRowCount());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void toggleActiveSelectedUser(boolean active, JLabel countLabel) {
        int row = usersTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String email = (String) usersTable.getValueAt(row, 2);
        try {
            User user = authService.getUserByEmail(email);
            if (user == null || user.getUserId() == null) {
                JOptionPane.showMessageDialog(this, "❌ Could not resolve user ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean ok = authService.setUserActive(user.getUserId(), active);
            if (ok) {
                JOptionPane.showMessageDialog(this, (active ? "✅ Activated" : "🚫 Deactivated") + " user: " + email, "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
                countLabel.setText("Users: " + usersTable.getRowCount());
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to update active state", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}