package ca.concordia.encs.diameter.base.avptype;


import de.fhg.fokus.diameter.DiameterPeer.data.AVP;
import de.fhg.fokus.diameter.DiameterPeer.data.AVPDecodeException;


public class Grouped extends AVP {
	
	public Grouped() {
	}
	
	public Grouped(int Code, boolean Mandatory, int Vendor_id) {
		super(Code, Mandatory, Vendor_id);
	}
	
	/* (non-Javadoc)
	 * @see de.fhg.fokus.diameter.DiameterPeer.data.AVP#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		//sbf.append(commonInfo());
		try {
			ungroup();
		} catch (AVPDecodeException e) {
			// error while ungrouping
			e.printStackTrace();
		}
		int size = this.getChildCount();
		sbf.append(" Grouped \n");
		//sbf.append("\t===================================="+AVPFactory.queryAVPName(code)+"_Beginn\n");
		for(int i = 0;i<size;i++)
		{
			String b = this.getChildAVP(i).toString();
			if(b.indexOf("\n")<0)
				b="\t"+b+"\n";
			else	{
				b = "\t"+b.replace("\n", "\n\t");
				b=b.substring(0,b.length()-1);
			}
			sbf.append("\t"+b);
		}
		//sbf.append("\t===================================="+AVPFactory.queryAVPName(code)+"_Ende\n");
		//if(sbf.lastIndexOf("\n")==sbf.length()-1)
		//	sbf.deleteCharAt(sbf.length()-1);
		return sbf.toString();
	}

}
