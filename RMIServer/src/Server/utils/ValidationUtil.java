package Server.utils;

import java.util.regex.Pattern;
import java.util.regex. Matcher;

public class ValidationUtil {

    private static final String EMAIL_PATTERN =
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final String PHONE_PATTERN = "^0\\d{10}$";

    private static final int MIN_PASSWORD_LENGTH = 6;

    /**
     * Validate email format
     * Example valid: user@example.com, john.doe@company.co.uk
     * Example invalid: user@, @example.com, user. example.com
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email. trim());
        return matcher.matches();
    }

    /**
     * Validate phone number
     * Must start with 0 and be exactly 11 digits
     * Example valid: 01234567890, 01012345678
     * Example invalid: 1234567890 (doesn't start with 0), 012345678 (too short), 0123456789012 (too long)
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone. trim());
        return matcher.matches();
    }

    /**
     * Validate password length
     * Must be at least 6 characters
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    /**
     * Validate name (not empty)
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.trim().length() >= 2;
    }

    /**
     * Get email validation error message
     */
    public static String getEmailErrorMessage() {
        return "Invalid email format.  Example: user@example.com";
    }

    /**
     * Get phone validation error message
     */
    public static String getPhoneErrorMessage() {
        return "Invalid phone number. Must start with 0 and be exactly 11 digits.  Example: 01234567890";
    }

    /**
     * Get password validation error message
     */
    public static String getPasswordErrorMessage() {
        return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long. ";
    }

    /**
     * Get name validation error message
     */
    public static String getNameErrorMessage() {
        return "Name must be at least 2 characters long. ";
    }
}