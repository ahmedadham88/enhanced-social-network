package ca.concordia.encs.resources.qos;

import java.io.Serializable;

public class SessionRequestResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Has value only if accepted
	private String sessionId;
	private int status;
	private String message;
	private AcceptableService acceptableService;

	public SessionRequestResult(){
		
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AcceptableService getAcceptableService() {
		return acceptableService;
	}

	public void setAcceptableService(AcceptableService acceptableService) {
		this.acceptableService = acceptableService;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Boolean isSuccess() {
		return status == SessionRequestStatus.ACCEPTED
				|| status == SessionRequestStatus.ACCEPTED_WITH_MODIFICATION;
	}
}
