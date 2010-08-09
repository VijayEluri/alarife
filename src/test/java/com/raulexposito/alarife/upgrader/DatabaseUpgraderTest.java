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
package com.raulexposito.alarife.upgrader;

import com.raulexposito.alarife.commons.DatabaseUtils;
import com.raulexposito.alarife.enumeration.ApplicationMode;
import com.raulexposito.alarife.exception.DatabaseException;
import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.sqlexecutor.SQLExecutor;
import com.raulexposito.alarife.upgrader.pojo.Version;
import com.raulexposito.alarife.util.ScriptsDirectoryUtil;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

/**
 * Tests the differents methods of the database upgrader
 */
public class DatabaseUpgraderTest {

    private static final String INSTEAD_OF = "instead of";
    private static final String THE_VERSION_SHOULD_BE = "the version should be";

    @Test
    public void testRecoverLastVersionNumber() {
        final List<Version> versionList = DatabaseUtils.generateTestVersionList();
        final Version v3_0_24 = new Version(3, 0, 24);
        Collections.sort(versionList);

        if (!DatabaseUpgrader.recoverLatestVersion(versionList).equals(v3_0_24)) {
            fail("last version should be '3.0.24' instead of '" +
                    DatabaseUpgrader.recoverLatestVersion(versionList) + "'");
        }
    }

    @Test
    public void testUpgradeIsNeeded() {
        final Version v0_1_3 = new Version(0, 1, 3);
        final Version v0_2_8 = new Version(0, 2, 8);
        final Version v3_0_24 = new Version(3, 0, 24);

        if (DatabaseUpgrader.upgradeIsNeeded(v3_0_24, v3_0_24)) {
            fail("no upgrade is needed, both are the same version");
        }

        if (!DatabaseUpgrader.upgradeIsNeeded(v0_1_3, v0_2_8)) {
            fail("an upgrade is needed");
        }

        if (DatabaseUpgrader.upgradeIsNeeded(v3_0_24, v0_2_8)) {
            fail("no upgrade is needed, current version is the newest");
        }
    }

    @Test
    public void testRecoverCurrentVersion()
            throws DatabaseException {
        final Version v6_6_6 = new Version(6, 6, 6);
        final Version v0_0_0 = new Version(0, 0, 0);
        final Version v3_0_24 = new Version(3, 0, 24);
        final SQLExecutor sqlExecutor = new SQLExecutor();


        // drops database and generates a fresh database with the demonic version as version
        sqlExecutor.createFromScratch(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.DATABASE_PROPERTIES_FILE,
                DatabaseUtils.DEMONIC_TESTING_VERSION);

        final Version shouldBe_6_6_6 =
                sqlExecutor.recoverCurrentVersion(DatabaseType.MYSQL, ApplicationMode.TESTING,
                DatabaseUtils.DATABASE_PROPERTIES_FILE);

        if (!shouldBe_6_6_6.equals(v6_6_6)) {
            fail("the recovered version should be '6.6.6', but '" + shouldBe_6_6_6 + "' was recovered");
        }

        // drops database and generates a fresh database with 0.0.0 as version
        sqlExecutor.createFromScratch(DatabaseType.MYSQL, ApplicationMode.TESTING, DatabaseUtils.DATABASE_PROPERTIES_FILE, "0.0.0");

        final Version shouldBe_0_0_0 =
                sqlExecutor.recoverCurrentVersion(DatabaseType.MYSQL, ApplicationMode.TESTING,
                DatabaseUtils.DATABASE_PROPERTIES_FILE);

        if (!shouldBe_0_0_0.equals(v0_0_0)) {
            fail("the recovered version should be '0.0.0', but '" + shouldBe_0_0_0 + "' was recovered");
        }

        // drops database and generates a fresh database with 3.0.24 as version
        sqlExecutor.createFromScratch(DatabaseType.MYSQL,  ApplicationMode.TESTING,DatabaseUtils.DATABASE_PROPERTIES_FILE, "3.0.24");

        final Version shouldBe_3_0_24 =
                sqlExecutor.recoverCurrentVersion(DatabaseType.MYSQL, ApplicationMode.TESTING,
                DatabaseUtils.DATABASE_PROPERTIES_FILE);

        if (!shouldBe_3_0_24.equals(v3_0_24)) {
            fail("the recovered version should be '3.0.24', but '" + shouldBe_3_0_24 + "' was recovered");
        }
    }

    @Test
    public void testGenerateVersionIfItsValid() {
        final DatabaseUpgrader databaseUpgrader = new DatabaseUpgrader();
        final String mySQLScriptsPath = ScriptsDirectoryUtil.getScriptsDirectory(DatabaseType.MYSQL, ApplicationMode.TESTING);

        if (databaseUpgrader.generateVersionFromDirectory(mySQLScriptsPath, "0.0.1") == null) {
            fail("the '0.0.1' directory is a valid directory");
        }

        if (databaseUpgrader.generateVersionFromDirectory(mySQLScriptsPath, "0.0.1b") != null) {
            fail("the '0.0.1b' directory is not a valid directory");
        }

        if (databaseUpgrader.generateVersionFromDirectory(mySQLScriptsPath, "0.0.2") != null) {
            fail("the '0.0.2' directory is not a valid directory");
        }

        if (databaseUpgrader.generateVersionFromDirectory(mySQLScriptsPath, "0.0.3") != null) {
            fail("the '0.0.3' directory is not a valid directory");
        }

        if (databaseUpgrader.generateVersionFromDirectory(mySQLScriptsPath, "0.0.4") != null) {
            fail("the '0.0.4' directory is not a valid directory");
        }

        if (databaseUpgrader.generateVersionFromDirectory(mySQLScriptsPath, "0.0.5") == null) {
            fail("the '0.0.5' directory is a valid directory");
        }
    }

    @Test
    public void testRecoverVersionList()
            throws DatabaseException {
        final DatabaseUpgrader databaseUpgrader = new DatabaseUpgrader();
        final Version v0_0_1 = new Version(0, 0, 1);
        final Version v0_0_5 = new Version(0, 0, 5);
        final List<Version> versionList = databaseUpgrader.recoverVersionList(DatabaseType.MYSQL, ApplicationMode.TESTING);

        if (versionList.size() != 2) {
            String versions = "";

            for (Version version : versionList) {
                versions = version + ", ";
            }

            fail("It must be only 2 versions, '0.0.1' and '0.0.5', but there are " + versionList.size() + ": " +
                    versions);
        }

        if (!versionList.contains(v0_0_1)) {
            fail("The list must contains the version '0.0.1'");
        }

        if (!versionList.contains(v0_0_5)) {
            fail("The list must contains the version '0.0.5'");
        }

        if (!versionList.get(0).equals(v0_0_1)) {
            fail("The first element of the list must be '0.0.1'");
        }

        if (!versionList.get(1).equals(v0_0_5)) {
            fail("The second element of the list must be '0.0.5'");
        }
    }

    @Test
    public void testGetNextVersion() {
        final List<Version> versionList = DatabaseUtils.generateTestVersionList();
        Collections.sort(versionList);

        final Version v0_0_0 = new Version(0, 0, 0);
        final Version v0_0_7 = new Version(0, 0, 7);
        final Version v0_1_3 = new Version(0, 1, 3);
        final Version v0_1_98 = new Version(0, 1, 98);
        final Version v0_2_8 = new Version(0, 2, 8);
        final Version v1_15_44 = new Version(1, 15, 44);
        final Version v2_3_66 = new Version(2, 3, 66);
        final Version v3_0_24 = new Version(3, 0, 24);

        final Version firstVersion = versionList.get(0);

        if (!firstVersion.equals(v0_0_0)) {
            fail("the first version should be '" + v0_0_0 + "'" + INSTEAD_OF + "'" + firstVersion + "'");
        }

        Version currentVersion = DatabaseUpgrader.getNextVersion(firstVersion, versionList);

        if (!currentVersion.equals(v0_0_7)) {
            fail(THE_VERSION_SHOULD_BE + "'" + v0_0_7 + "'" + INSTEAD_OF + "'" + currentVersion + "'");
        }

        currentVersion = DatabaseUpgrader.getNextVersion(currentVersion, versionList);

        if (!currentVersion.equals(v0_1_3)) {
            fail(THE_VERSION_SHOULD_BE + "'" + v0_1_3 + "'" + INSTEAD_OF + "'" + currentVersion + "'");
        }

        currentVersion = DatabaseUpgrader.getNextVersion(currentVersion, versionList);

        if (!currentVersion.equals(v0_1_98)) {
            fail(THE_VERSION_SHOULD_BE + "'" + v0_1_98 + "'" + INSTEAD_OF + "'" + currentVersion + "'");
        }

        currentVersion = DatabaseUpgrader.getNextVersion(currentVersion, versionList);

        if (!currentVersion.equals(v0_2_8)) {
            fail(THE_VERSION_SHOULD_BE + "'" + v0_2_8 + "'" + INSTEAD_OF + "'" + currentVersion + "'");
        }

        currentVersion = DatabaseUpgrader.getNextVersion(currentVersion, versionList);

        if (!currentVersion.equals(v1_15_44)) {
            fail(THE_VERSION_SHOULD_BE + "'" + v1_15_44 + "'" + INSTEAD_OF + "'" + currentVersion + "'");
        }

        currentVersion = DatabaseUpgrader.getNextVersion(currentVersion, versionList);

        if (!currentVersion.equals(v2_3_66)) {
            fail(THE_VERSION_SHOULD_BE + "'" + v2_3_66 + "'" + INSTEAD_OF + "'" + currentVersion + "'");
        }

        currentVersion = DatabaseUpgrader.getNextVersion(currentVersion, versionList);

        if (!currentVersion.equals(v3_0_24)) {
            fail(THE_VERSION_SHOULD_BE + "'" + v3_0_24 + "'" + INSTEAD_OF + "'" + currentVersion + "'");
        }
    }
}
