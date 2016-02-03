package ca.concordia.encs.resources.qos;

import ca.concordia.encs.constants.EventNotificationType;

public class NotificationSubscription {

	private String applicationId;
	private EventNotificationType[] eventNotifications;
	private String notificationUrl;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public EventNotificationType[] getEventNotifications() {
		return eventNotifications;
	}

	public void setEventNotifications(EventNotificationType[] eventNotifications) {
		this.eventNotifications = eventNotifications;
	}

	public String getNotificationUrl() {
		return notificationUrl;
	}

	public void setNotificationUrl(String notificationUrl) {
		this.notificationUrl = notificationUrl;
	}

}
