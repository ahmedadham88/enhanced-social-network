
package ca.concordia.encs.diameter.base.messages;

import ca.concordia.encs.diameter.base.avp.AuthApplicationId;
import ca.concordia.encs.diameter.base.avp.DestinationHost;
import ca.concordia.encs.diameter.base.avp.DestinationRealm;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;


public class DiameterRequest extends DiameterMessage {
	
	public static final boolean REQUEST_FLAG = true; 
	
	public DiameterRequest(int _CommandCode, int _ApplicationId) {
		super(_CommandCode, true, _ApplicationId);
	}
	
	public DiameterRequest(DiameterMessage _msg){
		super(_msg.commandCode, _msg.flagRequest, _msg.flagProxiable, _msg.applicationID, _msg.hopByHopID, _msg.endToEndID);
		this.avps = _msg.avps;
	}
	
	/**
	 * Searches for the AuthApplicationId AVP inside a message
	 * @return the found AuthApplicationId AVP, null if not found
	 * 
	 * @author cegger
	 */
	public AuthApplicationId getAuthApplicationId() {
		return (AuthApplicationId)findAVP(AuthApplicationId.AVP_CODE);
	}
	/**
	 * sets the AuthApplicationId and overrides a existing one
	 * @param _authApplicationId the new AuthApplicationId
	 * 
	 * @author cegger
	 */
	public void setAuthApplicationId(AuthApplicationId _authApplicationId)	{
		this.setSingleAVP(_authApplicationId);
	}
}
