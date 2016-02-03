package ca.concordia.encs.resources.m2m;

public class AlarmNotificationSubscription {

	private String applicationId;
	private String callBackNotificationUrl;
	private M2MEvent[] events;
	
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getCallBackNotificationUrl() {
		return callBackNotificationUrl;
	}
	public void setCallBackNotificationUrl(String callBackNotificationUrl) {
		this.callBackNotificationUrl = callBackNotificationUrl;
	}
	public M2MEvent[] getEvents() {
		return events;
	}
	public void setEvents(M2MEvent[] events) {
		this.events = events;
	}
	
	public AlarmNotificationSubscription()
	{
		
		
	}
	/**
	 * @param applicationId
	 * @param callBackNotificationUrl
	 * @param events
	 */
	public AlarmNotificationSubscription(String applicationId,
			String callBackNotificationUrl, M2MEvent[] events) {
		this.applicationId = applicationId;
		this.callBackNotificationUrl = callBackNotificationUrl;
		this.events = events;
	}
	
	
}
