package shared.models;

/**
 * Recruiter (HR Member) class - Extends User
 * Represents a recruiter/HR person in the system
 */
public class Recruiter extends User {

    private static final long serialVersionUID = 1L;

    private String department;

    public Recruiter() {
        super();
        this.setRole("RECRUITER");  // Set role automatically
    }

    public Recruiter(String username, String password, String email, String department) {
        super(username, password, email, "RECRUITER");
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getId() {
        return getUserId();
    }

    public void setId(String id) {
        setUserId(id);
    }

    public String getName() {
        return getUsername();
    }

    public void setName(String name) {
        setUsername(name);
    }

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