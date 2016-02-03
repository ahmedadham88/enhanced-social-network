package ca.concordia.encs.diameter.base.messages;

import ca.concordia.encs.diameter.base.avp.FailedAVP;
import ca.concordia.encs.diameter.base.avp.ResultCode;
import de.fhg.fokus.diameter.DiameterPeer.data.DiameterMessage;


public class DiameterAnswer extends DiameterMessage{

	public static final boolean REQUEST_FLAG = false; 
	
	public DiameterAnswer(int _CommandCode, int _ApplicationId) {
		super(_CommandCode, false, _ApplicationId);
	}
	
	public DiameterAnswer(DiameterMessage _msg){
		super(_msg.commandCode, _msg.flagRequest, _msg.flagProxiable, _msg.applicationID, _msg.hopByHopID, _msg.endToEndID);
		this.avps = _msg.avps;
	}
	
	/**
	 * Searches for the ResultCode AVP inside a message
	 * @return the found ResultCode AVP, null if not found
	 * 
	 * @author mhappenhofer
	 */
	public ResultCode getResultCode() {
		return (ResultCode)findAVP(ResultCode.AVP_CODE);
	}
	/**
	 * sets the ResultCode and overrides a existing one
	 * @param _resultCode	the new ResultCode
	 * 
	 * @author mhappenhofer
	 */
	public void setResultCode(ResultCode _resultCode)	{
		this.setSingleAVP( _resultCode);
	}
	
	/**
	 * Searches for the FailedAVP AVP inside a message
	 * @return the found FailedAVP AVP, null if not found
	 * 
	 * @author mhappenhofer
	 */
	public FailedAVP getFailedAVP() {
		return (FailedAVP)findAVP(FailedAVP.AVP_CODE);
	}
	/**
	 * sets the FailedAVP and overrides a existing one
	 * @param _failedAVP	the new FailedAVP
	 * 
	 * @author mhappenhofer
	 */
	public void setFailedAVP(FailedAVP _failedAVP)	{
		this.setSingleAVP( _failedAVP);
	}
	
//	/**
//	 * Searches for the ErrorMessage AVP inside a message
//	 * @return the found ErrorMessage AVP, null if not found
//	 * 
//	 * @author mhappenhofer
//	 */
//	public ErrorMessage getErrorMessage() {
//		return (ErrorMessage)findAVP(ErrorMessage.AVP_CODE);
//	}
//	/**
//	 * sets the ErrorMessage and overrides a existing one
//	 * @param _errorMessage	the new ErrorMessage
//	 * 
//	 * @author mhappenhofer
//	 */
//	public void setErrorMessage(ErrorMessage _errorMessage)	{
//		this.setSingleAVP( _errorMessage);
//	}
	
//	/**
//	 * Searches for the ErrorReportingHost AVP inside a message
//	 * @return the found ErrorReportingHost AVP, null if not found
//	 * 
//	 * @author mhappenhofer
//	 */
//	public ErrorReportingHost getErrorReportingHost() {
//		return (ErrorReportingHost)findAVP(ErrorReportingHost.AVP_CODE);
//	}
//	/**
//	 * sets the ErrorReportingHost and overrides a existing one
//	 * @param _errorReportingHost	the new ErrorReportingHost
//	 * 
//	 * @author mhappenhofer
//	 */
//	public void setErrorReportingHost(ErrorReportingHost _errorReportingHost)	{
//		this.setSingleAVP( _errorReportingHost);
//	}
//	
//	/**
//	 * Searches for the AuthApplicationId AVP inside a message
//	 * @return the found AuthApplicationId AVP, null if not found
//	 * 
//	 * @author cegger
//	 */
//	public AuthApplicationId getAuthApplicationId() {
//		return (AuthApplicationId)findAVP(AuthApplicationId.AVP_CODE);
//	}
//	/**
//	 * sets the AuthApplicationId and overrides a existing one
//	 * @param _authApplicationId the new AuthApplicationId
//	 * 
//	 * @author cegger
//	 */
//	public void setAuthApplicationId(AuthApplicationId _authApplicationId)	{
//		this.setSingleAVP(_authApplicationId);
//	}
}
