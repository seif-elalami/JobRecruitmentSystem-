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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecruiterMenu {

    private Session session;
    private IJobService jobService;
    private IApplicationService applicationService;
    private IRecruiterService recruiterService;

    public RecruiterMenu(RMIClient client, Session session) {
        this.session = session;
        this.jobService = client.getJobService();
        this.applicationService = client.getApplicationService();
        this.recruiterService = client.getRecruiterService();
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
                case 6:  // ✅ NEW - Match CV
                    matchCandidatesToJob();
                    break;
                case 7:  // ✅ NEW - View Candidate Details
                    viewCandidateDetails();
                    break;
                case 8:  // ✅ NEW - Search by Skills (Read-Only)
                    searchCandidatesBySkillsReadOnly();
                    break;
                case 9:  // ✅ NEW - Search by Experience
                    searchByExperienceLevel();
                    break;
                case 10:
                    scheduleInterview();
                    break;
                case 11:
                    viewMyInterviews();
                    break;
                case 12:
                    updateInterview();
                    break;
                case 13:
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
        System.out. println("\n╔════════════════════════════════════════╗");
        System.out. println("║       Recruiter Menu                   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out. println("Welcome, " + session.getUserEmail() + " (Recruiter)");
        System.out.println();
        System.out.println("📋 Job Management:");
        System.out.println("  1. Create Job Posting");
        System.out.println("  2. View My Job Postings");
        System.out.println("  3. View All Applications");
        System.out.println("  4. Review Application");
        System.out.println("  5. Close Job Posting");
        System.out.println();
        System.out.println("🔍 Candidate Matching:");
        System.out.println("  6. Match Candidates to Job (View CVs)");
        System.out.println("  7. View Candidate Details");
        System.out.println("  8. Search Candidates by Skills");
        System.out.println("  9. Search by Experience Level");
        System.out.println();
        System.out.println("📅 Interview Management:");
        System.out.println("  10. Schedule Interview");
        System.out. println("  11. View My Interviews");
        System.out. println("  12. Update Interview");
        System.out.println("  13. Cancel Interview");
        System.out.println();
        System.out.println("  0. Logout");
        System.out.print("\nChoice: ");
    }

    // ========================================
    // JOB MANAGEMENT
    // ========================================

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
                if (!req.isEmpty()) {
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
            System.err. println("❌ Failed to create job posting: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewMyJobPostings() {
        try {
            System.out.println("=== MY JOB POSTINGS ===\n");

            System.out.println("📤 Fetching your job postings...");

            // Only get jobs posted by this recruiter
            List<Job> jobs = jobService.getJobsByRecruiterId(session.getUserId());

            if (jobs.isEmpty()) {
                System.out. println("⚠️  You haven't posted any jobs yet!");
            } else {
                System.out.println("✅ You have " + jobs. size() + " job posting(s):\n");
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
                System. out.println("⚠️  No applications found!");
            } else {
                System.out. println("✅ Found " + applications.size() + " application(s):\n");
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

            System. out.println("✅ Application found:\n");
            displayApplication(application);

            System.out.println("\nUpdate Status:");
            System.out.println("1.  Approve (ACCEPTED)");
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

            if (! jobToClose.getRecruiterId().equals(session.getUserId())) {
                System.out.println("❌ You can only close your own job postings!");
                return;
            }

            if (InputHelper.confirm("Are you sure you want to close this job posting? ")) {
                System.out. println("\n📤 Closing job.. .");
                boolean closed = jobService.closeJob(jobId);

                if (closed) {
                    System.out.println("✅ Job posting closed successfully!");
                } else {
                    System.out.println("❌ Failed to close job posting!");
                }
            } else {
                System. out.println("⚠️  Cancelled");
            }

        } catch (Exception e) {
            System.err.println("❌ Error closing job:  " + e.getMessage());
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

            System.out. print("Enter Job ID: ");
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
            System.err. println("❌ Error viewing candidate:  " + e.getMessage());
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

            System. out.print("Enter skills to search for (e.g., 'Java, RMI, MongoDB'): ");
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
            System.out. println("\n╔════════════════════════════════════════╗");
            System.out. println("║   Search by Experience Level           ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System. out.print("Enter minimum years of experience required: ");
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

            System.out. print("Job ID: ");
            String jobId = InputHelper.getString();

            System.out.print("Applicant ID: ");
            String applicantId = InputHelper.getString();

            System.out.print("Interview Date (DD/MM/YYYY): ");
            String dateStr = InputHelper.getString();

            System.out.print("Interview Time (HH:MM, 24-hour format): ");
            String timeStr = InputHelper.getString();

            // Parse date and time
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date scheduledDate = sdf.parse(dateStr + " " + timeStr);

            System.out.print("Location (e.g., 'Online - Zoom' or 'Office - Room 301'): ");
            String location = InputHelper.getString();

            System.out.print("Notes [Optional, press Enter to skip]: ");
            String notes = InputHelper.getString();

            // Create interview
            Interview interview = new Interview(jobId, applicantId, session.getUserId(), scheduledDate, location);
            if (! notes.isEmpty()) {
                interview.setNotes(notes);
            }

            System.out.println("\n📤 Scheduling interview.. .");
            String interviewId = recruiterService.createInterview(interview);

            System.out.println("✅ Interview scheduled successfully!");
            System. out.println("   Interview ID: " + interviewId);
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
            System. out.println("   Interview ID:  " + interviewId);

        } catch (Exception e) {
            System.err.println("❌ Error scheduling interview: " + e.getMessage());
        }
    }

    private void viewMyInterviews() {
        try {
            System.out. println("=== MY INTERVIEWS ===\n");

            System.out. println("📤 Fetching your interviews...");
            List<Interview> interviews = recruiterService.getMyInterviews(session.getUserId());

            if (interviews.isEmpty()) {
                System.out. println("⚠️  You haven't scheduled any interviews yet!");
            } else {
                System. out.println("✅ You have " + interviews.size() + " interview(s):\n");

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

            System.out. print("Enter Interview ID: ");
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
            System. out.println("=== CANCEL INTERVIEW ===\n");

            System.out.print("Enter Interview ID: ");
            String interviewId = InputHelper.getString();

            if (InputHelper.confirm("Are you sure you want to cancel this interview?")) {
                System.out.println("\n📤 Cancelling interview...");
                boolean cancelled = recruiterService.cancelInterview(interviewId);

                if (cancelled) {
                    System. out.println("✅ Interview cancelled successfully!");
                } else {
                    System.out.println("❌ Cancellation failed!");
                }
            } else {
                System. out.println("⚠️  Cancelled");
            }

        } catch (Exception e) {
            System.err.println("❌ Error cancelling interview:  " + e.getMessage());
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
