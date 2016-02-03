package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.Unsigned32;
import ca.concordia.encs.diameter.base.constants.Vendors;

public class AuthorizatinLifeTime extends Unsigned32{

	public static final int AVP_CODE = 291;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;
	
	public AuthorizatinLifeTime() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	/**
	 * @param unsigned32
	 */
	public AuthorizatinLifeTime(long unsigned32) {
		super(AVP_CODE,true,VENDOR_ID);
		setUnsigned32(unsigned32);
	}

}