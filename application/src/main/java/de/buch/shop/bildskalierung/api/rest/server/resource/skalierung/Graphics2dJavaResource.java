/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.api.rest.server.resource.skalierung;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.buch.commons.utils.api.rest.server.resource.BaseRestResource;
import de.buch.shop.bildskalierung.backend.exception.BildskalierungException;
import de.buch.shop.bildskalierung.backend.service.BildService;

/**
 * Eine Rest-Resource um ein Bild in ein bestimmtes Format zu skalieren.
 */
@Controller
@RequestMapping(value = "api/rest/graphics2djava/*")
public class Graphics2dJavaResource extends BaseRestResource implements InterfaceRessource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Graphics2dJavaResource.class);

    @Inject
    private BildService graphics2dJavaBildService;

    /**
     * Rest-Service welcher mit dem Path zum Bild aufgerufen wird. Er das Bild
     * zurück.
     * 
     * Ausgangspunkt: http://media.buch.de/img-adb/34201026-00-00/inferno.jpg
     * Input:
     * http://localhost:8080/de.buch.shop.bildskalierung.application/api/rest
     * /imagemagick/img-adb/34201026-00-00/inferno.jpg
     * 
     * Bildnummer ab, z.B. liegt das Bilder 15724534-00-06.jpg im Verzeichnis
     * /img-adb/45/34/ Im Bild noch ArtikelNr-SequenzNr-Size.jpg
     * 
     * http://media-ftp.buch.de//img-adb/26/49/34602649-00-00.jpg
     * 
     * @param fileName
     * @return
     */
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

        if (width <= 0) {
            throw new IllegalArgumentException("Inputparameter scaledWidth is 0 oder negativ");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Inputparameter scaledHeight is 0 oder negativ");
        }

        response.setContentType(MIME_TYPE);

        try {
            graphics2dJavaBildService.scaleFile(SLASH + filePath + SLASH + filePath2 + SLASH + file + POINT + fileEnd,
                    width, height, response.getOutputStream());
        } catch (IOException e) {
            throw new BildskalierungException("Der Rest-Request hat keinen Outputstream der beschrieben werden kann ",
                    e);
        }

    }
}
