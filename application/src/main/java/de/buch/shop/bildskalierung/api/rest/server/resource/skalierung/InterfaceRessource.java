/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.api.rest.server.resource.skalierung;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;

/**
 * Das Interface zu den verschiedenen Rest-Ressourcen
 */
public interface InterfaceRessource {

    public static final String MIME_TYPE = "image/jpeg";
    public static final String SLASH = "/";
    public static final String POINT = ".";

    public void scaleImage(final HttpServletResponse response, @PathVariable("file_path") final String filePath,
            @PathVariable("file_path_2") final String filePath2, @PathVariable("file") final String file,
            @PathVariable("width") final int width, @PathVariable("height") final int height,
            @PathVariable("fileEnd") final String fileEnd);

}
