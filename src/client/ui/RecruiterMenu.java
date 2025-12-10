package client.ui;

import client. RMIClient;
import client.utils.InputHelper;
import shared.models.Session;
import shared.models.Job;
import shared.models.Application;
import shared.models.Applicant;
import shared.interfaces.*;

import java.util.List;

public class RecruiterMenu {

    private RMIClient client;
    private Session session;

    public RecruiterMenu(RMIClient client, Session session) {
        this.client = client;
        this.session = session;
    }

    public void run() {
        try {
            IJobService jobService = client.getJobService();
            IApplicationService appService = client.getApplicationService();
            IApplicantService applicantService = client.getApplicantService();

            boolean logout = false;

            while (!logout) {
                showMenu();
                int choice = InputHelper.getInt();
                System.out.println();

                switch (choice) {
                    case 1:
                        postJob(jobService);
                        break;
                    case 2:
                        viewMyJobs(jobService);
                        break;
                    case 3:
                        viewApplicationsForJob(jobService, appService, applicantService);
                        break;
                    case 4:
                        searchApplicants(applicantService);
                        break;
                    case 5:
                        closeJob(jobService);
                        break;
                    case 0:
                        logout = true;
                        System.out.println("✅ Logged out successfully!");
                        break;
                    default:
                        System. out.println("❌ Invalid choice!");
                }

                if (!logout) InputHelper.pause();
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       Recruiter Dashboard              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out. println("   User: " + session.getEmail());
        System.out.println();
        System.out.println("1. Post New Job");
        System.out. println("2. View My Job Postings");
        System.out.println("3. View Applications for a Job");
        System.out. println("4. Search Applicants");
        System.out.println("5. Close Job Posting");
        System.out. println("0. Logout");
        System.out.print("\nChoice: ");
    }

    private void postJob(IJobService jobService) {
        try {
            System.out. println("=== POST NEW JOB ===\n");

            System.out.print("Job Title: ");
            String title = InputHelper.getString();

            System.out.print("Description: ");
            String description = InputHelper.getString();

            System.out.print("Company Name: ");
            String company = InputHelper.getString();

            System.out.print("Location: ");
            String location = InputHelper.getString();

            System.out.print("Salary:  ");
            double salary = InputHelper.getDouble();

            System.out. print("Requirements: ");
            String requirements = InputHelper.getString();

            Job job = new Job(title, description, company, location, salary, requirements);

            System.out.println("\n📤 Posting job...");
            String jobId = jobService.createJob(job);

            System.out.println("✅ Job posted successfully!");
            System.out.println("   Job ID: " + jobId);
            System.out.println("   Title: " + title);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void viewMyJobs(IJobService jobService) {
        try {
            System.out.println("=== MY JOB POSTINGS ===\n");
            System.out. println("📤 Fetching jobs.. .");

            // In real system, filter by recruiter ID
            // For now, show all jobs
            List<Job> jobs = jobService.getAllJobs();

            if (jobs.isEmpty()) {
                System. out.println("You haven't posted any jobs yet.");
                return;
            }

            System.out.println("✅ Found " + jobs. size() + " job(s)\n");

            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);
                System.out. println("─────────────────────────────────────");
                System.out.println((i + 1) + ". " + job.getTitle());
                System. out.println("   Company: " + job.getCompany());
                System.out.println("   Location: " + job.getLocation());
                System.out.println("   Salary: $" + job.getSalary());
                System.out.println("   Status: " + job.getStatus());
                System.out.println("   Job ID: " + job. getId());
            }
            System.out.println("─────────────────────────────────────");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void viewApplicationsForJob(IJobService jobService, IApplicationService appService, IApplicantService applicantService) {
        try {
            System.out. println("=== VIEW APPLICATIONS ===\n");

            System.out.print("Enter Job ID: ");
            String jobId = InputHelper.getString();

            System.out.println("\n📤 Fetching job.. .");
            Job job = jobService.getJobById(jobId);

            if (job == null) {
                System.out.println("❌ Job not found!");
                return;
            }

            System.out.println("✅ Job: " + job.getTitle());

            System.out.println("\n📤 Fetching applications...");
            List<Application> apps = appService.getApplicationsByJobId(jobId);

            if (apps.isEmpty()) {
                System.out. println("No applications for this job yet.");
                return;
            }

            System.out. println("✅ " + apps.size() + " application(s)\n");

            for (int i = 0; i < apps.size(); i++) {
                Application app = apps.get(i);
                Applicant applicant = applicantService.getApplicantById(app.getApplicantId());

                System.out.println("─────────────────────────────────────");
                System.out.println((i + 1) + ". " + (applicant != null ? applicant.getName() : "Unknown"));
                System.out.println("   Email: " + (applicant != null ? applicant.getEmail() : "N/A"));
                System.out.println("   Experience: " + (applicant != null ? applicant.getExperience() + " years" : "N/A"));
                System.out.println("   Skills: " + (applicant != null ? applicant.getSkills() : "N/A"));
                System.out.println("   Status: " + app.getStatus());
                System.out.println("   Application ID: " + app.getId());
            }
            System.out. println("─────────────────────────────────────");

            // Option to accept/reject
            if (InputHelper.confirm("\nUpdate an application status?")) {
                System.out.print("Enter Application ID: ");
                String appId = InputHelper.getString();

                System.out.println("\n1. Accept");
                System.out.println("2. Reject");
                System.out.print("Choice: ");
                int statusChoice = InputHelper.getInt();

                String newStatus = (statusChoice == 1) ? "ACCEPTED" : "REJECTED";

                System.out. println("\n📤 Updating status...");
                boolean updated = appService.updateApplicationStatus(appId, newStatus);

                System.out.println(updated ? "✅ Status updated to: " + newStatus : "❌ Update failed!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void searchApplicants(IApplicantService applicantService) {
        try {
            System.out. println("=== SEARCH APPLICANTS ===\n");

            System.out.print("Enter skill to search: ");
            String skill = InputHelper.getString();

            System.out.println("\n📤 Searching.. .");
            List<Applicant> applicants = applicantService. searchApplicantsBySkill(skill);

            if (applicants.isEmpty()) {
                System.out. println("No applicants found with that skill.");
                return;
            }

            System.out.println("✅ Found " + applicants.size() + " applicant(s)\n");

            for (int i = 0; i < applicants. size(); i++) {
                Applicant a = applicants.get(i);
                System.out.println("─────────────────────────────────────");
                System.out.println((i + 1) + ". " + a.getName());
                System.out.println("   Email: " + a. getEmail());
                System.out.println("   Phone: " + a.getPhone());
                System.out.println("   Experience: " + a.getExperience() + " years");
                System.out.println("   Skills: " + a.getSkills());
            }
            System.out.println("─────────────────────────────────────");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e. getMessage());
        }
    }

    private void closeJob(IJobService jobService) {
        try {
            System. out.println("=== CLOSE JOB ===\n");

            System.out. print("Enter Job ID:  ");
            String jobId = InputHelper.getString();

            System.out.println("\n📤 Checking job.. .");
            Job job = jobService.getJobById(jobId);

            if (job == null) {
                System.out.println("❌ Job not found!");
                return;
            }

            System.out.println("✅ Job: " + job.getTitle());

            if (job.getStatus().equals("CLOSED")) {
                System.out. println("⚠️ This job is already closed!");
                return;
            }

            if (! InputHelper.confirm("\nClose this job?")) {
                System.out.println("❌ Cancelled.");
                return;
            }

            System.out.println("\n📤 Closing job...");
            boolean closed = jobService.closeJob(jobId);

            System.out.println(closed ? "✅ Job closed!" : "❌ Failed!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
