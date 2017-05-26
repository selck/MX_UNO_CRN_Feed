package mx.com.amx.mx.uno.proceso.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Secciones")
public class Secciones implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private String mensaje;
	private List<TipoSeccionDTO> tipoSeccionesLst;
	private List<SeccionDTO> seccionesLst;
	
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
	public List<TipoSeccionDTO> getTipoSeccionesLst() {
		return tipoSeccionesLst;
	}
	public void setTipoSeccionesLst(List<TipoSeccionDTO> tipoSeccionesLst) {
		this.tipoSeccionesLst = tipoSeccionesLst;
	}
	public List<SeccionDTO> getSeccionesLst() {
		return seccionesLst;
	}
	public void setSeccionesLst(List<SeccionDTO> seccionesLst) {
		this.seccionesLst = seccionesLst;
	}
}