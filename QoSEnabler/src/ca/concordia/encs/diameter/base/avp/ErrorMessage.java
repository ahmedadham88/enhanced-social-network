
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.UTF8String;
import ca.concordia.encs.diameter.base.constants.Vendors;

public class ErrorMessage extends UTF8String{

	public static final int AVP_CODE = 281;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;
	
	public ErrorMessage() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	/**
	 * @param string
	 */
	public ErrorMessage(String string) {
		super(AVP_CODE,true,VENDOR_ID);
		setOctetString(string);
	}

}
