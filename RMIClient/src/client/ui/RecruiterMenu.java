package client.ui;

import client.RMIClient;
import client.utils.InputHelper;
import shared.interfaces.IJobService;
import shared.interfaces. IApplicationService;
import shared.models.Job;
import shared.models.Application;
import shared.models.Session;

import java.util.ArrayList;
import java.util.List;

public class RecruiterMenu {

    private RMIClient client;
    private Session session;
    private IJobService jobService;
    private IApplicationService applicationService;

    public RecruiterMenu(RMIClient client, Session session) {
        this.client = client;
        this.session = session;
        this.jobService = client.getJobService();
        this.applicationService = client.getApplicationService();
    }

    public void run() {
        show();
    }

    public void show() {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = InputHelper.getInt();
            System.out.println();

            switch (choice) {
                case 1:
                    createJobPosting();
                    break;
                case 2:
                    viewMyJobPostings();
                    break;
                case 3:
                    viewAllApplications();
                    break;
                case 4:
                    reviewApplication();
                    break;
                case 5:
                    closeJobPosting();
                    break;
                case 0:
                    running = false;
                    System.out.println("👋 Logging out...");
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }

            if (running && choice != 0) {
                InputHelper.pause();
            }
        }
    }

    private void showMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out. println("║       Recruiter Menu                   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Welcome, " + session.getUserEmail() + " (Recruiter)");
        System.out.println();
        System.out.println("1. Create Job Posting");
        System.out.println("2. View My Job Postings");
        System.out.println("3. View All Applications");
        System.out.println("4. Review Application");
        System.out.println("5. Close Job Posting");
        System.out.println("0.  Logout");
        System.out. print("\nChoice: ");
    }

    private void createJobPosting() {
        try {
            System.out.println("=== CREATE JOB POSTING ===\n");

            System. out.print("Job Title: ");
            String title = InputHelper.getString();

            System.out.print("Job Description: ");
            String description = InputHelper.getString();

            System.out.println("Requirements (enter one per line, type 'done' when finished):");
            List<String> requirements = new ArrayList<>();
            while (true) {
                System.out.print("  Requirement: ");
                String req = InputHelper.getString();
                if (req.equalsIgnoreCase("done")) {
                    break;
                }
                if (! req.isEmpty()) {
                    requirements.add(req);
                }
            }

            // Create job with recruiterId from session
            Job job = new Job(title, description, requirements, session.getUserId());

            System.out.println("\n📤 Creating job posting...");

            String jobId = jobService.createJob(job);

            System.out.println("✅ Job posted successfully!");
            System.out.println("   Job ID: " + jobId);
            System.out.println("   Title: " + title);
            System.out.println("   Requirements: " + requirements.size());

        } catch (Exception e) {
            System.err.println("❌ Failed to create job posting: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewMyJobPostings() {
        try {
            System.out. println("=== MY JOB POSTINGS ===\n");

            System.out.println("📤 Fetching your job postings...");

            // Only get jobs posted by this recruiter
            List<Job> jobs = jobService.getJobsByRecruiterId(session.getUserId());

            if (jobs.isEmpty()) {
                System.out.println("⚠️  You haven't posted any jobs yet!");
            } else {
                System.out.println("✅ You have " + jobs.size() + " job posting(s):\n");
                for (int i = 0; i < jobs.size(); i++) {
                    Job job = jobs.get(i);
                    System.out.println("--- Job " + (i + 1) + " ---");
                    displayJob(job);
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error fetching job postings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewAllApplications() {
        try {
            System.out.println("=== ALL APPLICATIONS ===\n");

            System.out.println("📤 Fetching applications...");

            List<Application> applications = applicationService. getAllApplications();

            if (applications.isEmpty()) {
                System.out.println("⚠️  No applications found!");
            } else {
                System.out.println("✅ Found " + applications.size() + " application(s):\n");
                for (int i = 0; i < applications.size(); i++) {
                    Application app = applications.get(i);
                    System.out.println("--- Application " + (i + 1) + " ---");
                    displayApplication(app);
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error fetching applications: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void reviewApplication() {
        try {
            System.out.println("=== REVIEW APPLICATION ===\n");

            System.out.print("Application ID: ");
            String applicationId = InputHelper.getString();

            System.out.println("\n📤 Fetching application.. .");
            Application application = applicationService.getApplicationById(applicationId);

            if (application == null) {
                System.out.println("❌ Application not found!");
                return;
            }

            System.out.println("✅ Application found:\n");
            displayApplication(application);

            System.out.println("\nUpdate Status:");
            System.out.println("1. Approve (ACCEPTED)");
            System.out. println("2. Reject (REJECTED)");
            System.out.println("3. Under Review (UNDER_REVIEW)");
            System.out.println("0. Cancel");
            System.out.print("Choice: ");

            int choice = InputHelper.getInt();
            String newStatus = null;

            switch (choice) {
                case 1:
                    newStatus = "ACCEPTED";
                    break;
                case 2:
                    newStatus = "REJECTED";
                    break;
                case 3:
                    newStatus = "UNDER_REVIEW";
                    break;
                case 0:
                    System.out.println("⚠️  Review cancelled");
                    return;
                default:
                    System.out. println("❌ Invalid choice!");
                    return;
            }

            System.out. println("\n📤 Updating application status...");
            boolean updated = applicationService.updateApplicationStatus(applicationId, newStatus);

            if (updated) {
                System.out.println("✅ Application status updated to: " + newStatus);
            } else {
                System.out.println("❌ Failed to update application status!");
            }

        } catch (Exception e) {
            System.err. println("❌ Error reviewing application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeJobPosting() {
        try {
            System.out.println("=== CLOSE JOB POSTING ===\n");

            // First, show recruiter's jobs
            System.out.println("📤 Fetching your job postings...");
            List<Job> jobs = jobService. getJobsByRecruiterId(session.getUserId());

            if (jobs.isEmpty()) {
                System.out.println("⚠️  You haven't posted any jobs yet!");
                return;
            }

            System.out.println("Your open job postings:\n");
            int openJobCount = 0;
            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);
                if ("OPEN".equals(job.getStatus())) {
                    System.out.println((i + 1) + ". " + job.getTitle() + " (ID: " + job.getJobId() + ")");
                    openJobCount++;
                }
            }

            if (openJobCount == 0) {
                System.out.println("⚠️  You have no open job postings to close!");
                return;
            }

            System.out.print("\nJob ID to close: ");
            String jobId = InputHelper.getString();

            // Verify the job belongs to this recruiter
            Job jobToClose = jobService.getJobById(jobId);
            if (jobToClose == null) {
                System.out.println("❌ Job not found!");
                return;
            }

            if (!jobToClose.getRecruiterId().equals(session.getUserId())) {
                System.out.println("❌ You can only close your own job postings!");
                return;
            }

            if (InputHelper.confirm("Are you sure you want to close this job posting?")) {
                System.out.println("\n📤 Closing job.. .");
                boolean closed = jobService.closeJob(jobId);

                if (closed) {
                    System.out.println("✅ Job posting closed successfully!");
                } else {
                    System.out.println("❌ Failed to close job posting!");
                }
            } else {
                System.out. println("⚠️  Cancelled");
            }

        } catch (Exception e) {
            System.err.println("❌ Error closing job:  " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayJob(Job job) {
        System.out.println("Job ID:      " + job.getJobId());
        System.out.println("Title:       " + job.getTitle());
        System.out.println("Description: " + job.getDescription());
        System.out.println("Status:      " + job.getStatus());
        System.out.println("Posted:      " + job.getPostedDate());
        System.out.println("Requirements:");
        if (job.getRequirements() != null && !job.getRequirements().isEmpty()) {
            for (String req : job.getRequirements()) {
                System.out. println("  • " + req);
            }
        } else {
            System.out. println("  (None)");
        }
    }

    private void displayApplication(Application app) {
        System.out. println("Application ID: " + app.getApplicationId());
        System.out.println("Job ID:          " + app.getJobId());
        System.out.println("Applicant ID:   " + app.getApplicantId());
        System.out.println("Status:         " + app. getStatus());
        System.out.println("Applied Date:   " + app.getApplicationDate());
        if (app.getCoverLetter() != null && !app.getCoverLetter().isEmpty()) {
            System.out.println("Cover Letter:   " + app.getCoverLetter());
        }
    }
}
