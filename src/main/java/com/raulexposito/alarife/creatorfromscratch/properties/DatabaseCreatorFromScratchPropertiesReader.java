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
package com.raulexposito.alarife.creatorfromscratch.properties;

import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.util.DatabasePropertiesReaderUtil;
import java.io.IOException;

/**
 * Reads the content of the properties file that contains the database
 * configuration needed to generate it
 */
public class DatabaseCreatorFromScratchPropertiesReader
        extends DatabasePropertiesReaderUtil {
    // suffix of a properties file key

    private static final String DROP_DATABASE = ".dropDatabase";
    // suffix of a properties file key
    private static final String CREATE_DATABASE = ".createDatabase";
    // suffix of a properties file key
    private static final String CHANGE_DATABASE = ".changeDatabase";
    // suffix of a properties file key
    private static final String CREATE_TABLE = ".createVersionTable";
    // suffix of a properties file key
    private static final String INSERT_TABLE = ".insertVersionTable";
    // default values for MySQL database generation
    private static final String DEFAULT_MYSQL_DROP_DATABASE = "drop database if exists `{0}`;";
    private static final String DEFAULT_MYSQL_CREATE_DATABASE = "create database if not exists `{0}`;";
    private static final String DEFAULT_MYSQL_CHANGE_DATABASE = "use `{0}`;";
    private static final String DEFAULT_MYSQL_CREATE_TABLE =
            "create table `{0}`.`Version` (`version` VARCHAR(10) NOT NULL);";
    private static final String DEFAULT_MYSQL_INSERT_TABLE = "insert into `{0}`.`Version` (`version`) values ('{1}');";

    // ----------------------------- //
    //          CONSTRUCTOR          //
    // ----------------------------- //
    public DatabaseCreatorFromScratchPropertiesReader(final String databasePropertiesfile)
            throws IOException {
        super(databasePropertiesfile);
    }

    // ----------------------------- //
    //            METHODS            //
    // ----------------------------- //
    /**
     * Recovers the value of the command that drops the database received by
     * parameter<br>
     * <br>
     * for instance, if the database is mysql, it finds the key:<br>
     * MYSQL.dropDatabase<br>
     *
     * @param databaseType type of the database
     * @return the value of the command that drops the database received by
     * parameter
     */
    public String getDropDatabase(final DatabaseType databaseType) {
        final String[] parameters = new String[]{super.getSchema()};
        return super.getProperty(databaseType + DROP_DATABASE, DEFAULT_MYSQL_DROP_DATABASE, parameters);
    }

    /**
     * Recovers the value of the command that creates the database received by
     * parameter<br>
     * <br>
     * for instance, if the database is mysql, it finds the key:<br>
     * MYSQL.createDatabase<br>
     *
     * @param databaseType type of the database
     * @return the value of the command that creates the database received by
     * parameter
     */
    public String getCreateDatabase(final DatabaseType databaseType) {
        final String[] parameters = new String[]{super.getSchema()};
        return super.getProperty(databaseType + CREATE_DATABASE, DEFAULT_MYSQL_CREATE_DATABASE, parameters);
    }

    /**
     * Recovers the value of the command that changes the database received by
     * parameter<br>
     * <br>
     * for instance, if the database is mysql, it finds the key:<br>
     * MYSQL.changeDatabase<br>
     *
     * @param databaseType type of the database
     * @return the value of the command that changes the database received by
     * parameter
     */
    public String getChangeDatabase(final DatabaseType databaseType) {
        final String[] parameters = new String[]{super.getSchema()};
        return super.getProperty(databaseType + CHANGE_DATABASE, DEFAULT_MYSQL_CHANGE_DATABASE, parameters);
    }

    /**
     * Recovers the value of the command that creates the table 'Version'<br>
     * <br>
     * for instance, if the database is mysql, it finds the key:<br>
     * MYSQL.createVersionTable<br>
     *
     * @param databaseType type of the database
     * @return the value of the command that creates the database received by
     * parameter
     */
    public String getCreateVersionTable(final DatabaseType databaseType) {
        final String[] parameters = new String[]{super.getSchema()};
        return super.getProperty(databaseType + CREATE_TABLE, DEFAULT_MYSQL_CREATE_TABLE, parameters);
    }

    /**
     * Recovers the value of the command that inserts the version of the
     * software in the table 'Version'<br>
     * <br>
     * for instance, if the database is mysql, it finds the key:<br>
     * MYSQL.insertVersionTable<br>
     *
     * @param databaseType type of the database
     * @param version version number to save in database
     * @return the value of the command that creates the database received by
     * parameter
     */
    public String getInsertVersionTable(final DatabaseType databaseType, final String version) {
        final String[] parameters = new String[]{super.getSchema(), version};
        return super.getProperty(databaseType + INSERT_TABLE, DEFAULT_MYSQL_INSERT_TABLE, parameters);
    }
}
