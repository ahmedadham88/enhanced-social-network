package ca.concordia.encs.diameter.base.messages;

import ca.concordia.encs.diameter.base.constants.IMessageCode;

public class STAnswer extends DiameterAnswer {

	public final static int MESSAGE_CODE = IMessageCode.ST_CODE;
	
	public STAnswer(int _ApplicationId) {
		super(MESSAGE_CODE, _ApplicationId);
	}
}
