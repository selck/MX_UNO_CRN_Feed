package mx.com.amx.mx.uno.proceso.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import mx.com.amx.mx.uno.proceso.dto.ParametrosDTO;

import org.apache.log4j.Logger;

/**
 * Clase para escribir en el archivo de monitore 
 * */
public class EscribeArchivoMonitoreo {

	//LOG
	private static Logger LOG = Logger.getLogger(EscribeArchivoMonitoreo.class);
		
	/**
	 * Metodo que escribe en el archivo de monitoreo
	 * */
	public static void escribeArchivoMon(ParametrosDTO parametrosDTO) {	
			String line = "";
	        int intLine = 0;

	        try {
				
	        	//Fecha formato timestamp linux
	        	long unixTime = System.currentTimeMillis() / 1000L;
	 			
	        	//Linea a actualizar
	        	String lineNew = unixTime+" "+parametrosDTO.getRutaEstaticoMot()+" "+parametrosDTO.getNombreAplicacion();
	        	
	        	
	        	//Leemos el archivo actual y lo pasamo a un HashMap
	        	Map<Integer, String> map = new HashMap<Integer, String>();
	        
	        	File file = new File(parametrosDTO.getRutaArchivoMot());
	        	FileReader fr = new FileReader(file);
	        	
	        	BufferedReader in = new BufferedReader(fr);
	 	        while ((line = in.readLine()) != null) {
	 	            map.put(intLine, line.toString());
	 	            intLine++;
	 	        }
	 	        in.close();
	 	        
	 	        LOG.debug("map obtenido: "+map.toString());
	 	       
	 	       //Escribimos la linea a modificar
	 	       map.put(Integer.parseInt(parametrosDTO.getLine_write()), lineNew);

	 	       //Sobrescribimos el archivo con los nuevos valores.	 	       
	 	       BufferedWriter writer = new BufferedWriter(new FileWriter(file));	 	        
	 	       for (Map.Entry<Integer, String> entry : map.entrySet()) {
	 	    	    Integer key = entry.getKey();
	 	    	    String value = entry.getValue();
	 	    	    LOG.debug(key+ " - "+value);	 	    	    
	 	    	    writer.write(value);
	 	            writer.newLine();
	 	    	}	 	        
	 	        writer.flush();
	 	        writer.close();
	 	        
			} catch (Exception e) {
				LOG.error("Exception en escribe: ",e);
			}

	}
}//FIN CLASE