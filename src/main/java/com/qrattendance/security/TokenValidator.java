package com.qrattendance.security;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TokenValidator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    /**
     * Validates if the scanned token matches the active session credentials
     * and was generated within an acceptable 30-second temporal window.
     */
    public static boolean isValidToken(String token, String expectedSubject, String expectedSecretWord) {
        if (token == null || token.trim().isEmpty() || expectedSubject == null || expectedSecretWord == null) {
            return false;
        }

        // Expected format prefix: SUBJECT_SECRETWORD_
        String prefix = expectedSubject.trim() + "_" + expectedSecretWord.trim() + "_";
        if (!token.startsWith(prefix)) {
            return false; // Subject or Secret Word mismatch
        }

        // Extract timestamp substring following the prefix
        String timestampStr = token.substring(prefix.length());

        try {
            LocalDateTime tokenTime = LocalDateTime.parse(timestampStr, FORMATTER);
            LocalDateTime now = LocalDateTime.now();

            // Calculate age of the token in seconds
            long secondsElapsed = ChronoUnit.SECONDS.between(tokenTime, now);

            // Valid if token is between 0 and 30 seconds old (current active window + grace period)
            // Rejects future-dated tokens or tokens older than 30 seconds (stale screenshots)
            return secondsElapsed >= 0 && secondsElapsed <= 30;

        } catch (Exception e) {
            System.err.println("[VALIDATION ERROR] Failed to parse token timestamp: " + e.getMessage());
            return false;
        }
    }
}
