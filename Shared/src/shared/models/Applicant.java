package shared.models;

import shared.interfaces.ICandidateView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Applicant now extends User to reuse core identity fields
 * and implements ICandidateView for UI/DTO projections.
 */
public class Applicant extends User implements ICandidateView {
    private static final long serialVersionUID = 1L;

    private String applicantId;
    private String resumeID;
    private String resume;
    private String resumePath;
    private String education;
    private int yearsExperience; // numeric projection of User's experience
    private List<Application> applications;

    public Applicant() {
        super();
        this.applications = new ArrayList<>();

        setRole("APPLICANT");
    }

    /**
     * Construct Applicant from an existing User record.
     * Copies core fields and sets applicant-specific projections.
     */
    public Applicant(User user) {
        this();
        if (user != null) {
            setUserId(user.getUserId());
            setUsername(user.getUsername());
            setEmail(user.getEmail());
            setPassword(user.getPassword());
            setRole(user.getRole() == null ? "APPLICANT" : user.getRole());
            setPhone(user.getPhone());
            setCreatedAt(user.getCreatedAt());
            setLastLogin(user.getLastLogin());
            setActive(user.isActive());

            setDepartment(user.getDepartment());
            setCompany(user.getCompany());
            setPosition(user.getPosition());
            setDescription(user.getDescription());

            this.yearsExperience = parseYearsFromExperience(user.getExperience());
        }
    }

    public Applicant(String name, String email, String phone,
                     String resume, String education, int experience) {
        this();
        setUsername(name);
        setEmail(email);
        setPhone(phone);
        this.resume = resume;
        this.education = education;
        setExperience(experience); // updates yearsExperience and User.experience
    }

    @Override
    public String getId() { return applicantId; }
    public String getApplicantId() { return applicantId; }
    public String getResumeID() { return resumeID; }
    @Override
    public String getName() { return getUsername(); }
    @Override
    public String getEmail() { return super.getEmail(); }
    @Override
    public String getPhone() { return super.getPhone(); }
    @Override
    public String getResume() { return resume; }
    public String getResumePath() { return resumePath; }
    @Override
    public String getSkills() { return super.getSkills(); }
    @Override
    public String getEducation() { return education; }
    @Override
    public String getExperience() { return super.getExperience(); }
    public int getYearsExperience() { return yearsExperience; }
    public List<Application> getApplications() { return applications; }

    public void setId(String id) { this.applicantId = id; }
    public void setApplicantId(String applicantId) { this.applicantId = applicantId; }
    public void setResumeID(String resumeID) { this.resumeID = resumeID; }
    public void setResume(String resume) { this.resume = resume; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }
    public void setEducation(String education) { this.education = education; }
    public void setYearsExperience(int yearsExperience) { this.yearsExperience = yearsExperience; }
    public void setApplications(List<Application> applications) { this.applications = applications; }

    public void setName(String name) { super.setUsername(name); }
    public void setExperience(int years) {
        this.yearsExperience = years;
        super.setExperience(Integer.toString(years));
    }
    public void setSkills(List<String> skillsList) {
        if (skillsList == null || skillsList.isEmpty()) {
            super.setSkills("");
        } else {
            super.setSkills(String.join(",", skillsList));
        }
    }

    public void addSkill(String skill) {

        String current = super.getSkills();
        if (current == null || current.isEmpty()) {
            super.setSkills(skill);
        } else {
            super.setSkills(current + "," + skill);
        }
    }

    public void addApplication(Application application) {
        if (this.applications == null) {
            this.applications = new ArrayList<>();
        }
        this.applications.add(application);
    }

    public void uploadResume() {

        System.out.println("📤 Uploading resume...");

    }

    public void applyToJob(String jobId) {

        System.out.println("📋 Applying to job: " + jobId);

    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id='" + applicantId + '\'' +
                ", name='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", education='" + education + '\'' +
                ", experience=" + yearsExperience + " years" +
                ", skills=" + mapSkillsStringToList(getSkills()) +
                '}';
    }

    private List<String> mapSkillsStringToList(String skillsStr) {
        if (skillsStr == null || skillsStr.trim().isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(skillsStr.split("\s*,\s*")));
    }

    public int parseYearsFromExperience(String expStr) {
        if (expStr == null) return 0;
        try {

            return Integer.parseInt(expStr.trim());
        } catch (NumberFormatException ignored) {

            String digits = expStr.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) return 0;
            try { return Integer.parseInt(digits); } catch (NumberFormatException e) { return 0; }
        }
    }
}