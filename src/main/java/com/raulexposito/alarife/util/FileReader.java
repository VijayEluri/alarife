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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Class whose mission is read and extract the content of files
 */
public final class FileReader {

    private static final Log log = LogFactory.getLog(FileReader.class);

    /**
     * Returns a file from the classpath as an <code>InputStream</code>
     * @param filePath relative path to the file to be recovered
     * @return an <code>InputStream</code> of the file
     */
    public InputStream getFileAsInputStream(final String filePath) {
        // log if the file exists
        getFileFormRelativeURL(filePath);

        return this.getClass().getClassLoader().getResourceAsStream(filePath);
    }

    /**
     * Returns an object of class <code>File</code> of a file
     * @param filePath relative path to the file to be recovered
     * @return an <code>File</code> of the file
     */
    public File getFileFormRelativeURL(final String filePath) {
        File file = null;

        try {
            file = new File(this.getClass().getClassLoader().getResource(filePath).toURI());
            log.debug("The file is in '" + file.getAbsolutePath() + "'");
        } catch (URISyntaxException ex) {
            log.warn("cannot get the URI of the file '" + filePath + "': " + ex);
        }

        return file;
    }
}
