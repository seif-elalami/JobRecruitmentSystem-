package client.ui;

import client.RMIClient;
import client.utils.InputHelper;

public class ConsoleUI {

    private RMIClient client;

    public void start() {
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
                    runInteractiveTests();  // Dev mode - no login
                    break;
                case 5:
                    runUserApplication();   // Production mode - login required
                    break;
                case 0:
                    running = false;
                    System.out.println("\n👋 Goodbye!");
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }

            if (running && choice != 1) {
                InputHelper.pause();
            }
        }

        InputHelper.close();
    }

    private void showMainMenu() {
        System.out. println("\n╔════════════════════════════════════════╗");
        System.out.println("║    Job Recruitment System - Main      ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out. println();
        System.out.println("1. Start Server");
        System.out. println("2. Start Client");
        System.out.println("3. Start Both");
        System.out.println("4. Interactive Tests (Dev Mode)");
        System.out.println("5. User Application (Production Mode)");
        System.out.println("0. Exit");
        System.out.println("═══════════════════════════════════════════");
        System.out.print("Choice: ");
    }

    private void startServer() {
        System.out.println("\n⚠️  Please start the RMI Server separately using:");
        System.out.println("   cd RMIServer");
        System.out.println("   run.bat");
        System.out.println("\n💡 The server and client should run in separate processes.");
    }

    private void startClient() {
        try {
            System.out.println("\n🔗 Connecting to RMI Server...");
            client = new RMIClient();
            System.out.println("✅ Client connected successfully!");
            System.out.println("\n💡 You can now run interactive tests (option 4 or 5).");
        } catch (Exception e) {
            System.err.println("\n❌ Client connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\n💡 Make sure:");
            System.err.println("   1. MongoDB is running (port 27020)");
            System.err. println("   2. RMI Server is running (port 1099)");
        }
    }

    private void startBoth() {
        System.out.println("\n⚠️  Server and client should run in separate processes.");
        System.out.println("\n💡 Please:");
        System.out.println("   1. Start the server: cd RMIServer && run.bat");
        System.out.println("   2. Start the client: cd RMIClient && run.bat");
        System.out.println("\nThen use option 2 to connect to the server.");
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
        while (!back) {
            showTestMenu();
            int choice = InputHelper.getInt();
            System.out.println();

            switch (choice) {
                case 1:
                    new ApplicantServiceTest(client).run();
                    break;
                case 2:
                    new JobServiceTest(client).run();
                    break;
                case 3:
                    new ApplicationServiceTest(client).run();
                    break;
                case 4:
                    new AuthServiceTest(client).run();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out. println("❌ Invalid choice!");
            }
        }
    }

    private void showTestMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       Interactive Tests Menu           ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out. println("1. Test Applicant Service");
        System.out.println("2. Test Job Service");
        System.out.println("3. Test Application Service");
        System.out.println("4. Test Auth Service");
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

        UserApplication app = new UserApplication(client);
        app.run();
    }
}
