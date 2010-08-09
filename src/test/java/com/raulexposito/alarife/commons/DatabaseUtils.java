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
package com.raulexposito.alarife.commons;

import com.raulexposito.alarife.upgrader.pojo.Version;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {

    public static final String DEMONIC_TESTING_VERSION = "6.6.6";
    private static final String DATABASE_GENERATION_PROPERTIES_DIR = "databaseGeneration" + File.separator;
    public static final String DATABASE_PROPERTIES_FILE =
            DATABASE_GENERATION_PROPERTIES_DIR + "database-generation.properties";
    public static final String WRONG_INSTANCE_FILE = DATABASE_GENERATION_PROPERTIES_DIR + "wrong-instance.properties";
    public static final String WRONG_DRIVER_FILE = DATABASE_GENERATION_PROPERTIES_DIR + "wrong-driver.properties";
    public static final String WRONG_USERNAME_FILE = DATABASE_GENERATION_PROPERTIES_DIR + "wrong-username.properties";
    public static final String WRONG_PASSWORD_FILE = DATABASE_GENERATION_PROPERTIES_DIR + "wrong-password.properties";
    public static final String WRONG_MYSQL_DROP_DATABASE_FILE =
            DATABASE_GENERATION_PROPERTIES_DIR + "wrong-mysql-drop.properties";
    public static final String WRONG_DATABASE_PROPERTIES_FILE =
            DATABASE_GENERATION_PROPERTIES_DIR + "no_exist.properties";

    /**
     * Generates a list of versions to develop test
     * @return a list of versions to develop test
     */
    public static List<Version> generateTestVersionList() {
        final List<Version> list = new ArrayList<Version>();
        final Version v0_0_7 = new Version(0, 0, 7);
        final Version v0_1_3 = new Version(0, 1, 3);
        final Version v0_2_8 = new Version(0, 2, 8);
        final Version v0_0_0 = new Version(0, 0, 0);
        final Version v0_1_98 = new Version(0, 1, 98);
        final Version v3_0_24 = new Version(3, 0, 24);
        final Version v1_15_44 = new Version(1, 15, 44);
        final Version v2_3_66 = new Version(2, 3, 66);

        list.add(v0_0_7);
        list.add(v0_1_3);
        list.add(v0_2_8);
        list.add(v0_0_0);
        list.add(v0_1_98);
        list.add(v3_0_24);
        list.add(v1_15_44);
        list.add(v2_3_66);

        return list;
    }
}
