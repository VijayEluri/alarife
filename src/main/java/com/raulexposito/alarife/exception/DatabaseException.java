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
package com.raulexposito.alarife.exception;

/**
 * Exception thrown and catched in the generation and upgrade of the database
 */
public class DatabaseException
        extends Exception {

    /**
     * Creates a new exception
     * @param msg phrase with the error
     */
    public DatabaseException(final String msg) {
        super(msg);
    }

    /**
     * Creates a new exception
     * @param msg phrase with the error
     * @param t original exception
     */
    public DatabaseException(final String msg, final Throwable t) {
        super(msg);
        this.setStackTrace(t.getStackTrace());
    }
}
