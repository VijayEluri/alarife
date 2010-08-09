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
package com.raulexposito.alarife.upgrader;

import com.raulexposito.alarife.upgrader.pojo.Version;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VersionNumberSorterTest {

    @Test
    public void testSortListWithVersionNumbers() {
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

        Collections.sort(list);

        if (!list.get(0).equals(v0_0_0)) {
            fail("[0] must be '0.0.0', but it's: '" + list.get(0) + "'");
        }

        if (!list.get(1).equals(v0_0_7)) {
            fail("[1] must be '0.0.7', but it's: '" + list.get(1) + "'");
        }

        if (!list.get(2).equals(v0_1_3)) {
            fail("[2] must be '0.1.3', but it's: '" + list.get(2) + "'");
        }

        if (!list.get(3).equals(v0_1_98)) {
            fail("[3] must be '0.1.98', but it's: '" + list.get(3) + "'");
        }

        if (!list.get(4).equals(v0_2_8)) {
            fail("[4] must be '0.2.8', but it's: '" + list.get(4) + "'");
        }

        if (!list.get(5).equals(v1_15_44)) {
            fail("[5] must be '1.15.44', but it's: '" + list.get(5) + "'");
        }

        if (!list.get(6).equals(v2_3_66)) {
            fail("[6] must be '2.3.66', but it's: '" + list.get(6) + "'");
        }

        if (!list.get(7).equals(v3_0_24)) {
            fail("[7] must be '3.0.24', but it's: '" + list.get(7) + "'");
        }
    }
}
