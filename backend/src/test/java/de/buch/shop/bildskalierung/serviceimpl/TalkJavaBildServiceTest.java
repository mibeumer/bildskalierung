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
import de.buch.shop.bildskalierung.backend.serviceimpl.TalkJavaBildServiceImpl;

/**
 * Tests für den Service TalkJavaBildServiceImpl.
 */
public class TalkJavaBildServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(TalkJavaBildServiceTest.class);

    @Test
    public void testScaleFile() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        TalkJavaBildServiceImpl impl = new TalkJavaBildServiceImpl();
        ConfigBean c = new ConfigBean();
        c.setImagePath("src/test/resources/");
        ReflectionTestUtils.setField(impl, "configBean", c);
        impl.scaleFile("test.jpg", 75, 150, bos);
        LOG.debug("Size des Bildes: " + bos.size());

        Assert.assertTrue(bos.size() == 3634);
    }
}
