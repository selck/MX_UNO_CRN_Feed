package mx.com.amx.mx.uno.proceso.dto;

import java.io.Serializable;

public class ParametrosDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dominio;
	private String logo;
	private String pathPropertiesDominios;
	private String rutaCarpeta;
	private String rutaDestino;
	private String pathShell;
	private String id_magazine_home;

	//propiedades de monitoreo
	private String rutaArchivoMot;
	private String rutaEstaticoMot;
	private String nombreAplicacion;
	private String line_write;
	
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getPathPropertiesDominios() {
		return pathPropertiesDominios;
	}
	public void setPathPropertiesDominios(String pathPropertiesDominios) {
		this.pathPropertiesDominios = pathPropertiesDominios;
	}
	public String getRutaCarpeta() {
		return rutaCarpeta;
	}
	public void setRutaCarpeta(String rutaCarpeta) {
		this.rutaCarpeta = rutaCarpeta;
	}
	public String getRutaDestino() {
		return rutaDestino;
	}
	public void setRutaDestino(String rutaDestino) {
		this.rutaDestino = rutaDestino;
	}
	public String getPathShell() {
		return pathShell;
	}
	public void setPathShell(String pathShell) {
		this.pathShell = pathShell;
	}
	public String getRutaArchivoMot() {
		return rutaArchivoMot;
	}
	public void setRutaArchivoMot(String rutaArchivoMot) {
		this.rutaArchivoMot = rutaArchivoMot;
	}
	public String getRutaEstaticoMot() {
		return rutaEstaticoMot;
	}
	public void setRutaEstaticoMot(String rutaEstaticoMot) {
		this.rutaEstaticoMot = rutaEstaticoMot;
	}
	public String getNombreAplicacion() {
		return nombreAplicacion;
	}
	public void setNombreAplicacion(String nombreAplicacion) {
		this.nombreAplicacion = nombreAplicacion;
	}
	public void setLine_write(String line_write) {
		this.line_write = line_write;
	}
	public String getLine_write() {
		return line_write;
	}
	public String getId_magazine_home() {
		return id_magazine_home;
	}
	public void setId_magazine_home(String id_magazine_home) {
		this.id_magazine_home = id_magazine_home;
	}
	
}
