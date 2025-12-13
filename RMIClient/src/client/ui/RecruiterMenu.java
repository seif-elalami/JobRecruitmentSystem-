package client.ui;

import shared.interfaces.ICandidateView;
import client.RMIClient;
import client.utils.InputHelper;
import shared.interfaces.IJobService;
import shared.interfaces.IApplicationService;
import shared.interfaces.IRecruiterService;
import shared.interfaces.IAuthService;
import shared.models.Job;
import shared.models.Application;
import shared.models.Session;
import shared.models.Interview;
import shared.models.Applicant;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecruiterMenu {

    private Session session;
    private IJobService jobService;
    private IApplicationService applicationService;
    private IRecruiterService recruiterService;
    private IAuthService authService;

    public RecruiterMenu(RMIClient client, Session session) {
        this.session = session;
        this.jobService = client.getJobService();
        this.applicationService = client.getApplicationService();
        this.recruiterService = client.getRecruiterService();
        this.authService = client.getAuthService();
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
            // Recruiter Profile
            case 1:
                viewMyProfile();
                break;
            case 2:
                updateMyProfile();  // ✅ Includes optional password update
                break;
            case 3:
                changePasswordSecurely();  // ✅ Secure password change with verification
                break;

            // Job Management
            case 4:
                createJobPosting();
                break;
            case 5:
                viewMyJobPostings();
                break;
            case 6:
                closeJobPosting();
                break;

            // Application Management
            case 7:
                viewAllApplications();
                break;
            case 8:
                viewApplicationsForSpecificJob();
                break;
            case 9:
                reviewApplication();
                break;

            // Candidate Matching (Read-Only)
            case 10:
                matchCandidatesToJob();
                break;
            case 11:
                viewCandidateDetails();
                break;
            case 12:
                searchCandidatesBySkillsReadOnly();
                break;
            case 13:
                searchByExperienceLevel();
                break;


            // Interview Management
            case 14:
                scheduleInterview();
                break;
            case 15:
                viewMyInterviews();
                break;
            case 16:
                viewInterviewDetails();
                break;
            case 17:
                updateInterview();
                break;
            case 18:
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
    System.out.println("  1.  View My Profile");
    System.out.println("  2.  Update Profile (Phone, Company, Dept, Password)");
    System.out.println("  3.  Change Password Securely");
    System.out.println();

    System.out.println("📋 Job Management:");
    System.out.println("  4.  Create Job Posting");
    System.out.println("  5.  View My Job Postings");
    System.out.println("  6.  Close Job Posting");
    System.out.println();

    System.out.println("📝 Application Management:");
    System.out.println("  7.  View All Applications");
    System.out.println("  8.  View Applications for Specific Job");
    System.out.println("  9.  Review Application (Accept/Reject)");
    System.out.println();

    System.out.println("🔍 Candidate Matching & Search (Read-Only):");
    System.out.println("  10. Match Candidates to Job (View CVs)");
    System.out.println("  11. View Candidate Details");
    System.out.println("  12. Search Candidates by Skills");
    System.out.println("  13. Search by Experience Level");
    System.out.println();



    System.out.println("📅 Interview Management:");
    System.out.println("  14. Schedule Interview");
    System.out.println("  15. View My Interviews");
    System.out.println("  16. View Interview Details");
    System.out.println("  17. Update Interview");
    System.out.println("  18. Cancel Interview");
    System.out.println();

    System.out.println("  0.   Logout");
    System.out.print("\nChoice: ");
}


    private void createJobPosting() {
        try {
            System.out.println("=== CREATE JOB POSTING ===\n");

            // Title
            System.out.print("Job Title: ");
            String title = InputHelper.getString();
            if (title.trim().isEmpty()) {
                System.out.println("❌ Job title cannot be empty!");
                return;
            }

            // Description
            System.out.print("Job Description: ");
            String description = InputHelper.getString();
            if (description.trim().isEmpty()) {
                System.out.println("❌ Job description cannot be empty!");
                return;
            }

            // Company
            System.out.print("Company Name: ");
            String company = InputHelper.getString();
            if (company.trim().isEmpty()) {
                System.out.println("❌ Company name cannot be empty!");
                return;
            }

            // Location
            System.out.print("Job Location: ");
            String location = InputHelper.getString();
            if (location.trim().isEmpty()) {
                System.out.println("❌ Job location cannot be empty!");
                return;
            }

            // Salary
            System.out.print("Salary (annual): ");
            double salary = InputHelper.getDouble();
            if (salary <= 0) {
                System.out.println("❌ Salary must be a positive number!");
                return;
            }

            // Requirements
            System.out.println("Requirements (enter one per line, type 'done' when finished):");
            List<String> requirements = new ArrayList<>();
            while (true) {
                System.out.print("  Requirement: ");
                String req = InputHelper.getString();
                if (req.equalsIgnoreCase("done")) {
                    break;
                }
                if (!req.isEmpty()) {
                    requirements.add(req);
                }
            }

            if (requirements.isEmpty()) {
                System.out.println("❌ At least one requirement is needed!");
                return;
            }

            // Create job with all fields
            Job job = new Job(title, description, requirements, session.getUserId());
            job.setCompany(company);
            job.setLocation(location);
            job.setSalary(salary);

            System.out.println("\n📤 Creating job posting...");

            String jobId = jobService.createJob(job);

            System.out.println("✅ Job posted successfully!");
            System.out.println("   Job ID: " + jobId);
            System.out.println("   Title: " + title);
            System.out.println("   Company: " + company);
            System.out.println("   Location: " + location);
            System.out.println("   Salary: $" + String.format("%,.2f", salary));
            System.out.println("   Requirements: " + requirements.size());

        } catch (Exception e) {
            System.err.println("❌ Failed to create job posting: " + e.getMessage());
        }
    }

    private void viewMyJobPostings() {
        try {
            System.out.println("=== MY JOB POSTINGS ===\n");

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

            List<Application> applications = applicationService.getAllApplications();

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

            System.out.print("Application ID:  ");
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
            System.out.println("1.  Approve (ACCEPTED)");
            System.out.println("2. Reject (REJECTED)");
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
                    System.out.println("❌ Invalid choice!");
                    return;
            }

            System.out.println("\n📤 Updating application status...");
            boolean updated = applicationService.updateApplicationStatus(applicationId, newStatus);

            if (updated) {
                System.out.println("✅ Application status updated to: " + newStatus);
            } else {
                System.out.println("❌ Failed to update application status!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error reviewing application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeJobPosting() {
        try {
            System.out.println("=== CLOSE JOB POSTING ===\n");

            // First, show recruiter's jobs
            System.out.println("📤 Fetching your job postings...");
            List<Job> jobs = jobService.getJobsByRecruiterId(session.getUserId());

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

            if (! jobToClose.getRecruiterId().equals(session.getUserId())) {
                System.out.println("❌ You can only close your own job postings!");
                return;
            }

            if (InputHelper.confirm("Are you sure you want to close this job posting? ")) {
                System.out.println("\n📤 Closing job...");
                boolean closed = jobService.closeJob(jobId);

                if (closed) {
                    System.out.println("✅ Job posting closed successfully!");
                } else {
                    System.out.println("❌ Failed to close job posting!");
                }
            } else {
                System.out.println("⚠️  Cancelled");
            }

        } catch (Exception e) {
            System.err.println("❌ Error closing job: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========================================
    // ✅ MATCH CV FEATURE (READ-ONLY VIEWS)
    // ========================================

    /**
     * Match candidates to a specific job - View all CVs of applicants
     */
    private void matchCandidatesToJob() {
        try {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║     Match Candidates to Job (CVs)      ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.print("Enter Job ID: ");
            String jobId = InputHelper.getString();

            System.out.println("\n📤 Fetching candidates who applied to this job...");

            // Get read-only candidate views
            List<ICandidateView> candidates = recruiterService.getCandidatesForJob(jobId);

            if (candidates.isEmpty()) {
                System.out.println("⚠️  No candidates have applied to this job yet!");
                System.out.println("\n💡 Tip:  Candidates will appear here after they apply.");
            } else {
                System.out.println("✅ Found " + candidates.size() + " candidate(s):\n");

                for (int i = 0; i < candidates.size(); i++) {
                    ICandidateView cv = candidates.get(i);

                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("📄 Candidate " + (i + 1) + ":");
                    displayCandidateCV(cv);
                    System. out.println();
                }

                // Offer to schedule interview
                if (InputHelper.confirm("\n💡 Would you like to schedule an interview with a candidate?")) {
                    System.out.print("Enter Candidate ID: ");
                    String candidateId = InputHelper.getString();
                    scheduleInterviewForCandidate(jobId, candidateId);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error matching candidates:  " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * View detailed CV of a specific candidate
     */
    private void viewCandidateDetails() {
        try {
            System. out.println("\n╔════════════════════════════════════════╗");
            System. out.println("║       View Candidate Details           ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System. out.print("Enter Candidate ID: ");
            String candidateId = InputHelper.getString();

            System.out.println("\n📤 Fetching candidate details.. .");

            // Get read-only candidate view
            ICandidateView candidate = recruiterService.getCandidateById(candidateId);

            if (candidate == null) {
                System.out.println("❌ Candidate not found!");
            } else {
                System.out.println("✅ Candidate found:\n");
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                displayCandidateCV(candidate);
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

                // Cannot modify - read-only!
                // candidate.setName("Hacker"); // ← This would be a COMPILE ERROR!
            }

        } catch (Exception e) {
            System.err.println("❌ Error viewing candidate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Search candidates by skills (read-only views)
     */
    private void searchCandidatesBySkillsReadOnly() {
        try {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║     Search Candidates by Skills        ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.print("Enter skills to search for (e.g., 'Java, RMI, MongoDB'): ");
            String skills = InputHelper.getString();

            System.out.println("\n🔍 Searching for candidates with skills: " + skills + "...");

            // Get read-only candidate views
            List<ICandidateView> candidates = recruiterService.searchCandidatesBySkillsReadOnly(skills);

            if (candidates.isEmpty()) {
                System.out.println("❌ No candidates found with skills: " + skills);
                System.out.println("\n💡 Try searching with different or fewer skills.");
            } else {
                System.out.println("✅ Found " + candidates.size() + " matching candidate(s):\n");

                for (int i = 0; i < candidates.size(); i++) {
                    ICandidateView cv = candidates.get(i);

                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("📄 Candidate " + (i + 1) + ":");
                    displayCandidateCV(cv);
                    System.out. println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error searching candidates:  " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Search candidates by minimum experience level
     */
    private void searchByExperienceLevel() {
        try {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   Search by Experience Level           ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.print("Enter minimum years of experience required: ");
            int minYears = InputHelper.getInt();

            System.out.println("\n🔍 Searching for candidates with at least " + minYears + " years of experience...");

            // Get read-only candidate views
            List<ICandidateView> candidates = recruiterService.searchCandidatesByMinExperience(minYears);

            if (candidates.isEmpty()) {
                System.out.println("❌ No candidates found with " + minYears + "+ years of experience.");
            } else {
                System.out.println("✅ Found " + candidates.size() + " qualified candidate(s):\n");

                for (int i = 0; i < candidates.size(); i++) {
                    ICandidateView cv = candidates.get(i);

                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    System.out.println("📄 Candidate " + (i + 1) + ":");
                    displayCandidateCV(cv);
                    System.out. println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error searching candidates:  " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========================================
    // INTERVIEW MANAGEMENT
    // ========================================
private void scheduleInterview() {
    try {
        System.out.println("=== SCHEDULE INTERVIEW ===\n");

        System.out.print("Job ID: ");
        String jobId = InputHelper.getString();

        // Job ID validation
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            System.out.println("❌ Error: This job does not exist. Please enter a valid Job ID.");
            return;
        }

        System.out.print("Applicant (Candidate) ID: ");
        String applicantId = InputHelper.getString();

        // Candidate ID validation
        ICandidateView candidate = recruiterService.getCandidateById(applicantId);
        if (candidate == null) {
            System.out.println("❌ Error: This candidate does not exist. Please enter a valid Candidate ID.");
            return;
        }

        System.out.print("Interview Date (DD/MM/YYYY): ");
        String dateStr = InputHelper.getString();

        System.out.print("Interview Time (HH:MM, 24-hour format): ");
        String timeStr = InputHelper.getString();

        // Parse date and time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date scheduledDate = sdf.parse(dateStr + " " + timeStr);

        // Optional: check that the interview is not set in the past
        if (scheduledDate.before(new Date())) {
            System.out.println("❌ Error: Interview date and time must be in the future.");
            return;
        }

        System.out.print("Location (e.g., 'Online - Zoom' or 'Office - Room 301'): ");
        String location = InputHelper.getString();

        System.out.print("Notes [Optional, press Enter to skip]: ");
        String notes = InputHelper.getString();

        // Create interview
        Interview interview = new Interview(jobId, applicantId, session.getUserId(), scheduledDate, location);
        if (!notes.isEmpty()) {
            interview.setNotes(notes);
        }

        System.out.println("\n📤 Scheduling interview...");
        String interviewId = recruiterService.createInterview(interview);

        System.out.println("✅ Interview scheduled successfully!");
        System.out.println("   Interview ID: " + interviewId);
        System.out.println("   Date & Time: " + sdf.format(scheduledDate));
        System.out.println("   Location: " + location);
        System.out.println("   Status:  SCHEDULED");

    } catch (Exception e) {
        System.err.println("❌ Error scheduling interview: " + e.getMessage());
        e.printStackTrace();
    }
}


    /**
     * Helper method to schedule interview directly from candidate matching
     */
    private void scheduleInterviewForCandidate(String jobId, String candidateId) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            System.out.println("\n📅 Scheduling Interview");
            System.out.println("   Job ID:        " + jobId);
            System.out.println("   Candidate ID: " + candidateId);

            System.out.print("\nInterview Date (DD/MM/YYYY): ");
            String dateStr = InputHelper.getString();

            System.out.print("Interview Time (HH:MM): ");
            String timeStr = InputHelper.getString();

            Date scheduledDate = sdf.parse(dateStr + " " + timeStr);

            System.out. print("Location (e.g., 'Online - Zoom' or 'Office - Room 301'): ");
            String location = InputHelper.getString();

            System. out.print("Notes [Optional]:  ");
            String notes = InputHelper.getString();

            Interview interview = new Interview(jobId, candidateId, session.getUserId(), scheduledDate, location);
            if (!notes.isEmpty()) {
                interview.setNotes(notes);
            }

            System.out.println("\n📤 Scheduling interview...");
            String interviewId = recruiterService.createInterview(interview);

            System.out.println("✅ Interview scheduled successfully!");
            System.out.println("   Interview ID: " + interviewId);

        } catch (Exception e) {
            System.err.println("❌ Error scheduling interview: " + e.getMessage());
        }
    }

    private void viewMyInterviews() {
        try {
            System.out.println("=== MY INTERVIEWS ===\n");

            System.out.println("📤 Fetching your interviews...");
            List<Interview> interviews = recruiterService.getMyInterviews(session.getUserId());

            if (interviews.isEmpty()) {
                System.out.println("⚠️  You haven't scheduled any interviews yet!");
            } else {
                System.out.println("✅ You have " + interviews.size() + " interview(s):\n");

                for (int i = 0; i < interviews.size(); i++) {
                    Interview interview = interviews.get(i);
                    System.out.println("--- Interview " + (i + 1) + " ---");
                    displayInterview(interview);
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error fetching interviews: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateInterview() {
        try {
            System.out.println("=== UPDATE INTERVIEW ===\n");

            System.out.print("Enter Interview ID: ");
            String interviewId = InputHelper.getString();

            System.out.println("\n📤 Fetching interview details...");
            Interview interview = recruiterService.getInterviewById(interviewId);

            if (interview == null) {
                System.out.println("❌ Interview not found!");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            System.out.println("✅ Current interview details loaded.");
            System.out.println("Update fields (press Enter to skip):\n");

            // Update date
            System.out.print("New Date (DD/MM/YYYY) [Current: " + sdf.format(interview.getScheduledDate()) + "]: ");
            String newDateStr = InputHelper.getString();

            System.out.print("New Time (HH:MM) [Current: " + sdf.format(interview.getScheduledDate()) + "]: ");
            String newTimeStr = InputHelper.getString();

            if (!newDateStr.isEmpty() && !newTimeStr.isEmpty()) {
                Date newDate = sdf.parse(newDateStr + " " + newTimeStr);
                interview.setScheduledDate(newDate);
            }

            // Update location
            System.out.print("Location [Current: " + interview.getLocation() + "]: ");
            String newLocation = InputHelper.getString();
            if (!newLocation.isEmpty()) {
                interview.setLocation(newLocation);
            }

            // Update notes
            System.out.print("Notes [Current: " + (interview.getNotes() != null ? interview.getNotes() : "None") + "]: ");
            String newNotes = InputHelper.getString();
            if (!newNotes.isEmpty()) {
                interview. setNotes(newNotes);
            }

            System.out.println("\n📤 Updating interview...");
            boolean updated = recruiterService.updateInterview(interview);

            if (updated) {
                System.out.println("✅ Interview updated successfully!");
            } else {
                System.out.println("❌ Update failed!");
            }

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
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.print("Enter experience to search for (e.g., '5 years', '3'): ");
        String experience = InputHelper.getString();

        System.out.println("\n🔍 Searching applicants with experience: " + experience + "...");

        List<Applicant> applicants = recruiterService.searchApplicantsByExperience(experience);

        if (applicants.isEmpty()) {
            System.out.println("\n⚠️  No applicants found with that experience!");
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


private void viewInterviewDetails() {
    try {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       View Interview Details           ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.print("Enter Interview ID: ");
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
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         Update My Profile              ║");
        System.out.println("╚════════════════════════════════════════╝");

        shared.models.Recruiter recruiter = recruiterService.getRecruiterById(session.getUserId());

        if (recruiter == null) {
            System.out.println("❌ Profile not found!");
            return;
        }

        System.out.println("\nCurrent Profile:");
        System.out.println("  Name:        " + recruiter.getName());
        System.out.println("  Email:      " + recruiter.getEmail());
        System.out.println("  Phone:      " + (recruiter.getPhone() != null ? recruiter.getPhone() : "Not set"));
        System.out.println("  Company:    " + (recruiter.getCompany() != null ? recruiter.getCompany() : "Not set"));
        System.out.println("  Department: " + (recruiter.getDepartment() != null ? recruiter.getDepartment() : "Not set"));
        System.out.println("  Position:   " + (recruiter.getPosition() != null ? recruiter.getPosition() : "Not set"));

        System.out.println("\n--- Enter new details (press Enter to keep current) ---\n");

        System.out.print("New Phone: ");
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

        System.out.print("New Position: ");
        String position = InputHelper.getString();
        if (!position.isEmpty()) {
            recruiter.setPosition(position);
        }

        // ✅ NEW: Add password update option
        System.out.println("\n--- Password Update (Optional) ---");
        System.out.print("Do you want to change your password? (y/n): ");
        String changePassword = InputHelper.getString().toLowerCase();

        if (changePassword.equals("y")) {
            System.out.print("Enter new password (min 6 characters): ");
            String newPassword = InputHelper.getString();

            if (newPassword.isEmpty()) {
                System.out.println("⚠️  Password not changed (empty input)");
                recruiter.setPassword(null);
            } else if (newPassword.length() < 6) {
                System.out.println("⚠️  Password not changed (too short - min 6 characters)");
                recruiter.setPassword(null);
            } else {
                System.out.print("Confirm new password: ");
                String confirmPassword = InputHelper.getString();

                if (!newPassword.equals(confirmPassword)) {
                    System.out.println("⚠️  Passwords don't match! Password not changed.");
                    recruiter.setPassword(null);
                } else {
                    recruiter.setPassword(newPassword);
                    System.out.println("✅ Password will be updated");
                }
            }
        } else {
            // Don't update password
            recruiter. setPassword(null);
        }

        System.out.println("\n📤 Updating profile.. .");

        boolean success = recruiterService.updateRecruiter(recruiter);

        if (success) {
            System.out.println("✅ Profile updated successfully!");

            // If password was changed, recommend re-login
            if (recruiter.getPassword() != null && ! recruiter.getPassword().isEmpty()) {
                System.out.println("\n💡 Password changed! Please logout and login again for security.");
            }
        } else {
            System.out.println("❌ Failed to update profile!");
        }

    } catch (Exception e) {
        System.err.println("❌ Error updating profile: " + e.getMessage());
        e.printStackTrace();
    }
}

private void changePasswordSecurely() {
    try {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out. println("║      Change Password Securely          ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\n🔒 This method verifies your current password");
        System.out.println("   for added security.\n");

        // ✅ STEP 1:  Verify current password
        System.out.print("Enter your CURRENT password: ");
        String currentPassword = InputHelper.getString();

        System.out.println("\n🔍 Verifying current password...");

        try {
            // Verify by attempting login
            authService.login(session. getUserEmail(), currentPassword);
            System. out.println("✅ Current password verified!\n");
        } catch (Exception e) {
            System.out.println("❌ Current password is incorrect!");
            System.out.println("   Access denied for security reasons.");
            return;
        }

        // ✅ STEP 2: Get new password
        System.out. print("Enter NEW password (min 6 characters): ");
        String newPassword = InputHelper.getString();

        // Validate length
        if (newPassword.length() < 6) {
            System.out.println("❌ Password must be at least 6 characters!");
            return;
        }

        // Check if same as current
        if (newPassword. equals(currentPassword)) {
            System.out.println("⚠️  New password is the same as current password!");
            System.out.print("Continue anyway? (y/n): ");
            String confirm = InputHelper.getString().toLowerCase();
            if (!confirm.equals("y")) {
                System.out.println("❌ Password change cancelled.");
                return;
            }
        }

        // ✅ STEP 3: Confirm new password
        System.out. print("CONFIRM new password: ");
        String confirmPassword = InputHelper.getString();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("❌ Passwords don't match!  Please try again.");
            return;
        }

        // ✅ STEP 4: Update password
        shared.models. Recruiter recruiter = recruiterService.getRecruiterById(session.getUserId());

        if (recruiter == null) {
            System.out.println("❌ Profile not found!");
            return;
        }

        recruiter.setPassword(newPassword);

        System.out.println("\n📤 Updating password...");

        boolean success = recruiterService.updateRecruiter(recruiter);

        if (success) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   ✅ PASSWORD CHANGED SUCCESSFULLY!    ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("\n🔐 Your password has been securely updated.");
            System.out.println("💡 Please logout and login again with your");
            System.out.println("   new password for security.");
            System.out.println("\n📝 Password requirements met:");
            System.out.println("   ✅ Minimum 6 characters");
            System.out. println("   ✅ Confirmed correctly");
            System.out.println("   ✅ Current password verified");
            System. out.println("   ✅ Encrypted with BCrypt");
        } else {
            System. out.println("❌ Failed to change password!");
            System.out.println("   Please try again or contact support.");
        }

    } catch (Exception e) {
        System.err.println("❌ Error changing password: " + e.getMessage());
        e.printStackTrace();
    }
}





    // DISPLAY HELPER METHODS


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
