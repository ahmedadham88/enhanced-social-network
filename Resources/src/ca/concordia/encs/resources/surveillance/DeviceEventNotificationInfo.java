package ca.concordia.encs.resources.surveillance;

import java.io.Serializable;

import ca.concordia.encs.resources.m2m.M2MEvent;

public class DeviceEventNotificationInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String deviceId;
	private M2MEvent[] events;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public M2MEvent[] getEvents() {
		return events;
	}

	public void setEvents(M2MEvent[] events) {
		this.events = events;
	}

}
