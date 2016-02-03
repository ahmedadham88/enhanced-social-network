package ca.concordia.encs.services;

import org.apache.log4j.Logger;

import sun.security.jca.GetInstance;

import ca.concordia.encs.diameter.base.avp.DestinationHost;
import ca.concordia.encs.diameter.base.avp.DestinationRealm;
import ca.concordia.encs.diameter.base.avp.OriginHost;
import ca.concordia.encs.diameter.base.avp.OriginRealm;
import ca.concordia.encs.diameter.base.avp.ResultCode;
import ca.concordia.encs.diameter.base.avp.SessionID;
import ca.concordia.encs.diameter.base.constants.IMessageCode;
import ca.concordia.encs.diameter.base.messages.DiameterAnswer;
import ca.concordia.encs.diameter.base.messages.DiameterRequest;
import ca.concordia.encs.diameter.rx.avp.SpecificAction;
import ca.concordia.encs.diameter.rx.messages.RxAAAnswer;
import ca.concordia.encs.diameter.rx.messages.RxAARequest;
import ca.concordia.encs.diameter.rx.messages.RxASAnswer;
import ca.concordia.encs.diameter.rx.messages.RxRAAnswer;
import ca.concordia.encs.diameter.rx.messages.RxSTRequest;
import ca.concordia.encs.resources.qos.Session;
import ca.concordia.encs.webservice.resources.SessionsResource;
import de.fhg.fokus.diameter.DiameterPeer.DiameterPeer;
import de.fhg.fokus.diameter.DiameterPeer.EventListener;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;

public class DiameterManager implements EventListener {

	private static final Logger LOGGER = Logger
			.getLogger(DiameterManager.class);

	private static DiameterManager diameterManager = null;
	private DiameterPeer diameterPeer;
	private String localFQDN;
	private String localRealm;
	private String remoteFQDN;
	private String remoteRealm;
	private int hopByHopID;
	private int endToEndID;

	public DiameterManager(String xmlFilename, String remoteFQDN,
			String remoteRealm) {

		diameterPeer = new DiameterPeer();
		diameterPeer.configure(xmlFilename, true);
		diameterPeer.enableTransactions(12, 1);
		diameterPeer.addEventListener(this);

		this.localFQDN = diameterPeer.FQDN;
		this.localRealm = diameterPeer.Realm;
		this.remoteFQDN = remoteFQDN;
		this.remoteRealm = remoteRealm;
		hopByHopID = diameterPeer.getNextHopByHopId();
		endToEndID = diameterPeer.getNextEndToEndId();

		diameterManager = this;

	}

	public void shutdown() {
		diameterPeer.shutdown();
	}

	public static DiameterManager getInstance() {
		return diameterManager;
	}

	public String getDestinationHost() {
		return this.remoteFQDN;
	}

	// public void addEventListener(EventListener listener) {
	// if (listener != null)
	// diameterPeer.addEventListener(listener);
	// }
	//
	// public void removeEventListener(EventListener listener) {
	// if (listener != null)
	// diameterPeer.removeEventListener(listener);
	// }

	public RxAARequest createRxAARequest() {
		RxAARequest aar = new RxAARequest();
		aar.hopByHopID = hopByHopID;
		aar.endToEndID = endToEndID;
		aar.setOriginHost(new OriginHost(localFQDN));
		aar.setOriginRealm(new OriginRealm(localRealm));
		aar.setDestinationHost(new DestinationHost(remoteFQDN));
		aar.setDestinationRealm(new DestinationRealm(remoteRealm));
		return aar;
	}

	public RxSTRequest createRxSTRequest() {
		RxSTRequest str = new RxSTRequest();
		str.hopByHopID = hopByHopID;
		str.endToEndID = endToEndID;
		str.setOriginHost(new OriginHost(localFQDN));
		str.setOriginRealm(new OriginRealm(localRealm));
		str.setDestinationHost(new DestinationHost(remoteFQDN));
		str.setDestinationRealm(new DestinationRealm(remoteRealm));
		return str;
	}

	public RxRAAnswer createRxRAAnswer() {
		RxRAAnswer aaa = new RxRAAnswer();
		aaa.hopByHopID = hopByHopID;
		aaa.endToEndID = endToEndID;
		aaa.setOriginHost(new OriginHost(localFQDN));
		aaa.setOriginRealm(new OriginRealm(localRealm));
		aaa.setDestinationHost(new DestinationHost(remoteFQDN));
		aaa.setDestinationRealm(new DestinationRealm(remoteRealm));
		return aaa;
	}

	public RxASAnswer createRxASAnswer() {
		RxASAnswer asa = new RxASAnswer();
		asa.hopByHopID = hopByHopID;
		asa.endToEndID = endToEndID;
		asa.setOriginHost(new OriginHost(localFQDN));
		asa.setOriginRealm(new OriginRealm(localRealm));
		asa.setDestinationHost(new DestinationHost(remoteFQDN));
		asa.setDestinationRealm(new DestinationRealm(remoteRealm));
		return asa;
	}

	public DiameterMessage sendBlocking(DiameterRequest request) {

		LOGGER.debug("sendBlocking method called.");
		LOGGER.debug("DiameterRequest = " + request.toString());
		DiameterMessage dma = diameterPeer.sendRequestBlocking(this.remoteFQDN,
				request);

		return dma;

	}

	// TODO change parameter to Diameter response
	public void sendMessage(DiameterAnswer response) {

		LOGGER.info("sendMessage method called.");
		LOGGER.debug("DiameterMessage = " + response.toString());
		diameterPeer.sendMessage(this.remoteFQDN, response);

	}

	public static void sendResponse(String sessionId) {

		LOGGER.debug("sendResponse method called \n session id is " + sessionId);

		// create STR message
		RxRAAnswer raa = DiameterManager.getInstance().createRxRAAnswer();
		raa.setSessionId(new SessionID(sessionId));

		// Send message
		DiameterManager.getInstance().sendMessage(raa);

	}

	@Override
	public void recvMessage(String FQDN, DiameterMessage msg) {

		LOGGER.info("DiameterManager recieved a new message : recvMessage method called \n");

		LOGGER.debug("commandCode = " + msg.commandCode + " ,flagRequest = "
				+ msg.flagRequest + "\n");

		processMessage(msg);
	}

	private void processMessage(final DiameterMessage msg) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (msg.flagRequest) {
					if (msg.commandCode == IMessageCode.RA_CODE) {
						LOGGER.debug("Diameter message is  RAR \n");
						QoSManager.getInstance().processRAR(msg);
					}

					else if (msg.commandCode == IMessageCode.AS_CODE) {
						LOGGER.debug("Diameter message is  ASR \n");
						QoSManager.getInstance().processASR(msg);
					}
				} else {

					// It is an answer , do not care
				}
			}
		});
	}
	
	// public int sendBlocking(DiameterRequest request, DiameterAnswer response)
	// {
	//
	// LOGGER.debug("sendBlocking method ...");
	// LOGGER.debug("DiameterRequest = " + request.toString());
	// DiameterMessage dma = diameterPeer.sendRequestBlocking(this.remoteFQDN,
	// request);
	//
	// if (dma == null) {
	// LOGGER.error("The response is null !!");
	// return -1;
	// }
	// System.out.println("answer command code is : " + dma.commandCode);
	// // System.out.println("esponse.getResultCode().getIntData() is : " +
	// // dma.getResultCode().getIntData());
	// ResultCode a = (ResultCode) dma.findAVP(ResultCode.AVP_CODE);
	// if (dma instanceof DiameterAnswer) {
	// DiameterAnswer ret = (DiameterAnswer) dma;
	// response = ret;
	// } else {
	// LOGGER.error("The response is not an  DiameterAnswer !!");
	// return -1;
	// }
	//
	// LOGGER.debug("DiameterAnswer is : " + dma.toString());
	// System.out.println("answer command code is : " + dma.commandCode);
	// // if(!response.getResultCode().isSuccess())
	// // {
	// // // LOGGER.debug(TestName()+ " Did not Receive 2001 as Result code!");
	// // }
	// // response.getFailedAVP();
	// System.out.println("esponse.getResultCode().getIntData() is : "
	// + response.getResultCode().getIntData());
	// return response.getResultCode().getIntData();
	// }

	// protected void addMedia(RxAAR aar, SessionDescription
	// sessionDescription){
	// aar.setServiceInfoStatus(sessionDescription.getServiceInfoStatus());
	// aar.addSubscriptionId(sessionDescription.getSubscriptionId());
	// //aar.setFramedIPAddress(sessionDescription.getLocalIP());
	// aar.setSessionId(sessionDescription.getSessionID());
	//
	// SSDP offer = sessionDescription.getOffer();
	// SSDP answer = sessionDescription.getAnswer();
	//
	// Media audioOffer = offer.audio;
	// Media audioAnswer = answer.audio;
	//
	// Media videoOffer = offer.video;
	// Media videoAnswer = answer.video;
	//
	// String lIP = sessionDescription.getLocalIP().getOctetString();
	// String rIP = sessionDescription.getRemoteIP().getOctetString();
	//
	// if(audioOffer!=null) {
	// aar.addMediaComponentDescription(createMediaComponentDescription(1,
	// audioOffer, audioAnswer, lIP, rIP, MediaType.AUDIO));
	// }
	// if(videoOffer!=null) {
	// aar.addMediaComponentDescription(createMediaComponentDescription(2,
	// videoOffer, videoAnswer, lIP, rIP, MediaType.VIDEO));
	// }
	// }

	// private static MediaComponentDescription
	// createMediaComponentDescription(long _id, Media _offer, Media _answer,
	// String ipOfferer, String ipAnswerer, int _mediaType) {
	// MediaComponentDescription mcd = new MediaComponentDescription(_id);
	// //media
	// MediaSubComponent msc = new MediaSubComponent();
	// msc.setFlowNumber(new FlowNumber(1));
	// msc.setMaxRequestedBandwidthDL(new
	// MaxRequestedBandwidthDL(_offer.bandwidth));
	// if(_answer!=null)
	// msc.setMaxRequestedBandwidthUL(new
	// MaxRequestedBandwidthUL(_answer.bandwidth));
	// msc.setFlowUsage(new FlowUsage(FlowUsage.NO_INFORMATION));
	// int rcvPort =-1;
	// if( _answer!=null)
	// rcvPort=_answer.recvPort;
	// msc.setFlowDescriptionIn(new FlowDescription(ipfilter(ipOfferer,
	// ipAnswerer, rcvPort, true)));
	// rcvPort = _offer.recvPort;
	// msc.setFlowDescriptionOut(new FlowDescription(ipfilter(ipOfferer,
	// ipAnswerer, rcvPort, false)));
	// mcd.addMediaSubComponent(msc);
	//
	// //RTCP
	// MediaSubComponent rtcp = new MediaSubComponent();
	// rtcp.setFlowNumber(new FlowNumber(2));
	// rtcp.setMaxRequestedBandwidthDL(new MaxRequestedBandwidthDL(8000));
	// if(_answer!=null)
	// rtcp.setMaxRequestedBandwidthUL(new MaxRequestedBandwidthUL(8000));
	// rtcp.setFlowUsage(new FlowUsage(FlowUsage.RTCP));
	// rcvPort =-1;
	// if( _answer!=null)
	// rcvPort=_answer.recvPort;
	// rtcp.setFlowDescriptionIn(new FlowDescription(ipfilter(ipOfferer,
	// ipAnswerer, rcvPort+1, true)));
	// rcvPort = _offer.recvPort;
	// rtcp.setFlowDescriptionOut(new FlowDescription(ipfilter(ipOfferer,
	// ipAnswerer, rcvPort+1, false)));
	// mcd.addMediaSubComponent(rtcp);
	// CodecData cdOffer = new CodecData("uplink\noffer\n"+_offer.toString());
	// mcd.addCodecData(cdOffer);
	// if(_answer==null)
	// mcd.setFlowStatus(new FlowStatus(FlowStatus.DISABLED));
	// else {
	// if(_offer.status!=Media.INACTIVE)
	// mcd.setFlowStatus(new FlowStatus(FlowStatus.ENABLED));
	// else
	// mcd.setFlowStatus(new FlowStatus(FlowStatus.DISABLED));
	// CodecData cdAnswer = new
	// CodecData("downlink\nanswer\n"+_answer.toString());
	// mcd.addCodecData(cdAnswer);
	// }
	// mcd.setMediaType(new MediaType(_mediaType));
	// return mcd;
	// }
	//

	// private static String ipfilter(String ipOfferer, String ipAnswerer, int
	// rcvPort, boolean in){
	// StringBuffer sbf = new StringBuffer();
	// sbf.append("permit ");
	// if(in)
	// sbf.append("in");
	// else
	// sbf.append("out");
	// sbf.append(" 17 from ");
	// if(in)
	// sbf.append(ipOfferer);
	// else
	// {
	// if(ipAnswerer!=null)
	// sbf.append(ipAnswerer);
	// else
	// sbf.append("0.0.0.0");
	// }
	// sbf.append(" to ");
	// if(in)
	// {
	// if(ipAnswerer!=null)
	// sbf.append(ipAnswerer);
	// else
	// sbf.append("0.0.0.0");
	// }
	// else
	// sbf.append(ipOfferer);
	// if(rcvPort>0)
	// {
	// sbf.append(" "+rcvPort);
	// }
	// return sbf.toString();
	// }

}