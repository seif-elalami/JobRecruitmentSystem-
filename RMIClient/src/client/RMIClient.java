package client;

import shared.interfaces.IApplicantService;
import shared.interfaces.IJobService;
import shared.interfaces. IApplicationService;
import shared.interfaces. IAuthService;

import java.rmi. Naming;
import java.rmi.RemoteException;

public class RMIClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1099;

    private IApplicantService applicantService;
    private IJobService jobService;
    private IApplicationService applicationService;
    private IAuthService authService;

    /**
     * Constructor - Connects to RMI Server
     */
    public RMIClient() throws Exception {
        connectToServer();
    }

    /**
     * Connect to RMI Server and lookup services
     */
    private void connectToServer() throws Exception {
        try {
            String serverURL = "rmi://" + SERVER_HOST + ":" + SERVER_PORT + "/";

            System.out.println("🔗 Connecting to RMI Server at " + serverURL);

            // Lookup all services
            applicantService = (IApplicantService) Naming.lookup(serverURL + "ApplicantService");
            System.out.println("   ✅ ApplicantService connected");

            jobService = (IJobService) Naming.lookup(serverURL + "JobService");
            System.out.println("   ✅ JobService connected");

            applicationService = (IApplicationService) Naming.lookup(serverURL + "ApplicationService");
            System.out. println("   ✅ ApplicationService connected");

            authService = (IAuthService) Naming.lookup(serverURL + "AuthService");
            System.out.println("   ✅ AuthService connected");

            System.out.println("✅ All services connected successfully!");

        } catch (Exception e) {
            System.err.println("❌ Failed to connect to RMI Server!");
            System.err.println("   Error: " + e.getMessage());
            System.err.println("\n💡 Make sure:");
            System.err.println("   1. RMI Server is running");
            System.err.println("   2. Server is on " + SERVER_HOST + ":" + SERVER_PORT);
            System. err.println("   3. MongoDB is running (required by server)");
            throw e;
        }
    }

    /**
     * Get ApplicantService
     */
    public IApplicantService getApplicantService() {
        return applicantService;
    }

    /**
     * Get JobService
     */
    public IJobService getJobService() {
        return jobService;
    }

    /**
     * Get ApplicationService
     */
    public IApplicationService getApplicationService() {
        return applicationService;
    }

    /**
     * Get AuthService
     */
    public IAuthService getAuthService() {
        return authService;
    }

    /**
     * Test connection to server
     */
    public boolean testConnection() {
        try {
            // Try to call a simple method to verify connection
            authService.getUserByEmail("test@test.com");
            return true;
        } catch (RemoteException e) {
            System.err.println("❌ Connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        try {
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║      RMI Client Connection Test       ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println();

            // Test connection
            new RMIClient();

            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║     ✅ Client Ready!                     ║");
            System.out.println("╚════════════════════════════════════════╝");
            System. out.println();
            System.out.println("Available services:");
            System.out. println("  • ApplicantService");
            System.out.println("  • JobService");
            System.out. println("  • ApplicationService");
            System.out.println("  • AuthService");

        } catch (Exception e) {
            System.err.println("\n❌ Client initialization failed!");
            e.printStackTrace();
        }
    }
}
