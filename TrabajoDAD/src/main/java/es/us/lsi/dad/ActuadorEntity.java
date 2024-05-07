package es.us.lsi.dad;

import java.util.Calendar;

public class ActuadorEntity {
	
	protected Integer idActuators;
	protected Long timeStamp;
	protected Boolean estado;
	protected Integer idGroup;

	public ActuadorEntity(Integer idActuators, Boolean estado, Integer idGroup) {
		super();
		this.idActuators = idActuators;
		this.timeStamp = Calendar.getInstance().getTimeInMillis();
		this.estado = estado;
		this.idGroup = idGroup;
	}

	public ActuadorEntity() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getIdActuators() {
		return idActuators;
	}

	public void setIdActuators(Integer idActuators) {
		this.idActuators = idActuators;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
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
	public String toString() {
		return "ActuadorEntity [idActuators=" + idActuators + ", timeStamp=" + timeStamp + ", estado=" + estado
				+ ", idGroup=" + idGroup + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idActuators == null) ? 0 : idActuators.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((idGroup == null) ? 0 : idGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		ActuadorEntity other = (ActuadorEntity) obj;
		
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		
		if (idActuators == null) {
			if (other.idActuators != null)
				return false;
		} else if (!idActuators.equals(other.idActuators))
			return false;
		
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		
		if (idGroup == null) {
			if (other.idGroup != null)
				return false;
		} else if (!idGroup.equals(other.idGroup))
			return false;
		
		
		return true;
		
		
	}

}


