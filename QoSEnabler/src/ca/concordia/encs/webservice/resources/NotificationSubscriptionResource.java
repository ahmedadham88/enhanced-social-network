package ca.concordia.encs.webservice.resources;

import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import ca.concordia.encs.data.DataManager;
import ca.concordia.encs.resources.qos.NotificationSubscription;
import ca.concordia.encs.util.Utilities;
import ca.concordia.encs.webservice.application.URLBuilder;

public class NotificationSubscriptionResource extends ServerResource {
	private String applicationId;

	@Override
	protected void doInit() throws ResourceException {
		// Get the "applicationId" attribute value taken from the URI template
		this.applicationId = (String) getRequest().getAttributes().get(
				"applicationId");
		this.setExisting(this.applicationId != null
				&& !this.applicationId.isEmpty());
		System.out.println("doInit : applicationId = " + applicationId);
	}

	@Get
	public NotificationSubscription Retrieve() {
		NotificationSubscription subscription = null;
		if (!Utilities.isNullOrEmpty(this.applicationId)
				|| (subscription = DataManager
						.getNotificationSubscription(this.applicationId)) == null) {

			setStatus(Status.SUCCESS_OK);

		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}

		this.commit();
		return subscription;
	}

	@Put
	public String Update(NotificationSubscription subscription) {
		String url = "";
		if (subscription != null
				&& !Utilities.isNullOrEmpty(subscription.getApplicationId())) {

			if (DataManager.getNotificationSubscription(subscription
					.getApplicationId()) != null) {
				DataManager.updateNotificationSubscription(subscription);
				url = URLBuilder.getNotificationSubscriptionURL(subscription
						.getApplicationId());
				this.setStatus(Status.SUCCESS_OK);
			} else {
				this.setStatus(Status.CLIENT_ERROR_FORBIDDEN);

			}
		} else {

			this.setStatus(Status.CLIENT_ERROR_PRECONDITION_FAILED);
		}

		this.commit();

		return url;

	}

	@Delete
	public void Remove() {

		if (!Utilities.isNullOrEmpty(this.applicationId)
				|| DataManager.getNotificationSubscription(this.applicationId) != null) {
			DataManager.removeNotificationSubscription(this.applicationId);
			setStatus(Status.SUCCESS_OK);

		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}
		this.commit();
	}

}
