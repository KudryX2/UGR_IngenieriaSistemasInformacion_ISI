import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Servlet implementation class Busqueda
 */
@WebServlet(
	name = "/busqueda",
	urlPatterns = {"/busqueda"}
)


public class BusquedaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BusquedaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
				
		// Buscamos el resultado de busqueda en Steam por el nombre proporcionado
		String html = getSteamResults(request.getParameter("nombre")).html();
		
		// Nos quedamos con la lista de los juegos encontrados y eliminamos el resto del html para evitar errores
		int index = html.indexOf("<!-- End List Items --> ");
		if(index != -1)
			html = html.substring(0, index);
					
		/*
			Recuperamos la informacion sobre los juegos del html conseguido y la almacenamos gracias a una clase Juegos
		  	y un contenedor
		 */
		ArrayList<Juego> juegos = getJuegos(html);
		
		// Almacenamos un string resultado en html con los juegos encontrados 
		String resultadoHTML = ""; 
		for(Juego juego : juegos) {
			resultadoHTML += juego.html();
		}		
		
		request.setAttribute("info", resultadoHTML);
		
		// Redirregimos
		request.getRequestDispatcher("busqueda.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/*
		Recibe por parametro el nombre del juego a buscar y deuelve un elemento que contine el html de resultados
		de busqueda en Steam
	*/
	Element getSteamResults(String nombre) {
		
	//	System.out.println("CODE: " + getStatusConnectionCode("https://store.steampowered.com/search/?term=" + nombre + "/"));
		Document doc = getHtmlDocument("https://store.steampowered.com/search/?term=" + nombre + "/"); 
		Element informacion = doc.getElementById("search_result_container");
			
		return informacion;
	}
	
	
	/* 
		Almacena los juegos encontrados como objetos de la clase Juego 
		recibiendo como parametro el html de resultado de busqueda en Steam
	*/
	ArrayList<Juego> getJuegos(String html){
		ArrayList<Juego> juegos= new ArrayList<Juego>();

		String stringClave = "<a href=";
		String htmlJuego;
		
		Boolean seguir = true;
		int cantidadMax = 50; // Cantidad maxima de juegos a almacenar
		
		// Separa los juegos gracias a la etiqueta propia de un enlace 
		while(seguir && juegos.size() < cantidadMax) {
		
			int beginIndex = html.indexOf(stringClave);
			int endIndex = html.indexOf(stringClave, beginIndex+stringClave.length());
			
			if(beginIndex == -1 || endIndex == -1) {
				seguir = false;
			
			}else {
				// htmlJuego informacion contenida en el enlace del juego  
				htmlJuego = html.substring(beginIndex, endIndex);
				
				// Solo se incluyen juegos, en caso de que sea un pack se omite
				if(htmlJuego.indexOf("data-ds-bundleid=") == -1)
					juegos.add(new Juego(htmlJuego));
				
				html = html.replace(htmlJuego, "");
			}
		}
		
		return juegos;
	}
	
	
	/**
	 * Con este método compruebo el Status code de la respuesta que recibo al hacer la petición
	 * EJM:
	 * 		200 OK			300 Multiple Choices
	 * 		301 Moved Permanently	305 Use Proxy
	 * 		400 Bad Request		403 Forbidden
	 * 		404 Not Found		500 Internal Server Error
	 * 		502 Bad Gateway		503 Service Unavailable
	 * @param url
	 * @return Status Code
	 */
	public static int getStatusConnectionCode(String url) {
			
	    Response response = null;
		
	    try {
		response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
	    } catch (IOException ex) {
		System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
	    }
	    return response.statusCode();
	}
	
	/**
	 * Con este método devuelvo un objeto de la clase Document con el contenido del
	 * HTML de la web que me permitirá parsearlo con los métodos de la librelia JSoup
	 * @param url
	 * @return Documento con el HTML
	 */
	public static Document getHtmlDocument(String url) {

	    Document doc = null;
		try {
		    doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		    } catch (IOException ex) {
			System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
		    }
	    return doc;
	}
	
}
