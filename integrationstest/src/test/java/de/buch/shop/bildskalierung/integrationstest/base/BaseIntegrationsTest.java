package de.buch.shop.bildskalierung.integrationstest.base;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Basisklasse für die Integrationstests.
 * 
 * @author beumer
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })
public class BaseIntegrationsTest {

    public static final String LOCALHOST_URL = "http://localhost:8080/de.buch.pim.standardschnittstelle.application-1.0-SNAPSHOT/";

    public static final String DEVELOPMENT_URL = "http://vm-pimimp1e.buch.de:8080/de.buch.pim.standardschnittstelle.application/";

    @Autowired
    protected RestTemplate restTemplate;

}
