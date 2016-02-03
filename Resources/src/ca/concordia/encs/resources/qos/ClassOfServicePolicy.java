package ca.concordia.encs.resources.qos;

public class ClassOfServicePolicy {

	String applicationId;
	ClassOfService classOfService;
	int maximuimNumberOfSessions;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public ClassOfService getClassOfService() {
		return classOfService;
	}

	public void setClassOfService(ClassOfService classOfService) {
		this.classOfService = classOfService;
	}

	public int getMaximuimNumberOfSessions() {
		return maximuimNumberOfSessions;
	}

	public void setMaximuimNumberOfSessions(int maximuimNumberOfSessions) {
		this.maximuimNumberOfSessions = maximuimNumberOfSessions;
	}

}
