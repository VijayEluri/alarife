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
package com.raulexposito.alarife.upgrader.properties;

import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.util.DatabasePropertiesReaderUtil;

import java.io.IOException;

/**
 * Reads the content of the properties file that contains the database
 * configuration needed to upgrade it
 */
public class DatabaseUpgraderPropertiesReader
        extends DatabasePropertiesReaderUtil {
    // suffix of a properties file key

    private static final String RECOVER_VERSION_TABLE = ".recoverVersionTable";
    // default values for MySQL database version recovery
    private static final String DEFAULT_MYSQL_RECOVER_VERSION_TABLE = "select `version` FROM `alarife`.`Version`;";

    // ----------------------------- //
    //          CONSTRUCTOR          //
    // ----------------------------- //
    public DatabaseUpgraderPropertiesReader(final String databasePropertiesfile)
            throws IOException {
        super(databasePropertiesfile);
    }

    // ----------------------------- //
    //            METHODS            //
    // ----------------------------- //
    /**
     * Recovers the value of the command that recovers the application version
     * <br>
     * for instance, if the database is mysql, finds the key:<br>
     * MYSQL.recoverVersionTable<br>
     *
     * @param databaseType type of the database
     * @return the value of the command that recovers the application version
     */
    public String getDatabaseVersionTable(final DatabaseType databaseType) {
        final String[] parameters = new String[]{super.getSchema()};
        return super.getProperty(databaseType + RECOVER_VERSION_TABLE, DEFAULT_MYSQL_RECOVER_VERSION_TABLE, parameters);
    }
}
