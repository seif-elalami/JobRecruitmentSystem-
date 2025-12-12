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
        
        // Hash the password
        String hash1 = PasswordUtil.hashPassword(testPassword);
        System.out.println("Hash 1: " + hash1);
        
        // Verify with the same hash
        System.out.println("\n✅ Verifying hash 1 with original password...");
        boolean matches1 = PasswordUtil.verifyPassword(testPassword, hash1);
        System.out.println("Result: " + (matches1 ? "✅ MATCH" : "❌ NO MATCH"));
        
        // Hash again to see if it's different
        String hash2 = PasswordUtil.hashPassword(testPassword);
        System.out.println("\nHash 2: " + hash2);
        System.out.println("Hash 1 == Hash 2? " + hash1.equals(hash2));
        
        // Verify hash2 with original password
        System.out.println("\n✅ Verifying hash 2 with original password...");
        boolean matches2 = PasswordUtil.verifyPassword(testPassword, hash2);
        System.out.println("Result: " + (matches2 ? "✅ MATCH" : "❌ NO MATCH"));
        
        // Test with wrong password
        System.out.println("\n❌ Verifying hash 1 with WRONG password...");
        boolean wrongMatch = PasswordUtil.verifyPassword("WrongPassword", hash1);
        System.out.println("Result: " + (wrongMatch ? "✅ MATCH (BAD!)" : "❌ NO MATCH (GOOD!)"));
    }
}
