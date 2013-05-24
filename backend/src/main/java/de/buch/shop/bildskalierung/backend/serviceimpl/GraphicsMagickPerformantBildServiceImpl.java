/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.backend.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.gm4java.engine.GMConnection;
import org.gm4java.engine.GMException;
import org.gm4java.engine.GMServiceException;
import org.gm4java.engine.support.PooledGMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.buch.shop.bildskalierung.backend.config.ConfigBean;
import de.buch.shop.bildskalierung.backend.exception.BildskalierungException;
import de.buch.shop.bildskalierung.backend.service.BildService;

/**
 * Ist die Implementierung von BildService und bietet Methoden zum Skalieren und
 * Versenden eines Bildes an den Client.
 */
@Component("graphicsMagickPerformantBildService")
public class GraphicsMagickPerformantBildServiceImpl implements BildService {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GraphicsMagickPerformantBildServiceImpl.class);

    private static final int BUFFER_SIZE = 4096;

    @Autowired
    private ConfigBean configBean;

    @Autowired
    @Qualifier("gmService")
    private PooledGMService gmService;

    // @Autowired
    // private GMConnectionPoolConfig config;

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.buch.shop.bildskalierung.backend.service.ImageMagickService#getFile()
     */
    @Override
    public void scaleFile(final String filePath, final int width, final int height, final OutputStream outputstream) {

        GMConnection connection = null;
        try {
            connection = gmService.getConnection();

            String absFilePath = configBean.getImagePath() + filePath;
            String uuid = UUID.randomUUID().toString();
            File tempFile = File.createTempFile(uuid, ".jpg");

            String result2 = connection.execute("convert -size " + width + "x" + height + " " + absFilePath
                    + " -resize " + width + "x" + height + "> +profile \"*\" " + tempFile);

            LOG.debug(result2);

            writeResponse(outputstream, tempFile);

        } catch (GMServiceException e) {
            throw new BildskalierungException(
                    "GraphicsMagick konnte das Bild nicht skalieren oder der Pool an Threads ist beschädigt", e);
        } catch (GMException e) {
            throw new BildskalierungException("GraphicsMagick konnte das Bild nicht skalieren", e);
        } catch (IOException e) {
            throw new BildskalierungException("Das Bild konnte nicht gelesen oder geschrieben werden", e);
        } finally {

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (GMServiceException e) {
                throw new BildskalierungException(
                        "Der Thread konnte nicht ordnungegemaess an den Pool zurueckgegeben werden", e);
            }
        }
    }

    /**
     * Schreibt die skalierte Datei an den Client zurueck.
     * 
     * @param outputstream
     * @param tempFile
     */
    private void writeResponse(final OutputStream outputstream, final File tempFile) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempFile);
        } catch (FileNotFoundException e) {
            throw new BildskalierungException("Das temporaere Bild konnte nicht geoeffnet werden", e);
        }

        // get output stream of the response
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputstream.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            throw new BildskalierungException("Das Bild konnte nicht zurueckgeschickt werden", e);
        } finally {
            if (tempFile.exists() != false) {
                tempFile.delete();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new BildskalierungException(
                            "Der Fileinputstream konnte nicht ordnungsgemaess geschlossen werden.", e);
                }
            }
            if (outputstream != null) {
                try {
                    outputstream.close();
                } catch (IOException e) {
                    throw new BildskalierungException(
                            "Der Http-Outputstream konnte nicht ordnungsgemaess geschlossen werden.", e);
                }
            }
        }
    }

}
