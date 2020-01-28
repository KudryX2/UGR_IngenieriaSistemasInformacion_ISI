public class Juego {
	
	private String nombre, urlImagen, steamID;
	
	
	public Juego(String html) {	
		setNombre(html);
		setSteamID(html);
		setUrlImagen(html);
	}
	
	/*
		Devuelve en un string el html a mostrar para representar el juego en la lista de juegos encontrados en la busqueda
	 */
	public String html() {
		
		/* */
		return "<div class=\"juego\">"+
					"<div class=\"izq\">"+
						"<div>"+
							"<img class=\"imagen\" src=" + urlImagen + "</img>"+
							"<h1>" + nombre + "</h1> " + 
						"</div>"+
					"</div>"+
					
					"<div class=\"der\">"+
						"<div>"+
							"<a href=\"juego?id=" + steamID + "&nombre="+ nombre +"\"<h1>COMPARAR</h1></a>"+
						"</div>"+
					"</div>"+
						
				"</div>";
		/* */
	}
	
	
	/*
		Recibiendo el html del enlace del juego halla el nombre del juego 
	*/
	private void setNombre(String cadena) {
		
		nombre = "";
		String claveNombre = "<span class=\"title\">";
		int index = cadena.indexOf(claveNombre);
		index += claveNombre.length();
		
		while(cadena.charAt(index) != '<') {
			if(Character.isLetter(cadena.charAt(index)) || cadena.charAt(index) == ' ')
				nombre += cadena.charAt(index);
			index++;
		}
			
	}
	
	/*
		Recibiendo el html del enlace del juego halla el id de Steam del juego 
	*/
	private void setSteamID(String cadena) {
				
		steamID = "";
		String claveSteamID = "data-ds-appid=\"";
		int index = cadena.indexOf(claveSteamID);
		index += claveSteamID.length();
		
		while(cadena.charAt(index) != '"') {
			steamID += cadena.charAt(index);
			index++;
		}
	}
	
	/*
		Recibiendo el html del enlace del juego halla el enlace de la imagen del juego 
	*/
	private void setUrlImagen(String cadena) {
		
		urlImagen = "";
		String claveUrl = "<img src=\"";
		int index = cadena.indexOf(claveUrl);
		index += claveUrl.length();
				
		while(cadena.charAt(index) != '"') {
			urlImagen += cadena.charAt(index);
			index++;
		}
			
	}
	
}
