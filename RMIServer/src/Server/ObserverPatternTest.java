package Server;

import Server.services.InterviewServiceImpl;
import shared.models.Interview;
import java.util.Date;

public class ObserverPatternTest {
    public static void main(String[] args) {
        try {
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║     🧪 Observer Pattern Test Suite     ║");
            System.out.println("╚════════════════════════════════════════╝");

            System.out.println("\nwaiting for MongoDB connection...");
            InterviewServiceImpl service = new InterviewServiceImpl();

            System.out.println("\n------------------------------------------------");
            System.out.println("[Test 1] Create Interview");
            System.out.println("Goal: Verify 'SCHEDULED' notifications (Email & System)");
            System.out.println("------------------------------------------------");

            Interview interview = new Interview();
            interview.setJobId("test_job_obs_1");
            interview.setApplicantId("test_applicant_obs_1");
            interview.setRecruiterId("test_recruiter_obs_1");
            interview.setScheduledDate(new Date());
            interview.setLocation("Observer Test Lab");
            interview.setNotes("Testing notification triggers");

            String id = service.createInterview(interview);
            System.out.println("✅ Result: Created Interview ID: " + id);

            System.out.println("\n------------------------------------------------");
            System.out.println("[Test 2] Update Interview Status");
            System.out.println("Goal: Verify 'COMPLETED' notifications");
            System.out.println("------------------------------------------------");

            interview.setInterviewId(id);
            interview.setStatus("COMPLETED");
            service.updateInterview(interview);
            System.out.println("✅ Result: Updated status to COMPLETED");

            System.out.println("\n------------------------------------------------");
            System.out.println("[Test 3] Cancel Interview");
            System.out.println("Goal: Verify 'CANCELLED' notifications");
            System.out.println("------------------------------------------------");

            service.cancelInterview(id);
            System.out.println("✅ Result: Cancelled interview");

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