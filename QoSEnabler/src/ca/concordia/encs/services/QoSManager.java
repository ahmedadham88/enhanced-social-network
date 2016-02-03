package ca.concordia.encs.services;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.apache.log4j.Logger;
import org.restlet.resource.ClientResource;

import util.UtilHelper;
import ca.concordia.encs.constants.EventNotificationType;
import ca.concordia.encs.constants.MediaType;
import ca.concordia.encs.data.DataManager;
import ca.concordia.encs.diameter.base.avp.*;
import ca.concordia.encs.diameter.base.constants.IMessageCode;
import ca.concordia.encs.diameter.rx.avp.AbortCause;
import ca.concordia.encs.diameter.rx.avp.IPCANType;
import ca.concordia.encs.diameter.rx.avp.RATType;
import ca.concordia.encs.diameter.rx.avp.SpecificAction;
import ca.concordia.encs.diameter.rx.messages.RxASAnswer;
import ca.concordia.encs.resources.qos.EventInformation;
import ca.concordia.encs.resources.qos.Session;
import ca.concordia.encs.resources.qos.SessionRequestResult;
import ca.concordia.encs.resources.qos.SessionRequestStatus;
import ca.concordia.encs.util.Utilities;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;

public final class QoSManager {
	
	
	private static final Logger LOGGER = Logger.getLogger(QoSManager.class);

	private static QoSManager instance;

	public static QoSManager getInstance() {

		if (instance == null)
			instance = new QoSManager();

		return instance;
	}

	public SessionRequestResult servePost(Session newSession)
			throws InvalidParameterException {

		if (!PolicyManager.getInstance().AuthorizeSession(newSession)) {

			SessionRequestResult result = new SessionRequestResult();
			result.setStatus(SessionRequestStatus.REJECTED_EXCEED_CLASS_OF_SERVICE_QUOTA);
			return result;
		}
		//////////////////////Differentiated QoS Algorithm///////////////////////
		java.util.Date date= new java.util.Date();
		//System.out.println("The Start Time (2): "+new Timestamp(date.getTime()));
		
		
		int total_bandwidth = 10000000;//100Mbps
		int gold_bandwidth = 160000; //160Kbps
		int silver_bandwidth = 80000;//80Kbps
		int bronze_bandwidth = 40000;//40Kbps
		int number_of_sessions = 0;
		int session_gold = 0;
		int session_silver = 0;
		int session_bronze = 0;
	/*	int sessions_created_gold=0;
		int sessions_terminated_gold=0;
		int sessions_rejected_gold=0;
		int sessions_created_silver=0;
		int sessions_terminated_silver=0;
		int sessions_rejected_silver=0;
		int sessions_created_bronze=0;
		int sessions_terminated_bronze=0;
		int sessions_rejected_bronze=0;*/
		//check if there is available bandwidth for this upcoming session
		DatabaseConnector c = new DatabaseConnector();
		c.databaseConnect();
		int [] database_result = new int [4]; 
		int sessionPriority = newSession.getServiceInfo().getPriority();
		database_result = c.getSessionNumber();
		number_of_sessions = database_result [0];
		session_gold = database_result[1];
		session_silver = database_result[2];
		session_bronze = database_result[3];
		//Get current sessions created, sessions terminated, sessions rejected
	/*	int []statistics = new int[3];
		statistics = c.getStatistics(1);
		sessions_created_gold = statistics[0];
		sessions_terminated_gold=statistics[1];
		sessions_rejected_gold = statistics[2];
		statistics = c.getStatistics(2);
		sessions_created_silver = statistics[0];
		sessions_terminated_silver=statistics[1];
		sessions_rejected_silver = statistics[2];
		statistics = c.getStatistics(3);
		sessions_created_bronze = statistics[0];
		sessions_terminated_bronze=statistics[1];
		sessions_rejected_bronze = statistics[2]; */
		//Calculate available bandwidth
		int available_bandwidth = total_bandwidth - ((session_gold*gold_bandwidth) + (session_silver*silver_bandwidth) + (session_bronze*bronze_bandwidth));
		System.out.println("The available bandwidth: "+available_bandwidth);
	if((number_of_sessions!=0)&&(available_bandwidth<160000)){
		if(sessionPriority==1){//Gold
			//If there is available bandwidth, admit the session
			
				System.out.println("Number of Sessions: "+number_of_sessions+" Gold ONLY: "+session_gold);
				//int ratio = session_gold/number_of_sessions;
				Random randomGenerator = new Random();
			    int ratio = randomGenerator.nextInt(100);
			    
				if(ratio<10){
					//Reject the session
		//			sessions_rejected_gold++;
		//			c.updateStatistics(sessions_created_gold, sessions_terminated_gold, sessions_rejected_gold, 1);
					c.databaseDisconnect();
					SessionRequestResult result = new SessionRequestResult();
					result.setStatus(SessionRequestStatus.REJECTED);
					return result;
				}else{
					//Terminate Silver or Bronze to accept the Gold
					//int second_ratio = session_bronze/number_of_sessions;
					if(session_bronze>14){
						//Delete 4 bronze sessions
				/*		String [] sessions = new String [4];
						sessions = c.getSessionIDs(4, 3);
						sampleRemove(sessions[0]);
						sampleRemove(sessions[1]);
						sampleRemove(sessions[2]);
						sampleRemove(sessions[3]);*/
						number_of_sessions=number_of_sessions-4;
						session_bronze=session_bronze-4;
						//sessions_terminated_bronze--;
						c.updateSessionNumber(number_of_sessions,session_gold ,session_silver ,session_bronze);
					//	c.updateStatistics(sessions_created_bronze, sessions_terminated_bronze, sessions_rejected_bronze, 3);
					//	c.databaseDisconnect();
					}else{ if(session_silver>22){
						//Delete 2 silver sessions
						//String [] sessions = new String [2];
					/*	sessions = c.getSessionIDs(2, 2);
						sampleRemove(sessions[0]);
						sampleRemove(sessions[1]);*/
						number_of_sessions=number_of_sessions-2;
						session_silver=session_silver-2;
						c.updateSessionNumber(number_of_sessions,session_gold ,session_silver ,session_bronze);
					//	sessions_terminated_silver--;
					//	c.updateStatistics(sessions_created_silver, sessions_terminated_silver, sessions_rejected_silver, 2);
					//	c.databaseDisconnect();
					}else{
						//Reject the session
					//	sessions_rejected_gold++;
					//	c.updateStatistics(sessions_created_gold, sessions_terminated_gold, sessions_rejected_gold, 1);
						c.databaseDisconnect();
						SessionRequestResult result = new SessionRequestResult();
						result.setStatus(SessionRequestStatus.REJECTED);
						return result;
					}
					}
				}
			}
		
		if(sessionPriority==2){//Silver
			//If there is available bandwidth, admit the session
			
				System.out.println("Number of Sessions: "+number_of_sessions+" Silver ONLY: "+session_silver);
				//int ratio = session_gold/number_of_sessions;
				Random randomGenerator = new Random();
			    int ratio = randomGenerator.nextInt(100);
			    
				if(ratio<40){
					//Reject the session
				//	sessions_rejected_silver++;
				//	c.updateStatistics(sessions_created_silver, sessions_terminated_silver, sessions_rejected_silver, 2);
					c.databaseDisconnect();
					SessionRequestResult result = new SessionRequestResult();
					result.setStatus(SessionRequestStatus.REJECTED);
					return result;
				}else{
					if(session_bronze>12){
						number_of_sessions=number_of_sessions-2;
						session_bronze=session_bronze-2;
					//	sessions_terminated_bronze--;
						c.updateSessionNumber(number_of_sessions,session_gold ,session_silver ,session_bronze);
					//	c.updateStatistics(sessions_created_bronze, sessions_terminated_bronze, sessions_rejected_bronze, 3);
						c.databaseDisconnect();
					}else{ 
						//Reject the session
					//	sessions_rejected_silver++;
					//	c.updateStatistics(sessions_created_silver, sessions_terminated_silver, sessions_rejected_silver, 2);
						c.databaseDisconnect();
						SessionRequestResult result = new SessionRequestResult();
						result.setStatus(SessionRequestStatus.REJECTED);
						return result;
					
					}
				}
			}
		
		if(sessionPriority==3){//Bronze
			//Reject the session
		//	sessions_rejected_bronze++;
		//	c.updateStatistics(sessions_created_bronze, sessions_terminated_bronze, sessions_rejected_bronze, 3);
			c.databaseDisconnect();
			SessionRequestResult result = new SessionRequestResult();
			result.setStatus(SessionRequestStatus.REJECTED);
			return result;
				
			}
			
		
	}
		////////////////////////////////////////////////////
		String ApplicationId = newSession.getServiceInfo().getServiceId();

		String user_SIP_URI = newSession.getSessionOwner().getSip_uri();
		if (!user_SIP_URI.startsWith("sip:"))
			user_SIP_URI = "sip:" + user_SIP_URI;

		String ownerIPAddress = newSession.getSessionOwner().getIp();
		long ownerPort = newSession.getSessionOwner().getPort();

		String otherPartyIPAddress = newSession.getSessionOtherParty().getIp();
		long otherPartyPort = newSession.getSessionOtherParty().getPort();

		long downloadBW = newSession.getServiceInfo().getDownloadBandwidth();
		if (downloadBW < 0)
			downloadBW = 0;

		long uploadBW = newSession.getServiceInfo().getUploadBandwidth();
		if (uploadBW < 0)
			uploadBW = 0;
		// Default priority for all requests
		int reservationPriority = newSession.getServiceInfo().getPriority();

		int lifeTime = newSession.getServiceInfo().getLifeTime();
		if (lifeTime < 0)
			lifeTime = 0;

		int mediaType = UtilHelper.mediaTypeToDiameterMediaType(newSession
				.getServiceInfo().getMediaType());

		// int[] specificActions = newSession.getSpecificActions();
		List<Integer> specificActions = new ArrayList<Integer>();
		int specificAction;
		if (newSession.getEventNotificationsType() != null
				&& newSession.getEventNotificationsType().length > 0) {

			for (EventNotificationType event : newSession
					.getEventNotificationsType()) {
				specificAction = UtilHelper
						.eventNotificationTypeToSpecifcAction(event);
				if (specificAction != -1) {
					specificActions.add(specificAction);
				}
			}
		}

		if ((downloadBW == 0 && uploadBW == 0) || mediaType == -1)
			throw new InvalidParameterException();
		
		SessionRequestResult result = RxInterface.newService(ApplicationId,
				user_SIP_URI, ownerIPAddress, ownerPort, otherPartyIPAddress,
				otherPartyPort, downloadBW, uploadBW, mediaType,
				reservationPriority, lifeTime, specificActions);
		
		
		/////////////////////Save session ID to database table sessions////////////
	/*	String sessionID = result.getSessionId();
		c.setSessionID(sessionID,reservationPriority);*/
		number_of_sessions++;
		if(reservationPriority==1){
		session_gold++;
	//	sessions_created_gold++;
	//	c.updateStatistics(sessions_created_gold, sessions_terminated_gold, sessions_rejected_gold, 1);
		}else{
		if(reservationPriority==2){
		session_silver++;
	//	sessions_created_silver++;
	//	c.updateStatistics(sessions_created_silver, sessions_terminated_silver, sessions_rejected_silver, 2);
		}else
		session_bronze++;
	//	sessions_created_bronze++;
	//	c.updateStatistics(sessions_created_bronze, sessions_terminated_bronze, sessions_rejected_bronze, 3);
		}
		System.out.println("Number of Sessions: "+number_of_sessions+" Gold: "+session_gold+" Silver "+session_silver+" Bronze "+session_bronze);
		c.updateSessionNumber(number_of_sessions,session_gold ,session_silver ,session_bronze);
		
		
		
		c.databaseDisconnect();
		///////////////////////////////////////////////////////////////////////
		// ... do something ...
		
		
		return result;

	}

	public SessionRequestResult servePut(Session newSession)
			throws InvalidParameterException {

		if (!PolicyManager.getInstance().AuthorizeSession(newSession)) {

			SessionRequestResult result = new SessionRequestResult();
			result.setStatus(SessionRequestStatus.REJECTED_EXCEED_CLASS_OF_SERVICE_QUOTA);
			return result;
		}
		
		SessionID sessionId = new SessionID(newSession.getSessionId());
		String ApplicationId = newSession.getServiceInfo().getServiceId();

		String user_SIP_URI = newSession.getSessionOwner().getSip_uri();
		if (!user_SIP_URI.startsWith("sip:"))
			user_SIP_URI = "sip:" + user_SIP_URI;

		String ownerIPAddress = newSession.getSessionOwner().getIp();
		long ownerPort = newSession.getSessionOwner().getPort();

		String otherPartyIPAddress = newSession.getSessionOtherParty().getIp();
		long otherPartyPort = newSession.getSessionOtherParty().getPort();

		long downloadBW = newSession.getServiceInfo().getDownloadBandwidth();
		if (downloadBW < 0)
			downloadBW = 0;

		long uploadBW = newSession.getServiceInfo().getUploadBandwidth();
		if (uploadBW < 0)
			uploadBW = 0;
		// Default priority for all requests
		int reservationPriority = ReservationPriority.DEFAULT;

		int lifeTime = newSession.getServiceInfo().getLifeTime();
		if (lifeTime < 0)
			lifeTime = 0;

		int mediaType = UtilHelper.mediaTypeToDiameterMediaType(newSession
				.getServiceInfo().getMediaType());

		// int[] specificActions = newSession.getSpecificActions();
		List<Integer> specificActions = new ArrayList<Integer>();
		int specificAction;
		if (newSession.getEventNotificationsType() != null
				&& newSession.getEventNotificationsType().length > 0) {

			for (EventNotificationType event : newSession
					.getEventNotificationsType()) {
				specificAction = UtilHelper
						.eventNotificationTypeToSpecifcAction(event);
				if (specificAction != -1) {
					specificActions.add(specificAction);
				}
			}
		}

		if ((downloadBW == 0 && uploadBW == 0) || mediaType == -1)
			throw new InvalidParameterException();

		SessionRequestResult result = RxInterface.updateService(sessionId,
				ApplicationId, user_SIP_URI, ownerIPAddress, ownerPort,
				otherPartyIPAddress, otherPartyPort, downloadBW, uploadBW,
				mediaType, reservationPriority, lifeTime, specificActions);

		return result;

	}
	
	public void sampleRemove (String sessionId){
		DatabaseConnector d = new DatabaseConnector();
		d.databaseConnect();
		d.removeSession(sessionId);
		int [] database_result = new int[4];
		database_result = d.getSessionNumber();
		int number_of_sessions = database_result [0];
		int session_gold = database_result[1];
		int session_silver = database_result[2];
		int session_bronze = database_result[3];
		number_of_sessions--;
		int priority = d.getPriority(sessionId);
		if(priority==1){
			session_gold--;
		}else{
			if(priority==2){
				session_silver--;
			}else{
				session_bronze--;
			}
		}
		d.updateSessionNumber(number_of_sessions,session_gold, session_silver, session_bronze);
		d.databaseDisconnect();
	}
	
	public void serveRemove(String sessionId) {

		RxInterface.terminateService(new SessionID(sessionId));
		///////////Remove the session from sessions and decrease session_number////////////////
	/*	DatabaseConnector d = new DatabaseConnector();
		d.databaseConnect();
		d.removeSession(sessionId);
		int [] database_result = new int[4];
		database_result = d.getSessionNumber();
		int number_of_sessions = database_result [0];
		int session_gold = database_result[1];
		int session_silver = database_result[2];
		int session_bronze = database_result[3];
		number_of_sessions--;
		int priority = d.getPriority(sessionId);
		if(priority==1){
			session_gold--;
		}else{
			if(priority==2){
				session_silver--;
			}else{
				session_bronze--;
			}
		}
		d.updateSessionNumber(number_of_sessions,session_gold, session_silver, session_bronze);
		d.databaseDisconnect();*/
		//////////////////////////////////////////////////////////////////////////////////////
	}

	public void processRAR(DiameterMessage message) {
		LOGGER.info("processRAR method called.\n");

		if (!(message != null && message.commandCode == IMessageCode.RA_CODE && message.flagRequest))
			return;

		SpecificAction specificAction = RxInterface.getSpecificAction(message);
		List<EventInformation> events = new ArrayList<EventInformation>();
		EventInformation event = null;
		if (specificAction != null) {
			event = new EventInformation();
			event.setEvent(UtilHelper
					.specifcActionToEventNotificationTypeTo(specificAction
							.getEnumerated()));
			if (specificAction.getEnumerated() == SpecificAction.IP_CAN_CHANGE) {

				IPCANType ipCANType = RxInterface.getIPCANType(message);
				if (ipCANType != null) {
					event.setDetail1(ipCANType.getName());

					RATType rateType = RxInterface.getRATType(message);
					if (rateType != null)
						event.setDetail2(rateType.getName());
				}

			}
			events.add(event);
		} else {
			LOGGER.debug("SpecificAction is null. \n");
		}

		AbortCause abortCause = RxInterface.getAbortCause(message);
		if (abortCause != null) {
			event = new EventInformation();
			event.setEvent(EventNotificationType.Session_Aborted);

			event.setDetail1(abortCause.getName());
		}

		String sessionId = RxInterface.getSessionId(message);
		// Send RAA
		RxInterface.sendRAA(sessionId);

		// Notify third party providers if needed
		Session session = DataManager.getSession(sessionId);
		if (session != null && events.size() > 0) {
			NotificationManger.getInstance().sendNotificationIfNeeded(session,
					events);
		}
	}

	public Boolean processASR(DiameterMessage message) {

		if (message != null && message.commandCode == IMessageCode.AS_CODE
				&& message.flagRequest)
			return false;

		// Send ASA
		List<EventInformation> events = new ArrayList<EventInformation>();
		EventInformation eventInfo = null;
		String sessionId = RxInterface.getSessionId(message);
		Session session = DataManager.getSession(sessionId);

		int resultCode = ResultCode.Diameter_UNABLE_TO_COMPLY;
		if (session == null) {
			resultCode = ResultCode.Diameter_UNKNOWN_SESSION_ID;
		} else {

			if (!Utilities.isNullOrEmpty(session.getSessionTerminationUrl())) {

				if (terminateServiceProviderSession(session
						.getSessionTerminationUrl())) {
					// failed to terminate the session
					resultCode = ResultCode.Diameter_UNABLE_TO_COMPLY;

				} else {
					resultCode = ResultCode.Diameter_SUCCESS;
					DataManager.removeSession(sessionId);

					eventInfo = new EventInformation();
					eventInfo.setEvent(EventNotificationType.Session_Aborted);
				}

			} else {
				// TODO:What should I do ? --> no url

			}

		}

		RxASAnswer asa = DiameterManager.getInstance().createRxASAnswer();
		asa.setResultCode(new ResultCode(resultCode));
		asa.setSessionId(new SessionID(sessionId));
		DiameterManager.getInstance().sendMessage(asa);

		// Send STR
		RxInterface.terminateService(new SessionID(sessionId));

		// Notify the service provider
		if (eventInfo != null && session != null) {
			events.add(eventInfo);
			NotificationManger.getInstance().sendNotificationIfNeeded(session,
					events);
		}

		return true;

	}

	private Boolean terminateServiceProviderSession(String terminationUrl) {

		LOGGER.info("terminateServiceProviderSession called\n");
		if (Utilities.isNullOrEmpty(terminationUrl))
			return false;

		ClientResource client = null;
		Boolean result = true;
		try {

			client = new ClientResource(terminationUrl);
			client.delete();

		} catch (Exception e) {
			result = false;
			LOGGER.error(e.getMessage());

		} finally {
			client.release();
		}

		return result;
	}

}
