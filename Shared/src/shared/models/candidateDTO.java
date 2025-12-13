package shared.models;

import java.io.Serializable;
import java.util.List;

import shared.interfaces.ICandidateView;

public class candidateDTO implements ICandidateView, Serializable  {

     
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String email;
    private String phone;
    private String resume;
    private String education;
    private int experience;
    private java.util.List<String> skills;

    public candidateDTO(String id, String name, String email, String phone, String resume, String education, int experience, java.util.List<String> skills) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.resume = resume;
        this.education = education;
        this.experience = experience;
        this.skills = skills;
    }

    
    


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

    
    public String getEducation() {
        return education;
    }

    
    public int getExperience() {
        return experience;
    }

    
    public java.util.List<String> getSkills() {
        return skills;
    }



}
