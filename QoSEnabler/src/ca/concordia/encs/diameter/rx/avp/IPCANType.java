package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;

/**
 * The IP-CAN-Type AVP (AVP code 1027) is of type Enumerated, and it shall
 * indicate the type of Connectivity Access Network in which the user is
 * connected. The IP-CAN-Type AVP shall always be present during the IP-CAN
 * session establishment. During an IP-CAN session modification, this AVP shall
 * be present when there has been a change in the IP-CAN type and the PCRF
 * requested to be informed of this event. The Event-Trigger AVP with value
 * IP-CAN CHANGE shall be provided together with the IP-CAN-Type AVP.
 * 
 * TS 29.212
 * 
 * 
 */
public class IPCANType extends Enumerated {

	public static final int AVP_CODE = 1027;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	/**
	 * This value shall be used to indicate that the IP-CAN is associated with a
	 * 3GPP access and is further detailed by the RAT-Type AVP.
	 */
	public static final int ThreeGPP = 0;
	/**
	 * This value shall be used to indicate that the IP-CAN is associated with a
	 * DOCSIS access.
	 */
	public static final int DOCSIS = 1;
	/**
	 * This value shall be used to indicate that the IP-CAN is associated with
	 * an xDSL access.
	 */
	public static final int xDSL = 2;
	/**
	 * This value shall be used to indicate that the IP-CAN is associated with a
	 * WiMAX access (IEEE 802.16).
	 */
	public static final int WiMAX = 3;
	/**
	 * This value shall be used to indicate that the IP-CAN is associated with a
	 * 3GPP2 access and is further detailed by the RAT-Type AVP.
	 */
	public static final int ThreeGPP2 = 4;

	public IPCANType() {
		super(AVP_CODE, true, VENDOR_ID);
		init();
	}

	/**
	 * @param enumerated
	 */
	public IPCANType(int enumerated) {
		super(AVP_CODE, true, VENDOR_ID);
		setEnumerated(enumerated);
		init();
	}

	private void init() {
		mapping.put(0, "3GPP");
		mapping.put(1, "DOCSIS");
		mapping.put(2, "xDSL");
		mapping.put(3, "WiMAX");
		mapping.put(4, "3GPP2");
	}
}
