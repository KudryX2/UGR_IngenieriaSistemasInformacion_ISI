<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="ISO-8859-1">
		<title>Insert title here</title>
		<%@ page import="org.jsoup.Jsoup" %> 
		<%@ page import="org.jsoup.Connection.Response" %> 
		<%@ page import="org.jsoup.nodes.Document" %> 
		<%@ page import="org.jsoup.nodes.Element" %> 
		<%@ page import="org.jsoup.select.Elements" %>
		<style><%@include file="/WEB-INF/css/destacados.css"%></style>
	</head>

	<body>
		
		<section>
							
		<%
			/*
				Obtenemos el html de la web de Steam del apartado destacados
			*/	
			Document doc = Jsoup.connect("https://store.steampowered.com/?l=latam").userAgent("Mozilla/5.0").timeout(100000).get(); 

			Element dest = doc.getElementById("tab_newreleases_content");
			Elements elements = dest.select("a[href]");
			
			/*
				Para cada elemento de la lista obtenida obtenemos el id de Steam, el nombre, la imagen y 
				calculamos la url para comparar los precios del elemento
				
				Omitimos el ultimo elemento ya que no es un juego sino que un enlace a otra pagina.
			*/ 
			for(Element link : elements.subList(0, elements.size()-1)){
				
				String info = link.html();
				
				
				// Nombre
				String nombre = "";
				String clave = "tab_item_name\">";
				int index = info.indexOf(clave);
				index+=clave.length();
				
				while(info.charAt(index) != '<'){
					nombre += info.charAt(index);
					index++;
				}
			
					
				// SteamId
				String steamId = "";
				clave = "https://steamcdn-a.akamaihd.net/steam/apps/";
				index = info.indexOf(clave);
				index += clave.length();
				
				while(info.charAt(index) != '/'){
					steamId += info.charAt(index);
					index++;
				}
				
				// Imagen
				String imgURL = " ";
				
				index++;
				while(info.charAt(index) != '"'){
					imgURL += info.charAt(index);
					index++;
				}
				imgURL = clave + steamId + "/" + imgURL;
				imgURL = imgURL.replace(" ", "");
				
				
			
				/*
					Imprimimos en html cada elemento procesado de la lista
				*/
				out.print(
						"<div class=\"juego\">"+
								"<div class=\"izq\">"+
									"<div>"+
										"<img class=\"imagen\" src=" + imgURL + "</img>"+
										"<h1>" + nombre + "</h1> " + 
									"</div>"+
								"</div>"+
								
								"<div class=\"der\">"+
									"<div>"+
										"<a href=\"juego?id=" + steamId + "&nombre="+ nombre +"\"<h1>COMPARAR</h1></a>"+
									"</div>"+
								"</div>"+
									
							"</div>"
						);
				
			}
			
	
		%>
		</section>
		
	</body>

</html>