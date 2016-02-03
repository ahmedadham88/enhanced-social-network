package ca.concordia.encs.resources.qos;

import java.util.Date;

import ca.concordia.encs.constants.EventNotificationType;

public class EventNotification {

	private String sessionId;
	private Date notificationDate;
	private EventInformation[] events;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}

	public EventInformation[] getEvents() {
		return events;
	}

	public void setEvents(EventInformation[] events) {
		this.events = events;
	}

}
