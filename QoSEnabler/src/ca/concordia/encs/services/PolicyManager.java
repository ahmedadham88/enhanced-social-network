package ca.concordia.encs.services;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import sun.nio.cs.FastCharsetProvider;

import ca.concordia.encs.data.DataManager;
import ca.concordia.encs.resources.qos.ClassOfServicePolicy;
import ca.concordia.encs.resources.qos.Session;

public class PolicyManager {
	private static final Logger LOGGER = Logger.getLogger(PolicyManager.class);

	private static PolicyManager instance;
	private PolicyType policyType = PolicyType.BandWidth;
	private static long Maximum_Network_Capcity = 62000;

	public static PolicyManager getInstance() {

		if (instance == null)
			instance = new PolicyManager();

		return instance;
	}

	public boolean AuthorizeSession(Session newSession) {

		boolean result = false;
//		if (policyType == PolicyType.BandWidth) {
//			result = AuthorizeSessionBasedOnBandwidth(newSession);
//		} else if (policyType == PolicyType.Class_Of_Service_Qouta) {
//			result = AuthorizeSessionBasedOnQouta(newSession);
//		}
		result = true;
		return result;
	}

	public boolean AuthorizeSessionBasedOnBandwidth(Session newSession) {

		ArrayList<Session> sessions = DataManager.getAllSessions();
		Session oldSession = DataManager.getSession(newSession.getSessionId());

		boolean hasGold = false;
		int goldCnt = 0;
		for (Session s : sessions) {
			if (s.getServiceInfo().getServiceId().contains("Gold")) {
				hasGold = true;
				goldCnt++;
				// break;
			}
		}
		if (hasGold) {
			// We want to chage the session and we only have the golde session
			if (goldCnt >= 2 && newSession.getServiceInfo().getServiceId()
							.contains("Gold")) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
			// if (sessions.size() == 4) {
			// return false;
			// } else {
			// return true;
			// }

		}

		// We downgrade a session already exist , then accept it
		// if (oldSession != null
		// && getSessionBandwidth(oldSession) > getSessionBandwidth(newSession))
		// {
		// return true;
		// } else {
		//
		// long currentUsedBandwidth = 0;
		// for (Session s : sessions) {
		// currentUsedBandwidth += getSessionBandwidth(s);
		//
		// }
		// if (oldSession != null) {
		// //Remove old bandwidth
		// currentUsedBandwidth -= getSessionBandwidth(oldSession);
		// }
		//
		// if ((currentUsedBandwidth + getSessionBandwidth(newSession)) >
		// PolicyManager.Maximum_Network_Capcity) {
		// return false;
		// }
		//
		// }

		// return true;
	}

	// public boolean AuthorizeSessionBasedOnBandwidth(Session newSession) {
	//
	// ArrayList<Session> sessions = DataManager.getAllSessions();
	// Session oldSession = DataManager.getSession(newSession.getSessionId());
	//
	// boolean hasGold = false;
	// for (Session s : sessions) {
	// if (s.getServiceInfo().getServiceId().contains("Gold")) {
	// hasGold = true;
	// break;
	// }
	// }
	// if (hasGold) {
	// // We want to chage the session and we only have the golde session
	// if (oldSession != null || sessions.size() <= 2) {
	// return true;
	// } else {
	// return false;
	// }
	// } else {
	// if (sessions.size() == 4) {
	// return false;
	// } else {
	// return true;
	// }
	//
	// }
	//
	// // We downgrade a session already exist , then accept it
	// // if (oldSession != null
	// // && getSessionBandwidth(oldSession) > getSessionBandwidth(newSession))
	// // {
	// // return true;
	// // } else {
	// //
	// // long currentUsedBandwidth = 0;
	// // for (Session s : sessions) {
	// // currentUsedBandwidth += getSessionBandwidth(s);
	// //
	// // }
	// // if (oldSession != null) {
	// // //Remove old bandwidth
	// // currentUsedBandwidth -= getSessionBandwidth(oldSession);
	// // }
	// //
	// // if ((currentUsedBandwidth + getSessionBandwidth(newSession)) >
	// // PolicyManager.Maximum_Network_Capcity) {
	// // return false;
	// // }
	// //
	// // }
	//
	// // return true;
	// }

	public boolean AuthorizeSessionBasedOnQouta(Session newSession) {

		ClassOfServicePolicy policy = DataManager.getClassOfServicePolicy(
				newSession.getApplicationId(), newSession.getServiceInfo()
						.getServiceId());

		if (policy != null) {
			if (policy.getMaximuimNumberOfSessions() >= 0) {
				ArrayList<Session> sessions = DataManager.getSessions(
						newSession.getApplicationId(), newSession
								.getServiceInfo().getServiceId());

				if (sessions.size() >= policy.getMaximuimNumberOfSessions()) {
					return false;
				} else {
					return true;
				}

			}

		}
		return false;

	}

	private long getSessionBandwidth(Session newSession) {
		long bandwidth = 0;

		if (newSession != null) {
			bandwidth += newSession.getServiceInfo().getDownloadBandwidth();
			bandwidth += newSession.getServiceInfo().getUploadBandwidth();
		}

		return bandwidth;
	}

	private enum PolicyType {
		Class_Of_Service_Qouta, BandWidth
	}
}
