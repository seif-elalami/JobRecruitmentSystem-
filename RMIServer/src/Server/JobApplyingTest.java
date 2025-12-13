package Server;

import Server.services.ApplicationServiceImpl;
import shared.models.Application;
import java.util.Date;

public class JobApplyingTest {
    public static void main(String[] args) {
        try {
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║         🧪 Job Applying Test           ║");
            System.out.println("╚════════════════════════════════════════╝");
            
            // 1. Initialize Service
            System.out.println("\nwaiting for MongoDB connection...");
            ApplicationServiceImpl service = new ApplicationServiceImpl();
            
            // 2. Submit Application
            System.out.println("\n------------------------------------------------");
            System.out.println("[Test 1] Submit Application");
            System.out.println("Goal: Verify application submission and initial state");
            System.out.println("------------------------------------------------");
            
            Application application = new Application();
            application.setJobId("valuable_job_123");
            application.setApplicantId("hopeful_applicant_456");
            application.setCoverLetter("I am the best candidate!");
            
            String id = service.SubmitApplication(application);
            System.out.println("✅ Result: Submitted Application ID: " + id);
            
            Application fetchedApp = service.getApplicationById(id);
            if (fetchedApp != null && "APPLIED".equals(fetchedApp.getStatus())) {
                 System.out.println("✅ Verified: Initial status is 'APPLIED'");
            } else {
                 throw new RuntimeException("❌ Failed: Initial status incorrect. Expected 'APPLIED', got: " + (fetchedApp != null ? fetchedApp.getStatus() : "null"));
            }

            // 3. Update Status (Trigger Notification)
            System.out.println("\n------------------------------------------------");
            System.out.println("[Test 2] Update Status -> UNDER_REVIEW");
            System.out.println("Goal: Verify status update triggers notification");
            System.out.println("------------------------------------------------");
            
            boolean updated = service.updateApplicationStatus(id, "UNDER_REVIEW");
            if (updated) {
                 System.out.println("✅ Result: Updated status to UNDER_REVIEW");
            } else {
                 throw new RuntimeException("❌ Failed: Status update returned false");
            }
            
            fetchedApp = service.getApplicationById(id);
             if (fetchedApp != null && "UNDER_REVIEW".equals(fetchedApp.getStatus())) {
                 System.out.println("✅ Verified: Status is now 'UNDER_REVIEW'");
            } else {
                 throw new RuntimeException("❌ Failed: Status update verification failed.");
            }

            // 4. Delete Application
            System.out.println("\n------------------------------------------------");
            System.out.println("[Test 3] Withdraw/Delete Application");
            System.out.println("Goal: Verify application cleanup");
            System.out.println("------------------------------------------------");
            
            boolean deleted = service.deleteApplication(id);
            if (deleted) {
                System.out.println("✅ Result: Application deleted");
            } else {
                 throw new RuntimeException("❌ Failed: Application deletion returned false");
            }
            
            fetchedApp = service.getApplicationById(id);
            if (fetchedApp == null) {
                 System.out.println("✅ Verified: Application no longer exists");
            } else {
                 throw new RuntimeException("❌ Failed: Application still exists after deletion");
            }
            
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║          ✅ Test Suite Passed!         ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.exit(0);
            
        } catch (Exception e) {
            System.err.println("\n❌ Test Failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
