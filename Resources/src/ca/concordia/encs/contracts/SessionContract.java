package ca.concordia.encs.contracts;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import ca.concordia.encs.resources.qos.*;

public interface SessionContract {

	@Get
	public Session Retrieve(String sessionId);

	@Post
	public SessionRequestResult Store(Session newSession);

	@Put
	public SessionRequestResult Update(Session newSession);

}
