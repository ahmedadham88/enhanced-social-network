
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.Unsigned32;
import ca.concordia.encs.diameter.base.constants.Vendors;


public class AuthApplicationId extends Unsigned32{

	public static final int AVP_CODE = 258;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;
	
	public AuthApplicationId() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	/**
	 * @param unsigned32
	 */
	public AuthApplicationId(long unsigned32) {
		super(AVP_CODE,true,VENDOR_ID);
		setUnsigned32(unsigned32);
	}

}
