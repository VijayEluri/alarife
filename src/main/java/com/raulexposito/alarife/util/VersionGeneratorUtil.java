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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class useful to generate version numbers
 */
public final class VersionGeneratorUtil {

    private static final Log log = LogFactory.getLog(VersionGeneratorUtil.class);
    private static final String ONLY_THREE_ELEMENTS = "the must be only three elements to generate a x.y.z version";

    /**
     * private constructor to avoid instantiations
     */
    private VersionGeneratorUtil() {
    }

    /**
     * Tries to generate a version number object from a single String
     * @param version in a String mode
     * @return a object with the vversion number
     * @throws com.raulexposito.alarife.exception.DatabaseException if there are any error converting the String into an object
     */
    public static Version generateVersion(final String version)
            throws DatabaseException {
        Integer mayor = 0;
        Integer minor = 0;
        Integer revision = 0;

        try {
            final String[] numbers = version.split("[.]");

            if (numbers.length != 3) {
                log.warn(ONLY_THREE_ELEMENTS);
                throw new DatabaseException(ONLY_THREE_ELEMENTS);
            }

            mayor = Integer.parseInt(numbers[0]);
            minor = Integer.parseInt(numbers[1]);
            revision = Integer.parseInt(numbers[2]);
        } catch (Exception e) {
            log.warn("It was impossible generate a version x.y.x from '" + version + "'", e);
            throw new DatabaseException(e.getMessage(), e);
        }

        return new Version(mayor, minor, revision);
    }
}
