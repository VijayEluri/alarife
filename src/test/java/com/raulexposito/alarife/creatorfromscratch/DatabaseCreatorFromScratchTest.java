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
package com.raulexposito.alarife.creatorfromscratch;

import com.raulexposito.alarife.creatorfromscratch.DatabaseCreatorFromScratch;
import com.raulexposito.alarife.commons.DatabaseUtils;
import com.raulexposito.alarife.enumeration.ApplicationMode;
import com.raulexposito.alarife.exception.DatabaseException;
import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.sqlexecutor.SQLExecutor;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This test allows us to know if the reading of the parameters from the properties
 * file is consistent and if we can create a database with the version number we want
 */
public class DatabaseCreatorFromScratchTest {

    /**
     * Try to drops and creates databases with the correct arguments from the properties file
     * and with the version we want
     */
    @Test
    public void testMySQLReloadWithDatabaseGenerator()
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

        // in this step a little database exists vith a demonic version, it's time to drop it and regenerate it
        sqlExecutor.createFromScratch(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.DATABASE_PROPERTIES_FILE,
                DatabaseCreatorFromScratch.ZERO_VERSION);

        if (!sqlExecutor.recoverCurrentVersion(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.DATABASE_PROPERTIES_FILE).toString().equals(DatabaseCreatorFromScratch.ZERO_VERSION)) {
            fail("the version should be '" + DatabaseCreatorFromScratch.ZERO_VERSION + "', but '" +
                    sqlExecutor.recoverCurrentVersion(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.DATABASE_PROPERTIES_FILE) +
                    "' was recovered");
        }
    }

    /**
     * Incorrect properties file
     */
    @Test(expected = DatabaseException.class)
    public void testPropertiesFileDoesntExists()
            throws DatabaseException {
        final SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.createFromScratch(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.WRONG_DATABASE_PROPERTIES_FILE,
                DatabaseCreatorFromScratch.ZERO_VERSION);
    }

    /**
     * Incorrect parameter from properties file
     */
    @Test(expected = DatabaseException.class)
    public void testWrongDatabaseDriver()
            throws DatabaseException {
        final SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.createFromScratch(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.WRONG_DRIVER_FILE,
                DatabaseCreatorFromScratch.ZERO_VERSION);
    }

    /**
     * Incorrect parameter from properties file
     */
    @Test(expected = DatabaseException.class)
    public void testWrongUrl()
            throws DatabaseException {
        final SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.createFromScratch(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.WRONG_INSTANCE_FILE,
                DatabaseCreatorFromScratch.ZERO_VERSION);
    }

    /**
     * Incorrect parameter from properties file
     */
    @Test(expected = DatabaseException.class)
    public void testWrongSQLCommand()
            throws DatabaseException {
        final SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.createFromScratch(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.WRONG_MYSQL_DROP_DATABASE_FILE,
                DatabaseCreatorFromScratch.ZERO_VERSION);
    }

    /**
     * Incorrect parameter from properties file
     */
    @Test(expected = DatabaseException.class)
    public void testWrongPassword()
            throws DatabaseException {
        final SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.createFromScratch(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.WRONG_PASSWORD_FILE,
                DatabaseCreatorFromScratch.ZERO_VERSION);
    }

    /**
     * Incorrect parameter from properties file
     */
    @Test(expected = DatabaseException.class)
    public void testWrongUsername()
            throws DatabaseException {
        final SQLExecutor sqlExecutor = new SQLExecutor();
        sqlExecutor.createFromScratch(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.WRONG_USERNAME_FILE,
                DatabaseCreatorFromScratch.ZERO_VERSION);
    }
}
