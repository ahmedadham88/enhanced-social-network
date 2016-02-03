package ca.concordia.encs.resources.qos;

import java.io.Serializable;

import ca.concordia.encs.common.*;
import ca.concordia.encs.constants.EventNotificationType;

public class Session implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String applicationId = "";

	private ConnectionParty owner = null;

	private ConnectionParty otherParty = null;

	private ServiceInfo serviceInfo = null;
	// TODO :Should be removed
	private String sessionId = "";

	private EventNotificationType[] eventNotificationsType = null;

	private String notificationUrl = "";

	private String sessionTerminationUrl = "";
	public Session(){
		
	}
	/**
	 * @param applicationId
	 * @param source
	 * @param destination
	 * @param serviceInfo
	 */
	public Session(String applicationId, ConnectionParty owner,
			ConnectionParty otherParty, ServiceInfo serviceInfo) {
		this.applicationId = applicationId;
		this.owner = owner;
		this.otherParty = otherParty;
		this.serviceInfo = serviceInfo;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sesstionId) {
		this.sessionId = sesstionId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public ConnectionParty getSessionOwner() {
		return owner;
	}

	public void setSessionOwner(ConnectionParty owner) {
		this.owner = owner;
	}

	public ConnectionParty getSessionOtherParty() {
		return otherParty;
	}

	public void setSessionOtherParty(ConnectionParty otherParty) {
		this.otherParty = otherParty;
	}

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public String getNotificationUrl() {
		return notificationUrl;
	}

	public void setNotificationUrl(String notificationUrl) {
		this.notificationUrl = notificationUrl;
	}

	public EventNotificationType[] getEventNotificationsType() {
		return eventNotificationsType;
	}

	public void setEventNotificationsType(
			EventNotificationType[] eventNotificationsType) {
		this.eventNotificationsType = eventNotificationsType;
	}

	public String getSessionTerminationUrl() {
		return sessionTerminationUrl;
	}

	public void setSessionTerminationUrl(String sessionTerminationUrl) {
		this.sessionTerminationUrl = sessionTerminationUrl;
	}

}
