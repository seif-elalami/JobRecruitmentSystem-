package shared.models;

public class LinkedInApiData{

  private String FullName;
  private String EmailAddress;
  private int Experience;


  public void setFullName(String fullName)
  {
       
         FullName = fullName;

  }

 public String getFullName()
{

      return FullName;
    

}

public void setEmialAddress(String emailAddress)
{

  EmailAddress = emailAddress;


}

public String getEmailAddress()
{

   return EmailAddress;

}

public void setExperience(int experience)
{

   Experience = experience;

}

public int getExperience()
{

    return Experience;

}



}
