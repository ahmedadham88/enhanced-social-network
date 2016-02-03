package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.constants.Vendors;



public class DestinationHost extends DiameterIdentity{

	public static final int AVP_CODE = 293;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;

	public DestinationHost() {
		super(AVP_CODE, true, VENDOR_ID);
	}

	/**
	 * @param octetString
	 */
	public DestinationHost(String octetString) {
		super(AVP_CODE,true,VENDOR_ID);
		setOctetString(octetString);
	}

}
