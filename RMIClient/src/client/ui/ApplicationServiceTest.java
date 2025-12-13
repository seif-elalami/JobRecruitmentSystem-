package client.ui;

import client.RMIClient;
import client.utils.InputHelper;
import shared.models.Application;
import shared.models. Applicant;
import shared.models.Job;
import shared.interfaces.IApplicationService;
import shared.interfaces.IApplicantService;
import shared.interfaces.IJobService;

import java.util.List;

public class ApplicationServiceTest {

    private RMIClient client;
    private IApplicationService service;
    private IApplicantService applicantService;
    private IJobService jobService;

    public ApplicationServiceTest(RMIClient client) {
        this.client = client;
    }

    public void run() {
        try {
            service = client.getApplicationService();
            applicantService = client.getApplicantService();
            jobService = client.getJobService();

            boolean back = false;
            while (!back) {
                showMenu();
                int choice = InputHelper.getInt();
                System.out.println();

                switch (choice) {
                    case 1: submitApplication(); break;
                    case 2: getApplicationById(); break;
                    case 3: getApplicationsByJob(); break;
                    case 4: getApplicationsByApplicant(); break;
                    case 5: updateStatus(); break;
                    case 6: deleteApplication(); break;
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
        System.out.println("║      APPLICATION SERVICE TEST          ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out. println("1. Submit Application");
        System.out.println("2. Get Application by ID");
        System.out.println("3. Get Applications by Job ID");
        System.out.println("4. Get Applications by Applicant ID");
        System.out.println("5. Update Application Status");
        System.out.println("6. Delete Application");
        System.out.println("0. Back");
        System.out. print("\nChoice: ");
    }

    private void submitApplication() {
        try {
            System.out.println("=== SUBMIT APPLICATION ===\n");

            System.out.print("Job ID: ");
            String jobId = InputHelper.getString();

            System.out.println("\n📤 Verifying job.. .");
            Job job = jobService.getJobById(jobId);
            if (job == null) {
                System.out.println("❌ Job not found!");
                return;
            }
            System.out.println("✅ Job:  " + job.getTitle() + " at " + job.getCompany());

            System.out.print("\nApplicant ID: ");
            String applicantId = InputHelper.getString();

            System.out.println("\n📤 Verifying applicant...");
            Applicant applicant = applicantService.getApplicantById(applicantId);
            if (applicant == null) {
                System.out.println("❌ Applicant not found!");
                return;
            }
            System.out.println("✅ Applicant: " + applicant.getName());

            System.out. print("\nCover Letter: ");
            String coverLetter = InputHelper.getString();

            Application application = new Application(jobId, applicantId, coverLetter);

            System. out.println("\n📤 Submitting application...");
            String appId = service.SubmitApplication(application);

            System.out.println("✅ SUCCESS!");
            System.out.println("   Application ID: " + appId);
            System.out.println("   Job:  " + job.getTitle());
            System.out.println("   Applicant: " + applicant.getName());
            System.out.println("   Status:  PENDING");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void getApplicationById() {
        try {
            System.out.println("=== GET APPLICATION BY ID ===\n");
            System.out.print("Enter Application ID: ");
            String id = InputHelper.getString();

            System.out.println("\n📤 Fetching.. .");
            Application app = service. getApplicationById(id);

            if (app != null) {
                System.out.println("✅ FOUND!\n");
                printApplicationWithDetails(app);
            } else {
                System.out.println("❌ Application not found!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void getApplicationsByJob() {
        try {
            System.out.println("=== GET APPLICATIONS BY JOB ===\n");
            System. out.print("Enter Job ID:  ");
            String jobId = InputHelper.getString();

            System.out.println("\n📤 Fetching job.. .");
            Job job = jobService.getJobById(jobId);
            if (job == null) {
                System.out.println("❌ Job not found!");
                return;
            }

            System.out.println("✅ Job: " + job.getTitle() + " at " + job.getCompany());

            System.out.println("\n📤 Fetching applications...");
            List<Application> applications = service.getApplicationsByJobId(jobId);

            System.out.println("✅ Found " + applications.size() + " application(s)\n");

            if (applications.isEmpty()) {
                System. out.println("No applications for this job yet.");
            } else {
                for (int i = 0; i < applications.size(); i++) {
                    System.out. println("─────────────────────────────────────");
                    System.out.println("Application " + (i + 1) + ":");
                    printApplicationWithDetails(applications.get(i));
                }
                System.out.println("─────────────────────────────────────");
            }

        } catch (Exception e) {
            System. err.println("❌ Error: " + e.getMessage());
        }
    }

    private void getApplicationsByApplicant() {
        try {
            System.out.println("=== GET APPLICATIONS BY APPLICANT ===\n");
            System.out.print("Enter Applicant ID: ");
            String applicantId = InputHelper.getString();

            System.out.println("\n📤 Fetching applicant...");
            Applicant applicant = applicantService. getApplicantById(applicantId);
            if (applicant == null) {
                System. out.println("❌ Applicant not found!");
                return;
            }

            System.out.println("✅ Applicant: " + applicant.getName());

            System.out.println("\n📤 Fetching applications...");
            List<Application> applications = service.getApplicationsByApplicantId(applicantId);

            System.out.println("✅ Found " + applications. size() + " application(s)\n");

            if (applications.isEmpty()) {
                System.out.println("This applicant hasn't applied to any jobs yet.");
            } else {
                for (int i = 0; i < applications.size(); i++) {
                    System.out. println("─────────────────────────────────────");
                    System.out.println("Application " + (i + 1) + ":");
                    printApplicationWithDetails(applications.get(i));
                }
                System.out.println("─────────────────────────────────────");
            }

        } catch (Exception e) {
            System. err.println("❌ Error: " + e.getMessage());
        }
    }

    private void updateStatus() {
        try {
            System.out.println("=== UPDATE APPLICATION STATUS ===\n");
            System.out.print("Enter Application ID: ");
            String id = InputHelper.getString();

            System.out.println("\n📤 Fetching application...");
            Application app = service.getApplicationById(id);

            if (app == null) {
                System. out.println("❌ Application not found!");
                return;
            }

            System. out.println("✅ Found:");
            printApplicationWithDetails(app);

            System. out.println("\nCurrent Status: " + app.getStatus());
            System.out. println("\nAvailable statuses:");
            System.out. println("  1. PENDING");
            System. out.println("  2. ACCEPTED");
            System.out. println("  3. REJECTED");
            System.out.print("\nEnter new status (1-3): ");

            int statusChoice = InputHelper.getInt();
            String newStatus;

            switch (statusChoice) {
                case 1: newStatus = "PENDING"; break;
                case 2: newStatus = "ACCEPTED"; break;
                case 3: newStatus = "REJECTED"; break;
                default:
                    System.out.println("❌ Invalid status!");
                    return;
            }

            System.out.println("\n📤 Updating status to: " + newStatus);
            boolean updated = service.updateApplicationStatus(id, newStatus);

            if (updated) {
                System. out.println("✅ Status updated successfully!");
            } else {
                System.out.println("❌ Update failed!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void deleteApplication() {
        try {
            System.out.println("=== DELETE APPLICATION ===\n");
            System.out.print("Enter Application ID: ");
            String id = InputHelper.getString();

            System.out.println("\n📤 Fetching application...");
            Application app = service.getApplicationById(id);

            if (app == null) {
                System.out.println("❌ Application not found!");
                return;
            }

            System.out.println("✅ Found:");
            printApplicationWithDetails(app);

            if (!InputHelper.confirm("\n⚠️  Delete this application? ")) {
                System.out. println("❌ Cancelled.");
                return;
            }

            System.out.println("\n📤 Deleting application...");
            boolean deleted = service.deleteApplication(id);

            System.out.println(deleted ? "✅ Deleted!" : "❌ Failed!");

        } catch (Exception e) {
            System. err.println("❌ Error: " + e.getMessage());
        }
    }

    private void printApplicationWithDetails(Application app) {
        System.out.println("  Application ID: " + app.getId());
        System.out.println("  Status: " + app.getStatus());
        System.out.println("  Date: " + app.getApplicationDate());

        try {
            Job job = jobService.getJobById(app.getJobId());
            if (job != null) {
                System.out.println("  Job: " + job.getTitle() + " at " + job.getCompany());
            } else {
                System.out. println("  Job ID: " + app.getJobId() + " (Not found)");
            }
        } catch (Exception e) {
            System.out.println("  Job ID: " + app.getJobId());
        }

        try {
            Applicant applicant = applicantService.getApplicantById(app.getApplicantId());
            if (applicant != null) {
                System.out.println("  Applicant: " + applicant. getName() + " (" + applicant. getEmail() + ")");
            } else {
                System.out.println("  Applicant ID: " + app.getApplicantId() + " (Not found)");
            }
        } catch (Exception e) {
            System.out.println("  Applicant ID: " + app. getApplicantId());
        }

        if (app.getCoverLetter() != null && ! app.getCoverLetter().isEmpty()) {
            String cover = app.getCoverLetter();
            System.out.println("  Cover Letter: " +
                (cover.length() > 100 ? cover.substring(0, 100) + "..." : cover));
        }
    }
}