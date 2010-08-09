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

import com.raulexposito.alarife.exception.DatabaseException;
import com.raulexposito.alarife.upgrader.pojo.Version;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests that the version generator works perfectly. To perform this action, the
 * version generator has to recibe as parameter an String as the name of the
 * directory with the scripts to update data and create new tables
 */
public class VersionGeneratorTest {

    @Test(expected = DatabaseException.class)
    public void testGenerateVersionWithText()
            throws DatabaseException {
        final String phrase = "this cant' work";
        VersionGeneratorUtil.generateVersion(phrase);
    }

    @Test(expected = DatabaseException.class)
    public void testGenerateVersionWithNull()
            throws DatabaseException {
        VersionGeneratorUtil.generateVersion(null);
    }

    @Test(expected = DatabaseException.class)
    public void testGenerateVersionWithoutAllParameters()
            throws DatabaseException {
        final String phrase = "0.1";
        VersionGeneratorUtil.generateVersion(phrase);
    }

    @Test(expected = DatabaseException.class)
    public void testGenerateVersionTextAndNumbers()
            throws DatabaseException {
        final String phrase = "1.b.3";
        VersionGeneratorUtil.generateVersion(phrase);
    }

    @Test
    public void testGenerateVersionWithAllParameters()
            throws DatabaseException {
        final String phrase = "1.2.3";
        final Version v1_2_3 = new Version(1, 2, 3);
        final Version generated = VersionGeneratorUtil.generateVersion(phrase);

        if (!generated.equals(v1_2_3)) {
            fail("both versions must be the same");
        }
    }

    @Test(expected = DatabaseException.class)
    public void testGenerateVersionWithExcedeedParameters()
            throws DatabaseException {
        final String phrase = "1.2.3.4";
        final Version v1_2_3 = new Version(1, 2, 3);
        final Version generated = VersionGeneratorUtil.generateVersion(phrase);

        if (!generated.equals(v1_2_3)) {
            fail("both versions must be the same");
        }
    }
}
