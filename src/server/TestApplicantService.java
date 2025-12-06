package server;

import server.services. ApplicantServiceImpl;
import shared.models. Applicant;
import java .util.Arrays;

public class TestApplicantService {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Testing ApplicantService");
        System.out.println("========================================\n");

        try {
            // Create the service
            ApplicantServiceImpl service = new ApplicantServiceImpl();

            // ========================================
            // TEST 1: CREATE Applicant
            // ========================================
            System.out.println("--- Test 1: Creating applicant ---");
            Applicant applicant1 = new Applicant(
                "Sara Hassan",
                "sara@example. com",
                "+20-111-222-3333",
                "Senior Java Developer with 8 years experience",
                "Master's in Computer Science",
                8
            );
            applicant1.addSkill("Java");
            applicant1.addSkill("Spring Boot");
            applicant1.addSkill("Microservices");
            applicant1.addSkill("AWS");

            String id1 = service.createApplicant(applicant1);
            System.out.println("✅ Test 1 PASSED - Applicant created with ID: " + id1);
            System.out.println();

            // ========================================
            // TEST 2: GET Applicant by ID
            // ========================================
            System.out.println("--- Test 2: Getting applicant by ID ---");
            Applicant retrieved = service.getApplicantById(id1);

            if (retrieved != null) {
                System.out.println("✅ Test 2 PASSED - Applicant retrieved!");
                System.out.println("   Retrieved applicant details:");
                System.out.println("   - ID: " + retrieved.getId());
                System.out.println("   - Name: " + retrieved.getName());
                System.out.println("   - Email: " + retrieved.getEmail());
                System. out.println("   - Phone: " + retrieved.getPhone());
                System.out.println("   - Education: " + retrieved.getEducation());
                System.out.println("   - Experience: " + retrieved.getExperience() + " years");
                System. out.println("   - Skills: " + retrieved.getSkills());

                // Verify the data matches
                if (retrieved.getName().equals("Sara Hassan") &&
                    retrieved.getEmail().equals("sara@example.com")) {
                    System.out. println("✅ Data verification PASSED!");
                } else {
                    System.out.println("❌ Data verification FAILED!");
                }
            } else {
                System.out.println("❌ Test 2 FAILED - Applicant not found!");
            }
            System.out.println();

            // ========================================
            // TEST 3: GET with Invalid ID
            // ========================================
            System.out.println("--- Test 3: Getting applicant with invalid ID ---");
            String fakeId = "000000000000000000000000"; // Valid ObjectId format but doesn't exist
            Applicant notFound = service.getApplicantById(fakeId);

            if (notFound == null) {
                System.out.println("✅ Test 3 PASSED - Correctly returned null for non-existent ID");
            } else {
                System.out.println("❌ Test 3 FAILED - Should have returned null!");
            }
            System.out.println();

            // ========================================
            // Summary
            // ========================================
            System.out.println("========================================");
            System.out. println("✅ All tests completed!");
            System.out.println("========================================");
            System.out.println("\n💡 Functions tested:");
            System.out.println("   1. createApplicant() ✅");
            System.out.println("   2. getApplicantById() ✅");

        } catch (Exception e) {
            System.err.println("\n❌ Test failed with error:");
            e.printStackTrace();
        }
    }
}
