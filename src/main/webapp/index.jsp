<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	
	<head>
		<meta charset="ISO-8859-1">
		<title>Ludopedia: Game Comparator</title></head>
		<style><%@include file="/WEB-INF/css/index.css"%></style>	
		<style><%@include file="/WEB-INF/css/general.css"%></style>	
	<head>
		  
	
	<body>
	
	   <div id="buscador">
			<form action="/busqueda">
			
				<h>NOMBRE</h>
				<input type="text" name="nombre">
				<input type="submit" value="Buscar"> <br>
				
			</form>

		</div>


		<div id="destacados">
		
			<jsp:include page="/destacados.jsp"/>
		
		</div>
	
	
	</body>

</html>

