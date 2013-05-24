/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.api.rest.server.resource.skalierung;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.buch.commons.utils.api.rest.server.resource.BaseRestResource;

/**
 * Eine Rest-Resource um ein Bild in ein bestimmtes Format zu skalieren.
 */
@Controller
@RequestMapping(value = "api/rest/dummy/*")
public class DummyResource extends BaseRestResource implements InterfaceRessource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(DummyResource.class);

    @RequestMapping(value = "/{file_path}/{file_path_2}/{file}.{width}.{height}.{fileEnd}", method = RequestMethod.GET)
    @ResponseBody
    public void scaleImage(final HttpServletResponse response, @PathVariable("file_path") final String filePath,
            @PathVariable("file_path_2") final String filePath2, @PathVariable("file") final String file,
            @PathVariable("width") final int width, @PathVariable("height") final int height,
            @PathVariable("fileEnd") final String fileEnd) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Dies ist der Pfad zur Datei: " + SLASH + filePath + SLASH + filePath2 + SLASH + file + POINT
                    + fileEnd);
        }

    }
}
