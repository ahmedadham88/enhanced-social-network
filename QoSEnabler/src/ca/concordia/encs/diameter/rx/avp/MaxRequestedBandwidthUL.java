
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Unsigned32;
import ca.concordia.encs.diameter.base.constants.Vendors;


/**
 * The Max -Bandwidth-UL AVP (AVP code 516) is of type Unsigned32, and it 
 * indicates the maximum requested bandwidth in bits per second for an uplink 
 * IP flow. The bandwidth contains all the overhead coming from the IP-layer 
 * and the layers above, e.g. IP, UDP, RTP and RTP payload.
 * When provided in an AA-Request, it indicates the maximum requested bandwidth. 
 * When provided in an AA-Answer, it indicates the maximum bandwidth acceptable 
 * by PCRF.
 * 
 * TS 29.214
 * 
 *
 */
public class MaxRequestedBandwidthUL extends Unsigned32 {

	public static final int AVP_CODE = 516;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	public MaxRequestedBandwidthUL() {
		super(AVP_CODE,true,VENDOR_ID);
	}
	

	public MaxRequestedBandwidthUL(long _maxRequestedBandwidth) {
		super(AVP_CODE,true,VENDOR_ID);
		setUnsigned32(_maxRequestedBandwidth);
	}

}
