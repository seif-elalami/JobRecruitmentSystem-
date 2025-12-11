package shared.models;

import shared.interfaces.ICandidateView;
import java.util.ArrayList;
import java.util.List;

// ✅ REMOVE Serializable - only implement ICandidateView
public class Applicant implements ICandidateView {  // ← Fixed!
    private static final long serialVersionUID = 1L;

    // Fields
    private String id;
    private String name;
    private String email;
    private String phone;
    private String resume;
    private List<String> skills;
    private String education;
    private int experience;

    // Constructors
    public Applicant() {
        this.skills = new ArrayList<>();
    }

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
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getResume() {
        return resume;
    }

    @Override
    public List<String> getSkills() {
        return skills;
    }

    @Override
    public String getEducation() {
        return education;
    }

    @Override
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

    // Helper method
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
