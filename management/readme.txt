# README-Datei der Applikation: de.buch.shop.bildskalierung
#
# In dieser Datei werden die notwendigen Konfigurationen beschrieben, damit
# die Applikation auf einem lokalen Entwicklerrechner gestartet werden kann.
#
# Ziel ist es, die Schritte zu dokumentieren, die nach dem Auschecken mit GIT 
# noch notwendig sind, bis die Applikation im lokalen Tomcat startet.

# Bauen der Applikation

Die Applikation wird über maven mit 

  mvn clean install
  
gebaut. Es sind keine weiteren Parameter / Schritte erforderlich.

# Starten der Applikation

Folgender Parameter muss als JVM-Parameter an den Tomcat übergeben werden, um
die Applikation zu starten:

  -Dde.buch.shop.bildskalierung.config.path=file://localhost/...<ABSOLUTER_PFAD_ZUM_PROJEKT>.../management/config
  

