/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.backend.serviceimpl;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.buch.shop.bildskalierung.backend.config.ConfigBean;
import de.buch.shop.bildskalierung.backend.exception.BildskalierungException;
import de.buch.shop.bildskalierung.backend.service.BildService;

/**
 * Ist die Implementierung vom BildService und bietet Methoden zum Skalieren und
 * Versenden eines Bildes an den Client.
 */
@Component("graphics2dJavaBildService")
public class Graphics2dJavaBildServiceImpl implements BildService {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Graphics2dJavaBildServiceImpl.class);

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

        LOG.debug(configBean.getImagePath() + filePath + " Breite: " + width + " Hoehe: " + height);

        BufferedImage bufferedImage = null;
        try {
            // Read the original image from the Server Location
            bufferedImage = ImageIO.read(new File(configBean.getImagePath() + filePath));

            WidthAndHeight orgWidthAndHeight = new WidthAndHeight(bufferedImage.getWidth(), bufferedImage.getHeight());

            final WidthAndHeight requestedWidthAndHeight = WidthAndHeight.determineScaleFactor(orgWidthAndHeight,
                    definedWidthAndHeight);

            if ((orgWidthAndHeight.getWidth() <= definedWidthAndHeight.getWidth())
                    && (orgWidthAndHeight.getHeight() <= definedWidthAndHeight.getHeight())) {
                // if (orgWidthAndHeight == requestedWidthAndHeight) {
                /*
                 * Hier können wir das Bild unverändert herausschreiben.
                 */
                ImageIO.write(bufferedImage, "jpg", outputstream);
            } else {
                // Schreib das skalierte Bild in die Response
                ImageIO.write(
                        getScaledInstance(bufferedImage, requestedWidthAndHeight.getWidth().intValue(),
                                requestedWidthAndHeight.getHeight().intValue(),
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR, true), "jpg", outputstream);
            }

        } catch (IOException e) {
            throw new BildskalierungException("Das Bild konnte nicht gelesen oder geschrieben werden ", e);
        } finally {
            try {
                if (outputstream != null) {
                    outputstream.close();
                }
            } catch (final Exception e) {
                throw new BildskalierungException("Der Outputstream konnte nicht geschlossen werden ", e);
            }
            if (bufferedImage != null) {
                bufferedImage.flush();
            }
        }
    }

    /**
     * Convenience method that returns a scaled instance of the provided
     * {@code BufferedImage}.
     * 
     * @param img
     *            the original image to be scaled
     * @param targetWidth
     *            the desired width of the scaled instance, in pixels
     * @param targetHeight
     *            the desired height of the scaled instance, in pixels
     * @param hint
     *            one of the rendering hints that corresponds to
     *            {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *            {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *            {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *            {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality
     *            if true, this method will use a multi-step scaling technique
     *            that provides higher quality than the usual one-step technique
     *            (only useful in downscaling cases, where {@code targetWidth}
     *            or {@code targetHeight} is smaller than the original
     *            dimensions, and generally only when the {@code BILINEAR} hint
     *            is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
    public BufferedImage getScaledInstance(final BufferedImage img, final int targetWidth, final int targetHeight,
            final Object hint, final boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
                : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && (w > targetWidth)) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && (h > targetHeight)) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while ((w != targetWidth) || (h != targetHeight));
        img.flush();
        return ret;
    }

    // /**
    // * Bereitet die wirkliche Skalierung von dem "originalImage" mit der
    // Breite
    // * "scaledWidth" und der Hoehe "scaledHeight" mit der Graphics2D
    // * Java-Standard-Library vor.
    // *
    // * Es wird durch RenderingHints die hoechstmoegliche Qualitaet gewaehlt
    // *
    // * @param originalImage
    // * @param scaledWidth
    // * @param scaledHeight
    // * @return
    // */
    // BufferedImage createResizedCopy(final BufferedImage originalImage, final
    // int scaledWidth, final int scaledHeight) {
    // BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight,
    // BufferedImage.TYPE_INT_RGB);
    // Graphics2D g2d = scaledBI.createGraphics();
    // // g2d.setComposite(AlphaComposite.Src); // eher unuetz
    // // below three lines are for RenderingHints for better image quality at
    // // cost of higher processing time
    // g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    // RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    // g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
    // RenderingHints.VALUE_RENDER_QUALITY);
    // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    // RenderingHints.VALUE_ANTIALIAS_ON);// kann
    // // Sinn
    // // machen
    // // für
    // // Rand-Schoenheit
    //
    // // Wirft keine Exception wenn das reinkommende File z.B. PDF ist,
    // // überprüfen und Exception werfen!
    // g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
    // g2d.dispose();
    // originalImage.flush();
    // return scaledBI;
    // }
}
