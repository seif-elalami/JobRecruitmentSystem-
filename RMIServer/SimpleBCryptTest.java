import org.mindrot.jbcrypt.BCrypt;

public class SimpleBCryptTest {
    public static void main(String[] args) {
        String password = "Admin@123456";
        
        System.out.println("Testing BCrypt directly:");
        System.out.println("Password: " + password);
        System.out.println();
        
        // Generate hash
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Generated hash: " + hash);
        System.out.println("Hash length: " + hash.length());
        System.out.println();
        
        // Test verification
        boolean matches = BCrypt.checkpw(password, hash);
        System.out.println("Verification result: " + matches);
        System.out.println("Hash first 60 chars: " + hash.substring(0, Math.min(60, hash.length())));
    }
}
