package client.ui;

import client.RMIClient;
import shared.interfaces.IJobService;
import shared.interfaces.IReportService;
import shared.models.Job;
import java.util.List;

public class ReportTester {

    public static void main(String[] args) {
        try {
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║    REPORT GENERATION TEST PROGRAM      ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.println("🔗 Connecting to RMI Server...");
            RMIClient client = new RMIClient();
            System.out.println("✅ Connected!\n");

            IJobService jobService = (IJobService) java.rmi.Naming.lookup("rmi://localhost:1099/JobService");
            IReportService reportService = (IReportService) java.rmi.Naming.lookup("rmi://localhost:1099/ReportService");

            List<Job> jobs = jobService.getAllJobs();
            System.out.println("📊 Found " + jobs.size() + " jobs in database\n");

            System.out.println("\n" + "═".repeat(50));
            System.out.println("TEST 1: SIMPLE REPORT");
            System.out.println("═".repeat(50) + "\n");
            String simpleReport = reportService.generateSimpleReport(jobs, "Job Summary Report");
            System.out.println(simpleReport);

            System.out.println("\n" + "═".repeat(50));
            System.out.println("TEST 2: DETAILED REPORT");
            System.out.println("═".repeat(50) + "\n");
            String detailedReport = reportService.generateDetailedReport(jobs, "Complete Job Listings");
            System.out.println(detailedReport);

            System.out.println("\n" + "═".repeat(50));
            System.out.println("TEST 3: FILTERED REPORT (Salary Filter)");
            System.out.println("═".repeat(50) + "\n");

            double minSalary = 60000;
            double maxSalary = 100000;

            String filteredReport = reportService.generateFilteredReportBySalary(
                jobs,
                "Jobs in Salary Range: $" + minSalary + " - $" + maxSalary,
                minSalary,
                maxSalary
            );
            System.out.println(filteredReport);

            System.out.println("\n" + "═".repeat(50));
            System.out.println("✅ ALL REPORTS GENERATED SUCCESSFULLY!");
            System.out.println("═".repeat(50) + "\n");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}