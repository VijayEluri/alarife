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

import com.raulexposito.alarife.enumeration.ApplicationMode;
import com.raulexposito.alarife.exception.DatabaseException;
import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.sqlexecutor.SQLExecutor;

/**
 * The goal of this class is allow to the developers to have a new and fresh
 * database without inconsistences or differences, and it brings the oportunity
 * of launching tests with initial data
 */
public class DatabaseCreatorFromScratch {
    // Default application version

    public static final String ZERO_VERSION = "0.0.0";

    /**
     * Drops the old database and regenerates it to make a new and fresh
     * database
     * @param databaseType enumeration with the database type
     * @throws DatabaseInstallerException if something goes wrong
     */
    public void createFromScratch(final DatabaseType databaseType, final ApplicationMode applicationMode, final String propertiesFileName)
            throws DatabaseException {
        new SQLExecutor().createFromScratch(databaseType, applicationMode, propertiesFileName, ZERO_VERSION);
    }
}
