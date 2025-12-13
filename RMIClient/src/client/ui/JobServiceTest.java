package client.ui;

import client.RMIClient;
import client.utils.InputHelper;
import shared.models.Job;
import shared.interfaces.IJobService;
import shared.interfaces.IReportService;

import java.util.List;

public class JobServiceTest {

    private RMIClient client;
    private IJobService service;
    private IReportService reportService;

    public JobServiceTest(RMIClient client) {
        this.client = client;
    }

    public void run() {
        try {
            service = (shared.interfaces.IJobService) java.rmi.Naming.lookup("rmi://localhost:1099/JobService");
            reportService = (shared.interfaces.IReportService) java.rmi.Naming.lookup("rmi://localhost:1099/ReportService");

            boolean back = false;
            while (!back) {
                showMenu();
                int choice = InputHelper.getInt();
                System.out.println();

                switch (choice) {
                    case 1: createJob(); break;
                    case 2: getJobById(); break;
                    case 3: getAllJobs(); break;
                    case 4: searchByTitle(); break;
                    case 5: searchByLocation(); break;
                    case 6: closeJob(); break;
                    case 7: deleteJob(); break;
                    case 8: generateSimpleReport(); break;
                    case 9: generateDetailedReport(); break;
                    case 10: generateFilteredReport(); break;
                    case 0: back = true; break;
                    default: System.out.println("❌ Invalid choice!");
                }

                if (!back) InputHelper.pause();
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         JOB SERVICE TEST               ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("1. Create Job");
        System.out.println("2. Get Job by ID");
        System.out.println("3. Get All Jobs");
        System.out.println("4. Search Jobs by Title");
        System.out.println("5. Search Jobs by Location");
        System.out.println("6. Close Job");
        System.out.println("7. Delete Job");
        System.out.println("───── REPORTS ─────");
        System.out.println("8. Generate Simple Report");
        System.out.println("9. Generate Detailed Report");
        System.out.println("10. Generate Filtered Report");
        System.out.println("0. Back");
        System.out.print("\nChoice: ");
    }

    private void createJob() {
        try {
            System.out.println("=== CREATE JOB ===\n");

            System.out.print("Job Title: ");
            String title = InputHelper.getString();

            System.out.print("Description: ");
            String description = InputHelper.getString();

            System.out.print("Company Name: ");
            String company = InputHelper.getString();

            System.out.print("Location: ");
            String location = InputHelper.getString();

            System.out.print("Salary: ");
            double salary = InputHelper.getDouble();

            System.out.print("Requirements: ");
            String requirements = InputHelper.getString();

            Job job = new Job(title, description, company, location, salary, requirements);

            System.out.println("\n📤 Creating job...");
            String id = service.createJob(job);

            System.out.println("✅ SUCCESS!");
            System.out.println("   Job ID: " + id);
            System.out.println("   Title: " + title);
            System.out.println("   Company: " + company);
            System.out.println("   Salary: $" + salary);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void getJobById() {
        try {
            System.out.println("=== GET JOB BY ID ===\n");
            System.out.print("Enter Job ID: ");
            String id = InputHelper.getString();

            System.out.println("\n📤 Fetching...");
            Job job = service.getJobById(id);

            if (job != null) {
                System.out.println("✅ FOUND!\n");
                printJob(job);
            } else {
                System.out.println("❌ Job not found!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void getAllJobs() {
        try {
            System.out.println("=== GET ALL JOBS ===\n");
            System.out.println("📤 Fetching...");

            List<Job> all = service.getAllJobs();

            System.out.println("✅ Found " + all.size() + " job(s)\n");

            if (all.isEmpty()) {
                System.out.println("No jobs in database.");
            } else {
                for (int i = 0; i < all.size(); i++) {
                    System.out.println("─────────────────────────────────────");
                    System.out.println("Job " + (i + 1) + ":");
                    printJob(all.get(i));
                }
                System.out.println("─────────────────────────────────────");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void searchByTitle() {
        try {
            System.out.println("=== SEARCH BY TITLE ===\n");
            System.out.print("Enter title keyword: ");
            String keyword = InputHelper.getString();

            System.out.println("\n📤 Searching...");
            List<Job> results = service.searchJobsByTitle(keyword);

            System.out.println("✅ Found " + results.size() + " job(s)\n");

            if (results.isEmpty()) {
                System.out.println("No jobs found with that title.");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    System.out.println("─────────────────────────────────────");
                    System.out.println("Match " + (i + 1) + ":");
                    printJob(results.get(i));
                }
                System.out.println("─────────────────────────────────────");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void searchByLocation() {
        try {
            System.out.println("=== SEARCH BY LOCATION ===\n");
            System.out.print("Enter location: ");
            String location = InputHelper.getString();

            System.out.println("\n📤 Searching...");
            List<Job> results = service.getJobsByLocation(location);

            System.out.println("✅ Found " + results.size() + " job(s)\n");

            if (results.isEmpty()) {
                System.out.println("No jobs found in that location.");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    System.out.println("─────────────────────────────────────");
                    System.out.println("Match " + (i + 1) + ":");
                    printJob(results.get(i));
                }
                System.out.println("─────────────────────────────────────");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void closeJob() {
        try {
            System.out.println("=== CLOSE JOB ===\n");
            System.out.print("Enter Job ID: ");
            String id = InputHelper.getString();

            System.out.println("\n📤 Fetching job...");
            Job job = service.getJobById(id);

            if (job == null) {
                System.out.println("❌ Job not found!");
                return;
            }

            System.out.println("✅ Found:");
            printJob(job);

            if (job.getStatus().equals("CLOSED")) {
                System.out.println("\n⚠️  This job is already closed!");
                return;
            }

            if (! InputHelper.confirm("\n⚠️  Close this job? ")) {
                System.out.println("❌ Cancelled.");
                return;
            }

            System.out.println("\n📤 Closing job...");
            boolean closed = service.closeJob(id);

            System.out.println(closed ? "✅ Job closed!" : "❌ Failed!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void deleteJob() {
        try {
            System.out.println("=== DELETE JOB ===\n");
            System.out.print("Enter Job ID: ");
            String id = InputHelper.getString();

            System.out.println("\n📤 Fetching job...");
            Job job = service.getJobById(id);

            if (job == null) {
                System.out.println("❌ Job not found!");
                return;
            }

            System.out.println("✅ Found:");
            printJob(job);

            if (!InputHelper.confirm("\n⚠️  Delete this job? ")) {
                System.out.println("❌ Cancelled.");
                return;
            }

            System.out.println("\n📤 Deleting job...");
            boolean deleted = service.deleteJob(id);

            System.out.println(deleted ? "✅ Deleted!" : "❌ Failed!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void generateSimpleReport() {
        try {
            System.out.println("=== SIMPLE REPORT ===\n");
            System.out.println("📤 Generating simple report for all jobs...");

            List<Job> jobs = service.getAllJobs();
            String report = reportService.generateSimpleReport(jobs, "Job Postings Summary");

            System.out.println("\n" + report);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void generateDetailedReport() {
        try {
            System.out.println("=== DETAILED REPORT ===\n");
            System.out.println("📤 Generating detailed report for all jobs...");

            List<Job> jobs = service.getAllJobs();
            String report = reportService.generateDetailedReport(jobs, "Complete Job Listings");

            System.out.println("\n" + report);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void generateFilteredReport() {
        try {
            System.out.println("=== FILTERED REPORT ===\n");
            System.out.print("Enter minimum salary: $");
            double minSalary = InputHelper.getDouble();

            System.out.print("Enter maximum salary: $");
            double maxSalary = InputHelper.getDouble();

            System.out.println("\n📤 Generating filtered report (salary range: $" + minSalary + " - $" + maxSalary + ")...");

            List<Job> jobs = service.getAllJobs();
            String report = reportService.generateFilteredReportBySalary(jobs, "Jobs in Salary Range: $" + minSalary + " - $" + maxSalary, minSalary, maxSalary);

            System.out.println("\n" + report);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void printJob(Job j) {
        System.out.println("  ID: " + j.getId());
        System.out.println("  Title: " + j.getTitle());
        System.out.println("  Company: " + j.getCompany());
        System.out.println("  Location: " + j.getLocation());
        System.out.println("  Salary: $" + j.getSalary());
        System.out.println("  Status: " + j.getStatus());
        System.out.println("  Description: " + j.getDescription());
        System.out.println("  Requirements: " + j.getRequirements());
    }
}
