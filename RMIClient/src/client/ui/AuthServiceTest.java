package client.ui;

import client.RMIClient;
import client.utils.InputHelper;
import shared.models.User;
import shared.models.Session;
import shared.interfaces.IAuthService;

public class AuthServiceTest {

    private RMIClient client;
    private IAuthService service;

    public AuthServiceTest(RMIClient client) {
        this.client = client;
    }

    public void run() {
        try {
            service = client.getAuthService();

            boolean back = false;
            while (!back) {
                showMenu();
                int choice = InputHelper.getInt();
                System.out.println();

                switch (choice) {
                    case 1: registerUser(); break;
                    case 2: login(); break;
                    case 3: getUserByEmail(); break;
                    case 4: getUserById(); break;
                    case 5: changePassword(); break;
                    case 0: back = true; break;
                    default: System.out.println("❌ Invalid choice!");
                }

                if (! back) InputHelper.pause();
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showMenu() {
        System.out. println("\n╔════════════════════════════════════════╗");
        System.out. println("║        AUTH SERVICE TEST               ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out. println("1. Register User");
        System.out.println("2. Login");
        System.out.println("3. Get User by Email");
        System.out.println("4. Get User by ID");
        System.out.println("5. Change Password");
        System.out.println("0. Back");
        System.out.print("\nChoice: ");
    }

   private void registerUser() {
    try {
        System.out.println("=== REGISTER USER ===\n");

        System.out.print("Username (Full Name): ");
        String username = InputHelper.getString();

        System.out.print("Email: ");
        String email = InputHelper.getString();

        System.out.print("Password (min 6 chars): ");
        String password = InputHelper.getString();

        System.out.println("\nRole options:");
        System.out.println("  1. APPLICANT");
        System.out.println("  2. RECRUITER");
        System.out.print("Choose role (1-2): ");
        int roleChoice = InputHelper.getInt();

        String role;
        if (roleChoice == 1) {
            role = "APPLICANT";
        } else if (roleChoice == 2) {
            role = "RECRUITER";
        } else {
            System.out.println("❌ Invalid role!");
            return;
        }

        // ✅ Create User object with correct constructor
        User user = new User(username, password, email, role);

        System.out.println("\n📤 Registering user...");
        Session session = service.register(user);

        if (session != null) {
            System.out.println("✅ Registration successful!");
            System.out.println("   User ID: " + session.getUserId());
            System.out.println("   Email: " + session.getUserEmail());
            System.out. println("   Role: " + session.getRole());
            System. out.println("\n💡 You are now logged in!");
        } else {
            System.out.println("❌ Registration failed!");
        }

    } catch (Exception e) {
        System.err.println("❌ Error: " + e.getMessage());
        e.printStackTrace();
    }
}


    private void login() {
        try {
            System.out.println("=== LOGIN ===\n");

            System.out.print("Email: ");
            String email = InputHelper.getString();

            System. out.print("Password: ");
            String password = InputHelper.getString();

            System.out.println("\n📤 Logging in...");
            Session session = service.login(email, password);

            if (session != null) {
                System.out.println("✅ Login successful!\n");
                System.out. println("Session Details:");
                System.out. println("  User ID:     " + session.getUserId());
                System.out.println("  Email:      " + session.getUserEmail());
                System.out.println("  Role:       " + session.getRole());
                System.out. println("  Login Time: " + session.getLoginTime());
                System.out.println("  Expired:    " + (session.isExpired() ? "Yes" : "No"));

                if (! session.isExpired()) {
                    System.out.println("  Time Left:   " + session.getRemainingMinutes() + " minutes");
                }
            } else {
                System.out.println("❌ Login failed!");
                System.out.println("   Invalid email or password.");
            }

        } catch (Exception e) {
            System. err.println("❌ Error: " + e.getMessage());
        }
    }

    private void getUserByEmail() {
        try {
            System.out. println("=== GET USER BY EMAIL ===\n");
            System.out.print("Enter Email: ");
            String email = InputHelper.getString();

            System.out.println("\n📤 Fetching user.. .");
            User user = service. getUserByEmail(email);

            if (user != null) {
                System.out.println("✅ FOUND!\n");
                printUser(user);
            } else {
                System.out.println("❌ User not found!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e. getMessage());
        }
    }

    private void getUserById() {
        try {
            System. out.println("=== GET USER BY ID ===\n");
            System.out.print("Enter User ID: ");
            String id = InputHelper.getString();

            System.out.println("\n📤 Fetching user...");
            User user = service.getUserById(id);

            if (user != null) {
                System.out.println("✅ FOUND!\n");
                printUser(user);
            } else {
                System.out.println("❌ User not found!");
            }

        } catch (Exception e) {
            System. err.println("❌ Error: " + e.getMessage());
        }
    }

    private void changePassword() {
        try {
            System.out.println("=== CHANGE PASSWORD ===\n");

            System.out.print("Email: ");
            String email = InputHelper.getString();

            System. out.print("Old Password: ");
            String oldPassword = InputHelper.getString();

            System.out.print("New Password (min 6 chars): ");
            String newPassword = InputHelper.getString();

            System.out.print("Confirm New Password: ");
            String confirmPassword = InputHelper. getString();

            if (! newPassword.equals(confirmPassword)) {
                System.out.println("❌ Passwords don't match!");
                return;
            }

            System.out.println("\n📤 Changing password...");
            boolean changed = service.changePassword(email, oldPassword, newPassword);

            if (changed) {
                System.out.println("✅ Password changed successfully!");
                System.out. println("💡 Please login again with your new password.");
            } else {
                System.out. println("❌ Password change failed!");
                System.out.println("   Possible reasons:");
                System.out.println("   - Old password is incorrect");
                System.out.println("   - Email not found");
                System.out.println("   - New password too short (min 6 chars)");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void printUser(User u) {
        System.out.println("  User ID:    " + u.getUserId());
        System.out. println("  Username:   " + u.getUsername());
        System.out.println("  Email:     " + u. getEmail());
        System.out. println("  Role:      " + u.getRole());
    }
}
