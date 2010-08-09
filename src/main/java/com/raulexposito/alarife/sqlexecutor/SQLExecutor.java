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
package com.raulexposito.alarife.sqlexecutor;

import com.raulexposito.alarife.creatorfromscratch.properties.DatabaseCreatorFromScratchPropertiesReader;
import com.raulexposito.alarife.enumeration.ApplicationMode;

import com.raulexposito.alarife.exception.DatabaseException;
import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.upgrader.DatabaseUpgrader;
import com.raulexposito.alarife.upgrader.pojo.Version;
import com.raulexposito.alarife.upgrader.properties.DatabaseUpgraderPropertiesReader;
import com.raulexposito.alarife.util.DatabasePropertiesReaderUtil;
import com.raulexposito.alarife.util.ScriptsDirectoryUtil;
import com.raulexposito.alarife.util.VersionGeneratorUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes the commands to the database to upgrade, request information, regenerate ...
 */
public final class SQLExecutor {

    private static final Log log = LogFactory.getLog(SQLExecutor.class);
    private static final String CANNOT_UPGRADE_TO_VERSION = "Cannot upgrade to version ";
    private static final String SQL_COMMENT = "--";
    private static final String SQL_END_OF_COMMAND = ";";
    private static final int NOT_EXISTS = -1;
    // properties file reader
    private DatabasePropertiesReaderUtil dpru = null;
    // properties file reader
    private DatabaseCreatorFromScratchPropertiesReader dcfspr = null;
    // properties file reader
    private DatabaseUpgraderPropertiesReader dupr = null;

    /**
     * Recovers the version of the database that is being used by the application<br>
     * @param databaseType type of the database in use
     * @param propertiesFile name of the properties file where you can read the database configuration
     * @throws com.raulexposito.alarife.exception.DatabaseException if something goes wrong
     * @return the version of the database that is being used by the application
     */
    public Version recoverCurrentVersion(final DatabaseType databaseType, final ApplicationMode applicationMode, final String propertiesFile)
            throws DatabaseException {
        return performSQLAction(SQLExecutorCommandType.RECOVER_CURRENT_VERSION, databaseType, applicationMode, propertiesFile, null,
                null);
    }

    /**
     * Recovers the version of the database that is being used by the application<br>
     * @param databaseType type of the database in use
     * @param propertiesFile name of the properties file where you can read the database configuration
     * @throws es.futbol.database.exception.DatabaseException if something goes wrong
     * @return the version of the database that is being used by the application
     */
    public Version updateToVersion(final List<Version> versionList, final ApplicationMode applicationMode, final String propertiesFile,
            final DatabaseType databaseType)
            throws DatabaseException {

        return performSQLAction(SQLExecutorCommandType.UPDATE_TO_VERSION, databaseType, applicationMode, propertiesFile, versionList,
                null);
    }

    /**
     * Drops the old database and regenerates it to make a new and fresh
     * database
     * @param databaseType enumeration with the database type
     * @throws com.raulexposito.alarife.exception.DatabaseException if something goes wrong
     */
    public void createFromScratch(final DatabaseType databaseType, final ApplicationMode applicationMode, final String propertiesFile,
            final String version)
            throws DatabaseException {

        performSQLAction(SQLExecutorCommandType.CREATE_FROM_SCRATCH, databaseType, applicationMode, propertiesFile, null, version);
    }

    /**
     * Connects with the database and performs some operations with it
     * @param commandType enumeration with the operation to perform
     * @param databaseType enumeration with the database type
     * @param propertiesFile propertiesFile name of the properties file where you can read the database configuration
     * @param nextVersion version you are migrating to in upgrades
     * @param initialVersionNumber version to start the database in regenerations
     * @return initially a Version object
     * @throws com.raulexposito.alarife.exception.DatabaseException if something goes worng
     */
    private Version performSQLAction(final SQLExecutorCommandType commandType, final DatabaseType databaseType,
            final ApplicationMode applicationMode, final String propertiesFile, final List<Version> versionList,
            final String initialVersionNumber)
            throws DatabaseException {
        Connection con = null;
        Statement st = null;
        boolean rollbackConnection = false;

        try {
            // read the database properties file if it's needed
            if (dpru == null) {
                dpru = new DatabasePropertiesReaderUtil(propertiesFile);

                final String driverClassName = dpru.getDriverClassName();

                // load the driver
                Class.forName(driverClassName);
                log.info("database driver loaded: '" + driverClassName + "'");
            }

            if (dcfspr == null) {
                dcfspr = new DatabaseCreatorFromScratchPropertiesReader(propertiesFile);
            }

            // connection to the database using an instance, an username and password
            con = DriverManager.getConnection(dpru.getInstance(),
                    dpru.getUsername(),
                    dpru.getPassword());
            con.setAutoCommit(false);

            // creation of a statements to execute commands
            st = con.createStatement();

            // actions to be performed
            if (commandType.equals(SQLExecutorCommandType.CREATE_FROM_SCRATCH)) {
                regenerateDatabaseFromScratch(propertiesFile, databaseType, st, initialVersionNumber);
            } else if (commandType.equals(SQLExecutorCommandType.RECOVER_CURRENT_VERSION)) {
                return recoverCurrentVersion(propertiesFile, databaseType, st);
            } else if (commandType.equals(SQLExecutorCommandType.UPDATE_TO_VERSION)) {
                Version currentVersion = this.recoverCurrentVersion(databaseType, applicationMode, propertiesFile);
                final Version latestVersion = DatabaseUpgrader.recoverLatestVersion(versionList);

                while (DatabaseUpgrader.upgradeIsNeeded(currentVersion, latestVersion)) {
                    log.info("the current version '" + currentVersion + "' is older than the latest version '" +
                            latestVersion + "', so an upgrade is needed");

                    final Version nextVersion = DatabaseUpgrader.getNextVersion(currentVersion, versionList);
                    updateToVersion(databaseType, applicationMode, st, nextVersion);
                    currentVersion = recoverCurrentVersion(databaseType, applicationMode, propertiesFile);
                }
            }

            // commit the changes in database
            con.commit();
        } catch (IOException ex) {
            log.error("There is a problem reading the configuration file");
            rollbackConnection = true;
            throw new DatabaseException(ex.getMessage(), ex);
        } catch (SQLException ex) {
            log.error("There is a problem with a SQL command");
            rollbackConnection = true;
            throw new DatabaseException(ex.getMessage(), ex);
        } catch (DatabaseException ex) {
            log.error("There is a problem updating the database");
            rollbackConnection = true;
            throw new DatabaseException(ex.getMessage(), ex);
        } catch (ClassNotFoundException ex) {
            log.error("The database driver cannot be loaded");
            rollbackConnection = true;
            throw new DatabaseException(ex.getMessage(), ex);
        } finally {
            if (rollbackConnection && (con != null)) {
                try {
                    con.rollback();
                } catch (Exception e) {
                    log.warn("cannot rollback the connection: " + e);
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    log.warn("cannot close the connection: " + e);
                }
            }

            if (st != null) {
                try {
                    st.close();
                } catch (Exception e) {
                    log.warn("cannot close the statement: " + e);
                }
            }
        }

        return null;
    }

    /**
     * Drops the database and regenerates it using the SQL commands from the configuration file
     * @param propertiesFile configuration file
     * @param databaseType type of the database
     * @param st statement to launch the SQL commands
     * @param initialVersionNumber number to be inserted into the database as initial version
     * @throws java.io.IOException if the configuration file cannot be readed
     * @throws java.sql.SQLException if there is an error with the SQL commands
     */
    private void regenerateDatabaseFromScratch(final String propertiesFile, final DatabaseType databaseType,
            final Statement st, final String initialVersionNumber)
            throws IOException, SQLException {
        if (dcfspr == null) {
            dcfspr = new DatabaseCreatorFromScratchPropertiesReader(propertiesFile);
        }

        // drop the database
        st.executeUpdate(dcfspr.getDropDatabase(databaseType));
        log.debug("schema '" + dcfspr.getInstance() + "' dropped");

        // create a new database
        st.executeUpdate(dcfspr.getCreateDatabase(databaseType));
        log.debug("schema '" + dcfspr.getInstance() + "' created");

        // changes to the new database
        st.executeUpdate(dcfspr.getChangeDatabase(databaseType));
        log.debug("changed to schema '" + dcfspr.getInstance() + "'");

        // create a table 'VERSION' in the database with the version of the software
        st.executeUpdate(dcfspr.getCreateVersionTable(databaseType));
        log.debug("table 'VERSION' created");

        // save the version by default in the new table 'Version'
        st.executeUpdate(dcfspr.getInsertVersionTable(databaseType, initialVersionNumber));
        log.debug("inserted the version '" + initialVersionNumber + "' in the table 'VERSION'");
    }

    /**
     * Recovers the current database version
     * @param propertiesFile configuration file
     * @param databaseType type of the database
     * @param st statement to launch the SQL commands
     * @param result result of the query
     * @param con connection to the database
     * @return an instance of the class 'Version' with the version saved in database
     * @throws java.io.IOException if the configuration file cannot be readed
     * @throws com.raulexposito.alarife.exception.DatabaseException if convert the version is not possible
     * @throws java.sql.SQLException if there is an error with the SQL commands
     */
    private Version recoverCurrentVersion(final String propertiesFile, final DatabaseType databaseType,
            final Statement st)
            throws IOException, DatabaseException, SQLException {
        if (dupr == null) {
            dupr = new DatabaseUpgraderPropertiesReader(propertiesFile);
        }

        String recoveredVersion = "";

        // launchs the query
        final ResultSet executeQueryResultset = st.executeQuery(dupr.getDatabaseVersionTable(databaseType));

        if (executeQueryResultset != null) {

            // there must be only one result
            while (executeQueryResultset.next()) {
                recoveredVersion = executeQueryResultset.getString(1);
            }

            log.debug("current version: '" + recoveredVersion + "'");
        
            try {
                executeQueryResultset.close();
            } catch (Exception e) {
                log.warn("cannot rollback the result set: " + e);
            }
        }

        return VersionGeneratorUtil.generateVersion(recoveredVersion);
    }

    /**
     * Updates the database from one version to other by using the scripts
     * @param databaseType database type
     * @param st sql statement to launch the creates and updates
     * @param nextVersion version of the scripts to be launched to upgrade to the next version
     * @throws com.raulexposito.alarife.exception.DatabaseException if something goes wrong
     */
    private void updateToVersion(final DatabaseType databaseType, final ApplicationMode applicationMode, final Statement st, final Version nextVersion)
            throws DatabaseException {
        log.info("migrating to '" + nextVersion + "' version");

        final long startTime = System.currentTimeMillis();
        final ScriptsDirectoryUtil scriptsDirectoryUtil = new ScriptsDirectoryUtil();

        try {

            st.executeUpdate(dcfspr.getChangeDatabase(databaseType));
            log.debug("changed to schema '" + dcfspr.getInstance() + "'");

            // read the content of the file with the SQL commands to update tables
            final InputStream upgradeTables =
                    scriptsDirectoryUtil.getUpgradeTablesScript(databaseType, applicationMode, nextVersion);
            log.info("reading the content of the upgrade tables script");

            final List<String> upgradeTablesCommands = getSQLCommandsFromScriptFile(upgradeTables);

            for (String command : upgradeTablesCommands) {
                st.addBatch(command);
            }

            // read the content of the file with the SQL commands to insert data
            final InputStream insertData =
                    scriptsDirectoryUtil.getInsertDataScript(databaseType, applicationMode, nextVersion);
            log.info("reading the content of the insert data script");

            final List<String> insertDataCommands = getSQLCommandsFromScriptFile(insertData);

            for (String command : insertDataCommands) {
                st.addBatch(command);
            }

            // execution of the different commands
            st.executeBatch();
            log.info("scripts succesfully executed [" + (System.currentTimeMillis() - startTime) + " ms]");
        } catch (Exception e) {
            log.error(CANNOT_UPGRADE_TO_VERSION + "'" + nextVersion + "': " + e);
            throw new DatabaseException(CANNOT_UPGRADE_TO_VERSION + "'" + nextVersion + "'", e);
        }
    }

    /**
     * Reads the content of a script file and, for each command on it, generates a single command
     * @param file the script file with the commands
     * @return a list with the commands of the script
     * @throws java.io.IOException if the file cannot be readed
     */
    protected static List<String> getSQLCommandsFromScriptFile(final InputStream file)
            throws IOException {
        // list of commands to be returned
        final List<String> SQLCommands = new ArrayList<String>();

        // reader for the script file
        final LineNumberReader scriptFile =
                new LineNumberReader(new InputStreamReader(
                new BufferedInputStream(file),
                "ISO-8859-1"));

        // string builder where append the lines of the file
        final StringBuilder content = new StringBuilder();

        // next line to be readed from the script file
        String line;

        while ((line = scriptFile.readLine()) != null) {
            log.trace(line);
            line = line.trim();

            // deletes the comments
            if (line.indexOf(SQL_COMMENT) != NOT_EXISTS) {
                line =
                        line.substring(0,
                        line.indexOf(SQL_COMMENT));
            }

            content.append(line);

            // is the end of the command?
            if (line.endsWith(SQL_END_OF_COMMAND)) {
                SQLCommands.add(content.toString());
                content.setLength(0);
            }
        }

        return SQLCommands;
    }
}
