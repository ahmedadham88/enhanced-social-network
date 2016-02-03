package ca.concordia.encs.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.restlet.resource.ClientResource;

import ca.concordia.encs.constants.EventNotificationType;
import ca.concordia.encs.data.DataManager;
import ca.concordia.encs.diameter.base.constants.IMessageCode;
import ca.concordia.encs.diameter.rx.avp.SpecificAction;
import ca.concordia.encs.resources.qos.EventInformation;
import ca.concordia.encs.resources.qos.EventNotification;
import ca.concordia.encs.resources.qos.NotificationSubscription;
import ca.concordia.encs.resources.qos.Session;
import ca.concordia.encs.util.Utilities;
import ca.concordia.encs.webservice.resources.SessionsResource;
import de.fhg.fokus.diameter.DiameterPeer.EventListener;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;

public class NotificationManger {
	protected final Logger LOGGER = Logger.getLogger(NotificationManger.class);
	private static NotificationManger instance;

	public NotificationManger() {
		if (instance == null)
			instance = this;

	}

	public static NotificationManger getInstance() {

		if (instance == null)
			instance = new NotificationManger();

		return instance;
	}

	void sendNotificationIfNeeded(Session session, List<EventInformation> events) {

		LOGGER.info("sendNotificationIfNeeded called");
		if (session == null || events == null || events.size() == 0)
			return;

		String notificationUrl = "";
		EventNotificationType[] ptofileEvents = null;

		// Get the notification subscription , session or application based
		if (!Utilities.isNullOrEmpty(session.getNotificationUrl())
				&& session.getEventNotificationsType() != null
				&& session.getEventNotificationsType().length > 0) {

			LOGGER.debug("session based notification");
			notificationUrl = session.getNotificationUrl();
			ptofileEvents = session.getEventNotificationsType();

		} else {

			NotificationSubscription subscription = DataManager
					.getNotificationSubscription(session.getApplicationId());
			if (subscription != null
					&& !Utilities.isNullOrEmpty(subscription
							.getNotificationUrl())
					&& subscription.getEventNotifications() != null
					&& subscription.getEventNotifications().length > 0) {

				LOGGER.debug("application based notification");
				notificationUrl = subscription.getNotificationUrl();
				ptofileEvents = subscription.getEventNotifications();
			} else {
				LOGGER.debug("User does not have  notificatiob subscription.");
			}

		}
//
//		// Check if the user is subscribed in these notification
//		if (ptofileEvents != null && !Utilities.isNullOrEmpty(notificationUrl)) {
//			List<EventInformation> eventsInSubscription = new ArrayList<EventInformation>();
//			for (EventInformation eventInfo : events) {
//				for (EventNotificationType ptofileEvent : ptofileEvents) {
//					if (eventInfo.getEvent() == ptofileEvent) {
//						eventsInSubscription.add(eventInfo);
//						break;
//					}
//				}
//			}
//			if (!eventsInSubscription.isEmpty()) {
//
//				EventNotification notification = new EventNotification();
//				EventInformation[] eventsInfo = new EventInformation[eventsInSubscription
//						.size()];
//				for (int i = 0; i < eventsInSubscription.size(); i++) {
//					eventsInfo[i] = eventsInSubscription.get(i);
//				}
//				notification.setEvents(eventsInfo);
//				LOGGER.debug("Send notification to user using the notification url = "
//						+ notificationUrl);
//				// send notification
//				ClientResource client = null;
//				try {
//
//					client = new ClientResource(notificationUrl);
//					//client.post(notification, EventNotification.class);
//
//				} catch (Exception e) {
//					// TODO: handle exception
//				} finally {
//					client.release();
//				}
//
//			} else {
//				LOGGER.debug("It seems that user is not subscribed in the fired events");
//			}
//		}
	}
}
