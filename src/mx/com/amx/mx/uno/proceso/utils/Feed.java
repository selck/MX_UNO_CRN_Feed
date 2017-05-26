package mx.com.amx.mx.uno.proceso.utils;

import java.io.File;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mx.com.amx.mx.uno.proceso.bo.impl.ProcesoBO;
import mx.com.amx.mx.uno.proceso.dto.CategoriaDTO;
import mx.com.amx.mx.uno.proceso.dto.NoticiaExtraRSSDTO;
import mx.com.amx.mx.uno.proceso.dto.NoticiaRSSDTO;
import mx.com.amx.mx.uno.proceso.dto.ParametrosDTO;
import mx.com.amx.mx.uno.proceso.dto.SeccionDTO;
import mx.com.amx.mx.uno.proceso.dto.TipoSeccionDTO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Feed {
	
	private static final Logger log = Logger.getLogger(Feed.class);
	
	private List<NoticiaRSSDTO> feedList;
	private ParametrosDTO parametros;

	private ProcesoBO procesoBO;
	private final Logger LOG = Logger.getLogger(this.getClass().getName());
	
	public Feed(){
		ObtenerProperties props = new ObtenerProperties();
		parametros = props.obtenerPropiedades();
	}
	 
	public static String eliminaEspacios(String cad) {
		String cadena = "";
		cad = cad.trim();
		cadena = cad.replaceAll("\\s+", " ");
		cadena = cad.replaceAll("\t", "");
		cadena = cad.replaceAll("\t", "");
		cadena = cad.replaceAll("\n", "");
		cadena = cad.replace("^\\s+", "");
		cadena = cad.replace("\\s+$", "");
		return cadena.trim();
	}
	
	public static String filter(String str) {
		StringBuilder filtered = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
			char current = str.charAt(i);
			if (current == ' ') {
				filtered.append("");
			} else{
				filtered.append(current);
			}
		}
		return filtered.toString();
	}
	
	 private void generarXML(String stNombreArchivo, String stNombreSeccion, String tipo) {
		try {
			if(tipo.equalsIgnoreCase("seccion")){
				feedList = consultarUltimasPorSeccion(stNombreSeccion);
			} else if(tipo.equalsIgnoreCase("tipoSeccion")){
				feedList = consultarUltimasPorTipoSeccion(stNombreSeccion);
			} else if(tipo.equalsIgnoreCase("magazine")){
				feedList = consultarNoticiasMagazine(stNombreSeccion);
			} else{
				feedList = obtenerNoticias(stNombreSeccion);
			}
			
			if ((this.feedList != null) && (this.feedList.size() > 0)) {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				Document docXML = docBuilder.newDocument();
				Document docXMLApex = docBuilder.newDocument();
				Element rootElement = docXML.createElement("NewsML");
				Element rootElementApex = docXMLApex.createElement("NewsML");
				docXML.appendChild(rootElement);
				docXMLApex.appendChild(rootElementApex);

				Element catalog = docXML.createElement("Catalog");
				Element catalogApex = docXMLApex.createElement("Catalog");
				catalog.setAttribute("Href", "http://www.afp.com/dtd/AFPCatalog.xml");
				catalogApex.setAttribute("Href", "http://www.afp.com/dtd/AFPCatalog.xml");
				rootElement.appendChild(catalog);
				rootElementApex.appendChild(catalogApex);
				for (int i = 0; i < this.feedList.size(); i++) {
					//DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					//DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					//StringWriter xmlAsWriter = new StringWriter();

					Element newsenv = docXML.createElement("NewsEnvelope");
					Element newsenvapex = docXMLApex.createElement("NewsEnvelope");
					Element datime = docXML.createElement("DateAndTime");
					Element datimeApex = docXMLApex.createElement("DateAndTime");
					SimpleDateFormat fc = new SimpleDateFormat("MM/dd/yy HH:mm a");
					//SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd'T'HHmmss");
					
					Date ahora = new Date();
					//String fecha = sdf.format(ahora);
					datime.appendChild(docXML.createTextNode(CodificaCaracteres.getDateZoneTime(fc.format(ahora))));
					datimeApex.appendChild(docXMLApex.createTextNode(CodificaCaracteres.getDateZoneTime(fc.format(ahora))));
					
					Element newserv = docXML.createElement("NewsService");
					newserv.setAttribute("FormalName", "Feed");
					newserv.setAttribute("link-url", parametros.getDominio());
					newserv.appendChild(docXML.createTextNode("http://www.unotv.com/portal/unotv/utils/imagenes/UnoTV.jpg"));
					newsenv.appendChild(datime);
					newsenvapex.appendChild(datimeApex);
					newsenv.appendChild(newserv);
					rootElement.appendChild(newsenv);
					rootElementApex.appendChild(newsenvapex);

					Element newsItem = docXML.createElement("NewsItem");
					Element newsItemApex = docXMLApex.createElement("NewsItem");
					rootElement.appendChild(newsItem);
					rootElementApex.appendChild(newsItemApex);

					Element identification = docXML.createElement("Identification");
					Element identificationApex = docXMLApex.createElement("Identification");
					Element newsIdentifier = docXML.createElement("NewsIdentifier");
					Element newsIdentifierApex = docXMLApex.createElement("NewsIdentifier");
					Element providerId = docXML.createElement("ProviderId");

					providerId.appendChild(docXML.createTextNode(this.parametros.getDominio()));

					Element newsItemId = docXML.createElement("NewsItemId");
					newsItemId.appendChild(docXML.createCDATASection(((NoticiaRSSDTO) this.feedList.get(i)).getFC_ID_CONTENIDO()));
					Element newsItemIdApex = docXMLApex.createElement("NewsItemId");
					newsItemIdApex.appendChild(docXMLApex.createCDATASection(((NoticiaRSSDTO) this.feedList.get(i)).getFC_ID_CONTENIDO()));

					newsIdentifier.appendChild(providerId);

					newsIdentifier.appendChild(newsItemId);
					newsIdentifierApex.appendChild(newsItemIdApex);
					identification.appendChild(newsIdentifier);
					identificationApex.appendChild(newsIdentifierApex);
					newsItem.appendChild(identification);
					newsItemApex.appendChild(identificationApex);

					Element newsManagement = docXML.createElement("NewsManagement");
					Element newsManagementApex = docXMLApex.createElement("NewsManagement");
					Element newsItemType = docXML.createElement("NewsItemType");
					Element newsItemTypeApex = docXMLApex.createElement("NewsItemType");
					newsItemType.setAttribute("FormalName", "News");
					newsItemTypeApex.setAttribute("FormalName", "News");
					Element firstCreated = docXML.createElement("FirstCreated");
					Element firstCreatedApex = docXMLApex.createElement("FirstCreated");

				//	Date publicada = ((NoticiaRSSDTO) this.feedList.get(i)).getFD_FECHA_PUBLICACION();
					firstCreated.appendChild(docXML.createTextNode(CodificaCaracteres.getDateZoneTime(this.feedList.get(i).getFD_FECHA_PUBLICACION())));
					firstCreatedApex.appendChild(docXMLApex.createTextNode(CodificaCaracteres.getDateZoneTime(this.feedList.get(i).getFD_FECHA_PUBLICACION())));
					Element thisRevisionCreated = docXML.createElement("ThisRevisionCreated");
					thisRevisionCreated.appendChild(docXML.createTextNode(CodificaCaracteres.getDateZoneTime(this.feedList.get(i).getFD_FECHA_PUBLICACION())));
					Element thisRevisionCreatedApex = docXMLApex.createElement("ThisRevisionCreated");
					thisRevisionCreatedApex.appendChild(docXMLApex.createTextNode(CodificaCaracteres.getDateZoneTime(this.feedList.get(i).getFD_FECHA_PUBLICACION())));
					
					Element status = docXML.createElement("Status");
					status.setAttribute("FormalName", "Usable");
					Element statusApex = docXMLApex.createElement("Status");
					statusApex.setAttribute("FormalName", "Usable");
					
					newsManagement.appendChild(newsItemType);
					newsManagement.appendChild(firstCreated);
					newsManagement.appendChild(thisRevisionCreated);
					newsManagement.appendChild(status);
					newsManagementApex.appendChild(newsItemTypeApex);
					newsManagementApex.appendChild(firstCreatedApex);
					newsManagementApex.appendChild(thisRevisionCreatedApex);
					newsManagementApex.appendChild(statusApex);
					newsItem.appendChild(newsManagement);
					newsItemApex.appendChild(newsManagementApex);

					Element newsComponent = docXML.createElement("NewsComponent");
					Element newsComponentApex = docXMLApex.createElement("NewsComponent");
					//SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
					
					newsComponent.setAttribute("Duid", ((NoticiaRSSDTO) this.feedList.get(i)).getFC_ID_CONTENIDO());
					newsComponent.setAttribute("Essential", "no");
					newsComponentApex.setAttribute("Duid", ((NoticiaRSSDTO) this.feedList.get(i)).getFC_ID_CONTENIDO());
					newsComponentApex.setAttribute("Essential", "no");
					
					Element newsLines = docXML.createElement("NewsLines");
					Element copyrightLine = docXML.createElement("CopyrightLine");
					Element newsLinesApex = docXMLApex.createElement("NewsLines");
					Element copyrightLineApex = docXMLApex.createElement("CopyrightLine");
					
					copyrightLine.appendChild(docXML.createTextNode("Uno Noticias - Todos los derechos reservados"));
					newsLines.appendChild(copyrightLine);
					copyrightLineApex.appendChild(docXMLApex.createTextNode("Uno Noticias - Todos los derechos reservados"));
					newsLinesApex.appendChild(copyrightLineApex);
					newsComponent.appendChild(newsLines);
					newsComponentApex.appendChild(newsLinesApex);
					
					Element newsComponent2 = docXML.createElement("NewsComponent");
					Element newsComponent2Apex = docXMLApex.createElement("NewsComponent");
					newsComponent2.setAttribute("Duid", ((NoticiaRSSDTO) this.feedList.get(i)).getFC_ID_CONTENIDO());
					newsComponent2Apex.setAttribute("Duid", ((NoticiaRSSDTO) this.feedList.get(i)).getFC_ID_CONTENIDO());
					Element dateLine = docXML.createElement("DateLine");
					dateLine.appendChild(docXML.createTextNode(CodificaCaracteres.getDateZoneTime(this.feedList.get(i).getFD_FECHA_PUBLICACION())));
					Element dateLineApex = docXMLApex.createElement("DateLine");
					dateLineApex.appendChild(docXMLApex.createTextNode(CodificaCaracteres.getDateZoneTime(this.feedList.get(i).getFD_FECHA_PUBLICACION())));
					Element author = docXML.createElement("Author");
					author.appendChild(docXML.createTextNode("UnoTV"));
					Element authorApex = docXMLApex.createElement("Author");
					authorApex.appendChild(docXMLApex.createTextNode("UnoTV"));
					
					newsComponent2.appendChild(dateLine);
					newsComponent2.appendChild(author);
					newsComponent2Apex.appendChild(dateLineApex);
					newsComponent2Apex.appendChild(authorApex);
					newsComponent.appendChild(newsComponent2);
					newsComponentApex.appendChild(newsComponent2Apex);
					Element newsComponent3 = docXML.createElement("NewsComponent");
					Element newsComponent3Apex = docXMLApex.createElement("NewsComponent");
					Element role = docXML.createElement("Role");
					role.setAttribute("FormalName", "Main");
					Element roleApex = docXMLApex.createElement("Role");
					roleApex.setAttribute("FormalName", "Main");
					newsComponent3.appendChild(role);
					newsComponent3Apex.appendChild(roleApex);
					Element newsLines2 = docXML.createElement("NewsLines");
					Element newsLines2Apex = docXMLApex.createElement("NewsLines");
					Element category = docXML.createElement("Category");
					category.appendChild(docXML.createTextNode(stNombreSeccion));
					Element headLine = docXML.createElement("HeadLine");
					headLine.appendChild(docXML.createTextNode(((NoticiaRSSDTO) this.feedList.get(i)).getFC_TITULO()));
					Element slugLine = docXML.createElement("SlugLine");
					slugLine.appendChild(docXML.createTextNode(((NoticiaRSSDTO) this.feedList.get(i)).getFC_DESCRIPCION()));
					Element categoryApex = docXMLApex.createElement("Category");
					categoryApex.appendChild(docXMLApex.createTextNode(stNombreSeccion));
					Element headLineApex = docXMLApex.createElement("HeadLine");
					headLineApex.appendChild(docXMLApex.createTextNode(((NoticiaRSSDTO) this.feedList.get(i)).getFC_TITULO()));
					Element slugLineApex = docXMLApex.createElement("SlugLine");
					slugLineApex.appendChild(docXMLApex.createTextNode(((NoticiaRSSDTO) this.feedList.get(i)).getFC_DESCRIPCION()));
					Element moreLink = docXML.createElement("MoreLink");
					moreLink.appendChild(docXML.createTextNode(parametros.getDominio()+ "/"+ ((NoticiaRSSDTO) this.feedList.get(i)).getFcLink() + "/" ));
					newsLines2.appendChild(category);
					newsLines2.appendChild(headLine);
					newsLines2.appendChild(slugLine);
					newsLines2.appendChild(moreLink);
					newsLines2Apex.appendChild(categoryApex);
					newsLines2Apex.appendChild(headLineApex);
					newsLines2Apex.appendChild(slugLineApex);
					newsComponent3.appendChild(newsLines2);
					newsComponent3Apex.appendChild(newsLines2Apex);
					Element contentItem = docXML.createElement("ContentItem");
					Element mediaType = docXML.createElement("MediaType");
					mediaType.setAttribute("FormalName", "ComplexData");
					Element mimeType = docXML.createElement("MimeType");
					mimeType.setAttribute("FormalName", "text/vnd.IPTC.NITF");
					Element dataContent = docXML.createElement("DataContent");
					Element nitf = docXML.createElement("nitf");
					Element body = docXML.createElement("body");
					Element bodyHead = docXML.createElement("body.head");
					Element hedline = docXML.createElement("hedline");
					Element hl1 = docXML.createElement("hl1");
					hl1.appendChild(docXML.createTextNode(((NoticiaRSSDTO) this.feedList.get(i)).getFC_TITULO()));
					hedline.appendChild(hl1);
					bodyHead.appendChild(hedline);
					Element contentItemApex = docXMLApex.createElement("ContentItem");
					Element mediaTypeApex = docXMLApex.createElement("MediaType");
					mediaTypeApex.setAttribute("FormalName", "ComplexData");
					Element mimeTypeApex = docXMLApex.createElement("MimeType");
					mimeTypeApex.setAttribute("FormalName", "text/vnd.IPTC.NITF");
					Element dataContentApex = docXMLApex.createElement("DataContent");
					Element nitfApex = docXMLApex.createElement("nitf");
					Element bodyApex = docXMLApex.createElement("body");
					Element bodyHeadApex = docXMLApex.createElement("body.head");
					Element hedlineApex = docXMLApex.createElement("hedline");
					Element hl1Apex = docXMLApex.createElement("hl1");
					hl1Apex.appendChild(docXMLApex.createTextNode(((NoticiaRSSDTO) this.feedList.get(i)).getFC_TITULO()));
					hedlineApex.appendChild(hl1Apex);
					bodyHeadApex.appendChild(hedlineApex);
					Element bodyContent = docXML.createElement("body.content");
					Element media = docXML.createElement("media");
					media.setAttribute("media-type", "image");
					Element mediaReference = docXML.createElement("media-reference");
					mediaReference.setAttribute("mime-type", "image/jpeg");
					Element bodyContentApex = docXMLApex.createElement("body.content");
					Element mediaApex = docXMLApex.createElement("media");
					mediaApex.setAttribute("media-type", "image");
					Element mediaReferenceApex = docXMLApex.createElement("media-reference");
					mediaReferenceApex.setAttribute("mime-type", "image/jpeg");

					String[] cadenas = ((NoticiaRSSDTO) this.feedList.get(i)).getFC_IMAGEN_PRINCIPAL().replaceAll("myconnect", "connect").split("&");
					mediaReference.setAttribute("source", parametros.getDominio() + cadenas[0]);
					mediaReference.setAttribute("alternate-text", ((NoticiaRSSDTO) this.feedList.get(i)).getFC_TITULO());
					Element mediaCaption = docXML.createElement("media-caption");
					media.appendChild(mediaReference);
					media.appendChild(mediaCaption);
					bodyContent.appendChild(media);
					mediaReferenceApex.setAttribute("source", parametros.getDominio() + cadenas[0]);
					mediaReferenceApex.setAttribute("alternate-text", ((NoticiaRSSDTO) this.feedList.get(i)).getFC_TITULO());
					Element mediaCaptionApex = docXMLApex.createElement("media-caption");
					mediaApex.appendChild(mediaReferenceApex);
					mediaApex.appendChild(mediaCaptionApex);
					bodyContentApex.appendChild(mediaApex);

					//String contenido = ((NoticiaRSSDTO) this.feedList.get(i)).getCL_RTF_CONTENIDO().replace("<strong>", "");
					String contenido=CodificaCaracteres.getEmbedPost(((NoticiaRSSDTO) this.feedList.get(i)).getCL_RTF_CONTENIDO().replace("<strong>", ""));
					String contenidoS = contenido.replace("</strong>", "");
					String contenidoU = contenidoS.replace("<br />", "");
					String contenidoF = contenidoU.replace("&nbsp;", "");
					String contenidoFinal = contenidoF.replace("<p dir=\"ltr\">", "");
					
					ArrayList<Element> elementosParrafo = insertaElementP(contenidoFinal, docXML);
					if ((elementosParrafo != null) && (elementosParrafo.size() > 0)) {
						for (int x = 0; x < elementosParrafo.size(); x++) {
							bodyContent.appendChild((Node) elementosParrafo.get(x));
						}
					}
					ArrayList<Element> elementosParrafoApex = insertaElementP(contenidoFinal, docXMLApex);
					if ((elementosParrafoApex != null) && (elementosParrafoApex.size() > 0)) {
						for (int x = 0; x < elementosParrafoApex.size(); x++) {
							bodyContentApex.appendChild((Node) elementosParrafoApex.get(x));
						}
					}
								
					body.appendChild(bodyHead);
					body.appendChild(bodyContent);
					nitf.appendChild(body);
					dataContent.appendChild(nitf);
					contentItem.appendChild(mediaType);
					contentItem.appendChild(mimeType);
					contentItem.appendChild(dataContent);

					bodyApex.appendChild(bodyHeadApex);
					bodyApex.appendChild(bodyContentApex);
					nitfApex.appendChild(bodyApex);
					dataContentApex.appendChild(nitfApex);
					contentItemApex.appendChild(mediaTypeApex);
					contentItemApex.appendChild(mimeTypeApex);
					contentItemApex.appendChild(dataContentApex);
					newsComponent3.appendChild(contentItem);
					newsComponent3Apex.appendChild(contentItemApex);
					
					
					List<NoticiaExtraRSSDTO> listExtra4;
					String fechaPublicacion=this.feedList.get(i).getFD_FECHA_PUBLICACION();
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm a");
					Date date = format.parse(fechaPublicacion);
					java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
					
					if(tipo.equalsIgnoreCase("seccion")){
						listExtra4 = consultarNotasExtraBySeccion(stNombreSeccion,timeStampDate.toString());
						log.info("Notas Extras de tipo: seccion["+stNombreSeccion+"] "+listExtra4.size());
					} else if(tipo.equalsIgnoreCase("tipoSeccion")){
						listExtra4 = consultarNotasExtraByTipoSeccion(stNombreSeccion,timeStampDate.toString());
						log.info("Notas Extras de tipo: tipoSeccion["+stNombreSeccion+"] "+listExtra4.size());
					} else if(tipo.equalsIgnoreCase("magazine")){
						listExtra4 = consultarNotasExtraByCategoria(this.feedList.get(i).getFC_ID_CATEGORIA(),timeStampDate.toString());
						log.info("Notas Extras de tipo: magazine["+stNombreSeccion+"] "+listExtra4.size());
					} else{
						listExtra4 = consultarNotasExtraByCategoria(stNombreSeccion,timeStampDate.toString());
						log.info("Notas Extras de tipo: categoria["+stNombreSeccion+"] "+listExtra4.size());
					}
					String descripcion="";
					for (NoticiaExtraRSSDTO nota : listExtra4) {
						Element associatedWith  = docXML.createElement("AssociatedWith");
						associatedWith.setAttribute("FormalName", "SeeAlso");
						associatedWith.setAttribute("NewsItem", nota.getUrl_nota());
						Element comment1  = docXML.createElement("Comment");
						comment1.setAttribute("xml:lang", "es");
						comment1.setAttribute("FormalName", "Title");
						comment1.appendChild(docXML.createTextNode(nota.getTitulo()));
						Element comment2  = docXML.createElement("Comment");
						comment2.setAttribute("xml:lang", "es");
						comment2.setAttribute("FormalName", "Genre");
						if(tipo.equalsIgnoreCase("seccion")){
							descripcion=nota.getDescripcion_seccion();
						} else if(tipo.equalsIgnoreCase("tipoSeccion")){
							descripcion=nota.getDescripcion_tipo_seccion();
						}else{
							descripcion=nota.getDescripcion_categoria();
						}
						comment2.appendChild(docXML.createTextNode(descripcion));
						String[] cadenasAssociate = nota.getImagen_principal().replaceAll("myconnect", "connect").replaceAll("Principal", "Miniatura").split("&");
						Element mediaAssociate = docXML.createElement("media");
						mediaAssociate.setAttribute("media-type", "image");
						Element mediaReferenceAssociate = docXML.createElement("media-reference");
						mediaReferenceAssociate.setAttribute("mime-type", "image/jpeg");
						mediaReferenceAssociate.setAttribute("source", parametros.getDominio() + cadenasAssociate[0]);
						mediaReferenceAssociate.setAttribute("alternate-text", nota.getPie_imagen());
						mediaReferenceAssociate.setAttribute("copyright", nota.getFuente());
						Element mediaCaptionAssociate = docXML.createElement("media-caption");
						mediaCaptionAssociate.appendChild(docXML.createTextNode(nota.getPie_imagen()));
						
						mediaAssociate.appendChild(mediaReferenceAssociate);
						mediaAssociate.appendChild(mediaCaptionAssociate);
						
						associatedWith.appendChild(comment1);
						associatedWith.appendChild(comment2);
						associatedWith.appendChild(mediaAssociate);
						newsComponent3.appendChild(associatedWith);
						
					}
					
					newsComponent2.appendChild(newsComponent3);
					newsComponent2Apex.appendChild(newsComponent3Apex);
					newsItem.appendChild(newsComponent);
					newsItemApex.appendChild(newsComponentApex);
				}//fin del for
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty("indent", "yes");
				transformer.setOutputProperty("encoding", "UTF-8");
				DOMSource source = new DOMSource(docXML);
//				DOMSource sourceApex = new DOMSource(docXMLApex);

//				String rutaGuardadorssApex = this.rutaCarpetaApex;

				File f = new File(parametros.getRutaCarpeta() + stNombreArchivo);
				StreamResult finalResult = new StreamResult(f);

//				File fApex = new File(parametros.getRutaCarpetaApex() + stNombreArchivo);
//				StreamResult finalResultApex = new StreamResult(fApex);

				log.info(" Archivo  " + f.getAbsoluteFile());
				log.info("=================================================>");
//				log.info(" ArchivoApex  " + fApex.getAbsoluteFile());

				transformer.transform(source, finalResult);
//				transformer.transform(sourceApex, finalResultApex);

//				transfiereWebServer(parametros.getPathShell(), parametros.getRutaCarpeta()+"*",parametros.getRutaDestino());
			}
		} catch (Exception e) {
			log.info("Error: " + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @param local
	 * @param remote
	 * @return
	 */
	public boolean transfiereWebServer(String rutaShell, String pathLocal, String pathRemote) {
		boolean success = false;

		String comando = "";
		  
		if(pathLocal.equals("") && pathRemote.equals("")){
			  comando = rutaShell;
		} else{
			  comando = rutaShell + " " + pathLocal+ "* " + pathRemote;
		}
		
		log.info("Comando:  " + comando);
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(comando);
			success = true;
		} catch(Exception e) {
			success = false;
			log.error("Ocurrio un error al ejecutar el Shell " + comando + ": ", e);
		}
		return success;
	}
	public static void main(String[] args){
		try {
			Feed getInfo = new Feed();
			getInfo.writeNewsML();
		} catch (Exception e) {
			System.out.println("Error main"+e);
		}
	}
	public org.w3c.dom.Document writeNewsML() {
		log.info(".: Ejecutandose...");
		procesoBO = new ProcesoBO();
		try{
			log.info(":::: [INI] generamos archivos por categorias ::::");
			List<CategoriaDTO> lst = procesoBO.getCategorias().getCategotiasLst();
			log.info("categorias size: "+lst.size());
			for(CategoriaDTO dto : lst){
				generarXML(dto.getFC_ID_CATEGORIA()+".xml", dto.getFC_ID_CATEGORIA(), "categoria");
			}
			log.info(":::: [FIN] generamos archivos por categorias ::::");
			log.info(":::: [INI] generamos archivos por seccion ::::");
			List<SeccionDTO> lstSecciones = procesoBO.getSecciones().getSeccionesLst();
			log.info("Secciones size: "+lstSecciones.size());
			for(SeccionDTO dto : lstSecciones){
				generarXML(dto.getFC_FRIENDLY_URL()+"sec.xml", dto.getFC_ID_SECCION(), "seccion");
			}
			log.info(":::: [FIN] generamos archivos por seccion ::::");
			log.info(":::: [INI] generamos archivos por tipo seccion ::::");
			List<TipoSeccionDTO> lstTipoSecciones = procesoBO.getTipoSecciones().getTipoSeccionesLst();
			log.info("Tipo Secciones size: "+lstTipoSecciones.size());
			for(TipoSeccionDTO dto : lstTipoSecciones){
				generarXML(dto.getFC_FRIENDLY_URL()+".xml", dto.getFC_ID_TIPO_SECCION(), "tipoSeccion");
			}
			log.info(":::: [FIN] generamos archivos por tipo seccion ::::");
			
			log.info(":::: [INI] generamos archivos NoticiasMagazine ::::");
				generarXML("magazine.xml", parametros.getId_magazine_home(), "magazine");
			log.info(":::: [FIN] generamos archivos NoticiasMagazine ::::");
		
			log.info(":::: [INI] actualizamos monitoreo");
			EscribeArchivoMonitoreo.escribeArchivoMon(parametros);
			log.info(":::: [FIN] actualizamos monitoreo");
			
		} catch ( Exception e ){
			log.error("[ getInfoRSS ] Ocurrio un error al obtener informacion " + e.getCause());
		}
		
		return null;
	}
	
	private static ArrayList<Element> insertaElementP(String cad, Document docXML) {
		ArrayList<Element> parrafos = new ArrayList();
		try {
			cad = filter(cad);
			String cadena = cad.trim();
			cadena = eliminaEspacios(cad);
			String[] temp = cadena.split("</p>");
			int auxi = 0;
			String aux = "";
			for (int i = 0; i < temp.length; i++) {
				aux = eliminaEspacios(temp[i]);
				aux.replace("strong", "");
				auxi = temp[i].trim().length();
				if (auxi > 0) {
					//log.info("AUXI: " + auxi);
					char car = temp[i].trim().charAt(auxi - 1);
					Element parrafo = docXML.createElement("p");
					if (car == '.') {
						parrafo.appendChild(docXML.createTextNode(aux));
					} else {
						parrafo.appendChild(docXML.createTextNode(aux + "."));
					}
					parrafos.add(parrafo);
				}
			}
		} catch (Exception e) {
			log.error("[ insertaElementP ] Error:" + e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return parrafos;
	}
	
	/**
	 * @param idSeccion
	 * @return List<NoticiaRSSDTO>
	 * @throws Exception
	 */
	public List<NoticiaRSSDTO> consultarNoticiasMagazine(String idMagazine) throws Exception{
		procesoBO = new ProcesoBO();
		List<NoticiaRSSDTO> listaNoticias = procesoBO.consultarNoticiasMagazine(idMagazine).getNoticiasLst();
		return listaNoticias;
	}
	/**
	 * @param idCategoria
	 * @return
	 * @throws Exception
	 */
	public List<NoticiaRSSDTO> obtenerNoticias(String idCategoria) throws Exception{
		//LOG.info("idCategoria: "+idCategoria);
		procesoBO = new ProcesoBO();
		List<NoticiaRSSDTO> listaNoticias = procesoBO.consultarNoticias(idCategoria).getNoticiasLst();;
		return listaNoticias;
	}
	
	/**
	 * @param idSeccion
	 * @return List<NoticiaRSSDTO>
	 * @throws Exception
	 */
	public List<NoticiaRSSDTO> consultarUltimasPorSeccion(String idSeccion) throws Exception{
		procesoBO = new ProcesoBO();
		List<NoticiaRSSDTO> listaNoticias = procesoBO.consultarUltimasPorSeccion(idSeccion).getNoticiasLst();
		return listaNoticias;
	}
	

	/**
	 * @param idSeccion
	 * @return List<NoticiaRSSDTO>
	 * @throws Exception
	 */
	public List<NoticiaRSSDTO> consultarUltimasPorTipoSeccion(String idSeccion) throws Exception{
		procesoBO = new ProcesoBO();
		List<NoticiaRSSDTO> listaNoticias = procesoBO.consultarUltimasPorTipoSeccion(idSeccion).getNoticiasLst();
		return listaNoticias;
	}
	/**
	 * @param idCategoria
	 * @return Lista de Notas Extras por categoría List<NoticiaRSSDTO>
	 * @throws Exception
	 */
	public List<NoticiaExtraRSSDTO> consultarNotasExtraByCategoria(String idCategoria,String fecha) throws Exception{
		procesoBO = new ProcesoBO();
		return procesoBO.consultarNotasExtraByCategoria(idCategoria, fecha);
	}
	
	/**
	 * @param idSeccion
	 * @return Lista de Notas Extras por seccion List<NoticiaRSSDTO>
	 * @throws Exception
	 */
	public List<NoticiaExtraRSSDTO> consultarNotasExtraBySeccion(String idSeccion, String fecha) throws Exception{
		procesoBO = new ProcesoBO();
		return procesoBO.consultarNotasExtraBySeccion(idSeccion, fecha);
		
	}
	
	
	/**
	 * @param idSeccion
	 * @return Lista de Notas Extras por tipo de seccion List<NoticiaRSSDTO>
	 * @throws Exception
	 */
	public List<NoticiaExtraRSSDTO> consultarNotasExtraByTipoSeccion(String idSeccion, String fecha) throws Exception{
		procesoBO = new ProcesoBO();
		return procesoBO.consultarNotasExtraByTipoSeccion(idSeccion, fecha);
	}
	
	@Autowired
	public void setProcesoBO(ProcesoBO procesoBO) {
		this.procesoBO = procesoBO;
	}

	public ProcesoBO getProcesoBO() {
		return procesoBO;
	}
	
}