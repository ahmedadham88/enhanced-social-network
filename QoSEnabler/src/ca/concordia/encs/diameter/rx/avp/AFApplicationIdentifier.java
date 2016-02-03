
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.OctetString;
import ca.concordia.encs.diameter.base.constants.Vendors;

/**
 * The AF-Application-identifier AVP (AVP code 504) is of type OctetString, and 
 * it contains information that identifies the particular service that the AF 
 * service session belongs to. This information may be used by the PCRF to 
 * differentiate QoS for different application services.
 * For example the AF-Application-Identifier may be used as additional 
 * information together with the Media-Type AVP when the QoS class for the bearer 
 * authorization at the Gx interface is selected. The AF-Application-Identifier 
 * may be used also to complete the QoS authorization with application specific 
 * default settings in the PCRF if the AF does not provide full 
 * Session-Component-Description information.
 * 
 * TS 29.214
 *
 *
 */
public class AFApplicationIdentifier extends OctetString {

	public static final int AVP_CODE = 504;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	public AFApplicationIdentifier() {
		super(AVP_CODE,true,VENDOR_ID);
	}
	
	public AFApplicationIdentifier(String _afApplicationIdentifier) {
		super(AVP_CODE,true,VENDOR_ID);
		setOctetString(_afApplicationIdentifier);
	}


}
