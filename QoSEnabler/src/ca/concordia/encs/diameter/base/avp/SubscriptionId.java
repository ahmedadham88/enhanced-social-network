
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.Grouped;
import ca.concordia.encs.diameter.base.constants.Vendors;

public class SubscriptionId extends Grouped{


	public static final int AVP_CODE = 443;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;
	
	public SubscriptionId() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	/**
	 * @param _subscriptionIdType
	 * @param _subscriptionIdData
	 */
	public SubscriptionId(SubscriptionIdType _SubscriptionIdType, SubscriptionIdData _SubscriptionIdData) {
		super(AVP_CODE,true,VENDOR_ID);
		this.setSubscriptionIdData(_SubscriptionIdData);
		this.setSubscriptionIdType(_SubscriptionIdType);
	}

	public void setSubscriptionIdType(SubscriptionIdType _subscriptionIdType)	{
		this.setSingleAVP(_subscriptionIdType);
	}
	public SubscriptionIdType getSubscriptionIdType()	{
		return (SubscriptionIdType)findChildAVP(SubscriptionIdType.AVP_CODE);
	}
	
	public void setSubscriptionIdData(SubscriptionIdData _subscriptionIdData)	{
		setSingleAVP(_subscriptionIdData);
	}
	public SubscriptionIdData getSubscriptionIdData()	{
		return (SubscriptionIdData)findChildAVP(SubscriptionIdData.AVP_CODE);
	}
}
