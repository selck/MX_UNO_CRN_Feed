package mx.com.amx.mx.uno.proceso.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import mx.com.amx.mx.uno.proceso.dto.ParametrosDTO;

import org.apache.log4j.Logger;

public class ObtenerProperties {
	
	private final Logger LOG = Logger.getLogger(this.getClass().getName());
	
	/** Obtiene los datos del archivo de propiedades externo
	 * @return ParametrosDTO con los datos obtenidos
	 * */
	public ParametrosDTO obtenerPropiedades() {
		ParametrosDTO parametros = new ParametrosDTO();		 
		try {	    		
			Properties propsTmp = new Properties();
		    propsTmp.load(this.getClass().getResourceAsStream( "/general.properties" ));
		    String ambiente = propsTmp.getProperty("ambiente");
			String rutaProperties = propsTmp.getProperty( ambiente  +".ruta.properties");	
			
			Properties props = new Properties();
			props.load(new FileInputStream(new File(rutaProperties)));			
			
			parametros.setDominio(props.getProperty("dominio") == null? "" : props.getProperty("dominio"));
			parametros.setLogo(props.getProperty("logo") == null? "" : props.getProperty("logo"));
			parametros.setPathPropertiesDominios(props.getProperty("pathPropertiesDominios") == null? "" : props.getProperty("pathPropertiesDominios"));
			parametros.setRutaCarpeta(props.getProperty("rutaCarpeta") == null? "" : props.getProperty("rutaCarpeta"));
			parametros.setRutaDestino(props.getProperty("rutaDestino") == null? "" : props.getProperty("rutaDestino"));
			parametros.setPathShell(props.getProperty("pathShell") == null? "" : props.getProperty("pathShell"));
			parametros.setId_magazine_home(props.getProperty("id_magazine_home") == null ?"" : props.getProperty("id_magazine_home"));
			
			//parametros de monitoreo
			parametros.setRutaArchivoMot(props.getProperty("rutaArchivoMot") == null? "" : props.getProperty("rutaArchivoMot"));
			parametros.setRutaEstaticoMot(props.getProperty("rutaEstaticoMot") == null? "" : props.getProperty("rutaEstaticoMot"));
			parametros.setNombreAplicacion(props.getProperty("nombreAplicacion") == null? "" : props.getProperty("nombreAplicacion"));
			parametros.setLine_write(props.getProperty("line_write") == null? "" : props.getProperty("line_write"));
		
		} catch (Exception ex) {
			parametros = new ParametrosDTO();
			LOG.error("No se encontro el Archivo de propiedades: ", ex);			
		}
		return parametros;
    }	
}