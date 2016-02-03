
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avp.IPFilterRule;
import ca.concordia.encs.diameter.base.constants.Vendors;


/**
 * The Flow-Description AVP (AVP code 507) is of type IPFilterRule, and 
 * defines a packet filter for an IP flow with the following information:
 * -	Direction (in or out).
 * -	Source and destination IP address (possibly masked).
 * -	Protocol.
 * -	Source and destination port (The Source Port may be omitted to 
 * 		indicate that any source port is allowed. For the Rx interface, 
 * 		lists or ranges shall not be used.).
 * NOTE:	For TCP protocol, destination port may also be omitted.
 * The IPFilterRule type shall be used with the following restrictions:
 * -	Only the Action "permit" shall be used.
 * -	No "options" shall be used.
 * -	The invert modifier "!" for addresses shall not be used.
 * -	The keyword "assigned" shall not be used.
 * If any of these restrictions is not observed by the AF, the server shall 
 * send an error response to the AF containing the Experimental-Result-Code 
 * AVP with value FILTER_RESTRICTIONS. 
 * For the Rx interface, the Flow description AVP shall be used to describe 
 * a single IP flow.
 * The direction "in" refers to uplink IP flows, and the direction "out" 
 * refers to downlink IP flows.
 *  
 * TS 29.214
 *
 */
public class FlowDescription extends IPFilterRule {

	public static final int AVP_CODE = 507;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;
	
	public FlowDescription() {
		super(AVP_CODE,true,VENDOR_ID);

	}

	/**
	 * @param octetString
	 */
	public FlowDescription(String octetString) {
		super(AVP_CODE,true,VENDOR_ID);
		setOctetString(octetString);
	}

}
