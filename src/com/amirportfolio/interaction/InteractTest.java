package com.amirportfolio.interaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InteractTest {

    @Test
    public void shouldCheckForCorrectInput() {
        Assertions.assertTrue(new Interact().repeatAnswerIsValid("y"));
    }

}
