package ca.concordia.encs.diameter.base.avptype;


import java.io.UnsupportedEncodingException;


public class UTF8String extends OctetString {
	
	public UTF8String() {
		super();
	}
	/**
	 * @param Code
	 * @param Mandatory
	 * @param Vendor_id
	 */
	public UTF8String(int Code, boolean Mandatory, int Vendor_id) {
		super(Code, Mandatory, Vendor_id);
	}

	public void setUTF8String(String _UTF8String) 	{
		try {
			this.setData(_UTF8String.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	public String getUTF8String() 	{
		try {
			return new String(data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.fhg.fokus.diameter.DiameterPeer.data.AVP#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		//sbf.append(commonInfo());
		sbf.append(" UTF8String="+this.getUTF8String());
		return sbf.toString();
	}
}
