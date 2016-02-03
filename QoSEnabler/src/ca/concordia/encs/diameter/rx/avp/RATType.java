package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;

/**
 * The RAT-Type AVP (AVP code yyyy) is of type Enumerated and is used to
 * identify the radio access technology that is serving the UE. NOTE1: Values
 * 0-999 are used for generic radio access technologies that can apply to
 * different IP-CAN types and are not IP-CAN specific. NOTE2: Values 1000-1999
 * are used for 3GPP specific radio access technology types. NOTE3: Values
 * 2000-2999 are used for 3GPP2 specific radio access technology types.
 * 
 * TS 29.212
 * 
 * 
 */
public class RATType extends Enumerated {

	// FIXME AVP CODE assignment is not done
	public static final int AVP_CODE = 9996;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	/**
	 * This value shall be used to indicate that the RAT is WLAN.
	 */
	public static final int WLAN = 0;
	/**
	 * This value shall be used to indicate that the RAT is UTRAN. For further
	 * details refer to 3GPP TS 29.060 [18].
	 */
	public static final int UTRAN = 1000;
	/**
	 * This value shall be used to indicate that the RAT is GERAN. For further
	 * details refer to 3GPP TS 29.060 [18].
	 */
	public static final int GERAN = 1001;
	/**
	 * This value shall be used to indicate that the RAT is GAN. For further
	 * details refer to 3GPP TS 29.060 [18].
	 */
	public static final int GAN = 1002;
	/**
	 * This value shall be used to indicate that the RAT is HSPA Evolution. For
	 * further details refer to 3GPP TS 29.060 [18].
	 */
	public static final int HSPA_EVOLUTION = 1003;
	/**
	 * This value shall be used to indicate that the RAT is CDMA2000 1X. For
	 * further details refer to 3GPP2 X.S0011-D [20].
	 */
	public static final int CDMA2000_1X = 2000;
	/**
	 * This value shall be used to indicate that the RAT is HRPD. For further
	 * details refer to 3GPP2 X.S0011-D [20].
	 */
	public static final int HRPD = 2001;
	/**
	 * This value shall be used to indicate that the RAT is UMB. For further
	 * details refer to 3GPP2 X.S0011-D [20].
	 */
	public static final int UMB = 2002;

	public RATType() {
		super(AVP_CODE, true, VENDOR_ID);
		init();
	}

	/**
	 * @param enumerated
	 */
	public RATType(int enumerated) {
		super(AVP_CODE, true, VENDOR_ID);
		setEnumerated(enumerated);
		init();
	}

	private void init() {
		mapping.put(0, "WLAN");
		mapping.put(1, "UTRAN");
		mapping.put(2, "GERAN");
		mapping.put(3, "GAN");
		mapping.put(4, "HSPA_EVOLUTION");
		mapping.put(5, "CDMA2000_1X");
		mapping.put(6, "HRPD");
		mapping.put(7, "UMB");
	}

}
