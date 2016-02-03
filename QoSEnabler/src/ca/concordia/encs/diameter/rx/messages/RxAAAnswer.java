/* 
 * RxAAA.java
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

import ca.concordia.encs.diameter.base.constants.Applications;
import ca.concordia.encs.diameter.base.constants.IMessageCode;
import ca.concordia.encs.diameter.base.messages.AAAnswer;

/**
 * The AAA command, indicated by the Command-Code field set to 265 and the 'R'
 * bit cleared in the Command Flags field, is sent by the PCRF to the AF in
 * response to the AAR command. Message Format: <AA-Answer> ::= < Diameter
 * Header: 265, PXY > < Session-Id > { Auth-Application-Id } { Origin-Host } {
 * Origin-Realm } [ Result-Code ] [ Experimental-Result ] *[
 * Access-Network-Charging-Identifier ] [ Access-Network-Charging-Address ] [
 * Acceptable-Service-Info ] [ IP-CAN-Type ] [RAT-Type ] *[ Class ] [
 * Error-Message ] [ Error-Reporting-Host ] *[ Failed-AVP ] [ Origin-State-Id ]
 * *[ Redirect-Host ] [ Redirect-Host-Usage ] [ Redirect-Max-Cache-Time ] *[
 * Proxy-Info ] *[ AVP ]
 * 
 * TS 29.214
 * 
 * @author Christoph Egger
 * 
 */
public class RxAAAnswer extends AAAnswer {

	public static final int APPLICATION_ID = Applications.RX_APPLICATION_ID;

	public RxAAAnswer() {
		super(IMessageCode.AA_CODE, APPLICATION_ID);
	}

	// /**
	// * adds a AccessNetworkChargingIdentifier to this GxRAR Message
	// * @param _AccessNetworkChargingIdentifier
	// */
	// public void
	// addAccessNetworkChargingIdentifier(AccessNetworkChargingIdentifier
	// _AccessNetworkChargingIdentifier) {
	// this.addAVP(_AccessNetworkChargingIdentifier);
	// }
	// /**
	// * Queries all AccessNetworkChargingIdentifier stored at this GxRAR
	// Message
	// * @return Iterator of all AccessNetworkChargingIdentifier
	// */
	// public Iterator<AccessNetworkChargingIdentifier>
	// getAccessNetworkChargingIdentifierIterator() {
	// return getIterator(new AccessNetworkChargingIdentifier());
	// }
	// /**
	// * Searches for the AccessNetworkChargingAddress AVP inside a message
	// * @return the found AccessNetworkChargingAddress AVP, null if not found
	// *
	// * @author mhappenhofer
	// */
	// public AccessNetworkChargingAddress getAccessNetworkChargingAddress() {
	// return
	// (AccessNetworkChargingAddress)findAVP(AccessNetworkChargingAddress.AVP_CODE);
	// }
	// /**
	// * sets the AccessNetworkChargingAddress and overrides a existing one
	// * @param _AccessNetworkChargingAddress the new
	// AccessNetworkChargingAddress
	// *
	// * @author mhappenhofer
	// */
	// public void setAccessNetworkChargingAddress(AccessNetworkChargingAddress
	// _AccessNetworkChargingAddress) {
	// this.setSingleAVP( _AccessNetworkChargingAddress);
	// }
	// /**
	// * Searches for the AcceptableServiceInfo AVP inside a message
	// * @return the found AcceptableServiceInfo AVP, null if not found
	// *
	// * @author mhappenhofer
	// */
	// public AcceptableServiceInfo getAcceptableServiceInfo() {
	// return (AcceptableServiceInfo)findAVP(AcceptableServiceInfo.AVP_CODE);
	// }
	// /**
	// * sets the AcceptableServiceInfo and overrides a existing one
	// * @param _AcceptableServiceInfo the new AcceptableServiceInfo
	// *
	// * @author mhappenhofer
	// */
	// public void setAcceptableServiceInfo(AcceptableServiceInfo
	// _AcceptableServiceInfo) {
	// this.setSingleAVP( _AcceptableServiceInfo);
	// }
	// /**
	// * Searches for the IPCANType AVP inside a message
	// * @return the found IPCANType AVP, null if not found
	// *
	// * @author mhappenhofer
	// */
	// public IPCANType getIPCANType() {
	// return (IPCANType)findAVP(IPCANType.AVP_CODE);
	// }
	// /**
	// * sets the IPCANType and overrides a existing one
	// * @param _IPCANType the new IPCANType
	// *
	// * @author mhappenhofer
	// */
	// public void setIPCANType(IPCANType _IPCANType) {
	// this.setSingleAVP( _IPCANType);
	// }
	// /**
	// * Searches for the RATType AVP inside a message
	// * @return the found RATType AVP, null if not found
	// *
	// * @author mhappenhofer
	// */
	// public RATType getRATType() {
	// return (RATType)findAVP(RATType.AVP_CODE);
	// }
	// /**
	// * sets the RATType and overrides a existing one
	// * @param _RATType the new RATType
	// *
	// * @author mhappenhofer
	// */
	// public void setRATType(RATType _RATType) {
	// this.setSingleAVP( _RATType);
	// }
}
