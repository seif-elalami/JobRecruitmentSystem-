package shared.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import shared.models.candidateDTO;

public interface ILinkedInService extends Remote    {


 candidateDTO getCandidateFromLinkedIn(String linkedinId)
            throws RemoteException;



}
