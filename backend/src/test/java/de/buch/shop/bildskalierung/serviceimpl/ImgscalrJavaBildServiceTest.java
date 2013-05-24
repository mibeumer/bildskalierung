/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.serviceimpl;

import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import de.buch.shop.bildskalierung.backend.config.ConfigBean;
import de.buch.shop.bildskalierung.backend.service.BildService;
import de.buch.shop.bildskalierung.backend.serviceimpl.ImgscalrJavaBildServiceImpl;

/**
 * Tests für den Service ImgscalrJavaBildServiceImpl.
 */
public class ImgscalrJavaBildServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(ImgscalrJavaBildServiceTest.class);

    @Test
    public void testScaleFile() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        BildService impl = new ImgscalrJavaBildServiceImpl();
        ConfigBean c = new ConfigBean();
        c.setImagePath("src/test/resources/");
        ReflectionTestUtils.setField(impl, "configBean", c);
        impl.scaleFile("test.jpg", 75, 150, bos);
        LOG.debug("Size des Bildes: " + bos.toByteArray().length);

        Assert.assertTrue(bos.size() == 2298);
    }
}
