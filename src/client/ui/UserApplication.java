package client.ui;

import client.RMIClient;
import client.utils.InputHelper;
import shared.models.Session;
import shared.interfaces.IAuthService;

public class UserApplication {

    private RMIClient client;
    private Session currentSession;

    public UserApplication(RMIClient client) {
        this.client = client;
    }

    public void run() {
        try {
            IAuthService authService = client.getAuthService();

            // Login/Register loop
            while (currentSession == null) {
                showLoginMenu();
                int choice = InputHelper.getInt();
                System.out.println();

                switch (choice) {
                    case 1:
                        currentSession = login(authService);
                        break;
                    case 2:
                        register(authService);
                        break;
                    case 0:
                        return; // Exit to main menu
                    default:
                        System.out. println("❌ Invalid choice!");
                }
            }

            // User is logged in - show role-based menu
            if (currentSession. getRole().equals("APPLICANT")) {
                new ApplicantMenu(client, currentSession).run();
            } else if (currentSession.getRole().equals("RECRUITER")) {
                new RecruiterMenu(client, currentSession).run();
            }

        } catch (Exception e) {
            System.err.println("❌ Error:  " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showLoginMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out. println("║    Job Recruitment System - Login     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Back to Main Menu");
        System.out.print("\nChoice: ");
    }

    private Session login(IAuthService authService) {
        try {
            System.out.println("\n=== LOGIN ===\n");

            System.out.print("Email: ");
            String email = InputHelper. getString();

            System.out.print("Password: ");
            String password = InputHelper.getString();

            System.out.println("\n📤 Logging in...");
            Session session = authService.login(email, password);

            if (session != null) {
                System.out.println("✅ Login successful!");
                System.out.println("   Welcome, " + session.getEmail());
                System.out.println("   Role: " + session.getRole());
                return session;
            } else {
                System. out.println("❌ Invalid email or password!");
                return null;
            }

        } catch (Exception e) {
            System.err.println("❌ Login error: " + e. getMessage());
            return null;
        }
    }

    private void register(IAuthService authService) {
        try {
            System.out.println("\n=== REGISTER ===\n");

            System. out.print("Full Name: ");
            String name = InputHelper.getString();

            // Validate name
            if (name.trim().length() < 2) {
                System.out.println("❌ Name must be at least 2 characters long.");
                return;
            }

            System.out.print("Email: ");
            String email = InputHelper.getString();
            System.out.println("   (Example: user@example.com)");

            System.out.print("Password (min 6 chars): ");
            String password = InputHelper.getString();

            // Validate password length
            if (password.length() < 6) {
                System.out.println("❌ Password must be at least 6 characters long.");
                return;
            }

            System. out.println("\nRegister as:");
            System.out.println("  1. Applicant (Job Seeker)");
            System. out.println("  2. Recruiter (Employer)");
            System.out. print("Choice: ");
            int roleChoice = InputHelper.getInt();

            String role = (roleChoice == 1) ? "APPLICANT" : "RECRUITER";
            String phone = "";

            // ============================================
            // Collect phone if registering as applicant
            // ============================================
            if (roleChoice == 1) {
                System.out.println("\n--- Applicant Profile Setup ---");

                boolean validPhone = false;
                while (! validPhone) {
                    System.out.print("Phone Number (must start with 0 and be 11 digits): ");
                    System.out.println("   Example: 01234567890");
                    phone = InputHelper.getString();

                    // Validate phone format
                    if (phone.matches("^0\\d{10}$")) {
                        validPhone = true;
                    } else {
                        System.out.println("❌ Invalid phone format!");
                        System.out.println("   Phone must:");
                        System.out.println("   • Start with 0");
                        System.out.println("   • Be exactly 11 digits");
                        System.out.println("   • Example: 01234567890");

                        if (! InputHelper.confirm("\nTry again? ")) {
                            return;
                        }
                    }
                }
            }

            System.out.println("\n📤 Registering...");

            // Use new method with phone
            String userId = authService.registerWithPhone(email, password, role, name, phone);

            if (userId != null && !userId.isEmpty()) {
                System.out.println("✅ Registration successful!");
                System. out.println("   User ID:  " + userId);
                System. out.println("   Email: " + email);
                System.out.println("   Role: " + role);
                if (! phone.isEmpty()) {
                    System.out.println("   Phone: " + phone);
                }
                System.out.println("\n💡 You can now login with your credentials.");
            } else {
                System.out.println("❌ Registration failed!");
                System.out.println("\nPossible reasons:");
                System. out.println("   • Email format is invalid (must be like user@example.com)");
                System.out.println("   • Email already exists");
                System.out.println("   • Password is too short (min 6 characters)");
                System.out. println("   • Name is too short (min 2 characters)");
                System.out.println("   • Phone format is invalid (must start with 0 and be 11 digits)");
            }

        } catch (Exception e) {
            System. err.println("❌ Registration error: " + e.getMessage());
        }
    }
}
