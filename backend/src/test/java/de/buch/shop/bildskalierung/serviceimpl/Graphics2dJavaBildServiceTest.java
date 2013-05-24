/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.serviceimpl;

import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import de.buch.shop.bildskalierung.backend.config.ConfigBean;
import de.buch.shop.bildskalierung.backend.serviceimpl.Graphics2dJavaBildServiceImpl;

/**
 * Tests für den Service Graphics2dJavaBildServiceImpl.
 */
public class Graphics2dJavaBildServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(Graphics2dJavaBildServiceTest.class);

    @Test
    public void testScaleFile() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        Graphics2dJavaBildServiceImpl impl = new Graphics2dJavaBildServiceImpl();
        ConfigBean c = new ConfigBean();
        c.setImagePath("src/test/resources/");
        ReflectionTestUtils.setField(impl, "configBean", c);
        impl.scaleFile("test.jpg", 75, 150, bos);
        LOG.debug("Size des Bildes: " + bos.size());

        Assert.assertTrue(bos.size() == 2378);
    }
}
