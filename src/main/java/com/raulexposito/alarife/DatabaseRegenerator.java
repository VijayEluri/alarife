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

package com.raulexposito.alarife;

import com.raulexposito.alarife.creatorfromscratch.DatabaseCreatorFromScratch;
import com.raulexposito.alarife.exception.DatabaseException;
import com.raulexposito.alarife.enumeration.ApplicationMode;
import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.upgrader.DatabaseUpgrader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Manager whose mission is allow the regeneration or update of the database
 */
public class DatabaseRegenerator
        extends DatabaseRegeneratorDependencies {

    private static final Log log = LogFactory.getLog(DatabaseRegenerator.class);

    public DatabaseRegenerator (final String databasePropertiesFilename) {
        this.setDatabasePropertiesFilename(databasePropertiesFilename);
        execute();
    }

    public DatabaseRegenerator (final String databasePropertiesFilename, final DatabaseType databaseType) {
        this.setDatabaseType(databaseType);
        this.setDatabasePropertiesFilename(databasePropertiesFilename);
        execute();
    }

    public DatabaseRegenerator (final String databasePropertiesFilename, final ApplicationMode applicationMode) {
        this.setApplicationMode(applicationMode);
        this.setDatabasePropertiesFilename(databasePropertiesFilename);
        execute();
    }

    public DatabaseRegenerator (final String databasePropertiesFilename, final ApplicationMode applicationMode, final DatabaseType databaseType) {
        this.setDatabaseType(databaseType);
        this.setApplicationMode(applicationMode);
        this.setDatabasePropertiesFilename(databasePropertiesFilename);
        execute();
    }

    /**
     * Organizes the regeneration and update of the database
     */
    private void execute() {

        log.info("the environment is configured as '" + this.getApplicationMode() + "' with a database '" + this.getDatabaseType() + "'");
        try
        {
            // Generates a new database if the application is configured in an
            // development environment
            if ((this.getApplicationMode().equals(ApplicationMode.DEVELOPMENT) || (this.getApplicationMode().equals(ApplicationMode.TESTING)))) {
                final DatabaseCreatorFromScratch databaseCreatorFromScratch = new DatabaseCreatorFromScratch();
                databaseCreatorFromScratch.createFromScratch(this.getDatabaseType(), this.getApplicationMode(), this.getDatabasePropertiesFilename());
            } else {
                log.debug("the environment is configured as '" + this.getApplicationMode() +
                        "', so the database will be not regenerated from scratch");
            }

            // upgrades the database if it's needed
            final DatabaseUpgrader databaseUpgrader = new DatabaseUpgrader();
            databaseUpgrader.upgradeDatabase(this.getDatabaseType(), this.getApplicationMode(), this.getDatabasePropertiesFilename());
        }
        catch (DatabaseException de) {
            log.fatal(de.getMessage());
        }
    }
}
