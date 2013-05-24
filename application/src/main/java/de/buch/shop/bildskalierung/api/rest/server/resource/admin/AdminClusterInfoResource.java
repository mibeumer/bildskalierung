/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.api.rest.server.resource.admin;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.buch.commons.utils.api.rest.server.resource.BaseRestResource;
import de.buch.commons.utils.api.rest.server.util.ClusterInformation;

/**
 * Admin-Resource um den Status der Applikation abzufragen. Gibt diverse Daten
 * ueber die Applikation zurueck, z.B. die Build-Nummer und den Hostnamen des
 * Servers, auf dem die Tomcat-Instanz laeuft.
 */
@Controller
@RequestMapping(value = "api/rest/admin/cluster")
public class AdminClusterInfoResource extends BaseRestResource {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AdminClusterInfoResource.class);

    /**
     * Der Datencontainer der z.B. die Build-Nummer enthaelt.
     */
    @Inject
    private ClusterInformation clusterInformation;

    /**
     * Gibt Server- und Applikationsinformationen zurueck.
     * 
     * @return Eine ClusterInformation Instanz mit Serverdaten.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<ClusterInformation> getInformation() {
        LOG.info("ClusterInformation requested: " + clusterInformation);
        return new ResponseEntity<ClusterInformation>(clusterInformation, HttpStatus.OK);
    }

}
