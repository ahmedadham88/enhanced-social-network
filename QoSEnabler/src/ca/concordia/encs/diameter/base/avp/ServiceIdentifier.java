
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.Unsigned32;
import ca.concordia.encs.diameter.base.constants.Vendors;


public class ServiceIdentifier extends Unsigned32 {

	public static final int AVP_CODE = 439;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;
	
	public ServiceIdentifier() {
		super(AVP_CODE,true,VENDOR_ID);
	}
	
	/**
	 * @param _serviceIdentifier
	 */
	public ServiceIdentifier(long _serviceIdentifier) {
		super(AVP_CODE,true,VENDOR_ID);
		this.setUnsigned32(_serviceIdentifier);
		
	}
}
