package Server.services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import shared.Adapters.Adapter;
import shared.interfaces.ILinkedInService;
import shared.models.LinkedInApiData;
import shared.models.candidateDTO;

public class LinkedInService  extends UnicastRemoteObject implements ILinkedInService  {

     public LinkedInService() throws RemoteException {}

     public candidateDTO getCandidateFromLinkedIn(String linkedinId) throws RemoteException {

          LinkedInApiData apiData = new LinkedInApiData(
                linkedinId,
                "Ahmed Ali", 5, "Experienced Software Engineer" ,   List.of("Java", "Spring Boot", "Docker"),"203","omar@example.com", "01012345678","Bachelor's in Computer Science" 
        );

       Adapter adapter = new Adapter(apiData);

       return new candidateDTO(
        adapter.getId(),
        adapter.getName(),
        adapter.getEmail(),
        adapter.getPhone(),
        adapter.getResume(),
        adapter.getEducation(),
        Integer.parseInt(adapter.getExperience()),
        List.of(adapter.getSkills().split(","))
       );

     }

}