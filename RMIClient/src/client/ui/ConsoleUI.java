package client.ui;

import client.RMIClient;
import client.utils.InputHelper;

public class ConsoleUI {

    private RMIClient client;

    public ConsoleUI() {
        this.client = null;
    }

    public void start() {
        showWelcome();

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = InputHelper.getInt();
            System.out.println();

            switch (choice) {
                case 1:
                    startServer();
                    break;
                case 2:
                    startClient();
                    break;
                case 3:
                    startBoth();
                    break;
                case 4:
                    runInteractiveTests();
                    break;
                case 5:
                    runUserApplication();
                    break;
                case 0:
                    running = false;
                    System.out.println("👋 Goodbye!");
                    break;
                default:
                    System.out.println("❌ Invalid choice!  Please try again.");
            }
        }
    }

    private void showWelcome() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║    Job Recruitment System - Main      ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private void showMainMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           Main Menu                    ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("1. Start Server");
        System.out.println("2. Start Client");
        System.out.println("3. Start Both");
        System.out.println("4. Interactive Tests (Dev Mode)");
        System.out.println("5. User Application (Production Mode)");
        System.out.println("0. Exit");
        System.out.println("═══════════════════════════════════════════");
        System.out.print("Choice: ");
    }

    private void startServer() {
        System.out.println("⚠️  Server must be started separately!");
        System.out.println("\nTo start the server:");
        System.out.println("1. Open a new terminal");
        System.out.println("2. Run: cd RMIServer");
        System.out.println("3. Run: java -cp \"bin;../Shared/bin;lib/*\" server.Main");
        System.out.println("\nOr open RMIServer/src/server/Main.java in VS Code and click Run");
        InputHelper.pause();
    }

    private void startClient() {
        if (client != null) {
            System.out.println("⚠️  Client already connected!");
            return;
        }

        System.out.println("🔗 Connecting to RMI Server...");

        try {
            client = new RMIClient();
            System.out.println("✅ Client connected successfully!");
            System.out.println("\n💡 You can now run interactive tests (option 4 or 5).");
            InputHelper.pause();

        } catch (Exception e) {
            System.err.println("❌ Client connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\n💡 Make sure:");
            System.err.println("   1. MongoDB is running (port 27017)");
            System.err.println("   2. RMI Server is running (port 1099)");
            InputHelper.pause();
        }
    }

    private void startBoth() {
        System.out.println("⚠️  Cannot start both from client!");
        System.out.println("\nPlease start server and client separately:");
        System.out.println("\n📋 Step 1: Start MongoDB");
        System.out.println("   mongod --port 27020 --dbpath D:\\mongodb-data\\JobRecruitmentDB");
        System.out.println("\n📋 Step 2: Start Server");
        System.out.println("   Open RMIServer/src/server/Main.java and click Run");
        System.out.println("\n📋 Step 3: Start Client");
        System.out.println("   Choose option 2 from this menu");
        InputHelper.pause();
    }

    private void runInteractiveTests() {
        if (client == null) {
            System.out.println("\n⚠️  Client not connected!");
            if (InputHelper.confirm("Connect now?")) {
                startClient();
                if (client == null) return;
            } else {
                return;
            }
        }

        boolean back = false;
        while (! back) {
            showTestMenu();
            int choice = InputHelper.getInt();
            System.out.println();

            switch (choice) {
                case 1:
                    new ApplicantServiceTest(client).run();
                    break;
                case 2:
                    new RecruiterServiceTest(client).run();
                    break;
                case 3:
                    new JobServiceTest(client).run();
                    break;
                case 4:
                    new ApplicationServiceTest(client).run();
                    break;
                case 5:
                    new AuthServiceTest(client).run();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System. out.println("❌ Invalid choice!");
            }
        }
    }

    private void showTestMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       Interactive Tests Menu           ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("1. Test Applicant Service");
        System.out.println("2. Test Recruiter Service");
        System.out.println("3. Test Job Service");
        System.out.println("4. Test Application Service");
        System.out.println("5. Test Auth Service");
        System.out.println("0. Back to Main Menu");
        System.out.print("\nChoice: ");
    }

    private void runUserApplication() {
        if (client == null) {
            System.out.println("\n⚠️  Client not connected!");
            if (InputHelper.confirm("Connect now?")) {
                startClient();
                if (client == null) return;
            } else {
                return;
            }
        }

        System.out.println("\n🚀 Starting User Application...\n");

        UserApplication app = new UserApplication(client);
        app.run();
    }
}
