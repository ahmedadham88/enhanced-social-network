
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.OctetString;
import ca.concordia.encs.diameter.base.constants.Vendors;


/**
 * The Service-URN AVP (AVP code 525) is of type OctetString, and it indicates 
 * that an AF session is used for emergency traffic.
 * It contains values of the service URN including subservices, as defined 
 * in [21] or registered at IANA. The string "urn:service:" in the beginning of 
 * the URN shall be omitted in the AVP and all subsequent text shall be included. 
 * Examples of valid values of the AVP are "sos", "sos.fire", "sos.police" and 
 * "sos.ambulance".
 * 
 * TS 29.214
 *
 * 

 *
 */
public class ServiceURN extends OctetString {

	public static final int AVP_CODE = 525;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	public ServiceURN() {
		super(AVP_CODE,true,VENDOR_ID);
	}


	public ServiceURN(String _serviceURN) {
		super(AVP_CODE,true,VENDOR_ID);
		setOctetString(_serviceURN);
	}

}
