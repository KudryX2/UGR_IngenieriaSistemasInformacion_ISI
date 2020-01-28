<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
	
	
	<head>
		<meta charset="utf-8">
		<title>Busqueda</title>
		<style><%@include file="/WEB-INF/css/busqueda.css"%></style>
		<style><%@include file="/WEB-INF/css/general.css"%></style>					
	</head>
	
	
	<body>
	
		 <div id="buscador">
			<form action="/busqueda">
			
				<h>NOMBRE</h>
				<input type="text" name="nombre">
				<input type="submit" value="Buscar"> <br>
			
			</form>
		</div>
		
	
		<h1 id="resultadosHeader">RESULTADOS DE LA BUSQUEDA</h1>
			
		<section>${info}</section>	
		
	</body>

</html>