
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;


/**
 * The Flow-Usage AVP (AVP code 512) is of type Enumerated, and provides information 
 * about the usage of IP Flows.
 * 
 * TS 29.214
 *  
 *
 */
public class FlowUsage extends Enumerated {

	public static final int AVP_CODE = 512;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	/**
	 * This value is used to indicate that no information about the usage of 
	 * the IP flow is being provided.
	 */
	public static final int NO_INFORMATION = 0;
	/**
	 * This value is used to indicate that an IP flow is used to transport RTCP.
	 */
	public static final int RTCP = 1;
	/**
	 * This value is used to indicate that the IP flow is used to transport AF 
	 * Signalling Protocols (e.g. SIP/SDP).
	 */
	public static final int AF_SIGNALLING = 2;
	

	public FlowUsage() {
		super(AVP_CODE,true,VENDOR_ID);
		init();
	}
	
	public FlowUsage(int _flowUsage) {
		super(AVP_CODE,true,VENDOR_ID);
		setEnumerated(_flowUsage);
		init();
	}
	
	private void init()	{
		mapping.put(0,"NO_INFORMATION");
		mapping.put(1,"RTCP");
		mapping.put(2,"AF_SIGNALING");
	}

}
