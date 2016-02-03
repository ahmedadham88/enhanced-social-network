package ca.concordia.encs.diameter.base.avptype;


import de.fhg.fokus.diameter.DiameterPeer.data.AVP;

public class Integer32 extends AVP {
	
	public Integer32(){
	}

	public Integer32(int Code, boolean Mandatory, int Vendor_id) {
		super(Code, Mandatory, Vendor_id);
	}
	
	public int getInteger32()	{
		return this.getIntData();
	}
	
	public void setInteger32(int _Integer32)	{
		this.setData(_Integer32);
	}
	
	/* (non-Javadoc)
	 * @see de.fhg.fokus.diameter.DiameterPeer.data.AVP#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		//sbf.append(commonInfo());
		sbf.append(" Integer32="+this.getInteger32());
		return sbf.toString();
	}

}
