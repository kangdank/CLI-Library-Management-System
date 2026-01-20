package com.library.util;

import java.util.regex.Pattern;

public class InputValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[+]?[0-9]{10,15}$");

    private static final Pattern ISBN_PATTERN =
            Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");

    private InputValidator() {
    }


    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }


    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }


    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.replaceAll("[\\s-]", "")).matches();
    }


    public static boolean isValidIsbn(String isbn) {
        if (!isNotEmpty(isbn)) {
            return false;
        }
        return ISBN_PATTERN.matcher(isbn.replaceAll("[\\s-]", "")).matches();
    }


    public static boolean isValidPublicationYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        return year >= 1450 && year <= currentYear;
    }


    public static boolean hasMinLength(String value, int minLength) {
        return isNotEmpty(value) && value.trim().length() >= minLength;
    }


    public static boolean hasMaxLength(String value, int maxLength) {
        return value != null && value.trim().length() <= maxLength;
    }

    public static boolean isAlphabetic(String value) {
        if (!isNotEmpty(value)) {
            return false;
        }
        return value.matches("^[a-zA-Z\\s]+$");
    }

    public static boolean isPositiveInteger(int value) {
        return value > 0;
    }

    public static boolean isNonNegative(double value) {
        return value >= 0;
    }
}