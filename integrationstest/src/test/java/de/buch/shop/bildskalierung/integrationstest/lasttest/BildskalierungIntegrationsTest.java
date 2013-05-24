package de.buch.shop.bildskalierung.integrationstest.lasttest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.buch.shop.bildskalierung.integrationstest.base.BaseIntegrationsTest;
import de.buch.shop.bildskalierung.integrationstest.base.BaseUtil;

/**
 * Integrationstestklasse für den UploadController.
 * 
 * @author beumer
 * 
 */
public class BildskalierungIntegrationsTest extends BaseIntegrationsTest {

    private static final int NUM_REQUESTS = 3;

    private static final Logger LOGGER = Logger.getLogger(BildskalierungIntegrationsTest.class);

    private final static String CONTROLLER_NAME = "xml";

    private final static String CURRENTSYSTEM = DEVELOPMENT_URL + CONTROLLER_NAME; // Ändere
                                                                                   // die
                                                                                   // Test-Umgebung

    /**
     * Generiert eine fixe Nummer an POST-Request gegen den REST-Service des
     * UploadController
     * 
     */
    @Test
    public void testPerformancePost() throws InterruptedException, ExecutionException {

        LOGGER.info("------------------------------------------------------------------");
        LOGGER.info("Test wird mit " + NUM_REQUESTS + " Request gestartet.");
        LOGGER.info("Zielsystem inst: " + CURRENTSYSTEM);

        // Füllt das "form" aus
        final MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("lieferant", "beumer");
        parameters.add("artDesUploads", "XMLDATEI");
        parameters.add("produkttyp", "");
        parameters.add("artDesContent", "");
        FileSystemResource fileResource = new FileSystemResource("src/test/resources/BigXml.xml");
        final long fileSize = fileResource.getFile().getTotalSpace();
        parameters.add("datei", fileResource);

        // Setze zusätzlich Optionen im Header
        final HttpHeaders headers = BaseUtil.createHeaders("beumer", "beumer");
        headers.set("Content-Type", "multipart/form-data");
        headers.set("Accept", "text/plain");

        final long timeBeforeTest = System.currentTimeMillis();

        final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        for (int i = 0; i < NUM_REQUESTS; i++) {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    LOGGER.info("Post-Abfrage der Website. Die Dateigröße beträgt: " + fileSize + " Byte");
                    restTemplate.exchange(CURRENTSYSTEM, HttpMethod.POST,
                            new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
                }
            };
            executorService.execute(runnable);
        }
        executorService.shutdown();

        while (!executorService.isTerminated()) {
        }

        final long timeAfterTest = System.currentTimeMillis();

        final long totalExecutionTime = timeAfterTest - timeBeforeTest;
        final double timePerTestInMillis = (((double) totalExecutionTime) / NUM_REQUESTS);

        LOGGER.info(String.format("Komplette Zeit in Millisekunden: " + totalExecutionTime));
        LOGGER.info(String.format("Durchschnittliche Zeit in Millisekunden: " + timePerTestInMillis));
        LOGGER.info("------------------------------------------------------------------");
    }

}
