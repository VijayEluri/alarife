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

import com.raulexposito.alarife.creatorfromscratch.DatabaseCreatorFromScratch;
import com.raulexposito.alarife.enumeration.ApplicationMode;
import com.raulexposito.alarife.exception.DatabaseException;
import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.sqlexecutor.SQLExecutor;
import com.raulexposito.alarife.upgrader.pojo.Version;
import com.raulexposito.alarife.util.ScriptsDirectoryUtil;
import com.raulexposito.alarife.util.VersionGeneratorUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs the database upgrade if needed
 */
public final class DatabaseUpgrader
{
    private static final Log log = LogFactory.getLog( DatabaseCreatorFromScratch.class );

    /**
     * Upgrades the database and enables working with the latest version of the database<br>
     * @param databaseType type of the database in use
     * @param propertiesFile name of the properties file where you can read the database configuration
     * @param scriptsPath path where find the scripts to perform the upgrade
     */
    public void upgradeDatabase( final DatabaseType databaseType, final ApplicationMode applicationMode, final String propertiesFile )
                         throws DatabaseException
    {
        final List<Version> versionList = this.recoverVersionList( databaseType, applicationMode );
        new SQLExecutor().updateToVersion( versionList, applicationMode, propertiesFile, databaseType );
    }

    /**
     * Recover a list with all the versions of the differents scripts<br>
     * <br>
     * <strong>This method is PROTECTED to allow testing</strong>
     * <br>
     * @param databaseType type of the database in use
     * @param scriptsPath path where find the scripts to perform the upgrade
     * @return a list with all the versions of the differents scripts
     */
    protected List<Version> recoverVersionList( final DatabaseType databaseType, final ApplicationMode applicationMode )
                                        throws DatabaseException
    {
        final List<Version> versions = new ArrayList<Version>(  );

        // recover the directory where found the scripts
        try
        {
            final File scriptsDirectory = new ScriptsDirectoryUtil().getRootScriptsDirectory( databaseType, applicationMode );
            final String[] directories = scriptsDirectory.list(  );

            if ( directories == null )
            {
                log.warn( "there are no directories with the scripts ... WTF ???" );
            } else
            {
                for ( String directory : directories )
                {
                    final Version version =
                        generateVersionFromDirectory( ScriptsDirectoryUtil.getScriptsDirectory(databaseType, applicationMode), directory );

                    if ( version == null )
                    {
                        log.debug( "there is a version that cannot be recovered" );
                    } else
                    {
                        versions.add( version );
                    }
                }
            }
        } catch ( URISyntaxException ex )
        {
            throw new DatabaseException( "there is an error finding the directory with the upgrade scripts: ", ex );
        }

        return versions;
    }

    /**
     * Indicates if a directory is a valid scripts directory. It has 2 restrictions:<br>
     * <br>
     * - first, it must be a 'versioned' directory,for instance: '1.2.4', '2.5.1', not '1.3.2.4' or '1.3.4b'
     * - second, it must contain two files ('upgradeTables' and 'insertData') with the version number as perfix, for instance: '1.2.4/upgradeTables-1.2.4.sql'
     *
     * @param scriptsDirectory directory with all the scripts
     * @param directory directory when found the scripts 'upgradeTables' and 'insertData'
     * @return an object of type 'Version' if the directory is valid to perform upgrades
     */
    protected Version generateVersionFromDirectory( final String scriptsDirectory, final String directory )
    {
        try
        {
            // tests if the directory has the correct form
            final Version version = VersionGeneratorUtil.generateVersion( directory );

            // test if the upgrade script is present
            final String upgradeScript =
                scriptsDirectory + File.separator + directory + File.separator +
                ScriptsDirectoryUtil.UPGRADE_TABLES_SCRIPT_PREFFIX + version + ScriptsDirectoryUtil.SQL_EXTENSION;
            final InputStream upgradeScriptIS =
                this.getClass(  ).getClassLoader(  ).getResourceAsStream( upgradeScript );

            if ( upgradeScriptIS == null )
            {
                log.warn( "There is an invalid scripts directory: '" + directory +
                          "'. It hasn't the upgrade script file '" + upgradeScript + "'" );

                return null;
            }

            // test if the insert data script is present
            final String insertDataScript =
                scriptsDirectory + File.separator + directory + File.separator +
                ScriptsDirectoryUtil.INSERT_DATA_SCRIPT_PREFFIX + version + ScriptsDirectoryUtil.SQL_EXTENSION;
            final InputStream insertDataScriptIS =
                this.getClass(  ).getClassLoader(  ).getResourceAsStream( insertDataScript );

            if ( insertDataScriptIS == null )
            {
                log.warn( "There is an invalid scripts directory: '" + directory +
                          "'. It hasn't the insert data script file '" + insertDataScript + "'" );

                return null;
            }

            log.debug( "the directory with the version '" + version + "' is valid" );

            return version;
        } catch ( DatabaseException ex )
        {
            log.warn( "There is an invalid scripts directory: '" + directory + "' ", ex );

            return null;
        }
    }

    /**
     * Recover the newest version of the upgrade scripts<br>
     * <br>
     * <strong>This method is PROTECTED to allow testing</strong>
     * <br>
     * @param versionList list with all versions of scripts
     * @return the newest version of the upgrade scripts
     */
    public static Version recoverLatestVersion( final List<Version> versionList )
    {
        log.debug( "the latest version is '" + versionList.get( versionList.size(  ) - 1 ) + "'" );

        return versionList.get( versionList.size(  ) - 1 );
    }

    /**
     * The database should be upgraded?<br>
     * @param currentVersion the version of the database that is being used by the application
     * @param latestVersion the newest version of the upgrade scripts
     * @return if the database must be upgraded
     */
    public static boolean upgradeIsNeeded( final Version currentVersion, final Version latestVersion )
    {
        log.debug( "current version: '" + currentVersion + "' | " + "latest version: '" + latestVersion + "'" );

        return ( currentVersion.compareTo( latestVersion ) == -1 );
    }

    /**
     * Recovers the next version of the current version
     * <br>
     * <strong>This method is PROTECTED to allow testing</strong>
     * <br>
     * @param currentVersion the current version
     * @param versionList the list with all versions
     * @return the next version of the current version
     */
    public static Version getNextVersion( final Version currentVersion, final List<Version> versionList )
    {
        final int currentPosition = versionList.indexOf( currentVersion );

        return versionList.get( currentPosition + 1 );
    }
}
