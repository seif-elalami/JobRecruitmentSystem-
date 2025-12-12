package client;

import client.ui.ConsoleUI;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("╔════════════════════════════════════════╗");
            System. out.println("║    Job Recruitment System - Client    ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println();

            // Start the console UI4
            ConsoleUI ui = new ConsoleUI();
            ui.start();

        } catch (Exception e) {
            System. err.println("\n❌ Client failed to start!");
            System.err. println("Error: " + e. getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
