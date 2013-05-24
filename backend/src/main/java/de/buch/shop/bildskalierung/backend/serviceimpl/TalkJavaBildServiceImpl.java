/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.backend.serviceimpl;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.buch.shop.bildskalierung.backend.config.ConfigBean;
import de.buch.shop.bildskalierung.backend.service.BildService;

/**
 * Ist die Implementierung vom BildService und bietet Methoden zum Skalieren und
 * Versenden eines Bildes an den Client.
 */
@Component("talkJavaBildService")
public class TalkJavaBildServiceImpl implements BildService {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TalkJavaBildServiceImpl.class);

    @Autowired
    private ConfigBean configBean;

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.buch.shop.bildskalierung.backend.service.ImageMagickService#getFile()
     */
    @Override
    public void scaleFile(final String filePath, final int width, final int height, final OutputStream outputstream) {

        WidthAndHeight definedWidthAndHeight = new WidthAndHeight(width, height);

        // Read the original image from the Server Location
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(configBean.getImagePath() + filePath));
        } catch (IOException e1) {
            LOG.debug("Das Bild konnte nicht aus dem Dateisystem gelesen werden", e1);
        }

        int orgWidth = bufferedImage.getWidth();
        int orgHeight = bufferedImage.getHeight();

        final WidthAndHeight requestedWidthAndHeight = WidthAndHeight.determineScaleFactor(new WidthAndHeight(orgWidth,
                orgHeight), definedWidthAndHeight);

        // Alte TALK
        Image scaledImage = null;

        BufferedImage resultImage = null;
        Graphics2D g2 = null;
        ImageWriter writer = null;
        ImageOutputStream imageOut = null;
        try {
            /*
             * Nachdem wir anhand des Originals ausgerechnet haben, wie groß das
             * Zielbild sein soll, lesen wir mal das Original in ein wirkliches
             * Bild ein!
             */
            if (bufferedImage == null) {
                LOG.error("writeScaledImageToFile: Bilddaten zur Skalierung konnten nicht eingelesen werden!");
                throw new IllegalArgumentException("input file could not be read");
            }
            /*
             * ...und skalieren es.
             */
            scaledImage = bufferedImage.getScaledInstance(requestedWidthAndHeight.getWidth().intValue(),
                    requestedWidthAndHeight.getHeight().intValue(), Image.SCALE_AREA_AVERAGING);
            System.out.println(bufferedImage.getColorModel().getColorSpace().getType());
            /*
             * Das Resultat zu schreiben erfordert mehr Anstrengung...
             */

            resultImage = new BufferedImage(requestedWidthAndHeight.getWidth().intValue(), requestedWidthAndHeight
                    .getHeight().intValue(), BufferedImage.TYPE_INT_RGB);
            // resultImage.getRaster().setRect(bufferedImage.getRaster());

            g2 = resultImage.createGraphics();
            // Optimierte TALK
            // g2.setComposite(AlphaComposite.Src); // eher unuetz
            // g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            // RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            // g2.setRenderingHint(RenderingHints.KEY_RENDERING,
            // RenderingHints.VALUE_RENDER_QUALITY);
            // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            // RenderingHints.VALUE_ANTIALIAS_ON); // kann Sinn machen für
            // Rand-Schoenheit
            // g2.drawImage(bufferedImage, 0, 0,
            // requestedWidthAndHeight.getWidth().intValue(),
            // requestedWidthAndHeight
            // .getHeight().intValue(), null);

            // Alte TALK
            g2.drawImage(scaledImage, 0, 0, null);

            writer = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
            imageOut = ImageIO.createImageOutputStream(outputstream);
            writer.setOutput(imageOut);
            /*
             * wegschreiben stets als JPEG mit konfigurierter compression
             * quality
             */
            final ImageWriteParam writeParam = new JPEGImageWriteParam(Locale.getDefault());
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(0.9F); // siehe
                                                    // MediaServiceImageExportDisk
                                                    // 0.9F
            writer.write(null, new IIOImage(resultImage, null, null), writeParam);
        } catch (IOException e) {
            LOG.debug("Das Bild konnte in die Response an den Client geschrieben werden", e);
        } finally {
            try {
                if (outputstream != null) {
                    outputstream.close();
                }
            } catch (final Exception e) {
                LOG.warn("Fehler beim Abschluss der Bildskalierung.", e);
            }
            try {
                if (writer != null) {
                    writer.dispose();
                }
            } catch (final Exception e) {
                LOG.warn("Fehler beim Abschluss der Bildskalierung.", e);
            }
            try {
                if (imageOut != null) {
                    imageOut.close();
                }
            } catch (final Exception e) {
                LOG.warn("Fehler beim Abschluss der Bildskalierung.", e);
            }
            try {
                if (resultImage != null) {
                    resultImage.flush();
                }
            } catch (final Exception e) {
                LOG.warn("Fehler beim Abschluss der Bildskalierung.", e);
            }
            // Alte TALK
            try {
                if (scaledImage != null) {
                    scaledImage.flush();
                }
            } catch (final Exception e) {
                LOG.warn("Fehler beim Abschluss der Bildskalierung.", e);
            }
            try {
                if (bufferedImage != null) {
                    bufferedImage.flush();
                }
            } catch (final Exception e) {
                LOG.warn("Fehler beim Abschluss der Bildskalierung.", e);
            }
            try {
                if (g2 != null) {
                    g2.dispose();
                }
            } catch (final Exception e) {
                LOG.warn("Fehler beim Abschluss der Bildskalierung.", e);
            }
        }
    }
}
