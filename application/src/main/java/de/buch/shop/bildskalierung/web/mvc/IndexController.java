/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * REST-Controller der Index-Seite der Applikation.
 */
@Controller
@RequestMapping("/index.html")
public class IndexController {

	/**
	 * Gibt die index Seite zurueck.
	 * 
	 * @return Die ID der Seite, die angezeigt werden soll.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getIndex() {
	    // Wird im servletContext.xml zu "WEB-INF/views/index.jsp" erweitert.
		return "index";
	}

}
