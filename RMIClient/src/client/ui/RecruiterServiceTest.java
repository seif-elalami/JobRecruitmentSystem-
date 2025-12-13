package client.ui;

import client.RMIClient;
import client.utils.InputHelper;
import shared.interfaces.IRecruiterService;
import shared.models.Job;
import shared.models.Application;
import shared.models.Applicant;
import shared.models.Interview;
import shared.models.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecruiterServiceTest {

    private IRecruiterService service;
    private Session session;

    public RecruiterServiceTest(RMIClient client, Session session) {
        this.service = client.getRecruiterService();
        this.session = session;
    }

    public RecruiterServiceTest(RMIClient client) {
        this.service = client.getRecruiterService();

        this.session = createTestSession();
    }

    private Session createTestSession() {
        System.out.println("\n⚠️  Test Mode: Creating test recruiter session");
        System.out.print("Enter test recruiter ID (or press Enter for default): ");
        String recruiterId = InputHelper.getString();

        if (recruiterId.isEmpty()) {
            recruiterId = "test-recruiter-123";
        }

        Session testSession = new Session(recruiterId, "test@recruiter.com", "RECRUITER");
        System.out.println("✅ Test session created with ID: " + recruiterId);

        return testSession;
    }

    public void run() {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = InputHelper.getInt();
            System.out.println();

            switch (choice) {
                case 1:
                    postJob();
                    break;
                case 2:
                    viewMyJobPostings();
                    break;
                case 3:
                    viewApplicationsForJob();
                    break;
                case 4:
                    searchApplicantsBySkills();
                    break;
                case 5:
                    closeJobPosting();
                    break;
                case 6:
                    createInterview();
                    break;
                case 7:
                    viewMyInterviews();
                    break;
                case 8:
                    updateInterview();
                    break;
                case 9:
                    cancelInterview();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }

            if (running) {
                InputHelper.pause();
            }
        }
    }

    private void showMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    Recruiter Service Test Menu         ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("1. Post Job");
        System.out.println("2. View My Job Postings");
        System.out.println("3. View Applications for Job");
        System.out.println("4. Search Applicants by Skills");
        System.out.println("5. Close Job Posting");
        System.out.println("6. Create Interview");
        System.out.println("7. View My Interviews");
        System.out.println("8. Update Interview");
        System.out.println("9. Cancel Interview");
        System.out.println("0. Back");
        System.out.print("\nChoice: ");
    }

    private void postJob() {
        try {
            System.out.println("=== POST JOB ===\n");

            System.out.print("Job Title: ");
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

            Job job = new Job(title, description, requirements, session.getUserId());

            System.out.println("\n📤 Posting job...");
            String jobId = service.postJob(job);

            System.out.println("✅ Job posted successfully!");
            System.out.println("   Job ID: " + jobId);
            System.out.println("   Title: " + title);
            System.out.println("   Requirements: " + requirements.size());

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewMyJobPostings() {
        try {
            System.out.println("=== MY JOB POSTINGS ===\n");

            System.out.println("📤 Fetching your job postings...");
            List<Job> jobs = service.getMyJobPostings(session.getUserId());

            if (jobs.isEmpty()) {
                System.out.println("⚠️  You haven't posted any jobs yet!");
            } else {
                System.out.println("✅ You have " + jobs.size() + " job posting(s):\n");
                for (int i = 0; i < jobs.size(); i++) {
                    System.out.println("--- Job " + (i + 1) + " ---");
                    displayJob(jobs.get(i));
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error:  " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeJobPosting() {
        try {
            System.out.println("=== CLOSE JOB POSTING ===\n");

            System.out.print("Job ID to close: ");
            String jobId = InputHelper.getString();

            if (InputHelper.confirm("Are you sure you want to close this job posting?")) {
                System.out.println("\n📤 Closing job.. .");
                boolean closed = service.closeJobPosting(jobId, session.getUserId());

                if (closed) {
                    System.out.println("✅ Job posting closed successfully!");
                } else {
                    System.out.println("❌ Failed to close job posting!");
                }
            } else {
                System.out.println("⚠️  Cancelled");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void viewApplicationsForJob() {
        try {
            System.out.println("=== VIEW APPLICATIONS FOR JOB ===\n");

            System.out.print("Job ID: ");
            String jobId = InputHelper.getString();

            System.out.println("\n📤 Fetching applications...");
            List<Application> applications = service.getApplicationsForJob(jobId);

            if (applications.isEmpty()) {
                System.out.println("⚠️  No applications found for this job!");
            } else {
                System.out.println("✅ Found " + applications.size() + " application(s):\n");
                for (int i = 0; i < applications.size(); i++) {
                    System.out.println("--- Application " + (i + 1) + " ---");
                    displayApplication(applications.get(i));
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void searchApplicantsBySkills() {
        try {
            System.out.println("=== SEARCH APPLICANTS BY SKILLS ===\n");

            System.out.print("Skills to search for: ");
            String skills = InputHelper.getString();

            System.out.println("\n📤 Searching...");
            List<Applicant> applicants = service.searchApplicantsBySkills(skills);

            if (applicants.isEmpty()) {
                System.out.println("❌ No applicants found with skills: " + skills);
            } else {
                System.out.println("✅ Found " + applicants.size() + " applicant(s):\n");
                for (int i = 0; i < applicants.size(); i++) {
                    System.out.println("--- Applicant " + (i + 1) + " ---");
                    displayApplicant(applicants.get(i));
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void createInterview() {
        try {
            System.out.println("=== CREATE INTERVIEW ===\n");

            System.out.print("Job ID: ");
            String jobId = InputHelper.getString();

            System.out.print("Applicant ID: ");
            String applicantId = InputHelper.getString();

            System.out.print("Interview Date (DD/MM/YYYY HH:MM): ");
            String dateStr = InputHelper.getString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date scheduledDate = sdf.parse(dateStr);

            System.out.print("Location (e.g., 'Online' or address): ");
            String location = InputHelper.getString();

            System.out.print("Notes [Optional]: ");
            String notes = InputHelper.getString();

            Interview interview = new Interview(jobId, applicantId, session.getUserId(), scheduledDate, location);
            if (!notes.isEmpty()) {
                interview.setNotes(notes);
            }

            System.out.println("\n📤 Creating interview...");
            String interviewId = service.createInterview(interview);

            System.out.println("✅ Interview created successfully!");
            System.out.println("   Interview ID: " + interviewId);
            System.out.println("   Scheduled:  " + scheduledDate);
            System.out.println("   Location: " + location);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewMyInterviews() {
        try {
            System.out.println("=== MY INTERVIEWS ===\n");

            System.out.println("📤 Fetching your interviews...");
            List<Interview> interviews = service.getMyInterviews(session.getUserId());

            if (interviews.isEmpty()) {
                System.out.println("⚠️  You haven't scheduled any interviews yet!");
            } else {
                System.out.println("✅ You have " + interviews.size() + " interview(s):\n");
                for (int i = 0; i < interviews.size(); i++) {
                    System.out.println("--- Interview " + (i + 1) + " ---");
                    displayInterview(interviews.get(i));
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void updateInterview() {
        try {
            System.out.println("=== UPDATE INTERVIEW ===\n");

            System.out.print("Interview ID: ");
            String interviewId = InputHelper.getString();

            Interview interview = service.getInterviewById(interviewId);
            if (interview == null) {
                System.out.println("❌ Interview not found!");
                return;
            }

            System.out.println("Current interview loaded.");
            System.out.println("Update fields (press Enter to skip):\n");

            System.out.print("New Date (DD/MM/YYYY HH:MM) [" + interview.getScheduledDate() + "]: ");
            String dateStr = InputHelper.getString();
            if (!dateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                interview.setScheduledDate(sdf.parse(dateStr));
            }

            System.out.print("Location [" + interview.getLocation() + "]: ");
            String location = InputHelper.getString();
            if (!location.isEmpty()) {
                interview.setLocation(location);
            }

            System.out.print("Notes [" + interview.getNotes() + "]: ");
            String notes = InputHelper.getString();
            if (!notes.isEmpty()) {
                interview.setNotes(notes);
            }

            System.out.println("\n📤 Updating interview...");
            boolean updated = service.updateInterview(interview);

            if (updated) {
                System.out.println("✅ Interview updated successfully!");
            } else {
                System.out.println("❌ Update failed!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void cancelInterview() {
        try {
            System.out.println("=== CANCEL INTERVIEW ===\n");

            System.out.print("Interview ID: ");
            String interviewId = InputHelper.getString();

            if (InputHelper.confirm("Are you sure you want to cancel this interview? ")) {
                System.out.println("\n📤 Cancelling interview...");
                boolean cancelled = service.cancelInterview(interviewId);

                if (cancelled) {
                    System.out.println("✅ Interview cancelled successfully!");
                } else {
                    System.out.println("❌ Cancellation failed!");
                }
            } else {
                System.out.println("⚠️  Cancelled");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void displayJob(Job job) {
        System.out.println("Job ID:       " + job.getJobId());
        System.out.println("Title:       " + job.getTitle());
        System.out.println("Description: " + job.getDescription());
        System.out.println("Status:      " + job.getStatus());
        System.out.println("Posted:      " + job.getPostedDate());
        System.out.println("Requirements:");
        if (job.getRequirements() != null && !job.getRequirements().isEmpty()) {
            for (String req : job.getRequirements()) {
                System.out.println("  • " + req);
            }
        }
    }

    private void displayApplication(Application app) {
        System.out.println("Application ID:  " + app.getApplicationId());
        System.out.println("Job ID:         " + app.getJobId());
        System.out.println("Applicant ID:   " + app.getApplicantId());
        System.out.println("Status:         " + app.getStatus());
        System.out.println("Applied Date:   " + app.getApplicationDate());
        if (app.getCoverLetter() != null && !app.getCoverLetter().isEmpty()) {
            System.out.println("Cover Letter:   " + app.getCoverLetter());
        }
    }

    private void displayApplicant(Applicant a) {
        System.out.println("ID:          " + a.getId());
        System.out.println("Name:        " + a.getName());
        System.out.println("Email:       " + a.getEmail());
        System.out.println("Phone:       " + a.getPhone());
        System.out.println("Skills:      " + a.getSkills());
        System.out.println("Experience:  " + a.getExperience());
    }

    private void displayInterview(Interview i) {
        System.out.println("Interview ID:    " + i.getInterviewId());
        System.out.println("Job ID:         " + i.getJobId());
        System.out.println("Applicant ID:   " + i.getApplicantId());
        System.out.println("Scheduled:       " + i.getScheduledDate());
        System.out.println("Location:       " + i.getLocation());
        System.out.println("Status:         " + i.getStatus());
        if (i.getNotes() != null && !i.getNotes().isEmpty()) {
            System.out.println("Notes:          " + i.getNotes());
        }
    }
}