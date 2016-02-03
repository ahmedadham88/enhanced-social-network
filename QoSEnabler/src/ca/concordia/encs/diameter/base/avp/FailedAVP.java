package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.Grouped;
import ca.concordia.encs.diameter.base.constants.Vendors;

public class FailedAVP extends Grouped {

	public static final int AVP_CODE = 279;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;
	
	public FailedAVP() {
		super(AVP_CODE,true,VENDOR_ID );
	}

}
