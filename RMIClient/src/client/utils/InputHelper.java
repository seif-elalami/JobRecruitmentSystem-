package client.utils;

import java.util.Scanner;

public class InputHelper {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * Get integer input from user
     */
    public static int getInt() {
        try {
            int value = scanner. nextInt();
            scanner.nextLine(); // Consume newline
            return value;
        } catch (Exception e) {
            scanner.nextLine(); // Clear bad input
            return -1;
        }
    }

    /**
     * Get string input from user
     */
    public static String getString() {
        return scanner.nextLine();
    }

    /**
     * Get double input from user
     */
    public static double getDouble() {
        try {
            double value = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            return value;
        } catch (Exception e) {
            scanner.nextLine(); // Clear bad input
            return -1;
        }
    }

    /**
     * Pause and wait for Enter
     */
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Get yes/no confirmation
     */
    public static boolean confirm(String message) {
        System.out.print(message + " (y/n): ");
        String answer = scanner.nextLine();
        return answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes");
    }

    /**
     * Close scanner (call when program exits)
     */
    public static void close() {
        scanner.close();
    }
}