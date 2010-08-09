/**
 *           DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *                   Version 2, December 2004
 *
 * Copyright (C) 2004 Sam Hocevar
 * 14 rue de Plaisance, 75014 Paris, France
 * Everyone is permitted to copy and distribute verbatim or modified
 * copies of this license document, and changing it is allowed as long
 * as the name is changed.
 *
 *            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *  0. You just DO WHAT THE FUCK YOU WANT TO.
 */
package com.raulexposito.alarife.manager;

import com.raulexposito.alarife.commons.DatabaseUtils;
import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.DatabaseRegenerator;
import com.raulexposito.alarife.enumeration.ApplicationMode;
import com.raulexposito.alarife.sqlexecutor.SQLExecutor;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests that the database manager can drop and create a new database or update
 * it based on its configuration
 */
public class DefaultDatabaseManagerTest {

    private final String DEFAULT_TEST_VERSION = "0.0.5";

    /**
     * Drops a database and creates a new one using the scripts to update it
     */
    @Test
    public void testMySQLReloadWithDatabaseManager()
            throws Exception {
        final SQLExecutor sqlExecutor = new SQLExecutor();

        // drops database and generates a fresh database with the demonic version as version
        sqlExecutor.createFromScratch(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.DATABASE_PROPERTIES_FILE,
                DatabaseUtils.DEMONIC_TESTING_VERSION);

        if (!sqlExecutor.recoverCurrentVersion(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.DATABASE_PROPERTIES_FILE).toString().equals(DatabaseUtils.DEMONIC_TESTING_VERSION)) {
            fail("the version should be '" + DatabaseUtils.DEMONIC_TESTING_VERSION + "', but '" +
                    sqlExecutor.recoverCurrentVersion(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.DATABASE_PROPERTIES_FILE) +
                    "' was recovered");
        }
        
        new DatabaseRegenerator(DatabaseUtils.DATABASE_PROPERTIES_FILE, ApplicationMode.TESTING, DatabaseType.MYSQL);

        if (!sqlExecutor.recoverCurrentVersion(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.DATABASE_PROPERTIES_FILE).toString().equals(DEFAULT_TEST_VERSION)) {
            fail("the version should be '" + DEFAULT_TEST_VERSION + "', but '" +
                    sqlExecutor.recoverCurrentVersion(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.DATABASE_PROPERTIES_FILE) +
                    "' was recovered");
        }
    }
}
