package com.qrattendance.security;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TokenValidator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    /**
     * Validates if the scanned token matches active session credentials
     * and strictly enforces the 30-second expiration window to block late screenshots.
     */
    public static boolean isValidToken(String token, String expectedSubject, String expectedSecretWord) {
        if (token == null || token.trim().isEmpty() || expectedSubject == null || expectedSecretWord == null) {
            return false;
        }

        // Expected format prefix: SUBJECT_SECRETWORD_
        String prefix = expectedSubject.trim() + "_" + expectedSecretWord.trim() + "_";
        if (!token.startsWith(prefix)) {
            System.err.println("[SECURITY] Token prefix mismatch or invalid format.");
            return false;
        }

        // Extract timestamp substring following the prefix
        String timestampStr = token.substring(prefix.length());

        try {
            LocalDateTime tokenTime = LocalDateTime.parse(timestampStr, FORMATTER);
            LocalDateTime now = LocalDateTime.now();

            // Calculate exact seconds elapsed between token creation and current scan submission
            long secondsElapsed = ChronoUnit.SECONDS.between(tokenTime, now);

            System.out.println("[SECURITY] Token age: " + secondsElapsed + " seconds.");

            // STRICT RULE: Must be between 0 (accounting for minor clock skews) and 30 seconds old.
            // Anything > 30 seconds is a stale screenshot or expired code.
            if (secondsElapsed >= 0 && secondsElapsed <= 30) {
                return true;
            } else {
                System.out.println("[REJECTED] Token expired! Age was " + secondsElapsed + "s (Limit: 30s).");
                return false;
            }

        } catch (Exception e) {
            System.err.println("[VALIDATION ERROR] Failed to parse token timestamp: " + e.getMessage());
            return false;
        }
    }
}
