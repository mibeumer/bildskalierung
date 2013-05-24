package de.buch.shop.bildskalierung.integrationstest.test;

import junit.framework.Assert;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author beumer
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:spring-security2.xml" })
public class BildskalierungTest {

    private static final int anzahlDurchlaufe = 1;

    private static final String CURRENTSYSTEM = "http://localhost:8080/de.buch.pim.standardschnittstelle.application-1.0-SNAPSHOT/xml";

    private static final Logger LOGGER = Logger.getLogger(BildskalierungTest.class);

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void createXmlUpdateErfolgreichRestTest() {
        for (int i = 0; i < anzahlDurchlaufe; i++) {
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

            parameters.add("datei", new FileSystemResource("src/test/resources/buch.xml"));

            HttpHeaders headers = createHeaders("lesen", "Buch123");
            headers.set("Content-Type", "multipart/form-data");
            headers.set("Accept", "text/plain");

            ResponseEntity<String> result = restTemplate.exchange(CURRENTSYSTEM, HttpMethod.POST,
                    new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
            LOGGER.debug("HTTP Statuscode: " + result.getStatusCode() + " Inhalt: " + result.getBody());
            Assert.assertTrue(result.getStatusCode() == HttpStatus.CREATED);
            Assert.assertTrue(
                    "Der POST der XmlResource schlägt fehl",
                    result.getBody().equals(
                            "Die Datei wurde übertragen und das Lieferantenupdate wurde erfolgreich durchgeführt!"));
        }
    }

    /**
     * HttpClientErrorException wird bei einer HTTP-Fehlermeldung(5xx, 4xx)
     * geworfen
     */
    @Test(expected = HttpClientErrorException.class)
    public void createXmlUpdateUngueltigeXmlRestTest() {
        for (int i = 0; i < anzahlDurchlaufe; i++) {
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

            parameters.add("datei", new FileSystemResource("src/test/resources/properties.xml"));

            HttpHeaders headers = createHeaders("lesen", "Buch123");
            headers.set("Content-Type", "multipart/form-data");
            headers.set("Accept", "text/plain");

            restTemplate.exchange(CURRENTSYSTEM, HttpMethod.POST, new HttpEntity<MultiValueMap<String, Object>>(
                    parameters, headers), String.class);

            // LOGGER.debug("HTTP Statuscode: " + result.getStatusCode() +
            // " Inhalt: " + result.getBody());
            // Assert.assertTrue("Der POST der XmlResource schlägt fehl",result.getBody().equals("Es wurde keine oder eine falsche XML-Datei übermittelt"));
        }
    }

    /**
     * HttpClientErrorException wird bei einer HTTP-Fehlermeldung(5xx, 4xx)
     * geworfen
     */
    @Test(expected = HttpClientErrorException.class)
    public void createXmlUpdateKeineDateiUebertragenRestTest() {
        for (int i = 0; i < anzahlDurchlaufe; i++) {
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

            parameters.add("datei", "");

            HttpHeaders headers = createHeaders("lesen", "Buch123");
            headers.set("Content-Type", "multipart/form-data");
            headers.set("Accept", "text/plain");

            restTemplate.exchange(CURRENTSYSTEM, HttpMethod.POST, new HttpEntity<MultiValueMap<String, Object>>(
                    parameters, headers), String.class);
        }
    }

    /**
     * 
     * @param username
     *            Username zur Authentifizierung an der Basic Auth
     * @param password
     *            Passwort zur Authentifizierung an der Basic Auth
     * @return
     */
    HttpHeaders createHeaders(final String username, final String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }
}
