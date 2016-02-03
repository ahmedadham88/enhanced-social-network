package ca.concordia.encs.diameter.rx.messages;

import ca.concordia.encs.diameter.base.avp.AuthApplicationId;
import ca.concordia.encs.diameter.base.avp.TerminationCause;
import ca.concordia.encs.diameter.base.constants.Applications;
import ca.concordia.encs.diameter.base.constants.IMessageCode;
import ca.concordia.encs.diameter.base.messages.AARequest;

public class RxASRequest extends AARequest {

	public static final int APPLICATION_ID = Applications.RX_APPLICATION_ID;

	public RxASRequest() {
		super(IMessageCode.AS_CODE, APPLICATION_ID);
		this.setAuthApplicationId(new AuthApplicationId(
				RxASRequest.APPLICATION_ID));
	}

	/**
	 * Searches for the TerminationCause AVP inside a message
	 * 
	 * @return the found TerminationCause AVP, null if not found
	 * 
	 * @author mhappenhofer
	 */
	public TerminationCause getTerminationCause() {
		return (TerminationCause) findAVP(TerminationCause.AVP_CODE);
	}

	/**
	 * sets the TerminationCause and overrides a existing one
	 * 
	 * @param _TerminationCause
	 *            the new TerminationCause
	 * 
	 * @author mhappenhofer
	 */
	public void setTerminationCause(TerminationCause _TerminationCause) {
		this.setSingleAVP(_TerminationCause);
	}

}
