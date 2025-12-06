package server;

import server.services.ApplicantServiceImpl;
import server.services.JobServiceImpl;
import server.services.ApplicationServiceImpl;
import server.services.AuthServiceImpl;
import shared.interfaces.IApplicantService;
import shared. interfaces.IJobService;
import shared.interfaces.IApplicationService;
import shared.interfaces.IAuthService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    private static final int RMI_PORT = 1099;

    public static void main(String[] args) {
        try {
            System.out.println("╔════════════════════════════════════════╗");
            System.out. println("║     Job Recruitment System - SERVER   ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.println("Starting RMI Server...\n");

            // ========================================
            // STEP 1: Create Service Instances
            // ========================================
            System.out.println("Creating service instances...");
            IApplicantService applicantService = new ApplicantServiceImpl();
            IJobService jobService = new JobServiceImpl();
            IApplicationService applicationService = new ApplicationServiceImpl();
            IAuthService authService = new AuthServiceImpl(applicantService);  // NEW!
            System.out.println("✅ All services created\n");

            // ========================================
            // STEP 2: Create RMI Registry
            // ========================================
            System.out.println("Creating RMI Registry on port " + RMI_PORT + "...");
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            System.out.println("✅ RMI Registry created\n");

            // ========================================
            // STEP 3: Bind Services to Registry
            // ========================================
            System.out. println("Binding services to registry.. .");

            registry.rebind("ApplicantService", applicantService);
            System.out.println("   ✅ ApplicantService bound");

            registry.rebind("JobService", jobService);
            System.out.println("   ✅ JobService bound");

            registry.rebind("ApplicationService", applicationService);
            System.out.println("   ✅ ApplicationService bound");

            registry.rebind("AuthService", authService);  // NEW!
            System.out.println("   ✅ AuthService bound");

            System.out.println();

            // ========================================
            // Server is Ready!
            // ========================================
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║   ✅ RMI SERVER IS RUNNING!            ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System. out.println("Server Details:");
            System.out.println("   📡 Host: localhost");
            System.out.println("   🔌 Port: " + RMI_PORT);
            System.out.println("   📋 Services Available:");
            System.out.println("      - ApplicantService");
            System.out.println("      - JobService");
            System.out. println("      - ApplicationService");
            System.out.println("      - AuthService");  // NEW!
            System.out.println();
            System.out.println("⏳ Server is waiting for client connections...");
            System.out.println("   (Press Ctrl+C to stop the server)");
            System.out.println();

            // Keep server running
            Thread.currentThread().join();

        } catch (Exception e) {
            System.err.println("❌ Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
