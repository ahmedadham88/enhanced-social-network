
package ca.concordia.encs.diameter.base.avptype;

import java.util.Hashtable;


public class Enumerated extends Integer32 {

	protected Hashtable<Integer, String> mapping;

	public Enumerated() {
		mapping = new Hashtable<Integer, String>();
	}

	/**
	 * @param Code
	 * @param Mandatory
	 * @param Vendor_id
	 */
	public Enumerated(int Code, boolean Mandatory, int Vendor_id) {
		super(Code, Mandatory, Vendor_id);
		mapping = new Hashtable<Integer, String>();
	}

	public int getEnumerated() {
		return this.int_data;
	}

	public void setEnumerated(int _Enumerated) {
		this.setData(_Enumerated);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.fhg.fokus.diameter.DiameterPeer.data.AVP#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		// sbf.append(commonInfo());
		if (mapping.containsKey(this.getEnumerated()))
			sbf.append(" Enumerated=" + mapping.get(this.getEnumerated()) + "("
					+ this.getEnumerated() + ")");
		else
			sbf.append(" Enumerated= Unknown (" + this.getEnumerated() + ")");
		return sbf.toString();
	}

	public String getName() {
		String name = null;

		if (mapping.containsKey(this.getEnumerated()))
			name = mapping.get(this.getEnumerated());

		return name;
	}
}
