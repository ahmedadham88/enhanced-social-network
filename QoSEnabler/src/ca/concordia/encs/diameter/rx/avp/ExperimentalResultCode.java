package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Unsigned32;
import ca.concordia.encs.diameter.base.constants.Vendors;


public class ExperimentalResultCode extends Unsigned32 {

	public static final int AVP_CODE = 298;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	/**
	 * The PCRF rejects new or modified service information the service
	 * information provided by the AF is invalid or insufficient for the server
	 * to perform the requested action.
	 */
	public static final int INVALID_SERVICE_INFORMATION = 5061;

	/**
	 * The PCRF rejects new or modified service information because the
	 * Flow-Description AVP(s) cannot be handled by the server because
	 * restrictions defined in clause 5.3.7 are not observed.
	 */
	public static final int FILTER_RESTRICTIONS = 5062;

	/**
	 * The PCRF rejects new or modified service information because the
	 * requested service, as described by the service information provided by
	 * the AF, is not consistent with either the related subscription
	 * information, operator defined policy rules and/or the supported features
	 * in the IP-CAN network.
	 */
	public static final int REQUESTED_SERVICE_NOT_AUTHORIZED = 5063;

	/**
	 * The PCRF rejects a new Rx session setup because the new Rx session
	 * relates to an AF session with another related active Rx session, e.g. if
	 * the AF provided the same AF charging identifier for this new Rx session
	 * that is already in use for the other ongoing Rx session.
	 */
	public static final int DUPLICATED_AF_SESSION = 5064;

	/**
	 * The PCRF rejects a new Rx session setup when it fails to associate the
	 * described service IP flows within the session information received from
	 * the AF to an existing IP-CAN session.
	 */
	public static final int IP_CAN_SESSION_NOT_AVAILABLE = 5065;

	/**
	 * The PCRF rejects a new Rx session setup because the session binding
	 * function associated a non-Emergency IMS session to an IP-CAN session
	 * established to an Emergency APN.
	 */
	public static final int UNAUTHORIZED_NON_EMERGENCY_SESSION = 5066;

	/**
	 * The PCRF rejects a new Rx session setup because the PCRF can"t authorize
	 * the sponsored data connectivity based on the sponsored data connectivity
	 * profile or the operator policy (e.g. sponsored data connectivity not
	 * authorised in the roaming case).
	 */
	public static final int UNAUTHORIZED_SPONSORED_DATA_CONNECTIVITY = 5067;

	public ExperimentalResultCode() {
		super(AVP_CODE, true, VENDOR_ID);
	}

	public ExperimentalResultCode(long _experimentalResultCode) {
		super(AVP_CODE, true, VENDOR_ID);
		setUnsigned32(_experimentalResultCode);
	}
}
