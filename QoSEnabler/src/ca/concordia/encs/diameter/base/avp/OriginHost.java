
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.constants.Vendors;


public class OriginHost extends DiameterIdentity {

	public static final int AVP_CODE = 264;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;

	public OriginHost() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	/**
	 * @param octetString
	 */
	public OriginHost(String octetString) {
		super(AVP_CODE,true,VENDOR_ID);
		this.setOctetString(octetString);
	}

}
