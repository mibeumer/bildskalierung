package de.buch.shop.bildskalierung.integrationstest.base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;

/**
 * Utility Klasse für die Integrationstests.
 * 
 * @author beumer
 * 
 */
public class BaseUtil {

    private static final Logger LOGGER = Logger.getLogger(BaseUtil.class);

    private BaseUtil() {
        // static
    }

    /**
     * 
     * @param username
     *            Username zur Authentifizierung an der Basic Auth
     * @param password
     *            Passwort zur Authentifizierung an der Basic Auth
     * @return
     */
    public static HttpHeaders createHeaders(final String username, final String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

    /**
     * Vergleich die zurückbekommene View zeilenweise mit der testView
     * "testNeu.html".
     * 
     * Zeile 18 wird ignoriert da diese die JSESSION beinhaltet.
     * 
     * @param fileName
     *            Name der Test Datei
     * @param viewString
     *            Zurückbekommene View
     * @param zeile
     *            Zeile die ignoriert werden soll.
     * @return {@link Boolean} valide oder nicht?
     */
    public static boolean validateGegenTestView(final String fileName, final String viewString, final int zeile) {
        BufferedReader rd = null;
        try {
            rd = new BufferedReader(new FileReader("src/test/resources/" + fileName));
            String line = null;
            int i = 0;
            String[] array = viewString.split("\r\n");
            while ((line = rd.readLine()) != null) {
                i++;
                if ((i != zeile) && (i != 18)) { // da in Zeile 19 die
                                                 // Errormeldung
                                                 // jedesmal eine andere sein
                                                 // kann.
                    if (line.equals(array[i - 1].toString())) {
                        // LOGGER.debug(i + " " + line);
                    } else {
                        LOGGER.debug(i + " " + line);
                        rd.close();
                        return false;
                    }
                }
            }
            rd.close();
            return true;
        } catch (IOException ex) {
            try {
                rd.close();
            } catch (IOException e) {
                Assert.fail("Unexpected Exception");
            }
            LOGGER.info(ex);
        }
        return false;
    }
}
