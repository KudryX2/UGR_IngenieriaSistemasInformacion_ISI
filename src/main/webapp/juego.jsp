<%@ page language="java" contentType="text/html; UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>Ludopedia</title>
		<style><%@include file="/WEB-INF/css/general.css"%></style>	
		<style><%@include file="/WEB-INF/css/juego.css"%></style>				
	</head>

	<body>

		<div id="buscador">
			<form action="/busqueda">
				<h>NOMBRE</h>
				<input type="text" name="nombre">
				<input type="submit" value="Buscar"> <br>
			</form>
		</div>

		<section id = "juego">
			<div id="infoJuego">
			
				<p id="nombreJuego">${nombre}</p>
				
				<div id="contenido">
					<img src="${imagen}">
					<p id="descripcion"> ${descripcion} </p>
				</div>
			</div>
										
			<div id="precios">
			
				<p id="preciosTexto"> PRECIOS ENCONTRADOS </p>
				
				<div class="resultado">
					<div class="izq"><p class="nombreJuego"> Precio Steam: ${precioSteam} </p></div>
					<a class="enlace" href="${urlSteam}" target="_blank" ><p>A LA WEB</p></a>
				</div>
				
				<%
					if(request.getAttribute("precioSteam") != "Free To Play"){
						out.print(	"<div class=\"resultado\">"+
										"<div class=\"izq\"><p class=\"nombreJuego\"> Precio Instant Gaming: "+ request.getAttribute("precioInstant") + "</p></div>"+ 
										"<a class=\"enlace\" href=" + request.getAttribute("urlInstant") + "\" target=\"_blank\"><p>A LA WEB</p></a>"+
									"</div>"
								);

						if(request.getAttribute("precioFnac") != "No encontrado"){
							out.print(
										"<div class=\"resultado\">"+
											"<div class=\"izq\"><p class=\"nombreJuego\"> Precio Fnac: "+ request.getAttribute("precioFnac") + "</p></div>"+ 
											"<a class=\"enlace\" href=" + request.getAttribute("urlFnac") + "\" target=\"_blank\"><p>A LA WEB</p></a>"+
										"</div>"
									);
						}
					}
				%>
				
				
			</div>
					
		</section>
		

	</body>
	
</html>