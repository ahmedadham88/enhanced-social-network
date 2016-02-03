package ca.concordia.encs.diameter.base.avptype;

import de.fhg.fokus.diameter.DiameterPeer.data.AVP;


public class OctetString extends AVP {
	
	public OctetString() {
		super();
	}
	
	public OctetString(int Code, boolean Mandatory, int Vendor_id) {
		super(Code, Mandatory, Vendor_id);
	}
	
	public String getOctetString()	{
		return new String(data);
	}
	
	public void setOctetString(String _octetString)	{
		this.setData(_octetString);
	}
	
	/* (non-Javadoc)
	 * @see de.fhg.fokus.diameter.DiameterPeer.data.AVP#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		//sbf.append(commonInfo());
		sbf.append(" OctetString="+this.getOctetString().replace("\n", "\\n"));
		return sbf.toString();
	}
}
