package client.ui;

import client.RMIClient;
import client.utils.InputHelper;
import shared.models.Applicant;
import shared.interfaces.IApplicantService;

import java.util.List;

public class ApplicantServiceTest {

    private RMIClient client;
    private IApplicantService service;

    public ApplicantServiceTest(RMIClient client) {
        this.client = client;
    }

    public void run() {
        try {
            service = client.getApplicantService();

            boolean back = false;
            while (! back) {
                showMenu();
                int choice = InputHelper.getInt();
                System. out.println();

                switch (choice) {
                    case 1: createApplicant(); break;
                    case 2: getApplicantById(); break;
                    case 3: getAllApplicants(); break;
                    case 4: searchBySkill(); break;
                    case 5: updateApplicant(); break;
                    case 6: deleteApplicant(); break;
                    case 0: back = true; break;
                    default: System.out.println("❌ Invalid choice!");
                }

                if (! back) InputHelper.pause();
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e. getMessage());
            e.printStackTrace();
        }
    }

    private void showMenu() {

        System.out.println("║      APPLICANT SERVICE TEST            ║");
        System.out. println("1. Create Applicant");
        System.out.println("2. Get Applicant by ID");
        System.out. println("3. Get All Applicants");
        System.out. println("4. Search by Skill");
        System.out.println("5. Update Applicant");
        System.out. println("6. Delete Applicant");
        System.out.println("0. Back");
        System.out.print("\nChoice: ");
    }

   private void createApplicant() {
    try {
        System.out.println("=== CREATE APPLICANT ===\n");

        System.out.print("Name: ");
        String name = InputHelper.getString();

        System.out.print("Email (e.g., user@example.com): ");
        String email = InputHelper.getString();

        System.out.print("Phone (must start with 0 and be 11 digits, e.g., 01234567890): ");
        String phone = InputHelper.getString();

        // Client-side validation hint
        if (! phone.isEmpty() && !phone.matches("^0\\d{10}$")) {
            System.out.println("⚠️  Warning: Phone format might be invalid!");
            System.out.println("   Phone must start with 0 and be exactly 11 digits.");
            if (! InputHelper.confirm("Continue anyway?  ")) {
                return;
            }
        }

        System.out.print("Education:  ");
        String education = InputHelper.getString();

        System.out.print("Experience (years): ");
        int experience = InputHelper.getInt();

        System.out.print("Resume:  ");
        String resume = InputHelper.getString();

        Applicant applicant = new Applicant(name, email, phone, resume, education, experience);

        System.out.println("\nAdd skills (type 'done' when finished):");
        while (true) {
            System.out.print("  Skill: ");
            String skill = InputHelper.getString();
            if (skill.equalsIgnoreCase("done")) break;
            if (! skill.trim().isEmpty()) {
                applicant.addSkill(skill. trim());
            }
        }

        System.out. println("\n📤 Creating applicant...");

        try {
            String id = service.createApplicant(applicant);

            System.out.println("✅ SUCCESS!");
            System.out.println("   ID: " + id);
            System.out.println("   Name: " + name);
            System.out.println("   Email: " + email);
            System.out.println("   Phone: " + phone);
            System.out.println("   Skills: " + applicant.getSkills());

        } catch (Exception e) {
            System.err.println("❌ Creation failed: " + e.getMessage());
            System.err.println("\nCheck:");
            System.err.println("   • Email format (must be like user@example.com)");
            System.err.println("   • Phone format (must start with 0 and be 11 digits)");
        }

    } catch (Exception e) {
        System.err.println("❌ Error: " + e.getMessage());
    }
}

    private void getApplicantById() {
        try {
            System.out.println("=== GET APPLICANT BY ID ===\n");
            System.out.print("Enter ID:  ");
            String id = InputHelper.getString();

            System.out.println("\n📤 Fetching.. .");
            Applicant applicant = service.getApplicantById(id);

            if (applicant != null) {
                System.out.println("✅ FOUND!\n");
                printApplicant(applicant);
            } else {
                System. out.println("❌ Not found!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void getAllApplicants() {
        try {
            System.out.println("=== GET ALL APPLICANTS ===\n");
            System.out.println("📤 Fetching.. .");

            List<Applicant> all = service.getAllApplicants();

            System.out.println("✅ Found " + all.size() + " applicant(s)\n");

            if (all.isEmpty()) {
                System.out.println("No applicants in database.");
            } else {
                for (int i = 0; i < all.size(); i++) {
                    System.out.println("─────────────────────────────────────");
                    System.out.println("Applicant " + (i + 1) + ":");
                    printApplicant(all.get(i));
                }
                System.out.println("─────────────────────────────────────");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void searchBySkill() {
        try {
            System.out.println("=== SEARCH BY SKILL ===\n");
            System.out.print("Enter skill: ");
            String skill = InputHelper.getString();

            System.out.println("\n📤 Searching...");
            List<Applicant> results = service.searchApplicantsBySkill(skill);

            System.out.println("✅ Found " + results.size() + " applicant(s)\n");

            if (results. isEmpty()) {
                System.out.println("No matches found.");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    System.out.println("─────────────────────────────────────");
                    System.out.println("Match " + (i + 1) + ":");
                    printApplicant(results.get(i));
                }
                System. out.println("─────────────────────────────────────");
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void updateApplicant() {
        try {
            System.out.println("=== UPDATE APPLICANT ===\n");
            System. out.print("Enter ID: ");
            String id = InputHelper. getString();

            System. out.println("\n📤 Fetching...");
            Applicant applicant = service.getApplicantById(id);

            if (applicant == null) {
                System.out.println("❌ Not found!");
                return;
            }

            System.out. println("✅ Found:  " + applicant.getName());
            System.out.println("\n--- Update Fields (Enter to skip) ---");

            System.out.print("New Phone [" + applicant.getPhone() + "]: ");
            String phone = InputHelper.getString();
            if (!phone.isEmpty()) applicant.setPhone(phone);

            System.out.print("New Education [" + applicant.getEducation() + "]: ");
            String education = InputHelper.getString();
            if (!education.isEmpty()) applicant.setEducation(education);

            if (InputHelper.confirm("\nAdd skill? ")) {
                System.out. print("Skill: ");
                applicant.addSkill(InputHelper.getString());
            }

            System.out.println("\n📤 Updating.. .");
            boolean updated = service.updateApplicant(applicant);

            System.out.println(updated ? "✅ Updated!" : "❌ Failed!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void deleteApplicant() {
        try {
            System. out.println("=== DELETE APPLICANT ===\n");
            System.out.print("Enter ID: ");
            String id = InputHelper.getString();

            System.out.println("\n📤 Fetching...");
            Applicant applicant = service.getApplicantById(id);

            if (applicant == null) {
                System.out.println("❌ Not found!");
                return;
            }

            System.out.println("✅ Found:");
            printApplicant(applicant);

            if (! InputHelper.confirm("\n⚠️ Delete this applicant?")) {
                System.out.println("❌ Cancelled.");
                return;
            }

            System.out.println("\n📤 Deleting...");
            boolean deleted = service.deleteApplicant(id);

            System.out.println(deleted ?  "✅ Deleted!" :  "❌ Failed!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void printApplicant(Applicant a) {
        System.out. println("  ID: " + a.getId());
        System.out.println("  Name: " + a.getName());
        System.out.println("  Email: " + a.getEmail());
        System.out.println("  Phone: " + a.getPhone());
        System.out. println("  Education: " + a.getEducation());
        System.out.println("  Experience: " + a.getExperience() + " years");
        System.out.println("  Skills: " + a.getSkills());
    }
}
