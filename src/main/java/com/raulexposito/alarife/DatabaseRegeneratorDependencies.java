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

import com.raulexposito.alarife.enumeration.ApplicationMode;
import com.raulexposito.alarife.enumeration.DatabaseType;

/**
 * Cleans the <code>DefaultDatabaseManager</code> and manages its dependencies
 */
public class DatabaseRegeneratorDependencies {

    // application mode
    private ApplicationMode applicationMode = ApplicationMode.DEVELOPMENT;
    // database type
    private DatabaseType databaseType = DatabaseType.MYSQL;
    // name of the properties file with the database configuration
    private String databasePropertiesFilename;

    // -------------------------------------------------- //
    //                      getters                       //
    // -------------------------------------------------- //
    /**
     * Recovers the value of the application mode type
     * @return the value of the application mode type
     */
    protected ApplicationMode getApplicationMode() {
        return applicationMode;
    }

    /**
     * Recovers the value of the application database type
     * @return the value of the application database type
     */
    protected DatabaseType getDatabaseType() {
        return databaseType;
    }

    /**
     * Recovers the database properties
     * @return the database properties
     */
    protected String getDatabasePropertiesFilename() {
        return databasePropertiesFilename;
    }

    // -------------------------------------------------- //
    //                       setters                      //
    // -------------------------------------------------- //
    /**
     * Sets the application mode
     * @param applicationMode application mode
     */
    protected void setApplicationMode(final ApplicationMode applicationMode) {
        this.applicationMode = applicationMode;
    }

    /**
     * Sets the database type
     * @param databaseType database type
     */
    protected void setDatabaseType(final DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * Sets the filename of the database properties
     * @param databaseProperties filename of the database properties
     */
    protected void setDatabasePropertiesFilename(final String databasePropetiesFilename) {
        this.databasePropertiesFilename = databasePropetiesFilename;
    }
}
