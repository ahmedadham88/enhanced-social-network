
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;

/**
 * The Service-Info-Status AVP (AVP code 527) is of type Enumerated, 
 * and indicates the status of the service information that the AF is 
 * providing to the PCRF. If the Service-Info-Status AVP is not provided 
 * in the AA request, the value FINAL SERVICE INFORMATION shall be assumed.
 * 
 * TS 29.214
 * 
 *
 */
public class ServiceInfoStatus extends Enumerated{

	public static final int AVP_CODE = 527;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;
	/**
	 * This value is used to indicate that the service has been fully 
	 * negotiated between the two ends and service information provided 
	 * is the result of that negotiation.
	 */
	public static final int FINAL_SERVICE_INFORMATION = 0;
	/**
	 * This value is used to indicate that the service information that 
	 * the AF has provided to the PCRF is preliminary and needs to be further 
	 * negotiated between the two ends (e.g. for IMS when the service information 
	 * is sent based on the SDP offer).
	 */
	public static final int PRELIMINARY_SERVICE_INFORMATION = 1;
	
	public ServiceInfoStatus() {
		super(AVP_CODE,true,VENDOR_ID);
		init();
	}

	public ServiceInfoStatus(int _serviceInfoStatus) {
		super(AVP_CODE,true,VENDOR_ID);
		setEnumerated(_serviceInfoStatus);
		init();
	}
	
	private void init()	{
		mapping.put(0, "FINAL_SERVICE_INFORMATION");
		mapping.put(1, "PRELIMINARY_SERVICE_INFORMATION");
	}

}
