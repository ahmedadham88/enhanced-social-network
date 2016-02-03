
package ca.concordia.encs.diameter.rx.avp;

import ca.concordia.encs.diameter.base.avptype.Grouped;
import ca.concordia.encs.diameter.base.constants.Vendors;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;


/**
 * The Media-Sub-Component AVP (AVP code 519) is of type Grouped, and it 
 * contains the requested bitrate and filters for the set of IP flows 
 * identified by their common Flow-Identifier. The Flow-Identifier is 
 * defined in Annex B.
 * Possible Bandwidth information and Flow-Status information provided 
 * within the Media-Sub-Component AVP takes precedence over information 
 * within the encapsulating Media Component Description AVP. If a 
 * Media-Sub-Component- AVP is not supplied, or if optional AVP(s) within 
 * a Media-Sub-Component AVP are omitted, but corresponding information 
 * has been provided in previous Diameter messages, the previous 
 * information for the corresponding IP flow(s) remains valid, unless new 
 * information is provided within the encapsulating Media 
 * Component-Description AVP. If Flow-Description AVP(s) are supplied, 
 * they replace all previous Flow Description AVP(s), even if a new 
 * Flow-Description AVP has the opposite direction as the previous Flow 
 * Description AVP.
 * All IP flows within a Media-Sub-Component- AVP are permanently disabled 
 * by supplying a Flow Status AVP with value "REMOVED". The server may 
 * delete corresponding filters and state information.
 * AVP format:
 * Media-Sub-Component ::= < AVP Header: 519 >
 *						{ Flow-Number }      		   ; Ordinal number of the IP flow
 *						0*2[ Flow-Description ]        ; UL and/or DL
 *						   [ Flow-Status ]
 *						   [ Flow-Usage ]
 *						   [ Max-Requested-Bandwidth-UL ]
 *						   [ Max-Requested-Bandwidth-DL ]
 *						  *[ AVP ]
 *
 * TS 29.214
 *
 *
 */
public class MediaSubComponent extends Grouped {

	public static final int AVP_CODE = 519;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;
	
	public MediaSubComponent() {
		super(AVP_CODE,true,VENDOR_ID);
	}

	public MediaSubComponent(int _flowNumber, int _flowStatus, 
			int _flowUsage,int _upload, int _download, 
			FlowDescription _inFlow, FlowDescription _outFlow)	{
		super(AVP_CODE,true,VENDOR_ID);
		setFlowNumber(new FlowNumber(_flowNumber));
		setFlowStatus(new FlowStatus(_flowStatus));
		setFlowUsage(new FlowUsage(_flowUsage));
		setMaxRequestedBandwidthDL(new MaxRequestedBandwidthDL(_download));
		setMaxRequestedBandwidthUL(new MaxRequestedBandwidthUL(_upload));
		setFlowDescriptionIn(_inFlow);
		setFlowDescriptionOut(_outFlow);
	}
	
	/**
	 * configures the flow Status of this flow
	 * all previous configurations will be dropped
	 * @param _flowUsage
	 */
	public void setFlowStatus(FlowStatus _flowStatus)	{
		this.setSingleAVP(_flowStatus);
	}
	/**
	 * queries the Flow Status of this flow
	 * @return
	 */
	public FlowStatus getFlowStatus()	{
		return (FlowStatus)findChildAVP(FlowStatus.AVP_CODE);
	}
	
	/**
	 * configures the flow Usage of this flow
	 * all previous configurations will be dropped
	 * @param _flowUsage
	 */
	public void setFlowUsage(FlowUsage _flowUsage)	{
		this.setSingleAVP(_flowUsage);
	}
	/**
	 * queries the Flow Usage of this flow
	 * @return
	 */
	public FlowUsage getFlowUsage()	{
		return (FlowUsage)findChildAVP(FlowUsage.AVP_CODE);
	}
	
	/**
	 * sets the flow number
	 * if the flow number was configured, it will be deleted before
	 * @param _flowNumber
	 */
	public void setFlowNumber(FlowNumber _flowNumber)	{
		this.setSingleAVP(_flowNumber);
	}
	/**
	 * queries the flow number
	 * @return
	 */
	public FlowNumber getFlowNumber()	{
		return (FlowNumber)findChildAVP(FlowNumber.AVP_CODE);
	}
	
	/**
	 * sets the maximal Requested Bandwidth in the download direction
 	 * if this bandwidth was already configured, it will be deleted before
	 * @param _maxRequestedBandwidthUL
	 */
	public void setMaxRequestedBandwidthDL(MaxRequestedBandwidthDL _maxRequestedBandwidthDL)	{
		this.setSingleAVP(_maxRequestedBandwidthDL);
	}
	/**
	 * queries the maximal Requested Bandwidth in the download direction
	 * @return
	 */
	public MaxRequestedBandwidthDL getMaxRequestedBandwidthDL()	{
		return (MaxRequestedBandwidthDL)findChildAVP(MaxRequestedBandwidthDL.AVP_CODE);
	}
	
	/**
	 * sets the maximal Requested Bandwidth in the upload direction
	 * if this bandwidth was already configured, it will be deleted before
	 * @param _maxRequestedBandwidthUL
	 */
	public void setMaxRequestedBandwidthUL(MaxRequestedBandwidthUL _maxRequestedBandwidthUL)	{
		this.setSingleAVP(_maxRequestedBandwidthUL);
	}
	/**
	 * queries the maximal Requested Bandwidth in the upload direction
	 * @return
	 */
	public MaxRequestedBandwidthUL getMaxRequestedBandwidthUL()	{
		return (MaxRequestedBandwidthUL)findChildAVP(MaxRequestedBandwidthUL.AVP_CODE);
	}
	
	/**
	 * sets the Flowdescription for the In Flow
	 * if this description already exists, it will be deleted before
	 * @param _flowDescriptionOut
	 */
	public void setFlowDescriptionIn(FlowDescription _flowDescriptionIn){
		//special treatment, becuase of in and out
		FlowDescription current = getFlowDescriptionIn();
		if(current!=null)
			this.deleteChildAVP(current);
		this.addChildAVP(_flowDescriptionIn);
	}
	/**
	 * queries the Flowdescription for the In flow
	 * @return	the flow description or null if none exists
	 */
	public FlowDescription getFlowDescriptionIn()	{
		//special treatment, becuase of in and out
		for(int i = 0;i<this.getChildCount();i++)
		{
			AVP curr = this.getChildAVP(i);
			if (curr instanceof FlowDescription) {
				FlowDescription type = (FlowDescription) curr;
				if(type.isDirectionIn())
					return type;
			}
		}
		return null;
	}
	
	/**
	 * sets the Flowdescription for the Out Flow
	 * if this description already exists, it will be deleted before
	 * @param _flowDescriptionOut
	 */
	public void setFlowDescriptionOut(FlowDescription _flowDescriptionOut){
		//special treatment, becuase of in and out
		FlowDescription current = getFlowDescriptionOut();
		if(current!=null)
			this.deleteChildAVP(current);
		this.addChildAVP(_flowDescriptionOut);		
	}
	/**
	 * queries the Flowdescription for the Out flow
	 * @return	the flow description or null if none exists
	 */
	public FlowDescription getFlowDescriptionOut()	{
		//special treatment, becuase of in and out
		for(int i = 0;i<this.getChildCount();i++)
		{
			AVP curr = this.getChildAVP(i);
			if (curr instanceof FlowDescription) {
				FlowDescription type = (FlowDescription) curr;
				if(type.isDirectionOut())
					return type;
			}
		}
		return null;
	}
	
}
