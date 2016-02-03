package ca.concordia.encs.diameter.base.avptype;


import de.fhg.fokus.diameter.DiameterPeer.data.AVP;

public abstract class Unsigned32 extends AVP {
	
	public Unsigned32() {
		super();
	}
	
	public Unsigned32(int _Code, boolean _Mandatory, int _VendorId) {
		super(_Code, _Mandatory, _VendorId);
	}
	

	public void setUnsigned32(long _Unsigned32)	{
		data = new byte[4];
		data[0] = (byte)((_Unsigned32>>24) &0xFF);
		data[1] = (byte)((_Unsigned32>>16) &0xFF);
		data[2] = (byte)((_Unsigned32>> 8) &0xFF);
		data[3] = (byte)((_Unsigned32    ) &0xFF);
		//unsignedData = _Unsigned32;
	}
		
	public long getUnsigned32()	{
		long unsignedData = 0;
		for(int i=0;i<4&&i<data.length;i++)
			unsignedData = (unsignedData<<8)|(0xFF & data[i]);
		return unsignedData;
	}
	
	/* (non-Javadoc)
	 * @see de.fhg.fokus.diameter.DiameterPeer.data.AVP#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		//sbf.append(commonInfo());
		sbf.append(" Unsigned32="+this.getUnsigned32());
		return sbf.toString();
	}

}
