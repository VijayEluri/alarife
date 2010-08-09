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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Abtract util class whose mission is being the base of another classes
 * designed to read properties files
 */
public abstract class PropertiesReader {

    private static final Log log = LogFactory.getLog(PropertiesReader.class);
    // Properties of the file that has to be readed
    private final Properties properties = new Properties();

    /**
     * Constructs a properties file reader
     * @param filename file to be readed
     * @throws java.io.IOException if the file doesn't exist or cannot be readed
     */
    public PropertiesReader(final String filename)
            throws IOException {
        try {
            final InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);

            if (is == null) {
                log.warn("the file '" + filename + "' is not in the classpath: ");
                throw new IOException("the file '" + filename + "' is not in the classpath");
            }

            properties.load(is);
            log.trace("the file '" + filename + "' has been loaded");
        } catch (IOException ioe) {
            log.warn("there is an IO error reading the file '" + filename + "'");
            throw ioe;
        }
    }

    /**
     * Returns the value of the key found in the properties file or the default
     * value if the key is not found
     * @param key name of the value to found in the properties file
     * @param defaultValue value to return if the key is not in the file
     * @return the value of the key found in the properties file or the default
     * value if none
     */
    public String getProperty(final String key, final String defaultValue) {
        final String recoveredValue = properties.getProperty(key, defaultValue);
        log.trace("finding the value '" + key + "' (default: '" + defaultValue + "', recovered :'" + recoveredValue +
                "')");

        return recoveredValue;
    }

    /**
     * Returns the value of the key found in the properties file or the default
     * value if the key is not found and replaces the undefines values
     * @param key name of the value to found in the properties file
     * @param defaultValue value to return if the key is not in the file
     * @param replacedValues values to be replaced in the properties file
     * @return the value of the key found in the properties file or the default
     * value if none
     */
    public String getProperty(final String key, final String defaultValue, final String[] replacedValues) {
        return replacedText(properties.getProperty(key, defaultValue),
                replacedValues);
    }

    /**
     * Replaces the undefined values of a phrase with form '{n}' with
     * other values received in a String Array
     * @param text phrase with the undefined values
     * @param value colletion of values to substitute
     * @return the phrase with the values changed
     */
    private String replacedText(final String text, final String[] value) {
        String replacedText = text;

        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                if (replacedText.indexOf("{" + i + "}") != -1) {
                    replacedText = replacedText.replaceAll("\\{" + i + "\\}", value[i]);
                }
            }
        }

        log.trace("the text '" + text + "' has been replaced by '" + replacedText + "'");

        return replacedText;
    }
}
