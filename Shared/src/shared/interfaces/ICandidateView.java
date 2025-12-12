package shared.interfaces;

import java.io.Serializable;
import java.util.List;

public interface ICandidateView extends Serializable {
    String getId();
    String getName();
    String getEmail();
    String getPhone();
    List<String> getSkills();
    String getExperience();
    String getEducation();
    String getResume();
}
