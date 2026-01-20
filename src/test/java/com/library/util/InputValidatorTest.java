package com.library.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class InputValidatorTest {

    @Test
    void testIsNotEmpty_WithValidString_ReturnsTrue() {
        assertTrue(InputValidator.isNotEmpty("Hello"));
        assertTrue(InputValidator.isNotEmpty("   text   "));
    }

    @Test
    void testIsNotEmpty_WithEmptyString_ReturnsFalse() {
        assertFalse(InputValidator.isNotEmpty(""));
        assertFalse(InputValidator.isNotEmpty("   "));
        assertFalse(InputValidator.isNotEmpty(null));
    }

    @Test
    void testIsValidEmail_WithValidEmails_ReturnsTrue() {
        assertTrue(InputValidator.isValidEmail("user@email.com"));
        assertTrue(InputValidator.isValidEmail("test.user@domain.co.uk"));
        assertTrue(InputValidator.isValidEmail("ten+tag@email.com"));
    }

    @Test
    void testIsValidEmail_WithInvalidEmails_ReturnsFalse() {
        assertFalse(InputValidator.isValidEmail("khongphaiemail"));
        assertFalse(InputValidator.isValidEmail("@email.com"));
        assertFalse(InputValidator.isValidEmail("user@"));
        assertFalse(InputValidator.isValidEmail(""));
        assertFalse(InputValidator.isValidEmail(null));
    }

    @Test
    void testIsValidPhone_WithValidPhones_ReturnsTrue() {
        assertTrue(InputValidator.isValidPhone("1234567890"));
        assertTrue(InputValidator.isValidPhone("+12345678901"));
        assertTrue(InputValidator.isValidPhone("123-456-7890"));
    }

    @Test
    void testIsValidPhone_WithInvalidPhones_ReturnsFalse() {
        assertFalse(InputValidator.isValidPhone("123"));
        assertFalse(InputValidator.isValidPhone("abcdefghij"));
        assertFalse(InputValidator.isValidPhone(""));
        assertFalse(InputValidator.isValidPhone(null));
    }

    @Test
    void testIsValidIsbn_WithValidIsbns_ReturnsTrue() {
        assertTrue(InputValidator.isValidIsbn("978-0-13-235088-4"));
        assertTrue(InputValidator.isValidIsbn("9780132350884"));
        assertTrue(InputValidator.isValidIsbn("0-596-52068-9"));
    }

    @Test
    void testIsValidPublicationYear_WithValidYears_ReturnsTrue() {
        assertTrue(InputValidator.isValidPublicationYear(2020));
        assertTrue(InputValidator.isValidPublicationYear(1500));
        assertTrue(InputValidator.isValidPublicationYear(java.time.Year.now().getValue()));
    }

    @Test
    void testIsValidPublicationYear_WithInvalidYears_ReturnsFalse() {
        assertFalse(InputValidator.isValidPublicationYear(1400));
        assertFalse(InputValidator.isValidPublicationYear(2100));
        assertFalse(InputValidator.isValidPublicationYear(-100));
    }

    @Test
    void testHasMinLength_WithValidLength_ReturnsTrue() {
        assertTrue(InputValidator.hasMinLength("Hello", 3));
        assertTrue(InputValidator.hasMinLength("Hello", 5));
    }

    @Test
    void testHasMinLength_WithInvalidLength_ReturnsFalse() {
        assertFalse(InputValidator.hasMinLength("Hi", 3));
        assertFalse(InputValidator.hasMinLength("", 1));
        assertFalse(InputValidator.hasMinLength(null, 1));
    }

    @Test
    void testIsAlphabetic_WithValidStrings_ReturnsTrue() {
        assertTrue(InputValidator.isAlphabetic("Hello"));
        assertTrue(InputValidator.isAlphabetic("Hello World"));
        assertTrue(InputValidator.isAlphabetic("ABC"));
    }

    @Test
    void testIsAlphabetic_WithInvalidStrings_ReturnsFalse() {
        assertFalse(InputValidator.isAlphabetic("Hello123"));
        assertFalse(InputValidator.isAlphabetic("Hello!"));
        assertFalse(InputValidator.isAlphabetic(""));
        assertFalse(InputValidator.isAlphabetic(null));
    }

    @Test
    void testIsPositiveInteger_WithValidValues_ReturnsTrue() {
        assertTrue(InputValidator.isPositiveInteger(1));
        assertTrue(InputValidator.isPositiveInteger(100));
        assertTrue(InputValidator.isPositiveInteger(Integer.MAX_VALUE));
    }

    @Test
    void testIsPositiveInteger_WithInvalidValues_ReturnsFalse() {
        assertFalse(InputValidator.isPositiveInteger(0));
        assertFalse(InputValidator.isPositiveInteger(-1));
        assertFalse(InputValidator.isPositiveInteger(-100));
    }

    @Test
    void testIsNonNegative_WithValidValues_ReturnsTrue() {
        assertTrue(InputValidator.isNonNegative(0));
        assertTrue(InputValidator.isNonNegative(1.5));
        assertTrue(InputValidator.isNonNegative(100.0));
    }

    @Test
    void testIsNonNegative_WithInvalidValues_ReturnsFalse() {
        assertFalse(InputValidator.isNonNegative(-0.1));
        assertFalse(InputValidator.isNonNegative(-100.0));
    }
}