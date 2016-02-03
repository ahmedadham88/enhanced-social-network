
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Unsigned32;
import ca.concordia.encs.diameter.base.constants.Vendors;


/**
 * The Flow-Number AVP (AVP code 509) is of type Unsigned32, and it contains 
 * the ordinal number of the IP flow(s), assigned according to the rules in 
 * Annex B.
 * 
 * TS 29.214
 * 
 *
 */
public class FlowNumber extends Unsigned32 {

	public static final int AVP_CODE = 509;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;
	
	public FlowNumber() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	public FlowNumber(long _flowNumber) {
		super(AVP_CODE,true,VENDOR_ID);
		setUnsigned32(_flowNumber);
	}
}
