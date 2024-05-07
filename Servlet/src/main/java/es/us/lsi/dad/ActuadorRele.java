package es.us.lsi.dad;

import java.util.Calendar;
import java.util.Objects;

public class ActuadorRele {
	
	private Integer idActuador;
	private Long timeStamp;
	private Boolean estado;
	private Integer idGroup;
	
	public ActuadorRele(Integer idActuador, Boolean estado, Integer idGroup) {
		super();
		this.idActuador = idActuador;
		this.timeStamp = Calendar.getInstance().getTimeInMillis();
		this.estado = estado;
		this.idGroup = idGroup;
	}


	public Integer getIdActuador() {
		return idActuador;
	}


	public void setIdActuador(Integer idActuador) {
		this.idActuador = idActuador;
	}


	public Long getTimeStamp() {
	    return timeStamp;
	}

	public void setFechaUltimaActivacion(Long timeStamp) {
		this.timeStamp = timeStamp;
	}


	public Boolean getEstado() {
		return estado;
	}


	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Integer getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}


	@Override
	public int hashCode() {
		return Objects.hash(idActuador);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActuadorRele other = (ActuadorRele) obj;
		return Objects.equals(idActuador, other.idActuador);
	}


	@Override
	public String toString() {
		return "ActuadorRele [idActuador=" + idActuador + ", timeStamp=" + timeStamp
				+ ", estado=" + estado + "]";
	}
	
	
	
	
	
	
	
	

}
