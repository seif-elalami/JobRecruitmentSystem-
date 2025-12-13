package client;

import shared.interfaces.IApplicantService;
import shared.interfaces.IJobService;
import shared.interfaces.IApplicationService;
import shared.interfaces.IAuthService;
import shared.interfaces.IRecruiterService;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class RMIClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1099;

    private IApplicantService applicantService;
    private IJobService jobService;
    private IApplicationService applicationService;
    private IAuthService authService;
    private IRecruiterService recruiterService;

    public RMIClient() throws Exception {
        connectToServer();
    }

    private void connectToServer() throws Exception {
        try {
            String serverURL = "rmi://" + SERVER_HOST + ":" + SERVER_PORT + "/";

            System.out.println("🔗 Connecting to RMI Server at " + serverURL);

            applicantService = (IApplicantService) Naming.lookup(serverURL + "ApplicantService");
            System.out.println("   ✅ ApplicantService connected");

            recruiterService = (IRecruiterService) Naming.lookup(serverURL + "RecruiterService");
            System.out.println("   ✅ RecruiterService connected");

            jobService = (IJobService) Naming.lookup(serverURL + "JobService");
            System.out.println("   ✅ JobService connected");

            applicationService = (IApplicationService) Naming.lookup(serverURL + "ApplicationService");
            System.out.println("   ✅ ApplicationService connected");

            authService = (IAuthService) Naming.lookup(serverURL + "AuthService");
            System.out.println("   ✅ AuthService connected");

            System.out.println("✅ All services connected successfully!");

        } catch (Exception e) {
            System.err.println("❌ Failed to connect to RMI Server!");
            System.err.println("   Error: " + e.getMessage());
            throw e;
        }
    }

    public IApplicantService getApplicantService() {
        return applicantService;
    }

    public IJobService getJobService() {
        return jobService;
    }

    public IApplicationService getApplicationService() {
        return applicationService;
    }

    public IAuthService getAuthService() {
        return authService;
    }

    public IRecruiterService getRecruiterService() {
        return recruiterService;
    }

    public boolean testConnection() {
        try {
            authService.getUserByEmail("test@test.com");
            return true;
        } catch (RemoteException e) {
            System.err.println("❌ Connection test failed: " + e.getMessage());
            return false;
        }
    }
}