package ca.concordia.encs.webservice.application;

public final class URLBuilder {

	public static String getSessionsURL() {
		return ApplicationConfiguration.getWebServiceResourcesAddress()
				+ "/sessions";
	}

	public static String getSessionURL() {
		return ApplicationConfiguration.getWebServiceResourcesAddress()
				+ "/sessions/{sessionId}";
	}

	public static String getSessionURL(String sessionId) {
		return ApplicationConfiguration.getWebServiceResourcesAddress()
				+ "/sessions/" + sessionId;
	}

	public static String getNotificationSubscriptionsURL() {
		return ApplicationConfiguration.getWebServiceResourcesAddress()
				+ "/notifications";
	}

	public static String getNotificationSubscriptionURL() {
		return ApplicationConfiguration.getWebServiceResourcesAddress()
				+ "/notifications/{applicationId}";
	}

	public static String getNotificationSubscriptionURL(String applicationId) {
		return ApplicationConfiguration.getWebServiceResourcesAddress()
				+ "/notifications/" + applicationId;
	}
}
