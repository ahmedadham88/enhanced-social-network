package ca.concordia.encs.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

//import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;

import ca.concordia.encs.diameter.base.avp.AuthorizatinLifeTime;
import ca.concordia.encs.diameter.base.avp.DiameterIdentity;
import ca.concordia.encs.diameter.base.avp.ErrorMessage;
import ca.concordia.encs.diameter.base.avp.FramedIPAddress;
import ca.concordia.encs.diameter.base.avp.ReservationPriority;
import ca.concordia.encs.diameter.base.avp.ResultCode;
import ca.concordia.encs.diameter.base.avp.SessionID;
import ca.concordia.encs.diameter.base.avp.SubscriptionId;
import ca.concordia.encs.diameter.base.avp.SubscriptionIdData;
import ca.concordia.encs.diameter.base.avp.SubscriptionIdType;
import ca.concordia.encs.diameter.base.avp.TerminationCause;
import ca.concordia.encs.diameter.base.constants.IMessageCode;
import ca.concordia.encs.diameter.base.constants.Vendors;
import ca.concordia.encs.diameter.base.messages.DiameterAnswer;
import ca.concordia.encs.diameter.rx.avp.*;
import ca.concordia.encs.diameter.rx.messages.RxAARequest;
import ca.concordia.encs.diameter.rx.messages.RxRAAnswer;
import ca.concordia.encs.diameter.rx.messages.RxSTRequest;
import ca.concordia.encs.resources.qos.AcceptableService;
import ca.concordia.encs.resources.qos.SessionRequestResult;
import ca.concordia.encs.resources.qos.SessionRequestStatus;
import ca.concordia.encs.util.Utilities;
import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.EventListener;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;

public final class RxInterface {

	protected static final Logger LOGGER = Logger.getLogger(RxInterface.class);

	public static SessionRequestResult newService(String ApplicationId,
			String owner_SIP_URI, String ownerIPAddress, long srcPort,
			String otherPartyIPAddress, long otherPartyPort, long downloadBW,
			long uploadBW, int mediaType, int reservationPriority,
			int lifeTime, List<Integer> specificActions) {

		LOGGER.info("newService method called.");

		RxAARequest aar = DiameterManager.getInstance().createRxAARequest();

		aar.setAFApplicationIdentifier(new AFApplicationIdentifier(
				ApplicationId));
		aar.addSubscriptionId(new SubscriptionId(new SubscriptionIdType(
				SubscriptionIdType.END_USER_SIP_URI), new SubscriptionIdData(
				owner_SIP_URI)));

//		if (!Utilities.isNullOrEmpty(ownerIPAddress))
//			aar.setFramedIPAddress(new FramedIPAddress(ownerIPAddress));
//		else {
//			// If the IP for the owner is unknown , this the owner should have
//			// the same IP exists in the DB
//			// Otherwise the session will not be created
//			LOGGER.warn("The IP address of the session owner is unknown.");
//		}

		aar.setServiceInfoStatus(new ServiceInfoStatus(
				ServiceInfoStatus.FINAL_SERVICE_INFORMATION));

		SessionID sessionId = new SessionID(new DiameterIdentity(
		// DiameterManager.getInstance().getDestinationHost()),
				"mdf-rx.openepc.test"), owner_SIP_URI);
		aar.setSessionId(sessionId);

		aar.addMediaComponentDescription(createMediaComponentDescription(1L,
				ownerIPAddress, srcPort, otherPartyIPAddress, otherPartyPort,
				downloadBW, uploadBW, mediaType));

		if (reservationPriority > 0)
			aar.setReservationPriority(new ReservationPriority(
					reservationPriority));

		if (lifeTime > 0)
			aar.setAuthorizatinLifeTime(new AuthorizatinLifeTime(lifeTime));

		// Add the specific Actions(events)
		// TODO: This should be fixed ,no validation for the real value for
		// specific action
		if (specificActions != null && specificActions.size() > 0) {
			for (int specificAction : specificActions)
				aar.addSpecificAction(new SpecificAction(specificAction));
		}

		DiameterMessage responseMessage = DiameterManager.getInstance()
				.sendBlocking(aar);

		SessionRequestResult result = new SessionRequestResult();
		if (responseMessage != null && !responseMessage.flagRequest) {
			ResultCode resultCode = getResultCode(responseMessage);

			AcceptableServiceInfo acceptableServiceInfo = getAcceptableServiceInfo(responseMessage);
			if (acceptableServiceInfo != null) {
				AcceptableService acceptableService = new AcceptableService();

				MaxRequestedBandwidthDL dl = acceptableServiceInfo
						.getMaxRequestedBandwidthDL();
				if (dl != null)
					acceptableService.setMaxRequestedBandwidthDL(dl
							.getUnsigned32());

				MaxRequestedBandwidthUL ul = acceptableServiceInfo
						.getMaxRequestedBandwidthUL();
				if (ul != null)
					acceptableService.setMaxRequestedBandwidthUL(ul
							.getUnsigned32());

				result.setAcceptableService(acceptableService);
			}

			if (resultCode != null && resultCode.isSuccess()) {
				LOGGER.debug("Creating new session succeeded.");

				AVP avp = responseMessage.findAVP(SessionID.AVP_CODE);
				if (avp != null) {
					result.setSessionId(new String(avp.getData()));
					LOGGER.debug(" returned session id is : "
							+ result.getSessionId());
					String QOS = "";
					if (reservationPriority==1)
						QOS = "GOLD";
					else if(reservationPriority==2)
						QOS = "SILVER";
					else
						QOS = "BRONZE";
					LOGGER.debug(" session subscription is : "
							+ QOS + " and session priority is "+ reservationPriority);
				}

				if (acceptableServiceInfo != null) {
					result.setStatus(SessionRequestStatus.ACCEPTED_WITH_MODIFICATION);
				} else {
					result.setStatus(SessionRequestStatus.ACCEPTED);
				}

			}

			else {

				if (acceptableServiceInfo != null) {
					result.setStatus(SessionRequestStatus.REJECTED_WITH_OFFER);
				} else {
					result.setStatus(SessionRequestStatus.REJECTED);
				}

				// TODO : more cases here , it may not be fail
				String error = getErrorMessage(responseMessage);
				if (error != null && error.length() > 0) {
					LOGGER.debug("Received error message is " + error);
					result.setMessage(error);
				} else {
					// TODO:Need more improvement , you may add a new item to
					// SessionRequestResult or at least add the name of code
					// instead of value
					ExperimentalResultCode experimentalResultCode = getExperimentalResultCode(responseMessage);
					if (experimentalResultCode != null) {
						result.setMessage(Long.toString(experimentalResultCode
								.getUnsigned32()));
					}
				}

				LOGGER.debug("Creating new session failed.");
			}

		}

		// DiameterAnswer danswer = null;
		// if(dma == null){
		// LOGGER.error("The response is null !!");
		// return -1;
		// }
		// System.out.println("answer command code is : " + dma.commandCode);
		// //System.out.println("esponse.getResultCode().getIntData() is : " +
		// dma.getResultCode().getIntData());
		// ResultCode a = (ResultCode)dma.findAVP(ResultCode.AVP_CODE);
		// if (dma instanceof DiameterAnswer) {
		// DiameterAnswer ret = (DiameterAnswer) dma;
		// response = ret;
		// }else {
		// LOGGER.error("The response is not an  DiameterAnswer !!");
		// return -1;
		// }
		//
		// LOGGER.debug("DiameterAnswer is : "+dma.toString());
		// System.out.println("answer command code is : " + dma.commandCode);
		// // if(!response.getResultCode().isSuccess())
		// // {
		// //// LOGGER.debug(TestName()+
		// " Did not Receive 2001 as Result code!");
		// // }
		// //response.getFailedAVP();
		// System.out.println("esponse.getResultCode().getIntData() is : " +
		// response.getResultCode().getIntData());
		// return response.getResultCode().getIntData();

		//
		//
		// //LOGGER.info(TestName()+" send AAR!");
		// if(diameterProcess.sendBlocking(aar, danswer)!=2001)
		// {
		// //return false;
		// }
		// if(danswer != null)
		// System.out.println("answer command code is : " +
		// danswer.commandCode);
		//
		//
		//
		// return aar.getSessionId().toString();
		return result;
	}

	public static SessionRequestResult updateService(SessionID sessionId,
			String ApplicationId, String user_SIP_URI, String srcIPAddress,
			long srcPort, String desIPAddress, long destPort, long downloadBW,
			long uploadBW, int mediaType, int reservationPriority,
			int lifeTime, List<Integer> specificActions)  {

		LOGGER.debug("updateService method called.");

		RxAARequest aar = DiameterManager.getInstance().createRxAARequest();

		aar.setAFApplicationIdentifier(new AFApplicationIdentifier(
				ApplicationId));
		aar.addSubscriptionId(new SubscriptionId(new SubscriptionIdType(
				SubscriptionIdType.END_USER_SIP_URI), new SubscriptionIdData(
				user_SIP_URI)));

		if (!Utilities.isNullOrEmpty(srcIPAddress))
			aar.setFramedIPAddress(new FramedIPAddress(srcIPAddress));
		else {
			// If the IP for the owner is unknown , this the owner should have
			// the same IP exists in the DB
			// Otherwise the session will not be created
			LOGGER.warn("The IP address of the session owner is unknown.");
		}

		aar.setServiceInfoStatus(new ServiceInfoStatus(
				ServiceInfoStatus.FINAL_SERVICE_INFORMATION));

		aar.setSessionId(sessionId);

		aar.addMediaComponentDescription(createMediaComponentDescription(1L,
				srcIPAddress, srcPort, desIPAddress, destPort, downloadBW,
				uploadBW, MediaType.VIDEO));

		if (reservationPriority > 0)
			aar.setReservationPriority(new ReservationPriority(
					reservationPriority));

		if (lifeTime > 0)
			aar.setAuthorizatinLifeTime(new AuthorizatinLifeTime(lifeTime));

		// Add the specific Actions(events)
		// TODO: This should be fixed ,no validation for the real value for
		// specific action
		if (specificActions != null && specificActions.size() > 0) {
			for (int specificAction : specificActions)
				aar.addSpecificAction(new SpecificAction(specificAction));
		}

		DiameterMessage responseMessage = DiameterManager.getInstance()
				.sendBlocking(aar);

		SessionRequestResult result = new SessionRequestResult();
		if (responseMessage != null && !responseMessage.flagRequest) {
			ResultCode resultCode = getResultCode(responseMessage);

			AcceptableServiceInfo acceptableServiceInfo = getAcceptableServiceInfo(responseMessage);
			if (acceptableServiceInfo != null) {
				AcceptableService acceptableService = new AcceptableService();

				MaxRequestedBandwidthDL dl = acceptableServiceInfo
						.getMaxRequestedBandwidthDL();
				if (dl != null)
					acceptableService.setMaxRequestedBandwidthDL(dl
							.getUnsigned32());

				MaxRequestedBandwidthUL ul = acceptableServiceInfo
						.getMaxRequestedBandwidthUL();
				if (ul != null)
					acceptableService.setMaxRequestedBandwidthUL(ul
							.getUnsigned32());

				result.setAcceptableService(acceptableService);
			}

			if (resultCode != null && resultCode.isSuccess()) {
				LOGGER.debug("Updating session succeeded.");

				AVP avp = responseMessage.findAVP(SessionID.AVP_CODE);
				if (avp != null) {
					result.setSessionId(new String(avp.getData()));
					LOGGER.debug(" returned session id is : "
							+ result.getSessionId());
			//		Write(result.getSessionId());
				}

				if (acceptableServiceInfo != null) {
					result.setStatus(SessionRequestStatus.ACCEPTED_WITH_MODIFICATION);
				} else {
					result.setStatus(SessionRequestStatus.ACCEPTED);
				}

			}

			else {

				if (acceptableServiceInfo != null) {
					result.setStatus(SessionRequestStatus.REJECTED_WITH_OFFER);
				} else {
					result.setStatus(SessionRequestStatus.REJECTED);
				}

				// TODO : more cases here , it may not be fail
				String error = getErrorMessage(responseMessage);
				if (error != null && error.length() > 0) {
					LOGGER.debug("Received error message is " + error);
					result.setMessage(error);
				} else {
					// TODO:Need more improvement , you may add a new item to
					// SessionRequestResult or at least add the name of code
					// instead of value
					ExperimentalResultCode experimentalResultCode = getExperimentalResultCode(responseMessage);
					if (experimentalResultCode != null) {
						result.setMessage(Long.toString(experimentalResultCode
								.getUnsigned32()));
					}
				}

				LOGGER.debug("Updating session failed.");
			}

		}

		return result;
	}
	/*
	public static void Write(String f) throws IOException{
		   File file = new File("/root/Desktop/ZING/sessionid.txt");
		   String content = f;
		   
			FileOutputStream fop = new FileOutputStream(file);

				// if file doesn't exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				// get the content in bytes
				byte[] contentInBytes = content.getBytes();

				fop.write(contentInBytes);
				fop.flush();
				fop.close();

				System.out.println("Done writing the new file");
	   }
*/
	public static Boolean terminateService(SessionID sessionID) {

		LOGGER.debug("terminateService method called \n session id is "
				+ sessionID.getData().toString());

		Boolean result = false;
		// create STR message
		RxSTRequest str = DiameterManager.getInstance().createRxSTRequest();
		str.setSessionId(sessionID);
		str.setTerminationCause(new TerminationCause(
				TerminationCause.DIAMETER_LOGOUT));

		// Send message
		DiameterMessage responseMessage = DiameterManager.getInstance()
				.sendBlocking(str);

		if (responseMessage != null && !responseMessage.flagRequest) {
			LOGGER.debug("The command code of answer is : "
					+ responseMessage.commandCode);

			ResultCode resultCode = getResultCode(responseMessage);
			if (resultCode.isSuccess()) {
				LOGGER.debug("Session termination succeeded.");
				result = true;
			} else {
				// TODO : more cases here , it may not be fail
				String error = getErrorMessage(responseMessage);
				if (error != null && error.length() > 0)
					LOGGER.debug("Received error message is " + error);

				LOGGER.debug("Session termination failed.");

			}

		} else if (responseMessage == null) {
			LOGGER.debug("Session termination failed !!");
		} else {
			LOGGER.debug("Unexpected message ...");
		}

		return result;

	}

	private static MediaComponentDescription createMediaComponentDescription(
			long flowNumber, String ownerIPAddress, long ownerPort,
			String otherPartyAddress, long otherPartyPort, long downloadBW,
			long uploadBW, int mediaType) {

		MediaComponentDescription mcd = new MediaComponentDescription(
				flowNumber);
		// media
		MediaSubComponent msc = new MediaSubComponent();
		int mediaSubComponentFlowNumber = 1;
		msc.setFlowNumber(new FlowNumber(mediaSubComponentFlowNumber));
		msc.setFlowUsage(new FlowUsage(FlowUsage.NO_INFORMATION));

		// TODO : Check if the bandwidth in byte kilo Byte, or what is the unit
		// This code only supports upload or download not both
		if (downloadBW > 0) {
			msc.setMaxRequestedBandwidthDL(new MaxRequestedBandwidthDL(
					downloadBW));

			msc.setFlowDescriptionIn(new FlowDescription(getIpFilter(
					otherPartyAddress, otherPartyPort, ownerIPAddress,
					ownerPort, false)));

		} else if (uploadBW > 0) {
			// msc.setFlowDescriptionIn(new FlowDescription(getIpFilter(
			// otherPartyAddress, otherPartyPort, ownerIPAddress,
			// ownerPort, true)));
			msc.setFlowDescriptionIn(new FlowDescription(getIpFilter(
					ownerIPAddress, ownerPort, otherPartyAddress,
					otherPartyPort, true)));
		}

		// Set flow status
		int flowStatus = FlowStatus.DISABLED;
		if (downloadBW > 0 && uploadBW > 0)
			flowStatus = FlowStatus.ENABLED;
		else if (downloadBW > 0)
			flowStatus = FlowStatus.ENABLED_DOWNLINK;
		else if (uploadBW > 0)
			flowStatus = FlowStatus.ENABLED_UPLINK;

		msc.setFlowStatus(new FlowStatus(flowStatus));
		// TODO: you may need to set it on sub component level
		mcd.setMediaType(new MediaType(mediaType));
		mcd.setReservationPriority(new ReservationPriority(
				ReservationPriority.DEFAULT));

		mcd.addMediaSubComponent(msc);
		return mcd;
	}

	/*
	 * IPFilterRule is for example:
	 * "permit in ip from 192.168.2.4 32847 to 145.12.34.62 19257" The ports
	 * could be missing, and the "ip" should be a number matching a protocol as
	 * specified by IANA , ip means any protocol
	 * 
	 * @param flowdesc - the string with the flow description
	 * 
	 * @returns the five-tuple (in pointers to the flow-description string)
	 * /*protocol http://www.iana.org/assignments/protocol-numbers
	 */

	/*
	 * Returns a string of type [permit|deny] [in|out] [<protocol>|ip] from
	 * [<ipA>|0.0.0.0|::] [<protocolA> ]to [<ipB>|0.0.0.0|::][ <protocolB>]
	 * ----------> Check the source of this rule allocated into pkg
	 * 
	 * @param t - the five tuple to build from
	 */

	public static String getIpFilter(String fromIP, long fromPort, String toIP,
			long toPort, boolean in) {

		LOGGER.debug("getIpFilter medthod called.");

		int udpProtocolNumber = 17;
		String IPv4_ANY = "0.0.0.0";
		StringBuffer sbf = new StringBuffer();

		sbf.append("permit ");

		if (in)
			sbf.append("in ");
		else
			sbf.append("out ");

		sbf.append(udpProtocolNumber + " from ");

		if (fromIP != null && !fromIP.isEmpty())
			sbf.append(fromIP);
		else
			sbf.append(IPv4_ANY);

		sbf.append(" ");

		if (fromPort > 0)
			sbf.append(fromPort + " ");

		sbf.append("to ");
		if (toIP != null && !toIP.isEmpty())
			sbf.append(toIP);
		else
			sbf.append(IPv4_ANY);

		sbf.append(" ");

		if (toPort > 0)
			sbf.append(toPort + " ");

		LOGGER.debug("The generated IPFilter is " + sbf.toString());
		return sbf.toString();

		// return new
		// String("permit out udp from 192.168.1.41 to 192.168.3.129 23022");
	}

	public static ResultCode getResultCode(DiameterMessage message) {

		if (message == null)
			return null;

		LOGGER.debug("getResultCode medthod called.");

		AVP avp = null;
		ResultCode resultCode = null;

		if (!message.flagRequest) {
			avp = message.findAVP(ResultCode.AVP_CODE);
			if (avp != null) {
				resultCode = new ResultCode(avp.getIntData());
				LOGGER.debug("ResultCode = " + resultCode.getUnsigned32());
			} else
				LOGGER.debug("Can not find result code AVP !!");

		}
		return resultCode;
	}

	public static ExperimentalResultCode getExperimentalResultCode(
			DiameterMessage message) {

		if (message == null)
			return null;

		LOGGER.debug("getExperimentalResultCode medthod called");

		AVP avp = null;
		ExperimentalResultCode resultCode = null;

		if (!message.flagRequest) {
			avp = message.findAVP(ExperimentalResultCode.AVP_CODE);
			if (avp != null) {
				resultCode = new ExperimentalResultCode(avp.getIntData());
				LOGGER.debug("Experimenta Result Code = "
						+ resultCode.getUnsigned32());
			} else
				LOGGER.debug("Can not find ExperimentalResultCode AVP !!");

		}
		return resultCode;

	}

	public static IPCANType getIPCANType(DiameterMessage message) {

		if (message == null)
			return null;

		LOGGER.debug("getIPCANType medthod called");

		AVP avp = null;
		IPCANType type = null;

		if (!message.flagRequest) {
			avp = message.findAVP(IPCANType.AVP_CODE);
			if (avp != null) {
				type = new IPCANType(avp.getIntData());
				LOGGER.debug("IP-CAN Type  = " + type.getEnumerated());
			} else
				LOGGER.debug("Can not find IPCANType AVP !!");

		}
		return type;

	}

	public static RATType getRATType(DiameterMessage message) {

		if (message == null)
			return null;

		LOGGER.debug("getRATType medthod called");

		AVP avp = null;
		RATType type = null;

		if (!message.flagRequest) {
			avp = message.findAVP(RATType.AVP_CODE);
			if (avp != null) {
				type = new RATType(avp.getIntData());
				LOGGER.debug("IP-CAN Type  = " + type.getEnumerated());
			} else
				LOGGER.debug("Can not find RATType AVP !!");

		}
		return type;

	}

	public static String getErrorMessage(DiameterMessage message) {

		if (message == null)
			return "";

		LOGGER.debug("getErrorMessage medthod called");

		AVP avp = null;
		String error = "";
		if (!message.flagRequest) {
			avp = message.findAVP(ErrorMessage.AVP_CODE);
			if (avp != null) {
				error = new String(avp.getData());
				LOGGER.debug("error message is : " + error);
			} else {
				LOGGER.debug("Can not find error message AVP !!");
			}

		}
		return error;

	}

	public static AcceptableServiceInfo getAcceptableServiceInfo(
			DiameterMessage message) {

		if (message == null)
			return null;

		LOGGER.debug("getAcceptableServiceInfo medthod called");

		AVP avp = null;
		AcceptableServiceInfo acceptableService = null;

		if (!message.flagRequest) {
			avp = message.findAVP(AcceptableServiceInfo.AVP_CODE);
			if (avp != null) {
				acceptableService = new AcceptableServiceInfo();
				AVP avpChild;
				for (int i = 0; i < avp.getChildCount(); i++) {
					avpChild = avp.getChildAVP(i);

					if (avpChild.code == MaxRequestedBandwidthDL.AVP_CODE) {
						MaxRequestedBandwidthDL dl = new MaxRequestedBandwidthDL();
						dl.setData(avpChild.getIntData());
						acceptableService.setMaxRequestedBandwidthDL(dl);
					} else if (avpChild.code == MaxRequestedBandwidthUL.AVP_CODE) {
						MaxRequestedBandwidthUL ul = new MaxRequestedBandwidthUL();
						ul.setData(avpChild.getIntData());
						acceptableService.setMaxRequestedBandwidthUL(ul);

					} else if (avpChild.code == MediaComponentDescription.AVP_CODE) {
						MediaComponentDescription mcd = new MediaComponentDescription();

						AVP avpChild1;
						for (int j = 0; j < avpChild.getChildCount(); j++) {
							avpChild1 = avp.getChildAVP(j);

							if (avpChild1.code == MediaComponentNumber.AVP_CODE) {
								mcd.setMediaComponentNumber(new MediaComponentNumber(
										avpChild1.getIntData()));

							} else if (avpChild1.code == AFApplicationIdentifier.AVP_CODE) {
								mcd.setAFApplicationIdentifier(new AFApplicationIdentifier(
										new String(avpChild1.getData())));

							} else if (avpChild1.code == MediaType.AVP_CODE) {

								mcd.setMediaType(new MediaType(avpChild1
										.getIntData()));

							} else if (avpChild1.code == MaxRequestedBandwidthDL.AVP_CODE) {
								mcd.setMaxRequestedBandwidthDL(new MaxRequestedBandwidthDL(
										avpChild1.getIntData()));
							} else if (avpChild1.code == MaxRequestedBandwidthUL.AVP_CODE) {
								mcd.setMaxRequestedBandwidthUL(new MaxRequestedBandwidthUL(
										avpChild1.getIntData()));
							} else if (avpChild1.code == FlowStatus.AVP_CODE) {
								mcd.setFlowStatus(new FlowStatus(avpChild1
										.getIntData()));
							} else if (avpChild1.code == ReservationPriority.AVP_CODE) {
								mcd.setReservationPriority(new ReservationPriority(
										avpChild1.getIntData()));
							} else if (avpChild1.code == RSBandwidth.AVP_CODE) {
								mcd.setRSBandwidth(new RSBandwidth(avpChild1
										.getIntData()));
							} else if (avpChild1.code == RRBandwidth.AVP_CODE) {
								mcd.setRRBandwidth(new RRBandwidth(avpChild1
										.getIntData()));
							}
							// TODO :implement CodecData
							// else if (avpChild1.code == CodecData.AVP_CODE) {
							// mcd.setCodecData(new
							// CodecData(avpChild1.getIntData()));
							// }
							else if (avpChild1.code == MediaSubComponent.AVP_CODE) {
								MediaSubComponent msc = new MediaSubComponent();
								AVP avpChild2;
								for (int x = 0; x < avpChild1.getChildCount(); x++) {
									avpChild2 = avp.getChildAVP(x);

									if (avpChild1.code == FlowStatus.AVP_CODE) {
										msc.setFlowStatus(new FlowStatus(
												avpChild2.getIntData()));
									} else if (avpChild2.code == FlowUsage.AVP_CODE) {
										msc.setFlowUsage(new FlowUsage(
												avpChild2.getIntData()));

									} else if (avpChild2.code == FlowNumber.AVP_CODE) {
										msc.setFlowNumber(new FlowNumber(
												avpChild2.getIntData()));
									} else if (avpChild2.code == MaxRequestedBandwidthDL.AVP_CODE) {
										msc.setMaxRequestedBandwidthDL(new MaxRequestedBandwidthDL(
												avpChild2.getIntData()));
									} else if (avpChild2.code == MaxRequestedBandwidthUL.AVP_CODE) {
										msc.setMaxRequestedBandwidthUL(new MaxRequestedBandwidthUL(
												avpChild2.getIntData()));
									} else if (avpChild1.code == FlowDescription.AVP_CODE) {
										FlowDescription fd = new FlowDescription(
												new String(avpChild2.getData()));
										if (fd.isDirectionOut())
											msc.setFlowDescriptionOut(fd);
										else if (fd.isDirectionIn())
											msc.setFlowDescriptionIn(fd);
									}

									mcd.addMediaSubComponent(msc);

								}// for x

							}// if MediaSubComponent.AVP_CODE

						}// for j

						acceptableService.addMediaComponentDescription(mcd);
					}

				}// for i
			}// if avp != null

		} // if !message.flagRequest
		else {
			LOGGER.debug("Can not find AcceptableServiceInfo AVP !!");
		}

		return acceptableService;

	}

	public static SpecificAction getSpecificAction(DiameterMessage message) {

		if (message == null)
			return null;

		LOGGER.debug("getSpecificAction medthod called");

		AVP avp = null;
		SpecificAction specificAction = null;
		avp = message.findAVP(SpecificAction.AVP_CODE);
		if (avp != null) {
			specificAction = new SpecificAction(avp.getIntData());
			LOGGER.debug("SpecificAction = " + specificAction.getEnumerated());
		} else
			LOGGER.debug("Can not find SpecificAction AVP !!");

		return specificAction;

	}

	public static AbortCause getAbortCause(DiameterMessage message) {

		if (message == null)
			return null;

		LOGGER.debug("getAbortCause medthod called");

		AVP avp = null;
		AbortCause abortCause = null;
		avp = message.findAVP(AbortCause.AVP_CODE);
		if (avp != null) {
			abortCause = new AbortCause(avp.getIntData());
			LOGGER.debug("AbortCause = " + abortCause.getEnumerated());
		} else
			LOGGER.debug("Can not find AbortCause AVP !!");

		return abortCause;

	}

	public static String getSessionId(DiameterMessage message) {

		if (message == null)
			return null;

		LOGGER.debug("getSessionId medthod called");

		AVP avp = null;
		String sessionId = null;
		avp = message.findAVP(SessionID.AVP_CODE);
		if (avp != null) {
			sessionId = new String(avp.getData());
			LOGGER.debug("sessionId = " + sessionId);
		} else
			LOGGER.debug("Can not find SessionID AVP !!");

		return sessionId;

	}

	public static void sendRAA(String sessionId) {

		LOGGER.debug("sendRAA method called for session id = " + sessionId
				+ "\n");

		// create RAA message
		RxRAAnswer raa = DiameterManager.getInstance().createRxRAAnswer();
		raa.setSessionId(new SessionID(sessionId));

		// Send message
		DiameterManager.getInstance().sendMessage(raa);
	}

}
// class TestEventListener implements EventListener {
//
// DiameterPeer diameterPeer;
// protected static final Logger LOGGER = Logger
// .getLogger(TestEventListener.class);
//
// public void recvMessage(String FQDN, DiameterMessage msg) {
// int i;
// DiameterMessage response;
//
// LOGGER.debug("msg.commandCode = " + msg.commandCode);
// if (msg.flagRequest) {
// if (msg.commandCode == IMessageCode.RA_CODE) {
// AVP avp = msg.findAVP(SpecificAction.AVP_CODE);
// if (avp != null) {
// int a = avp.int_data;
// }
// }
//
// } else {
//
// }
// LOGGER.debug("recvMessage recvMessage recvMessage recvMessage recvMessage recvMessage");
//
// // response = diameterPeer.newResponse(msg);
// // AVP a = new AVP(602,true,0);
// // a.setData("HelloWorld!");
// //
// // for(i=0;i<10;i++){
// // response.addAVP(a);
// // }
// // diameterPeer.sendMessage(FQDN,response);
// }
//
//
//
