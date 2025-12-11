package shared.models;

/**
 * Recruiter (HR Member) class - Extends User
 * Represents a recruiter/HR person in the system
 */
public class Recruiter extends User {

    private static final long serialVersionUID = 1L;

    // Additional attribute specific to Recruiter : department)
    private String department;

    // Constructors
    public Recruiter() {
        super();
        this.setRole("RECRUITER");  // Set role automatically
    }

    public Recruiter(String username, String password, String email, String department) {
        super(username, password, email, "RECRUITER");
        this.department = department;
    }

    // Getter and Setter for department
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Convenience methods for backward compatibility
    // Map User. userId to id
    public String getId() {
        return getUserId();
    }

    public void setId(String id) {
        setUserId(id);
    }

    // Map User. username to name
    public String getName() {
        return getUsername();
    }

    public void setName(String name) {
        setUsername(name);
    }

    // Phone is not in User, so we keep it in Recruiter
    // But we can store it in a custom field or use department
    // For now, let's add it as a separate field
    private String phone;
    private String company;
    private String position;
    private String description;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Recruiter{" +
                "id='" + getUserId() + '\'' +
                ", name='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role='" + getRole() + '\'' +
                ", phone='" + phone + '\'' +
                ", company='" + company + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
