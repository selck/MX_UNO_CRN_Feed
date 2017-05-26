package mx.com.amx.mx.uno.proceso.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import mx.com.amx.mx.uno.proceso.dto.RedSocialEmbedPost;

import org.apache.log4j.Logger;


public class CodificaCaracteres {
	
	private static Logger logger=Logger.getLogger(CodificaCaracteres.class);
	
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
			logger.error("Error getDateZoneTime: ",e);
			return fechaString;
		}
		return fecha;
	}
	
	private static String devuelveCadenasPost(String id_red_social, String rtfContenido){
		String url="", cadenaAReemplazar="", salida="";
		try {
			cadenaAReemplazar=rtfContenido.substring(rtfContenido.indexOf("["+id_red_social+"="), rtfContenido.indexOf("="+id_red_social+"]"))+"="+id_red_social+"]";
			url=cadenaAReemplazar.replace("["+id_red_social+"=", "").replace("="+id_red_social+"]", "");
			salida=cadenaAReemplazar+"|"+url;
		} catch (Exception e) {
			logger.error("Error devuelveCadenasPost: ",e);
			return "|";
		}
		return salida;
	}
	public static String getEmbedPost(String RTFContenido){
		try {
			String rtfContenido=RTFContenido;
			String url, cadenaAReemplazar;
			StringBuffer embedCode;
			HashMap<String,ArrayList<RedSocialEmbedPost>> MapAReemplazar = new HashMap<String,ArrayList<RedSocialEmbedPost>>();
			int num_post_embebidos;
			int contador;
			if(rtfContenido.contains("[instagram")){
				logger.info("Embed Code instagram");
				ArrayList<RedSocialEmbedPost> listRedSocialEmbedInstagram=new ArrayList<RedSocialEmbedPost>();
				num_post_embebidos=rtfContenido.split("\\[instagram=").length-1;
				contador=1;
				do{
					RedSocialEmbedPost embebedPost=new RedSocialEmbedPost();
					String cadenas=devuelveCadenasPost("instagram", rtfContenido);
					cadenaAReemplazar=cadenas.split("\\|")[0];
					url=cadenas.split("\\|")[1];
					rtfContenido=rtfContenido.replace(cadenaAReemplazar, "");
					embedCode=new StringBuffer();
					embedCode.append(" <div class=\"instagram-post\"> \n");
					embedCode.append(" <blockquote data-instgrm-captioned data-instgrm-version=\"6\" class=\"instagram-media\"> \n");
					embedCode.append(" <div> \n");
					embedCode.append(" 	<p><a href=\""+url+"\"></a></p> \n");
					embedCode.append(" </div> \n");
					embedCode.append(" </blockquote> \n");
					embedCode.append(" <script async defer src=\"//platform.instagram.com/en_US/embeds.js\"></script> \n");
					embedCode.append(" </div> \n");
					
					embebedPost.setCadena_que_sera_reemplazada(cadenaAReemplazar);
					embebedPost.setRed_social("instagram");
					embebedPost.setCodigo_embebido(embedCode.toString());
					
					listRedSocialEmbedInstagram.add(embebedPost);
					contador ++;
				}while(contador <= num_post_embebidos);
				
				MapAReemplazar.put("instagram", listRedSocialEmbedInstagram);
			}
			if(rtfContenido.contains("[twitter")){
				logger.info("Embed Code twitter");
				ArrayList<RedSocialEmbedPost> listRedSocialEmbedTwitter=new ArrayList<RedSocialEmbedPost>();
				num_post_embebidos=rtfContenido.split("\\[twitter=").length-1;
				contador=1;
				do{
					RedSocialEmbedPost embebedPost=new RedSocialEmbedPost();
					String cadenas=devuelveCadenasPost("twitter", rtfContenido);
					cadenaAReemplazar=cadenas.split("\\|")[0];
					url=cadenas.split("\\|")[1];
					rtfContenido=rtfContenido.replace(cadenaAReemplazar, "");
					embedCode=new StringBuffer();
					embedCode.append(" <div class=\"tweeet-post\"> \n");
					embedCode.append(" 		<blockquote data-width=\"500\" lang=\"es\" class=\"twitter-tweet\"><a href=\""+url+"\"></a></blockquote> \n");
					embedCode.append(" 		<script type=\"text/javascript\" async defer src=\"//platform.twitter.com/widgets.js\" id=\"twitter-wjs\"></script> \n");
					embedCode.append(" </div> \n");
					
					embebedPost.setCadena_que_sera_reemplazada(cadenaAReemplazar);
					embebedPost.setRed_social("twitter");
					embebedPost.setCodigo_embebido(embedCode.toString());
					
					listRedSocialEmbedTwitter.add(embebedPost);
					contador ++;
				}while(contador <= num_post_embebidos);
				
				MapAReemplazar.put("twitter", listRedSocialEmbedTwitter);
			
			}
			if(rtfContenido.contains("[facebook")){
				logger.info("Embed Code facebook");
				ArrayList<RedSocialEmbedPost> listRedSocialEmbedFacebook=new ArrayList<RedSocialEmbedPost>();
				num_post_embebidos=rtfContenido.split("\\[facebook=").length-1;
				contador=1;
				do{
					RedSocialEmbedPost embebedPost=new RedSocialEmbedPost();
					String cadenas=devuelveCadenasPost("facebook", rtfContenido);
					cadenaAReemplazar=cadenas.split("\\|")[0];
					url=cadenas.split("\\|")[1];
					rtfContenido=rtfContenido.replace(cadenaAReemplazar, "");
					embedCode=new StringBuffer();
					embedCode=new StringBuffer();
					embedCode.append(" <div class=\"facebook-post\"> \n");
					embedCode.append(" 		<div data-href=\""+url+"\" data-width=\"500\" class=\"fb-post\"></div> \n");
					embedCode.append(" </div> \n");
					
					embebedPost.setCadena_que_sera_reemplazada(cadenaAReemplazar);
					embebedPost.setRed_social("facebook");
					embebedPost.setCodigo_embebido(embedCode.toString());
					
					listRedSocialEmbedFacebook.add(embebedPost);
					contador++;;
				}while(contador <= num_post_embebidos);
				
				MapAReemplazar.put("facebook", listRedSocialEmbedFacebook);
			}
			if(rtfContenido.contains("[giphy")){
				logger.info("Embed Code giphy");
				ArrayList<RedSocialEmbedPost> listRedSocialEmbedGiphy=new ArrayList<RedSocialEmbedPost>();
				num_post_embebidos=rtfContenido.split("\\[giphy=").length-1;
				contador=1;
				do{
					RedSocialEmbedPost embebedPost=new RedSocialEmbedPost();
					String cadenas=devuelveCadenasPost("giphy", rtfContenido);
					cadenaAReemplazar=cadenas.split("\\|")[0];
					url=cadenas.split("\\|")[1];
					rtfContenido=rtfContenido.replace(cadenaAReemplazar, "");
					embedCode=new StringBuffer();
					embedCode=new StringBuffer();
					embedCode.append(" <img src=\""+url.split("\\,")[1]+"\" class=\"giphy\"> \n");
					embedCode.append(" <span>Vía  \n");
					embedCode.append(" 	<a href=\""+url.split("\\,")[0]+"\" target=\"_blank\">Giphy</a> \n");
					embedCode.append("  </span> \n");
					
					embebedPost.setCadena_que_sera_reemplazada(cadenaAReemplazar);
					embebedPost.setRed_social("giphy");
					embebedPost.setCodigo_embebido(embedCode.toString());
					
					listRedSocialEmbedGiphy.add(embebedPost);
					contador ++;
				}while(contador <= num_post_embebidos);
				
				MapAReemplazar.put("giphy", listRedSocialEmbedGiphy);
			}
			
			
			if(!MapAReemplazar.isEmpty()){
				Iterator<String> iterator_red_social = MapAReemplazar.keySet().iterator();
				String red_social="", codigo_embebido="", cadena_que_sera_reemplazada="";
				while(iterator_red_social.hasNext()){
					red_social = iterator_red_social.next();
			        if(red_social.equalsIgnoreCase("twitter") || red_social.equalsIgnoreCase("facebook") || red_social.equalsIgnoreCase("instagram") 
			        		|| red_social.equalsIgnoreCase("giphy")){
			        	ArrayList<RedSocialEmbedPost> listEmbebidos=MapAReemplazar.get(red_social);
			        	for (RedSocialEmbedPost redSocialEmbedPost : listEmbebidos) {
				        	cadena_que_sera_reemplazada=redSocialEmbedPost.getCadena_que_sera_reemplazada();
				        	codigo_embebido=redSocialEmbedPost.getCodigo_embebido();
				        	RTFContenido=RTFContenido.replace(cadena_que_sera_reemplazada, codigo_embebido);
						}
			        	
			        }
			    } 
			}
			
			return RTFContenido;
		} catch (Exception e) {
			logger.error("Error getEmbedPost: ",e);
			return RTFContenido;
		}
	}
	public String cambiaCaracteres(String texto) {
		texto = texto.replaceAll("á", "&#225;");
        texto = texto.replaceAll("é", "&#233;");
        texto = texto.replaceAll("í", "&#237;");
        texto = texto.replaceAll("ó", "&#243;");
        texto = texto.replaceAll("ú", "&#250;");  
        texto = texto.replaceAll("Á", "&#193;");
        texto = texto.replaceAll("É", "&#201;");
        texto = texto.replaceAll("Í", "&#205;");
        texto = texto.replaceAll("Ó", "&#211;");
        texto = texto.replaceAll("Ú", "&#218;");
        texto = texto.replaceAll("Ñ", "&#209;");
        texto = texto.replaceAll("ñ", "&#241;");        
        texto = texto.replaceAll("ª", "&#170;");          
        texto = texto.replaceAll("ä", "&#228;");
        texto = texto.replaceAll("ë", "&#235;");
        texto = texto.replaceAll("ï", "&#239;");
        texto = texto.replaceAll("ö", "&#246;");
        texto = texto.replaceAll("ü", "&#252;");    
        texto = texto.replaceAll("Ä", "&#196;");
        texto = texto.replaceAll("Ë", "&#203;");
        texto = texto.replaceAll("Ï", "&#207;");
        texto = texto.replaceAll("Ö", "&#214;");
        texto = texto.replaceAll("Ü", "&#220;");
        texto = texto.replaceAll("¿", "&#191;");
        texto = texto.replaceAll("“", "&#8220;");        
        texto = texto.replaceAll("”", "&#8221;");
        texto = texto.replaceAll("‘", "&#8216;");
        texto = texto.replaceAll("’", "&#8217;");
        texto = texto.replaceAll("…", "...");	        
		return texto;
	}
	
	public String cambiaAcentos(String texto) {
		texto = texto.replaceAll("á", "a");
        texto = texto.replaceAll("é", "e");
        texto = texto.replaceAll("í", "i");
        texto = texto.replaceAll("ó", "o");
        texto = texto.replaceAll("ú", "u");  
        texto = texto.replaceAll("Á", "A");
        texto = texto.replaceAll("É", "E");
        texto = texto.replaceAll("Í", "I");
        texto = texto.replaceAll("Ó", "O");
        texto = texto.replaceAll("Ú", "U");
        texto = texto.replaceAll("Ñ", "N");
        texto = texto.replaceAll("ñ", "n");        
        texto = texto.replaceAll("ª", "");          
        texto = texto.replaceAll("ä", "a");
        texto = texto.replaceAll("ë", "e");
        texto = texto.replaceAll("ï", "i");
        texto = texto.replaceAll("ö", "o");
        texto = texto.replaceAll("ü", "u");    
        texto = texto.replaceAll("Ä", "A");
        texto = texto.replaceAll("Ë", "E");
        texto = texto.replaceAll("Ï", "I");
        texto = texto.replaceAll("Ö", "O");
        texto = texto.replaceAll("Ü", "U");
        texto = texto.replaceAll("%", "");        
		return texto;
	}

}
