
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;

/**
 * The Abort-Cause AVP (AVP code 500) is of type Enumerated, and determines 
 * the cause of an abort session request (ASR) or of a RAR indicating a bearer 
 * release. 
 * 
 * TS 29.214
 * 
 *
 */
public class AbortCause extends Enumerated {

	public static final int AVP_CODE = 500;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;
	
	/**
	 * This value is used when the bearer has been deactivated as a result 
	 * from normal signalling handling. For GPRS the bearer refers to the PDP 
	 * Context.
	 */
	public static final int BEARER_RELEASED = 0;
	/**
	 * This value is used to indicate that the server is overloaded and needs 
	 * to abort the session.
	 */
	public static final int INSUFFICIENT_SERVER_RESOURCES = 1;
	/**
	 * This value is used when the bearer has been deactivated due to 
	 * insufficient bearer resources at a transport gateway (e.g. GGSN for GPRS).
	 */
	public static final int INSUFFICIENT_BEARER_RESOURCES = 2;
	


	public AbortCause() {
		super(AVP_CODE,true,VENDOR_ID);
		init();
	}
	/**
	 * 
	 * @param _abortCause
	 */
	public AbortCause(int _abortCause) {
		super(AVP_CODE,true,VENDOR_ID);
		setEnumerated(_abortCause);
		init();
	}
	
	private void init()	{
		mapping.put(0, "BEARER_RELEASED");
		mapping.put(1, "INSUFFICIENT_SERVER_RESOURCES");
		mapping.put(2, "INSUFFICIENT_BEARER_RESOURCES");
	}

}
