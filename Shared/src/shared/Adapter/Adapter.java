package shared.Adapters;
import java.util.*;
import shared.interfaces.ICandidateView;
import shared.models.LinkedInApiData;

public class Adapter implements ICandidateView {

   LinkedInApiData linkedIn;  

 public Adapter( LinkedInApiData linkedIn)
  {
      this.linkedIn = linkedIn; 
  }

  
  
  public String getId() {
      return linkedIn.getid();
  }

  
  public String getName() {
      return linkedIn.getFullName();
  }

  
  public List<String> getSkills() {
      return linkedIn.getSkill();
  }

  
    
  

  
  

  public String getAddress() {
      return linkedIn.getaddress();
  }


  public String getEmail() {
      return linkedIn.getEmail();
  }

    public String getPhone() {
        return linkedIn.getphone();
    }



    
    public int getExperience() {
        return linkedIn.getexperience();
        
    }



    
    public String getEducation() {
        return linkedIn.geteducation();
    }



    
    public String getResume() {
        return linkedIn.getresume();
    }

   

}
