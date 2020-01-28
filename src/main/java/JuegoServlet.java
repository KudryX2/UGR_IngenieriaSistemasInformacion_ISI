

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Servlet implementation class JuegoServlet
 */
@WebServlet(
	name = "/juego",
	urlPatterns = {"/juego"}
)


public class JuegoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	String steamInfo, nombre;

	String 	precioSteam, precioInstant, precioFnac,	
			urlSteam,    urlInstant,    urlFnac;
	
    public JuegoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				
		// Codificacion
		request.setCharacterEncoding("UTF-8");
		
		// Informacion del juego de steam
		setSteamGameInfo(request.getParameter("id"));
		nombre = request.getParameter("nombre");
		
		
		request.setAttribute("nombre", nombre);
		request.setAttribute("imagen", getImagen());
		request.setAttribute("descripcion", getDescripcion());
		
		// Precios
		setInfoSteam();
		// Url compra steam
		urlSteam = "https://store.steampowered.com/app/" + request.getParameter("id");

		request.setAttribute("precioSteam", precioSteam);
		request.setAttribute("urlSteam", urlSteam);
		
		if(precioSteam != "Free To Play") {

			setInfoInstantGaming();
			request.setAttribute("precioInstant", precioInstant);	
			request.setAttribute("urlInstant", urlInstant);
			
			setInfoFnac();
			request.setAttribute("precioFnac", precioFnac);
			request.setAttribute("urlFnac", urlFnac);
		}
				
		request.getRequestDispatcher("juego.jsp").forward(request, response);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	
	/*
		Recibe como parametro el id de Steam de un juego y accediendo a la api de Steam 
		almacena la informacion obtenida. (JSON)
	 */
	private void setSteamGameInfo(String steamID) throws IOException {
		steamInfo = "";
		
		// Crear peticion
		URL url = new URL("https://store.steampowered.com/api/appdetails?appids="+steamID+"&cc=es&l=spanish");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		
		// Recibir respuesta
		InputStreamReader input = new InputStreamReader(con.getInputStream(),"UTF-8");
		BufferedReader in = new BufferedReader(input);
		
		String inputLine;		
		while ((inputLine = in.readLine()) != null) {
		    steamInfo+=inputLine;
		}
			
		
//		in.close();
		con.disconnect();
	
	}
	
	/*
	 	Mediante la informacion recibida de la api de Steam se halla la url de la 
	 	imagen del juego 
	 */
	private String getImagen() {
		String url = "";
		String clave = "header_image\":";
		int index = steamInfo.indexOf(clave);
			
		index += clave.length();
		while(steamInfo.charAt(index) != ',') {
			url += steamInfo.charAt(index);
			index++;
		}	
		url = url.replace("\"", "");
		
		return url;	
	}
	
	/*
	 	Mediante la informacion recibida de la api de Steam se halla la descripcion del juego 
	*/
	private String getDescripcion() {
		String descripcion = "";
		String clave = "short_description\":";
		int index = steamInfo.indexOf(clave);
		
		index += clave.length();
		index += 1; // Saltarse las comillas
		while(steamInfo.charAt(index) != '"') {
			descripcion += steamInfo.charAt(index);
			index++;
		}	
		
		// Eliminamos elementos de html que hay en la descripcion
		descripcion = Jsoup.parse(descripcion).text();
		descripcion = descripcion.replace("\\r", "");
		descripcion = descripcion.replace("\\n", "");
		descripcion = descripcion.replace("\\u", "");
		descripcion = descripcion.replace("\\/i", "");
		descripcion = descripcion.replace("\\/", "");
		descripcion = descripcion.replace("<", "");		
		descripcion = descripcion.replace(">", "");		
		
		// Sustituimos caracteres especiales y con tildes
		descripcion = descripcion.replace("00a1", ""); // ¡
		descripcion = descripcion.replace("00f3", "ó");
		descripcion = descripcion.replace("00ed", "í");
		descripcion = descripcion.replace("00e9", "é");
		descripcion = descripcion.replace("00e1", "á");
		descripcion = descripcion.replace("00fa", "ú");
		descripcion = descripcion.replace("00f1", "ñ");
		
//		System.out.println(descripcion);
		
		return descripcion;
	}

	// PRECIOS
	
	/*
	 	Se calcula el precio del juego en Steam
	 */
	private void setInfoSteam() {
		
		precioSteam = "";
		urlSteam = "";
		
		String clave = "\"final\":";
		int index = steamInfo.indexOf(clave);
		
		
		if(index == -1) {
			precioSteam = "Free To Play";			
		}else {
			
			// Precio
			index += clave.length();
			while(steamInfo.charAt(index) != ',') {
				precioSteam += steamInfo.charAt(index);
				index++;
			}	
			
			int indexEnt = precioSteam.length()-2;
			precioSteam = precioSteam.substring(0, indexEnt) + "." + precioSteam.substring(indexEnt,precioSteam.length()) + "€";
						
		}
		
		
	}
	
	/*
	 	Se calcula el precio del juego en Instant Gaming 
	 	usando scrapping
	 */
	private void setInfoInstantGaming() {
		
		precioInstant = "";
		urlInstant = "";
	
		Document doc = getHtmlDocument("https://www.instant-gaming.com/es/busquedas/?q=" + nombre);
		
		if(!doc.html().isEmpty()) {
			Elements informacion = doc.getElementsByClass("price");
			
			if(!informacion.html().isEmpty()) {
				// Precio de la primera coincidencia
				precioInstant = informacion.get(0).html();	
		
				// Url de compra
				Elements url = doc.getElementsByClass("cover");
				urlInstant = url.attr("href");	
				
			}else {
				precioInstant = "No encontrado";
			}
		
		}else {
			precioInstant = "Juego no disponible";
		}
		

		
		
	}
	
	/*
	private String getPrecioHumbleBundle() {
		String resultado = "";
		
		try {
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			
			
			HtmlPage page = webClient.getPage("https://www.game.es/buscar/"+ nombre +"/o=0&cf=000a+:GIDSb_aa0173:4b,aa0103:3b");
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.waitForBackgroundJavaScript(2000);
			
		//	List<HtmlAnchor> teams = (List) page.getByXPath("//td[@class='price']");
			
			String control = "PC SOFTWARE";
			String html = page.asText();
			
		//	System.out.println(html);
			
			int index = html.indexOf(control);
			index+=control.length();
			
			while(!Character.isLetter(html.charAt(index))) {
				resultado += html.charAt(index);
				index++;
			}
			
		//	System.out.println("PARAFERNALIA " + teams.get(0).asText());

		}catch (IOException ex ) {
            ex.printStackTrace();
        }	
		
		return resultado;
	
	}
	*/
	
	/*
	 	Se calcula el precio del juego en Fnac 
	 	usando scrapping
	 */
	private void setInfoFnac() {
		precioFnac = "";
		urlFnac = "";
		
		Document doc = getHtmlDocument("https://www.fnac.es/SearchResult/ResultList.aspx?SCat=5!1%2c5001!2%2c5001001!3&Search=" + nombre + "&sft=1&sl"); 
		
		Elements informacion = doc.getElementsByClass("userPrice");	
		
		
		if(!informacion.html().isEmpty()) {
			
			// Precio
			precioFnac = informacion.get(0).html();
				
			precioFnac = precioFnac.replace("<sup>", "");
			precioFnac = precioFnac.replace("</sup>", "");
			precioFnac = precioFnac.replace(",", ".");
			
			
			// Url de compra
			Elements url = doc.getElementsByClass("Article-desc");
			String clave = "<a href=\"";
			int index = url.html().indexOf(clave);
			index += clave.length();
			
			while(url.html().charAt(index) != '"') {
				urlFnac += url.html().charAt(index);
				index++;
			}
			
		}else {
			precioFnac = "No encontrado";
		}

	}
	
	/*
	 JSOUP 
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
	
	public static int getStatusConnectionCode(String url) {
		
	    Response response = null;
		
	    try {
		response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
	    } catch (IOException ex) {
		System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
	    }
	    return response.statusCode();
	}
	
	
}
