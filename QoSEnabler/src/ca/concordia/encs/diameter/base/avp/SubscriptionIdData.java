
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.UTF8String;
import ca.concordia.encs.diameter.base.constants.Vendors;

public class SubscriptionIdData extends UTF8String{

	public static final int AVP_CODE = 444;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;
	
	public SubscriptionIdData() {
		super(AVP_CODE,true,VENDOR_ID);
	}
	
	/**
	 * @param _subscriptionIdData
	 */
	public SubscriptionIdData(String _subscriptionIdData) {
		super(AVP_CODE,true,VENDOR_ID);
		this.setUTF8String(_subscriptionIdData);
	}


}
