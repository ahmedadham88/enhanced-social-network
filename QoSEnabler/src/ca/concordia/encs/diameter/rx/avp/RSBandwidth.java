
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Unsigned32;
import ca.concordia.encs.diameter.base.constants.Vendors;

/**
 * The RS-Bandwidth AVP (AVP code 522) is of type Unsigned32, and it 
 * indicates the maximum required bandwidth in bits per second for RTCP 
 * sender reports within the session component, as specified in RFC 3556 [11].
 * The bandwidth contains all the overhead coming from the IP-layer and the 
 * layers above, i.e. IP, UDP and RTCP.
 * 
 * TS 29.214
 * 
 *
 */
public class RSBandwidth extends Unsigned32 {

	public static final int AVP_CODE = 522;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	public RSBandwidth() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	public RSBandwidth(long _rsBandwidth) {
		super(AVP_CODE,true,VENDOR_ID);
		setUnsigned32(_rsBandwidth);
	}

}
