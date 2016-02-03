
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.OctetString;
import ca.concordia.encs.diameter.base.constants.Vendors;

public class FramedIPv6Prefix extends OctetString {

	public static final int AVP_CODE=97;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;
	
	public FramedIPv6Prefix() {
		super(AVP_CODE,true,VENDOR_ID);
	}


	/**
	 * @param _framedIPv6Prefix
	 */
	public FramedIPv6Prefix(String _framedIPv6Prefix) {
		super(AVP_CODE,true,VENDOR_ID);
		this.setOctetString(_framedIPv6Prefix);
	}
}
