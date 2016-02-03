
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Unsigned32;
import ca.concordia.encs.diameter.base.constants.Vendors;

/**
 * The Media-Component-Number AVP (AVP code 518) is of type Unsigned32, 
 * and it contains the ordinal number of the media component, assigned 
 * according to the rules in Annex B.
 * When this AVP refers to AF signalling, this is indicated by using the 
 * value 0 according to the rules in Annex B.
 *
 * TS 29.214
 * 
 *
 */
public class MediaComponentNumber extends Unsigned32 {

	public static final int AVP_CODE = 518;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;
	
	
	
	public MediaComponentNumber() {
		super(AVP_CODE,true,VENDOR_ID);
	}
	
	public MediaComponentNumber(long _mediaComponentNumber) {
		super(AVP_CODE,true,VENDOR_ID);
		setUnsigned32(_mediaComponentNumber);
	}

}
