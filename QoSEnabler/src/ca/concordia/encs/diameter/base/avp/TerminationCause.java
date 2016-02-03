
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;


public class TerminationCause extends Enumerated {


	public static final int AVP_CODE = 295;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;
	
	/**
	 * The user initiated a disconnect
	 */
	public static final int DIAMETER_LOGOUT = 1;
	/**
	 * This value is used when the user disconnected prior to the receipt
	 * of the authorization answer message.
	 */
    public static final int DIAMETER_SERVICE_NOT_PROVIDED = 2;
	/**
	 * This value indicates that the authorization answer received by the
	 * access device was not processed successfully.
	 */      
	public static final int DIAMETER_BAD_ANSWER = 3;
	/**      
	 * The user was not granted access, or was disconnected, due to 
	 * administrative reasons, such as the receipt of a 
	 * Abort-Session-Request message.
	 */
	public static final int DIAMETER_ADMINISTRATIVE = 4;
	/**
	 * The communication to the user was abruptly disconnected.
	 */
	public static final int DIAMETER_LINK_BROKEN = 5;
	/**
	 * The user's access was terminated since its authorized session time 
	 * has expired.
	 */      
	public static final int DIAMETER_AUTH_EXPIRED = 6;
	/**
	 * The user is receiving services from another access device.
	 */      
	public static final int DIAMETER_USER_MOVED = 7;
	/**
	 * The user's session has timed out, and service has been terminated.
	 */
	public static final int DIAMETER_SESSION_TIMEOUT = 8;
	      

	public TerminationCause() {
		super(AVP_CODE,true,VENDOR_ID);
		init();
	}


	/**
	 * @param enumerated
	 */
	public TerminationCause(int enumerated) {
		super(AVP_CODE,true,VENDOR_ID);
		setEnumerated(enumerated);
		init();
	}
	
	private void init()	{
		mapping.put(1,"DIAMETER_LOGOUT");
		mapping.put(2,"DIAMETER_SERVICE_NOT_PROVIDED");
		mapping.put(3,"DIAMETER_BAD_ANSWER");
		mapping.put(4,"DIAMETER_ADMINISTRATIVE");
		mapping.put(5,"DIAMETER_LINK_BROKEN");
		mapping.put(6,"DIAMETER_AUTH_EXPIRED");
		mapping.put(7,"DIAMETER_USER_MOVED");
		mapping.put(8,"DIAMETER_SESSION_TIMEOUT");
	}
}
