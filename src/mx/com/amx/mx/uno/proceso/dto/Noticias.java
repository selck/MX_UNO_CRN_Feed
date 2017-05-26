package mx.com.amx.mx.uno.proceso.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Noticias")
public class Noticias implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private String mensaje;
	private List<NoticiaRSSDTO> noticiasLst;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public void setNoticiasLst(List<NoticiaRSSDTO> noticiasLst) {
		this.noticiasLst = noticiasLst;
	}
	public List<NoticiaRSSDTO> getNoticiasLst() {
		return noticiasLst;
	}
}