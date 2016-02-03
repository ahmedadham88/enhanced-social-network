
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;



/**
 * The Flow-Status AVP (AVP code 511) is of type Enumerated, and describes 
 * whether the IP flow(s) are enabled or disabled.
 * 
 * TS 29.214
 *  
 *
 */
public class FlowStatus extends Enumerated {

	public static final int AVP_CODE = 511;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;
	/**
	 * This value shall be used to enable associated uplink IP flow(s) and to 
	 * disable associated downlink IP flow(s). 
	 */
	public static final int ENABLED_UPLINK = 0;
	/**
	 * This value shall be used to enable associated downlink IP flow(s) and to 
	 * disable associated uplink IP flow(s).
	 */
	public static final int ENABLED_DOWNLINK = 1;
	/**
	 * This value shall be used to enable all associated IP flow(s) in both 
	 * directions.
	 */
	public static final int ENABLED = 2;
	/**
	 * This value shall be used to disable all associated IP flow(s) in both 
	 * directions.
	 */
	public static final int DISABLED = 3;
	/**
	 * This value shall be used to remove all associated IP flow(s). The IP 
	 * Filters for the associated IP flow(s) shall be removed. The associated 
	 * IP flows shall not be taken into account when deriving the authorized QoS.
	 */
	public static final int REMOVED = 4;
	

	public FlowStatus() {
		super(AVP_CODE,true,VENDOR_ID);
		init();
	}

	/**
	 * 
	 * @param _flowStatus
	 */
	public FlowStatus(int _flowStatus) {
		super(AVP_CODE,true,VENDOR_ID);
		setEnumerated(_flowStatus);
		init();
	}

	private void init()	{
		mapping.put(0, "ENABLED_UPLINK");
		mapping.put(1, "ENABLED_DOWNLINK");
		mapping.put(2, "ENABLED");
		mapping.put(3, "DISABLED");
		mapping.put(4, "REMOVED");
		
	}
}
