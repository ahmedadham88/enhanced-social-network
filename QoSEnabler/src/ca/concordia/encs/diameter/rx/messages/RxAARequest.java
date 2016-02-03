/* 
* RxAAR.java
* Christoph Egger
* $Revision: 1 $
* 
* Copyright (C) 2010 FTW (Telecommunications Research Center Vienna)
* 
*
* This file is part of BIQINI, a free Policy and Charging Control Function
* for session-based services.
*
* BIQINI is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version
*
* For a license to use the BIQINI software under conditions
* other than those described here, or to purchase support for this
* software, please contact FTW by e-mail at the following addresses:
* fpcc@ftw.at ��
*
* BIQINI is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. �See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA �02111-1307 �USA
*/
package ca.concordia.encs.diameter.rx.messages;

import java.util.Iterator;

import ca.concordia.encs.diameter.base.avp.AuthApplicationId;
import ca.concordia.encs.diameter.base.avp.AuthorizatinLifeTime;
import ca.concordia.encs.diameter.base.avp.FramedIPAddress;
import ca.concordia.encs.diameter.base.avp.FramedIPv6Prefix;
import ca.concordia.encs.diameter.base.avp.ReservationPriority;
import ca.concordia.encs.diameter.base.avp.SubscriptionId;
import ca.concordia.encs.diameter.base.constants.Applications;
import ca.concordia.encs.diameter.base.constants.IMessageCode;
import ca.concordia.encs.diameter.base.messages.AARequest;
import ca.concordia.encs.diameter.rx.avp.AFApplicationIdentifier;
import ca.concordia.encs.diameter.rx.avp.MediaComponentDescription;
import ca.concordia.encs.diameter.rx.avp.ServiceInfoStatus;
import ca.concordia.encs.diameter.rx.avp.ServiceURN;
import ca.concordia.encs.diameter.rx.avp.SpecificAction;



/**
 * The AAR command, indicated by the Command-Code field set to 265 and the 'R' bit set in the Command Flags field, is sent by an AF to the PCRF in order to provide it with the Session Information.
 * Message Format:
 * <AA-Request> ::= < Diameter Header: 265, REQ, PXY >
 *				 < Session-Id >
 *				 { Auth-Application-Id }
 *				 { Origin-Host }
 *				 { Origin-Realm }
 *				 { Destination-Realm }
 *				 [ Destination-Host ]
 *				 [ AF-Application-Identifier ]
 *				*[ Media-Component-Description ]
 *				 [Service-Info-Status ]
 *				 [ AF-Charging-Identifier ]
 *				 [ SIP-Forking-Indication ]
 *				*[ Specific-Action ]
 *				*[ Subscription-ID ]
 *				 [ Reservation-Priority ]
 *				 [ Framed-IP-Address ]
 *				 [ Framed-IPv6-Prefix ]
 *				 [ Service-URN ]
 *				 [ Origin-State-Id ]
 *				*[ Proxy-Info ]
 *				*[ Route-Record ]
 *				*[ AVP ]
 *
 * TS 29.214
 *
 * @author Christoph Egger
 *
 */
public class RxAARequest extends AARequest {
	
	public static final int APPLICATION_ID = Applications.RX_APPLICATION_ID;
	
	public RxAARequest(){
		super(IMessageCode.AA_CODE, APPLICATION_ID);
		this.setAuthApplicationId(new AuthApplicationId(RxAARequest.APPLICATION_ID));
	}
	
	/**
	 * Searches for the AFApplicationIdentifier AVP inside a message
	 * @return the found AFApplicationIdentifier AVP, null if not found
	 * 
	 * @author mhappenhofer
	 */
	public AFApplicationIdentifier getAFApplicationIdentifier() {
		return (AFApplicationIdentifier)findAVP(AFApplicationIdentifier.AVP_CODE);
	}
	/**
	 * sets the AFApplicationIdentifier and overrides a existing one
	 * @param _AFApplicationIdentifier	the new AFApplicationIdentifier
	 * 
	 * @author mhappenhofer
	 */
	public void setAFApplicationIdentifier(AFApplicationIdentifier _AFApplicationIdentifier)	{
		this.setSingleAVP( _AFApplicationIdentifier);
	}
	/**
	 * adds a MediaComponentDescription to this RxAAR Message
	 * @param _MediaComponentDescription
	 */
	public void addMediaComponentDescription(MediaComponentDescription _MediaComponentDescription)	{
		this.addAVP(_MediaComponentDescription);
	}
	/**
	 * Queries all MediaComponentDescription stored at this RxAAR Message
	 * @return	Iterator of all MediaComponentDescription
	 */
	public Iterator<MediaComponentDescription> getMediaComponentDescriptionIterator()	{
		return getIterator(new MediaComponentDescription());
	}
	
	/**
	 * Searches for the ServiceInfoStatus AVP inside a message
	 * @return the found ServiceInfoStatus AVP, null if not found
	 * 
	 * @author mhappenhofer
	 */
	public ServiceInfoStatus getServiceInfoStatus() {
		return (ServiceInfoStatus)findAVP(ServiceInfoStatus.AVP_CODE);
	}
	/**
	 * sets the ServiceInfoStatus and overrides a existing one
	 * @param _ServiceInfoStatus	the new ServiceInfoStatus
	 * 
	 * @author mhappenhofer
	 */
	public void setServiceInfoStatus(ServiceInfoStatus _ServiceInfoStatus)	{
		this.setSingleAVP( _ServiceInfoStatus);
	}
//	/**
//	 * Searches for the AFChargingIdentifier AVP inside a message
//	 * @return the found AFChargingIdentifier AVP, null if not found
//	 * 
//	 * @author mhappenhofer
//	 */
//	public AFChargingIdentifier getAFChargingIdentifier() {
//		return (AFChargingIdentifier)findAVP(AFChargingIdentifier.AVP_CODE);
//	}
//	/**
//	 * sets the AFChargingIdentifier and overrides a existing one
//	 * @param _AFChargingIdentifier	the new AFChargingIdentifier
//	 * 
//	 * @author mhappenhofer
//	 */
//	public void setAFChargingIdentifier(AFChargingIdentifier _AFChargingIdentifier)	{
//		this.setSingleAVP( _AFChargingIdentifier);
//	}
//	/**
//	 * Searches for the SIPForkingIndication AVP inside a message
//	 * @return the found SIPForkingIndication AVP, null if not found
//	 * 
//	 * @author mhappenhofer
//	 */
//	public SIPForkingIndication getSIPForkingIndication() {
//		return (SIPForkingIndication)findAVP(SIPForkingIndication.AVP_CODE);
//	}
//	/**
//	 * sets the SIPForkingIndication and overrides a existing one
//	 * @param _SIPForkingIndication	the new SIPForkingIndication
//	 * 
//	 * @author mhappenhofer
//	 */
//	public void setSIPForkingIndication(SIPForkingIndication _SIPForkingIndication)	{
//		this.setSingleAVP( _SIPForkingIndication);
//	}
	
	/**
	 * adds a SpecificAction to this RxAAR Message
	 * @param _SpecificAction
	 */
	public void addSpecificAction(SpecificAction _SpecificAction)	{
		this.addAVP(_SpecificAction);
	}
	/**
	 * Queries all SpecificAction stored at this RxAAR Message
	 * @return	Iterator of all SpecificAction
	 */
	public Iterator<SpecificAction> getSpecificActionIterator()	{
		return getIterator(new SpecificAction());
	}
	/**
	 * adds a SubscriptionId to this RxAAR Message
	 * @param _SubscriptionId
	 */
	public void addSubscriptionId(SubscriptionId _SubscriptionId)	{
		this.addAVP(_SubscriptionId);
	}
	/**
	 * Queries all SubscriptionId stored at this RxAAR Message
	 * @return	Iterator of all SubscriptionId
	 */
	public Iterator<SubscriptionId> getSubscriptionIdIterator()	{
		return getIterator(new SubscriptionId());
	}
	
	
	/**
	 * Searches for the ReservationPriority AVP inside a message
	 * @return the found ReservationPriority AVP, null if not found
	 * 
	 * @author mhappenhofer
	 */
	public ReservationPriority getReservationPriority() {
		return (ReservationPriority)findAVP(ReservationPriority.AVP_CODE);
	}
	/**
	 * sets the ReservationPriority and overrides a existing one
	 * @param _ReservationPriority	the new ReservationPriority
	 * 
	 * @author mhappenhofer
	 */
	public void setReservationPriority(ReservationPriority _ReservationPriority)	{
		this.setSingleAVP( _ReservationPriority);
	}
	/**
	 * Searches for the FramedIPAddress AVP inside a message
	 * @return the found FramedIPAddress AVP, null if not found
	 * 
	 * @author mhappenhofer
	 */
	public FramedIPAddress getFramedIPAddress() {
		return (FramedIPAddress)findAVP(FramedIPAddress.AVP_CODE);
	}
	/**
	 * sets the FramedIPAddress and overrides a existing one
	 * @param _FramedIPAddress	the new FramedIPAddress
	 * 
	 * @author mhappenhofer
	 */
	public void setFramedIPAddress(FramedIPAddress _FramedIPAddress)	{
		this.setSingleAVP( _FramedIPAddress);
	}
	/**
	 * Searches for the FramedIPv6Prefix AVP inside a message
	 * @return the found FramedIPv6Prefix AVP, null if not found
	 * 
	 * @author mhappenhofer
	 */
	public FramedIPv6Prefix getFramedIPv6Prefix() {
		return (FramedIPv6Prefix)findAVP(FramedIPv6Prefix.AVP_CODE);
	}
	/**
	 * sets the FramedIPv6Prefix and overrides a existing one
	 * @param _FramedIPv6Prefix	the new FramedIPv6Prefix
	 * 
	 * @author mhappenhofer
	 */
	public void setFramedIPv6Prefix(FramedIPv6Prefix _FramedIPv6Prefix)	{
		this.setSingleAVP( _FramedIPv6Prefix);
	}
	/**
	 * sets the ServiceURN and overrides a existing one
	 * @param _ServiceURN	the new ServiceURN
	 * 
	 * @author mhappenhofer
	 */
	public void setServiceURN(ServiceURN _ServiceURN)	{
		this.setSingleAVP( _ServiceURN);
	}
	/**
	 * Searches for the ServiceURN AVP inside a message
	 * @return the found ServiceURN AVP, null if not found
	 * 
	 * @author mhappenhofer
	 */
	public ServiceURN getServiceURN() {
		return (ServiceURN)findAVP(ServiceURN.AVP_CODE);
	}
	/**
	 * sets the AuthorizatinLifeTime and overrides a existing one
	 * @param AuthorizatinLifeTime	the new AuthorizatinLifeTime
	 * 
	 * @author mhappenhofer
	 */
	public void setAuthorizatinLifeTime(AuthorizatinLifeTime _AuthorizatinLifeTime)	{
		this.setSingleAVP( _AuthorizatinLifeTime);
	}
	
	/**
	 * Searches for the AuthorizatinLifeTime AVP inside a message
	 * @return the found AuthorizatinLifeTime AVP, null if not found
	 * 
	 * @author mhappenhofer
	 */
	public AuthorizatinLifeTime getAuthorizatinLifeTime() {
		return (AuthorizatinLifeTime)findAVP(AuthorizatinLifeTime.AVP_CODE);
	}
	
	/*
	public Vector<Integer> getCodecs(){
		Vector<Integer> codecs = new Vector<Integer>(4);
		Iterator<MediaComponentDescription> mcdIt = getMediaComponentDescriptionIterator();
		while(mcdIt.hasNext()){
			MediaComponentDescription mcD = mcdIt.next();
			Iterator<CodecData> cdIt = mcD.getCodecDataIterator();
			while(cdIt.hasNext()){
				CodecData cd = cdIt.next();
				codecs.addAll(cd.getCodecs());
			}
		}
		return codecs;
	}*/
}
