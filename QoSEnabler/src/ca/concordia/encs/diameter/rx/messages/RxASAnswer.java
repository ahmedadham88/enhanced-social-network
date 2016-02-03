package ca.concordia.encs.diameter.rx.messages;

import ca.concordia.encs.diameter.base.constants.Applications;
import ca.concordia.encs.diameter.base.constants.IMessageCode;
import ca.concordia.encs.diameter.base.messages.AAAnswer;

public class RxASAnswer extends AAAnswer {
	public static final int APPLICATION_ID = Applications.RX_APPLICATION_ID;

	public RxASAnswer() {
		super(IMessageCode.AS_CODE, APPLICATION_ID);
	}
}




	
