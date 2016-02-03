
package ca.concordia.encs.diameter.base.avp;

import ca.concordia.encs.diameter.base.avptype.Enumerated;
import ca.concordia.encs.diameter.base.constants.Vendors;


public class ReservationPriority extends Enumerated{

	public static final int AVP_CODE = 458;
	public static final int VENDOR_ID = Vendors.tispan_VENDOR_ID;
	
	public static final int DEFAULT 		= 0;
	public static final int PRIORITY_ONE 	= 1;
	public static final int PRIORITY_TWO 	= 2;
	public static final int PRIORITY_THREE 	= 3;
	public static final int PRIORITY_FOUR 	= 4;
	public static final int PRIORITY_FIVE 	= 5;
	public static final int PRIORITY_SIX 	= 6;
	public static final int PRIORITY_SEVEN 	= 7;
	
	public ReservationPriority() {
		super(AVP_CODE,true,VENDOR_ID);
		init();
	}
	/**
	 * @param enumerated
	 */
	public ReservationPriority(int enumerated) {
		super(AVP_CODE,true,VENDOR_ID);
		setEnumerated(enumerated);
		init();
	}
	
	private void init()	{
		mapping.put(0, "DEFAULT");
		mapping.put(1, "PRIORITY_ONE");
		mapping.put(2, "PRIORITY_TWO");
		mapping.put(3, "PRIORITY_THREE");
		mapping.put(4, "PRIORITY_FOUR");
		mapping.put(5, "PRIORITY_FIVE");
		mapping.put(6, "PRIORITY_SIX");
		mapping.put(7, "PRIORITY_SEVEN");
	}

}
