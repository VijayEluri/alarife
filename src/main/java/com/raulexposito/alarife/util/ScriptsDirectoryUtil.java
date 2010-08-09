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
package com.raulexposito.alarife.util;

import com.raulexposito.alarife.enumeration.ApplicationMode;
import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.upgrader.pojo.Version;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Class with utilities to operate with the scripts
 */
public final class ScriptsDirectoryUtil {

    private static final Log log = LogFactory.getLog(ScriptsDirectoryUtil.class);
    // Default directory with upgrade scripts
    private static final String SCRIPTS_PATH = "scripts" + File.separator;
    // preffix of the SQL file with the upgrade script
    public static final String UPGRADE_TABLES_SCRIPT_PREFFIX = "upgradeTables-";
    // preffix of the SQL file with the insert data script
    public static final String INSERT_DATA_SCRIPT_PREFFIX = "insertData-";
    // extension of the SQL files
    public static final String SQL_EXTENSION = ".sql";

    /**
     * Recovers the root directory with the scripts from a database
     * @param databaseType type of the database used by the application
     * @return a <code>File</code> object with the directory with the scripts for the database
     * @throws java.net.URISyntaxException if the conversion of the resource from
     * the classloader to URI is not possible
     */
    public File getRootScriptsDirectory(final DatabaseType databaseType, final ApplicationMode applicationMode)
            throws URISyntaxException {

        final File scriptsDirectory =
                new File(this.getClass().getClassLoader().getResource(getScriptsDirectory(databaseType, applicationMode)).toURI());
        log.debug("The directory with the scripts is '" + scriptsDirectory.getAbsolutePath() + "'");

        return scriptsDirectory;
    }


    /**
     * Recovers the path to the scripts to be launched as String
     * @param databaseType type of the database used by the application
     * @param applicationMode the mode of the database to be launched
     * @return the path to the scripts to be launched as String
     */
    public static String getScriptsDirectory (final DatabaseType databaseType, final ApplicationMode applicationMode) {
        return SCRIPTS_PATH + databaseType.toString().toLowerCase() + File.separator + applicationMode.toString().toLowerCase() + File.separator;
    }

    /**
     * Recover the script that performs an upgrade table to the version received by parameter
     * @param databaseType type of the database used by the application
     * @param version version number to migrate
     * @return a <code>InputStream</code> object with the file with the script
     * @throws java.net.URISyntaxException if the conversion of the resource from
     * the classloader to URI is not possible
     */
    public InputStream getUpgradeTablesScript(final DatabaseType databaseType, final ApplicationMode applicationMode, final Version version) {
        return getScriptAsInputStream(databaseType, applicationMode, version, UPGRADE_TABLES_SCRIPT_PREFFIX);
    }

    /**
     * Recover the script that inserts data using the version received by parameter
     * @param databaseType type of the database used by the application
     * @param version version number to migrate
     * @return a <code>InputStream</code> object with the file with the script
     * @throws java.net.URISyntaxException if the conversion of the resource from
     * the classloader to URI is not possible
     */
    public InputStream getInsertDataScript(final DatabaseType databaseType, final ApplicationMode applicationMode, final Version version) {
        return getScriptAsInputStream(databaseType, applicationMode, version, INSERT_DATA_SCRIPT_PREFFIX);
    }

    /**
     * Recovers a script of update / insert from a database type and version
     * @param databaseType database type
     * @param version version of the scripts to be launched
     * @param scriptType if it's an insert or update script
     * @return a <code>InputStream</code> object with the file with the script
     */
    private InputStream getScriptAsInputStream(final DatabaseType databaseType, final ApplicationMode applicationMode, final Version version,
            final String scriptType) {
        final String filePath =
                getScriptsDirectory(databaseType, applicationMode) + version + File.separator + scriptType + version +
                SQL_EXTENSION;

        return new FileReader().getFileAsInputStream(filePath);
    }
}
