package ca.concordia.encs.webservice.resources;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import ca.concordia.encs.data.DataManager;
import ca.concordia.encs.resources.qos.Session;
import ca.concordia.encs.resources.qos.SessionRequestResult;
import ca.concordia.encs.services.QoSManager;
import ca.concordia.encs.webservice.application.URLBuilder;

public class SessionsResource extends ServerResource {

	@Post
	// ("form:json|xml")
	public SessionRequestResult Store(Session session) {

		SessionRequestResult result = null;
		if (session != null) {
			result = QoSManager.getInstance().servePost(session);
			if (result.isSuccess()) {
				session.setSessionId(result.getSessionId());
				DataManager.addSession(session);
				this.setStatus(Status.SUCCESS_CREATED);
				this.setLocationRef(URLBuilder.getSessionURL(result
						.getSessionId()));
			}
		} else {

			this.setStatus(Status.CLIENT_ERROR_PRECONDITION_FAILED);
		}

		return result;

	}

}
