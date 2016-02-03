package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.constants.Vendors;

public class DestinationRealm extends DiameterIdentity {

	public static final int AVP_CODE = 283;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;

	public DestinationRealm() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	/**
	 * @param octetString
	 */
	public DestinationRealm(String octetString) {
		super(AVP_CODE,true,VENDOR_ID);
		setOctetString(octetString);
	}

}
