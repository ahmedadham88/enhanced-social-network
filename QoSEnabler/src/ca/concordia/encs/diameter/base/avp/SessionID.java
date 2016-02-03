
package ca.concordia.encs.diameter.base.avp;

import java.util.Calendar;

import ca.concordia.encs.diameter.base.avptype.UTF8String;
import ca.concordia.encs.diameter.base.constants.Vendors;


public class SessionID extends UTF8String {

	public static final int AVP_CODE = 263;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;

	public static int HIGH32 = 0;
	public static int LOW32 = 0;
	
	static{
		Long l = Calendar.getInstance().getTimeInMillis();
		HIGH32 = l.intValue();
	}
	
	public SessionID() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	/**
	 * @param string
	 */
	public SessionID(String string) {
		super(AVP_CODE,true,VENDOR_ID);
		setUTF8String(string);
	}
	
	/**
	 * @param string
	 */
	public SessionID(DiameterIdentity _diameterIdentity,String identifier) {
		super(AVP_CODE,true,VENDOR_ID);
		setOctetString(_diameterIdentity.getOctetString()+";"+String.valueOf(HIGH32)+";"+String.valueOf(LOW32++)+";"+identifier);
	}

}
