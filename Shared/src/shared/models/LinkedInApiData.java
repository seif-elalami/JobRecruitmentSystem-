package shared.models;

import java.util.*;

public class LinkedInApiData{

  private String FullName;
  private String address;
  private int Experience;
  private String Resume;
  private List<String> Skill;
  private String id;
  private String email;
  private String phone;
  private String education;
   public LinkedInApiData(String fullName, String Address, int experience, String resume, List<String> skill, String id, String email, String phone , String education)
   {
          FullName = fullName;
          address = Address;
          Experience = experience;
          Resume = resume;
          Skill = skill;
            this.id = id;
            this.email = email;
            this.phone = phone;
            this.education = education;
   } 

  public void setFullName(String fullName)
  {

         FullName = fullName;

  }

 public String getFullName()
{

      return FullName;

}

public void setaddress(String Address)
{

  address = Address;

}

public String getaddress()
{

   return address;

}

public void setexperience(int experience)
{

   Experience = experience;

}

public int getexperience()
{

    return Experience;

}

public String getresume()
{
   return Resume;

}

public List<String>getSkill()
{

   return Skill;

}

public String getid()
{
   return id;
}

public String getEmail()
{
   return email;
}

public String getphone()
{
   return phone;
}

public String geteducation()
{
   return education;
}

}