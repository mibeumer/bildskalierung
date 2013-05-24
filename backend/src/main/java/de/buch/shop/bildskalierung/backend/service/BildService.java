/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.backend.service;

import java.io.OutputStream;

/**
 * Das Inferface BildService stellt Funktionen zum Lesen, Bearbeiten und
 * Versenden von Bildern bereit.
 */
public interface BildService {

    public static final String IMAGE_TYPE = "jpg";

    // HttpServletResponse anpassen
    // Mit DownloadDialog ("application/octet-stream")

    /**
     * Skaliert das Bild welches in "filePath" uebergeben wird mit der Breite
     * "width" und der Hoehe "height". Zudem wird das skalierte Bild direkt in
     * den Outputstream "outputstream" geschrieben um performant zu arbeiten.
     * Dieser schickt die Datei direkt an den Client
     * 
     * @param filePath
     *            z.B. "img-adb/26/49/34602649-00-00.jpg"
     * @param width
     * @param height
     * @param outputstream
     */
    public void scaleFile(String filePath, int width, int height, OutputStream outputstream);

}
