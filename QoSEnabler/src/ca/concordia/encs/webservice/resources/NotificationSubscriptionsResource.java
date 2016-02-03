package ca.concordia.encs.webservice.resources;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import ca.concordia.encs.data.DataManager;
import ca.concordia.encs.resources.qos.NotificationSubscription;
import ca.concordia.encs.util.Utilities;
import ca.concordia.encs.webservice.application.URLBuilder;

public class NotificationSubscriptionsResource extends ServerResource {

	@Post
	public String Store(NotificationSubscription subscription) {

		String url = "";
		if (subscription != null
				&& !Utilities.isNullOrEmpty(subscription.getApplicationId())) {

			// Same application can not subscribe twice
			if (DataManager.getNotificationSubscription(subscription
					.getApplicationId()) == null) {
				DataManager.addNotificationSubscription(subscription);
				url = URLBuilder.getNotificationSubscriptionURL(subscription
						.getApplicationId());
				this.setStatus(Status.SUCCESS_CREATED);
			} else {
				this.setStatus(Status.CLIENT_ERROR_FORBIDDEN);

			}
		} else {

			this.setStatus(Status.CLIENT_ERROR_PRECONDITION_FAILED);
		}

		this.commit();

		return url;
	}
}
