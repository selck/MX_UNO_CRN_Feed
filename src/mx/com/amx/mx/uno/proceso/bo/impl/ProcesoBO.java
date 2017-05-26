package mx.com.amx.mx.uno.proceso.bo.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import mx.com.amx.mx.uno.proceso.bo.IProcesoBO;
import mx.com.amx.mx.uno.proceso.dto.Categorias;
import mx.com.amx.mx.uno.proceso.dto.NoticiaExtraRSSDTO;
import mx.com.amx.mx.uno.proceso.dto.Noticias;
import mx.com.amx.mx.uno.proceso.dto.ParametrosDTO;
import mx.com.amx.mx.uno.proceso.dto.Secciones;
import mx.com.amx.mx.uno.proceso.utils.Feed;
import mx.com.amx.mx.uno.proceso.utils.ObtenerProperties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ProcesoBO implements IProcesoBO {
	
	public static String getDateZoneTime(String fechaString){
		String fecha="";
		try {
			//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm a");
	        Date date = formatter.parse(fechaString);
            
            TimeZone tz = TimeZone.getTimeZone("CST");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			df.setTimeZone(tz);
			
			fecha=df.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return fechaString;
		}
		return fecha;
	}
	
	public static void main(String [] args){
		String f="10/23/15 04:20 PM";
		//String g="10/23/15 09:57 PM";
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm a");
			/*Date date = format.parse(g);
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
			System.out.println(timeStampDate.toString()); */
			Date date = format.parse(f);
			//System.out.println(date); 
			Date ahora = new Date();
			System.out.println(getDateZoneTime(format.format(ahora)));
			//System.out.println(sdf.format(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private final Logger LOG = Logger.getLogger(this.getClass().getName());

	@Value( "${ambiente}" )
	String ambiente = "";	
	
	private  String URL_DAO = "";
	private final Properties props = new Properties();
	
	private RestTemplate restTemplate;
	HttpHeaders headers = new HttpHeaders();
	
	public ProcesoBO() {
		super();
		restTemplate = new RestTemplate();
		ClientHttpRequestFactory factory = restTemplate.getRequestFactory();
		
		if ( factory instanceof SimpleClientHttpRequestFactory) {
			((SimpleClientHttpRequestFactory) factory).setConnectTimeout( 35 * 1000 );
			((SimpleClientHttpRequestFactory) factory).setReadTimeout( 35 * 1000 );
			//LOG.info("Inicializando rest template");
		
		} else if ( factory instanceof HttpComponentsClientHttpRequestFactory) {
			((HttpComponentsClientHttpRequestFactory) factory).setReadTimeout( 35 * 1000);
			((HttpComponentsClientHttpRequestFactory) factory).setConnectTimeout( 35 * 1000);
			//LOG.info("Inicializando rest template");
		}
		
		restTemplate.setRequestFactory( factory );
		headers.setContentType(MediaType.APPLICATION_JSON);
	      
		try {
			props.load( this.getClass().getResourceAsStream( "/general.properties" ) );						
		} catch(Exception e) {
			LOG.error("[ConsumeWS::init]Error al iniciar y cargar arhivo de propiedades." + e.getMessage());
		}
		ambiente = props.getProperty("ambiente");
		URL_DAO = props.getProperty(ambiente+".ws.url.api.servicios");
		
	}
	
	public void procesoAutomatico() {
		LOG.info("************INI: GENERAR FEED *************");
		Feed getInfo = new Feed();
		getInfo.writeNewsML();
		if(ambiente!=null && ambiente.equalsIgnoreCase("desarrollo")){
			ObtenerProperties pro=new ObtenerProperties();
			ParametrosDTO parametros= pro.obtenerPropiedades();
			getInfo.transfiereWebServer(parametros.getPathShell(), parametros.getRutaCarpeta(), parametros.getRutaDestino());
		}
		LOG.info("************FIN: GENERAR FEED *************");
	}
	
	/**
	 * Este metodo trae una lista con las noticias del idMagazine que s especifique en el parametro
	 * @author: Jesús Vicuña
	 * @version: 10/mayo/2016
	 * @param idMagazine El parámetro idMagazine es el id del magazine del cual se quieren obtener las noticias.
	 */
	public Noticias consultarNoticiasMagazine( String idMagazine ) throws Exception {
		Noticias noticias = new Noticias();
		try{
			String lstMETODO = "/consultarNoticiasMagazine";
			String lstURL_WS = URL_DAO + lstMETODO;
						
			HttpEntity<String> entity = new HttpEntity<String>( idMagazine, headers);
			noticias = restTemplate.postForObject(lstURL_WS , entity, Noticias.class);
		}catch(Exception e){
			LOG.error("Error - Msg : " + e.getMessage() );
			e.printStackTrace();
			noticias.setNoticiasLst(null);
		}
		return noticias;		
	}

	/**
	 * Metodo que obtiene las noticias según la categoria.
	 * @param String idCategoria
	 * */
	public Noticias consultarNoticias( String idCategoria ) throws Exception {
		Noticias noticias = new Noticias();
		try{
			String lstMETODO = "/consultarNoticias";
			String lstURL_WS = URL_DAO + lstMETODO;
			HttpEntity<String> entity = new HttpEntity<String>( idCategoria, headers);
			noticias = restTemplate.postForObject(lstURL_WS , entity, Noticias.class);
		}catch(Exception e){
			LOG.error("Error - Msg : " + e.getMessage() );
			e.printStackTrace();
			noticias.setNoticiasLst(null);
		}
		return noticias;		
	}
	
	/**
	 * Metodo que obtiene las noticias según la sección.
	 * @param String idSeccion
	 * */
	public Noticias consultarUltimasPorSeccion( String idSeccion) throws Exception {
		Noticias noticias = new Noticias();
		try{
			String lstMETODO = "/consultarUltimasPorSeccion";
			String lstURL_WS = URL_DAO + lstMETODO;
			
			HttpEntity<String> entity = new HttpEntity<String>( idSeccion, headers);
			noticias = restTemplate.postForObject(lstURL_WS , entity, Noticias.class);
		}catch(Exception e){
			LOG.error("Error - Msg : " + e.getMessage() );
			e.printStackTrace();
			noticias.setNoticiasLst(null);
		}
		return noticias;		
	}
	
	/**
	 * Metodo que obtiene las noticias según el tipo de sección.
	 * @param String idSeccion
	 * */
	public Noticias consultarUltimasPorTipoSeccion( String idSeccion ) throws Exception {
		Noticias noticias = new Noticias();
		try{
			String lstMETODO = "/consultarUltimasPorTipoSeccion";
			String lstURL_WS = URL_DAO + lstMETODO;
			
			HttpEntity<String> entity = new HttpEntity<String>( idSeccion, headers);
			noticias = restTemplate.postForObject(lstURL_WS , entity, Noticias.class);
		}catch(Exception e){
			LOG.error("Error - Msg : " + e.getMessage() );
			e.printStackTrace();
			noticias.setNoticiasLst(null);
		}
		return noticias;		
	}
	
	/**
	 * Metodo que obtiene las categorias.
	 * */
	public Categorias getCategorias( ) throws Exception {
		Categorias categorias = new Categorias();
		
		try{
			
			String lstMETODO = "/getCategorias";
			String lstURL_WS = URL_DAO + lstMETODO;
			HttpEntity<Integer> entity = new HttpEntity<Integer>( headers);
			categorias = restTemplate.postForObject(lstURL_WS , entity, Categorias.class);
			
		}catch(Exception e){
			LOG.error("Error - Msg : " + e.getMessage() );
			e.printStackTrace();
			categorias.setCategotiasLst(null);
		}
		return categorias;		
	}
	
	/**
	 * Metodo que obtiene las secciones.
	 * */
	public Secciones getSecciones( ) throws Exception {
		Secciones secciones = new Secciones();
		try{
			String lstMETODO = "/getSecciones";
			String lstURL_WS = URL_DAO + lstMETODO;
			
			HttpEntity<Integer> entity = new HttpEntity<Integer>( headers);
			secciones = restTemplate.postForObject(lstURL_WS , entity, Secciones.class);
		}catch(Exception e){
			LOG.error("Error - Msg : " + e.getMessage() );
			e.printStackTrace();
			secciones.setSeccionesLst(null);
		}
		return secciones;		
	}
	
	/**
	 * Metodo que obtiene los tipos secciones.
	 * */
	public Secciones getTipoSecciones( ) throws Exception {
		Secciones secciones = new Secciones();
		try{
			String lstMETODO = "/getTipoSecciones";
			String lstURL_WS = URL_DAO + lstMETODO;
						
			HttpEntity<Integer> entity = new HttpEntity<Integer>( headers);
			secciones = restTemplate.postForObject(lstURL_WS , entity, Secciones.class);
						
		}catch(Exception e){
			LOG.error("Error - Msg : " + e.getMessage() );
			e.printStackTrace();
			secciones.setSeccionesLst(null);
		}
		return secciones;		
	}
	public List<NoticiaExtraRSSDTO> consultarNotasExtraByCategoria( String idCategoria, String fecha ) throws Exception {
		try{
			String lstMETODO = "/consultarNotasExtraByCategoria";
			String lstURL_WS = URL_DAO + lstMETODO;
			
			MultiValueMap<String, Object> parts;
			parts = new LinkedMultiValueMap<String, Object>();
			parts.add("idCategoria", idCategoria);
			parts.add("fecha", fecha);
			return Arrays.asList(restTemplate.postForObject(lstURL_WS, parts, NoticiaExtraRSSDTO[].class));
		}catch(Exception e){
			LOG.error("Error - Msg : " + e.getMessage() );
			return Collections.emptyList();
		}
		
	}
	public List<NoticiaExtraRSSDTO> consultarNotasExtraByTipoSeccion( String idTipoSeccion, String fecha ) throws Exception {
		try{
			String lstMETODO = "/consultarNotasExtraByTipoSeccion";
			String lstURL_WS = URL_DAO + lstMETODO;
			
			MultiValueMap<String, Object> parts;
			parts = new LinkedMultiValueMap<String, Object>();
			parts.add("idTipoSeccion", idTipoSeccion);
			parts.add("fecha", fecha);
			return Arrays.asList(restTemplate.postForObject(lstURL_WS, parts, NoticiaExtraRSSDTO[].class));
		}catch(Exception e){
			LOG.error("Error - Msg : " + e.getMessage() );
			return Collections.emptyList();
		}
		
	}
	public List<NoticiaExtraRSSDTO> consultarNotasExtraBySeccion( String idSeccion, String fecha ) throws Exception {
		try{
			String lstMETODO = "/consultarNotasExtraBySeccion";
			String lstURL_WS = URL_DAO + lstMETODO;
			
			MultiValueMap<String, Object> parts;
			parts = new LinkedMultiValueMap<String, Object>();
			parts.add("idSeccion", idSeccion);
			parts.add("fecha", fecha);
			return Arrays.asList(restTemplate.postForObject(lstURL_WS, parts, NoticiaExtraRSSDTO[].class));
		}catch(Exception e){
			LOG.error("Error - Msg : " + e.getMessage() );
			return Collections.emptyList();
		}
		
	}
}
