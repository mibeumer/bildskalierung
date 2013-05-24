/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.backend.config;

/**
 * Hält die Konfigurationsoptionen für die Image-Konvertierung bereit. Wird per
 * Spring mit der Properties-Datei
 * "de.buch.shop.bildskalierung-config.properties" geschrieben.
 */
public class ConfigBean {

    private String imagePath; // z.B. "/home/beumer/img-adb"

    /**
     * Generated getter for the imagePath property.
     * 
     * @return the imagePath currently used.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Generated setter for the imagePath property.
     * 
     * @param imagePath
     *            the imagePath to set.
     */
    public void setImagePath(final String imagePath) {
        this.imagePath = imagePath;
    }
}
