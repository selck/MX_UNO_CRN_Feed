package mx.com.amx.mx.uno.proceso.dto;


public class TipoSeccionDTO {
	 
	private String FC_ID_TIPO_SECCION;
	private String FC_DESCRIPCION;;
	private String FI_ESTATUS;
	private String FC_FRIENDLY_URL;
	
	public String getFC_ID_TIPO_SECCION() {
		return FC_ID_TIPO_SECCION;
	}
	public void setFC_ID_TIPO_SECCION(String fc_id_tipo_seccion) {
		FC_ID_TIPO_SECCION = fc_id_tipo_seccion;
	}
	public String getFC_DESCRIPCION() {
		return FC_DESCRIPCION;
	}
	public void setFC_DESCRIPCION(String fc_descripcion) {
		FC_DESCRIPCION = fc_descripcion;
	}
	public String getFI_ESTATUS() {
		return FI_ESTATUS;
	}
	public void setFI_ESTATUS(String fi_estatus) {
		FI_ESTATUS = fi_estatus;
	}
	public void setFC_FRIENDLY_URL(String fC_FRIENDLY_URL) {
		FC_FRIENDLY_URL = fC_FRIENDLY_URL;
	}
	public String getFC_FRIENDLY_URL() {
		return FC_FRIENDLY_URL;
	}	
}
