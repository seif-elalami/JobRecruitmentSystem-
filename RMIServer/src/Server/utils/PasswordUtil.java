package Server.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Hash a password using BCrypt
     * @param password Plain text password
     * @return BCrypt hashed password (starts with $2a$)
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        try {

            String hash = BCrypt.hashpw(password, BCrypt.gensalt());

            System.out.println("🔐 Password hashed with BCrypt");
            System.out.println("   Hash format: " + hash.substring(0, Math.min(20, hash.length())) + "...");

            return hash;

        } catch (Exception e) {
            System.err.println("❌ Error hashing password: " + e.getMessage());
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verify a password against a BCrypt hash
     * @param password Plain text password to verify
     * @param storedHash BCrypt hash from database
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (password == null || storedHash == null) {
            System.err.println("❌ Password or hash is null");
            return false;
        }

        try {
            System.out.println("🔍 Verifying password with BCrypt");
            System.out.println("   Hash format: " + storedHash.substring(0, Math.min(20, storedHash.length())) + "...");

            if (! isValidBCryptHash(storedHash)) {
                System.err.println("❌ Invalid BCrypt hash format!");
                System.err.println("   Expected: $2a$10$...");
                System.err.println("   Actual:   " + storedHash.substring(0, Math.min(20, storedHash.length())));

                System.out.println("⚠️  Attempting legacy SHA-256 verification.. .");
                return verifyPasswordLegacy(password, storedHash);
            }

            boolean matches = BCrypt.checkpw(password, storedHash);
            System.out.println("   Result: " + (matches ? "✅ MATCH" : "❌ NO MATCH"));

            return matches;

        } catch (Exception e) {
            System.err.println("❌ Error verifying password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a hash is a valid BCrypt format
     */
    public static boolean isValidBCryptHash(String hash) {
        return hash != null &&
               hash.length() >= 60 &&
               (hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"));
    }

    /**
     * LEGACY:  Verify passwords hashed with old SHA-256 method
     * This is temporary to support existing users
     * Once all users re-register, this can be removed
     */
    private static boolean verifyPasswordLegacy(String password, String storedHash) {
        try {

            byte[] combined = java.util.Base64.getDecoder().decode(storedHash);

            byte[] salt = new byte[16];
            System.arraycopy(combined, 0, salt, 0, 16);

            byte[] storedPasswordHash = new byte[combined.length - 16];
            System.arraycopy(combined, 16, storedPasswordHash, 0, storedPasswordHash.length);

            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] testPasswordHash = md.digest(password.getBytes("UTF-8"));

            boolean matches = java.security.MessageDigest.isEqual(storedPasswordHash, testPasswordHash);
            System.out.println("   Legacy SHA-256 result: " + (matches ? "✅ MATCH" : "❌ NO MATCH"));

            return matches;

        } catch (Exception e) {
            System.err.println("❌ Legacy verification failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate password strength
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}