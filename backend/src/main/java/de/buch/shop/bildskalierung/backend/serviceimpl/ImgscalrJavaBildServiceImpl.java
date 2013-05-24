/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.backend.serviceimpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
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
@Component("imgscalrJavaBildService")
public class ImgscalrJavaBildServiceImpl implements BildService {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImgscalrJavaBildServiceImpl.class);

    @Autowired
    private ConfigBean configBean;

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.buch.shop.bildskalierung.backend.service.ImageMagickService#getFile()
     */
    @Override
    public void scaleFile(final String filePath, final int scaledWidth, final int scaledHeight,
            final OutputStream outputstream) {

        WidthAndHeight originalWidthAndHeight = new WidthAndHeight(scaledWidth, scaledHeight);

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(configBean.getImagePath() + filePath));
            int orgWidth = bufferedImage.getWidth();
            int orgHeight = bufferedImage.getHeight();

            final WidthAndHeight requestedWidthAndHeight = WidthAndHeight.determineScaleFactor(new WidthAndHeight(
                    orgWidth, orgHeight),
                    new WidthAndHeight(originalWidthAndHeight.getWidth(), originalWidthAndHeight.getHeight()));

            if (requestedWidthAndHeight == originalWidthAndHeight) {
                // Hier können wir das Bild unverändert herausschreiben.
                ImageIO.write(bufferedImage, IMAGE_TYPE, outputstream);
            } else {
                // Skaliere die Bilder
                // Scalr.OP_ANTIALIAS
                ImageIO.write(Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC,
                        requestedWidthAndHeight.getWidth().intValue(), requestedWidthAndHeight.getHeight().intValue()),
                        IMAGE_TYPE, outputstream);
            }
        } catch (IOException e) {
            throw new BildskalierungException("Das Bild konnte nicht gelesen oder geschrieben werden", e);
        } finally {
            try {
                if (outputstream != null) {
                    outputstream.close();
                }
            } catch (final Exception e) {
                throw new BildskalierungException("Der Outputstream konnte nicht geschlossen werden ", e);
            }
            bufferedImage.flush();
        }
    }

}
