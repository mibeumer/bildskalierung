/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.backend;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.gm4java.engine.GMConnection;
import org.gm4java.engine.GMException;
import org.gm4java.engine.GMService;
import org.gm4java.engine.GMServiceException;
import org.gm4java.engine.support.GMConnectionPoolConfig;
import org.gm4java.engine.support.PooledGMService;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Document class JMagickTest.
 */
public class Gm4JavaTest {

    private static final Logger LOG = LoggerFactory.getLogger(Gm4JavaTest.class);

    @Test
    @Ignore
    public void testGm4Java() throws GMException, GMServiceException, IOException {
        GMConnectionPoolConfig config = new GMConnectionPoolConfig();
        GMService service = new PooledGMService(config);

        final GMConnection connection = service.getConnection();

        try {
            String actualPath = new File(".").getCanonicalPath();
            LOG.debug(actualPath);
            String result = connection.execute("identify", "/home/beumer/123.jpg");
            String result2 = connection
                    .execute("convert -size 120x120 /home/beumer/123.jpg -resize 120x120 +profile \"*\" /tmp/thumbnail.jpg");

            LOG.debug(result);
            LOG.debug(result2);
        } finally {

            connection.close();

        }

        Assert.assertTrue(true);
    }
}
