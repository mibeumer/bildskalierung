/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.backend;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

import org.junit.Ignore;
import org.junit.Test;

/**
 * TODO: Document class JMagickTest.
 */
public class JMagickTest {

    @Test
    @Ignore
    public void testJMagick() {
        System.out.println("Test startet");
        // getFile(filePath);
        int finalWidth = 100;
        int finalHeight = 100;

        /** Typical scaling implementation using JMagick **/
        System.loadLibrary("JMagick");
        // System.setProperty("jmagick.systemclassloader", "no"); // However,
        // this
        // will cause problems if you have more than one webapp using
        // jmagick in the same Tomcat installation.
        ImageInfo origInfo;
        try {
            origInfo = new ImageInfo("/home/beumer/img-adb/34201026-00-00/inferno.jpg");// load
            // image
            // info
            MagickImage image = new MagickImage(origInfo); // load image
            image = image.scaleImage(finalWidth, finalHeight); // to Scale image
            image.setFileName("/home/beumer/img-adb/34201026-00-00/infernotest.jpg"); // give
                                                                                      // new
            // location
            image.writeImage(origInfo); // save
        } catch (MagickException e) {
            e.printStackTrace();
        }
        assert (true);
    }
}
