package ca.concordia.encs.resources.m2m;

import java.io.Serializable;

public class M2MEventNotification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Device sourceDevice;
	private String time;
	private M2MEvent event;

	public Device getSourceDevice() {
		return sourceDevice;
	}

	public void setDeviceId(Device sourceDevice) {
		this.sourceDevice = sourceDevice;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public M2MEvent getEvent() {
		return event;
	}

	public void setEvent(M2MEvent event) {
		this.event = event;
	}

}
