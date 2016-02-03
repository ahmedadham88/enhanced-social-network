
package ca.concordia.encs.diameter.rx.avp;

import java.util.Iterator;

import ca.concordia.encs.diameter.base.avp.ReservationPriority;
import ca.concordia.encs.diameter.base.avptype.Grouped;
import ca.concordia.encs.diameter.base.constants.Vendors;
import de.fhg.fokus.diameter.DiameterPeer.data.AVP;

/**
 * The Media-Component-Description AVP (AVP code 517) is of type Grouped, and it
 * contains service information for a single media component within an AF
 * session or the AF signalling information. The service information may be
 * based on the SDI exchanged between the AF and the AF session client in the
 * UE. The information may be used by the PCRF to determine authorized QoS and
 * IP flow classifiers for bearer authorization and PCC rule selection. Within
 * one Diameter message, a single IP flow shall not be described by more than
 * one Media-Component-Description AVP. Bandwidth information and Flow-Status
 * information provided within the Media-Component-Description AVP applies to
 * all those IP flows within the media component, for which no corresponding
 * information is being provided within Media-Sub-Component AVP(s). If a
 * Media-Component-Description AVP is not supplied by the AF, or if optional
 * AVP(s) within a Media-Component-Description AVP are omitted, but
 * corresponding information has been provided in previous Diameter messages,
 * the previous information for the corresponding IP flow(s) remains valid. All
 * IP flows within a Media-Component-Description AVP are permanently disabled by
 * supplying a Flow Status AVP with value "REMOVED". The server may delete
 * corresponding filters and state information. Reservation-Priority provided
 * within the Media-Component-Description AVP in the request from the AF applies
 * to all those IP flows within the media component and describes the relative
 * importance of the IP flow as compared to other IP flows. The PCRF may use
 * this value to implement priority based admission. If the Reservation-Priority
 * AVP is not specified the IP flow priority is DEFAULT (0). Each
 * Media-Component-Description AVP shall contain either zero, or one, or two
 * Codec-Data AVPs. In the case of conflicts, information contained in other
 * AVPs either within this Media-Component-Description AVP, or within the
 * corresponding Media-Component-Description AVP in a previous message, shall
 * take precedence over information within the Codec-Data AVP(s). The AF shall
 * provision all the available information in other applicable AVPs in addition
 * to the information in the Codec-Data AVP, if such other AVPs are specified.
 * If the SDP offer-answer procedures of IETF RFC 3264 [18] are applicable for
 * the session negotiation between the two ends taking part in the communication
 * (e.g. for IMS), the following applies: - The AF shall provision information
 * derived from an SDP answer and shall also provision information derived from
 * the corresponding SDP offer. - If the Media-Component-Description AVP
 * contains two Codec-Data AVPs, one of them shall represent an SDP offer and
 * the other one the corresponding SDP answer. - If the
 * Media-Component-Description AVP contains one Codec-Data AVP, and this AVP
 * represents an SDP offer, the AF shall provision the corresponding SDP answer
 * information in a Codec-Data AVP within a subsequent Rx message. NOTE: Some
 * SDP parameters for the same codec in the SDP offer and answer are independent
 * of each other and refer to IP flows in opposite directions, for instance some
 * MIME parameters conveyed within "a=fmtp" SDP lines and the packetization time
 * within the "a=ptime" line. Other parameters within the SDP answer take
 * precedence over corresponding parameters within the SDP offer. If SDP is
 * applied without using the offer-answer procedures, zero or one Codec-Data AVP
 * shall be provisioned. The PCRF may provide the Media-Component-Description
 * AVP(s) within the Acceptable-Service-Info AVP in the AA-Answer command if the
 * service information received from the AF is rejected. For this usage, the
 * Media-Component-Description AVP shall only include the appropriate
 * Media-Component-Number AVP and the Max-Requested-Bandwidth-UL and/or
 * Max-Requested-Bandwidth-DL AVPs indicating the maximum acceptable bandwidth.
 * AVP format: Media-Component-Description ::= < AVP Header: 517 > {
 * Media-Component-Number } ; Ordinal number of the media comp. *[
 * Media-Sub-Component ] ; Set of flows for one flow identifier [
 * AF-Application-Identifier ] [ Media-Type ] [ Max-Requested-Bandwidth-UL ] [
 * Max-Requested-Bandwidth-DL ] [ Flow-Status ] [ Reservation-priority ] [
 * RS-Bandwidth ] [ RR-Bandwidth ] *[ Codec-Data ]
 * 
 * TS 29.214
 * 
 * 
 */
public class MediaComponentDescription extends Grouped {

	public static final int AVP_CODE = 517;
	public static final int VENDOR_ID = Vendors.threeGPP_VENDOR_ID;

	public MediaComponentDescription() {
		super(AVP_CODE, true, VENDOR_ID);
	}

	/**
	 * @param Mandatory
	 * @param Vendor_id
	 */
	public MediaComponentDescription(long _MediaComponentNumber) {
		super(AVP_CODE, true, VENDOR_ID);
		this.setMediaComponentNumber(new MediaComponentNumber(
				_MediaComponentNumber));
		this.setReservationPriority(new ReservationPriority(
				ReservationPriority.DEFAULT));

	}

	/**
	 * configures the mediaComponent Number all previous configurations will be
	 * deleted
	 * 
	 * @param _mediaComponentNumber
	 */
	public void setMediaComponentNumber(
			MediaComponentNumber _mediaComponentNumber) {
		this.setSingleAVP(_mediaComponentNumber);
	}

	/**
	 * Queries the current configured MediaComponentNumber
	 * 
	 * @return
	 */
	public MediaComponentNumber getMediaComponentNumber() {
		return (MediaComponentNumber) findChildAVP(MediaComponentNumber.AVP_CODE);
	}

	/**
	 * adds a MediaSubcomponent automatically updates some impilict values
	 * 
	 * @param _mediaSubComponent
	 */
	public void addMediaSubComponent(MediaSubComponent _mediaSubComponent) {
		this.addChildAVP(_mediaSubComponent);

		// update some parameters
		int maxReqUL = 0;
		int maxReqDL = 0;
		// FlowStatus
		for (Iterator<MediaSubComponent> it = this
				.getMediaSubComponentIterator(); it.hasNext();) {
			// update Requested bandwidths
			MediaSubComponent curr = it.next();
			MaxRequestedBandwidthUL maxRequestedBandwidthUL = curr
					.getMaxRequestedBandwidthUL();
			MaxRequestedBandwidthDL maxRequestedBandwidthDL = curr
					.getMaxRequestedBandwidthDL();
			if (maxRequestedBandwidthDL != null)
				maxReqDL = maxReqDL
						+ (int) maxRequestedBandwidthDL.getUnsigned32();
			if (maxRequestedBandwidthUL != null)
				maxReqUL = maxReqUL
						+ (int) maxRequestedBandwidthUL.getUnsigned32();
			// check FlowStatus
			FlowStatus currentMediaComponent = this.getFlowStatus();
			FlowStatus currentMediaSubComponent = curr.getFlowStatus();
			if (currentMediaComponent == null
					&& currentMediaSubComponent != null) {
				this.setFlowStatus(new FlowStatus(currentMediaSubComponent
						.getEnumerated()));
			}
			if (currentMediaComponent != null
					&& currentMediaSubComponent != null) {
				if (currentMediaSubComponent.getEnumerated() != currentMediaComponent
						.getEnumerated()) {
					System.err
							.println("Incompatible Flow Status. Will change the FlowStatus of MediaSubcomponent!");
					currentMediaSubComponent
							.setEnumerated(currentMediaComponent
									.getEnumerated());
				}
			}
			// TODO set the RSBandwidth and RRBandwidth
		}
		MaxRequestedBandwidthUL _mRUL = new MaxRequestedBandwidthUL(maxReqUL);
		MaxRequestedBandwidthDL _mRDL = new MaxRequestedBandwidthDL(maxReqDL);
		this.setMaxRequestedBandwidthDL(_mRDL);
		this.setMaxRequestedBandwidthUL(_mRUL);
	}

	/**
	 * Queries all MediaSubComponent stored
	 * 
	 * @return Iterator of all MediaSubComponent
	 */
	public Iterator<MediaSubComponent> getMediaSubComponentIterator() {
		return getIterator(new MediaSubComponent());
	}

	/**
	 * configures the AFApplicationIdentifier all previous configurations will
	 * be deleted
	 * 
	 * @param _afApplicationIdentifier
	 */
	public void setAFApplicationIdentifier(
			AFApplicationIdentifier _afApplicationIdentifier) {
		this.setSingleAVP(_afApplicationIdentifier);
	}

	/**
	 * Queries the current configured AFApplicationIdentifier
	 * 
	 * @return
	 */
	public AFApplicationIdentifier getAFApplicationIdentifier() {
		return (AFApplicationIdentifier) findChildAVP(AFApplicationIdentifier.AVP_CODE);
	}

	/**
	 * configures the MediaType all previous configurations will be deleted
	 * 
	 * @param _mediaType
	 */
	public void setMediaType(MediaType _mediaType) {
		this.setSingleAVP(_mediaType);
	}

	/**
	 * Queries the current configured MediaType
	 * 
	 * @return
	 */
	public MediaType getMediaType() {
		return (MediaType) findChildAVP(MediaType.AVP_CODE);
	}

	/**
	 * sets the maximal Requested Bandwidth in the download direction if this
	 * bandwidth was already configured, it will be deleted before
	 * 
	 * @param _maxRequestedBandwidthUL
	 */
	public void setMaxRequestedBandwidthDL(
			MaxRequestedBandwidthDL _maxRequestedBandwidthDL) {
		this.setSingleAVP(_maxRequestedBandwidthDL);
	}

	/**
	 * queries the maximal Requested Bandwidth in the download direction
	 * 
	 * @return
	 */
	public MaxRequestedBandwidthDL getMaxRequestedBandwidthDL() {
		return (MaxRequestedBandwidthDL) findChildAVP(MaxRequestedBandwidthDL.AVP_CODE);
	}

	/**
	 * sets the maximal Requested Bandwidth in the upload direction if this
	 * bandwidth was already configured, it will be deleted before
	 * 
	 * @param _maxRequestedBandwidthUL
	 */
	public void setMaxRequestedBandwidthUL(
			MaxRequestedBandwidthUL _maxRequestedBandwidthUL) {
		this.setSingleAVP(_maxRequestedBandwidthUL);
	}

	/**
	 * queries the maximal Requested Bandwidth in the upload direction
	 * 
	 * @return
	 */
	public MaxRequestedBandwidthUL getMaxRequestedBandwidthUL() {
		return (MaxRequestedBandwidthUL) findChildAVP(MaxRequestedBandwidthUL.AVP_CODE);
	}

	/**
	 * configures the flow Status of this flow all previous configurations will
	 * be dropped
	 * 
	 * @param _flowStatus
	 */
	public void setFlowStatus(FlowStatus _flowStatus) {
		this.setSingleAVP(_flowStatus);
	}

	/**
	 * queries the Flow Status of this flow
	 * 
	 * @return
	 */
	public FlowStatus getFlowStatus() {
		return (FlowStatus) findChildAVP(FlowStatus.AVP_CODE);
	}

	/**
	 * configures the ReservationPriority of this flow all previous
	 * configurations will be dropped
	 * 
	 * @param _reservationPriority
	 */
	public void setReservationPriority(ReservationPriority _reservationPriority) {
		this.setSingleAVP(_reservationPriority);
	}

	/**
	 * queries the ReservationPriority of this flow
	 * 
	 * @return
	 */
	public ReservationPriority getReservationPriority() {
		return (ReservationPriority) findChildAVP(ReservationPriority.AVP_CODE);
	}

	/**
	 * configures the RSBandwidth of this flow all previous configurations will
	 * be dropped
	 * 
	 * @param _rsBandwidth
	 */
	public void setRSBandwidth(RSBandwidth _rsBandwidth) {
		this.setSingleAVP(_rsBandwidth);
	}

	/**
	 * queries the RSBandwidth of this flow
	 * 
	 * @return
	 */
	public RSBandwidth getRSBandwidth() {
		return (RSBandwidth) findChildAVP(RSBandwidth.AVP_CODE);
	}

	/**
	 * configures the RRBandwidth of this flow all previous configurations will
	 * be dropped
	 * 
	 * @param _rrBandwidth
	 */
	public void setRRBandwidth(RRBandwidth _rrBandwidth) {
		this.setSingleAVP(_rrBandwidth);
	}

	/**
	 * queries the RRBandwidth of this flow
	 * 
	 * @return
	 */
	public RRBandwidth getRRBandwidth() {
		return (RRBandwidth) findChildAVP(RRBandwidth.AVP_CODE);
	}

	

	// /**
	// * adds a CodecData
	// * @param _codecData
	// */
	// public void addCodecData(CodecData _codecData) {
	// this.addChildAVP(_codecData);
	// }
	// /**
	// * Queries all CodecData stored
	// * @return Iterator of all CodecData
	// */
	// public Iterator<CodecData> getCodecDataIterator() {
	// return getIterator(new CodecData());
	// }
	/*
	 * public void updateMediaComponentDescription(MediaComponentDescription
	 * newMCD) throws Exception {
	 * if(this.getMediaComponentNumber().getUnsigned32
	 * ()!=newMCD.getMediaComponentNumber().getUnsigned32()) throw new
	 * Exception("MediaComponentNumber mismatch!");
	 * if(this.getMediaType().getEnumerated
	 * ()!=newMCD.getMediaType().getEnumerated()) throw new
	 * Exception("MediaType mismatch!");
	 * 
	 * //update missing AVPs MaxRequestedBandwidthDL maxRequestedBandwidthDL =
	 * newMCD.getMaxRequestedBandwidthDL(); if(maxRequestedBandwidthDL!=null)
	 * this.setMaxRequestedBandwidthDL(maxRequestedBandwidthDL);
	 * 
	 * MaxRequestedBandwidthUL maxRequestedBandwidthUL =
	 * newMCD.getMaxRequestedBandwidthUL(); if(maxRequestedBandwidthUL!=null)
	 * this.setMaxRequestedBandwidthUL(maxRequestedBandwidthUL);
	 * 
	 * RSBandwidth rsBandwidth = newMCD.getRSBandwidth(); if(rsBandwidth!=null)
	 * this.setRSBandwidth(rsBandwidth);
	 * 
	 * RRBandwidth rrBandwidth = newMCD.getRRBandwidth(); if(rrBandwidth!=null)
	 * this.setRRBandwidth(rrBandwidth);
	 * 
	 * ReservationPriority reservationPriority =
	 * newMCD.getReservationPriority(); if(reservationPriority != null)
	 * this.setReservationPriority(reservationPriority);
	 * 
	 * 
	 * }
	 */
}
