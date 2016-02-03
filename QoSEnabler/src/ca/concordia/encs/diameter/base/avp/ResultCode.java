
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.Unsigned32;
import ca.concordia.encs.diameter.base.constants.Vendors;


public class ResultCode extends Unsigned32 {

	public static final int AVP_CODE = 268;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;

	public static final int Diameter_SUCCESS = 2001;
	public static final int Diameter_UNABLE_TO_COMPLY = 5012;
	public static final int Diameter_UNKNOWN_SESSION_ID = 5002;

	public ResultCode() {
		super(AVP_CODE, true, VENDOR_ID);
	}

	/**
	 * @param unsigned32
	 */
	public ResultCode(long unsigned32) {
		super(AVP_CODE, true, VENDOR_ID);
		setUnsigned32(unsigned32);
	}

	public boolean isInformational() {
		return this.getUnsigned32() > 999 && this.getUnsigned32() < 2000;
	}

	public boolean isSuccess() {
		return this.getUnsigned32() > 1999 && this.getUnsigned32() < 3000;
	}

	public boolean isProtocolErrors() {
		return this.getUnsigned32() > 2999 && this.getUnsigned32() < 4000;
	}

	public boolean isTransientFailures() {
		return this.getUnsigned32() > 3999 && this.getUnsigned32() < 5000;
	}

	public boolean isPermanentFailure() {
		return this.getUnsigned32() > 4999 && this.getUnsigned32() < 6000;
	}
}
