package Server;

import Server.database.MongoDBConnection;
import Server.services.ApplicantServiceImpl;
import Server.services.JobServiceImpl;
import Server.services.ApplicationServiceImpl;
import Server.services.AuthServiceImpl;
import Server.services.RecruiterServiceImpl;
import Server.services.ReportServiceImpl;
import shared.interfaces.IApplicantService;
import shared.interfaces.IJobService;
import shared.interfaces.IApplicationService;
import shared.interfaces.IAuthService;
import shared.interfaces.IRecruiterService;
import shared.interfaces.IReportService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer {

    private static final int RMI_PORT = 1099;

    public static void main(String[] args) {
        try {

            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            System.out.println("    Job Recruitment System - SERVER   ");
            System.out.println("=========================================");
        

            System.out.println(" Step 1: Checking MongoDB connection...");
            MongoDBConnection.getInstance().getDatabase();
            System.out.println("    MongoDB connected successfully");
            System.out.println();

            System.out.println("🔧 Step 2: Starting RMI Registry on port " + RMI_PORT + "...");
            try {
                LocateRegistry.createRegistry(RMI_PORT);
                System.out.println("    RMI Registry started");
            } catch (Exception e) {
                System.out.println("    RMI Registry already running");
            }
            System.out.println();

            System.out.println(" Step 3: Creating service instances...");

            IApplicantService applicantService = new ApplicantServiceImpl();
            IRecruiterService recruiterService = new RecruiterServiceImpl();
            IJobService jobService = new JobServiceImpl();
            IApplicationService applicationService = new ApplicationServiceImpl();
            IAuthService authService = new AuthServiceImpl();
            IReportService reportService = new ReportServiceImpl();

            System.out.println("   All services created");
            System.out.println();

            System.out.println(" Step 4: Binding services to RMI Registry.. .");

            String serverURL = "rmi://localhost:" + RMI_PORT + "/";

            Naming.rebind(serverURL + "ApplicantService", applicantService);
            System.out.println("    ApplicantService bound");

            Naming.rebind(serverURL + "RecruiterService", recruiterService);
            System.out.println("    RecruiterService bound");

            Naming.rebind(serverURL + "JobService", jobService);
            System.out.println("    JobService bound");

            Naming.rebind(serverURL + "ApplicationService", applicationService);
            System.out.println("    ApplicationService bound");

            Naming.rebind(serverURL + "AuthService", authService);
            System.out.println("    AuthService bound");

            Naming.rebind(serverURL + "ReportService", reportService);
            System.out.println("    ReportService bound");

            System.out.println();
    
            System.out.println("      SERVER RUNNING!                    ");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println();
            System.out.println("Server Details:");
            System.out.println("  • RMI Port: " + RMI_PORT);
            System.out.println("  • MongoDB:  localhost:27017");
            System.out.println("  • Status: Ready to accept connections");
            System.out.println();
            System.out.println("Available Services:");
            System.out.println("  • " + serverURL + "ApplicantService");
            System.out.println("  • " + serverURL + "RecruiterService");
            System.out.println("  • " + serverURL + "JobService");
            System.out.println("  • " + serverURL + "ApplicationService");
            System.out.println("  • " + serverURL + "AuthService");
            System.out.println("  • " + serverURL + "ReportService");
            System.out.println();
            System.out.println("Press Ctrl+C to stop the server...");
            System.out.println("═══════════════════════════════════════════");

            Thread.currentThread().join();

        } catch (Exception e) {
            System.err.println("\n Server failed to start!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}