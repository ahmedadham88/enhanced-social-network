package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.OctetString;
import ca.concordia.encs.diameter.base.constants.Vendors;

public class FramedIPAddress extends OctetString {

	public static final int AVP_CODE = 8;
	public static final int VENDOR_ID = Vendors.ITEF_VENDOR_ID;

	public FramedIPAddress() {
		super(AVP_CODE, true, VENDOR_ID);
	}

	/**
	 * @param _framedIPAddress
	 */
	public FramedIPAddress(String ip) {
		super(AVP_CODE, true, VENDOR_ID);
		this.setData(getFramedIP(ip));
	}

	private byte[] getFramedIP(String ip) {

		byte[] ipTokens = null;
		if (ip != null && ip.length() > 0) {
			// Split by reg expression
			String[] tokens = ip.split("\\.");
			if (tokens != null && tokens.length == 4) {
				ipTokens = new byte[4];
				ipTokens[0] = (byte) Integer.parseInt(tokens[0]);
				ipTokens[1] = (byte) Integer.parseInt(tokens[1]);
				ipTokens[2] = (byte) Integer.parseInt(tokens[2]);
				ipTokens[3] = (byte) Integer.parseInt(tokens[3]);
			}
		}

		return ipTokens;
	}
}
