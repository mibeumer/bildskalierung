/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.backend.serviceimpl;

import java.io.IOException;
import java.io.OutputStream;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.buch.shop.bildskalierung.backend.config.ConfigBean;
import de.buch.shop.bildskalierung.backend.exception.BildskalierungException;
import de.buch.shop.bildskalierung.backend.service.BildService;

/**
 * Ist die Implementierung von BildService und bietet Methoden zum Skalieren und
 * Versenden eines Bildes an den Client.
 */
@Component("graphicsMagickBildService")
public class GraphicsMagickBildServiceImpl implements BildService {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GraphicsMagickBildServiceImpl.class);

    private static final int BUFFER_SIZE = 4096;

    @Autowired
    private ConfigBean configBean;

    /**
     * 
     * Alte Moeglichkeit nur mit IM4Java geloest ohne Pooling. Es wird jedes mal
     * ein neuer Thread erzeugt.
     * 
     * @param filePath
     * @param width
     * @param height
     * @param outputstream
     */
    public void scaleFile(final String filePath, final int width, final int height, final OutputStream outputstream) {

        // "true" -> GraphicsMagick und nicht ImageMagick.
        ConvertCmd cmd = new ConvertCmd(true);
        // ConvertCmd cmd = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        String absFilePath = configBean.getImagePath() + filePath;
        LOG.debug("Der absolute File Path: " + absFilePath);

        op.addImage(absFilePath);
        op.resize(width, height, ">");
        op.addImage("-"); // "jpg-"
        try {
            Pipe pipeOut = new Pipe(null, outputstream);

            cmd.setOutputConsumer(pipeOut);
            cmd.run(op);

        } catch (IOException e) {
            // TODO Alternative einbauen Default-Bild zurueck
            throw new BildskalierungException("Das Bild konnte nicht gelesen oder geschrieben werden", e);
        } catch (InterruptedException e) {
            throw new BildskalierungException("Der GraphicsMagick Thread ist beschädigt", e);
        } catch (IM4JavaException e) {
            throw new BildskalierungException("GraphicsMagick konnte das Bild nicht skalieren", e);
        } finally {
            try {
                if (outputstream != null) {
                    outputstream.close();
                }
            } catch (IOException e) {
                throw new BildskalierungException("Der Response-Outputstream konnte nicht geschlossen werden. ", e);
            }
        }
    }
}
