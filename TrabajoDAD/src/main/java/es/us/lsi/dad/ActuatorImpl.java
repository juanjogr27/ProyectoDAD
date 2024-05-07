package es.us.lsi.dad;


public class ActuatorImpl {

	protected Integer idActuators;
	protected Long timeStamp;
	protected Boolean estado;
	protected Integer idPlaca;
	protected Integer idGroup;
	

	public ActuatorImpl(Integer idActuators,Long timeStamp, Boolean estado,Integer idPlaca, Integer idGroup) {
		super();
		this.idActuators = idActuators;
		this.timeStamp = timeStamp;
		this.estado = estado;
		this.idPlaca = idPlaca;
		this.idGroup = idGroup;
	}

	public ActuatorImpl() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Integer getidActuators() {
		return idActuators;
	}

	public void setidActuators(Integer idActuators) {
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
	
	

	public Integer getIdPlaca() {
		return idPlaca;
	}

	public void setIdPlaca(Integer idPlaca) {
		this.idPlaca = idPlaca;
	}

	@Override
	public String toString() {
		return "ActuatorImpl [idActuators=" + idActuators + ", timeStamp=" + timeStamp + ", estado=" + estado
				+ ", idPlaca=" + idPlaca + ", idGroup=" + idGroup + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + ((idActuators == null) ? 0 : idActuators.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((idPlaca == null) ? 0 : idPlaca.hashCode());
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
		ActuatorImpl other = (ActuatorImpl) obj;
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
