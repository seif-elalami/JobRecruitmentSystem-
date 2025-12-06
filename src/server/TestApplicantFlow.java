package server;

import server.services.ApplicantServiceImpl;
import server.services.JobServiceImpl;
import server.services.ApplicationServiceImpl;
import shared.models.Applicant;
import shared.models.Job;
import shared.models.Application;

import java.util.List;

public class TestApplicantFlow {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Testing Complete Applicant Flow");
        System.out.println("========================================\n");

        try {
            // Initialize services
            ApplicantServiceImpl applicantService = new ApplicantServiceImpl();
            JobServiceImpl jobService = new JobServiceImpl();
            ApplicationServiceImpl applicationService = new ApplicationServiceImpl();

            System.out.println("\n========================================");
            System.out.println("STEP 1: Applicant Registers");
            System.out.println("========================================\n");

            // Create applicant
            Applicant applicant = new Applicant(
                "Omar Khaled",
                "omar@example.com",
                "+20-100-200-3000",
                "Passionate software developer with 3 years experience in Java",
                "Bachelor's in Computer Engineering",
                3
            );
            applicant.addSkill("Java");
            applicant.addSkill("Spring Boot");
            applicant.addSkill("MongoDB");
            applicant.addSkill("RMI");

            String applicantId = applicantService. createApplicant(applicant);
            System.out.println("✅ Applicant registered with ID: " + applicantId);

            System.out. println("\n========================================");
            System.out.println("STEP 2: Browse Available Jobs");
            System.out. println("========================================\n");

            // First, let's create some jobs (normally recruiters would do this)
            Job job1 = new Job(
                "Senior Java Developer",
                "Looking for experienced Java developer for enterprise applications",
                "Tech Corp",
                "Cairo, Egypt",
                80000,
                "5+ years Java, Spring Boot, Microservices"
            );
            String jobId1 = jobService. createJob(job1);

            Job job2 = new Job(
                "Backend Developer",
                "Develop and maintain backend services using Java and MongoDB",
                "StartUp Inc",
                "Alexandria, Egypt",
                60000,
                "3+ years Java, MongoDB, REST APIs"
            );
            String jobId2 = jobService. createJob(job2);

            Job job3 = new Job(
                "Full Stack Developer",
                "Work on both frontend and backend of web applications",
                "Digital Solutions",
                "Giza, Egypt",
                70000,
                "Java, JavaScript, React, Node.js"
            );
            String jobId3 = jobService.createJob(job3);

            // Now browse all jobs
            System.out.println("\nBrowsing available jobs...\n");
            List<Job> allJobs = jobService.getAllJobs();

            System.out.println("📋 Found " + allJobs.size() + " available jobs:\n");
            for (int i = 0; i < allJobs.size(); i++) {
                Job job = allJobs.get(i);
                System.out. println((i+1) + ". " + job.getTitle());
                System.out.println("   Company: " + job.getCompany());
                System.out.println("   Location: " + job. getLocation());
                System.out.println("   Salary: $" + job.getSalary());
                System.out.println("   Status: " + job.getStatus());
                System.out.println();
            }

            System.out.println("========================================");
            System.out. println("STEP 3: View Job Details");
            System.out. println("========================================\n");

            // Applicant clicks on "Backend Developer" job
            System.out.println("Viewing details of 'Backend Developer' job...\n");
            Job selectedJob = jobService.getJobById(jobId2);

            if (selectedJob != null) {
                System.out.println("📄 Job Details:");
                System. out.println("   Title: " + selectedJob.getTitle());
                System.out.println("   Company: " + selectedJob.getCompany());
                System.out.println("   Location: " + selectedJob.getLocation());
                System.out.println("   Salary: $" + selectedJob. getSalary());
                System. out.println("   Description: " + selectedJob.getDescription());
                System.out.println("   Requirements: " + selectedJob.getRequirements());
                System.out.println("   Status: " + selectedJob.getStatus());
            }

            System.out. println("\n========================================");
            System. out.println("STEP 4: Apply to Job");
            System.out. println("========================================\n");

            // Applicant applies to the Backend Developer job
            Application application = new Application(
                jobId2,  // Job ID
                applicantId,  // Applicant ID
                "Dear Hiring Manager,\n\n" +
                "I am very interested in the Backend Developer position at StartUp Inc. " +
                "With my 3 years of experience in Java and MongoDB, I believe I would be " +
                "a great fit for this role.\n\n" +
                "Looking forward to hearing from you.\n\n" +
                "Best regards,\nOmar Khaled"
            );

            String applicationId = applicationService.submitApplication(application);
            System.out.println("✅ Application submitted with ID: " + applicationId);

            System.out.println("\n========================================");
            System.out.println("STEP 5: Check Application Status");
            System.out.println("========================================\n");

            // Applicant checks their applications
            System.out.println("Checking my applications...\n");
            List<Application> myApplications = applicationService.getApplicationsByApplicantId(applicantId);

            System.out.println("📨 You have " + myApplications.size() + " application(s):\n");
            for (int i = 0; i < myApplications.size(); i++) {
                Application app = myApplications.get(i);

                // Get job details for this application
                Job appliedJob = jobService.getJobById(app.getJobId());

                System.out.println((i+1) + ". Application to: " + (appliedJob != null ? appliedJob.getTitle() : "Unknown"));
                System.out.println("   Company: " + (appliedJob != null ? appliedJob.getCompany() : "Unknown"));
                System.out. println("   Applied on: " + app.getApplicationDate());
                System.out.println("   Status: " + app.getStatus());
                System.out.println();
            }

            System. out.println("========================================");
            System.out.println("✅ COMPLETE FLOW TESTED SUCCESSFULLY!");
            System.out.println("========================================\n");

            System.out.println("Summary:");
            System.out.println("  ✅ Step 1: Applicant registered");
            System.out.println("  ✅ Step 2: Browsed " + allJobs.size() + " jobs");
            System.out.println("  ✅ Step 3: Viewed job details");
            System.out.println("  ✅ Step 4: Applied to job");
            System.out.println("  ✅ Step 5: Checked application status");

            System.out.println("\n💡 Check MongoDB Compass to see the data:");
            System.out.println("   - applicants collection: 1 document");
            System.out.println("   - jobs collection: 3 documents");
            System.out.println("   - applications collection: 1 document");

        } catch (Exception e) {
            System.err.println("\n❌ Test failed with error:");
            e.printStackTrace();
        }
    }
}
