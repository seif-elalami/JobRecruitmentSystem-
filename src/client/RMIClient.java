package client;

import shared.interfaces.IApplicantService;
import shared.interfaces.IJobService;
import shared.interfaces.IApplicationService;
import shared.interfaces.IAuthService;

import java.rmi.registry. LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1099;

    private IApplicantService applicantService;
    private IJobService jobService;
    private IApplicationService applicationService;
    private IAuthService authService;

    // Constructor - Connects to server
    public RMIClient() throws Exception {
        connectToServer();
    }

    // Connect to RMI Server
    private void connectToServer() throws Exception {
        System. out.println("╔════════════════════════════════════════╗");
        System.out.println("║     Job Recruitment System - CLIENT   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out. println("Connecting to RMI Server...");
        System.out.println("   Host: " + SERVER_HOST);
        System. out.println("   Port: " + SERVER_PORT);
        System.out.println();

        try {
            // Get registry from server
            Registry registry = LocateRegistry.getRegistry(SERVER_HOST, SERVER_PORT);
            System.out.println("✅ Connected to RMI Registry\n");

            // Look up services
            System.out.println("Looking up remote services.. .");

            applicantService = (IApplicantService) registry.lookup("ApplicantService");
            System.out.println("   ✅ ApplicantService found");

            jobService = (IJobService) registry.lookup("JobService");
            System. out.println("   ✅ JobService found");

            applicationService = (IApplicationService) registry.lookup("ApplicationService");
            System.out.println("   ✅ ApplicationService found");

            // ADD THIS LINE - This is what's missing!
            authService = (IAuthService) registry.lookup("AuthService");
            System.out.println("   ✅ AuthService found");

            System.out. println();
            System.out. println("╔════════════════════════════════════════╗");
            System.out.println("║   ✅ CLIENT CONNECTED SUCCESSFULLY!    ║");
            System.out.println("╚════════════════════════════════════════╝\n");

        } catch (Exception e) {
            System.err. println("❌ Connection failed: " + e.getMessage());
            System.err.println("\n⚠️  Make sure RMI Server is running first!");
            throw e;
        }
    }

    // Getters for services
    public IApplicantService getApplicantService() {
        return applicantService;
    }

    public IJobService getJobService() {
        return jobService;
    }

    public IApplicationService getApplicationService() {
        return applicationService;
    }

    // ADD THIS METHOD - This is what's missing!
    public IAuthService getAuthService() {
        return authService;
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            // Create client (connects to server)
            RMIClient client = new RMIClient();

            System.out.println("✅ RMI Client is ready!");
            System.out.println("✅ You can now call remote methods on the server!");
            System.out.println();
            System.out.println("Example usage:");
            System.out.println("   IJobService jobService = client.getJobService();");
            System.out.println("   IAuthService authService = client.getAuthService();");
            System. out.println();

        } catch (Exception e) {
            System.err.println("❌ Client initialization failed");
            e.printStackTrace();
        }
    }
}
