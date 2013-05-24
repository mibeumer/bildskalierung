/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.integrationstest.test;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test für die genierung vieler skalierter Bilder zur manuellen
 * Qualitätsüberprüfung und Sichtung
 */
public class ImgscalrTest {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImgscalrTest.class);

    public static final String PATH_TO_IMAGES = "/home/beumer/images-adb/00/0b/";
    public static final String TARGET_PATH_TO_IMAGES = "/home/beumer/images-adb/scaled/";
    public static final String IMAGE_TYPE = "jpg";

    @Test
    public void scaleImagesFromFileSystem() {

        File dir = new File(PATH_TO_IMAGES);
        File[] fileList = dir.listFiles();
        for (File f : fileList) {
            // LOG.debug(f.getAbsolutePath());
            try {
                scaleFile(f.getAbsolutePath(), 600, 600, f);
            } catch (Exception e) {
                LOG.debug("Total Unerwartete Exception: " + f.getAbsolutePath() + " " + e);
            }
        }

    }

    @Test
    @Ignore
    public void test() {
        int scaledWidth = Integer.parseInt(null);
        System.out.println(scaledWidth);
    }

    public void scaleFile(final String filePath, final Integer scaledWidth, final Integer scaledHeight, final File f) {

        if (filePath == null) {
            throw new IllegalArgumentException("filePath is null");
        }

        BufferedImage bufferedImage = null;
        File resultFile = new File(TARGET_PATH_TO_IMAGES + f.getName());
        try {
            bufferedImage = ImageIO.read(new File(filePath));
            int orgWidth = bufferedImage.getWidth();
            int orgHeight = bufferedImage.getHeight();

            if (scaledWidth != null) {
                if (scaledHeight != null) {
                    // 1.Fall scaledHeight und scaledWidth vorhanden
                    // Schreibt das Orginal Bild wieder raus
                    if ((orgWidth <= scaledWidth) && (orgHeight <= scaledHeight)) {
                        ImageIO.write(bufferedImage, IMAGE_TYPE, resultFile);
                        // Zu kleine Breite und passende Hoehe
                    } else if ((orgWidth <= scaledWidth) && (orgHeight > scaledHeight)) {
                        writeImage(bufferedImage, orgWidth, scaledHeight, resultFile);
                        // Passende Breite und zu kleine Hoehe
                    } else if ((orgWidth > scaledWidth) && (orgHeight <= scaledHeight)) {
                        writeImage(bufferedImage, scaledWidth, orgHeight, resultFile);
                        // Passende Breite und passende Hoehe
                    } else if ((orgWidth >= scaledWidth) && (orgHeight >= scaledHeight)) {
                        writeImage(bufferedImage, scaledWidth, scaledHeight, resultFile);
                        // Doppelt, da alle Möglichkeiten durch die vorigen if
                        // abgeprüft.
                    } else {
                        LOG.debug("Else-if-Error: " + f.getName());
                        writeImage(bufferedImage, scaledWidth, scaledHeight, resultFile);
                    }
                    // 2.Fall Wenn scaledWidth vorhanden und scaledHeight ==
                    // null
                    // Schreibt das Orginal wieder raus
                } else if (orgWidth <= scaledWidth) {
                    ImageIO.write(bufferedImage, IMAGE_TYPE, resultFile);
                    // Passende Breite
                } else if (orgWidth > scaledWidth) {
                    ImageIO.write(
                            Scalr.resize(bufferedImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, scaledWidth),
                            IMAGE_TYPE, resultFile);
                }
                // 3. Fall Wenn scaledWidth == null und scaledHeight == null
                // Schreibt das OrginalBild raus.
            } else {
                ImageIO.write(bufferedImage, IMAGE_TYPE, resultFile);
            }

        } catch (IOException e) {
            LOG.debug("Problem mit dem Bild: " + filePath + " Exception:" + e);
        } finally {
            bufferedImage.flush();
        }
    }

    /**
     * Skaliert das Bild so, dass es max. "width" breit ist und max. "height"
     * hoch ist.
     * 
     * @param bufferedImage
     * @param width
     * @param height
     * @param resultFile
     * @throws IllegalArgumentException
     * @throws ImagingOpException
     * @throws IOException
     */
    private void writeImage(final BufferedImage bufferedImage, final int width, final int height, final File resultFile)
            throws IllegalArgumentException, ImagingOpException, IOException {

        if (width >= height) {
            ImageIO.write(Scalr.resize(bufferedImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, width, height),
                    IMAGE_TYPE, resultFile);
        } else if (width < height) {
            ImageIO.write(Scalr.resize(bufferedImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, width, height),
                    IMAGE_TYPE, resultFile);
        }

    }

}
