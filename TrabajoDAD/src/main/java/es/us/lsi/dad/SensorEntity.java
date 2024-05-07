package es.us.lsi.dad;

import java.util.Calendar;

public class SensorEntity {

	protected Integer idSensors;
	protected Long timeStamp;
	protected Boolean isBedroom;
	protected Integer temperatura;
	protected Integer idPlaca;
	protected Integer idGroup;

	

	public SensorEntity(Integer idSensors, Boolean isBedroom, Integer temperatura,
			Integer idPlaca, Integer idGroup) {
		super();
		this.idSensors = idSensors;
		this.timeStamp = Calendar.getInstance().getTimeInMillis();
		this.isBedroom = isBedroom;
		this.temperatura = temperatura;
		this.idPlaca = idPlaca;
		this.idGroup = idGroup;
	}

	public SensorEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Integer getIdSensors() {
		return idSensors;
	}

	public void setIdSensors(Integer idSensors) {
		this.idSensors = idSensors;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Integer getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Integer temperatura) {
		this.temperatura = temperatura;
	}

	public Integer getIdPlaca() {
		return idPlaca;
	}

	public void setIdPlaca(Integer idPlaca) {
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
		return "SensorEntity [idSensors=" + idSensors + ", timeStamp=" + timeStamp + ", isBedroom=" + isBedroom
				+ ", temperatura=" + temperatura + ", idPlaca=" + idPlaca + ", idGroup=" + idGroup + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idSensors == null) ? 0 : idSensors.hashCode());
		result = prime * result + ((isBedroom == null) ? 0 : isBedroom.hashCode());
		result = prime * result + ((temperatura == null) ? 0 : temperatura.hashCode());
		result = prime * result + ((idPlaca == null) ? 0 : idPlaca.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
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
		
		SensorEntity other = (SensorEntity) obj;
		
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
		
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		
		if (idGroup == null) {
			if (other.idGroup != null)
				return false;
		} else if (!idGroup.equals(other.idGroup))
			return false;
		
		
		return true;
		
		
	}

}
