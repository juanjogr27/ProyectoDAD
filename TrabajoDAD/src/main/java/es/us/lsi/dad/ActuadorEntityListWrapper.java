package es.us.lsi.dad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActuadorEntityListWrapper {
	private List<ActuadorEntity> actuatorList;

	public ActuadorEntityListWrapper() {
		super();
	}

	public ActuadorEntityListWrapper(Collection<ActuadorEntity> actuatorList) {
		super();
		this.actuatorList = new ArrayList<ActuadorEntity>(actuatorList);
	}
	
	public ActuadorEntityListWrapper(List<ActuadorEntity> actuatorList) {
		super();
		this.actuatorList = new ArrayList<ActuadorEntity>(actuatorList);
	}

	public List<ActuadorEntity> getactuatorList() {
		return actuatorList;
	}

	public void setactuatorList(List<ActuadorEntity> actuatorList) {
		this.actuatorList = actuatorList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actuatorList == null) ? 0 : actuatorList.hashCode());
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
		ActuadorEntityListWrapper other = (ActuadorEntityListWrapper) obj;
		if (actuatorList == null) {
			if (other.actuatorList != null)
				return false;
		} else if (!actuatorList.equals(other.actuatorList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActuadorEntityListWrapper [actuatorList=" + actuatorList + "]";
	}

}
