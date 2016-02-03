
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.OctetString;


public class DiameterIdentity extends OctetString {

	/**
	 * 
	 */
	public DiameterIdentity() {
	}

	/**
	 * @param Mandatory
	 * @param Vendor_id
	 */
	public DiameterIdentity(int Code, boolean Mandatory, int Vendor_id) {
		super(Code, Mandatory, Vendor_id);
	}

	/**
	 * @param octetString
	 */
	public DiameterIdentity(String octetString) {
		super();
		this.setOctetString(octetString);
	}
	
	
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		//sbf.append(commonInfo());
		sbf.append(" DiameterIdentity="+this.getOctetString());
		return sbf.toString();
	}
}
