package client;

import shared.models.Applicant;
import shared.models.Job;
import shared.models.Application;
import shared.interfaces.IApplicantService;
import shared.interfaces.IJobService;
import shared.interfaces.IApplicationService;

import java.util.List;

public class TestRMI {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System. out.println("║      Testing RMI Remote Calls          ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        try {
            // ========================================
            // Connect to Server
            // ========================================
            RMIClient client = new RMIClient();

            IApplicantService applicantService = client.getApplicantService();
            IJobService jobService = client.getJobService();
            IApplicationService applicationService = client. getApplicationService();

            // ========================================
            // TEST 1: Create Applicant (Remote Call)
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 1: Remote Call - Create Applicant");
            System. out.println("========================================\n");

            Applicant applicant = new Applicant(
                "Remote Test User",
                "remote@test.com",
                "+20-999-888-7777",
                "Testing RMI remote calls",
                "Bachelor's Degree",
                2
            );
            applicant.addSkill("RMI");
            applicant.addSkill("Distributed Systems");
            applicant.addSkill("Java");

            System.out.println("Calling remote method: createApplicant()");
            String applicantId = applicantService.createApplicant(applicant);

            System.out.println("✅ Remote call successful!");
            System.out.println("   Returned ID: " + applicantId);
            System.out.println();

            // ========================================
            // TEST 2: Create Job (Remote Call)
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 2: Remote Call - Create Job");
            System.out.println("========================================\n");

            Job job = new Job(
                "RMI Developer",
                "Build distributed systems using Java RMI",
                "Remote Tech Inc",
                "Cairo, Egypt",
                65000,
                "Java RMI, Distributed Systems"
            );

            System.out.println("Calling remote method: createJob()");
            String jobId = jobService.createJob(job);

            System.out.println("✅ Remote call successful!");
            System.out.println("   Returned Job ID: " + jobId);
            System.out.println();

            // ========================================
            // TEST 3: Get All Jobs (Remote Call)
            // ========================================
            System. out.println("========================================");
            System.out.println("TEST 3: Remote Call - Get All Jobs");
            System.out. println("========================================\n");

            System.out.println("Calling remote method: getAllJobs()");
            List<Job> allJobs = jobService.getAllJobs();

            System. out.println("✅ Remote call successful!");
            System.out.println("   Retrieved " + allJobs.size() + " jobs from server");
            System.out. println();

            System.out.println("Jobs retrieved:");
            for (int i = 0; i < allJobs.size(); i++) {
                Job j = allJobs.get(i);
                System.out.println("   " + (i+1) + ". " + j.getTitle() + " at " + j.getCompany());
            }
            System.out.println();

            // ========================================
            // TEST 4: Submit Application (Remote Call)
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 4: Remote Call - Submit Application");
            System.out.println("========================================\n");

            Application application = new Application(
                jobId,
                applicantId,
                "I am very interested in this RMI position and I have experience with distributed systems."
            );

            System.out.println("Calling remote method: submitApplication()");
            String applicationId = applicationService.submitApplication(application);

            System.out.println("✅ Remote call successful!");
            System.out.println("   Returned Application ID: " + applicationId);
            System.out.println();

            // ========================================
            // TEST 5: Get Applicant by ID (Remote Call)
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 5: Remote Call - Get Applicant by ID");
            System. out.println("========================================\n");

            System.out.println("Calling remote method: getApplicantById()");
            Applicant retrieved = applicantService.getApplicantById(applicantId);

            System.out.println("✅ Remote call successful!");
            System.out.println("   Retrieved applicant:");
            System.out.println("      Name: " + retrieved.getName());
            System.out.println("      Email: " + retrieved.getEmail());
            System.out.println("      Skills: " + retrieved. getSkills());
            System. out.println();

            // ========================================
            // TEST 6: Search by Skill (Remote Call)
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 6: Remote Call - Search by Skill");
            System.out. println("========================================\n");

            System.out.println("Calling remote method: searchApplicantsBySkill('RMI')");
            List<Applicant> rmiApplicants = applicantService.searchApplicantsBySkill("RMI");

            System.out.println("✅ Remote call successful!");
            System.out.println("   Found " + rmiApplicants. size() + " applicant(s) with RMI skill");
            for (Applicant a : rmiApplicants) {
                System.out.println("      - " + a.getName() + " - " + a.getSkills());
            }
            System.out.println();

            // ========================================
            // SUMMARY
            // ========================================
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║   ✅ ALL RMI TESTS PASSED!             ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.println("Summary of Remote Calls:");
            System.out. println("   ✅ createApplicant() - SUCCESS");
            System.out.println("   ✅ createJob() - SUCCESS");
            System. out.println("   ✅ getAllJobs() - SUCCESS");
            System.out.println("   ✅ submitApplication() - SUCCESS");
            System.out.println("   ✅ getApplicantById() - SUCCESS");
            System.out.println("   ✅ searchApplicantsBySkill() - SUCCESS");
            System.out.println();
            System.out.println("🎉 Your RMI distributed system is working perfectly!");
            System.out. println("🎉 Server and Client are communicating over the network!");

        } catch (Exception e) {
            System.err.println("\n❌ RMI Test failed:");
            e.printStackTrace();
            System.err.println("\n⚠️  Make sure:");
            System.err.println("   1. MongoDB is running on port 27020");
            System.err.println("   2. RMI Server is running (run RMIServer.java first)");
        }
    }
}
