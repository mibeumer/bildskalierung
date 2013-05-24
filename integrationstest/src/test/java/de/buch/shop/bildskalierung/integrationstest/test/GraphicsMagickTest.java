/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.integrationstest.test;

import java.io.File;
import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test für die genierung vieler skalierter Bilder zur manuellen
 * Qualitätsüberprüfung und Sichtung
 */
public class GraphicsMagickTest {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GraphicsMagickTest.class);

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
                scaleFile(f.getAbsolutePath(), 30, 150, f);
            } catch (Exception e) {
                LOG.debug("Total Unerwartete Exception: " + f.getAbsolutePath() + " " + e);
            }
        }

    }

    public void scaleFile(final String filePath, final Integer scaledWidth, final Integer scaledHeight, final File f) {

        if (filePath == null) {
            throw new IllegalArgumentException("filePath is null");
        }

        ConvertCmd cmd = new ConvertCmd(true);

        IMOperation op = new IMOperation();
        String absFilePath = f.getAbsolutePath();

        op.addImage(absFilePath); // absFilePath
        op.resize(scaledWidth, scaledHeight, ">");
        op.addImage(TARGET_PATH_TO_IMAGES + f.getName()); // tempPath

        try {
            cmd.run(op);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IM4JavaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
