package client.ui;

import client.RMIClient;
import client. utils.InputHelper;
import shared.models.Session;
import shared.models.Job;
import shared.models. Applicant;
import shared.models.Application;
import shared.interfaces.*;

import java.util.List;

public class ApplicantMenu {

    private RMIClient client;
    private Session session;

    public ApplicantMenu(RMIClient client, Session session) {
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
                System. out.println();

                switch (choice) {
                    case 1:
                        browseJobs(jobService);
                        break;
                    case 2:
                        applyToJob(jobService, appService);
                        break;
                    case 3:
                        viewMyApplications(appService, jobService);
                        break;
                    case 4:
                        updateProfile(applicantService);
                        break;
                    case 5:
                        viewProfile(applicantService);
                        break;
                    case 0:
                        logout = true;
                        System.out.println("✅ Logged out successfully!");
                        break;
                    default:
                        System.out.println("❌ Invalid choice!");
                }

                if (! logout) InputHelper.pause();
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e. getMessage());
            e.printStackTrace();
        }
    }

    private void showMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       Applicant Dashboard              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("   User: " + session.getUserEmail());
        System.out.println();
        System.out.println("1. Browse Jobs");
        System.out.println("2. Apply to Job");
        System.out.println("3. View My Applications");
        System.out.println("4. Update My Profile");
        System.out. println("5. View My Profile");
        System.out. println("0. Logout");
        System.out.print("\nChoice: ");
    }

  private void browseJobs(IJobService jobService) {
    try {
        System.out.println("\n=== BROWSE AVAILABLE JOBS ===\n");

        System.out.println("📤 Fetching available jobs...");

        // Get all open jobs
        List<Job> jobs = jobService.getAllJobs();

        if (jobs.isEmpty()) {
            System.out.println("⚠️  No jobs available at the moment!");
        } else {
            System.out.println("✅ Found " + jobs.size() + " job(s):\n");

            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);

                // Only show OPEN jobs
                if ("OPEN".equals(job.getStatus())) {
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("📋 Job " + (i + 1) + ":");
                    System.out. println("   Job ID:        " + job.getJobId());
                    System. out.println("   Title:        " + job.getTitle());
                    System.out.println("   Description:   " + job.getDescription());
                    System.out.println("   Status:       " + job.getStatus());
                    System.out.println("   Posted:       " + job.getPostedDate());

                    if (job.getRequirements() != null && !job.getRequirements().isEmpty()) {
                        System. out.println("   Requirements:");
                        for (String req : job.getRequirements()) {
                            System.out.println("      • " + req);
                        }
                    }
                    System.out.println();
                }
            }
        }

    } catch (Exception e) {
        System.err.println("❌ Error fetching jobs: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void applyToJob(IJobService jobService, IApplicationService appService) {
        try {
            System.out.println("=== APPLY TO JOB ===\n");

            System.out.print("Enter Job ID: ");
            String jobId = InputHelper.getString();

            // Verify job exists and is open
            System.out.println("\n📤 Checking job.. .");
            Job job = jobService.getJobById(jobId);

            if (job == null) {
                System.out.println("❌ Job not found!");
                return;
            }

            if (!job.getStatus().equals("OPEN")) {
                System.out.println("❌ This job is closed!");
                return;
            }

            System.out.println("✅ Job:  " + job.getTitle() + " at " + job.getCompany());

            System.out.print("\nCover Letter:  ");
            String coverLetter = InputHelper.getString();

            Application application = new Application(
                jobId,
                session.getUserId(),
                coverLetter
            );

            System. out.println("\n📤 Submitting application...");
            String appId = appService.SubmitApplication(application);

            System.out.println("✅ Application submitted successfully!");
            System. out.println("   Application ID: " + appId);

        } catch (Exception e) {
            System.err. println("❌ Error: " + e.getMessage());
        }
    }

    private void viewMyApplications(IApplicationService appService, IJobService jobService) {
        try {
            System.out.println("=== MY APPLICATIONS ===\n");
            System.out.println("📤 Fetching your applications...");

            List<Application> apps = appService.getApplicationsByApplicantId(session.getUserId());

            if (apps. isEmpty()) {
                System.out.println("You haven't applied to any jobs yet.");
                return;
            }

            System.out.println("✅ You have " + apps.size() + " application(s)\n");

            for (int i = 0; i < apps.size(); i++) {
                Application app = apps.get(i);
                Job job = jobService.getJobById(app.getJobId());

                System.out.println("─────────────────────────────────────");
                System. out.println((i + 1) + ". " + (job != null ? job.getTitle() : "Unknown Job"));
                System.out.println("   Company: " + (job != null ? job.getCompany() : "N/A"));
                System.out.println("   Status: " + app.getStatus());
                System. out.println("   Applied: " + app.getApplicationDate());
            }
            System.out.println("─────────────────────────────────────");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

 private void updateProfile(IApplicantService applicantService) {
    try {
        System. out.println("=== UPDATE PROFILE ===\n");

        System.out.println("📤 Fetching your profile...");
        Applicant profile = applicantService.getApplicantById(session.getUserId());

        if (profile == null) {
            System.out.println("❌ Profile not found!");
            return;
        }

        System.out.println("✅ Current profile loaded");
        System.out.println("\nUpdate fields (press Enter to skip):");

        // Phone number with validation
        System.out.print("Phone [" + profile.getPhone() + "]: ");
        System.out.println("   (Must start with 0 and be 11 digits, e.g., 01234567890)");
        String phone = InputHelper.getString();

        if (! phone.isEmpty()) {
            // Validate phone format before setting
            if (phone.matches("^0\\d{10}$")) {
                profile. setPhone(phone);
            } else {
                System.out.println("⚠️  Invalid phone format!  Phone must:");
                System.out.println("   • Start with 0");
                System.out.println("   • Be exactly 11 digits");
                System.out.println("   • Example: 01234567890");
                System.out.println("   Phone not updated.");
            }
        }

        System.out.print("Education [" + profile.getEducation() + "]: ");
        String education = InputHelper.getString();
        if (!education.isEmpty()) profile.setEducation(education);

        if (InputHelper.confirm("\nAdd new skill? ")) {
            System.out. print("Skill: ");
            profile.addSkill(InputHelper.getString());
        }

        System.out.println("\n📤 Updating profile...");

        try {
            boolean updated = applicantService.updateApplicant(profile);

            if (updated) {
                System.out.println("✅ Profile updated successfully!");
            } else {
                System.out.println("❌ Update failed!");
            }
        } catch (Exception e) {
            System.err.println("❌ Update failed: " + e.getMessage());
        }

    } catch (Exception e) {
        System.err.println("❌ Error:  " + e.getMessage());
    }
}

    private void viewProfile(IApplicantService applicantService) {
        try {
            System.out.println("=== MY PROFILE ===\n");

            System.out.println("📤 Fetching your profile.. .");
            Applicant profile = applicantService.getApplicantById(session.getUserId());

            if (profile == null) {
                System.out.println("❌ Profile not found!");
                return;
            }

            System.out.println("✅ Profile:\n");
            System.out. println("  Name: " + profile.getName());
            System.out.println("  Email: " + profile.getEmail());
            System.out.println("  Phone: " + profile.getPhone());
            System.out. println("  Education: " + profile.getEducation());
            System.out.println("  Experience: " + profile.getExperience() + " years");
            System. out.println("  Skills: " + profile.getSkills());

        } catch (Exception e) {
            System.err.println("❌ Error: " + e. getMessage());
        }
    }
}
