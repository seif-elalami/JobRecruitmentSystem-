package client.ui;

import shared.interfaces.ICandidateView;
import client.RMIClient;
import client. utils.InputHelper;
import shared.interfaces.IJobService;
import shared.interfaces. IApplicationService;
import shared.interfaces. IRecruiterService;
import shared. models.Job;
import shared.models.Application;
import shared.models.Session;
import shared.models.Interview;
import shared.models. Applicant;

import java.util.List;

public class RecruiterMenu {

    private RMIClient client;
    private Session session;
    private IJobService jobService;
    private IApplicationService applicationService;
    private IRecruiterService recruiterService;

    public RecruiterMenu(RMIClient client, Session session) {
        this.client = client;
        this.session = session;
        this.jobService = client.getJobService();
        this.applicationService = client.getApplicationService();
        this.recruiterService = client.getRecruiterService();
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
            // Recruiter Profile
            case 1:
                viewMyProfile();
                break;
            case 2:
                updateMyProfile();
                break;

            // Job Management
            case 3:
                createJobPosting();
                break;
            case 4:
                viewMyJobPostings();
                break;
            case 5:
                closeJobPosting();
                break;

            // Application Management
            case 6:
                viewAllApplications();
                break;
            case 7:
                viewApplicationsForSpecificJob();
                break;
            case 8:
                reviewApplication();
                break;

            // Candidate Matching (Read-Only)
            case 9:
                matchCandidatesToJob();
                break;
            case 10:
                viewCandidateDetails();
                break;
            case 11:
                searchCandidatesBySkillsReadOnly();
                break;
            case 12:
                searchByExperienceLevel();
                break;

            // Applicant Search (Full Access)
            case 13:
                searchApplicantsBySkills();
                break;
            case 14:
                searchApplicantsByExperience();
                break;

            // Interview Management
            case 15:
                scheduleInterview();
                break;
            case 16:
                viewMyInterviews();
                break;
            case 17:
                viewInterviewDetails();
                break;
            case 18:
                updateInterview();
                break;
            case 19:
                cancelInterview();
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
    System.out.println("║       Recruiter Menu                   ║");
    System.out.println("╚════════════════════════════════════════╝");
    System.out.println("Welcome, " + session.getUserEmail() + " (Recruiter)");
    System.out.println();

    System.out.println("👤 Recruiter Profile:");
    System.out.println("  1.   View My Profile");
    System.out.println("  2.  Update My Profile");
    System.out.println();

    System.out.println("📋 Job Management:");
    System.out.println("  3.  Create Job Posting");
    System.out.println("  4.  View My Job Postings");
    System.out.println("  5.  Close Job Posting");
    System.out.println();

    System.out.println("📝 Application Management:");
    System.out.println("  6.  View All Applications");
    System.out.println("  7.  View Applications for Specific Job");
    System.out.println("  8.  Review Application (Accept/Reject)");
    System.out.println();

    System.out.println("🔍 Candidate Matching & Search (Read-Only):");
    System.out.println("  9.  Match Candidates to Job (View CVs)");
    System.out.println("  10. View Candidate Details");
    System.out.println("  11. Search Candidates by Skills");
    System.out.println("  12. Search by Experience Level");
    System.out.println();

    System.out.println("👥 Applicant Search (Full Access):");
    System.out.println("  13. Search Applicants by Skills");
    System.out.println("  14. Search Applicants by Experience");
    System.out.println();

    System.out.println("📅 Interview Management:");
    System.out.println("  15. Schedule Interview");
    System.out.println("  16. View My Interviews");
    System.out.println("  17. View Interview Details");
    System.out.println("  18. Update Interview");
    System.out.println("  19. Cancel Interview");
    System.out.println();

    System.out.println("  0.  Logout");
    System.out.print("\nChoice: ");
}

    // ========================================
    // JOB MANAGEMENT
    // ========================================

    private void createJobPosting() {
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
            System.err.println("❌ Error updating interview: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cancelInterview() {
        try {
            System.out.println("=== CANCEL INTERVIEW ===\n");

            System.out.print("Enter Interview ID: ");
            String interviewId = InputHelper.getString();

            if (InputHelper.confirm("Are you sure you want to cancel this interview?")) {
                System.out.println("\n📤 Cancelling interview...");
                boolean cancelled = recruiterService.cancelInterview(interviewId);

                if (cancelled) {
                    System.out.println("✅ Interview cancelled successfully!");
                } else {
                    System.out.println("❌ Cancellation failed!");
                }
            } else {
                System.out.println("⚠️  Cancelled");
            }

        } catch (Exception e) {
            System.err.println("❌ Error cancelling interview:  " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewMyProfile() {
        try {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║         My Recruiter Profile           ║");
            System.out.println("╚════════════════════════════════════════╝");

            System.out.println("\n📤 Fetching profile.. .");

            shared.models.Recruiter recruiter = recruiterService.getRecruiterById(session.getUserId());

            if (recruiter != null) {
                System.out.println("\n✅ Profile found:\n");
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.println("   🆔 ID:           " + recruiter.getId());
                System.out.println("   👤 Name:        " + recruiter.getName());
                System.out.println("   📧 Email:       " + recruiter.getEmail());
                System.out.println(
                        "   📱 Phone:       " + (recruiter.getPhone() != null ? recruiter.getPhone() : "Not set"));
                System.out.println("   🏢 Company:     "
                        + (recruiter.getCompany() != null ? recruiter.getCompany() : "Not specified"));
                System.out.println("   🏛️  Department:  "
                        + (recruiter.getDepartment() != null ? recruiter.getDepartment() : "Not specified"));
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            } else {
                System.out.println("⚠️  Profile not found!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error fetching profile: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void viewApplicationsForSpecificJob() {
        try {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║  View Applications for Specific Job    ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.print("Enter Job ID: ");
            String jobId = InputHelper.getString();

            System.out.println("\n📤 Fetching applications for job:  " + jobId + "...");

            List<Application> applications = recruiterService.getApplicationsForJob(jobId);

            if (applications.isEmpty()) {
                System.out.println("\n⚠️  No applications found for this job!");
            } else {
                System.out.println("\n✅ Found " + applications.size() + " application(s):\n");

                for (int i = 0; i < applications.size(); i++) {
                    Application app = applications.get(i);
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("📄 Application " + (i + 1) + ":");
                    displayApplication(app);
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error fetching applications: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchApplicantsBySkills() {
        try {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   Search Applicants by Skills          ║");
            System.out.println("║        (Full Access)                   ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.print("Enter skills to search for (e.g., 'Java, Python'): ");
            String skills = InputHelper.getString();

            System.out.println("\n🔍 Searching applicants with skills: " + skills + "...");

            List<Applicant> applicants = recruiterService.searchApplicantsBySkills(skills);

            if (applicants.isEmpty()) {
                System.out.println("\n⚠️  No applicants found with those skills!");
            } else {
                System.out.println("\n✅ Found " + applicants.size() + " applicant(s):\n");

                for (int i = 0; i < applicants.size(); i++) {
                    Applicant app = applicants.get(i);
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("📄 Applicant " + (i + 1) + ":");
                    displayApplicant(app);
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error searching applicants: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void searchApplicantsByExperience() {
    try {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ Search Applicants by Experience        ║");
        System.out.println("║        (Full Access)                   ║");
        System.out. println("╚════════════════════════════════════════╝\n");

        System.out.print("Enter experience to search for (e.g., '5 years', '3'): ");
        String experience = InputHelper.getString();

        System.out.println("\n🔍 Searching applicants with experience:  " + experience + "...");

        List<Applicant> applicants = recruiterService.searchApplicantsByExperience(experience);

        if (applicants. isEmpty()) {
            System.out.println("\n⚠️  No applicants found with that experience!");
        } else {
            System.out.println("\n✅ Found " + applicants. size() + " applicant(s):\n");

            for (int i = 0; i < applicants.size(); i++) {
                Applicant app = applicants.get(i);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                System.out.println("📄 Applicant " + (i + 1) + ":");
                displayApplicant(app);
                System.out.println();
            }
        }

    } catch (Exception e) {
        System.err.println("❌ Error searching applicants:  " + e.getMessage());
        e.printStackTrace();
    }
}


private void viewInterviewDetails() {
    try {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       View Interview Details           ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.print("Enter Interview ID:  ");
        String interviewId = InputHelper.getString();

        System.out.println("\n📤 Fetching interview details...");

        Interview interview = recruiterService.getInterviewById(interviewId);

        if (interview != null) {
            System.out.println("\n✅ Interview found:\n");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            displayInterview(interview);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        } else {
            System.out.println("⚠️  Interview not found!");
        }

    } catch (Exception e) {
        System.err.println("❌ Error fetching interview: " + e.getMessage());
        e.printStackTrace();
    }
}

private void updateMyProfile() {
    try {
        System.out. println("\n╔════════════════════════════════════════╗");
        System.out. println("║         Update My Profile              ║");
        System.out.println("╚════════════════════════════════════════╝");

        shared.models.Recruiter recruiter = recruiterService.getRecruiterById(session.getUserId());

        if (recruiter == null) {
            System.out.println("❌ Profile not found!");
            return;
        }

        System.out. println("\nCurrent Profile:");
        System.out.println("  Name:       " + recruiter.getName());
        System.out.println("  Email:      " + recruiter. getEmail());
        System.out.println("  Phone:      " + (recruiter.getPhone() != null ? recruiter.getPhone() : "Not set"));
        System.out.println("  Company:    " + (recruiter. getCompany() != null ? recruiter.getCompany() : "Not set"));
        System.out.println("  Department: " + (recruiter.getDepartment() != null ? recruiter.getDepartment() : "Not set"));

        System.out.println("\n--- Enter new details (press Enter to keep current) ---\n");

        System.out.print("New Phone:  ");
        String phone = InputHelper.getString();
        if (!phone.isEmpty()) {
            recruiter.setPhone(phone);
        }

        System.out.print("New Company: ");
        String company = InputHelper.getString();
        if (!company.isEmpty()) {
            recruiter.setCompany(company);
        }

        System.out.print("New Department: ");
        String department = InputHelper.getString();
        if (!department.isEmpty()) {
            recruiter.setDepartment(department);
        }

        System.out.println("\n📤 Updating profile...");

        boolean success = recruiterService.updateRecruiter(recruiter);

        if (success) {
            System.out.println("✅ Profile updated successfully!");
        } else {
            System.out.println("❌ Failed to update profile!");
        }

    } catch (Exception e) {
        System.err.println("❌ Error updating profile: " + e.getMessage());
        e.printStackTrace();
    }
}
    // ========================================
    // DISPLAY HELPER METHODS
    // ========================================

    private void displayJob(Job job) {
        System.out.println("Job ID:       " + job.getJobId());
        System.out.println("Title:       " + job.getTitle());
        System.out.println("Description: " + job.getDescription());
        System.out.println("Status:      " + job.getStatus());
        System.out.println("Posted:      " + job.getPostedDate());
        System.out. println("Requirements:");
        if (job.getRequirements() != null && !job.getRequirements().isEmpty()) {
            for (String req : job.getRequirements()) {
                System.out.println("  • " + req);
            }
        } else {
            System.out. println("  (None)");
        }
    }

    private void displayApplication(Application app) {
        System.out.println("Application ID:  " + app.getApplicationId());
        System.out.println("Job ID:         " + app.getJobId());
        System.out.println("Applicant ID:   " + app.getApplicantId());
        System.out. println("Status:         " + app.getStatus());
        System.out.println("Applied Date:   " + app.getApplicationDate());
        if (app.getCoverLetter() != null && !app.getCoverLetter().isEmpty()) {
            System.out.println("Cover Letter:   " + app.getCoverLetter());
        }
    }

    private void displayApplicant(Applicant applicant) {
        System.out. println("ID:          " + applicant.getId());
        System.out.println("Name:        " + applicant.getName());
        System.out.println("Email:       " + applicant.getEmail());
        System.out.println("Phone:       " + applicant.getPhone());
        System.out.println("Skills:       " + applicant.getSkills());
        System.out. println("Experience:  " + applicant.getExperience() + " years");
    }

    /**
     * ✅ NEW:  Display candidate CV in read-only format
     */
    private void displayCandidateCV(ICandidateView cv) {
        System.out. println("   🆔 ID:           " + cv.getId());
        System.out.println("   👤 Name:         " + cv.getName());
        System.out.println("   📧 Email:        " + cv. getEmail());
        System.out.println("   📱 Phone:        " + cv.getPhone());
        System.out.println("   🎓 Education:    " + (cv.getEducation() != null ? cv.getEducation() : "Not specified"));
        System.out. println("   💼 Experience:   " + cv.getExperience() + " years");

        System.out.print("   🛠️  Skills:        ");
        if (cv.getSkills() != null && !cv.getSkills().isEmpty()) {
            System.out.println(String.join(", ", cv.getSkills()));
        } else {
            System.out.println("Not specified");
        }

        System.out.println("   📄 Resume:       " + (cv.getResume() != null ? cv.getResume() : "Not uploaded"));
    }

    private void displayInterview(Interview interview) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        System.out.println("Interview ID:    " + interview.getInterviewId());
        System.out.println("Job ID:          " + interview.getJobId());
        System.out.println("Applicant ID:    " + interview. getApplicantId());
        System.out.println("Scheduled:        " + sdf.format(interview.getScheduledDate()));
        System.out.println("Location:        " + interview.getLocation());
        System.out.println("Status:          " + interview. getStatus());
        if (interview.getNotes() != null && !interview.getNotes().isEmpty()) {
            System.out.println("Notes:           " + interview.getNotes());
        }
    }
}
