package ca.concordia.encs.resources.qos;

import ca.concordia.encs.constants.EventNotificationType;

public class EventInformation {

	private EventNotificationType event;
	private String description;
	private String detail1;
	private String detail2;

	public EventNotificationType getEvent() {
		return event;
	}

	public void setEvent(EventNotificationType event) {
		this.event = event;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail1() {
		return detail1;
	}

	public void setDetail1(String detail) {
		this.detail1 = detail;
	}

	public String getDetail2() {
		return detail2;
	}

	public void setDetail2(String detail) {
		this.detail1 = detail2;
	}

}
