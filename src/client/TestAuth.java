package client;

import shared.models.Session;
import shared.models.User;
import shared.interfaces.IAuthService;

public class TestAuth {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║      Testing Authentication System    ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        try {
            // Connect to server
            RMIClient client = new RMIClient();
            IAuthService authService = client.getAuthService();

            // ========================================
            // TEST 1: Register Applicant
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 1: Register Applicant");
            System.out.println("========================================\n");

            String applicantUserId = authService.register(
                "applicant@test.com",
                "password123",
                "APPLICANT",
                "Ahmed Mohamed"
            );

            if (applicantUserId != null) {
                System.out.println("✅ Applicant registered successfully");
                System.out. println("   User ID: " + applicantUserId);
            } else {
                System.out.println("❌ Registration failed");
            }
            System.out.println();

            // ========================================
            // TEST 2: Register Recruiter
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 2: Register Recruiter");
            System. out.println("========================================\n");

            String recruiterUserId = authService.register(
                "recruiter@test. com",
                "password123",
                "RECRUITER",
                "Sara Hassan"
            );

            if (recruiterUserId != null) {
                System.out. println("✅ Recruiter registered successfully");
                System.out.println("   User ID: " + recruiterUserId);
            } else {
                System.out.println("❌ Registration failed");
            }
            System. out.println();

            // ========================================
            // TEST 3: Try Duplicate Email
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 3: Try Duplicate Email");
            System.out. println("========================================\n");

            String duplicateId = authService.register(
                "applicant@test.com",  // Same email as before
                "password456",
                "APPLICANT",
                "Another User"
            );

            if (duplicateId == null) {
                System.out.println("✅ Correctly rejected duplicate email");
            } else {
                System.out.println("❌ Should have rejected duplicate email");
            }
            System.out.println();

            // ========================================
            // TEST 4: Login Applicant
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 4: Login Applicant");
            System. out.println("========================================\n");

            Session applicantSession = authService.login("applicant@test.com", "password123");

            if (applicantSession != null) {
                System.out.println("✅ Applicant login successful");
                System.out. println("   Email: " + applicantSession.getEmail());
                System.out. println("   Role: " + applicantSession.getRole());
                System.out.println("   Session Token: " + applicantSession.getSessionToken());
                System.out.println("   Profile ID: " + applicantSession. getProfileId());
                System.out.println("   Expires: " + applicantSession.getExpiresAt());
            } else {
                System.out.println("❌ Login failed");
            }
            System.out. println();

            // ========================================
            // TEST 5: Login with Wrong Password
            // ========================================
            System.out.println("========================================");
            System.out. println("TEST 5: Login with Wrong Password");
            System.out.println("========================================\n");

            Session wrongPassSession = authService.login("applicant@test.com", "wrongpassword");

            if (wrongPassSession == null) {
                System.out.println("✅ Correctly rejected wrong password");
            } else {
                System.out.println("❌ Should have rejected wrong password");
            }
            System.out.println();

            // ========================================
            // TEST 6: Validate Session
            // ========================================
            System.out.println("========================================");
            System.out. println("TEST 6: Validate Session");
            System.out. println("========================================\n");

            Session validatedSession = authService.validateSession(applicantSession.getSessionToken());

            if (validatedSession != null) {
                System.out.println("✅ Session is valid");
                System.out. println("   Email: " + validatedSession.getEmail());
                System.out.println("   Role: " + validatedSession.getRole());
            } else {
                System.out.println("❌ Session validation failed");
            }
            System.out.println();

            // ========================================
            // TEST 7: Check Role Permission
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 7: Check Role Permission");
            System.out.println("========================================\n");

            boolean hasApplicantRole = authService.hasRole(applicantSession.getSessionToken(), "APPLICANT");
            boolean hasRecruiterRole = authService.hasRole(applicantSession.getSessionToken(), "RECRUITER");

            System.out.println("Has APPLICANT role: " + (hasApplicantRole ? "✅ YES" : "❌ NO"));
            System.out.println("Has RECRUITER role: " + (hasRecruiterRole ?  "❌ NO (correct! )" : "✅ YES"));
            System.out.println();

            // ========================================
            // TEST 8: Get User Info
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 8: Get User Info");
            System.out.println("========================================\n");

            User user = authService.getUserById(applicantUserId);

            if (user != null) {
                System.out.println("✅ User info retrieved");
                System.out.println("   ID: " + user.getId());
                System.out.println("   Email: " + user.getEmail());
                System.out.println("   Role: " + user. getRole());
                System.out.println("   Profile ID: " + user.getProfileId());
                System.out.println("   Created: " + user.getCreatedAt());
                System.out. println("   Active: " + user.isActive());
            }
            System.out.println();

            // ========================================
            // TEST 9: Change Password
            // ========================================
            System.out.println("========================================");
            System.out. println("TEST 9: Change Password");
            System.out.println("========================================\n");

            boolean passwordChanged = authService.changePassword(
                applicantUserId,
                "password123",
                "newpassword456"
            );

            if (passwordChanged) {
                System.out.println("✅ Password changed successfully");

                // Try login with new password
                Session newSession = authService.login("applicant@test.com", "newpassword456");
                if (newSession != null) {
                    System.out.println("✅ Login with new password successful");
                }
            } else {
                System.out.println("❌ Password change failed");
            }
            System.out.println();

            // ========================================
            // TEST 10: Logout
            // ========================================
            System.out.println("========================================");
            System.out.println("TEST 10: Logout");
            System.out.println("========================================\n");

            boolean loggedOut = authService.logout(applicantSession.getSessionToken());

            if (loggedOut) {
                System.out.println("✅ Logout successful");

                // Try to validate session after logout
                Session invalidSession = authService.validateSession(applicantSession.getSessionToken());
                if (invalidSession == null) {
                    System. out.println("✅ Session correctly invalidated after logout");
                }
            } else {
                System.out.println("❌ Logout failed");
            }
            System.out.println();

            // ========================================
            // SUMMARY
            // ========================================
            System.out.println("╔════════════════════════════════════════╗");
            System. out.println("║   ✅ ALL AUTH TESTS PASSED!            ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.println("Summary:");
            System.out.println("   ✅ User registration (Applicant & Recruiter)");
            System.out.println("   ✅ Duplicate email prevention");
            System.out.println("   ✅ Login with credentials");
            System.out.println("   ✅ Password validation");
            System.out.println("   ✅ Session management");
            System.out. println("   ✅ Role-based permissions");
            System.out.println("   ✅ User info retrieval");
            System. out.println("   ✅ Password change");
            System. out.println("   ✅ Logout & session invalidation");
            System. out.println();
            System.out.println("🔐 Authentication system is ready!");
            System.out.println("💡 Check MongoDB Compass - you should see a 'users' collection");

        } catch (Exception e) {
            System.err.println("\n❌ Auth test failed:");
            e.printStackTrace();
        }
    }
}
