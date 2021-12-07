package com.amirportfolio.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    private DataManager dataManager = new DataManager();

    @Test
    public void shouldCheckIfNameExistsInDatabase() {
        Assertions.assertTrue(dataManager.checkNameExists("Lily"));
    }

    @Test
    public void shouldCheckForCorrectPassword() {
        Assertions.assertTrue(dataManager.checkPassword(123456, "2244Test"));
    }

}
