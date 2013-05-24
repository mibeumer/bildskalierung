<!DOCTYPE html>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page session="false" contentType="text/html; charset=UTF-8"
	pageEncoding="utf8"%>
	
<html>

<head>
	<link rel="shortcut icon"
		href="<c:url value="/resources/pictures/favicon.ico" />">
		
	<title>de.buch.shop.bildskalierung</title>
	
	<link rel="stylesheet"
		href="<c:url value="/resources/css/application.css" />" type="text/css" />
</head>

<body>

	<div id="background">

		<div id="header">
			<div id="header_logo">
				<img src="<c:url value="/resources/pictures/global.logo.png" />" />
			</div>
			<div id="header_ueberschrift">
				<b>de.buch.shop.bildskalierung</b>
			</div>
		</div>

		<div id="breadcrumb">
			<div id="breadcrumb_inhalt">
				<a href="<c:url value="/index.html" />">Startseite</a>

			</div>
		</div>
				
		<div id="content">
			<h2>Inhalt</h2>
			Startseite der Applikation: de.buch.shop.bildskalierung.
		</div>
		
		<div id="footer">
			<a href="<c:url value="/index.html" />">Startseite</a> - Copyright 2012
			bei buch.de internetstores AG
		</div>
	</div>

</body>

</html>
