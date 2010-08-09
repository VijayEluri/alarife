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
package com.raulexposito.alarife.upgrader.pojo;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Simple pojo that contains an application version number. This pojo can be easily
 * sorted using 'Collections.sort();'
 *
 * http://www.adictosaltrabajo.com/tutoriales/tutoriales.php?pagina=listas
 */
public class Version
        implements Comparable {

    private final Integer mayor;
    private final Integer minor;
    private final Integer revision;

    // ---------------------------------------------- //
    //                  CONSTRUCTORS                  //
    // ---------------------------------------------- //
    public Version(final Integer mayor, final Integer minor, final Integer revision) {
        this.mayor = mayor;
        this.minor = minor;
        this.revision = revision;
    }

    // ---------------------------------------------- //
    //                    GETTERS                     //
    // ---------------------------------------------- //
    public Integer getMayor() {
        return mayor;
    }

    public Integer getMinor() {
        return minor;
    }

    public Integer getRevision() {
        return revision;
    }

    // ---------------------------------------------- //
    //           COMPARABLE IMPLEMENTATION            //
    // ---------------------------------------------- //
    public int compareTo(final Object object) {
        final Version version = (Version) object;

        if (this.getMayor().equals(version.getMayor())) {
            if (this.getMinor().equals(version.getMinor())) {
                return this.getRevision().compareTo(version.getRevision());
            } else {
                return this.getMinor().compareTo(version.getMinor());
            }
        } else {
            return this.getMayor().compareTo(version.getMayor());
        }
    }

    // ---------------------------------------------- //
    //               OBJECT OVERRIDINGS               //
    // ---------------------------------------------- //
    @Override
    public String toString() {
        return Integer.toString(mayor) + "." + Integer.toString(minor) + "." + Integer.toString(revision);
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(this.getMayor());
        hcb.append(this.getMinor());
        hcb.append(this.getRevision());

        return hcb.toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Version)) {
            return false;
        }

        boolean isEquals = false;

        try {
            final Version other = (Version) obj;
            final EqualsBuilder eqb = new EqualsBuilder();
            eqb.append(this.getMayor(),
                    other.getMayor());
            eqb.append(this.getMinor(),
                    other.getMinor());
            eqb.append(this.getRevision(),
                    other.getRevision());
            isEquals = eqb.isEquals();
        } catch (Exception e) {
            isEquals = false;
        }

        return isEquals;
    }
}
