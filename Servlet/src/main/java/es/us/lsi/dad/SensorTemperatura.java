package es.us.lsi.dad;

import java.util.Calendar;
import java.util.Objects;

public class SensorTemperatura {
	
	private Integer idSensor;
	private Long timeStamp;
	private Boolean isBedroom;
	private Integer temperatura;
	private Integer idPlaca;
	private Integer idGroup;
	
	public SensorTemperatura(Integer idSensor) {
		super();
		this.idSensor = idSensor;
	}


	public SensorTemperatura(Integer idSensor, Boolean isBedroom, Integer temperatura, Integer idPlaca,
			Integer idGroup) {
		super();
		this.idSensor = idSensor;
		this.timeStamp = Calendar.getInstance().getTimeInMillis();
		this.isBedroom = isBedroom;
		this.temperatura = temperatura;
		this.idPlaca = idPlaca;
		this.idGroup = idGroup;
	}



	public Integer getIdSensor() {
		return idSensor;
	}

	public void setIdSensor(Integer idSensor) {
		this.idSensor = idSensor;
	}

	public Long getFechaCalibracion() {
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
	public int hashCode() {
		return Objects.hash(idSensor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorTemperatura other = (SensorTemperatura) obj;
		return Objects.equals(idSensor, other.idSensor);
	}


	@Override
	public String toString() {
		return "SensorTemperatura [idSensor=" + idSensor + ", timeStamp=" + timeStamp + ", isBedroom=" + isBedroom
				+ ", temperatura=" + temperatura + ", idPlaca=" + idPlaca + ", idGroup=" + idGroup + "]";
	}


	


	


	

	

	
	
	
	
	
	

}
