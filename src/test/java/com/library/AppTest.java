package com.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class AppTest {

    // No need for a constructor like in JUnit 3 or 4
    // Simply use @Test to mark methods as test methods

    @Test
    public void testApp() {
        // Simple assertion
        Assertions.assertTrue(true, "This test should pass!");
    }
}
