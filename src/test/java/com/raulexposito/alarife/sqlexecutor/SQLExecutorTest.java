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

import com.raulexposito.alarife.enumeration.ApplicationMode;
import com.raulexposito.alarife.enumeration.DatabaseType;
import com.raulexposito.alarife.upgrader.pojo.Version;
import com.raulexposito.alarife.util.ScriptsDirectoryUtil;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SQLExecutorTest {

    private static final String FIRST_COMMAND =
            "CREATE TABLE `Team` (`id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,PRIMARY KEY (`id`)) engine=innodb default charset=utf8 collate=utf8_spanish_ci;";
    private static final String SECOND_COMMAND =
            "CREATE TABLE `OMG` (`id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,PRIMARY KEY (`id`)) engine=innodb default charset=utf8 collate=utf8_spanish_ci;";

    @Test
    public void testGetSQLCommandsFromScriptFile()
            throws IOException {
        final Version version = new Version(0, 0, 5);
        final InputStream upgradeTables =
                new ScriptsDirectoryUtil().getUpgradeTablesScript(DatabaseType.MYSQL, ApplicationMode.TESTING, version);
        final List<String> commands = SQLExecutor.getSQLCommandsFromScriptFile(upgradeTables);

        if (commands.size() != 2) {
            fail("In the script should be only two sql commands");
        }

        if (!commands.get(0).equals(FIRST_COMMAND)) {
            fail("the sql command recovered should be '" + FIRST_COMMAND + "' instead of '" + commands.get(0) + "'");
        }

        if (!commands.get(1).equals(SECOND_COMMAND)) {
            fail("the sql command recovered should be '" + SECOND_COMMAND + "' instead of '" + commands.get(1) +
                    "'");
        }
    }
}
