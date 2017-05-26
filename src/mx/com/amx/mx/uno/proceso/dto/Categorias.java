package mx.com.amx.mx.uno.proceso.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Categorias")
public class Categorias implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String codigo;
	private String mensaje;
	private List<CategoriaDTO> categotiasLst;
	
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
	public List<CategoriaDTO> getCategotiasLst() {
		return categotiasLst;
	}
	public void setCategotiasLst(List<CategoriaDTO> categotiasLst) {
		this.categotiasLst = categotiasLst;
	}
}