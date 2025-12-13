package client.ui;

import client.RMIClient;
import client.utils.InputHelper;
import shared.interfaces.IAuthService;
import shared.models.Session;
import shared.models.User;

public class UserApplication {

    private RMIClient client;
    private IAuthService authService;
    private Session currentSession;

    public UserApplication(RMIClient client) {
        this.client = client;
        this.authService = client.getAuthService();
    }

    public void run() {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = InputHelper.getInt();
            System.out.println();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 0:
                    running = false;
                    System.out.println("👋 Returning to main menu...");
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
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    Job Recruitment System - Login     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Back to Main Menu");
        System.out.print("\nChoice: ");
    }

    private void login() {
        try {
            System.out.println("\n=== LOGIN ===\n");

            System.out.print("Email: ");
            String email = InputHelper.getString();

            System.out.print("Password: ");
            String password = InputHelper.getString();

            System.out.println("\n📤 Logging in...");

            Session session = authService.login(email, password);

            if (session != null) {
                System.out.println("✅ Login successful!");
                System.out.println("   Welcome, " + session.getUserEmail() + "!");
                System.out.println("   Role: " + session.getRole());

                InputHelper.pause();

                currentSession = session;

                // Redirect to appropriate menu based on role (case-insensitive)
                String role = currentSession.getRole();
                if (role != null && role.equalsIgnoreCase("APPLICANT")) {
                    new ApplicantMenu(client, currentSession).run();
                } else if (role != null && role.equalsIgnoreCase("RECRUITER")) {
                    new RecruiterMenu(client, currentSession).run();
                } else {
                    System.out.println("⚠️  No console menu implemented for role: " + role);
                }

            } else {
                System.out.println("❌ Login failed!");
                System.out.println("   Invalid email or password.");
            }

        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
        }
    }

    private void register() {
        try {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║          User Registration             ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            // Get email
            String email = "";
            boolean validEmail = false;
            while (!validEmail) {
                System.out.print("Email: ");
                email = InputHelper.getString();

                if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    validEmail = true;
                } else {
                    System.out.println("❌ Invalid email format!");
                    System.out.println("   Example: user@example.com");
                    if (!InputHelper.confirm("\nTry again? ")) {
                        return;
                    }
                }
            }

            // Get password
            String password = "";
            boolean validPassword = false;
            while (!validPassword) {
                System.out.print("Password (min 6 characters): ");
                password = InputHelper.getString();

                if (password.length() >= 6) {
                    System.out.print("Confirm Password: ");
                    String confirmPassword = InputHelper.getString();

                    if (password.equals(confirmPassword)) {
                        validPassword = true;
                    } else {
                        System.out.println("❌ Passwords don't match!");
                        if (!InputHelper.confirm("\nTry again?")) {
                            return;
                        }
                    }
                } else {
                    System.out.println("❌ Password must be at least 6 characters!");
                    if (!InputHelper.confirm("\nTry again?")) {
                        return;
                    }
                }
            }

            // Get name
            System.out.print("Full Name: ");
            String name = InputHelper.getString();

            // Get role
            System.out.println("\nSelect Role:");
            System.out.println("1. Applicant (Job Seeker)");
            System.out.println("2. Recruiter (HR)");
            System.out.print("Choice (1-2): ");
            int roleChoice = InputHelper.getInt();

            String role;
            String phone = "";

            // Create User object
            User user = new User();
            user.setUsername(name);
            user.setEmail(email);
            user.setPassword(password);

            if (roleChoice == 1) {
                // ========================================
                // APPLICANT REGISTRATION
                // ========================================
                role = "APPLICANT";
                user.setRole(role);

                System.out.println("\n--- Applicant Profile Setup ---");

                // Get phone (REQUIRED for applicants)
                boolean validPhone = false;
                while (!validPhone) {
                    System.out.print("Phone Number (must start with 0 and be 11 digits): ");
                    System.out.println("   Example: 01234567890");
                    phone = InputHelper.getString();

                    if (phone.matches("^0\\d{10}$")) {
                        validPhone = true;
                        user.setPhone(phone);
                    } else {
                        System.out.println("❌ Invalid phone format!");
                        System.out.println("   Phone must:");
                        System.out.println("   • Start with 0");
                        System.out.println("   • Be exactly 11 digits");
                        System.out.println("   • Example: 01234567890");

                        if (!InputHelper.confirm("\nTry again?")) {
                            return;
                        }
                    }
                }

                // Optional: Get skills
                System.out.print("Skills (comma-separated) [Optional, press Enter to skip]: ");
                String skills = InputHelper.getString();
                if (!skills.isEmpty()) {
                    user.setSkills(skills);
                }

                // Optional: Get experience
                System.out.print("Experience (e.g., '3 years in Java') [Optional, press Enter to skip]: ");
                String experience = InputHelper.getString();
                if (!experience.isEmpty()) {
                    user.setExperience(experience);
                }

            } else if (roleChoice == 2) {
                // ========================================
                // RECRUITER REGISTRATION
                // ========================================
                role = "RECRUITER";
                user.setRole(role);

                System.out.println("\n--- Recruiter Profile Setup ---");

                // Get department (REQUIRED)
                String department = "";
                while (department.isEmpty()) {
                    System.out.print("Department (e.g., HR, IT, Sales): ");
                    department = InputHelper.getString();

                    if (department.isEmpty()) {
                        System.out.println("❌ Department is required!");
                    }
                }
                user.setDepartment(department);

                // ✅ Company - REMOVED (not required)

                // Position (OPTIONAL)
                System.out.print("Position (e.g., HR Manager) [Optional, press Enter to skip]: ");
                String position = InputHelper.getString();
                if (!position.isEmpty()) {
                    user.setPosition(position);
                }

                // Phone (OPTIONAL)
                System.out.print("Phone (11 digits starting with 0) [Optional, press Enter to skip]: ");
                phone = InputHelper.getString();
                if (!phone.isEmpty()) {
                    // Validate phone format if provided
                    if (phone.matches("^0\\d{10}$")) {
                        user.setPhone(phone);
                    } else {
                        System.out.println("⚠️  Invalid phone format, skipping phone number...");
                    }
                }

            } else {
                System.out.println("❌ Invalid role choice!");
                return;
            }

            System.out.println("\n📤 Registering.. .");

            // Register with new User-based method
            Session session = authService.register(user);

            if (session != null) {
                System.out.println("\n✅ Registration successful!");
                System.out.println("╔════════════════════════════════════════╗");
                System.out.println("║         Account Created                ║");
                System.out.println("╚════════════════════════════════════════╝");
                System.out.println("   User ID:   " + session.getUserId());
                System.out.println("   Email:    " + session.getUserEmail());
                System.out.println("   Role:     " + session.getRole());
                if (!phone.isEmpty()) {
                    System.out.println("   Phone:    " + phone);
                }

                System.out.println("\n💡 You are now logged in!");
                System.out.println("   Redirecting to your dashboard...\n");

                InputHelper.pause();

                // Store session and redirect to appropriate menu
                currentSession = session;

                if (currentSession.getRole().equals("APPLICANT")) {
                    new ApplicantMenu(client, currentSession).run();
                } else if (currentSession.getRole().equals("RECRUITER")) {
                    new RecruiterMenu(client, currentSession).run();
                }

            } else {
                System.out.println("❌ Registration failed!");
                System.out.println("   Please try again.");
            }

        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
