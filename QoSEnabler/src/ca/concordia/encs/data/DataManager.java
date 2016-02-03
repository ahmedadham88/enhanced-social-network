package ca.concordia.encs.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ca.concordia.encs.resources.m2m.Device;
import ca.concordia.encs.resources.qos.ClassOfService;
import ca.concordia.encs.resources.qos.ClassOfServicePolicy;
import ca.concordia.encs.resources.qos.NotificationSubscription;
import ca.concordia.encs.resources.qos.Session;
import ca.concordia.encs.util.Utilities;

public class DataManager {

	private static final ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	private static final ConcurrentMap<String, NotificationSubscription> notificationSubscriptions = new ConcurrentHashMap<String, NotificationSubscription>();
	// private static final ConcurrentMap<String, ClassOfService>
	// classOfServices = new ConcurrentHashMap<String, ClassOfService>();
	private static final ArrayList<ClassOfServicePolicy> ClassOfServicePolicies = new ArrayList<ClassOfServicePolicy>();

	static {
		init();
	}

	private static void init() {

		ClassOfService c = new ClassOfService();
		c.setId("1");
		c.setEpcName("Surveillance Golden");
		c.setDisplayName("Surveillance Golden");

		ClassOfServicePolicy classOfServicePolicy = new ClassOfServicePolicy();
		classOfServicePolicy.setClassOfService(c);
		classOfServicePolicy.setApplicationId("surveillance");
		classOfServicePolicy.setMaximuimNumberOfSessions(2);

		DataManager.AddClassOfServicePolicy(classOfServicePolicy);

		/***********************************/
		c = new ClassOfService();
		c.setId("2");
		c.setEpcName("Surveillance Silver");
		c.setDisplayName("Surveillance Silver");

		classOfServicePolicy = new ClassOfServicePolicy();
		classOfServicePolicy.setClassOfService(c);
		classOfServicePolicy.setApplicationId("surveillance");
		classOfServicePolicy.setMaximuimNumberOfSessions(10);

		DataManager.AddClassOfServicePolicy(classOfServicePolicy);

		/***********************************/
		c = new ClassOfService();
		c.setId("3");
		c.setEpcName("Surveillance Bronze");
		c.setDisplayName("Surveillance Bronze");

		classOfServicePolicy = new ClassOfServicePolicy();
		classOfServicePolicy.setClassOfService(c);
		classOfServicePolicy.setApplicationId("surveillance");
		classOfServicePolicy.setMaximuimNumberOfSessions(10);

		DataManager.AddClassOfServicePolicy(classOfServicePolicy);

		/***********************************/
		c = new ClassOfService();
		c.setId("4");
		c.setEpcName("Webcamstream");
		c.setDisplayName("Web streaming");

		classOfServicePolicy = new ClassOfServicePolicy();
		classOfServicePolicy.setClassOfService(c);
		classOfServicePolicy.setApplicationId("surveillance");
		classOfServicePolicy.setMaximuimNumberOfSessions(10);

		DataManager.AddClassOfServicePolicy(classOfServicePolicy);

	}

	/********************************************************************/
	public static boolean AddClassOfServicePolicy(ClassOfServicePolicy c) {
		if (c != null) {
			ClassOfServicePolicies.add(c);
			return true;
		}
		return false;
	}

	public static ClassOfServicePolicy getClassOfServicePolicy(
			String applicationId, String classOfServiceId) {
		ArrayList<ClassOfServicePolicy> lst = new ArrayList<ClassOfServicePolicy>();

		for (ClassOfServicePolicy c : ClassOfServicePolicies) {

			if (c.getApplicationId().equals(applicationId)
					&& classOfServiceId.equals(c.getClassOfService()
							.getEpcName()))
				return c;
		}

		return null;
	}

	/********************************************************************/
	// public static boolean AddClassOfService(ClassOfService c) {
	// if (c != null && !classOfServices.containsKey(c.getId())) {
	// classOfServices.put(c.getId(), c);
	// return true;
	// }
	// return false;
	// }

	/********************************************************************/
	public static boolean addSession(Session session) {
		if (session != null && !Utilities.isNullOrEmpty(session.getSessionId())
				&& !sessions.containsKey(session.getSessionId())) {
			sessions.put(session.getSessionId(), session);
			return true;
		}
		return false;
	}

	public static boolean updateSession(Session session) {
		if (session != null && !Utilities.isNullOrEmpty(session.getSessionId())
				&& sessions.containsKey(session.getSessionId())) {
			sessions.put(session.getSessionId(), session);
			return true;
		}
		return false;
	}

	public static Session getSession(String sessionId) {
		if (!Utilities.isNullOrEmpty(sessionId)
				&& sessions.containsKey(sessionId)) {
			return sessions.get(sessionId);
		}
		return null;
	}

	public static ArrayList<Session> getSessions(String applicationId,
			String classOfServiceEPCName) {

		ArrayList<Session> sessionList = new ArrayList<Session>();
		if (!Utilities.isNullOrEmpty(applicationId)
				&& !Utilities.isNullOrEmpty(classOfServiceEPCName)) {

			Session session;
			Iterator<Session> s = sessions.values().iterator();
			while (s.hasNext()) {
				session = s.next();
				if (session.getApplicationId().equals(applicationId)
						&& session.getServiceInfo().getServiceId()
								.equals(classOfServiceEPCName)) {
					sessionList.add(session);
				}
			}
		}
		return sessionList;

	}

	public static ArrayList<Session> getAllSessions() {

		ArrayList<Session> sessionList = new ArrayList<Session>();

		Session session;
		Iterator<Session> s = sessions.values().iterator();
		while (s.hasNext()) {
			sessionList.add(s.next());
		}

		return sessionList;
	}

	public static Boolean removeSession(String sessionId) {
		if (!Utilities.isNullOrEmpty(sessionId)
				&& sessions.containsKey(sessionId)) {
			sessions.remove(sessionId);
			return true;
		}
		return false;
	}

	public static boolean addNotificationSubscription(
			NotificationSubscription Subscription) {
		if (Subscription != null
				&& !Utilities.isNullOrEmpty(Subscription.getApplicationId())
				&& !notificationSubscriptions.containsKey(Subscription
						.getApplicationId())) {

			notificationSubscriptions.put(Subscription.getApplicationId(),
					Subscription);
			return true;
		}
		return false;
	}

	public static boolean updateNotificationSubscription(
			NotificationSubscription Subscription) {
		if (Subscription != null
				&& !Utilities.isNullOrEmpty(Subscription.getApplicationId())
				&& sessions.containsKey(Subscription.getApplicationId())) {

			notificationSubscriptions.put(Subscription.getApplicationId(),
					Subscription);
			return true;
		}
		return false;
	}

	public static NotificationSubscription getNotificationSubscription(
			String applicationId) {
		if (!Utilities.isNullOrEmpty(applicationId)
				&& notificationSubscriptions.containsKey(applicationId)) {
			return notificationSubscriptions.get(applicationId);
		}
		return null;
	}

	public static Boolean removeNotificationSubscription(String applicationId) {
		if (!Utilities.isNullOrEmpty(applicationId)
				&& notificationSubscriptions.containsKey(applicationId)) {
			notificationSubscriptions.remove(applicationId);
			return true;
		}
		return false;
	}

}
