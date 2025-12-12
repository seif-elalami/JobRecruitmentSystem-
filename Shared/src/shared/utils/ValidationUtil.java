package shared.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ValidationUtil {

    // Email regex pattern (standard email format)
    private static final String EMAIL_PATTERN =
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // Phone pattern (must start with 0 and be exactly 11 digits)
    private static final String PHONE_PATTERN = "^0\\d{10}$";

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email.trim());
        return matcher.matches();
    }

    /**
     * Validate phone number (must start with 0 and be exactly 11 digits)
     */
    public static boolean validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone.trim());
        return matcher.matches();
    }

    /**
     * Validate password (minimum 6 characters)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Validate name (not empty, reasonable length)
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() >= 2 && name.length() <= 100;
    }
}
