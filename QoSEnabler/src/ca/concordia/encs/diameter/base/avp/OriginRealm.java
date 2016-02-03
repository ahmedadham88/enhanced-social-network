
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.constants.Vendors;

public class OriginRealm extends DiameterIdentity {

	public static final int AVP_CODE = 296;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;

	public OriginRealm() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	/**
	 * @param octetString
	 */
	public OriginRealm(String octetString) {
		super(AVP_CODE,true,VENDOR_ID);
		setOctetString(octetString);
	}

}
