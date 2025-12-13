package Server;

import Server.utils.PasswordUtil;

/**
 * Test password hashing and verification
 */
public class TestPassword {

    public static void main(String[] args) {
        String testPassword = "Admin@123456";

        System.out.println("🔐 Password Test");
        System.out.println("================================================");
        System.out.println("Original password: " + testPassword);
        System.out.println();

        String hash1 = PasswordUtil.hashPassword(testPassword);
        System.out.println("Hash 1: " + hash1);

        System.out.println("\n✅ Verifying hash 1 with original password...");
        boolean matches1 = PasswordUtil.verifyPassword(testPassword, hash1);
        System.out.println("Result: " + (matches1 ? "✅ MATCH" : "❌ NO MATCH"));

        String hash2 = PasswordUtil.hashPassword(testPassword);
        System.out.println("\nHash 2: " + hash2);
        System.out.println("Hash 1 == Hash 2? " + hash1.equals(hash2));

        System.out.println("\n✅ Verifying hash 2 with original password...");
        boolean matches2 = PasswordUtil.verifyPassword(testPassword, hash2);
        System.out.println("Result: " + (matches2 ? "✅ MATCH" : "❌ NO MATCH"));

        System.out.println("\n❌ Verifying hash 1 with WRONG password...");
        boolean wrongMatch = PasswordUtil.verifyPassword("WrongPassword", hash1);
        System.out.println("Result: " + (wrongMatch ? "✅ MATCH (BAD!)" : "❌ NO MATCH (GOOD!)"));
    }
}