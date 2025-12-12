package shared.models;

import shared.interfaces.ICandidateView;
import java.util.ArrayList;
import java.util.List;

// ✅ REMOVE Serializable - only implement ICandidateView
public class Applicant implements ICandidateView {  // ← Fixed!
    private static final long serialVersionUID = 1L;

    // Fields
    private String applicantId;
    private String resumeID;
    private String name;
    private String email;
    private String phone;
    private String resume;
    private String resumePath;
    private List<String> skills;
    private String education;
    private int experience;
    private int yearsExperience;
    private List<Application> applications;

    // Constructors
    public Applicant() {
        this.skills = new ArrayList<>();
        this.applications = new ArrayList<>();
    }

    public Applicant(String name, String email, String phone,
                     String resume, String education, int experience) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.resume = resume;
        this.education = education;
        this.experience = experience;
        this.yearsExperience = experience;
        this.skills = new ArrayList<>();
        this.applications = new ArrayList<>();
    }

    // Getters
    @Override
    public String getId() {
        return applicantId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public String getResumeID() {
        return resumeID;
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

    public String getResumePath() {
        return resumePath;
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
   public String getExperience() {
    return String.valueOf(experience);
}

    public int getYearsExperience() {
        return yearsExperience;
    }

    public List<Application> getApplications() {
        return applications;
    }

    // Setters
    public void setId(String id) {
        this.applicantId = id;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public void setResumeID(String resumeID) {
        this.resumeID = resumeID;
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

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
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

    public void setYearsExperience(int yearsExperience) {
        this.yearsExperience = yearsExperience;
        this.experience = yearsExperience;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    // Helper and Action Methods
    public void addSkill(String skill) {
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        this.skills.add(skill);
    }

    public void addApplication(Application application) {
        if (this.applications == null) {
            this.applications = new ArrayList<>();
        }
        this.applications.add(application);
    }

    public void uploadResume() {
        // Implementation for resume upload
        // This will interact with file storage service
        System.out.println("📤 Uploading resume...");
        // File upload logic handled by service layer
    }

    public void applyToJob(String jobId) {
        // Implementation for job application
        // This will be handled by ApplicationService
        System.out.println("📋 Applying to job: " + jobId);
        // Application creation logic handled by service layer
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id='" + applicantId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", education='" + education + '\'' +
                ", experience=" + experience + " years" +
                ", skills=" + skills +
                '}';
    }
}
