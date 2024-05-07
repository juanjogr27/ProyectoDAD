package es.us.lsi.dad;

public class SensorImpl {

	protected Integer idSensors;
	protected Long timeStamp;
	protected Boolean isBedroom;
	protected Integer temperatura;
	protected Integer idPlaca;
	protected Integer idGroup;

	public SensorImpl(Integer idSensors, Long timeStamp,Boolean isBedroom, Integer temperatura, Integer idPlaca,
			Integer idGroup) {
		super();
		this.idSensors = idSensors;
		this.timeStamp = timeStamp;
		this.isBedroom = isBedroom;
		this.temperatura = temperatura;
		this.idPlaca = idPlaca;
		this.idGroup = idGroup;
	}

	public SensorImpl() {
		super();
	}
	
	public Integer getidSensors() {
		return idSensors;
	}

	public void setidSensors(Integer idSensors) {
		this.idSensors = idSensors;
	}
	
	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Integer gettemperatura() {
		return temperatura;
	}

	public void settemperatura(Integer temperatura) {
		this.temperatura = temperatura;
	}

	public Integer getidPlaca() {
		return idPlaca;
	}

	public void setidPlaca(Integer idPlaca) {
		this.idPlaca = idPlaca;
	}

	public Boolean getIsBedroom() {
		return isBedroom;
	}

	public void setIsBedroom(Boolean isBedroom) {
		this.isBedroom = isBedroom;
	}
	
	public Integer getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}

	

	@Override
	public String toString() {
		return "SensorImpl [idSensors=" + idSensors + ", timeStamp=" + timeStamp + ", isBedroom=" + isBedroom
				+ ", temperatura=" + temperatura + ", idPlaca=" + idPlaca + ", idGroup=" + idGroup + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isBedroom == null) ? 0 : isBedroom.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + ((idSensors == null) ? 0 : idSensors.hashCode());
		result = prime * result + ((temperatura == null) ? 0 : temperatura.hashCode());
		result = prime * result + ((idPlaca == null) ? 0 : idPlaca.hashCode());
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
		SensorImpl other = (SensorImpl) obj;
		if (isBedroom == null) {
			if (other.isBedroom != null)
				return false;
		} else if (!isBedroom.equals(other.isBedroom))
			return false;
		if (idSensors == null) {
			if (other.idSensors != null)
				return false;
		} else if (!idSensors.equals(other.idSensors))
			return false;
		if (temperatura == null) {
			if (other.temperatura != null)
				return false;
		} else if (!temperatura.equals(other.temperatura))
			return false;
		if (idPlaca == null) {
			if (other.idPlaca != null)
				return false;
		} else if (!idPlaca.equals(other.idPlaca))
			return false;
		return true;
	}

}
