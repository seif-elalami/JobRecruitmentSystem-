package server;

import server.services.ApplicantServiceImpl;
import server.services.JobServiceImpl;
import server.services.ApplicationServiceImpl;
import shared.models.Applicant;
import shared.models.Job;
import shared.models.Application;

import java.util.List;

public class TestRecruiterFlow {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out. println("Testing Complete Recruiter Flow");
        System.out.println("========================================\n");

        try {
            // Initialize services
            ApplicantServiceImpl applicantService = new ApplicantServiceImpl();
            JobServiceImpl jobService = new JobServiceImpl();
            ApplicationServiceImpl applicationService = new ApplicationServiceImpl();

            // ========================================
            // SETUP: Create some test data
            // ========================================
            System.out.println("Setting up test data...\n");

            // Create 3 applicants with different skills
            Applicant applicant1 = new Applicant(
                "Mohamed Ali",
                "mohamed@example.com",
                "+20-100-111-1111",
                "5 years Java developer",
                "Bachelor's CS",
                5
            );
            applicant1.addSkill("Java");
            applicant1.addSkill("Spring Boot");
            applicant1. addSkill("MySQL");
            String applicantId1 = applicantService.createApplicant(applicant1);

            Applicant applicant2 = new Applicant(
                "Fatima Hassan",
                "fatima@example.com",
                "+20-100-222-2222",
                "3 years backend developer",
                "Bachelor's SE",
                3
            );
            applicant2.addSkill("Java");
            applicant2.addSkill("MongoDB");
            applicant2.addSkill("Docker");
            String applicantId2 = applicantService.createApplicant(applicant2);

            Applicant applicant3 = new Applicant(
                "Ahmed Mahmoud",
                "ahmed@example.com",
                "+20-100-333-3333",
                "Fresh graduate",
                "Bachelor's IT",
                0
            );
            applicant3.addSkill("Python");
            applicant3.addSkill("Django");
            applicant3.addSkill("React");
            String applicantId3 = applicantService.createApplicant(applicant3);

            System.out.println("✅ Created 3 applicants\n");

            // ========================================
            // STEP 1: Recruiter Posts a Job
            // ========================================
            System.out.println("========================================");
            System.out.println("STEP 1: Recruiter Posts a Job");
            System.out.println("========================================\n");

            Job job = new Job(
                "Java Backend Developer",
                "We are looking for an experienced Java developer to join our backend team.  " +
                "You will work on building scalable microservices using Spring Boot and MongoDB.",
                "TechCorp Egypt",
                "Cairo, Egypt",
                75000,
                "3+ years Java, Spring Boot, MongoDB, REST APIs"
            );

            String jobId = jobService.createJob(job);
            System. out.println("✅ Recruiter posted job with ID: " + jobId);
            System.out.println();

            // Simulate applicants applying to this job
            System.out.println("Applicants are now applying to the job...\n");

            Application app1 = new Application(jobId, applicantId1,
                "I have 5 years of Java experience and would love to join your team.");
            String appId1 = applicationService.submitApplication(app1);

            Application app2 = new Application(jobId, applicantId2,
                "I'm excited about this opportunity.  I have experience with Java, Spring Boot, and MongoDB.");
            String appId2 = applicationService.submitApplication(app2);

            System.out.println("✅ 2 applicants have applied to the job\n");

            // ========================================
            // STEP 2: Recruiter Views All Applications
            // ========================================
            System.out.println("========================================");
            System.out. println("STEP 2: Recruiter Views Applications");
            System.out.println("========================================\n");

            System.out.println("Recruiter checks applications for job: " + jobId + "\n");
            List<Application> applications = applicationService.getApplicationsByJobId(jobId);

            System.out.println("📨 Received " + applications.size() + " application(s):\n");

            for (int i = 0; i < applications.size(); i++) {
                Application app = applications.get(i);
                System.out.println("Application #" + (i+1) + ":");
                System.out.println("   Application ID: " + app.getId());
                System.out.println("   Applicant ID: " + app. getApplicantId());
                System.out.println("   Status: " + app.getStatus());
                System.out.println("   Applied on: " + app.getApplicationDate());
                System.out.println();
            }

            // ========================================
            // STEP 3: Recruiter Views Applicant Details
            // ========================================
            System.out.println("========================================");
            System. out.println("STEP 3: Recruiter Reviews Applicants");
            System.out.println("========================================\n");

            System.out.println("Reviewing each applicant's profile...\n");

            for (int i = 0; i < applications.size(); i++) {
                Application app = applications.get(i);
                Applicant applicant = applicantService.getApplicantById(app.getApplicantId());

                if (applicant != null) {
                    System.out.println("👤 Applicant #" + (i+1) + ":");
                    System.out. println("   Name: " + applicant.getName());
                    System.out.println("   Email: " + applicant.getEmail());
                    System.out.println("   Phone: " + applicant.getPhone());
                    System.out.println("   Experience: " + applicant.getExperience() + " years");
                    System.out.println("   Skills: " + applicant.getSkills());
                    System.out.println("   Education: " + applicant.getEducation());
                    System.out.println("   Cover Letter: " + app.getCoverLetter());
                    System.out.println();
                }
            }

            // ========================================
            // STEP 4: Recruiter Accepts/Rejects
            // ========================================
            System.out.println("========================================");
            System.out.println("STEP 4: Recruiter Makes Decisions");
            System.out. println("========================================\n");

            // Accept first applicant (Mohamed Ali - 5 years experience)
            System.out.println("Decision: ACCEPT Mohamed Ali (5 years experience)");
            boolean accepted = applicationService.updateApplicationStatus(appId1, "ACCEPTED");
            if (accepted) {
                System. out.println("✅ Application ACCEPTED\n");
            }

            // Reject second applicant
            System.out.println("Decision: REJECT Fatima Hassan (looking for more experience)");
            boolean rejected = applicationService.updateApplicationStatus(appId2, "REJECTED");
            if (rejected) {
                System.out.println("✅ Application REJECTED\n");
            }

            // ========================================
            // STEP 5: Search for Applicants with Specific Skills
            // ========================================
            System.out.println("========================================");
            System. out.println("STEP 5: Search Applicants by Skill");
            System.out. println("========================================\n");

            System.out.println("Recruiter searches for applicants with 'MongoDB' skill...\n");
            List<Applicant> mongoDBApplicants = applicantService. searchApplicantsBySkill("MongoDB");

            System.out.println("🔍 Found " + mongoDBApplicants.size() + " applicant(s) with MongoDB:\n");
            for (Applicant a : mongoDBApplicants) {
                System.out.println("   - " + a.getName() + " (" + a.getExperience() + " years) - Skills: " + a.getSkills());
            }
            System.out.println();

            System.out.println("Recruiter searches for applicants with 'Java' skill...\n");
            List<Applicant> javaApplicants = applicantService. searchApplicantsBySkill("Java");

            System. out.println("🔍 Found " + javaApplicants.size() + " applicant(s) with Java:\n");
            for (Applicant a : javaApplicants) {
                System.out.println("   - " + a.getName() + " (" + a.getExperience() + " years) - Skills: " + a.getSkills());
            }
            System.out.println();

            // ========================================
            // STEP 6: Recruiter Closes the Job
            // ========================================
            System.out.println("========================================");
            System.out. println("STEP 6: Recruiter Closes the Job");
            System.out. println("========================================\n");

            System.out.println("Position has been filled.  Closing job posting...\n");
            boolean closed = jobService.closeJob(jobId);

            if (closed) {
                System.out.println("✅ Job closed successfully!");

                // Verify job is closed
                Job closedJob = jobService.getJobById(jobId);
                System.out.println("   Job Status: " + closedJob.getStatus());
            }

            System.out.println("\n========================================");
            System.out. println("✅ COMPLETE RECRUITER FLOW TESTED!");
            System.out.println("========================================\n");

            System.out.println("Summary:");
            System.out.println("  ✅ Step 1: Job posted");
            System.out.println("  ✅ Step 2: Viewed " + applications.size() + " applications");
            System.out.println("  ✅ Step 3: Reviewed applicant profiles");
            System.out.println("  ✅ Step 4: Accepted 1, Rejected 1");
            System.out.println("  ✅ Step 5: Searched applicants by skill");
            System.out. println("  ✅ Step 6: Closed the job");

            System.out.println("\n💡 Check MongoDB Compass to see:");
            System.out.println("   - jobs collection: Status changed to CLOSED");
            System.out. println("   - applications collection: Status changed to ACCEPTED/REJECTED");

        } catch (Exception e) {
            System.err.println("\n❌ Test failed with error:");
            e.printStackTrace();
        }
    }
}
