
package ca.concordia.encs.diameter.rx.avp;

import java.util.Iterator;

import ca.concordia.encs.diameter.base.avptype.Grouped;
import ca.concordia.encs.diameter.base.constants.Vendors;

/**
 * The Flows AVP (AVP code 510) is of type Grouped, and it indicates IP flows 
 * via their flow identifiers.
 * If no Flow-Number AVP(s) are supplied, the Flows AVP refers to all Flows 
 * matching the media component number.
 * AVP Format:
 *	Flows::= < AVP Header: x >
 *	      { Media-Component-Number}
 *	      *[ Flow-Number]
 *
 * TS 29.214
 *
 *
 */
public class Flows extends Grouped {

	public static final int AVP_CODE = 510;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;
	
	public Flows() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	public Flows(long _MediaComponentNumber) {
		super(AVP_CODE,true,VENDOR_ID);
		this.setMediaComponentNumber(new MediaComponentNumber(_MediaComponentNumber));
	}
	
	public Flows(MediaComponentNumber _MediaComponentNumber) {
		super(AVP_CODE,true,VENDOR_ID);
		this.setMediaComponentNumber(_MediaComponentNumber);
	}
	/**
	 * access the MediaComponentNumber
	 * @return
	 */
	public MediaComponentNumber getMediaComponentNumber()	{
		return (MediaComponentNumber)findChildAVP(MediaComponentNumber.AVP_CODE);
	}
	/**
	 * to set the MediaComponenetNumber AVP
	 * 
	 * a potential existing AVP will be deleted
	 * @param _mediaComponentNumber	the MediaComponentNumber AVP to set
	 */
	public void setMediaComponentNumber(MediaComponentNumber _mediaComponentNumber)	{
		this.setSingleAVP(_mediaComponentNumber);
	}
	/**
	 * adds a FlowNumber AVP to the Flow
	 * @param _flowNumber	the AVP
	 */
	public void addFlowNumber(FlowNumber _flowNumber)	{
		this.addChildAVP(_flowNumber);
	}

//	/**
//	 * Queries all FlowNumber stored 
//	 * @return	Iterator of all FlowNumber
//	 */
//	public Iterator<FlowNumber> getFlowNumberIterator()	{
//		return getIterator(new FlowNumber());
//	}
}
