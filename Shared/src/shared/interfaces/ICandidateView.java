package shared.interfaces;

import java.io.Serializable;
import java.util.List;

public interface ICandidateView extends Serializable {
    String getId();
    String getName();
    String getEmail();
    String getPhone();
    // Return raw skills string to avoid collision with User.getSkills()
    String getSkills();
    // Return raw experience string to align with User.getExperience()
    String getExperience();
    String getEducation();
    String getResume();
}
