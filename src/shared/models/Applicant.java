package shared.models;

import java. io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Applicant implements Serializable {
    private static final long serialVersionUID = 1L;

    // Fields
    private String id;              // MongoDB ObjectId (set after saving)
    private String name;            // Full name
    private String email;           // Email address (unique)
    private String phone;           // Phone number
    private String resume;          // Resume text or file path
    private List<String> skills;    // List of skills
    private String education;       // Education background
    private int experience;         // Years of experience

    // Default constructor
    public Applicant() {
        this.skills = new ArrayList<>();
    }

    // Constructor with all fields except id
    public Applicant(String name, String email, String phone,
                     String resume, String education, int experience) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.resume = resume;
        this.education = education;
        this.experience = experience;
        this.skills = new ArrayList<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getResume() {
        return resume;
    }

    public List<String> getSkills() {
        return skills;
    }

    public String getEducation() {
        return education;
    }

    public int getExperience() {
        return experience;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    // Helper method to add a single skill
    public void addSkill(String skill) {
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        this.skills.add(skill);
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", education='" + education + '\'' +
                ", experience=" + experience + " years" +
                ", skills=" + skills +
                '}';
    }
}
