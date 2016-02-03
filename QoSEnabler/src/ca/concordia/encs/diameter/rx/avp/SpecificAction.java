
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;

/**
 * The Specific-Action AVP (AVP code 513) is of type Enumerated.
 * Within a PCRF initiated Re-Authorization Request, the Specific-Action AVP 
 * determines the type of the action.
 * Within an initial AA request the AF may use the Specific-Action AVP to request 
 * specific actions from the server at the bearer events and to limit the contact 
 * to such bearer events where specific action is required. If the Specific-Action 
 * AVP is omitted within the initial AA request, no notification of any of the 
 * events defined below is requested.
 * 
 * TS 29.214
 * 
 *
 */
/**
 * @author root
 * 
 */
/**
 * @author root
 * 
 */
public class SpecificAction extends Enumerated {

	public static final int AVP_CODE = 513;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	/**
	 * Within a RAR, this value shall be used when the server reports the access
	 * network charging identifier to the AF. The
	 * Access-Network-Charging-Identifier AVP shall be included within the
	 * request. In the AAR, this value indicates that the AF requests the server
	 * to provide the access network charging identifier to the AF for each
	 * authorized flow, when the access network charging identifier becomes
	 * known at the PCRF.
	 */
	public static final int CHARGING_CORRELATION_EXCHANGE = 1;

	/**
	 * Within a RAR, this value shall be used when the server reports a loss of
	 * a bearer (e.g. in the case of GPRS PDP context bandwidth modification to
	 * 0 kbit) to the AF. The SDFs that are deactivated as a consequence of this
	 * loss of bearer shall be provided within the Flows AVP. In the AAR, this
	 * value indicates that the AF requests the server to provide a notification
	 * at the loss of a bearer.
	 */
	public static final int INDICATION_OF_LOSS_OF_BEARER = 2;

	/**
	 * Within a RAR, this value shall be used when the server reports a recovery
	 * of a bearer (e.g. in the case of GPRS, PDP context bandwidth modification
	 * from 0 kbit to another value) to the AF. The SDFs that are re-activated
	 * as a consequence of the recovery of bearer shall be provided within the
	 * Flows AVP. In the AAR, this value indicates that the AF requests the
	 * server to provide a notification at the recovery of a bearer.
	 */
	public static final int INDICATION_OF_RECOVERY_OF_BEARER = 3;

	/**
	 * Within a RAR, this value shall be used when the server reports the
	 * release of a bearer (e.g. PDP context removal for GPRS) to the AF. The
	 * SDFs that are deactivated as a consequence of this release of bearer
	 * shall be provided within the Flows AVP. In the AAR, this value indicates
	 * that the AF requests the server to provide a notification at the removal
	 * of a bearer.
	 */
	public static final int INDICATION_OF_RELEASE_OF_BEARER = 4;

	/**
	 * This value shall be used in RAR command by the PCRF to indicate a change
	 * in the IP-CAN type or RAT type (if the IP-CAN type is GPRS). When used in
	 * an AAR command, this value indicates that the AF is requesting
	 * subscription to IP-CAN change and RAT change notification. When used in
	 * RAR it indicates that the PCRF generated the request because of an IP-CAN
	 * or RAT change. IP-CAN-Type AVP and RAT-Type AVP (if the IP-CAN type is
	 * GPRS) shall be provided in the same request with the new/valid value(s).
	 * If an IP-CAN type or RAT type change is due to IP flow mobility and a
	 * subset of the flows within the AF session is affected, the affected
	 * service data flows shall be provided in the same request.
	 */
	public static final int IP_CAN_CHANGE = 6;

	/**
	 * Within a RAR, this value shall be used when the PCRF reports to the AF
	 * that SDFs have run out of credit, and that the termination action
	 * indicated by the corresponding Final-Unit-Action AVP applies (3GPP TS
	 * 32.240 [23] and 3GPP TS 32.299 [24). The SDFs that are impacted as a
	 * consequence of the out of credit condition shall be provided within the
	 * Flows AVP. In the AAR, this value indicates that the AF requests the PCRF
	 * to provide a notification of SDFs for which credit is no longer
	 * available. Applicable to functionality introduced with the Rel8 feature
	 * as described in clause 5.4.1.
	 */
	public static final int INDICATION_OF_OUT_OF_CREDIT = 7;

	/**
	 * Within a RAR, this value shall be used by the PCRF to indicate that the
	 * resources requested for particular service information have been
	 * successfully allocated. The SDFs corresponding to the resources
	 * successfully allocated shall be provided within the Flows AVP. In the
	 * AAR, this value indicates that the AF requests the PCRF to provide a
	 * notification when the resources associated to the corresponding service
	 * information have been allocated. Applicable to functionality introduced
	 * with the Rel8 feature as described in clause 5.4.1. NOTE: This value
	 * applies to applications for which the successful resource allocation
	 * notification is required for their operation since subscription to this
	 * value impacts the resource allocation signalling overhead towards the
	 * PCEF/BBERF.
	 */

	public static final int INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION = 8;

	/**
	 * Within a RAR, this value shall be used by the PCRF to indicate that the
	 * resources requested for a particular service information cannot be
	 * successfully allocated. The SDFs corresponding to the resources that
	 * could not be allocated shall be provided within the Flows AVP. In the
	 * AAR, this value indicates that the AF requests the PCRF to provide a
	 * notification when the resources associated to the corresponding service
	 * information cannot be allocated. Applicable to functionality introduced
	 * with the Rel8 feature as described in clause 5.4.1. NOTE: This value
	 * applies to applications for which the unsuccessful resource allocation
	 * notification is required for their operation since subscription to this
	 * value impacts the resource allocation signalling overhead towards the
	 * PCEF/BBERF.
	 */
	public static final int INDICATION_OF_FAILED_RESOURCES_ALLOCATION = 9;

	/**
	 * Within a RAR, this value shall be used when the server reports the
	 * limited PCC deployment (i.e. dynamically allocated resources are not
	 * applicable) as specified at Annex L in 3GPP TS 23.203 [2] to the AF. In
	 * the AAR, this value indicates that the AF requests the server to provide
	 * a notification for the limited PCC deployment. Applicable to
	 * functionality introduced with the Rel8 feature as described in clause
	 * 5.4.1.
	 */
	public static final int INDICATION_OF_LIMITED_PCC_DEPLOYMENT = 10;

	public SpecificAction() {
		super(AVP_CODE, true, VENDOR_ID);
		init();
	}

	public SpecificAction(int _specificAction) {
		super(AVP_CODE, true, VENDOR_ID);
		setEnumerated(_specificAction);
		init();
	}

	private void init() {

		mapping.put(1, "CHARGING_CORRELATION_EXCHANGE");
		mapping.put(2, "INDICATION_OF_LOSS_OF_BEARER");
		mapping.put(3, "INDICATION_OF_RECOVERY_OF_BEARER");
		mapping.put(4, "INDICATION_OF_RELEASE_OF_BEARER");
		mapping.put(6, "IP_CAN_CHANGE");
		mapping.put(7, "INDICATION_OF_OUT_OF_CREDIT");
		mapping.put(8, "INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION");
		mapping.put(9, "INDICATION_OF_FAILED_RESOURCES_ALLOCATION ");
		mapping.put(10, "INDICATION_OF_LIMITED_PCC_DEPLOYMENT");
		mapping.put(11, "USAGE_REPORT");

	}

}
