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

import java.io.IOException;

/**
 * Superclass for this classes that has to read the properties with the
 * database configuration
 */
public class DatabasePropertiesReaderUtil
        extends PropertiesReader {
    // properties file key

    private static final String INSTANCE = "instance";
    // properties file key
    private static final String DRIVER_CLASS_NAME = "driverClassName";
    // properties file key
    private static final String USERNAME = "username";
    // properties file key
    private static final String PASSWORD = "password";
    // properties file key
    private static final String SCHEMA = "schema";
    // default values for MySQL connection
    private static final String DEFAULT_INSTANCE = "jdbc:mysql://localhost:3306/";
    private static final String DEFAULT_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "";
    private static final String DEFAULT_SCHEMA = "alarife";

    // ----------------------------- //
    //          CONSTRUCTOR          //
    // ----------------------------- //
    public DatabasePropertiesReaderUtil(final String databasePropertiesfile)
            throws IOException {
        super(databasePropertiesfile);
    }

    // ----------------------------- //
    //            METHODS            //
    // ----------------------------- //
    /**
     * Recovers the value of the database instance
     * @return the value of the database instance
     */
    public String getInstance() {
        return super.getProperty(INSTANCE, DEFAULT_INSTANCE);
    }

    /**
     * Recovers the value of the driver of the database
     * @return the value of the driver of the database
     */
    public String getDriverClassName() {
        return super.getProperty(DRIVER_CLASS_NAME, DEFAULT_DRIVER_CLASS_NAME);
    }

    /**
     * Recovers the value of the username of the database
     * @return the value of the username of the database
     */
    public String getUsername() {
        return super.getProperty(USERNAME, DEFAULT_USERNAME);
    }

    /**
     * Recovers the value of the password of the username of the database
     * @return the value of the password of the username of the database
     */
    public String getPassword() {
        return super.getProperty(PASSWORD, DEFAULT_PASSWORD);
    }

    /**
     * Recovers the value of the name of the schema of the database
     * @return the value of the name of the schema of the database
     */
    public String getSchema() {
        return super.getProperty(SCHEMA, DEFAULT_SCHEMA);
    }
}
