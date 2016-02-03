package ca.concordia.encs.webservice.resources;

import org.restlet.data.Status;
import org.restlet.resource.*;

import ca.concordia.encs.data.DataManager;
import ca.concordia.encs.resources.qos.Session;
import ca.concordia.encs.resources.qos.SessionRequestResult;
import ca.concordia.encs.services.QoSManager;
import ca.concordia.encs.util.Utilities;

public class SessionResource extends ServerResource {

	private String sessionId;

	@Override
	protected void doInit() throws ResourceException {
		// Get the "itemName" attribute value taken from the URI template
		// /items/{itemName}.
		this.sessionId = (String) getRequest().getAttributes().get("sessionId");
		this.setExisting(this.sessionId != null && !this.sessionId.isEmpty());
		System.out.println("doInit : sessionId = " + sessionId);
	}

	@Get
	public Session Retrieve() {
		Session session = null;
		if (!Utilities.isNullOrEmpty(this.sessionId)
				|| (session = DataManager.getSession(this.sessionId)) == null) {

			setStatus(Status.SUCCESS_OK);

		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}

		this.commit();
		return session;
	}

	@Put
	public SessionRequestResult Update(Session session) {

		SessionRequestResult result = null;

		// Check if the id exists
		if (!Utilities.isNullOrEmpty(this.sessionId)
				|| DataManager.getSession(session.getSessionId()) == null) {

			// TODO:Check if the session object is valid
			if (session != null) {

				session.setSessionId(sessionId);
				result = QoSManager.getInstance().servePut(session);
				if (result.isSuccess()) {
					DataManager.updateSession(session);
				}

			} else {
				// session object is not valid
				this.setStatus(Status.CLIENT_ERROR_PRECONDITION_FAILED);
			}
			// request has been processed
			this.setStatus(Status.SUCCESS_OK);
		} else {
			this.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}

		this.commit();
		return result;

	}

	// @Post
	// // ("form:json|xml")

	@Delete
	public void Remove() {

		if (!Utilities.isNullOrEmpty(this.sessionId)
				|| DataManager.getSession(this.sessionId) != null) {
			DataManager.removeSession(this.sessionId);

			QoSManager.getInstance().serveRemove(sessionId);
			// Tells the client that the request has been successfully
			// fulfilled.
			setStatus(Status.SUCCESS_OK);

		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}
		this.commit();
	}
}
