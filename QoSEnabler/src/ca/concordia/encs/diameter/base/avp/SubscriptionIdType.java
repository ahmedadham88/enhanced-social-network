
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;

public class SubscriptionIdType extends Enumerated{

	public static final int AVP_CODE = 450;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;
	
	/**
	 * The identifier is in international E.164 format (e.g., MSISDN),
	 * according to the ITU-T E.164 numbering plan defined in [E164] and
	 * [CE164].
	 */
	public static final int END_USER_E164 = 0;
	/**
	 * The identifier is in international IMSI format, according to the
	 * ITU-T E.212 numbering plan as defined in [E212] and [CE212].
	 */
  	public static final int END_USER_IMSI = 1;
  	/**
  	 * The identifier is in the form of a SIP URI, as defined in [SIP].
  	 */
  	public static final int END_USER_SIP_URI = 2;
	/**
	 * The identifier is in the form of a Network Access Identifier, as
	 * defined in [NAI].
	 */      
	public static final int END_USER_NAI = 3;
	/**
	 * The Identifier is a credit-control server private identifier.
	 */
  	public static final int END_USER_PRIVATE = 4;
	      
	public SubscriptionIdType() {
		super(AVP_CODE,true,VENDOR_ID);
		init();
	}
	

	/**
	 * @param _subscriptionIdType
	 */
	public SubscriptionIdType(int _subscriptionIdType) {
		super(AVP_CODE,true,VENDOR_ID);
		this.setInteger32(_subscriptionIdType);
		init();
	}

	
	private void init()	{
		mapping.put(0, "END_USER_E164");
		mapping.put(1, "END_USER_IMSI");
		mapping.put(2, "END_USER_SIP_URI");
		mapping.put(3, "END_USER_NAI");
		mapping.put(4, "END_USER_PRIVATE");
		
	}
}
