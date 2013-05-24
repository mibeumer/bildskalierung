/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.api.rest.server.resource.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.buch.commons.utils.api.rest.server.resource.BaseRestResource;

/**
 * Einfache Admin-Resource um die Verfuegbarkeit der Applikation anzuzeigen.
 * <p>
 * Kann von Loadbalancern fuer eine "Alive-Check" verwendet werden.
 * <p>
 * Die "generelle Verfuegbarkeit" garantiert nicht die Lauffaehigkeit der
 * Applikation. Es wird nicht ueberprueft ob z.B. Datenbankverbindungen
 * existieren oder andere benoetigte Resourcen zur Verfuegung stehen.
 */
@Controller
@RequestMapping(value = "api/rest/admin/ping")
public class AdminClusterPingResource extends BaseRestResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AdminClusterPingResource.class);

    /**
     * Gibt ein "OK" zurueck um anzuzeigen, dass die Applikation laeuft.
     * 
     * @return String <code>ok</code>
     */
    @RequestMapping(method = RequestMethod.GET, produces = "text/plain")
    public final ResponseEntity<String> ping() {
        LOG.debug("Ping request received and answered with 'ok'...");
        return new ResponseEntity<String>("ok", HttpStatus.OK);
    }

}
