package shared.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    private String adminId;
    private String name;
    private String email;
    private List<String> permissions;
    private String accessLevel;  // "SUPER_ADMIN", "ADMIN", "MODERATOR"
    private Date assignedDate;
    private boolean canModifyUsers;
    private boolean canGenerateReports;
    private boolean canManageJobs;
    private boolean canManageApplications;
    private String department;
    private int actionsPerformed;

    public Admin() {
        this.permissions = new ArrayList<>();
        this.assignedDate = new Date();
        this.actionsPerformed = 0;
    }

    public Admin(String name, String email, String accessLevel) {
        this();
        this.name = name;
        this.email = email;
        this.accessLevel = accessLevel;
        initializeDefaultPermissions(accessLevel);
    }

    private void initializeDefaultPermissions(String accessLevel) {
        if ("SUPER_ADMIN".equals(accessLevel)) {
            this.canModifyUsers = true;
            this.canGenerateReports = true;
            this.canManageJobs = true;
            this.canManageApplications = true;
            this.permissions.add("ALL");
        } else if ("ADMIN".equals(accessLevel)) {
            this.canModifyUsers = true;
            this.canGenerateReports = true;
            this.canManageJobs = true;
            this.canManageApplications = true;
        } else if ("MODERATOR".equals(accessLevel)) {
            this.canGenerateReports = true;
            this.canManageJobs = false;
            this.canManageApplications = true;
        }
    }

    public String generateReport(String reportType, Date startDate, Date endDate) {
        if (!canGenerateReports) {
            return " Access denied: Insufficient permissions to generate reports";
        }

        actionsPerformed++;
        System.out.println("📊 Generating " + reportType + " report...");
        System.out.println("   Period: " + startDate + " to " + endDate);

        String reportId = "RPT-" + System.currentTimeMillis();
        System.out.println("✅ Report generated: " + reportId);

        return reportId;
    }

    public boolean manageUsers(String action, String userId) {
        if (!canModifyUsers) {
            System.out.println(" Access denied: Insufficient permissions to manage users");
            return false;
        }

        actionsPerformed++;
        System.out.println("👤 Managing user: " + userId);
        System.out.println("   Action: " + action);

        switch (action.toUpperCase()) {
            case "ACTIVATE":
            case "DEACTIVATE":
            case "DELETE":
            case "RESET_PASSWORD":
            case "CHANGE_ROLE":
                System.out.println("✅ User " + action + " successful");
                return true;
            default:
                System.out.println(" Invalid action: " + action);
                return false;
        }
    }

    public boolean manageJobs(String jobId, String action) {
        if (!canManageJobs) {
            System.out.println(" Access denied: Insufficient permissions to manage jobs");
            return false;
        }

        actionsPerformed++;
        System.out.println("💼 Managing job: " + jobId);
        System.out.println("   Action: " + action);

        return true;
    }

    public boolean manageApplications(String applicationId, String action) {
        if (!canManageApplications) {
            System.out.println(" Access denied: Insufficient permissions to manage applications");
            return false;
        }

        actionsPerformed++;
        System.out.println(" Managing application: " + applicationId);
        System.out.println("   Action: " + action);

        return true;
    }

    public void addPermission(String permission) {
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
            System.out.println(" Permission added: " + permission);
        }
    }

    public void removePermission(String permission) {
        if (this.permissions.remove(permission)) {
            System.out.println(" Permission removed: " + permission);
        }
    }

    public boolean hasPermission(String permission) {
        return this.permissions.contains(permission) || this.permissions.contains("ALL");
    }

    public List<String> viewSystemLogs(Date startDate, Date endDate) {
        if (!canGenerateReports) {
            System.out.println(" Access denied: Insufficient permissions to view logs");
            return new ArrayList<>();
        }

        System.out.println(" Fetching system logs from " + startDate + " to " + endDate);
        actionsPerformed++;

        return new ArrayList<>();
    }

    public void auditUserActivity(String userId) {
        if (!canModifyUsers) {
            System.out.println(" Access denied: Insufficient permissions to audit users");
            return;
        }

        System.out.println(" Auditing user activity: " + userId);
        actionsPerformed++;

    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
        initializeDefaultPermissions(accessLevel);
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public boolean isCanModifyUsers() {
        return canModifyUsers;
    }

    public void setCanModifyUsers(boolean canModifyUsers) {
        this.canModifyUsers = canModifyUsers;
    }

    public boolean isCanGenerateReports() {
        return canGenerateReports;
    }

    public void setCanGenerateReports(boolean canGenerateReports) {
        this.canGenerateReports = canGenerateReports;
    }

    public boolean isCanManageJobs() {
        return canManageJobs;
    }

    public void setCanManageJobs(boolean canManageJobs) {
        this.canManageJobs = canManageJobs;
    }

    public boolean isCanManageApplications() {
        return canManageApplications;
    }

    public void setCanManageApplications(boolean canManageApplications) {
        this.canManageApplications = canManageApplications;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getActionsPerformed() {
        return actionsPerformed;
    }

    public void setActionsPerformed(int actionsPerformed) {
        this.actionsPerformed = actionsPerformed;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId='" + adminId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                ", permissions=" + permissions +
                ", actionsPerformed=" + actionsPerformed +
                '}';
    }
}