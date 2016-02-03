
package ca.concordia.encs.diameter.base.messages;

import ca.concordia.encs.diameter.base.constants.IMessageCode;

public class STRequest extends DiameterRequest {

	public final static int MESSAGE_CODE = IMessageCode.ST_CODE;
	
	public STRequest(int _ApplicationId) {
		super(MESSAGE_CODE, _ApplicationId);
	}
}
